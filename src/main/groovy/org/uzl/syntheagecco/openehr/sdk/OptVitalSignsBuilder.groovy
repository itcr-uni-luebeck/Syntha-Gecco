package org.uzl.syntheagecco.openehr.sdk


import com.nedap.archie.rm.datavalues.quantity.DvProportion
import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SDiagnosticReport
import org.uzl.syntheagecco.extraction.model.SMultiObservation
import org.uzl.syntheagecco.extraction.model.SObservation
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.atemfrequenzcomposition.AtemfrequenzComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.atemfrequenzcomposition.definition.AtemfrequenzObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.atemfrequenzcomposition.definition.RegistereintragKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.atemfrequenzcomposition.definition.StatusDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.beatmungswertecomposition.BeatmungswerteComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.beatmungswertecomposition.definition.BeobachtungenAmBeatmungsgeraetObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.beatmungswertecomposition.definition.EingeatmeterSauerstoffCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.BefundDerBlutgasanalyseComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.BefundDerBlutgasanalyseKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.KohlendioxidpartialdruckCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.KohlendioxidpartialdruckErgebnisStatusElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.LaborergebnisObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.LabortestBezeichnungDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.PhWertCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.SauerstoffpartialdruckCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.SauerstoffpartialdruckErgebnisStatusElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.SauerstoffsaettigungCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.UntersuchterAnalytDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.UntersuchterAnalytDefiningCode2
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.UntersuchterAnalytDefiningCode3
import org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.UntersuchterAnalytDefiningCode4
import org.uzl.syntheagecco.openehr.sdk.model.generated.blutdruckcomposition.BlutdruckComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.blutdruckcomposition.definition.BlutdruckKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.blutdruckcomposition.definition.BlutdruckObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.herzfrequenzcomposition.HerzfrequenzComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.herzfrequenzcomposition.definition.PulsfrequenzHerzfrequenzObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.KoerpertemperaturComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.definition.KategorieDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.definition.KoerpertemperaturObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.pulsoxymetriecomposition.PulsoxymetrieComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.pulsoxymetriecomposition.definition.PulsoxymetrieObservation
import org.uzl.syntheagecco.utility.DateManipulation

class OptVitalSignsBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OptMedicationsBuilder.class)

    OptVitalSignsBuilder() {
        super()
    }

    OptVitalSignsBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        def compositions = geccoCase.getCompositions()

        compositions.addAll(buildRespRateResources(caseInfo.getRespRateMeasurements(), provider))
        compositions.addAll(buildFiO2Resources(caseInfo.getFiO2Measurements(), provider))
        compositions.addAll(buildBloodPressureResources(caseInfo.getBloodPressureMeasurements(), provider))
        compositions.addAll(buildHeartRateResources(caseInfo.getHeartRateMeasurements(), provider))
        compositions.addAll(buildBodyTemperatureResources(caseInfo.getBodyTemperatureMeasurements(), provider))
        compositions.addAll(buildOxySaturationResources(caseInfo.getOxySaturationMeasurements(), provider))
        compositions.addAll(buildBloodGasPanelResources(caseInfo.getBloodGasPanels(), caseInfo.getReportMap(), provider))
    }

    private List<AtemfrequenzComposition> buildRespRateResources(List<SObservation> observations, CompositionProvider provider){
        def respRateList = new ArrayList<AtemfrequenzComposition>()

        observations.each { observation ->
            def respRate = provider.getCompositionEntity(AtemfrequenzComposition.class)

            respRate.setStatusDefiningCode(StatusDefiningCode.FINAL)

            respRate.setKategorie([new RegistereintragKategorieElement().with(true) {
                 value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs"
            }] as ArrayList<RegistereintragKategorieElement>)

            respRate.setAtemfrequenz(new AtemfrequenzObservation().with(true) {
                messwertMagnitude = observation.getValue().toDouble()
                messwertUnits = "/min"

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as AtemfrequenzObservation)

            respRate.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            respRateList.add(respRate)
        }

        return respRateList
    }

    private List<BeatmungswerteComposition> buildFiO2Resources(List<SObservation> observations, CompositionProvider provider){
        def fiO2List = new ArrayList<BeatmungswerteComposition>()

        observations.each { observation ->
            def fiO2 = provider.getCompositionEntity(BeatmungswerteComposition.class)

            fiO2.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.beatmungswertecomposition.definition.StatusDefiningCode.FINAL)

            fiO2.setKategorieValue("http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs")

            fiO2.setBeobachtungenAmBeatmungsgeraet(new BeobachtungenAmBeatmungsgeraetObservation().tap {
                eingeatmeterSauerstoff = new EingeatmeterSauerstoffCluster().tap { o2 ->
                    inspiratorischeSauerstofffraktion = new DvProportion().tap { prop ->
                        numerator = observation.getValue().toDouble()
                        denominator = 100.0
                        //TODO: placeholder value
                        type = 2
                    } as DvProportion
                } as EingeatmeterSauerstoffCluster

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as BeobachtungenAmBeatmungsgeraetObservation)

            fiO2.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            fiO2List.add(fiO2)
        }

        return fiO2List
    }

    private List<BlutdruckComposition> buildBloodPressureResources(List<SMultiObservation> observations, CompositionProvider provider){
        def bpList = new ArrayList<BlutdruckComposition>()

        observations.each { observation ->
            def bp = provider.getCompositionEntity(BlutdruckComposition.class)

            bp.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.blutdruckcomposition.definition.StatusDefiningCode.FINAL)

            bp.setKategorie([new BlutdruckKategorieElement().with(true) {
                value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs"
            }] as List<BlutdruckKategorieElement>)

            bp.setBlutdruck(new BlutdruckObservation().with(true) {
                systolischMagnitude = observation.getComponent("systolic").getQuantity().getValue().toDouble()
                systolischUnits = "mm[Hg]"

                diastolischMagnitude = observation.getComponent("diastolic").getQuantity().getValue().toDouble()
                diastolischUnits = "mm[Hg]"

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as BlutdruckObservation)

            bp.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            bpList.add(bp)
        }

        return bpList
    }

    private List<HerzfrequenzComposition> buildHeartRateResources(List<SObservation> observations, CompositionProvider provider){
        def hrList = new ArrayList<HerzfrequenzComposition>()

        observations.each {observation ->
            def hr = provider.getCompositionEntity(HerzfrequenzComposition.class)

            hr.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.herzfrequenzcomposition.definition.StatusDefiningCode.FINAL)

            hr.setKategorie([new org.uzl.syntheagecco.openehr.sdk.model.generated.herzfrequenzcomposition.definition.RegistereintragKategorieElement().with(true) {
                value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs"
            }] as List<org.uzl.syntheagecco.openehr.sdk.model.generated.herzfrequenzcomposition.definition.RegistereintragKategorieElement>)

            hr.setPulsfrequenzHerzfrequenz(new PulsfrequenzHerzfrequenzObservation().with(true) {
                frequenzMagnitude = observation.getValue().toDouble()
                frequenzUnits = "/min"

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as PulsfrequenzHerzfrequenzObservation)

            hr.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            hrList.add(hr)
        }

        return hrList
    }

    private List<KoerpertemperaturComposition> buildBodyTemperatureResources(List<SObservation> observations, CompositionProvider provider){
        def btList = new ArrayList<KoerpertemperaturComposition>()

        observations.each {observation ->
            def bt = provider.getCompositionEntity(KoerpertemperaturComposition.class)

            bt.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.definition.StatusDefiningCode.FINAL)

            bt.setKategorie([new org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.definition.RegistereintragKategorieElement().with(true) {
                value = KategorieDefiningCode.VITAL_SIGNS
            }] as List<org.uzl.syntheagecco.openehr.sdk.model.generated.koerpertemperaturcomposition.definition.RegistereintragKategorieElement>)

            bt.setKoerpertemperatur(new KoerpertemperaturObservation().with(true) {
                temperaturMagnitude = observation.getValue().toDouble()
                temperaturUnits = "Cel"

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as KoerpertemperaturObservation)

            bt.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            btList.add(bt)
        }

        return btList
    }

    private List<PulsoxymetrieComposition> buildOxySaturationResources(List<SObservation> observations, CompositionProvider provider){
        def osList = new ArrayList<PulsoxymetrieComposition>()

        observations.each {observation ->
            def os = provider.getCompositionEntity(PulsoxymetrieComposition.class)

            os.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.pulsoxymetriecomposition.definition.StatusDefiningCode.FINAL)

            os.setKategorie([new org.uzl.syntheagecco.openehr.sdk.model.generated.pulsoxymetriecomposition.definition.RegistereintragKategorieElement().with(true) {
                value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs"
            }] as List<org.uzl.syntheagecco.openehr.sdk.model.generated.pulsoxymetriecomposition.definition.RegistereintragKategorieElement>)

            os.setPulsoxymetrie(new PulsoxymetrieObservation().with(true) {
                spo = new DvProportion().tap {
                    logger.debug("numerator: ${observation.getValue()}; ${observation.getValue().toDouble()}")
                    numerator = observation.getValue().toDouble()
                    denominator = 100.0
                    //TODO: placeholder value
                    type = 2
                } as DvProportion

                def config = CompositionProvider.getConfig()
                subject = new PartySelf().tap {self ->
                    externalRef = config.getPartyProxy().getExternalRef()
                }
                language = config.getLanguage()

                def dateTime = DateManipulation.localDateTimeFromDate(observation.getEffective())
                originValue = dateTime
                timeValue = dateTime
            } as PulsoxymetrieObservation)

            os.setStartTimeValue(DateManipulation.localDateTimeFromDate(observation.getEffective()))

            osList.add(os)
        }

        return osList
    }

    private List<BefundDerBlutgasanalyseComposition> buildBloodGasPanelResources(List<SDiagnosticReport> diagnosticReports, HashMap<SDiagnosticReport, List<SObservation>> reportMap, CompositionProvider provider){
        def dpList = new ArrayList<BefundDerBlutgasanalyseComposition>()

        diagnosticReports.each {diagnosticReport ->
            def dp = provider.getCompositionEntity(BefundDerBlutgasanalyseComposition.class)

            dp.setStatusDefiningCode(org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition.StatusDefiningCode.FINAL)

            dp.setKategorie([new BefundDerBlutgasanalyseKategorieElement().with(true) {
                value = "http://terminology.hl7.org/CodeSystem/observation-category::vital-signs::Vital Signs"
            }] as List<BefundDerBlutgasanalyseKategorieElement>)

            dp.setLaborergebnis(new LaborergebnisObservation().with(true) {it ->
                labortestBezeichnungDefiningCode = LabortestBezeichnungDefiningCode.GAS_PANEL_ARTERIAL_BLOOD

                reportMap.get(diagnosticReport).each {observation ->
                    switch (observation.getCoding().getCode()){
                        case "2019-8":
                            //PaCO2
                            kohlendioxidpartialdruck = new KohlendioxidpartialdruckCluster().tap { paCo2 ->
                                untersuchterAnalytDefiningCode = UntersuchterAnalytDefiningCode.CARBON_DIOXIDE_PARTIAL_PRESSURE_IN_ARTERIAL_BLOOD
                                analytErgebnisMagnitude = observation.getValue().toDouble()
                                analytErgebnisUnits = "mmHg"
                                paCo2.ergebnisStatus = [new KohlendioxidpartialdruckErgebnisStatusElement().with(true) { result ->
                                    result.value = "final"
                                }] as List<KohlendioxidpartialdruckErgebnisStatusElement>
                            }
                            break
                        case "2703-7":
                            //PaO2
                            sauerstoffpartialdruck = new SauerstoffpartialdruckCluster().with(true) { paO2 ->
                                untersuchterAnalytDefiningCode = UntersuchterAnalytDefiningCode2.OXYGEN_PARTIAL_PRESSURE_IN_ARTERIAL_BLOOD
                                analytErgebnisMagnitude = observation.getValue().toDouble()
                                analytErgebnisUnits = "mmHg"
                                paO2.ergebnisStatus = [new SauerstoffpartialdruckErgebnisStatusElement().with(true) { result ->
                                    result.value = "final"
                                }] as List<SauerstoffpartialdruckErgebnisStatusElement>
                            }
                            break
                        case "2744-1":
                            //pH
                            phWert = new PhWertCluster().with(true) { ph ->
                                untersuchterAnalytDefiningCode = UntersuchterAnalytDefiningCode3.PH_OF_ARTERIAL_BLOOD
                                analytErgebnisMagnitude = observation.getValue().toDouble()
                                analytErgebnisUnits = "mmHg"
                                ph.ergebnisStatus = [new SauerstoffpartialdruckErgebnisStatusElement().with(true) { result ->
                                    result.value = "final"
                                }] as List<SauerstoffpartialdruckErgebnisStatusElement>
                            }
                            break
                        case "2708-6":
                            //Oxygen saturation
                            sauerstoffsaettigung = new SauerstoffsaettigungCluster().with(true) { sat ->
                                untersuchterAnalytDefiningCode = UntersuchterAnalytDefiningCode4.OXYGEN_SATURATION_IN_ARTERIAL_BLOOD
                                analytErgebnisMagnitude = observation.getValue().toDouble()
                                analytErgebnisUnits = "mmHg"
                                sat.ergebnisStatus = [new SauerstoffpartialdruckErgebnisStatusElement().with(true) { result ->
                                    result.value = "final"
                                }] as List<SauerstoffpartialdruckErgebnisStatusElement>
                            }
                            break
                    }
                }
            } as LaborergebnisObservation)

            dp.setStartTimeValue(DateManipulation.localDateTimeFromDate(diagnosticReport.getEffective()))

            dpList.add(dp)
        }

        return dpList
    }

}
