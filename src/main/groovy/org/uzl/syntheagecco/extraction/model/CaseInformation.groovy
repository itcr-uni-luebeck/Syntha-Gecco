package org.uzl.syntheagecco.extraction.model

import org.uzl.syntheagecco.extraction.OpenEhrDiagnosisCategory
import org.uzl.syntheagecco.extraction.OpenEhrProcedureCategory
import org.uzl.syntheagecco.extraction.mapping.MultiListMap;

class CaseInformation {

    private SPatient patient

    private SEncounter hospAdmission

    private HashMap<SDiagnosticReport, List<SObservation>> reportMap

    private boolean dischargedAlive
    private Date onset
    private Date abatement

    private List<SDiagnosis> chronicLungDiseases
    private List<SDiagnosis> cardioVascularDisorders
    private List<SDiagnosis> chronicLiverDiseases
    private List<SDiagnosis> rheumatologicalImmunologicalDiseases
    private SDiagnosis hivInfection
    private List<SProcedure> tissueOrganReplacements
    private List<SDiagnosis> diabetesMellitusList
    private List<SDiagnosis> malignantNeoplasticDiseases
    private SObservation tobaccoSmokingStatus
    private List<SDiagnosis> chronicNeurologicalMentalDiseases
    private List<SProcedure> respiratoryTherapies
    private List<SDiagnosis> chronicKidneyDiseases
    private List<SDiagnosis> gastrointestinalUlcers
    private List<SImmunization> immunizations
    //TODO: Resuscitation status

    private SDiagnosis pregnancy
    //TODO: Frailty Score
    private SObservation bodyWeight
    private SObservation bodyHeight

    private List<SDiagnosis> complications

    //TODO: Stage at diagnosis

    private List<SObservation> laboratoryValues
    private List<SObservation> pcrTests
    private List<SObservation> antibodyTests

    private List<SMedicationStatement> covid19Therapies
    private List<SMedicationStatement> aceInhibitors
    private List<SMedicationStatement> immunoglobulins
    private List<SMedicationStatement> anticoagulationMeds

    private SDiagnosis respiratoryOutcome
    private SObservation outcomeAtDischarge
    private SObservation followUPSwapResult

    //TODO: Study enrollment due to Covid-19
    //TODO: Interventional studies participation

    private List<SDiagnosis> symptoms

    private List<SProcedure> hemofiltrationProcedures
    private List<SProcedure> pronePositionProcedures
    private List<SProcedure> apheresisProcedures
    private SEncounter presenceInICU
    private SProcedure ventilationType

    private List<SObservation> paCo2Measurements
    private List<SObservation> paO2Measurements
    private List<SObservation> fiO2Measurements
    private List<SObservation> pHMeasurements
    private List<SObservation> respRateMeasurements
    private List<SMultiObservation> bloodPressureMeasurements
    private List<SObservation> heartRateMeasurements
    private List<SObservation> bodyTemperatureMeasurements
    private List<SObservation> oxySaturationMeasurements
    private List<SDiagnosticReport> bloodGasPanels

    //For openEHR
    private MultiListMap<OpenEhrDiagnosisCategory, SOpenEhrDiagnosis> openEhrDiagnosisMap
    private MultiListMap<OpenEhrDiagnosisCategory, SOpenEhrProcedure> openEhrProcedureMap

