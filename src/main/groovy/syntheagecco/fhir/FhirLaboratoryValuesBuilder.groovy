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
import syntheagecco.extraction.model.SObservation
import syntheagecco.fhir.model.FhirGeccoCase

class FhirLaboratoryValuesBuilder extends BaseResourceBuilder{

    FhirLaboratoryValuesBuilder(){
        super()
    }

    FhirLaboratoryValuesBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        Patient patient = geccoCase.getPatient()

        //Observations
        List<Observation> observations = geccoCase.getObservations()
        observations.addAll(this.buildLaboratoryValueResources(caseInfo.getLaboratoryValues(), patient))
        observations.addAll(this.buildCovid19PcrTestResources(caseInfo.getPcrTests(), patient))
    }

    private List<Observation> buildLaboratoryValueResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()
        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/ObservationLab")

            //Identifier
            obs.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${obs.getId()}_LaboratoryValue")
            )

            //Status (always final)
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", "Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                            "laboratory", "Laboratory")
            ))

            //Code
            obs.setCode(new CodeableConcept(
                    observation.getCoding()
            ))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(observation.getValue().toBigDecimal())
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return obsList
    }

    private List<Observation> buildCovid19PcrTestResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()
        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Identifier
            obs.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${obs.getId()}_Covid19PcrTest")
            )

            //Profile
            obs.getMeta().addProfile("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/ObservationLab")

            //Status (always final)
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", " Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                            "laboratory", "Laboratory")
            ))

            //Code
            obs.setCode(new CodeableConcept(
                    new Coding("http://loinc.org", "94500-6",
                            "SARS-CoV-2 (COVID-19) RNA [Presence] in Respiratory specimen by NAA with probe detection")
                )
                .setText("SARS-CoV-2-RNA (PCR)")
            )

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new CodeableConcept(
                    //Synthea seems to be using the same CodeSystem
                    new Coding(observation.getSystem(), observation.getCode(), observation.getDisplay())
            ))

            obsList.add(obs)
        }

        return obsList
    }

    //TODO: See if Synthea contains SARS-CoV-2 antibody tests

}
