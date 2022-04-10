package org.uzl.syntheagecco.fhir


import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.fhir.model.FhirGeccoCase

class FhirDischargeOutcomeBuilder extends BaseResourceBuilder {

    FhirDischargeOutcomeBuilder(){
        super()
    }

    FhirDischargeOutcomeBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        def patient = geccoCase.getPatient()
        def observations = geccoCase.getObservations()
        def conditions = geccoCase.getConditions()

        observations.add(this.buildTypeOfDischargeResource(caseInfo, patient))
        conditions.add(this.buildRespOutcomeResource(caseInfo, patient))
    }

    private Observation buildTypeOfDischargeResource(CaseInformation caseInformation, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(UUID.randomUUID().toString())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_TypeOfDischarge")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/ValueSet/discharge-disposition")

        //Status (always final)
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept()
                .addCoding(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                        "social-history", "Social History"))
        )

        //Code
        obs.setCode(new CodeableConcept()
            .addCoding(new Coding("http://loinc.org", "55128-3", "Discharge disposition"))
        )

        //Value
        def valueCoding = new Coding().setSystem("http://snomed.info/sct")
        if(caseInformation.getDischargedAlive()){
            valueCoding.setCode("371827001").setDisplay("Patient discharged alive (finding)")
        }
        else{
            valueCoding.setCode("419099009").setDisplay("Dead (finding)")
        }
        obs.setValue(new CodeableConcept(valueCoding))

        //Subject reference
        obs.setSubject(new Reference(patient))

        //Effective date time
        obs.setEffective(new DateTimeType(caseInformation.getAbatement()))

        return obs
    }

    private Condition buildRespOutcomeResource(CaseInformation caseInformation, Patient patient){
        Condition con = new Condition()

        //Id
        con.setId(UUID.randomUUID().toString())

        //Identifier
        con.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${con.getId()}_RespiratoryOutcome")
        )

        //Profile
        con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/dependence-on-ventilator")

        //Status (always final)
        con.setVerificationStatus(new CodeableConcept().tap {it ->
            it.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "refuted", "Refuted"))
            it.addCoding(new Coding("http://snomed.info/sct", "410594000", "Definitely NOT present (qualifier value)"))
        })

        //Category
        con.addCategory(new CodeableConcept()
                .addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-category",
                        "encounter-diagnosis", "Encounter Diagnosis"))
        )

        //Code
        con.setCode(new CodeableConcept()
                .addCoding(new Coding("http://snomed.info/sct", "444932008", "Dependence on ventilator (finding)"))
        )

        //Subject reference
        con.setSubject(new Reference(patient))

        //Effective date time
        con.setRecordedDateElement(new DateTimeType(caseInformation.getAbatement()))

        return con
    }

}