    CaseInformation(){
        this.dischargedAlive = true
        this.patient = null
        this.hospAdmission = null
        this.reportMap = new HashMap<>()
        this.onset = null
        this.abatement = null
        this.chronicLungDiseases = new ArrayList<>()
        this.cardioVascularDisorders = new ArrayList<>()
        this.chronicLiverDiseases = new ArrayList<>()
        this.rheumatologicalImmunologicalDiseases = new ArrayList<>()
        this.hivInfection = null
        this.tissueOrganReplacements = new ArrayList<>()
        this.diabetesMellitusList = new ArrayList<>()
        this.malignantNeoplasticDiseases = new ArrayList<>()
        this.tobaccoSmokingStatus = null
        this.chronicNeurologicalMentalDiseases = new ArrayList<>()
        this.respiratoryTherapies = new ArrayList<>()
        this.chronicKidneyDiseases = new ArrayList<>()
        this.gastrointestinalUlcers = new ArrayList<>()
        this.immunizations = new ArrayList<>()
        this.pregnancy = null
        this.bodyWeight = null
        this.bodyHeight = null
        this.complications = new ArrayList<>()
        this.laboratoryValues = new ArrayList<>()
        this.pcrTests = new ArrayList<>()
        this.antibodyTests = new ArrayList<>()
        this.covid19Therapies = new ArrayList<>()
        this.aceInhibitors = new ArrayList<>()
        this.immunoglobulins = new ArrayList<>()
        this.anticoagulationMeds = new ArrayList<>()
        this.respiratoryOutcome = null
        this.outcomeAtDischarge = null
        this.followUPSwapResult = null
        this.symptoms = new ArrayList<>()
        this.hemofiltrationProcedures = new ArrayList<>()
        this.pronePositionProcedures = new ArrayList<>()
        this.presenceInICU = null
        this.ventilationType = null
        this.paCo2Measurements = new ArrayList<>()
        this.paO2Measurements = new ArrayList<>()
        this.fiO2Measurements = new ArrayList<>()
        this.pHMeasurements = new ArrayList<>()
        this.respRateMeasurements = new ArrayList<>()
        this.bloodPressureMeasurements = new ArrayList<>()
        this.heartRateMeasurements = new ArrayList<>()
        this.bodyTemperatureMeasurements = new ArrayList<>()
        this.oxySaturationMeasurements = new ArrayList<>()
        this.bloodGasPanels = new ArrayList<>()
        this.openEhrDiagnosisMap = new MultiListMap<>()
        this.openEhrProcedureMap = new MultiListMap<>()
    }

    SPatient getPatient() {
        return patient
    }

    void setPatient(SPatient patient) {
        this.patient = patient
    }

    SEncounter getHospAdmission() {
        return hospAdmission
    }

    void setHospAdmission(SEncounter hospAdmission) {
        this.hospAdmission = hospAdmission
    }

    HashMap<SDiagnosticReport, List<SObservation>> getReportMap() {
        return reportMap
    }

    void setReportMap(HashMap<SDiagnosticReport, List<SObservation>> reportMap) {
        this.reportMap = reportMap
    }

    Date getOnset() {
        return onset
    }

    void setOnset(Date onset) {
        this.onset = onset
    }

    Date getAbatement() {
        return abatement
    }

    void setAbatement(Date abatement) {
        this.abatement = abatement
    }

    List<SDiagnosis> getChronicLungDiseases() {
        return chronicLungDiseases
    }

    void setChronicLungDiseases(List<SDiagnosis> chronicLungDiseases) {
        this.chronicLungDiseases = chronicLungDiseases
    }

    void addChronicLungDisease(SDiagnosis chronicLungDisease) {
        this.chronicLungDiseases.add(chronicLungDisease)
    }

    List<SDiagnosis> getCardioVascularDisorders() {
        return cardioVascularDisorders
    }

    void setCardioVascularDisorders(List<SDiagnosis> cardioVascularDisorders) {
        this.cardioVascularDisorders = cardioVascularDisorders
    }

    void addCardioVascularDisorder(SDiagnosis cardioVascularDisorder) {
        this.cardioVascularDisorders.add(cardioVascularDisorder)
    }

    List<SDiagnosis> getChronicLiverDiseases() {
        return chronicLiverDiseases
    }

    void setChronicLiverDiseases(List<SDiagnosis> chronicLiverDiseases) {
        this.chronicLiverDiseases = chronicLiverDiseases
    }

