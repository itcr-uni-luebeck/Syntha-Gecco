package syntheagecco.openehr.sdk

import com.nedap.archie.rm.datavalues.DvIdentifier
import com.nedap.archie.rm.generic.PartyIdentified
import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SObservation
import syntheagecco.openehr.sdk.model.OptGeccoCase
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.GECCOVirologischerBefundComposition
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.BefundObservation
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.GeccoVirologischerBefundKategorieLoincElement
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.KategorieDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.KategorieLoincDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.LabortestBezeichnungDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.LabortestPanelCluster
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.NachweisDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.ProAnalytCluster
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.StatusDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition.VirusnachweistestDefiningCode
import syntheagecco.utility.DateManipulation

class OptLaboratoryBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptLaboratoryBuilder.class)

    OptLaboratoryBuilder(){
        super()
    }

    OptLaboratoryBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        geccoCase.getCompositions().addAll(buildCovid19PcrTestResources(caseInfo.getPcrTests(), provider))
    }

    private List<GECCOVirologischerBefundComposition> buildCovid19PcrTestResources(List<SObservation> observations, CompositionProvider provider){
        def findingList = new ArrayList<GECCOVirologischerBefundComposition>()

        observations.each { observation ->
            def finding = provider.getCompositionEntity(GECCOVirologischerBefundComposition.class)

            finding.setStatusDefiningCode(StatusDefiningCode.FINAL)

            finding.setKategorieDefiningCode(KategorieDefiningCode.LABORATORY)
            finding.setKategorieLoinc([new GeccoVirologischerBefundKategorieLoincElement().tap {element ->
                element.value = KategorieLoincDefiningCode.LABORATORY_STUDIES_SET
            }] as ArrayList<GeccoVirologischerBefundKategorieLoincElement>)

            finding.setBefund(new BefundObservation().tap { it ->
                labortestBezeichnungDefiningCode = LabortestBezeichnungDefiningCode.DETECTION_OF_VIRUS_PROCEDURE

                //Laboratory panel
                labortestPanel = new LabortestPanelCluster().tap { panel ->
                    def proanalyt = new ProAnalytCluster()
                    proanalyt.setVirusnachweistestDefiningCode(VirusnachweistestDefiningCode.SARS_COV2_COVID19_RNA_PRESENCE_IN_RESPIRATORY_SPECIMEN_BY_NAA_WITH_PROBE_DETECTION)

                    switch (observation.getCoding().getCode()) {
                        case "260373001":
                            proanalyt.setNachweisDefiningCode(NachweisDefiningCode.DETECTED_QUALIFIER_VALUE)
                            break
                        case "260415000":
                            proanalyt.setNachweisDefiningCode(NachweisDefiningCode.NOT_DETECTED_QUALIFIER_VALUE)
                            break
                        default:
                            proanalyt.setNachweisDefiningCode(NachweisDefiningCode.INCONCLUSIVE_QUALIFIER_VALUE)
                    }

                    proanalyt.setErgebnisStatusValue("final")

                    panel.proAnalyt = proanalyt

                    def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                    originValue = dateTime
                    timeValue = dateTime

                    def config = CompositionProvider.getConfig()
                    subject = new PartySelf().tap {self ->
                        self.externalRef = config.getPartyProxy().getExternalRef()
                    }
                    language = config.getLanguage()
                }
            })

            finding.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            findingList.add(finding)
        }

        return findingList
    }

}
