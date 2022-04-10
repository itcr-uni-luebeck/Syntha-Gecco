package org.uzl.syntheagecco.openehr.sdk


import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SMedicationStatement
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.GECCOMedikationComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.AntikoagulanzienObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.ArzneimittelNameDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.ArzneimittelNameDefiningCode4
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.Covid19TherapieObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.KategorieDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.StatusCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.StatusDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition.StatusDefiningCode2
import org.uzl.syntheagecco.utility.DateManipulation

class OptMedicationsBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptMedicationsBuilder.class)

    OptMedicationsBuilder(){
        super()
    }

    OptMedicationsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        def compositions = geccoCase.getCompositions()
        compositions.addAll(buildCovid19TherapyResources(caseInfo.getCovid19Therapies(), provider))
        compositions.addAll(buildAnticoagulationResources(caseInfo.getAnticoagulationMeds(), provider))
    }

    private List<GECCOMedikationComposition> buildCovid19TherapyResources(List<SMedicationStatement> medStatements, CompositionProvider provider){
        def medList = new ArrayList<GECCOMedikationComposition>()

        for(SMedicationStatement medStatement : medStatements){
            def med = provider.getCompositionEntity(GECCOMedikationComposition.class)
            def willBeAdded = true

            //Medication name
            med.setCovid19Therapie(new Covid19TherapieObservation().with(true) {
                switch(medStatement.getCode()){
                    case "360110":
                        //'Product containing lopinavir and ritonavir (medicinal product)'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode.PRODUCT_CONTAINING_LOPINAVIR_AND_RITONAVIR_MEDICINAL_PRODUCT
                        break
                    case "2284960":
                        //'Product containing remdesivir (medicinal product)'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode.PRODUCT_CONTAINING_REMDESIVIR_MEDICINAL_PRODUCT
                        break
                    case "979092":
                        //'Product containing hydroxychloroquine (medicinal product)'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode.PRODUCT_CONTAINING_HYDROXYCHLOROQUINE_MEDICINAL_PRODUCT
                        break
                    case "1116758":
                        //'Product containing chloroquine (medicinal product)'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode.PRODUCT_CONTAINING_CHLOROQUINE_MEDICINAL_PRODUCT
                        break
                    case "1657981":
                        //'Product containing tocilizumab (medicinal product)'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode.PRODUCT_CONTAINING_TOCILIZUMAB_MEDICINAL_PRODUCT
                        break
                    default:
                        //currently the FHIR implementation of GECCO supports more medications provided by Synthea
                        willBeAdded = false
                }

                treeStatusDefiningCode = StatusDefiningCode.ABGESCHLOSSEN
                itemTreeStatus = new StatusCluster().tap {cluster ->
                    statusDefiningCode = StatusDefiningCode2.ABGESCHLOSSEN
                }

                def dateTime = DateManipulation.localDateTimeFromDate(medStatement.getEffective())
                originValue = dateTime
                timeValue = dateTime

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    self.externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()
            } as Covid19TherapieObservation)

            if(willBeAdded){
                med.setKategorieDefiningCode(KategorieDefiningCode.INPATIENT)

                med.setStartTimeValue(DateManipulation.localDateTimeFromDate(medStatement.getEffective()))

                medList.add(med)
            }
        }

        return medList
    }

    private List<GECCOMedikationComposition> buildAnticoagulationResources(List<SMedicationStatement> medStatements, CompositionProvider provider){
        def medList = new ArrayList<GECCOMedikationComposition>()

        for(SMedicationStatement medStatement : medStatements){
            def med = provider.getCompositionEntity(GECCOMedikationComposition.class)
            def willBeAdded = true

            //Medication name
            med.setAntikoagulanzien(new AntikoagulanzienObservation().with(true) {
                switch(medStatement.getCode()){
                    case "1659263":
                        //'Heparin'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode4.HEPARIN
                        break
                    case "854228":
                        //'Enoxaparin'
                        arzneimittelNameDefiningCode = ArzneimittelNameDefiningCode4.ENOXAPARIN
                        break
                    default:
                        //currently the FHIR implementation of GECCO supports more medications provided by Synthea
                        willBeAdded = false
                }

                treeStatusDefiningCode = StatusDefiningCode.ABGESCHLOSSEN
                itemTreeStatus = new StatusCluster().tap {cluster ->
                    statusDefiningCode = StatusDefiningCode2.ABGESCHLOSSEN
                }

                def dateTime = DateManipulation.localDateTimeFromDate(medStatement.getEffective())
                originValue = dateTime
                timeValue = dateTime

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    self.externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()
            } as AntikoagulanzienObservation)

            if(willBeAdded){
                med.setKategorieDefiningCode(KategorieDefiningCode.INPATIENT)

                med.setStartTimeValue(DateManipulation.localDateTimeFromDate(medStatement.getEffective()))

                medList.add(med)
            }
        }

        return medList
    }

}
