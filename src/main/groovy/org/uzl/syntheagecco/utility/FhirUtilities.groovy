package org.uzl.syntheagecco.utility

import org.hl7.fhir.r4.model.CodeType
import org.hl7.fhir.r4.model.Extension
import org.hl7.fhir.r4.model.codesystems.DataAbsentReason

abstract class FhirUtilities {

    static Extension createDataAbsentReasonExt(DataAbsentReason reason){
        Extension ext = new Extension()
        ext.setUrl("http://terminology.hl7.org/CodeSystem/data-absent-reason")
        ext.setValue(new CodeType(reason.toCode()))
        return ext
    }

}
