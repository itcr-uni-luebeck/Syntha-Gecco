package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Base

abstract class BaseResource {

    private String id

    BaseResource(){}

    BaseResource(String id){
        this.id = id
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }
}
