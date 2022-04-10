package org.uzl.syntheagecco.openehr.sdk

import com.nedap.archie.rm.generic.PartySelf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.uzl.syntheagecco.config.SyntheaGeccoConfig;
import org.uzl.syntheagecco.extraction.model.CaseInformation;
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.GECCOEntlassungsdatenComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.ArtDerEntlassungDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.EntlassungsartAdminEntry
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.EntlassungsdatenKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoentlassungsdatencomposition.definition.StatusDefiningCode
import org.uzl.syntheagecco.utility.DateManipulation;

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
