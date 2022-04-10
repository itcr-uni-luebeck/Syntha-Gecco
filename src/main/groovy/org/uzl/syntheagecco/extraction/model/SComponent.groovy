package org.uzl.syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Quantity

class SComponent {

    private Coding coding
    private Quantity quantity

    SComponent(){}

    SComponent(Coding coding, Quantity quantity){
        this.coding = coding
        this.quantity = quantity
    }

    Coding getCoding() {
        return coding
    }

    void setCoding(Coding coding) {
        this.coding = coding
    }

    Quantity getQuantity() {
        return quantity
    }

    void setQuantity(Quantity quantity) {
        this.quantity = quantity
    }
}
