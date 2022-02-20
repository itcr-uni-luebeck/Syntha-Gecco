package syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode

class SOpenEhrDiagnosis extends SDiagnosis {

    private NameDesProblemsDerDiagnoseDefiningCode diagnosisName

    SOpenEhrDiagnosis(String id, Coding code, Date onsetDateTime, Date recordedDate, NameDesProblemsDerDiagnoseDefiningCode diagnosisName) {
        super(id, code, onsetDateTime, recordedDate)
        this.diagnosisName = diagnosisName
    }

    SOpenEhrDiagnosis(SDiagnosis diagnosis, NameDesProblemsDerDiagnoseDefiningCode diagnosisName) {
        super(diagnosis.id, diagnosis.code, diagnosis.onsetDateTime, diagnosis.recordedDate)
        this.diagnosisName = diagnosisName
    }

    NameDesProblemsDerDiagnoseDefiningCode getDiagnosisName() {
        return diagnosisName
    }

    void setDiagnosisName(NameDesProblemsDerDiagnoseDefiningCode diagnosisName) {
        this.diagnosisName = diagnosisName
    }

}
