package syntheagecco.openehr.sdk

import com.nedap.archie.rm.datavalues.DvIdentifier
import com.nedap.archie.rm.generic.PartyIdentified
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SOpenEhrDiagnosis
import syntheagecco.openehr.sdk.model.OptGeccoCase
import syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.GECCOMedikationComposition
import syntheagecco.openehr.sdk.model.generated.symptomcomposition.SymptomComposition
import syntheagecco.openehr.sdk.model.generated.symptomcomposition.definition.KategorieDefiningCode
import syntheagecco.openehr.sdk.model.generated.symptomcomposition.definition.NameDesSymptomsKrankheitsanzeichensDefiningCode
import syntheagecco.openehr.sdk.model.generated.symptomcomposition.definition.StatusDefiningCode
import syntheagecco.openehr.sdk.model.generated.symptomcomposition.definition.VorliegendesSymptomObservation
import syntheagecco.utility.DateManipulation

class OptSymptomsBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptMedicationsBuilder.class)
    private final HashMap<String, NameDesSymptomsKrankheitsanzeichensDefiningCode> symptomNameMap

    OptSymptomsBuilder() {
        super()
        symptomNameMap = buildSymptomNameMap()
    }

    OptSymptomsBuilder(SyntheaGeccoConfig config){
        super(config)
        symptomNameMap = buildSymptomNameMap()
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        geccoCase.getCompositions().addAll(buildSymptomResources(caseInfo.getSymptoms(), provider))
    }

    private List<SymptomComposition> buildSymptomResources(List<SDiagnosis> diagnoses, CompositionProvider provider){
        def symList = new ArrayList<SymptomComposition>()

        for(SDiagnosis diagnosis : diagnoses){
            def sym =  provider.getCompositionEntity(SymptomComposition.class)

            sym.setStatusDefiningCode(StatusDefiningCode.FINAL)

            sym.setKategorieDefiningCode(KategorieDefiningCode.SYMPTOM)

            sym.setVorliegendesSymptom(new VorliegendesSymptomObservation().with(true) {
                /*Since the extracted symptom diagnoses will match the codes represented by the enum, we don't need to
                * search for codes that might not be contained in the value set allowed by the openEhr resources*/
                def coding = diagnosis.getCode()
                def symptomName = symptomNameMap[coding.getCode()]
                nameDesSymptomsKrankheitsanzeichensDefiningCode = symptomName

                beginnDerEpisodeValue = DateManipulation.localDateTimeFromDate(diagnosis.getOnsetDateTime())
                datumUhrzeitDesRueckgangsValue = DateManipulation.localDateTimeFromDate(diagnosis.getAbatementDate())
            } as VorliegendesSymptomObservation)

            symList.add(sym)
        }

        return symList
    }

    /**
     * This method builds the hash map for easier and faster access to the enum elements of the
     * NameDesSymptomsKrankheitsanzeichensDefiningCode enum by their code attribute
     * @return HaspMap with which enum entries can be queried by their code field
     */
    private static HashMap<String, NameDesSymptomsKrankheitsanzeichensDefiningCode> buildSymptomNameMap(){
        def map = new HashMap<String, NameDesSymptomsKrankheitsanzeichensDefiningCode>()

        NameDesSymptomsKrankheitsanzeichensDefiningCode.values().each {symptomName ->
            map.put(symptomName.getCode(), symptomName)
        }

        return map
    }

}
