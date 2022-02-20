package syntheagecco.fhir

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Quantity
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SObservation
import syntheagecco.extraction.model.SPatient
import syntheagecco.fhir.model.FhirGeccoCase

class FhirDemographicsBuilder extends BaseResourceBuilder {

    FhirDemographicsBuilder(){
        super()
    }

    FhirDemographicsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        //IMPORTANT: It is assumed that the patient resource has already been created at this point
        Patient patient = geccoCase.getPatient()

        //Observation Resources
        if(caseInfo.getPregnancy()) geccoCase.addObservation(this.buildPregnancyResource(caseInfo.getPregnancy(), patient))
        geccoCase.addObservation(this.buildBiologicalSexResource(caseInfo.getPatient(), patient))
        if(caseInfo.getBodyWeight()) geccoCase.addObservation(this.buildBodyWeightResource(caseInfo.getBodyWeight(), patient))
        if(caseInfo.getBodyHeight()) geccoCase.addObservation(this.buildBodyHeightResource(caseInfo.getBodyHeight(), patient))
    }

    private Observation buildPregnancyResource(SDiagnosis diagnosis, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(diagnosis.getId())

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/pregnancy-status")

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${diagnosis.getId()}_Pregnancy")
        )

        //Status (always 'final')
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                "social-history", "Social History")))

        //Code
        obs.setCode(new CodeableConcept(new Coding("http://loinc.org", "82810-3", "Pregnancy status")))

        //Subject Reference
        obs.setSubject(new Reference("Patient/" + patient.getId()))

        //Effective date time
        obs.setEffective(new DateTimeType(diagnosis.getRecordedDate()))

        //Value
        obs.setValue(new CodeableConcept()
                .addCoding(new Coding("http://loinc.org", "LA15173-0", "Pregnant"))
                .addCoding(new Coding("http://snomed.info/sct", "77386006", "Pregnant (finding)"))
        )

        return obs
    }

    private Observation buildBiologicalSexResource(SPatient sPatient, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(UUID.randomUUID().toString())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_SexAssignedAtBirth")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/sex-assigned-at-birth")

        //Status (always 'final')
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                "social-history", "Social History")))

        //Code
        obs.setCode(new CodeableConcept(
                new Coding("http://loinc.org", "76689-9", "Sex assigned at birth")
        ))

        //Subject Reference
        obs.setSubject(new Reference(patient))

        //Effective date time (recorded date time) is assumed to be at patient birth
        obs.setEffective(new DateTimeType(patient.getBirthDate()))

        //Value
        Coding birthSex = new Coding().setSystem("http://hl7.org/fhir/administrative-gender")
        switch(sPatient.getAssignedSex()){
            case "F":
                birthSex.setCode("female").setDisplay("Female")
                break
            case "M":
                birthSex.setCode("male").setDisplay("Male")
                break
            default:
                birthSex.setCode("unkown").setDisplay("Unknown")
        }
        obs.setValue(new CodeableConcept(birthSex))

        return obs
    }

    private Observation buildBodyWeightResource(SObservation observation, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(observation.getId())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_BodyWeight")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/body-weight")

        //Identifier
        obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("OBI").setDisplay("Observation Instance Identifier")
        )).setSystem("https://www.charite.de/fhir/CodeSystem/observation-identifiers").setValue("29463-7_BodyWeight")
                //TODO: Assigner (Hospital)
        )

        //Status (always 'final')
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                "vital-signs", "Vital Signs")))

        //Code
        obs.setCode(new CodeableConcept()
                .addCoding(new Coding("http://loinc.org", "29463-7", "Body weight"))
                .addCoding(new Coding("http://snomed.info/sct", "27113001", "Body weight (observable entity)"))
        )

        //Subject Reference
        obs.setSubject(new Reference("Patient/" + patient.getId()))

        //Effective date time
        obs.setEffective(new DateTimeType(observation.getEffective()))

        //Value
        obs.setValue(new Quantity().setValue(observation.getValue().toBigDecimal()).setUnit(observation.getUnit())
                .setSystem(observation.getSystem()).setCode(observation.getCode()))

        return obs
    }

    private Observation buildBodyHeightResource(SObservation observation, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(observation.getId())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_BodyHeight")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/body-height")

        //Identifier
        obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("OBI").setDisplay("Observation Instance Identifier")
        )).setSystem("https://www.charite.de/fhir/CodeSystem/observation-identifiers").setValue("8302-2_BodyHeight")
                //TODO: Assigner (Hospital)
        )

        //Status (always 'final')
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                "vital-signs", "Vital Signs")))

        //Code
        obs.setCode(new CodeableConcept()
                .addCoding(new Coding("http://loinc.org", "8302-2", "Body height"))
                .addCoding(new Coding("http://snomed.info/sct", "248334005", "Length of body (observable entity)"))
        )

        //Subject Reference
        obs.setSubject(new Reference("Patient/" + patient.getId()))

        //Effective date time
        obs.setEffective(new DateTimeType(observation.getEffective()))

        //Value
        obs.setValue(new Quantity().setValue(observation.getValue().toBigDecimal()).setUnit(observation.getUnit())
                .setSystem(observation.getSystem()).setCode(observation.getCode()))

        return obs
    }

}
