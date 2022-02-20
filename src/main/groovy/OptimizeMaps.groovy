import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import groovy.io.FileType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneOffset

class OptimizeMaps {

    private static final ObjectMapper objectMapper = new ObjectMapper()
    private static final Logger logger = LogManager.getLogger(OptimizeMaps.class)

    static void main(String[] args){

        //Get JSON files
        logger.debug("Searching for JSON files ...")
        def jsonFiles = []
        def dir = new File("optimize")
        dir.traverse([type: FileType.FILES, nameFilter: ~/^.*\.json$/]){
            File jsonFile -> jsonFiles << jsonFile
                logger.debug("Found JSON file: '${jsonFile.getName()}'")
        }

        HashSet<String> codeSet = new HashSet<>()
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        def outputFolder = new File("src\\main\\resources\\output\\maps\\${currentTime}")
        if(!outputFolder.exists()){
            outputFolder.mkdirs()
        }

        jsonFiles.each { File jsonFile ->
            logger.debug("[#]Processing '${jsonFile.getName()}'")
            //Create new JSON tree
            ObjectNode json = objectMapper.createObjectNode()
            ArrayNode codes = objectMapper.createArrayNode()
            json.set("codes", codes)
            try{
                JsonNode node = objectMapper.readTree(jsonFile)
                ArrayNode codeEntries = node.get("contains")
                def linesRemoved = 0, entriesRemoved = 0
                if(codeEntries.isArray()){
                    for(int i = 0; i < codeEntries.size(); i++){
                        ObjectNode entry = codeEntries.get(i)
                        if(codeSet.contains(entry.get("code").asText())){
                            entriesRemoved++
                            linesRemoved += 5
                        }
                        else{
                            //Remove only system and display information since they are unnecessary
                            JsonNode codeEntry = objectMapper.createObjectNode()
                            codeEntry.set("code", entry.get("code"))
                            codes.add(codeEntry)
                            codeSet.add(entry.get("code").asText())
                            linesRemoved += 2
                        }
                    }
                }
                logger.debug("Removed ${entriesRemoved} entries.")
                logger.debug("Removed ${linesRemoved} lines total.")
                def resultFileName = "${outputFolder.getAbsolutePath()}\\${jsonFile.getName()}"
                try{
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(resultFileName).toFile(), json)
                }
                catch (Exception exc){
                    logger.error("Couldn't generate resulting JSON file '${resultFileName}'!")
                }
            }
            catch (Exception exc){
                logger.error("JSON file '${jsonFile.getName()}' couldn't be converted to JsonNode!")
            }
        }

    }

}
