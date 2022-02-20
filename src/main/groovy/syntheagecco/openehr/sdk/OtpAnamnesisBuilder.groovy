package syntheagecco.openehr.sdk

import com.nedap.archie.rm.datavalues.DvIdentifier
import com.nedap.archie.rm.generic.PartyIdentified
import com.nedap.archie.rm.generic.PartyProxy
import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.ehrbase.client.classgenerator.shareddefinition.Category
import org.ehrbase.client.classgenerator.shareddefinition.Language
import org.ehrbase.client.classgenerator.shareddefinition.Transition
import org.hl7.elm.r1.Case
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.OpenEhrDiagnosisCategory
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SImmunization
import syntheagecco.extraction.model.SObservation
import syntheagecco.extraction.model.SOpenEhrDiagnosis
import syntheagecco.extraction.model.SProcedure
import syntheagecco.openehr.sdk.model.OptGeccoCase
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.GECCODiagnoseComposition
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.AusgeschlosseneDiagnoseEvaluation
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.AussageUeberDenAusschlussDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.KategorieDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.StatusDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.VorliegendeDiagnoseEvaluation
import syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.GECCOMedikationComposition
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.GECCOProzedurComposition
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.CareflowStepDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.CurrentStateDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.ProzedurAction
import syntheagecco.openehr.sdk.model.generated.impfstatuscomposition.ImpfstatusComposition
import syntheagecco.openehr.sdk.model.generated.impfstatuscomposition.definition.ImpfstoffDefiningCode
import syntheagecco.openehr.sdk.model.generated.impfstatuscomposition.definition.ImpfungAction
import syntheagecco.openehr.sdk.model.generated.raucherstatuscomposition.RaucherstatusComposition
import syntheagecco.openehr.sdk.model.generated.raucherstatuscomposition.definition.RaucherstatusEvaluation
import syntheagecco.openehr.sdk.model.generated.raucherstatuscomposition.definition.RauchverhaltenDefiningCode
import syntheagecco.openehr.sdk.sdkextension.SnomedValue
import syntheagecco.utility.DateManipulation

import java.time.format.DateTimeFormatter

class OtpAnamnesisBuilder extends BaseOTBuilder {

    private static final Logger logger = LogManager.getLogger(OtpAnamnesisBuilder.class)

    OtpAnamnesisBuilder(){
        super()
    }

    OtpAnamnesisBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        def compositions = geccoCase.getCompositions()

