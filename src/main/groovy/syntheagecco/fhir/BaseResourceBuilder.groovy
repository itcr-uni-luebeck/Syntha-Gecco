package syntheagecco.fhir

import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.fhir.model.FhirGeccoCase

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
