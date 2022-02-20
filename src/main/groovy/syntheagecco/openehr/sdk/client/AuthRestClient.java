package syntheagecco.openehr.sdk.client;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.net.HttpHeaders;
import com.nedap.archie.rm.RMObject;
import com.nedap.archie.rm.archetyped.Locatable;
import com.nedap.archie.rm.composition.Composition;
import com.nedap.archie.rm.datastructures.ItemStructure;
import com.nedap.archie.rm.datavalues.DvText;
import com.nedap.archie.rm.directory.Folder;
import com.nedap.archie.rm.ehr.EhrStatus;
import com.nedap.archie.rm.support.identification.HierObjectId;
import com.nedap.archie.rm.support.identification.ObjectRef;
import com.nedap.archie.rm.support.identification.UIDBasedId;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehrbase.client.exception.ClientException;
import org.ehrbase.client.exception.OptimisticLockException;
import org.ehrbase.client.exception.WrongStatusCodeException;
import org.ehrbase.client.flattener.DefaultValuesProvider;
import org.ehrbase.client.openehrclient.*;
import org.ehrbase.client.openehrclient.defaultrestclient.*;
import org.ehrbase.serialisation.jsonencoding.CanonicalJson;
import org.ehrbase.serialisation.mapper.RmObjectJsonDeSerializer;
import org.ehrbase.serialisation.walker.defaultvalues.DefaultValues;
import org.ehrbase.webtemplate.templateprovider.TemplateProvider;
import syntheagecco.openehr.sdk.client.endpoint.AuthRestCompositionEndpoint;
import syntheagecco.openehr.sdk.client.endpoint.AuthRestEhrEndpoint;
import syntheagecco.openehr.sdk.client.endpoint.AuthRestTemplateEndpoint;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class AuthRestClient implements OpenEhrClient{

    private static final Logger logger = LogManager.getLogger(AuthRestClient.class);

    public static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    private final OpenEhrClientConfig config;
    private final TemplateProvider templateProvider;
    private final Executor executor;
    private final AuthRestEhrEndpoint authRestEhrEndpoint;
    private final Map<UUID, DefaultRestDirectoryEndpoint> directoryEndpointMap = new WeakHashMap<>();
    private final DefaultValuesProvider defaultValuesProvider;
    private final Map<String, String> headers;

    public AuthRestClient(OpenEhrClientConfig config, TemplateProvider templateProvider) {
        this(config, templateProvider, null);
    }

    public AuthRestClient(OpenEhrClientConfig config) {
        this(config, null, null);
    }

    public AuthRestClient(
            OpenEhrClientConfig config, TemplateProvider templateProvider, HttpClient httpClient) {
        this.config = config;

        if (templateProvider != null) {
            this.templateProvider = templateProvider;
        } else {
            this.templateProvider = new TemplateProviderImp();
        }

        executor = Executor.newInstance(httpClient);
        authRestEhrEndpoint = new AuthRestEhrEndpoint(this);

        if (config.getDefaultValuesProvider() != null) {
            defaultValuesProvider = config.getDefaultValuesProvider();
        } else {
            defaultValuesProvider = o -> new DefaultValues();
        }

        this.headers = new HashMap<>();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("openEHR", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(EhrStatus.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(HierObjectId.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(Composition.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(Folder.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(UIDBasedId.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(DvText.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(ObjectRef.class, new RmObjectJsonDeSerializer());
        module.addDeserializer(ItemStructure.class, new RmObjectJsonDeSerializer());

        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    public void addAuthorization(String user, String password){
        String credentials = user + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
        addHeader("Authorization", basicAuth);
    }

    public VersionUid httpPost(URI uri, RMObject body) {
        return httpPost(uri, body, this.headers);
    }

    public VersionUid httpPost(URI uri, RMObject body, Map<String, String> headers) {
        String bodyString = new CanonicalJson().marshal(body);
        HttpResponse response =
                internalPost(
                        uri,
                        headers,
                        bodyString,
                        ContentType.APPLICATION_JSON,
                        ContentType.APPLICATION_JSON.getMimeType());
        Header eTag = response.getFirstHeader(HttpHeaders.ETAG);
        return new VersionUid(eTag.getValue().replace("\"", ""));
    }

    public HttpResponse internalPost(
            URI uri,
            String bodyString,
            ContentType contentType,
            String accept) {
        return internalPost(uri, headers, bodyString, contentType, accept);
    }

    public HttpResponse internalPost(
            URI uri,
            Map<String, String> headers,
            String bodyString,
            ContentType contentType,
            String accept) {
        HttpResponse response;
        try {

            Request request =
                    Request.Post(uri)
                            .addHeader(HttpHeaders.ACCEPT, accept)
                            .bodyString(bodyString, contentType);
            if (headers != null) {
                headers.forEach(request::addHeader);
            }
            response = executor.execute(request).returnResponse();
            checkStatus(response, HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT);
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
        return response;
    }

    public VersionUid httpPut(URI uri, Locatable body, VersionUid versionUid) {
        return httpPut(uri, body, versionUid, null);
    }

    public VersionUid httpPut(
            URI uri, Locatable body, VersionUid versionUid, Map<String, String> headers) {
        try {
            Request request =
                    Request.Put(uri)
                            .addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
                            .addHeader(HttpHeaders.IF_MATCH, versionUid.toString())
                            .bodyString(new CanonicalJson().marshal(body), ContentType.APPLICATION_JSON);
            if (headers != null) {
                headers.forEach(request::addHeader);
            }
            HttpResponse response = executor.execute(request).returnResponse();
            checkStatus(
                    response, HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT, HttpStatus.SC_PRECONDITION_FAILED);
            if (HttpStatus.SC_PRECONDITION_FAILED == response.getStatusLine().getStatusCode()) {
                throw new OptimisticLockException("Entity outdated");
            }
            Header eTag = response.getFirstHeader(HttpHeaders.ETAG);
            return new VersionUid(eTag.getValue().replace("\"", ""));
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
    }

    public <T> Optional<T> httpGet(URI uri, Class<T> valueType) {
        return httpGet(uri, valueType, null);
    }

    public <T> Optional<T> httpGet(URI uri, Class<T> valueType, Map<String, String> headers) {
        HttpResponse response = internalGet(uri, headers, ContentType.APPLICATION_JSON.getMimeType());

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
            return Optional.empty();
        }
        try {
            String value = EntityUtils.toString(response.getEntity());
            return Optional.of(OBJECT_MAPPER.readValue(value, valueType));
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
    }

    public HttpResponse internalGet(URI uri, String accept) {
        return internalGet(uri, headers, accept);
    }

    public HttpResponse internalGet(URI uri, Map<String, String> headers, String accept) {
        HttpResponse response;
        try {
            Request request = Request.Get(uri).addHeader(HttpHeaders.ACCEPT, accept);
            if (headers != null) {
                headers.forEach(request::addHeader);
            }

            response = executor.execute(request).returnResponse();
            checkStatus(response, HttpStatus.SC_OK, HttpStatus.SC_NOT_FOUND);

        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
        return response;
    }

    /**
     * used with the admin endpoint for now
     *
     * @param uri
     * @param headers
     * @return
     */
    public HttpResponse internalDelete(URI uri, Map<String, String> headers) {
        HttpResponse response;
        try {
            Request request = Request.Delete(uri);
            if (headers != null) {
                headers.forEach(request::addHeader);
            }

            response = executor.execute(request).returnResponse();
            checkStatus(
                    response,
                    HttpStatus.SC_OK,
                    HttpStatus.SC_NO_CONTENT,
                    HttpStatus.SC_ACCEPTED,
                    HttpStatus.SC_NOT_FOUND);

        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
        return response;
    }

    public void checkStatus(HttpResponse httpResponse, int... expected) {
        if (!ArrayUtils.contains(expected, httpResponse.getStatusLine().getStatusCode())) {
            String message =
                    Optional.of(httpResponse)
                            .map(HttpResponse::getEntity)
                            .map(
                                    e -> {
                                        try {
                                            return EntityUtils.toString(e);
                                        } catch (IOException ex) {
                                            return null;
                                        }
                                    })
                            .orElse("");

            throw new WrongStatusCodeException(
                    message, httpResponse.getStatusLine().getStatusCode(), expected);
        }
    }

    public OpenEhrClientConfig getConfig() {
        return config;
    }

    public DefaultValuesProvider getDefaultValuesProvider() {
        return defaultValuesProvider;
    }

    public Executor getExecutor() {
        return executor;
    }

    @Override
    public AuthRestEhrEndpoint ehrEndpoint() {
        return authRestEhrEndpoint;
    }

    public TemplateProvider getTemplateProvider() {
        return templateProvider;
    }

    @Override
    public CompositionEndpoint compositionEndpoint(UUID ehrId) {
        return new AuthRestCompositionEndpoint(this, ehrId);
    }

    @Override
    public FolderDAO folder(UUID ehrId, String path) {
        return null;
    }

    @Override
    public TemplateEndpoint templateEndpoint() {
        return new AuthRestTemplateEndpoint(this);
    }

    @Override
    public AqlEndpoint aqlEndpoint() {
        return null;
    }

    @Override
    public AdminEhrEndpoint adminEhrEndpoint() {
        return null;
    }
}
