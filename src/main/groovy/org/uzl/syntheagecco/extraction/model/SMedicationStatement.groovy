package org.uzl.syntheagecco.extraction.model;

class SMedicationStatement extends BaseResource {

    private String code
    private String system
    private String display
    private Date effective

    SMedicationStatement(){}

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    String getSystem() {
        return system
    }

    void setSystem(String system) {
        this.system = system
    }

    String getDisplay() {
        return display
    }

    void setDisplay(String display) {
        this.display = display
    }

    Date getEffective() {
        return effective
    }

    void setEffective(Date effective) {
        this.effective = effective
    }

}
