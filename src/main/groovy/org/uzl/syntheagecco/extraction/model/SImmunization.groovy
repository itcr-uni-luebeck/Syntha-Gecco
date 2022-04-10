package org.uzl.syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding

class SImmunization extends BaseResource{

    private Coding vaccineCode
    private Date occurence
    private boolean primarySource

    SImmunization(){}

    SImmunization(String id, Coding vaccineCode, Date occurence, boolean primarySource){
        super(id)
        this.vaccineCode = vaccineCode
        this.occurence = occurence
    }

    Coding getVaccineCode() {
        return vaccineCode
    }

    void setVaccineCode(Coding vaccineCode) {
        this.vaccineCode = vaccineCode
    }

    Date getOccurence() {
        return occurence
    }

    void setOccurence(Date occurence) {
        this.occurence = occurence
    }

    boolean getPrimarySource() {
        return primarySource
    }

    void setPrimarySource(boolean primarySource) {
        this.primarySource = primarySource
    }

}
