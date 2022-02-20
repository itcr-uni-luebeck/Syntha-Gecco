package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding

class SMultiObservation {

    private String id
    private Coding code
    private Date effective
    private String encounterReference
    private HashMap<String, SComponent> components

    SMultiObservation(){
        this.components = new HashMap<>()
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    Coding getCode() {
        return code
    }

    void setCode(Coding code) {
        this.code = code
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

    SComponent getComponent(String componentName){
        return this.components.get(componentName)
    }

    void addComponent(String componentName, SComponent component){
        this.components.put(componentName, component)
    }

    int getComponentCount(){
        return this.components.size()
    }
}
