package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding

class SEncounter {

    private int id
    private Date start
    private Date end
    private Coding type

    SEncounter(){}

    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    Date getStart() {
        return start
    }

    void setStart(Date start) {
        this.start = start
    }

    Date getEnd() {
        return end
    }

    void setEnd(Date end) {
        this.end = end
    }

    Coding getType() {
        return type
    }

    void setType(Coding type) {
        this.type = type
    }
}