    void addChronicLiverDisease(SDiagnosis chronicLiverDisease) {
        this.chronicLiverDiseases.add(chronicLiverDisease)
    }

    List<SDiagnosis> getRheumatologicalImmunologicalDiseases() {
        return rheumatologicalImmunologicalDiseases
    }

    void setRheumatologicalImmunologicalDiseases(List<SDiagnosis> rheumatologicalImmunologicalDiseases) {
        this.rheumatologicalImmunologicalDiseases = rheumatologicalImmunologicalDiseases
    }

    void addRheumatologicalImmunologicalDisease(SDiagnosis rheumatologicalImmunologicalDisease) {
        this.rheumatologicalImmunologicalDiseases.add(rheumatologicalImmunologicalDisease)
    }

    SDiagnosis getHivInfection() {
        return hivInfection
    }

    void setHivInfection(SDiagnosis hivInfection) {
        this.hivInfection = hivInfection
    }

    List<SProcedure> getTissueOrganReplacements() {
        return tissueOrganReplacements
    }

    void setTissueOrganReplacements(List<SProcedure> tissueOrganReplacements) {
        this.tissueOrganReplacements = tissueOrganReplacements
    }

    void addTissueOrganReplacement(SProcedure tissueOrganReplacement) {
        this.tissueOrganReplacements.add(tissueOrganReplacement)
    }

    List<SDiagnosis> getDiabetesMellitusList() {
        return diabetesMellitusList
    }

    void setDiabetesMellitusList(List<SDiagnosis> diabetesMellitusList) {
        this.diabetesMellitusList = diabetesMellitusList
    }

    void addDiabetesMellitus(SDiagnosis diabetesMellitus) {
        this.diabetesMellitusList.add(diabetesMellitus)
    }

    List<SDiagnosis> getMalignantNeoplasticDiseases() {
        return malignantNeoplasticDiseases
    }

    void setMalignantNeoplasticDiseases(List<SDiagnosis> malignantNeoplasticDiseases) {
        this.malignantNeoplasticDiseases = malignantNeoplasticDiseases
    }

    void addMalignantNeoplasticDisease(SDiagnosis malignantNeoplasticDisease) {
        this.malignantNeoplasticDiseases.add(malignantNeoplasticDisease)
    }

    SObservation getTobaccoSmokingStatus() {
        return tobaccoSmokingStatus
    }

    void setTobaccoSmokingStatus(SObservation tobaccoSmokingStatus) {
        this.tobaccoSmokingStatus = tobaccoSmokingStatus
    }

    List<SDiagnosis> getChronicNeurologicalMentalDiseases() {
        return chronicNeurologicalMentalDiseases
    }

    void setChronicNeurologicalMentalDiseases(List<SDiagnosis> chronicNeurologicalMentalDiseases) {
        this.chronicNeurologicalMentalDiseases = chronicNeurologicalMentalDiseases
    }

    void addChronicNeurologicalMentalDisease(SDiagnosis chronicNeurologicalMentalDisease) {
        this.chronicNeurologicalMentalDiseases.add(chronicNeurologicalMentalDisease)
    }


    List<SDiagnosis> getChronicKidneyDiseases() {
        return chronicKidneyDiseases
    }

    void setChronicKidneyDiseases(List<SDiagnosis> chronicKidneyDiseases) {
        this.chronicKidneyDiseases = chronicKidneyDiseases
    }

    void addChronicKidneyDisease(SDiagnosis chronicKidneyDisease) {
        this.chronicKidneyDiseases.add(chronicKidneyDisease)
    }

    List<SDiagnosis> getGastrointestinalUlcers() {
        return gastrointestinalUlcers
    }

    void setGastrointestinalUlcers(List<SDiagnosis> gastrointestinalUlcers) {
        this.gastrointestinalUlcers = gastrointestinalUlcers
    }

