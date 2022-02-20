package syntheagecco.openehr.sdk.client

import com.nedap.archie.rm.ehr.EhrStatus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.ehrbase.client.openehrclient.OpenEhrClientConfig
import org.ehrbase.client.openehrclient.VersionUid
import syntheagecco.extraction.mapping.MultiListMap

class EhrBaseClient {

    private static final Logger logger = LogManager.getLogger(EhrBaseClient.class)
    private final AuthRestClient client

    EhrBaseClient(String url, String username, String password){
        def config = new OpenEhrClientConfig(new URI(url))
        def provider = new TemplateProviderImp()
        this.client = new AuthRestClient(config, provider)
        client.addAuthorization(username, password)

        //Ensure all operational templates are present at the endpoint
        provider.listTemplateIds().each {id ->
            logger.debug(OperationalTemplateData.findByTemplateId(id).toString())
            client.templateEndpoint().ensureExistence(id)
        }
    }

    UUID createEhr(){
        return client.ehrEndpoint().createEhr()
    }

    Optional<EhrStatus> getEhrStatus(UUID ehrId){
        return client.ehrEndpoint().getEhrStatus(ehrId)
    }

    Optional<EhrStatus> getEhrStatus(String ehrId){
        return getEhrStatus(UUID.fromString(ehrId))
    }

    void updateEhrStatus(UUID ehrId, EhrStatus status){
        client.ehrEndpoint().updateEhrStatus(ehrId, status)
    }

    void updateEhrStatus(String ehrId, EhrStatus status){
        updateEhrStatus(UUID.fromString(ehrId), status)
    }

    void commitComposition(UUID ehrId, CompositionEntity composition){
        logger.debug("[§]Committing ${composition.getClass().getSimpleName()} instance to ehrBase server ...")
        client.compositionEndpoint(ehrId).mergeCompositionEntity(composition)
    }

    void commitComposition(String ehrId, CompositionEntity composition){
        commitComposition(UUID.fromString(ehrId), composition)
    }

    void commitCompositions(UUID ehrId, List<CompositionEntity> compositions){
        compositions.each {composition ->
            commitComposition(ehrId, composition)
        }
    }

    void commitCompositions(String ehrId, List<CompositionEntity> compositions){
        commitCompositions(UUID.fromString(ehrId), compositions)
    }

    UUID createEhrAndCommitCompositions(List<CompositionEntity> compositions, MultiListMap<UUID, VersionUid> map){
        def ehrId = createEhr()
        logger.info("[§]EHR: ${ehrId}")
        compositions.each {composition ->
            commitComposition(ehrId, composition)
            map.putOne(ehrId, composition.getVersionUid())
            logger.info("   Composition: ${composition.getVersionUid().toString()}")
        }
        return ehrId
    }

    public <T> T findComposition(UUID ehrId, UUID versionId, Class<T> compositionClass){
        def optional = client.compositionEndpoint(ehrId).find(versionId, compositionClass)
        return optional.orElse(null)
    }

}
