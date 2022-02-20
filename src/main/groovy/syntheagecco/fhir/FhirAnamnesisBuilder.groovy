package syntheagecco.fhir

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hl7.fhir.r4.model.CodeType
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Extension
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Period
import org.hl7.fhir.r4.model.Procedure
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.codesystems.DataAbsentReason
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SImmunization
import syntheagecco.extraction.model.SObservation
import syntheagecco.extraction.model.SProcedure
import syntheagecco.fhir.model.FhirGeccoCase
import syntheagecco.utility.FhirUtilities

class FhirAnamnesisBuilder extends BaseResourceBuilder {

    private static final Logger logger = LogManager.getLogger(FhirAnamnesisBuilder.class)

    FhirAnamnesisBuilder(){
        super()
    }

    FhirAnamnesisBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        //IMPORTANT: It is assumed that the patient resource has already been created at this point
        Patient patient = geccoCase.getPatient()

        //Add conditions
        List<Condition> conditions = geccoCase.getConditions()
        conditions.addAll(this.buildChronicLungDiseaseResources(caseInfo.getChronicLungDiseases(), patient))
        conditions.addAll(this.buildCardioVascDiseaseResources(caseInfo.getCardioVascularDisorders(), patient))
        conditions.addAll(this.buildChronicLiverDiseaseResources(caseInfo.getChronicLiverDiseases(), patient))
        conditions.addAll(this.buildRheumaImmunoDiseaseResources(caseInfo.getRheumatologicalImmunologicalDiseases(), patient))
        if(caseInfo.getHivInfection()){
            conditions.add(this.buildHivResource(caseInfo.getHivInfection(), patient))
        }
        conditions.addAll(this.buildDiabetesMellitusResources(caseInfo.getDiabetesMellitusList(), patient))
        conditions.addAll(this.buildMalignantNeoplasticResources(caseInfo.getMalignantNeoplasticDiseases(), patient))
        conditions.addAll(this.buildChronicNeuroMentalResources(caseInfo.getChronicNeurologicalMentalDiseases(), patient))
        conditions.addAll(this.buildChronicKidneyDiseaseResources(caseInfo.getChronicKidneyDiseases(), patient))
        conditions.addAll(this.buildGastrointestinalUlcerResources(caseInfo.getGastrointestinalUlcers(), patient))
        conditions.addAll(this.buildTissueOrganRecipientResources(caseInfo.getTissueOrganReplacements(), patient))

        //Add observations
        if(caseInfo.getTobaccoSmokingStatus()){
            geccoCase.addObservation(this.buildSmokingStatusResource(caseInfo.getTobaccoSmokingStatus(), patient))
        }

        //Add procedures
        List<Procedure> procedures = geccoCase.getProcedures()
        procedures.addAll(this.buildRespTherapyResources(caseInfo.getRespiratoryTherapies(), patient))

