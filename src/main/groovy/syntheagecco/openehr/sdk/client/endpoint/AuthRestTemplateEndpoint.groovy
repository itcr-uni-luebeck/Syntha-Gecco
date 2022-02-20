package syntheagecco.openehr.sdk.client.endpoint

import com.fasterxml.jackson.core.type.TypeReference
import org.apache.http.Header
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.xmlbeans.XmlException
import org.apache.xmlbeans.XmlOptions
import org.ehrbase.client.exception.ClientException
import org.ehrbase.client.openehrclient.TemplateEndpoint
import org.ehrbase.client.openehrclient.defaultrestclient.DefaultRestClient
import org.ehrbase.response.ehrscape.TemplateMetaDataDto
import org.ehrbase.response.openehr.TemplatesResponseData
import org.openehr.schemas.v1.OPERATIONALTEMPLATE
import org.openehr.schemas.v1.TemplateDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import syntheagecco.openehr.sdk.client.AuthRestClient

import javax.xml.namespace.QName

import static syntheagecco.openehr.sdk.client.AuthRestClient.OBJECT_MAPPER

class AuthRestTemplateEndpoint implements TemplateEndpoint{

    public static final String DEFINITION_TEMPLATE_ADL_1_4_PATH = "definition/template/adl1.4/";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthRestClient authRestClient

    AuthRestTemplateEndpoint(AuthRestClient authRestClient) {
        this.authRestClient = authRestClient
    }

    @Override
    Optional<OPERATIONALTEMPLATE> findTemplate(String templateId) {
        TemplateDocument templateDocument

        try {
            URI uri = authRestClient
                    .getConfig()
                    .getBaseUri()
                    .resolve(new URIBuilder()
                            .setPath(authRestClient.getConfig().getBaseUri().getPath() + DEFINITION_TEMPLATE_ADL_1_4_PATH + templateId)
                            .build())

            HttpResponse httpResponse = authRestClient.internalGet(uri, ContentType.APPLICATION_XML.getMimeType())

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return Optional.empty()
            }

            templateDocument = TemplateDocument.Factory.parse(httpResponse.getEntity().getContent())
        } catch (IOException | XmlException | URISyntaxException e) {
            throw new ClientException(e.getMessage(), e)
        }

        return Optional.of(templateDocument.getTemplate())
    }

    @Override
    TemplatesResponseData findAllTemplates() {
        try {
            URI uri = authRestClient
                    .getConfig()
                    .getBaseUri()
                    .resolve(new URIBuilder()
                            .setPath(authRestClient.getConfig().getBaseUri().getPath() + DEFINITION_TEMPLATE_ADL_1_4_PATH)
                            .build())

            HttpResponse response = authRestClient.internalGet(uri, ContentType.APPLICATION_JSON.getMimeType())
            List<TemplateMetaDataDto> templateResponseData = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
                    new TypeReference<Object>() {})

            return new TemplatesResponseData(templateResponseData)
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage(), e)
            throw new ClientException(e.getMessage(), e)
        }
    }

    @Override
    void ensureExistence(String templateId) {
        Optional<OPERATIONALTEMPLATE> operationalTemplate = authRestClient.getTemplateProvider().find(templateId)
        if (!operationalTemplate.isPresent()) {
            throw new ClientException(String.format("Unknown Template with Id %s", templateId))
        }
        if (!findTemplate(templateId).isPresent()) {
            upload(operationalTemplate.get())
        }
    }


    /**
     * Upload a template to the remote system.
     *
     * @param operationaltemplate
     * @return The templateId
     * @throws ClientException
     * @throws org.ehrbase.client.exception.WrongStatusCodeException
     */
    String upload(OPERATIONALTEMPLATE operationaltemplate) {
        URI uri = authRestClient.getConfig().getBaseUri().resolve(DEFINITION_TEMPLATE_ADL_1_4_PATH);
        XmlOptions opts = new XmlOptions()
        opts.setSaveSyntheticDocumentElement(new QName("http://schemas.openehr.org/v1", "template"));

        HttpResponse response =
                authRestClient.internalPost(uri, operationaltemplate.xmlText(opts), ContentType.APPLICATION_XML, ContentType.APPLICATION_XML.getMimeType())

        Header etag = response.getFirstHeader(HttpHeaders.ETAG)
        return etag.getValue().replace("\"", "")

    }

}
