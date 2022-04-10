package org.uzl.syntheagecco.extraction.mapping.snomed

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.uzl.syntheagecco.extraction.GeccoCategory
import org.uzl.syntheagecco.extraction.mapping.MultiSetMap
import org.uzl.syntheagecco.utility.FileManipulation

import java.nio.file.Path

class SnomedMapper {

    private static final Logger logger = LogManager.getLogger(SnomedMapper.class)
    private File source
    private HashMap<GeccoCategory, MultiSetMap<String, String>> codeMap
    private MultiSetMap<String, GeccoCategory> categoryMap

    SnomedMapper(File source){
        logger.info("[#] Initializing SNOMED-CT mapper ...")
        this.codeMap = new HashMap<>()
        this.categoryMap = new MultiSetMap<>()
        this.source = source
        init()
        logger.debug("[#] Done.")
    }

    private void init(){
        def objectMapper = new ObjectMapper()
        def sourceDir = Path.of("src", "main", "resources", "maps", "snomed", "autogen").toString()
        def files = FileManipulation.getFilesInDirRecursive(sourceDir, FileManipulation.FileExtension.JSON)

        initCategoryMap(files, objectMapper)
        initCodeMap(files, objectMapper)
    }

    private void initCodeMap(List<File> files, ObjectMapper objectMapper){
        files.each {file ->
            def root = objectMapper.readTree(file)
            def category = GeccoCategory.findCategory(root.get("category").asInt())
            def setMap = new MultiSetMap<String, String>()

            def contains = root.get("contains")
            for(int i = 0; i < contains.size(); i++){
                def entryNode = contains.get(i)

                //TODO: remake maps so that the " symbol is not included from the json message
                def valueCode = entryNode.get("code").asText().replaceAll("\\\"", "")
                def childrenOrSelf = entryNode.get("childrenOrSelf")
                for(int j = 0; j < childrenOrSelf.size(); j++){
                    def node = childrenOrSelf.get(j)

                    def keyCode = node.get("code").asText()
                    setMap.put(keyCode, valueCode)
                }
            }

            this.codeMap.put(category, setMap)
        }
    }

    private void initCategoryMap(List<File> files, ObjectMapper objectMapper){
        files.each {file ->
            logger.debug(file.getName())
            def root = objectMapper.readTree(file)
            def category = GeccoCategory.findCategory(root.get("category").asInt())

            def contains = root.get("contains")
            for(int i = 0; i < contains.size(); i++){
                def entryNode = contains.get(i)

                def childrenOrSelf = entryNode.get("childrenOrSelf")
                for(int j = 0; j < childrenOrSelf.size(); j++){
                    def node = childrenOrSelf.get(j)

                    def code = node.get("code").asText()
                    this.categoryMap.put(code, category)
                }
            }
        }
    }

    /**
     * Categorizes SNOMED-CT code by returning a GeccoCategory Enum value which corresponds to a GECCO profile
     * @param code SNOMED-CT code to be categorized
     * @return corresponding GeccoCategory Enum value or null if no match has been found or the code can be categorized
     * to more than one category
     */
    GeccoCategory categorizeCode(String code){
        def categories = this.categoryMap[code]
        if(categories?.size() == 1) {
            return categories.collect()[0]
        }
        else {
            return null
        }
    }

    /**
     * Gets SNOMED-CT code from a value set defined for the category for a given SNOMED-CT code and GeccoCategory Enum
     * value
     * @param code SNOMED-CT code
     * @param category GeccoCategory Enum value
     * @return SNOMED-CT code string or null if one of the following is true:
     *      1) There is no matching code in the expanded value set meaning that the given category is wrong
     *      2) There are more than one possible matching codes and none of those is the given code string itself
     */
    String getCodeForCategorizedCode(String code, GeccoCategory category){
        def map = this.codeMap[category]
        def codes = map[code]
        if(codes?.size() == 1){
            return codes.collect()[0]
        }
        else if(codes?.contains(code)){
            return code
        }
        else{
            return null
        }
    }

}
