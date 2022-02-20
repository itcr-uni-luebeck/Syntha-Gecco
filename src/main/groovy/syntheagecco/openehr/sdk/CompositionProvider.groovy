package syntheagecco.openehr.sdk

import com.nedap.archie.rm.datavalues.DvIdentifier
import com.nedap.archie.rm.generic.PartyIdentified
import com.nedap.archie.rm.generic.PartyProxy
import com.nedap.archie.rm.generic.PartySelf
import com.nedap.archie.rm.support.identification.PartyRef
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.shareddefinition.Language
import org.ehrbase.client.classgenerator.shareddefinition.Setting
import org.ehrbase.client.classgenerator.shareddefinition.Territory
import org.hl7.fhir.r4.model.Patient
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SOpenEhrDiagnosis
import syntheagecco.extraction.model.SOpenEhrProcedure
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.GECCODiagnoseComposition
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.VorliegendeDiagnoseEvaluation
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.GECCOProzedurComposition
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.CareflowStepDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.CurrentStateDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.KategorieDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.ProzedurAction
import syntheagecco.openehr.sdk.sdkextension.SnomedValue
import syntheagecco.utility.DateManipulation

class CompositionProvider {

    private static final Logger logger = LogManager.getLogger(this.class)

    private CompositionProviderConfig config
    private static CompositionProvider provider

    private CompositionProvider(){ }

    static CompositionProvider getInstance(){
        if(provider == null){
            provider = new CompositionProvider()
        }
        return provider
    }

    public <T> T getCompositionEntity(Class<T> clazz){
        def instance = clazz.getDeclaredConstructor().newInstance() as T

        def proxy = config.getPartyProxy()
        def territory = config.getTerritory()
        def language = config.getLanguage()
        def setting = config.getSetting()

        try{
            clazz.getDeclaredMethod("setComposer", PartyProxy.class).invoke(instance, proxy)
            clazz.getDeclaredMethod("setTerritory", Territory.class).invoke(instance, territory)
            clazz.getDeclaredMethod("setLanguage", Language.class).invoke(instance, language)
            clazz.getDeclaredMethod("setSettingDefiningCode", Setting.class).invoke(instance, setting)
        }
        catch (Exception exc){
            throw new RuntimeException(exc)
        }

        return instance
    }

    GECCODiagnoseComposition getDiagnoseComposition(SOpenEhrDiagnosis diagnosis){
        def diag = getCompositionEntity(GECCODiagnoseComposition.class)
        def onsetDateTime = DateManipulation.localDateTimeFromDate(diagnosis.getOnsetDateTime())
        diag.setVorliegendeDiagnose(new VorliegendeDiagnoseEvaluation().tap {it ->
            it.language = config.getLanguage()
            it.subject = new PartySelf().tap {self ->
                self.externalRef = config.getPartyProxy().getExternalRef()
            }

            it.nameDesProblemsDerDiagnoseDefiningCode = diagnosis.getDiagnosisName()

            it.datumZeitpunktDesAuftretensDerErstdiagnoseValue = onsetDateTime
        })
        diag.setStartTimeValue(onsetDateTime)

        return diag
    }

    GECCOProzedurComposition getProcedureComposition(SOpenEhrProcedure procedure){
        def proc = getCompositionEntity(GECCOProzedurComposition.class)
        def start = DateManipulation.localDateTimeFromDate(procedure.getStart())
        def end = DateManipulation.localDateTimeFromDate(procedure.getEnd())
        proc.setProzedur(new ProzedurAction().tap {it ->
            it.currentStateDefiningCode = CurrentStateDefiningCode.COMPLETED
            it.careflowStepDefiningCode = CareflowStepDefiningCode.PROZEDUR_BEENDET

            it.language = config.getLanguage()
            it.subject = new PartySelf().tap {self ->
                self.externalRef = config.getPartyProxy().getExternalRef()
            }

            it.nameDerProzedurDefiningCode = procedure.getProcedureName()

            it.timeValue = start
        })

        proc.setStartTimeValue(start)
        proc.setEndTimeValue(end)

        return proc
    }

    static CompositionProviderConfig getConfig() {
        if(provider == null){
            provider = new CompositionProvider()
        }
        return provider.@config
    }

    static void setConfig(CompositionProviderConfig config) {
        if(provider == null){
            provider = new CompositionProvider()
        }
        provider.@config = config
    }

}
