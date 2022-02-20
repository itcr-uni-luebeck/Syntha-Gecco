package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode

class SDiagnosis extends CategorizedResource {

    private Coding code
    private Date onsetDateTime
    private Date recordedDate
    private Date abatementDate

    SDiagnosis() {}

    SDiagnosis(String id, Coding code, Date onsetDateTime, Date recordedDate) {
        super(id)
        this.code = code
        this.onsetDateTime = onsetDateTime
        this.recordedDate = recordedDate
    }

    Coding getCode() {
        return code
    }

    void setCode(Coding code) {
        this.code = code
    }

    Date getOnsetDateTime() {
        return onsetDateTime
    }

    void setOnsetDateTime(Date onsetDateTime) {
        this.onsetDateTime = onsetDateTime
    }

    Date getRecordedDate() {
        return recordedDate
    }

    void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate
    }

    Date getAbatementDate() {
        return abatementDate
    }

    void setAbatementDate(Date abatementDate) {
        this.abatementDate = abatementDate
    }

}