        //Add diagnoses
        compositions.addAll(buildChronicLungDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.PULMONARY_MEDICINE), provider))
        compositions.addAll(buildCardioVascDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.VASULAR_MEDICINE), provider))
        compositions.addAll(buildChronicLiverDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.HEPATOLOGY), provider))
        def hiv = caseInfo.getHivInfection()
        if(hiv){
            compositions.add(buildHivResource(caseInfo.getHivInfection(), provider))
        }
        compositions.addAll(buildTissueOrganRecipientResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.TRANSPLANT_MEDICINE), provider))
        compositions.addAll(buildDiabetesMellitusResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.DIABETIC_MEDICINE), provider))
        compositions.addAll(buildMalignantNeoplasticResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.ONCOLOGY), provider))
        compositions.addAll(buildRheumaticDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.RHEUMATOLOGY), provider))
        compositions.addAll(buildImmunologicDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.IMMUNOLOGY), provider))
        compositions.addAll(buildChronicNeuroDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.NEUROLOGY), provider))
        compositions.addAll(buildChronicMentalDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.PSYCHIATRY), provider))
        compositions.addAll(buildChronicKidneyDiseaseResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.NEPHROLOGY), provider))
        compositions.addAll(buildGastrointestinalUlcerResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.GASTROENTOLOGY), provider))

        //Add procedures
        compositions.addAll(buildRespTherapyResources(caseInfo.getRespiratoryTherapies(), provider))

        //Add smoking status
        def smokingStatus = caseInfo.getTobaccoSmokingStatus()
        if(smokingStatus){
            compositions.add(buildSmokingStatusResource(caseInfo.getTobaccoSmokingStatus(), provider))
        }

        //Add Immunization
        def immunizations = caseInfo.getImmunizations()
        compositions.add(buildImmunizationResource(immunizations, provider, caseInfo))
    }

    private List<CompositionEntity> buildChronicLungDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.PULMONARYMEDICINE)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildCardioVascDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.VASCULARMEDICINE)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildChronicLiverDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.HEPATOLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildRheumaticDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.RHEUMATOLOTY)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildImmunologicDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.IMMUNOLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private CompositionEntity buildHivResource(SDiagnosis diagnosis, CompositionProvider provider){
        def diag = provider.getCompositionEntity(GECCODiagnoseComposition.class)
        def config = CompositionProvider.getConfig()

        diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

        diag.setKategorieDefiningCode(KategorieDefiningCode.INFECTIOUSDISEASES)

        diag.setVorliegendeDiagnose(new VorliegendeDiagnoseEvaluation().with(true) {it ->
            def coding = diagnosis.getCode()

            nameDesProblemsDerDiagnoseDefiningCode = NameDesProblemsDerDiagnoseDefiningCode.HUMAN_IMMUNODEFICIENCY_VIRUS_INFECTION

            datumZeitpunktDesAuftretensDerErstdiagnoseValue = DateManipulation.localDateTimeFromDate(diagnosis.getOnsetDateTime())

            subject = new PartySelf().tap {self ->
                externalRef = config.getPartyProxy().getExternalRef()
            }

            language = config.getLanguage()
        } as VorliegendeDiagnoseEvaluation)

        diag.setStartTimeValue(DateManipulation.localDateTimeFromDate(diagnosis.getRecordedDate()))

        return diag
    }

    private List<CompositionEntity> buildTissueOrganRecipientResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getCompositionEntity(GECCODiagnoseComposition.class)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.TRANSPLANT_MEDICINE)

            diag.setVorliegendeDiagnose(new VorliegendeDiagnoseEvaluation().with(true) {it ->
                def coding = diagnosis.getCode()
                //Placeholder value
                nameDesProblemsDerDiagnoseDefiningCode = diagnosis.getDiagnosisName()

                datumZeitpunktDesAuftretensDerErstdiagnoseValue = DateManipulation.localDateTimeFromDate(diagnosis.getOnsetDateTime())
            } as VorliegendeDiagnoseEvaluation)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildDiabetesMellitusResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.DIABETIC_MEDICINE)
            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildMalignantNeoplasticResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.ONCOLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private CompositionEntity buildSmokingStatusResource(SObservation observation, CompositionProvider provider) {
        def smokingStatus = provider.getCompositionEntity(RaucherstatusComposition.class)
        def config = CompositionProvider.getConfig()

        smokingStatus.setStatusDefiningCode(syntheagecco.openehr.sdk.model.generated.raucherstatuscomposition.definition.StatusDefiningCode.FINAL)

        smokingStatus.setRaucherstatus(new RaucherstatusEvaluation().tap {
            switch (observation.getCode()) {
                case "266919005":
                    //Never smoker
                    rauchverhaltenDefiningCode = RauchverhaltenDefiningCode.NICHTRAUCHER
                    break
                case "8517006":
                    //Former smoker
                    rauchverhaltenDefiningCode = RauchverhaltenDefiningCode.EHEMALIGER_RAUCHER
                    break
                case "449868002":
                    //Current every day smoker
                    rauchverhaltenDefiningCode = RauchverhaltenDefiningCode.JA
                    break
                default:
                    //Unknown
                    rauchverhaltenDefiningCode = RauchverhaltenDefiningCode.UNBEKANNT
            }

            subject = new PartySelf().tap {self ->
                self.externalRef = config.getPartyProxy().getExternalRef()
            }
            language = config.getLanguage()
        } as RaucherstatusEvaluation)

        smokingStatus.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

        return smokingStatus
    }

    private List<CompositionEntity> buildChronicNeuroDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.NEUROLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildChronicMentalDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.PSYCHIATRY)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildRespTherapyResources(List<SProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getCompositionEntity(GECCOProzedurComposition.class)

            proc.setProzedur(new ProzedurAction().with(true) {it ->
                currentStateDefiningCode = CurrentStateDefiningCode.COMPLETED
                careflowStepDefiningCode = CareflowStepDefiningCode.PROZEDUR_BEENDET

                def coding = procedure.getCode()
                //Placeholder value
                nameDerProzedurDefiningCode = NameDerProzedurDefiningCode.RESPIRATORY_THERAPY_PROCEDURE

                artDerProzedurDefiningCode = syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.KategorieDefiningCode.RESPIRATORY_THERAPY_PROCEDURE

                timeValue = DateManipulation.localDateTimeFromDate(procedure.getStart())
            } as ProzedurAction)

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildChronicKidneyDiseaseResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.NEUROLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private List<CompositionEntity> buildGastrointestinalUlcerResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.GASTROENTEROLOGY)

            compositions.add(diag)
        }

        return compositions
    }

    private CompositionEntity buildImmunizationResource(List<SImmunization> immunizations, CompositionProvider provider, CaseInformation caseInfo){
        def imm = provider.getCompositionEntity(ImpfstatusComposition.class)
        def config = CompositionProvider.getConfig()
        def immuList = new ArrayList<ImpfungAction>()

        immunizations.each {immunization ->
            immuList.add(new ImpfungAction().tap {it ->
                switch (immunization.getVaccineCode().getCode()){
                    case "08":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HEPATITIS_B_VIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "21":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HUMAN_ALPHAHERPESVIRUS3_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "119":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_ROTAVIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "20":
                    case "115":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_ONLY_BORDETELLA_PERTUSSIS_AND_CLOSTRIDIUM_TETANI_AND_CORYNEBACTERIUM_DIPHTHERIAE_ANTIGENS_MEDICINAL_PRODUCT
                        break
                    case "49":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HAEMOPHILUS_INFLUENZAE_TYPE_B_ANTIGEN_MEDICINAL_PRODUCT_VACCINE_PRODUCT_CONTAINING_HUMAN_POLIOVIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "133":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_STREPTOCOCCUS_PNEUMONIAE_ANTIGEN_MEDICINAL_PRODUCT_VACCINE_PRODUCT_CONTAINING_HAEMOPHILUS_INFLUENZAE_TYPE_B_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "10":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HUMAN_POLIOVIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "140":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_INFLUENZA_VIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "03":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_ONLY_MEASLES_MORBILLIVIRUS_AND_MUMPS_ORTHORUBULAVIRUS_AND_RUBELLA_VIRUS_ANTIGENS_MEDICINAL_PRODUCT
                        break
                    case "83":
                    case "52":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HEPATITIS_A_VIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "114":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_NEISSERIA_MENINGITIDIS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "62":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_HUMAN_PAPILLOMAVIRUS_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "113":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_ONLY_CLOSTRIDIUM_TETANI_AND_CORYNEBACTERIUM_DIPHTHERIAE_ANTIGENS_MEDICINAL_PRODUCT
                        break
                    case "121":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_ONLY_LIVE_ATTENUATED_HUMAN_ALPHAHERPESVIRUS3_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    case "33":
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_CONTAINING_STREPTOCOCCUS_PNEUMONIAE_ANTIGEN_MEDICINAL_PRODUCT_VACCINE_PRODUCT_CONTAINING_HAEMOPHILUS_INFLUENZAE_TYPE_B_ANTIGEN_MEDICINAL_PRODUCT
                        break
                    default:
                        it.impfstoffDefiningCode = ImpfstoffDefiningCode.VACCINE_PRODUCT_PRODUCT_HAS_ACTIVE_INGREDIENT_ATTRIBUTE_HAEMOPHILUS_INFLUENZAE_TYPE_B_VACCINE_SUBSTANCE_HAS_ACTIVE_INGREDIENT_ATTRIBUTE_PERTUSSIS_VACCINE_SUBSTANCE_HAS_ACTIVE_INGREDIENT_ATTRIBUTE_TOXOID_SUBSTANCE
                        break
                }

                it.timeValue = DateManipulation.localDateTimeFromDate(immunization.getOccurence())

                it.currentStateDefiningCode = syntheagecco.openehr.sdk.model.generated.impfstatuscomposition.definition.CurrentStateDefiningCode.COMPLETED
                it.transitionDefiningCode = Transition.FINISH

                it.subject = new PartySelf().tap {self ->
                    self.externalRef = config.getPartyProxy().getExternalRef()
                }
                it.language = config.getLanguage()
            })
        }

        imm.setImpfung(immuList)

        imm.startTimeValue = DateManipulation.localDateTimeFromDate(caseInfo.getOnset())

        return imm
    }

}
