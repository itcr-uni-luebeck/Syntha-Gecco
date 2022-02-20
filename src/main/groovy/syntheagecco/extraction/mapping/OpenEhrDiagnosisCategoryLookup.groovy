package syntheagecco.extraction.mapping

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.extraction.OpenEhrDiagnosisCategory
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode
import syntheagecco.utility.FileManipulation

class OpenEhrDiagnosisCategoryLookup extends Lookup<NameDesProblemsDerDiagnoseDefiningCode, OpenEhrDiagnosisCategory>{

    private static final Logger logger = LogManager.getLogger(OpenEhrDiagnosisCategoryLookup.class)

    OpenEhrDiagnosisCategoryLookup(){
        super({
            logger.info("[#]Creating openEhr diagnosis category lookup ...")
            def mapper = new ObjectMapper()
            def codeMap = new HashMap<NameDesProblemsDerDiagnoseDefiningCode, OpenEhrDiagnosisCategory>()
            def mapSourceDir = "src/main/resources/maps/diagnosis_to_diagnosis_category"

            //Get all JSON files in the directories
            def mapFiles = FileManipulation.getFilesInDirRecursive(mapSourceDir, FileManipulation.FileExtension.JSON)

            //Map snomed codes to enum values
            def codeToDiagNameMap = new HashMap<String, NameDesProblemsDerDiagnoseDefiningCode>()
            NameDesProblemsDerDiagnoseDefiningCode.values().each {value ->
                codeToDiagNameMap[value.getCode()] = value
            }

            if(mapFiles.size().is(0)) {
                logger.error("No JSON files in parent directory '${mapSourceDir}'!")
                throw new Exception("No JSON files could be found in '${mapSourceDir}'")
            }
            else {
                //Read JSON files
                logger.debug("Reading JSON files containing codes...")
                mapFiles.each { mapFile ->
                    try {
                        JsonNode jsonRoot = mapper.readTree(mapFile)
                        JsonNode categories = jsonRoot.get("diagnosisCategories")
                        for(int i = 0; i < categories.size(); i++){
                            def category = categories.get(i)
                            def codes = category.get("codes")
                            def currentCategory = OpenEhrDiagnosisCategory.findCategory(category.get("categoryCode").asInt())
                            if (!codes.isNull()) {
                                if (codes != null && codes.isArray()) {
                                    for (int j = 0; j < codes.size(); j++) {
                                        JsonNode codeEntry = codes.get(j)
                                        def diagName = codeToDiagNameMap[codeEntry.get("code").asText()]
                                        codeMap.put(diagName, currentCategory)
                                    }
                                } else {
                                    throw new Exception("'codes' JSON object in JSON file '${mapFile.getAbsolutePath()}' could not " +
                                            "be converted to JSON array!")
                                }
                            } else {
                                logger.warn("There are no codes for diagnosis name category '${currentCategory.toString()}'.")
                            }
                        }
                    }
                    catch (Exception exc) {
                        logger.error("An error occurred during the creation of the code lookup!" +
                                "\nMessage:    ${exc.getMessage()}" +
                                "\nStacktrace: ${ExceptionUtils.getStackTrace(exc)}")
                    }
                }
                logger.debug("Total entry count: ${codeMap.size()}")
                return codeMap
            }
        }.call())
    }

}
