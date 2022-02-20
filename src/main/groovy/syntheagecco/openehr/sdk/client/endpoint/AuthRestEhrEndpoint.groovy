package syntheagecco.openehr.sdk.client.endpoint

import com.nedap.archie.rm.ehr.EhrStatus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.openehrclient.EhrEndpoint
import org.ehrbase.client.openehrclient.VersionUid
import syntheagecco.openehr.sdk.client.AuthRestClient

class AuthRestEhrEndpoint implements EhrEndpoint{

    private final static Logger logger = LogManager.getLogger(AuthRestEhrEndpoint.class)

    static final String EHR_PATH = "ehr/"
    static final String EHR_STATUS_PATH = "/ehr_status"
    private final AuthRestClient authRestClient

    AuthRestEhrEndpoint(AuthRestClient authRestClient) {
        this.authRestClient = authRestClient
    }

    @Override
    UUID createEhr() {
        return authRestClient.httpPost(authRestClient.getConfig().getBaseUri().resolve(EHR_PATH), null).getUuid()
    }

    @Override
    UUID createEhr(EhrStatus ehrStatus) {
        return authRestClient.httpPost(authRestClient.getConfig().getBaseUri().resolve(EHR_PATH), ehrStatus).getUuid()
    }


    @Override
    Optional<EhrStatus> getEhrStatus(UUID ehrId) {
        return authRestClient.httpGet(authRestClient.getConfig().getBaseUri().resolve(EHR_PATH + ehrId.toString() + EHR_STATUS_PATH), EhrStatus.class)
    }

    @Override
    void updateEhrStatus(UUID ehrId, EhrStatus ehrStatus) {
        authRestClient.httpPut(authRestClient.getConfig().getBaseUri().resolve(EHR_PATH + ehrId + EHR_STATUS_PATH), ehrStatus, new VersionUid(ehrStatus.getUid().getValue()))
    }

}
