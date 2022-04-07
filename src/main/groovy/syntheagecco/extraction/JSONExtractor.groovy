package syntheagecco.extraction


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Quantity
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.exception.ReadingError
import syntheagecco.extraction.mapping.LoincLookup
import syntheagecco.extraction.mapping.MultiListMap
import syntheagecco.extraction.mapping.MultiMap
import syntheagecco.extraction.mapping.OpenEhrDiagnosisCategoryLookup
import syntheagecco.extraction.mapping.OpenEhrNameOfDiagnosisLookup
import syntheagecco.extraction.mapping.OpenEhrNameOfProcedureLookup
import syntheagecco.extraction.mapping.OpenEhrProcedureCategoryLookup
import syntheagecco.extraction.mapping.OpenEhrTransplantLookup
import syntheagecco.extraction.mapping.RxNormLookup
import syntheagecco.extraction.mapping.SnomedLookup
import syntheagecco.extraction.mapping.snomed.SnomedMapper
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SComponent
import syntheagecco.extraction.model.SDiagnosis
import syntheagecco.extraction.model.SDiagnosticReport
import syntheagecco.extraction.model.SEncounter
import syntheagecco.extraction.model.SImmunization
import syntheagecco.extraction.model.SMedicationStatement
import syntheagecco.extraction.model.SMultiObservation
import syntheagecco.extraction.model.SObservation
import syntheagecco.extraction.model.SOpenEhrDiagnosis
import syntheagecco.extraction.model.SOpenEhrProcedure
import syntheagecco.extraction.model.SPatient
import syntheagecco.extraction.model.SProcedure
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode
import syntheagecco.utility.DateManipulation
import syntheagecco.utility.FileManipulation

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset

class JSONExtractor {

    private final static Logger logger = LogManager.getLogger(JSONExtractor.class)

    private SyntheaGeccoConfig config

    private static ObjectMapper objectMapper = getDefaultObjectMapper()
    private static SnomedLookup snomedCodeMap = new SnomedLookup()
    private static LoincLookup loincCodeMap = new LoincLookup()
    private static RxNormLookup rxNormCodeMap = new RxNormLookup()
    private static OpenEhrNameOfDiagnosisLookup openEhrNameOfDiagnosisLookup = new OpenEhrNameOfDiagnosisLookup()
    private static OpenEhrDiagnosisCategoryLookup openEhrDiagnosisCategoryLookup = new OpenEhrDiagnosisCategoryLookup()
    private static OpenEhrNameOfProcedureLookup openEhrNameOfProcedureLookup = new OpenEhrNameOfProcedureLookup()
    private static OpenEhrProcedureCategoryLookup openEhrProcedureCategoryLookup = new OpenEhrProcedureCategoryLookup()
    private static OpenEhrTransplantLookup openEhrTransplantLookup = new OpenEhrTransplantLookup()
    private static SnomedMapper snomedMapper = new SnomedMapper()

    JSONExtractor(){
        this.config = new SyntheaGeccoConfig()
    }

    JSONExtractor(SyntheaGeccoConfig config){
        this.config = config
    }

    SyntheaGeccoConfig getConfig() {
        return config
    }

    void setConfig(SyntheaGeccoConfig config) {
        this.config = config
    }

