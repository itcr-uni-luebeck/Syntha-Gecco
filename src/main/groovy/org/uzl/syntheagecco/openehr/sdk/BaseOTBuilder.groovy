package org.uzl.syntheagecco.openehr.sdk

import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase

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