    void addGastrointestinalUlcer(SDiagnosis gastrointestinalUlcer) {
        this.gastrointestinalUlcers.add(gastrointestinalUlcer)
    }

    List<SImmunization> getImmunizations() {
        return immunizations
    }

    void setImmunizations(List<SImmunization> immunizations) {
        this.immunizations = immunizations
    }

    void addImmunization(SImmunization immunization){
        this.immunizations.add(immunization)
    }

    SDiagnosis getPregnancy() {
        return pregnancy
    }

    void setPregnancy(SDiagnosis pregnancy) {
        this.pregnancy = pregnancy
    }

    SObservation getBodyWeight() {
        return bodyWeight
    }

    void setBodyWeight(SObservation bodyWeight) {
        this.bodyWeight = bodyWeight
    }

    SObservation getBodyHeight() {
        return bodyHeight
    }

    void setBodyHeight(SObservation bodyHeight) {
        this.bodyHeight = bodyHeight
    }

    List<SDiagnosis> getComplications() {
        return complications
    }

    void setComplications(List<SDiagnosis> complications) {
        this.complications = complications
    }

    void addComplication(SDiagnosis complication) {
        this.complications.add(complication)
    }

    List<SObservation> getLaboratoryValues() {
        return laboratoryValues
    }

    void setLaboratoryValues(List<SObservation> laboratoryValues) {
        this.laboratoryValues = laboratoryValues
    }

    void addLaboratoryValue(SObservation laboratoryValue) {
        this.laboratoryValues.add(laboratoryValue)
    }

    List<SObservation> getPcrTests() {
        return pcrTests
    }

    void setPcrTests(List<SObservation> pcrTests) {
        this.pcrTests = pcrTests
    }

    void addPcrTest(SObservation pcrTest) {
        this.pcrTests.add(pcrTest)
    }

    List<SObservation> getAntibodyTests() {
        return antibodyTests
    }

    void setAntibodyTests(List<SObservation> antibodyTests) {
        this.antibodyTests = antibodyTests
    }

    void addAntibodyTest(SObservation antibodyTest) {
        this.antibodyTests.add(antibodyTest)
    }

    List<SMedicationStatement> getCovid19Therapies() {
        return covid19Therapies
    }

    void setCovid19Therapies(List<SMedicationStatement> covid19Therapies) {
        this.covid19Therapies = covid19Therapies
    }

    void addCovid19Therapy(SMedicationStatement covid19Therapy){
        this.covid19Therapies.add(covid19Therapy)
    }

    List<SMedicationStatement> getAceInhibitors() {
        return aceInhibitors
    }

    void setAceInhibitors(List<SMedicationStatement> aceInhibitors) {
        this.aceInhibitors = aceInhibitors
    }

    void addAceInhibitor(SMedicationStatement aceInhibitor){
        this.aceInhibitors.add(aceInhibitor)
    }

    List<SMedicationStatement> getImmunoglobulins() {
        return immunoglobulins
    }

    void setImmunoglobulins(List<SMedicationStatement> immunoglobulins) {
        this.immunoglobulins = immunoglobulins
    }

    void addImmunoglobulin(SMedicationStatement immunoglobulin){
        this.immunoglobulins.add(immunoglobulin)
    }

    List<SMedicationStatement> getAnticoagulationMeds() {
        return anticoagulationMeds
    }

    void setAnticoagulationMeds(List<SMedicationStatement> anticoagulationMeds) {
        this.anticoagulationMeds = anticoagulationMeds
    }

    void addAnticoagulationMed(SMedicationStatement anticoagulationMed){
        this.anticoagulationMeds.add(anticoagulationMed)
    }

    SDiagnosis getRespiratoryOutcome() {
        return respiratoryOutcome
    }

    void setRespiratoryOutcome(SDiagnosis respiratoryOutcome) {
        this.respiratoryOutcome = respiratoryOutcome
    }

    SObservation getOutcomeAtDischarge() {
        return outcomeAtDischarge
    }

