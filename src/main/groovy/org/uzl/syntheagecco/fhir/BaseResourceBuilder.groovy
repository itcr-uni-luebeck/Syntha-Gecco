package org.uzl.syntheagecco.fhir

import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.fhir.model.FhirGeccoCase

abstract class BaseResourceBuilder {

    private SyntheaGeccoConfig config

    BaseResourceBuilder(){}

    BaseResourceBuilder(SyntheaGeccoConfig config){
        this.config = config
    }

    SyntheaGeccoConfig getConfig() {
        return config
    }

    void setConfig(SyntheaGeccoConfig config) {
        this.config = config
    }

    abstract void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase)

}
