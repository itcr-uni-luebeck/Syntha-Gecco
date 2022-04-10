package org.uzl.syntheagecco.openehr.sdk.sdkextension

import org.ehrbase.client.classgenerator.EnumValueSet

class ExtensibleValueSet implements EnumValueSet{

    private String value

    private String description

    private String terminologyId

    private String code

    ExtensibleValueSet(String value, String description, String terminologyId, String code) {
        this.value = value
        this.description = description
        this.terminologyId = terminologyId
        this.code = code
    }

    @Override
    String getValue() {
        return value
    }

    @Override
    String getDescription() {
        return description
    }

    @Override
    String getTerminologyId() {
        return terminologyId
    }

    @Override
    String getCode() {
        return code
    }

}
