package syntheagecco.openehr.sdk

import com.nedap.archie.rm.generic.PartySelf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.hl7.fhir.r4.model.Patient;
import syntheagecco.config.SyntheaGeccoConfig;
import syntheagecco.extraction.model.CaseInformation;
import syntheagecco.openehr.sdk.model.OptGeccoCase
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.GECCODiagnoseComposition
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.VorliegendeDiagnoseEvaluation
import syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.GECCOEntlassungsdatenComposition
import syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.ArtDerEntlassungDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.EntlassungsartAdminEntry
import syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.EntlassungsdatenKategorieElement
import syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.StatusDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.KategorieDefiningCode
import syntheagecco.utility.DateManipulation;

class OptDischargeOutcomeBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptDischargeOutcomeBuilder.class)

    OptDischargeOutcomeBuilder(){
        super()
    }

    OptDischargeOutcomeBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        geccoCase.addComposition(this.buildTypeOfDischargeResource(caseInfo, provider))
    }

    private CompositionEntity buildTypeOfDischargeResource(CaseInformation caseInformation, CompositionProvider provider){
        def typeOfDis = provider.getCompositionEntity(GECCOEntlassungsdatenComposition.class)
        def config = CompositionProvider.getConfig()

        typeOfDis.statusDefiningCode = StatusDefiningCode.FINAL

        typeOfDis.kategorie = new ArrayList<EntlassungsdatenKategorieElement>().tap {it ->
            it.add(new EntlassungsdatenKategorieElement().setValue("social-history"))
        }

        typeOfDis.entlassungsart = new EntlassungsartAdminEntry().tap {it ->
            if(caseInformation.getDischargedAlive()){
                it.artDerEntlassungDefiningCode = ArtDerEntlassungDefiningCode.PATIENT_DISCHARGED_ALIVE_FINDING
            }
            else{
                it.artDerEntlassungDefiningCode = ArtDerEntlassungDefiningCode.DEAD_FINDING
            }

            it.subject = new PartySelf().tap { self ->
                externalRef = config.getPartyProxy().getExternalRef()
            }
            it.language = config.getLanguage()
        }

        typeOfDis.setStartTimeValue(DateManipulation.localDateTimeFromDate(caseInformation.getAbatement()))

        return typeOfDis
    }

    /*The respiratory outcome can't be realised with the SDK and the used Template versions since there is no code for
    * respirator dependence*/
    private CompositionEntity buildRespOutcomeResource(CaseInformation caseInformation, CompositionProvider provider){
        return null
    }

}
