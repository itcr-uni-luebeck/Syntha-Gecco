package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding

class SProcedure extends CategorizedResource {

    private Coding code
    private Date start
    private Date end

    SProcedure(String id, Coding code, Date start, Date end){
        super(id)
        this.code = code
        this.start = start
        this.end = end
    }

    Coding getCode() {
        return code
    }

    void setCode(Coding code) {
        this.code = code
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
}