    void setOutcomeAtDischarge(SObservation outcomeAtDischarge) {
        this.outcomeAtDischarge = outcomeAtDischarge
    }

    SObservation getFollowUPSwapResult() {
        return followUPSwapResult
    }

    void setFollowUPSwapResult(SObservation followUPSwapResult) {
        this.followUPSwapResult = followUPSwapResult
    }

    List<SDiagnosis> getSymptoms() {
        return symptoms
    }

    void setSymptoms(List<SDiagnosis> symptoms) {
        this.symptoms = symptoms
    }

    void addSymptom(SDiagnosis symptom) {
        this.symptoms.add(symptom)
    }

    List<SProcedure> getHemofiltrationProcedures() {
        return hemofiltrationProcedures
    }

    void setHemofiltrationProcedures(List<SProcedure> hemofiltrationProcedures) {
        this.hemofiltrationProcedures = hemofiltrationProcedures
    }

    void addHemofiltrationProcedure(SProcedure hemofiltrationProcedure) {
        this.hemofiltrationProcedures.add(hemofiltrationProcedure)
    }

    List<SProcedure> getPronePositionProcedures() {
        return pronePositionProcedures
    }

    void setPronePositionProcedures(List<SProcedure> pronePositionProcedures) {
        this.pronePositionProcedures = pronePositionProcedures
    }

    void addPronePositionProcedure(SProcedure pronePositionProcedure) {
        this.pronePositionProcedures.add(pronePositionProcedure)
    }

    SEncounter getPresenceInIU() {
        return presenceInICU
    }

    void setPresenceInIU(SEncounter presenceInIU) {
        this.presenceInICU = presenceInIU
    }

    SProcedure getVentilationType() {
        return ventilationType
    }

    void setVentilationType(SProcedure ventilationType) {
        this.ventilationType = ventilationType
    }

    List<SProcedure> getRespiratoryTherapies() {
        return respiratoryTherapies
    }

    void addRespiratoryTherapy(SProcedure respiratoryTherapy) {
        this.respiratoryTherapies.add(respiratoryTherapy)
    }

    void setRespiratoryTherapies(List<SProcedure> respiratoryTherapy) {
        this.respiratoryTherapies = respiratoryTherapy
    }

    List<SProcedure> getApheresisProcedures() {
        return apheresisProcedures
    }

    void setApheresisProcedures(List<SProcedure> apheresisProcedures) {
        this.apheresisProcedures = apheresisProcedures
    }

    void addApheresisProcedure(SProcedure apheresisProcedure) {
        this.apheresisProcedures.add(apheresisProcedure)
    }

    List<SObservation> getPaCo2Measurements() {
        return paCo2Measurements
    }

    void setPaCo2Measurements(List<SObservation> paCo2Measurements) {
        this.paCo2Measurements = paCo2Measurements
    }

    void addPaCo2Measurement(SObservation paCo2Measurement){
        this.paCo2Measurements.add(paCo2Measurement)
    }

    List<SObservation> getPaO2Measurements() {
        return paO2Measurements
    }

    void setPaO2Measurements(List<SObservation> paO2Measurements) {
        this.paO2Measurements = paO2Measurements
    }

    void addPaO2Measurement(SObservation paO2Measurement){
        this.paO2Measurements.add(paO2Measurement)
    }

    List<SObservation> getFiO2Measurements() {
        return fiO2Measurements
    }

    void setFiO2Measurements(List<SObservation> fiO2Measurements) {
        this.fiO2Measurements = fiO2Measurements
    }
    void addFiO2Measurement(SObservation fiO2Measurement){
        this.fiO2Measurements.add(fiO2Measurement)
    }

    List<SObservation> getpHMeasurements() {
        return pHMeasurements
    }

    void setpHMeasurements(List<SObservation> pHMeasurements) {
        this.pHMeasurements = pHMeasurements
    }

