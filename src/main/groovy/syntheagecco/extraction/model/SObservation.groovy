package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType

class SObservation extends BaseResource {

    private String value
    private String unit
    private String system
    private String code
    private String display
    private Date effective
    private String encounterReference
    private Coding coding

    SObservation() {}

    SObservation(String id, String value, String unit, String system, String code, Date effective){
        super(id)
        this.value = value
        this.unit = unit
        this.system = system
        this.code = code
        this.effective = effective
    }

    String getValue() {
        return value
    }

    void setValue(String value) {
        this.value = value
    }

    String getUnit() {
        return unit
    }

    void setUnit(String unit) {
        this.unit = unit
    }

    String getSystem() {
        return system
    }

    void setSystem(String system) {
        this.system = system
    }

    void setCode(String code) {
        this.code = code
    }

    String getCode() {
        return code
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

    String getEncounterReference() {
        return encounterReference
    }

    void setEncounterReference(String encounterReference) {
        this.encounterReference = encounterReference
    }

    Coding getCoding() {
        return coding
    }

    void setCoding(Coding coding) {
        this.coding = coding
    }
}
