package syntheagecco.extraction.mapping

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.extraction.OpenEhrProcedureCategory
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode
import syntheagecco.utility.FileManipulation

class OpenEhrProcedureCategoryLookup extends Lookup<NameDerProzedurDefiningCode, OpenEhrProcedureCategory>{

    private static final Logger logger = LogManager.getLogger(OpenEhrDiagnosisCategoryLookup.class)

    OpenEhrProcedureCategoryLookup(){
        super({
            logger.info("[#]Creating openEhr procdure category lookup ...")
            def mapper = new ObjectMapper()
            def codeMap = new HashMap<NameDerProzedurDefiningCode, OpenEhrProcedureCategory>()
            def mapSourceDir = "src/main/resources/maps/procedure_to_procedure_category"

            //Get all JSON files in the directories
            def mapFiles = FileManipulation.getFilesInDirRecursive(mapSourceDir, FileManipulation.FileExtension.JSON)

            //Map snomed codes to enum values
            def codeToDiagNameMap = new HashMap<String, NameDerProzedurDefiningCode>()
            NameDerProzedurDefiningCode.values().each {value ->
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
                        JsonNode categories = jsonRoot.get("procedureCategories")
                        for(int i = 0; i < categories.size(); i++){
                            def category = categories.get(i)
                            def codes = category.get("codes")
                            def currentCategory = OpenEhrProcedureCategory.findCategory(category.get("categoryCode").asInt())
                            if (!codes.isNull()) {
                                if (codes != null && codes.isArray()) {
                                    for (int j = 0; j < codes.size(); j++) {
                                        JsonNode codeEntry = codes.get(j)
                                        def procName = codeToDiagNameMap[codeEntry.get("code").asText()]
                                        codeMap.put(procName, currentCategory)
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