    void addPHMeasurement(SObservation pHMeasurement){
        this.pHMeasurements.add(pHMeasurement)
    }

    List<SObservation> getRespRateMeasurements() {
        return respRateMeasurements
    }

    void setRespRateMeasurements(List<SObservation> respRateMeasurements) {
        this.respRateMeasurements = respRateMeasurements
    }

    void addRespRateMeasurement(SObservation respRateMeasurement){
        this.respRateMeasurements.add(respRateMeasurement)
    }

    List<SMultiObservation> getBloodPressureMeasurements() {
        return bloodPressureMeasurements
    }

    void setBloodPressureMeasurements(List<SMultiObservation> bloodPressureMeasurements) {
        this.bloodPressureMeasurements = bloodPressureMeasurements
    }

    void addBloodPressureMeasurement(SMultiObservation bloodPressureMeasurement){
        this.bloodPressureMeasurements.add(bloodPressureMeasurement)
    }

    List<SObservation> getHeartRateMeasurements() {
        return heartRateMeasurements
    }

    void setHeartRateMeasurements(List<SObservation> heartRateMeasurements) {
        this.heartRateMeasurements = heartRateMeasurements
    }

    void addHeartRateMeasurement(SObservation heartRateMeasurement){
        this.heartRateMeasurements.add(heartRateMeasurement)
    }

    List<SObservation> getBodyTemperatureMeasurements() {
        return bodyTemperatureMeasurements
    }

    void setBodyTemperatureMeasurements(List<SObservation> bodyTemperatureMeasurements) {
        this.bodyTemperatureMeasurements = bodyTemperatureMeasurements
    }

    void addBodyTemperatureMeasurement(SObservation bodyTemperatureMeasurement){
        this.bodyTemperatureMeasurements.add(bodyTemperatureMeasurement)
    }

    List<SObservation> getOxySaturationMeasurements() {
        return oxySaturationMeasurements
    }

    void setOxySaturationMeasurements(List<SObservation> oxySaturationBPMeasurements) {
        this.oxySaturationMeasurements = oxySaturationBPMeasurements
    }

    void addOxySaturationMeasurement(SObservation oxySaturationMeasurement){
        this.oxySaturationMeasurements.add(oxySaturationMeasurement)
    }

    List<SDiagnosticReport> getBloodGasPanels() {
        return bloodGasPanels
    }

    void setBloodGasPanels(List<SDiagnosticReport> bloodGasPanels) {
        this.bloodGasPanels = bloodGasPanels
    }

    void addBloodGasPanel(SDiagnosticReport bloodGasPanel){
        this.bloodGasPanels.add(bloodGasPanel)
    }

    void addDiagnosisToCategory(OpenEhrDiagnosisCategory category, SOpenEhrDiagnosis diagnosis){
        this.openEhrDiagnosisMap.putOne(category, diagnosis)
    }

    List<SOpenEhrDiagnosis> getDiagnosesForCategory(OpenEhrDiagnosisCategory category){
        return this.openEhrDiagnosisMap.get(category)
    }

    MultiListMap<OpenEhrDiagnosisCategory, SOpenEhrDiagnosis> getDiagnosisMap(){
        return this.openEhrDiagnosisMap
    }

    void addProcedureToCategory(OpenEhrProcedureCategory category, SOpenEhrProcedure procedure){
        this.openEhrProcedureMap.putOne(category, procedure)
    }

    List<SOpenEhrProcedure> getProceduresForCategory(OpenEhrProcedureCategory category){
        return this.openEhrProcedureMap.get(category)
    }

    MultiListMap<OpenEhrProcedureCategory, SOpenEhrProcedure> getProcedureMap(){
        return this.openEhrProcedureMap
    }

    boolean getDischargedAlive() {
        return dischargedAlive
    }

    void setDischargedAlive(boolean dischargedAlive) {
        this.dischargedAlive = dischargedAlive
    }
}
