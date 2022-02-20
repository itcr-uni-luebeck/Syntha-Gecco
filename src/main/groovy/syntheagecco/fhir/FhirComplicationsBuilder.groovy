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

class FhirComplicationsBuilder extends BaseResourceBuilder{

    FhirComplicationsBuilder(){
        super()
    }

    FhirComplicationsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        //Conditions
        geccoCase.getConditions().addAll(
                this.buildComplicationResources(caseInfo.getComplications(), geccoCase.getPatient())
        )
    }

    private List<Condition> buildComplicationResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conditions = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/complications-covid-19")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_Complication")
            )

            //Clinical status
            con.setClinicalStatus(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")
            ))

            //Verification status
            con.setVerificationStatus(new CodeableConcept()
                    .addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                            "confirmed", "Confirmed"))
                    .addCoding(
                            new Coding("http://snomed.info/sct", "410605003",
                                    "Confirmed present (qualifier value)")
                    )
            )

            //Category
            con.addCategory(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "116223007", "Complication (disorder)")
            ))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Subject reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())

            conditions.add(con)
        }

        return conditions
    }

}
