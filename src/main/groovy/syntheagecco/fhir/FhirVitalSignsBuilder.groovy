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
import syntheagecco.extraction.model.SComponent
import syntheagecco.extraction.model.SDiagnosticReport
import syntheagecco.extraction.model.SMultiObservation
import syntheagecco.extraction.model.SObservation
import syntheagecco.fhir.model.FhirGeccoCase

class FhirVitalSignsBuilder extends BaseResourceBuilder{

    FhirVitalSignsBuilder(){
        super()
    }

    FhirVitalSignsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        Patient patient = geccoCase.getPatient()

        //Observations
        List<Observation> observations = geccoCase.getObservations()
        observations.addAll(this.buildPaCo2Resources(caseInfo.getPaCo2Measurements(), patient))
        observations.addAll(this.buildPaO2Resources(caseInfo.getPaO2Measurements(), patient))
        observations.addAll(this.buildFiO2Resources(caseInfo.getFiO2Measurements(), patient))
        observations.addAll(this.buildPHResources(caseInfo.getpHMeasurements(), patient))
        observations.addAll(this.buildRespRateResources(caseInfo.getRespRateMeasurements(), patient))
        observations.addAll(this.buildHeartRateResources(caseInfo.getHeartRateMeasurements(), patient))
        observations.addAll(this.buildBloodPressureResources(caseInfo.getBloodPressureMeasurements(), patient))
        observations.addAll(this.buildBodyTempResources(caseInfo.getBodyTemperatureMeasurements(), patient))
        observations.addAll(this.buildOxySaturationResources(caseInfo.getOxySaturationMeasurements(), patient))
        observations.addAll(this.buildBloodGasPanelResources(caseInfo.getBloodGasPanels(), patient))
    }

    private List<Observation> buildPaCo2Resources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/blood-gas-panel")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
                    ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("PaCo2_${observation.getId()}")
                    //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", "Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory",
                            "Laboratory")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "18767-4", "Blood gas studies (set)")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildPaO2Resources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/oxygen-partial-pressure")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("PaO2_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", "Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory",
                            "Laboratory")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "18767-4", "Blood gas studies (set)")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildFiO2Resources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/inhaled-oxygen-concentration")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("FiO2_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", "Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory",
                            "Laboratory")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "18767-4", "Blood gas studies (set)")
            ))

            //Code
            obs.setCode(new CodeableConcept(
                    new Coding("http://loinc.org", "3150-0", "Inhaled oxygen concentration")
            ).setText("Inhaled oxygen concentration"))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildPHResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/pH")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("pH_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "26436-6", "Laboratory studies (set)")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory",
                            "Laboratory")
            ))
            obs.addCategory(new CodeableConcept(
                    new Coding("http://loinc.org", "18767-4", "Blood gas studies (set)")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildRespRateResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/respiratory-rate")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("RespiratoryRate_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildBloodPressureResources(List<SMultiObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SMultiObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/blood-pressure")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("BloodPressure_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Code
            obs.setCode(new CodeableConcept()
                    .addCoding(new Coding("http://loinc.org", "85354-9", "Blood pressure panel with all children optional"))
                    .addCoding(new Coding("http://snomed.info/sct", "75367002", "Blood pressure (observable entity)"))
            )

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Components
            SComponent systolic = observation.getComponent("systolic")
            SComponent diastolic = observation.getComponent("diastolic")
            obs.addComponent(new Observation.ObservationComponentComponent()
                    .setCode(new CodeableConcept()
                            .addCoding(new Coding("http://loinc.org", "8480-6", "Systolic blood pressure"))
                            .addCoding(new Coding("http://snomed.info/sct", "271649006", "Systolic blood pressure"))
                    )
                    .setValue(systolic.getQuantity())
            )
            obs.addComponent(new Observation.ObservationComponentComponent()
                    .setCode(new CodeableConcept()
                            .addCoding(new Coding("http://loinc.org", "8462-4", "Diastolic blood pressure"))
                            .addCoding(new Coding("http://snomed.info/sct", "271650006",
                                    "Diastolic blood pressure (observable entity)"))
                    )
                    .setValue(diastolic.getQuantity())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildHeartRateResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/heart-rate")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("HeartRate_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

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

        return  obsList
    }

    private List<Observation> buildBodyTempResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/body-temperature")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("BodyTemperature_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildOxySaturationResources(List<SObservation> observations, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SObservation observation : observations){
            Observation obs = new Observation()

            //Id
            obs.setId(observation.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/oxygen-saturation")

            //Type
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("OxygenSaturation_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Code
            obs.setCode(new CodeableConcept(observation.getCoding()))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(observation.getEffective()))

            //Value
            obs.setValue(new Quantity()
                    .setValue(new BigDecimal(observation.getValue()))
                    .setUnit(observation.getUnit())
                    .setSystem(observation.getSystem())
                    .setCode(observation.getCode())
            )

            obsList.add(obs)
        }

        return  obsList
    }

    private List<Observation> buildBloodGasPanelResources(List<SDiagnosticReport> diagnosticReports, Patient patient){
        List<Observation> obsList = new ArrayList<>()

        for(SDiagnosticReport diagnosticReport : diagnosticReports){
            Observation obs = new Observation()

            //Id
            obs.setId(diagnosticReport.getId())

            //Profile
            obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/blood-gas-panel")

            //Identifier
            obs.addIdentifier(new Identifier().setType(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "OBI",
                            "Observation Instance Identifier")
            ))
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("BloodGasPanel_${observation.getId()}")
            //TODO: Get assigner organisation
                    .setAssigner(new Reference("TODO"))
            )

            //Status: always 'final'
            obs.setStatus(Observation.ObservationStatus.FINAL)

            //Category: always measurement from arterial blood
            obs.addCategory(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs",
                            "Vital Signs")
            ))

            //Subject reference
            obs.setSubject(new Reference("Patient/" + patient.getId()))

            //Effective date time
            obs.setEffective(new DateTimeType(diagnosticReport.getEffective()))

            //Members
            diagnosticReport.getMembers().each {memberId ->
                obs.addHasMember(new Reference(memberId))
            }

            obsList.add(obs)
        }

        return obsList
    }

}
