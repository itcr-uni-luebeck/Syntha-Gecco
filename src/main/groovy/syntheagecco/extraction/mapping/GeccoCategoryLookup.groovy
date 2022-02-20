package syntheagecco.extraction.mapping

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.extraction.GeccoCategory
import syntheagecco.utility.FileManipulation

abstract class GeccoCategoryLookup extends Lookup<String, GeccoCategory> {

    private final static Logger logger = LogManager.getLogger(GeccoCategoryLookup.class)

    GeccoCategoryLookup(String mapSourceDir){
        super({
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
        }.call())
    }

}
