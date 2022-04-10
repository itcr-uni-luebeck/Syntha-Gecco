package org.uzl.syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode

class SOpenEhrProcedure extends SProcedure{

    private NameDerProzedurDefiningCode procedureName

    SOpenEhrProcedure(String id, Coding code, Date start, Date end, NameDerProzedurDefiningCode procedureName){
        super(id, code, start, end)
        this.procedureName = procedureName
    }

    SOpenEhrProcedure(SProcedure procedure, NameDerProzedurDefiningCode procedureName){
        super(procedure.id, procedure.code, procedure.start, procedure.end)
        this.procedureName = procedureName
    }

    NameDerProzedurDefiningCode getProcedureName() {
        return procedureName
    }

    void setProcedureName(NameDerProzedurDefiningCode procedureName) {
        this.procedureName = procedureName
    }

}
