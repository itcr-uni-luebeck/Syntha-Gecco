package org.uzl.syntheagecco.openehr.sdk


import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SDiagnosis
import org.uzl.syntheagecco.extraction.model.SObservation
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergewichtcomposition.KoerpergewichtComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergewichtcomposition.definition.KoerpergewichtObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.KoerpergroesseComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.definition.GroesseLaengeObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.schwangerschaftsstatuscomposition.SchwangerschaftsstatusComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.schwangerschaftsstatuscomposition.definition.RegistereintragKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.schwangerschaftsstatuscomposition.definition.SchwangerschaftsstatusObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.schwangerschaftsstatuscomposition.definition.StatusDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.schwangerschaftsstatuscomposition.definition.StatusDefiningCode2
import org.uzl.syntheagecco.utility.DateManipulation

class OptDemographicsBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptDemographicsBuilder.class)

    OptDemographicsBuilder(){
        super()
    }

    OptDemographicsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()
        def pregnancy = caseInfo.getPregnancy()
        def bodyWeight = caseInfo.getBodyWeight()
        def bodyHeight = caseInfo.getBodyHeight()

        if(pregnancy) geccoCase.addComposition(buildPregnancyResource(caseInfo.getPregnancy(), provider))
        if(bodyWeight) geccoCase.addComposition(buildBodyWeightResource(caseInfo.getBodyWeight(), provider))
        if(bodyHeight) geccoCase.addComposition(buildBodyHeightResource(caseInfo.getBodyHeight(), provider))
    }

    private CompositionEntity buildPregnancyResource(SDiagnosis diagnosis, CompositionProvider provider){
        def preg = provider.getCompositionEntity(SchwangerschaftsstatusComposition.class)

        preg.setStatusDefiningCode(StatusDefiningCode.FINAL)

        preg.setKategorie([new RegistereintragKategorieElement().with(true) {
            value = "http://terminology.hl7.org/CodeSystem/observation-category::social-history::Social History"
        }] as ArrayList<RegistereintragKategorieElement>)

        //Pregnancy status
        def pregObservation = new SchwangerschaftsstatusObservation().tap {it ->
            statusDefiningCode = StatusDefiningCode2.SCHWANGER

            def dateTime = DateManipulation.localDateTimeFromDate(diagnosis.getOnsetDateTime())
            originValue = dateTime
            timeValue = dateTime

            def config = CompositionProvider.getConfig()
            subject = new PartySelf().tap {self ->
                self.externalRef = config.getPartyProxy().getExternalRef()
            }
            language = config.getLanguage()
        }
        preg.setSchwangerschaftsstatus(pregObservation)

        preg.setStartTimeValue(DateManipulation.localDateTimeFromDate(diagnosis.getRecordedDate()))

        return preg
    }

    private KoerpergewichtComposition buildBodyWeightResource(SObservation observation, CompositionProvider provider) {
        def gew = provider.getCompositionEntity(KoerpergewichtComposition.class)
        def config = CompositionProvider.getConfig()

        gew.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergewichtcomposition.definition.StatusDefiningCode.FINAL)

        gew.setKategorie([new org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergewichtcomposition.definition.RegistereintragKategorieElement().with(true) { it ->
            value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-sign::Vital Sign"
        }] as ArrayList<org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergewichtcomposition.definition.RegistereintragKategorieElement>)

        gew.setKoerpergewicht(new KoerpergewichtObservation().with(true) { it ->
            timeValue = DateManipulation.localDateTimeFromDate(observation.getEffective())
            gewichtMagnitude = observation.getValue().toDouble()
            gewichtUnits = "kg"

            originValue = DateManipulation.localDateTimeFromDate(observation.getEffective())

            subject = new PartySelf().tap { self ->
                externalRef = config.getPartyProxy().getExternalRef()
            }
            language = config.getLanguage()
        } as KoerpergewichtObservation)

        gew.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

        return gew
    }

    private KoerpergroesseComposition buildBodyHeightResource(SObservation observation, CompositionProvider provider){
        def gew = provider.getCompositionEntity(KoerpergroesseComposition.class)
        def config = CompositionProvider.getConfig()

        gew.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.definition.StatusDefiningCode.FINAL)

        gew.setKategorie([new org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.definition.RegistereintragKategorieElement().with(true) { it ->
            value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-sign::Vital Sign"
        }] as ArrayList<org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.definition.RegistereintragKategorieElement>)

        gew.setGroesseLaenge(new GroesseLaengeObservation().with(true) {it ->
            timeValue = DateManipulation.localDateTimeFromDate(observation.getEffective())
            groesseLaengeMagnitude = observation.getValue().toDouble()
            groesseLaengeUnits = "cm"

            originValue = DateManipulation.localDateTimeFromDate(observation.getEffective())

            subject = new PartySelf().tap { self ->
                externalRef = config.getPartyProxy().getExternalRef()
            }
            language = config.getLanguage()
        } as GroesseLaengeObservation)

        gew.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

        return gew
    }

}