    private static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper()
        //Configuration
        return defaultObjectMapper
    }

    /*
    private static HashMap<String, GeccoCategory> createSNOMEDLookup() throws Exception{
        logger.info("[#]Creating SNOMED-CT code lookup ...")
        def snomedMapPath = "src/main/resources/maps/snomed"
        return createLookUp(snomedMapPath)
    }

    private static HashMap<String, GeccoCategory> createLoincLookup() throws Exception{
        logger.info("[#]Creating LOINC code lookup ...")
        def loincMapPath = "src/main/resources/maps/loinc"
        return createLookUp(loincMapPath)
    }

    private static HashMap<String, GeccoCategory> createRxNormLookup() throws Exception{
        logger.info("[#]Creating rxNorm code lookup ...")
        def rxNormMapPath = "src/main/resources/maps/rxnorm"
        return createLookUp(rxNormMapPath)
    }

    private static HashMap<String, GeccoCategory> createLookUp(String mapSourceDir) throws Exception{
        ObjectMapper mapper = new ObjectMapper()
        HashMap<String, GeccoCategory> codeMap = new HashMap<>()

        //Get all JSON files in the directory
        def mapFiles = FileManipulation.getFilesInDir(mapSourceDir, FileManipulation.FileExtension.JSON)

        if(mapFiles.size().is(0)) {
            logger.error("No JSON files in parent directory '${mapSourceDir}'!")
            throw new Exception("No JSON files could be found in '${mapSourceDir}'")
        }
        else{
            //Read JSON files
            logger.debug("Reading JSON files containing codes...")
            mapFiles.each { mapFile ->
                try{
                    JsonNode jsonRoot = mapper.readTree(mapFile)
                    GeccoCategory currentCategory = GeccoCategory.findCategory(jsonRoot.get("category").asInt())
                    ArrayNode codes = jsonRoot.get("codes")
                    if(codes.isArray()){
                        for(int i = 0; i < codes.size(); i++){
                            ObjectNode codeEntry = codes.get(i)
                            codeMap.put(codeEntry.get("code").asText(), currentCategory)
                        }
                    }
                    else{
                        throw new Exception("'codes' JSON object in JSON file '${mapFile.getAbsolutePath()}' could not" +
                                "be converted to JSON array!")
                    }
                }
                catch (Exception exc){
                    logger.error("An error occurred during the creation of the code lookup!" +
                            "\nMessage:    ${exc.getMessage()}" +
                            "\nStacktrace: ${ExceptionUtils.getStackTrace(exc)}")
                }
            }
            logger.debug("Total entry count: ${codeMap.size()}")
            return codeMap
        }
    }

    private static MultiMap<String, NameDesProblemsDerDiagnoseDefiningCode> createOpenEhrLookup(String mapSourceDir) throws Exception{
        ObjectMapper mapper = new ObjectMapper()
        MultiMap<String, NameDesProblemsDerDiagnoseDefiningCode> codeMap = new MultiMap<>()

        //Get all JSON files in the directory
        def mapFiles = FileManipulation.getFilesInDir(mapSourceDir, FileManipulation.FileExtension.JSON)

        def diagnosisMap = new HashMap<String, NameDesProblemsDerDiagnoseDefiningCode>()
        NameDesProblemsDerDiagnoseDefiningCode.values().each {value ->
            diagnosisMap[value.getCode()] = value
        }

        if(mapFiles.size().is(0)) {
            logger.error("No JSON files in parent directory '${mapSourceDir}'!")
            throw new Exception("No JSON files could be found in '${mapSourceDir}'")
        }
        else{
            //Read JSON files
            logger.debug("Reading JSON files containing codes...")
            mapFiles.each { mapFile ->
                try{
                    JsonNode jsonRoot = mapper.readTree(mapFile)
                    def currentCategory = diagnosisMap[jsonRoot.get("code").asText()]
                    JsonNode codes = jsonRoot.get("children")
                    if(!codes.isNull()){
                        if(codes != null && codes.isArray()){
                            for(int i = 0; i < codes.size(); i++){
                                JsonNode codeEntry = codes.get(i)
                                codeMap.putOne(codeEntry.get("code").asText(), currentCategory)
                            }
                        }
                        else{
                            throw new Exception("'codes' JSON object in JSON file '${mapFile.getAbsolutePath()}' could not " +
                                    "be converted to JSON array!")
                        }
                    }
                    else{
                        logger.warn("There are no codes for diagnosis name category '${currentCategory.toString()}'.")
                    }
                }
                catch (Exception exc){
                    logger.error("An error occurred during the creation of the code lookup!" +
                            "\nMessage:    ${exc.getMessage()}" +
                            "\nStacktrace: ${ExceptionUtils.getStackTrace(exc)}")
                }
            }
            logger.debug("Total entry count: ${codeMap.size()}")
            return codeMap
        }
    }
     */

    List<CaseInformation> extractInformationFromJSON(String pathToParentDirectory){
        logger.info("[#]Extracting information from generated patient records ...")
        //Get all file paths from the JSON files in the directory
        def fileList = FileManipulation.getFilesInDirRecursive(pathToParentDirectory, FileManipulation.FileExtension.JSON)
        List<CaseInformation> caseList = new ArrayList<>()
        def folderName = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()

        if(fileList.size().is(0)) {
            logger.warn("No JSON files in parent directory '${pathToParentDirectory}'!")
        }
        else{
            //Read JSON files
            logger.debug("Reading JSON files ...")
            fileList.each {jsonFile ->
                try{
                    JsonNode jsonNode = objectMapper.readTree(jsonFile)
                    JsonNode entryArray = jsonNode.get("entry")
                    //Only read patient record files
                    if(entryArray.isArray()){
                        if(entryArray.get(0).get("resource").get("resourceType").asText() == "Patient"){
                            logger.info("Current file: ${jsonFile.name}")
                            extractCaseInformation(entryArray as ArrayNode, caseList)
                        }
                    }
                    else{
                        throw new ReadingError("JSON node 'entry' isn't a JSON array!")
                    }
                }
                catch(Error e){
                    logger.error("Couldn't parse JSON file '${jsonFile.name}' at ${jsonFile.absolutePath}!" +
                            "\n${e.getMessage()}")
                    e.printStackTrace()
                }
                catch (Exception exc){
                    logger.error(exc.getMessage())
                    exc.printStackTrace()
                }

                //Last action: handle detected files based on detected file policy setting
                /*
                switch(config.getDetectedFilePolicy()) {
                    case SyntheaGeccoConfig.DetectedFilePolicy.DELETE_ON_FINISH:
                        if (jsonFile.delete()) {
                            logger.debug("SUCCESS: ${jsonFile.name} deleted.")
                        } else {
                            logger.warn("FAILURE: Can't delete ${jsonFile.name}!")
                        }
                        break

                    default:
                        def newDir = new File("archive\\${folderName}")
                        if (!newDir.exists()) {
                            newDir.mkdirs()
                        }
                        Files.move(jsonFile.toPath(), Paths.get(newDir.absolutePath, jsonFile.name),
                                    StandardCopyOption.REPLACE_EXISTING)
                        break
                }
                 */
            }
        }

        return caseList
    }

    private Optional<MultiListMap<String, JsonNode>> checkRelevance(ArrayNode record) throws ReadingError{
        def hadCovid = false
        def map = new MultiListMap<String, JsonNode>()

        for(JsonNode entry : record.iterator()){
            def resource = entry.get("resource")
            switch(resource.get("resourceType").asText()){
                case "Patient":
                    map.putOne("Patient", resource)
                    break
                case "Condition":
                    ArrayNode codes = resource.get("code").get("coding") as ArrayNode
                    for(JsonNode coding : codes){
                        def code = coding.get("code").asText()
                        def vStatus = resource.get("verificationStatus").get("coding").get(0)
                        if(coding.get("system").asText() == "http://snomed.info/sct"){
                            //COVID-19 diagnosis
                            if(code == "840539006" && vStatus.get("code").asText() == "confirmed"){
                                map.putOne("Covid19", resource)
                                hadCovid = true
                                break
                            }
                            //Suspected COVID-19
                            else if(code == "840544004" && vStatus.get("code").asText() == "confirmed"){
                                map.putOne("SuspectedCovid19", resource)
                                break
                            }
                            else if(snomedMapper.categorizeCode(code) != null){
                                map.putOne("Condition", resource)
                                break
                            }
                        }
                    }
                    break
                case "Procedure":
                    ArrayNode codes = resource.get("code").get("coding") as ArrayNode
                    for(JsonNode coding : codes){
                        def category = snomedCodeMap.get(coding.get("code").asText())
                        if(coding.get("system").asText() == "http://snomed.info/sct" && category != null){
                            map.putOne("Procedure", resource)
                            break
                        }
                    }
                    break
                case "Observation":
                    ArrayNode codes = resource.get("code").get("coding") as ArrayNode
                    for(JsonNode coding : codes) {
                        def code = coding.get("code").asText()
                        //Check for COVID-19 related death observation
                        if (code == "69453-9") {
                            /*
                            if (resource.get("valueCodeableConcept").get("coding").get(0).get("code").asText() == "840539006") {
                                map.putOne("Death", resource)
                                hadCovid = true
                                break
                            }
                             */
                            map.putOne("Death", resource)
                            break
                        }
                        //Other observations
                        else if (coding.get("system").asText() == "http://loinc.org") {
                            def category = loincCodeMap.get(code)
                            if(category != null){
                                if (category == GeccoCategory.BLOOD_PRESSURE) {
                                    map.putOne("BloodPressure", resource)
                                } else {
                                    map.putOne("Observations", resource)
                                }
                                break
                            }
                        }
                    }
                    break
                case "MedicationAdministration":
                    ArrayNode codes = resource.get("medicationCodeableConcept").get("coding") as ArrayNode
                    for(JsonNode coding : codes){
                        def category = rxNormCodeMap.get(coding.get("code").asText())
                        if(coding.get("system").asText() == "http://www.nlm.nih.gov/research/umls/rxnorm" && category != null){
                            map.putOne("MedicationAdministration", resource)
                            break
                        }
                    }
                    break
                case "Encounter":
                    ArrayNode codes = resource.get("type").get(0).get("coding") as ArrayNode
                    for(JsonNode coding : codes){
                        def category = snomedCodeMap.get(coding.get("code").asText())
                        if(coding.get("system").asText() == "http://snomed.info/sct" && category != null){
                            map.putOne("Encounter", resource)
                            break
                        }
                    }
                    break
                case "DiagnosticReport":
                    ArrayNode codes = resource.get("code").get("coding") as ArrayNode
                    for(JsonNode coding : codes){
                        def category = loincCodeMap.get(coding.get("code").asText())
                        if(coding.get("system").asText() == "http://loinc.org" && category != null){
                            map.putOne("DiagnosticReport", resource)
                            break
                        }
                    }
                    break
                case "Immunization":
                    //Since all immunizations can be mapped from CVX to SNOMED-CT or ATC all can be accepted
                    map.putOne("Immunization", resource)
                    break
                default:
                    break
            }
        }

        if(hadCovid) return Optional.of(map)
        else Optional.empty()
    }

    private void extractCaseInformation(ArrayNode record, List<CaseInformation> caseList) throws ReadingError {
        def map = checkRelevance(record).orElse(null)

        if(map != null){
            CaseInformation caseInfo = new CaseInformation()
            ResourceDateChecker dateChecker
            Date onsetDateTime
            Date abatementDateTime
            Date deathDateTime

            def suspected = map["SuspectedCovid19"][0]
            if(suspected != null) onsetDateTime = DateManipulation.dateFromSyntheaDate(suspected.get("onsetDateTime").asText())

            def death = map["Death"][0]
            if(death != null){
                deathDateTime = DateManipulation.dateFromSyntheaDate(death.get("effectiveDateTime").asText())
            }
            else{
                deathDateTime = new Date(Long.MAX_VALUE)
            }

            def covid19 = map["Covid19"][0]
            def dateTime = covid19?.get("abatementDateTime")?.asText()
            if(dateTime != null){
                abatementDateTime = DateManipulation.dateFromSyntheaDate(dateTime)
            }
            else{
                abatementDateTime = new Date(Long.MAX_VALUE)
            }

            //Set onset and abatement date times for later use

            caseInfo.setOnset(onsetDateTime)
            caseInfo.setAbatement(abatementDateTime.before(deathDateTime) ? abatementDateTime : deathDateTime)
            if(deathDateTime.before(abatementDateTime)){
                caseInfo.setAbatement(deathDateTime)
                caseInfo.setDischargedAlive(false)
            }
            else{
                caseInfo.setAbatement(abatementDateTime)
                caseInfo.setDischargedAlive(true)
            }
            dateChecker = new ResourceDateChecker(onsetDateTime, abatementDateTime)

            //The Patient resource
            def patient = map["Patient"][0]
            if(patient != null) caseInfo.setPatient(extractPatientInformation(patient, onsetDateTime))
            else throw new ReadingError("No Patient resource was found!")

            def conCount = 0, procCount = 0, obsCount = 0, medCount = 0, encCount = 0, diagRepCount = 0, immuCount = 0

            //Condition
            map["Condition"].each {con ->
                def category = snomedMapper.categorizeCode(getCodeFromCodingArray(con.get("code").get("coding"), "http://snomed.info/sct"))
                extractDiagnosis(con, caseInfo, category, dateChecker)
                conCount++
            }

            //Procedure
            map["Procedure"].each{proc ->
                def category = snomedCodeMap.get(getCodeFromCodingArray(proc.get("code").get("coding"), "http://snomed.info/sct"))
                extractProcedure(proc, caseInfo, category, dateChecker)
                procCount++
            }

            //Observation
            map["Observation"].each{obs ->
                def category = loincCodeMap.get(getCodeFromCodingArray(obs.get("code").get("coding"), "http://loinc.org"))
                extractObservation(obs, caseInfo, category, dateChecker)
                obsCount++
            }
            map["BloodPressure"].each{bp ->
                extractMultiObservation(bp, caseInfo, GeccoCategory.BLOOD_PRESSURE, dateChecker)
                obsCount++
            }

            //MedicationAdministration
            map["MedicationAdministration"].each {med ->
                def category = rxNormCodeMap.get(getCodeFromCodingArray(med.get("medicationCodeableConcept").get("coding"), "http://www.nlm.nih.gov/research/umls/rxnorm"))
                extractMedicationStatement(med, caseInfo, category, dateChecker)
                medCount++
            }

            //Encounter
            map["Encounter"].each {enc ->
                def category = snomedCodeMap.get(getCodeFromCodingArray(enc.get("type").get(0).get("coding"), "http://snomed.info/sct"))
                extractEncounter(enc, caseInfo, category, dateChecker)
                encCount++
            }

            //DiagnosticReport
            map["DiagnosticReport"].each {rep ->
                def category = loincCodeMap.get(rep.get("code").get("coding").get(0).get("code").asText())
                extractDiagnosticReport(rep, caseInfo, category, dateChecker)
                diagRepCount++
            }

            //Immunization
            map["Immunization"].each {imm ->
                extractImmunization(imm, caseInfo, dateChecker)
                immuCount++
            }

            //Create report map for helping in creating the openEHR GECCO blood gas analysis resource
            def observations = new ArrayList<SObservation>()
            observations.addAll(caseInfo.getPaCo2Measurements())
            observations.addAll(caseInfo.getPaO2Measurements())
            observations.addAll(caseInfo.getpHMeasurements())
            observations.addAll(caseInfo.getOxySaturationMeasurements())
            caseInfo.setReportMap(matchObservationsToDiagnosticReports(caseInfo.getBloodGasPanels(), observations))

            logger.debug("Extracted Conditions: ${conCount}")
            logger.debug("Extracted Procedures:  ${procCount}")
            logger.debug("Extracted Observations: ${obsCount}")
            logger.debug("Extracted MedicationAdministrations: ${medCount}")
            logger.debug("Extracted Encounters: ${encCount}")
            logger.debug("Extracted DiagnosticReports: ${diagRepCount}")
            logger.debug("Extracted Immunizations: ${immuCount}")

            logger.debug("Mapped Diagnoses: ${caseInfo.getDiagnosisMap().size()}")
            logger.debug("Mapped Procedures: ${caseInfo.getProcedureMap().size()}")

            caseList.add(caseInfo)
        }
    }

    private static String getCodeFromCodingArray(JsonNode codingArray, String system){
        for(JsonNode coding : codingArray){
            if(coding.get("system").asText() == system) return coding.get("code").asText()
        }
        return null
    }

    /*
    private void extractCaseInformation(ArrayNode record, List<CaseInformation> caseList) throws ReadingError{
        CaseInformation caseInfo = new CaseInformation()
        ResourceDateChecker dateChecker

        //The Patient resource is expected the be the first array entry
        ObjectNode patientNode = record.get(0).get("resource")

        //Check if COVID19 diagnosis is present, since it indicates a later infection
        def hadCovid = false, covidDeath = false
        Date onsetDateTime
        Date abatementDateTime
        Date deathDateTime
        for(int i = 1; i < record.size(); i++){
            JsonNode resource = record.get(i).get("resource")
            if(resource.get("resourceType").asText() == "Condition"){
                ArrayNode code = resource.get("code").get("coding")
                JsonNode vStatus = resource.get("verificationStatus").get("coding").get(0)
                for(JsonNode coding : code){
                    if(coding.get("code").asText() == "840539006" && vStatus.get("code").asText() == "confirmed" &&
                        !(resource.get("abatementDateTime") == null)){
                        abatementDateTime = DateManipulation.dateFromSyntheaDate(resource.get("abatementDateTime").asText())
                        hadCovid = true
                    }
                    if(coding.get("code").asText() == "840544004" && vStatus.get("code").asText() == "confirmed"){
                        //Take the onsetDateTime of the SuspectedCovid-19 diagnosis since some symptom diagnoses occur
                        //before the Covid-19 diagnosis is made
                        onsetDateTime = DateManipulation.dateFromSyntheaDate(resource.get("onsetDateTime").asText())
                    }
                }
            }
            //Check for COVID-19 related death observation
            else if(resource.get("resourceType").asText( ) == "Observation"
                    && resource.get("code").get("coding").get(0).get("code").asText() == "69453-9"){
                if(resource.get("valueCodeableConcept").get("coding").get(0).get("code").asText() == "840539006"){
                    deathDateTime = DateManipulation.dateFromSyntheaDate(resource.get("effectiveDateTime").asText())
                    covidDeath = true
                    hadCovid = true
                }
            }
        }
        if(covidDeath){
            dateChecker = new ResourceDateChecker(onsetDateTime, deathDateTime)
            abatementDateTime = deathDateTime
            caseInfo.setDischargedAlive(false)
        }
        else{
            dateChecker = new ResourceDateChecker(onsetDateTime, abatementDateTime)
            caseInfo.setDischargedAlive(true)
        }

        if(hadCovid){
            //Set onset and abatement date times for later use
            caseInfo.setOnset(onsetDateTime)
            caseInfo.setAbatement(abatementDateTime)

            //Create patient resource
            if(patientNode.get("resourceType").asText() == "Patient"){
                SPatient patient = extractPatientInformation(patientNode, onsetDateTime)
                caseInfo.setPatient(patient)
            }
            else{
                throw new ReadingError("Patient resource is not the first JSON array element!")
            }
            def conCount = 0, procCount = 0, obsCount = 0, medCount = 0, encCount = 0, diagRepCount = 0, immuCount = 0
            //Extract all relevant resources
            for(int i = 1; i < record.size(); i++){
                ObjectNode resource = record.get(i).get("resource")
                switch(resource.get("resourceType").asText()){
                    case "Condition":
                        ArrayNode codes = resource.get("code").get("coding")
                        for(JsonNode coding : codes){
                            def code = coding.get("code").asText()
                            def category = snomedMapper.categorizeCode(code)
                            if(coding.get("system").asText() == "http://snomed.info/sct" && category != null){
                                extractDiagnosis(resource, caseInfo, category, dateChecker)
                                conCount++
                                break
                            }
                        }
                        break
                    case "Procedure":
                        ArrayNode codes = resource.get("code").get("coding")
                        for(JsonNode coding : codes){
                            def category = snomedMapper.categorizeCode(coding.get("code").asText())
                            if(coding.get("system").asText() == "http://snomed.info/sct" && category != null){
                                extractProcedure(resource, caseInfo, category, dateChecker)
                                procCount++
                                break
                            }
                        }
                        break
                    case "Observation":
                        ArrayNode codes = resource.get("code").get("coding")
                        for(JsonNode coding : codes){
                            def category = loincCodeMap.get(coding.get("code").asText())
                            if(coding.get("system").asText() == "http://loinc.org" && category != null){
                                if(category == GeccoCategory.BLOOD_PRESSURE){
                                    extractMultiObservation(resource, caseInfo, category, dateChecker)
                                }
                                else{
                                    extractObservation(resource, caseInfo, category, dateChecker)
                                }
                                obsCount++
                                break
                            }
                        }
                        break
                    case "MedicationAdministration":
                        ArrayNode codes = resource.get("medicationCodeableConcept").get("coding")
                        for(JsonNode coding : codes){
                            def category = rxNormCodeMap.get(coding.get("code").asText())
                            if(coding.get("system").asText() == "http://www.nlm.nih.gov/research/umls/rxnorm" && category != null){
                                extractMedicationStatement(resource, caseInfo, category, dateChecker)
                                medCount++
                                break
                            }
                        }
                        break
                    case "Encounter":
                        ArrayNode codes = resource.get("type").get(0).get("coding")
                        for(JsonNode coding : codes){
                            def category = snomedCodeMap.get(coding.get("code").asText())
                            if(coding.get("system").asText() == "http://snomed.info/sct" && category != null){
                                extractEncounter(resource, caseInfo, category, dateChecker)
                                encCount ++
                                break
                            }
                        }
                        break
                    case "DiagnosticReport":
                        ArrayNode codes = resource.get("code").get("coding")
                        for(JsonNode coding : codes){
                            def category = loincCodeMap.get(coding.get("code").asText())
                            if(coding.get("system").asText() == "http://loinc.org" && category != null){
                                extractDiagnosticReport(resource, caseInfo, category, dateChecker)
                                diagRepCount ++
                                break
                            }
                        }
                        break
                    case "Immunization":
                        //Since all immunizations can be mapped from CVX to SNOMED-CT or ATC all can be accepted
                        extractImmunization(resource, caseInfo, dateChecker)
                        immuCount ++
                        break
                }
            }

            //Create report map for helping in creating the openEHR GECCO blood gas analysis resource
            def observations = new ArrayList<SObservation>()
            observations.addAll(caseInfo.getPaCo2Measurements())
            observations.addAll(caseInfo.getPaO2Measurements())
            observations.addAll(caseInfo.getpHMeasurements())
            observations.addAll(caseInfo.getOxySaturationMeasurements())
            caseInfo.setReportMap(matchObservationsToDiagnosticReports(caseInfo.getBloodGasPanels(), observations))

            logger.debug("Extracted Conditions: ${conCount}")
            logger.debug("Extracted Procedures:  ${procCount}")
            logger.debug("Extracted Observations: ${obsCount}")
            logger.debug("Extracted MedicationAdministrations: ${medCount}")
            logger.debug("Extracted Encounters: ${encCount}")
            logger.debug("Extracted DiagnosticReports: ${diagRepCount}")
            logger.debug("Extracted Immunizations: ${immuCount}")

            logger.debug("Mapped Diagnoses: ${caseInfo.getDiagnosisMap().size()}")
            logger.debug("Mapped Procedures: ${caseInfo.getProcedureMap().size()}")

            caseList.add(caseInfo)
        }
    }
     */

    private SPatient extractPatientInformation(JsonNode patientNode, Date onsetDateTime){
        SPatient patient = new SPatient()

        //Id
        patient.setId(patientNode.get("id").asText())

        //Birth date
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(patientNode.get("birthDate").asText())
        patient.setBirthDate(birthDate)

        //Calculate patient age and save it
        LocalDate onset = onsetDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        LocalDate birth = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        /*
        long millis = onsetDateTime - birthDate
        Calendar calendar = Calendar.getInstance()
        calendar.setTimeInMillis(millis)
        def ageInYears = calendar.get(Calendar.YEAR)
        */
        patient.setAge(Period.between(birth, onset))

        //Patient name
        def name = patientNode.get("name").get(0)
        patient.setFamily(name.get("family").asText())
        patient.setGiven(name.get("given").get(0).asText())

        ArrayNode extensions = patientNode.get("extension")
        for(JsonNode extension : extensions){
            //Ethnicity
            if(extension.get("url").asText() == "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity"){
                JsonNode ethnicity = extension.get("extension").get(0).get("valueCoding")
                patient.setEthnicGroup(new Coding(ethnicity.get("system").asText(), ethnicity.get("code").asText(),
                        ethnicity.get("display").asText()))
            }
            //Sex assigned at birth
            else if(extension.get("url").asText() == "http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex"){
                patient.setAssignedSex(extension.get("valueCode").asText())
            }
        }

        return patient
    }

    private void extractDiagnosis(JsonNode diagnosisNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        //Id
        String id = diagnosisNode.get("id").asText()

        def coding = new Coding().setSystem("http://snomed.info/sct")

        def openEhrDiagNames = new ArrayList<NameDesProblemsDerDiagnoseDefiningCode>()
        def openEhrDiagCategories = new HashMap<NameDesProblemsDerDiagnoseDefiningCode, OpenEhrDiagnosisCategory>()
        for(JsonNode code : diagnosisNode.get("code").get("coding")){
            if(code.get("system").asText() == "http://snomed.info/sct"){
                def codeString = snomedMapper.getCodeForCategorizedCode(code.get("code").asText(), category)

                //Check documentation on getCodeForCategorizedCode for possible cases
                if(codeString != null){
                    openEhrDiagNames = openEhrNameOfDiagnosisLookup.get(codeString) as List<NameDesProblemsDerDiagnoseDefiningCode>
                    openEhrDiagNames.each {value ->
                        openEhrDiagCategories.put(value, openEhrDiagnosisCategoryLookup.get(value) as OpenEhrDiagnosisCategory)
                    }
                    logger.debug("DiagCode: (${category.toString()}) ${codeString} vs ${code.get("code").asText()}")
                    coding.setCode(codeString).setDisplay(code.get("display").asText())

                    Date onsetDateTime = DateManipulation.dateFromSyntheaDate(diagnosisNode.get("onsetDateTime").asText())
                    Date recordedDate = DateManipulation.dateFromSyntheaDate(diagnosisNode.get("recordedDate").asText())
                    Date abatementDate = diagnosisNode.get("abatementDateTime") == null ?
                            null : DateManipulation.dateFromSyntheaDate(diagnosisNode.get("abatementDateTime").asText())
                    SDiagnosis diagnosis = new SDiagnosis(id, coding, onsetDateTime, recordedDate)
                    diagnosis.setAbatementDate(abatementDate)

                    /*Only proceed if the onset and abatement dates of the resource mark the resource as relevant for the specific
                      case*/
                    if(dateChecker.confirmDates(onsetDateTime, abatementDate, category)) {
                        switch (category) {
                            case GeccoCategory.CHRONIC_LUNG_DISEASE:
                                caseInfo.addChronicLungDisease(diagnosis)
                                break
                            case GeccoCategory.CARDIOVASCULAR_DISEASE:
                                caseInfo.addCardioVascularDisorder(diagnosis)
                                break
                            case GeccoCategory.CHRONIC_LIVER_DISEASE:
                                caseInfo.addChronicLiverDisease(diagnosis)
                                break
                            case GeccoCategory.RHEUMATOLOGICAL_IMMUNOLOGICAL_DISEASE:
                                caseInfo.addRheumatologicalImmunologicalDisease(diagnosis)
                                break
                            case GeccoCategory.HIV_INFECTION:
                                if (caseInfo.getHivInfection() == null) {
                                    caseInfo.setHivInfection(diagnosis)
                                }
                                break
                            case GeccoCategory.DIABETES_MELLITUS:
                                caseInfo.addDiabetesMellitus(diagnosis)
                                break
                            case GeccoCategory.MALIGNANT_NEOPLASTIC_DISEASE:
                                caseInfo.addMalignantNeoplasticDisease(diagnosis)
                                break
                            case GeccoCategory.CHRONIC_NEURO_MENTAL_DISEASE:
                                caseInfo.addChronicNeurologicalMentalDisease(diagnosis)
                                break
                            case GeccoCategory.CHRONIC_KIDNEY_DISEASE:
                                caseInfo.addChronicKidneyDisease(diagnosis)
                                break
                            case GeccoCategory.GASTROINTESTINAL_ULCERS:
                                caseInfo.addGastrointestinalUlcer(diagnosis)
                                break
                            case GeccoCategory.PREGNANCY:
                                if (caseInfo.getPregnancy() == null) {
                                    caseInfo.setPregnancy(diagnosis)
                                }
                                break
                            case GeccoCategory.COMPLICATIONS:
                                caseInfo.addComplication(diagnosis)
                                break
                        }

                        /*Only add the diagnoses for later processing to openEHR OPT instances if they could actually be mapped to a
                        * diagnosis defined in the NameDesProblemsDerDiagnoseDefiningCode code set*/
                        if (openEhrDiagNames) {
                            openEhrDiagNames.each { diagName ->

                            }
                            //Add all diagnosis names for openEhr instance creation
                            diagnosis.addAllDiagnosisTypes(openEhrDiagNames)
                            //Map to fitting category for openEhr instance creation
                            openEhrDiagCategories.each { entry ->
                                def openEhrDiagnosis = new SOpenEhrDiagnosis(diagnosis, entry.key)
                                caseInfo.addDiagnosisToCategory(entry.value, openEhrDiagnosis)
                            }
                        }
                    }
                }
            }
        }
    }

    private void extractProcedure(JsonNode procedureNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        //Id
        String id = procedureNode.get("id").asText()

        Coding coding = new Coding().setSystem("http://snomed.info/sct")

        NameDerProzedurDefiningCode openEhrProcName
        OpenEhrProcedureCategory openEhrProcCategory
        //If procedure contains information about transplantations
        NameDesProblemsDerDiagnoseDefiningCode openEhrDiagName
        for(JsonNode code : procedureNode.get("code").get("coding")){
            if(code.get("system").asText() == "http://snomed.info/sct"){
                def codeString = code.get("code").asText()
                coding.setCode(codeString).setDisplay(code.get("display").asText())
                openEhrProcName = openEhrNameOfProcedureLookup.get(codeString)?.getAt(0) as NameDerProzedurDefiningCode
                if(openEhrProcName) openEhrProcCategory = openEhrProcedureCategoryLookup.get(openEhrProcName) as OpenEhrProcedureCategory
                if(category == GeccoCategory.TISSUE_ORGAN_RECIPIENT) {
                    logger.debug("TOR: ${codeString}")
                    openEhrDiagName = openEhrTransplantLookup.get(codeString)[0] as NameDesProblemsDerDiagnoseDefiningCode
                }
            }
        }
        JsonNode performedPeriod = procedureNode.get("performedPeriod")
        Date start = DateManipulation.dateFromSyntheaDate(performedPeriod.get("start").asText())
        Date end = DateManipulation.dateFromSyntheaDate(performedPeriod.get("end").asText())
        SProcedure procedure = new SProcedure(id, coding, start, end)

        if(dateChecker.confirmDates(start, end, category)){
            switch (category){
                case GeccoCategory.RESPIRATORY_THERAPY:
                    caseInfo.addRespiratoryTherapy(procedure)
                    break
                case GeccoCategory.DIALYSIS_HEMOFILTRATION:
                    caseInfo.addHemofiltrationProcedure(procedure)
                    break
                case GeccoCategory.APHERESIS:
                    caseInfo.addApheresisProcedure(procedure)
                    break
                case GeccoCategory.PRONE_POSITION:
                    caseInfo.addPronePositionProcedure(procedure)
                    break
                case GeccoCategory.VENTILATION_TYPE:
                    caseInfo.setVentilationType(procedure)
                    break
                case GeccoCategory.TISSUE_ORGAN_RECIPIENT:
                    caseInfo.addTissueOrganReplacement(procedure)
                    break
            }

            /*Only add the procedures (that are later processed into a openEHR GECCOProzedur resource) for later processing
            * to openEHR OPT instances if they could actually be mapped to a procedure defined in the
            * NameDerProzedurDefiningCode enum*/
            if(openEhrProcName && !openEhrDiagName){
                //Map to fitting category for openEhr instance creation
                caseInfo.addProcedureToCategory(openEhrProcCategory, new SOpenEhrProcedure(procedure, openEhrProcName))
            }
            else if(openEhrDiagName){
                def diag = new SOpenEhrDiagnosis(procedure.getId(), procedure.getCode(), procedure.getEnd(), procedure.getEnd(), openEhrDiagName)
                caseInfo.addDiagnosisToCategory(OpenEhrDiagnosisCategory.TRANSPLANT_MEDICINE, diag)
            }
        }
    }

    private void extractObservation(JsonNode observationNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        SObservation observation = new SObservation()

        //Id
        observation.setId(observationNode.get("id").asText())

        //Code (Coding)
        JsonNode codeNode = observationNode.get("code").get("coding").get(0)
        Coding coding = new Coding().setSystem(codeNode.get("system").asText()).setCode(codeNode.get("code").asText())
            .setDisplay(codeNode.get("display").asText())
        observation.setCoding(coding)

        JsonNode valueEntry
        if(observationNode.get("valueQuantity") != null){
            valueEntry = observationNode.get("valueQuantity")
            observation.setValue(valueEntry.get("value").asText())
            observation.setUnit(valueEntry.get("unit").asText())
        }
        else if(observationNode.get("valueCodeableConcept") != null){
            valueEntry = observationNode.get("valueCodeableConcept").get("coding").get(0)
            observation.setDisplay(valueEntry.get("display").asText())
        }
        else{
            throw new RuntimeException("Observation resource contains unaccounted value entry!")
        }
        observation.setSystem(valueEntry.get("system").asText())
        observation.setCode(valueEntry.get("code").asText())

        Date effective = DateManipulation.dateFromSyntheaDate(observationNode.get("effectiveDateTime").asText())
        observation.setEffective(effective)

        String encounterReference = observationNode.get("encounter").get("reference").asText()
        observation.setEncounterReference(encounterReference)

        if(dateChecker.confirmDate(effective, category)){
            switch (category){
                case GeccoCategory.TOBACCO_SMOKING_STATUS:
                    SObservation smokingStatus = caseInfo.getTobaccoSmokingStatus()
                    if(smokingStatus == null || smokingStatus.getEffective().before(effective)){
                        caseInfo.setTobaccoSmokingStatus(observation)
                    }
                    break
                case GeccoCategory.LABORATORY_VALUE:
                    caseInfo.addLaboratoryValue(observation)
                    break
                case GeccoCategory.PCR_TEST:
                    caseInfo.addPcrTest(observation)
                    break
                case GeccoCategory.ANTIBODY_TEST:
                    caseInfo.addAntibodyTest(observation)
                    break
                case GeccoCategory.PACO2:
                    caseInfo.addPaCo2Measurement(observation)
                    break
                case GeccoCategory.PAO2:
                    caseInfo.addPaO2Measurement(observation)
                    break
                case GeccoCategory.FIO2:
                    caseInfo.addFiO2Measurement(observation)
                    break
                case GeccoCategory.RESPIRATORY_RATE:
                    caseInfo.addRespRateMeasurement(observation)
                    break
                case GeccoCategory.HEART_RATE:
                    caseInfo.addHeartRateMeasurement(observation)
                    break
                case GeccoCategory.BODY_TEMPERATURE:
                    caseInfo.addBodyTemperatureMeasurement(observation)
                    break
                case GeccoCategory.OXYGEN_SATURATION:
                    caseInfo.addOxySaturationMeasurement(observation)
                    break
                case GeccoCategory.PH:
                    caseInfo.addPHMeasurement(observation)
                    break
                case GeccoCategory.BODY_HEIGHT:
                    SObservation bodyHeight = caseInfo.getBodyHeight()
                    if(bodyHeight == null || bodyHeight.getEffective().before(effective)){
                        caseInfo.setBodyHeight(observation)
                    }
                    break
                case GeccoCategory.BODY_WEIGHT:
                    SObservation bodyWeight = caseInfo.getBodyWeight()
                    if(bodyWeight == null || bodyWeight.getEffective().before(effective)){
                        caseInfo.setBodyWeight(observation)
                    }
                    break
            }
        }
    }

    private void extractMultiObservation(JsonNode observationNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        SMultiObservation multiObservation = new SMultiObservation()

        //Id
        multiObservation.setId(observationNode.get("id").asText())

        JsonNode obsCoding = observationNode.get("code").get("coding").get(0)
        Coding code = new Coding()
        code.setCode(obsCoding.get("code").asText())
        code.setSystem(obsCoding.get("system").asText())
        code.setSystem(obsCoding.get("display").asText())
        multiObservation.setCode(code)

        Date effective = DateManipulation.dateFromSyntheaDate(observationNode.get("effectiveDateTime").asText())
        multiObservation.setEffective(effective)

        if(dateChecker.confirmDate(effective, category)){
            switch(category){
                case GeccoCategory.BLOOD_PRESSURE:
                    SComponent systolic = new SComponent(), diastolic = new SComponent()
                    ArrayNode components = observationNode.get("component")
                    for(JsonNode component : components){
                        Coding compCode = new Coding()
                        JsonNode coding = component.get("code").get("coding").get(0)
                        compCode.setSystem(coding.get("system").asText())
                        compCode.setCode(coding.get("code").asText())
                        compCode.setDisplay(coding.get("display").asText())

                        Quantity compQuantity = new Quantity()
                        JsonNode quantity = component.get("valueQuantity")
                        compQuantity.setValue(new BigDecimal(quantity.get("value").asText()))
                        compQuantity.setUnit(quantity.get("unit").asText())
                        compQuantity.setSystem(quantity.get("system").asText())
                        compQuantity.setCode(quantity.get("code").asText())

                        if(coding.get("code").asText() == "8462-4"){
                            diastolic.setCoding(compCode)
                            diastolic.setQuantity(compQuantity)
                            multiObservation.addComponent("diastolic", diastolic)
                        }
                        else if(coding.get("code").asText() == "8480-6"){
                            systolic.setCoding(compCode)
                            systolic.setQuantity(compQuantity)
                            multiObservation.addComponent("systolic", diastolic)
                        }

                    }

                    if(multiObservation.getComponentCount() == 2){
                        caseInfo.addBloodPressureMeasurement(multiObservation)
                    }
                    else{
                        logger.warn("Found unusable blood pressure observation! " +
                                "Component count: ${multiObservation.getComponentCount()}")
                        logger.debug(components.toPrettyString())
                    }
                    break
            }
        }
    }

    private void extractMedicationStatement(JsonNode medicationNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        SMedicationStatement medication = new SMedicationStatement()

        //Id
        medication.setId(medicationNode.get("id").asText())

        JsonNode medCoding = medicationNode.get("medicationCodeableConcept").get("coding").get(0)
        medication.setCode(medCoding.get("code").asText())
        medication.setSystem(medCoding.get("system").asText())
        medication.setSystem(medCoding.get("display").asText())

        Date effective = DateManipulation.dateFromSyntheaDate(medicationNode.get("effectiveDateTime").asText())
        medication.setEffective(effective)

        if(dateChecker.confirmDate(effective, category)){
            switch (category){
                case GeccoCategory.COVID_19_THERAPY:
                    caseInfo.addCovid19Therapy(medication)
                    break
                case GeccoCategory.ACE_INHIBITORS:
                    caseInfo.addAceInhibitor(medication)
                    break
                case GeccoCategory.IMMUNOGLOBULINS:
                    caseInfo.addImmunoglobulin(medication)
                    break
                case GeccoCategory.ANTICOAGULATION:
                    caseInfo.addAnticoagulationMed(medication)
                    break
            }
        }
    }

    private void extractEncounter(JsonNode encounterNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        SEncounter encounter = new SEncounter()

        //Id
        encounter.setId(encounterNode.get("id").asInt())

        JsonNode period = encounterNode.get("period")
        Date start = DateManipulation.dateFromSyntheaDate(period.get("start").asText())
        Date end = DateManipulation.dateFromSyntheaDate(period.get("end").asText())
        encounter.setStart(start)
        encounter.setEnd(end)

        if(dateChecker.confirmDates(start, end, category)){
            switch (category){
                case GeccoCategory.ICU_ADMISSION:
                    caseInfo.setPresenceInIU(encounter)
                    break
                case GeccoCategory.HOSP_ADMISSION:
                    caseInfo.setHospAdmission(encounter)
                    break
            }
        }
    }

    private void extractDiagnosticReport(JsonNode diagnosticReportNode, CaseInformation caseInfo, GeccoCategory category, ResourceDateChecker dateChecker){
        SDiagnosticReport diagnosticReport = new SDiagnosticReport()

        //Id
        diagnosticReport.setId(diagnosticReportNode.get("id").asText())

        //Coding
        //TODO: Create universal method for extracting Codings
        JsonNode diagRedCoding = diagnosticReportNode.get("code").get("coding").get(0)
        Coding coding = new Coding()
        coding.setCode(diagRedCoding.get("code").asText())
        coding.setSystem(diagRedCoding.get("system").asText())
        coding.setSystem(diagRedCoding.get("display").asText())
        diagnosticReport.setCoding(coding)

        //Effective
        Date effective = DateManipulation.dateFromSyntheaDate(diagnosticReportNode.get("effectiveDateTime").asText())
        diagnosticReport.setEffective(effective)

        //Reference string pattern 'urn:uuid:<<id>>'
        String encRef = diagnosticReportNode.get("encounter").get("reference").asText().substring(9)

        //Add members
        JsonNode members = diagnosticReportNode.get("result")
        for(JsonNode member : members){
            diagnosticReport.addMember(member.get("reference").asText())
        }

        if(dateChecker.confirmDate(effective, category)){
            switch (category){
                case GeccoCategory.BLOOD_GAS_PANEL:
                    if((encRef == caseInfo.getHospAdmission()?.getId()) || encRef == caseInfo.getPresenceInIU()?.getId()){
                        caseInfo.addBloodGasPanel(diagnosticReport)
                    }
                    break
            }
        }
    }

    private void extractImmunization(JsonNode immunizationNode, CaseInformation caseInfo, ResourceDateChecker dateChecker){
        SImmunization immunization = new SImmunization()

        def category = GeccoCategory.IMMUNIZATION_STATUS

        //Id
        immunization.setId(immunizationNode.get("id").asText())

        //Coding
        //TODO: Create universal method for extracting Codings
        JsonNode immuCoding = immunizationNode.get("vaccineCode").get("coding").get(0)
        Coding coding = new Coding()
        coding.setCode(immuCoding.get("code").asText())
        coding.setSystem(immuCoding.get("system").asText())
        coding.setSystem(immuCoding.get("display").asText())
        immunization.setVaccineCode(coding)

        //Effective
        Date occurence = DateManipulation.dateFromSyntheaDate(immunizationNode.get("occurrenceDateTime").asText())
        immunization.setOccurence(occurence)

        //Primary source
        immunization.setPrimarySource(immunizationNode.get("primarySource").asBoolean(false))

        if(dateChecker.confirmDate(occurence, category)){
            caseInfo.addImmunization(immunization)
        }
    }

    private HashMap<SDiagnosticReport, List<SObservation>> matchObservationsToDiagnosticReports(List<SDiagnosticReport> reportList, List<SObservation> observations){
        def reportMap = new HashMap<SDiagnosticReport, List<SObservation>>()
        def obsMap = new HashMap<String, SObservation>()

        //Fill observation map
        observations.each {observation ->
            obsMap.put(observation.getId(), observation)
        }

        //Fill report Map
        reportList.each {report ->
            def obsList = new ArrayList<SObservation>()
            report.getMembers().each {memberId ->
                obsList.add(obsMap[memberId])
            }
            reportMap.put(report, obsList)
        }

        return reportMap
    }

}
