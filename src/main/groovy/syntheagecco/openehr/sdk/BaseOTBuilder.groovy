package syntheagecco.openehr.sdk

import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.openehr.sdk.model.OptGeccoCase

abstract class BaseOTBuilder {

    private SyntheaGeccoConfig config

    BaseOTBuilder(){}

    BaseOTBuilder(SyntheaGeccoConfig config){
        this.config = config
    }

    SyntheaGeccoConfig getConfig() {
        return config
    }

    void setConfig(SyntheaGeccoConfig config) {
        this.config = config
    }

    abstract void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase)

}
