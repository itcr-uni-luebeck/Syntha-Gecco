package org.uzl.syntheagecco.openehr.sdk

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.OpenEhrDiagnosisCategory
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SOpenEhrDiagnosis
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.GECCODiagnoseComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.KategorieDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.SchweregradDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.StatusDefiningCode

class OptComplicationsBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptComplicationsBuilder.class)

    OptComplicationsBuilder(){
        super()
    }

    OptComplicationsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        geccoCase.getCompositions().addAll(buildComplicationResources(caseInfo.getDiagnosesForCategory(OpenEhrDiagnosisCategory.COMPLICATION), provider))
    }

    private List<CompositionEntity> buildComplicationResources(List<SOpenEhrDiagnosis> diagnoses, CompositionProvider provider){
        def diagList = new ArrayList<GECCODiagnoseComposition>()

        diagnoses.each {diagnosis ->
            def diag = provider.getDiagnoseComposition(diagnosis)

            diag.setStatusDefiningCode(StatusDefiningCode.FINAL)

            diag.setKategorieDefiningCode(KategorieDefiningCode.COMPLICATION_DISORDER)

            //Severity code for 'Unknown (qualifier value)'
            diag.getVorliegendeDiagnose().setSchweregradDefiningCode(SchweregradDefiningCode.UNKNOWN_QUALIFIER_VALUE)

            diagList.add(diag)
        }

        return diagList
    }

}
