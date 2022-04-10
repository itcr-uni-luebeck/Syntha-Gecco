package org.uzl.syntheagecco.extraction.model

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