        //Add immunizations
        List<Immunization> immunizations = geccoCase.getImmunizations()
        immunizations.addAll(this.buildImmunizationResource(caseInfo.getImmunizations(), patient))
    }

    private List<Condition> buildChronicLungDiseaseResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()

        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile: from the "Nationales Forschungsnetzwerk der Universitaetsmedizin zu Covid-19"
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/chronic-lung-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_ChronicLungDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")
            ))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed",
                            "Confirmed")
            )
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "418112009", "Pulmonary medicine")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Subject
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())

            conResources.add(con)
        }

        return conResources
    }

    private List<Condition> buildCardioVascDiseaseResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()

        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/cardiovascular-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_CardioVascularDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")
            ))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed",
                            "Confirmed")
            )
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "722414000",
                    "Vascular medicine")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())

            conResources.add(con)
        }
        return  conResources
    }

    private List<Condition> buildChronicLiverDiseaseResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/chronic-liver-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_ChronicLiverDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "408472002",
                    "Hepatology")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private List<Condition> buildRheumaImmunoDiseaseResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/rheumatological-immunological-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_RheumatologicalOrImmunologicalDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394810000",
                    "Rheumatology")))
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "408480009",
                    "Clinical immunology")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private Condition buildHivResource(SDiagnosis diagnosis, Patient patient){
        Condition con = new Condition()

        //Id
        con.setId(diagnosis.getId())

        //Profile
        con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/human-immunodeficiency-virus-infection")

        //Identifier
        con.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${diagnosis.getId()}_HIV")
        )

        /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
        * values such that they would've been active at their point of relevance for the Covid-19 case*/
        con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                "active", "Active")))

        //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
        CodeableConcept verification = new CodeableConcept()
        verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                "confirmed", "Confirmed"))
        verification.addCoding(
                new Coding("http://snomed.info/sct", "410605003",
                        "Confirmed present (qualifier value)")
        )
        con.setVerificationStatus(verification)

        //Category
        con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394807007",
                "Infectious diseases (specialty)")))

        //Code
        con.setCode(new CodeableConcept(diagnosis.getCode()))

        //Reference
        con.setSubject(new Reference("Patient/" + patient.getId()))

        //Recorded date
        con.setRecordedDate(diagnosis.getRecordedDate())

        return  con
    }

    private List<Condition> buildTissueOrganRecipientResources(List<SProcedure> procedures, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SProcedure procedure : procedures){
            Condition con = new Condition()

            //Id
            con.setId(procedure.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/organ-recipient")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${procedure.getId()}_HistoryOfBeingTissueOrOrganRecipient")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "788415003",
                    "Transplant medicine")))

            //Code
            con.setCode(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "161663000",
                            "History of being a tissue or organ recipient")
            ))

            //Body site
            def bodySiteSnomed = new Coding().setSystem("http://snomed.info/sct")
            def bodySiteIcd = new Coding().setSystem("http://fhir.de/CodeSystem/dimdi/icd-10-gm")
            switch (procedure.getCode().getCode()){
                case "88039007":
                case "232657004":
                    //Lung transplantation
                    bodySiteSnomed.setCode("181216001").setDisplay("Entire lung (body structure)")
                    bodySiteIcd.setCode("Z94.2").setDisplay("Zustand nach Lungentransplantation")
                    break
                case "32413006":
                    //Heart transplantation
                    bodySiteSnomed.setCode("302509004").setDisplay("Entire heart (body structure)")
                    bodySiteIcd.setCode("Z94.1").setDisplay("Zustand nach Herztransplantation")
                    break
                default:
                    logger.warn("[!]No specific mapping for '${procedure.getCode().getCode()}' (Tissue or organ recipient)")
                    bodySiteIcd.setCode("Z94.9").setDisplay("Zustand nach Organ- oder Gewebetransplantation, nicht näher bezeichnet")
                    bodySiteSnomed = null
            }
            con.addBodySite(new CodeableConcept().with {
                if(bodySiteSnomed) addCoding(bodySiteSnomed)
                addCoding(bodySiteIcd)
            })

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            /*Recorded date; we assume the end date to be the date of recording to give some notion of when this
              procedure occurred*/
            con.setRecordedDate(procedure.getEnd())
        }
        return  conResources
    }

    private List<Condition> buildDiabetesMellitusResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/diabetes-mellitus")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_DiabetesMellitus")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "408475000",
                    "Diabetic medicine")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private List<Condition> buildMalignantNeoplasticResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/malignant-neoplastic-disease")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_MalignantNeoplasticDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394593009",
                    "Medical oncology (qualifier value)")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private Observation buildSmokingStatusResource(SObservation observation, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(observation.getId())

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/smoking-status")

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${observation.getId()}_SmokingStatus")
        )

        /*Clinical status: the status is assumed to be 'final' since this is the standard value in a Synthea
        *  observation resource*/
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/observation-category",
                "social-history", "Social History")))

        //Code
        obs.setCode(new CodeableConcept(new Coding("http://loinc.org", "72166-2",
                "Tobacco smoking status")))

        //Reference
        obs.setSubject(new Reference("Patient/" + patient.getId()))

        //Recorded date
        obs.setEffective(new DateTimeType(observation.getEffective()))

        //Value
        Coding coding = new Coding().setSystem("http://loinc.org")
        switch (observation.getCode()){
            case "266919005":
                coding.setCode("LA18978-9").setDisplay("Never smoker")
                break
            case "8517006":
                coding.setCode("LA15920-4").setDisplay("Former smoker")
                break
            case "449868002":
                coding.setCode("LA18976-3").setDisplay("Current every day smoker")
                break
            default:
                coding.setCode("LA18980-5").setDisplay("Unknown if ever smoked")
                break
        }
        obs.setValue(new CodeableConcept(coding))

        return obs
    }

    private List<Condition> buildChronicNeuroMentalResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/chronic-neurological-mental-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_ChronicNeurologicalDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394591006",
                    "Neurology")))
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394587001",
                    "Psychiatry")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private List<Procedure> buildRespTherapyResources(List<SProcedure> procedures, Patient patient){
        List<Procedure> conResources = new ArrayList<>()

        if(procedures.isEmpty()){
            conResources.add(this.buildSingleRespTherapyResource(null, patient, false))
        }
        else{
            for(SProcedure procedure : procedures){
                conResources.add(this.buildSingleRespTherapyResource(procedure, patient, true))
            }
        }

        return  conResources
    }

    private Procedure buildSingleRespTherapyResource(SProcedure procedure, Patient patient, boolean receivedTherapy){
        Procedure proc = new Procedure()

        //Id
        proc.setId(receivedTherapy ? procedure.getId() : UUID.randomUUID().toString())

        //Profile
        proc.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/respiratory-therapies")

        //Identifier
        proc.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${proc.getId()}_RespiratoryTherapy")
        )

        //Status
        proc.setStatus(Procedure.ProcedureStatus.COMPLETED)

        //Category
        proc.setCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "277132007",
                "Therapeutic procedure (procedure)")))

        //Code
        CodeableConcept codings = new CodeableConcept()
        codings.addCoding(new Coding("http://snomed.info/sct", "53950000", "Respiratory therapy (procedure)"))
        if(receivedTherapy) codings.addCoding(procedure.getCode())
        proc.setCode(codings)

        //Reference
        proc.setSubject(new Reference("Patient/" + patient.getId()))

        //Performed peroid
        if(receivedTherapy){
            proc.setPerformed(new Period().setStart(procedure.getStart()).setEnd(procedure.getEnd()))
        }
        else{
            proc.setPerformed(new DateTimeType()
                    .addExtension(new Extension()
                            .setUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
                            .setValue(new CodeType().setValue("not-performed"))
                    )
            )
        }

        return proc
    }

    private List<Condition> buildChronicKidneyDiseaseResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/chronic-kidney-diseases")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_ChronicKidneyDisease")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394589003",
                    "Nephrology (qualifier value)")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }

    private List<Condition> buildGastrointestinalUlcerResources(List<SDiagnosis> diagnoses, Patient patient){
        List<Condition> conResources = new ArrayList<>()
        for(SDiagnosis diagnosis : diagnoses){
            Condition con = new Condition()

            //Id
            con.setId(diagnosis.getId())

            //Profile
            con.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/gastrointestinal-ulcers")

            //Identifier
            con.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${diagnosis.getId()}_GastrointestinalUlcer")
            )

            /*Clinical status: the status is assumed to be 'active' since the onset and abatement of the disease have
            * values such that they would've been active at their point of relevance for the Covid-19 case*/
            con.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical",
                    "active", "Active")))

            //Verification status: the status is assumed to be 'confirmed' since that is the value given by Synthea
            CodeableConcept verification = new CodeableConcept()
            verification.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status",
                    "confirmed", "Confirmed"))
            verification.addCoding(
                    new Coding("http://snomed.info/sct", "410605003",
                            "Confirmed present (qualifier value)")
            )
            con.setVerificationStatus(verification)

            //Category
            con.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "394584008",
                    "Gastroenterology")))

            //Code
            con.setCode(new CodeableConcept(diagnosis.getCode()))

            //Reference
            con.setSubject(new Reference("Patient/" + patient.getId()))

            //Recorded date
            con.setRecordedDate(diagnosis.getRecordedDate())
        }
        return  conResources
    }


    private List<Immunization> buildImmunizationResource(List<SImmunization> immunizations, Patient patient){
        def immuList = new ArrayList<Immunization>()

        immunizations.each {immunization ->
            Immunization imm = new Immunization()

            //Id
            imm.setId(immunization.getId())

            //Profile
            imm.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/immunization")

            //Identifier
            imm.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${imm.getId()}_ImmunizationStatus")
            )

            //Status
            imm.setStatus(Immunization.ImmunizationStatus.COMPLETED)

            //Vaccine code
            imm.setVaccineCode(new CodeableConcept().tap {it ->
                switch (immunization.getVaccineCode().getCode()){
                    case "08":
                        it.addCoding(new Coding("http://snomed.info/sct", "836374004", "Vaccine" +
                                " product containing Hepatitis B virus antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BC", "Hepatitis-Impfstoffe"))
                        break
                    case "21":
                        it.addCoding(new Coding("http://snomed.info/sct", "836495005", "Vaccine" +
                                " product containing Human alphaherpesvirus 3 antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BK", "Varicella Zoster Impfstoffe"))
                        break
                    case "119":
                        it.addCoding(new Coding("http://snomed.info/sct", "836387005", "Vaccine" +
                                " product containing Rotavirus antigen (medicinal product) "))
                        break
                    case "20":
                    case "115":
                        it.addCoding(new Coding("http://snomed.info/sct", "871875004", "Vaccine" +
                                " product containing only Bordetella pertussis and Clostridium tetani and " +
                                "Corynebacterium diphtheriae antigens (medicinal product)"))
                        break
                    case "49":
                        it.addCoding(new Coding("http://snomed.info/sct", "836380007", "Vaccine" +
                                " product containing Haemophilus influenzae type B antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07AG",
                                "Haemophilus influenzae B-Impfstoffe"))
                        break
                    case "133":
                        it.addCoding(new Coding("http://snomed.info/sct", "836398006", "Vaccine" +
                                " product containing Streptococcus pneumoniae antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07AL",
                                "Pneumokokken-Impfstoffe"))
                        break
                    case "10":
                        it.addCoding(new Coding("http://snomed.info/sct", "1031000221108",
                                "Vaccine product containing Human poliovirus antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BF",
                                "Poliomyelitis-Impfstoffe"))
                        break
                    case "140":
                        it.addCoding(new Coding("http://snomed.info/sct", "836377006", "Vaccine" +
                                " product containing Influenza virus antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BB",
                                "Influenza-Impfstoffe"))
                        break
                    case "03":
                        it.addCoding(new Coding("http://snomed.info/sct", "871831003", "Vaccine" +
                                " product containing only Measles morbillivirus and Mumps orthorubulavirus and Rubella" +
                                " virus antigens (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BD52",
                                "Masern, Kombinationen mit Mumps und Röteln, lebend abgeschwächt"))
                        break
                    case "83":
                    case "52":
                        it.addCoding(new Coding("http://snomed.info/sct", "836375003",
                                "Vaccine product containing Hepatitis A virus antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BC",
                                "Hepatitis-Impfstoffe"))
                        break
                    case "114":
                        it.addCoding(new Coding("http://snomed.info/sct", "836401009", "Vaccine" +
                                " product containing Neisseria meningitidis antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07AH",
                                "Meningokokken-Impfstoffe"))
                        break
                    case "62":
                        it.addCoding(new Coding("http://snomed.info/sct", "836379009", "Vaccine" +
                                " product containing Human papillomavirus antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BM01",
                                "Humaner-Papillomvirus-Impfstoff (Typen 6,11,16,18)"))
                        break
                    case "113":
                        it.addCoding(new Coding("http://snomed.info/sct", "871826000", "Vaccine" +
                                " product containing only Clostridium tetani and Corynebacterium diphtheriae antigens" +
                                " (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07AM51",
                                "Tetanus-Toxoid, Kombinationen mit Diphtherie-Toxoid"))
                        break
                    case "121":
                        it.addCoding(new Coding("http://snomed.info/sct", "2221000221107",
                                "Vaccine product containing only live attenuated Human alphaherpesvirus 3 " +
                                        "antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07BK02",
                                "Zoster Virus, lebend abgeschwächt"))
                        break
                    case "33":
                        it.addCoding(new Coding("http://snomed.info/sct", "836398006", "Vaccine" +
                                " product containing Streptococcus pneumoniae antigen (medicinal product)"))
                        it.addCoding(new Coding("http://fhir.de/CodeSystem/bfarm/atc", "J07AL01",
                                "Pneumokokken, gereinigtes Polysaccharid-Antigen"))
                        break
                    default:
                        it.addCoding(new Coding("http://snomed.info/sct", "787859002", "Vaccine" +
                                " product (medicinal product)"))
                        break

                }
            })

            //Patient
            imm.setPatient(new Reference(patient))

            //Occurence date time
            imm.setOccurrence(new DateTimeType(immunization.getOccurence()))

            //Primary Source
            imm.setPrimarySource(immunization.getPrimarySource())

            immuList.add(imm)
        }

        return immuList
    }
}
