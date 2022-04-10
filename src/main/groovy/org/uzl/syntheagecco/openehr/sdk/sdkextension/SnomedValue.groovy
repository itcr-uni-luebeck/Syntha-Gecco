package org.uzl.syntheagecco.openehr.sdk.sdkextension

class SnomedValue extends ExtensibleValueSet {

    SnomedValue(String value, String description, String code) {
        super(value, description, "SNOMED Clinical Terms", code)
    }

}
