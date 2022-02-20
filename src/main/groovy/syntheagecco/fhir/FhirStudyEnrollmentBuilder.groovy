package syntheagecco.fhir

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.codesystems.DataAbsentReason
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.fhir.model.FhirGeccoCase
import syntheagecco.utility.FhirUtilities

class FhirStudyEnrollmentBuilder extends BaseResourceBuilder{

    FhirStudyEnrollmentBuilder(){
        super()
    }

    FhirStudyEnrollmentBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        List<Observation> observations = geccoCase.getObservations()
        Patient patient = geccoCase.getPatient()

        //observations.add(this.buildStudyEnrollmentResource(patient))
        //observations.add(this.buildInterventionalStudyResources(patient))
    }

    private Observation buildStudyEnrollmentResource(Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(UUID.randomUUID().toString())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_StudyEnrollmentDueToCovid19")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/study-inclusion-covid-19")

        //Status
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(
                new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "survey",
                        "Survey")
        ))

        //Code
        obs.setCode(new CodeableConcept(
                new Coding("https://www.netzwerk-universitaetsmedizin.de/fhir/CodeSystem/ecrf-parameter-codes",
                        "02", "Study inclusion due to Covid-19")
        ))

        //Subject
        obs.setSubject(new Reference(patient))

        //Effective date time
        obs.setEffective(new DateTimeType()
                .addExtension(FhirUtilities.createDataAbsentReasonExt(DataAbsentReason.UNKNOWN))
        )

        //Value
        obs.setValue(new CodeableConcept(
                new Coding("http://snomed.info/sct", "261665006", "Unknown (qualifier value)")
        ))

        return obs
    }

    //TODO: Check if trials of medication count for this category
    private Observation buildInterventionalStudyResources(Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(UUID.randomUUID().toString())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_InterventionalClinicalTrialParticipation")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/interventional-clinical-trial-participation")

        //Status
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(
                new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "survey",
                        "Survey")
        ))

        //Code
        obs.setCode(new CodeableConcept(
                new Coding("https://www.netzwerk-universitaetsmedizin.de/fhir/CodeSystem/ecrf-parameter-codes",
                        "03", "Participation in interventional clinical trials")
        ))

        //Subject
        obs.setSubject(new Reference(patient))

        //Effective date time
        obs.setEffective(new DateTimeType()
                .addExtension(FhirUtilities.createDataAbsentReasonExt(DataAbsentReason.UNKNOWN))
        )

        //Value
        obs.setValue(new CodeableConcept(
                new Coding("http://snomed.info/sct", "261665006", "Unknown (qualifier value)")
        ))

        return obs
    }

}
