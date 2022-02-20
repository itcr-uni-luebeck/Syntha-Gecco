package syntheagecco.fhir

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.fhir.model.FhirGeccoCase

class FhirSymptomsBuilder extends BaseResourceBuilder{

    FhirSymptomsBuilder(){
        super()
    }

    FhirSymptomsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        //Conditions
        geccoCase.getConditions().addAll(this.buildSymptomResources(caseInfo.getSymptoms(), geccoCase.getPatient()))
    }

    private List<Condition> buildSymptomResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conList = new ArrayList<>()

        for(SDiagnosis diagnosis : diagnoses){
            Condition con =  new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_Symptom")
            )

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/symptoms-covid-19")

            //Clinical status
            con.setClinicalStatus(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")
            ))

            //Verification status
            con.setVerificationStatus(new CodeableConcept()
                .addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                        "confirmed", "Confirmed"))
                .addCoding(new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)"))
            )

            //Category
            con.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "75325-1", "Symptom")
            ))

            //Severity
            //TODO

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Subject reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())

            conList.add(con)
        }

        return conList
    }

}
