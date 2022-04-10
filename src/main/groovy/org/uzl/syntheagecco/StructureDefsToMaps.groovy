package org.uzl.syntheagecco

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.uzl.syntheagecco.utility.FileManipulation

class StructureDefsToMaps {

    private final static Logger logger = LogManager.getLogger(StructureDefsToMaps.class)

    static void main(String[] args){

        def sourceDir = "src/main/resources/fhir/temp"
        def targetDir = "src/main/resources/maps/snomed/autogen"
        def files = FileManipulation.getFilesInDirRecursive(sourceDir, FileManipulation.FileExtension.JSON)
        def objectMapper = new ObjectMapper()
        def valueSets = []

        files.each {file ->
            valueSets << getValueSet(file)
        }

        def writer = objectMapper.writerWithDefaultPrettyPrinter()
        valueSets.each {valueSet ->
            def root = buildMap(valueSet)
            writer.writeValue(new File(targetDir + "/${valueSet.getName()}.json"), root)
        }

    }

    static private ValueSet getValueSet(File file){
        def objectMapper = new ObjectMapper()
        def root = objectMapper.readTree(file)
        def valueSet = new ValueSet(root.get("name").asText())
        def compose = root.get("compose")
        logger.info("name: ${root.get("name")}")

        int index = 0

        def explValues = compose.get("include").get(index)
        def system = explValues.get("system").asText()
        def concept = explValues.get("concept")
        if(concept != null){
            for(int i = index; i < concept.size(); i++){
                def node = concept.get(i)

                def code = node.get("code").asText()
                def display = node.get("display").asText()

                valueSet.addValue(new CodeEntry(system, code, display))
                logger.info("expl code: ${code}")
            }
            index++
        }
        def implValues = compose.get("include")
        for(int i = index; i < implValues.size(); i++){
            def node = implValues.get(i)

            system = node.get("system")
            def code = node.get("filter").get(0).get("value")

            valueSet.addValue(new CodeEntry().tap {it ->
                it.system = system
                it.code = code
            })

            logger.info("impl code: ${code}")
        }

        return valueSet
    }

    static private ObjectNode buildMap(ValueSet valueSet){
        def objectMapper = new ObjectMapper()
        def mapEntries = []

        valueSet.getValues().each {value ->
            mapEntries << resolveSnomedConcept(value)
        }

        def root = objectMapper.createObjectNode().tap {it ->
            set("name", new TextNode(valueSet.getName()))
            set("contains", new ArrayNode(JsonNodeFactory.withExactBigDecimals(true)).tap {
                arrayNode -> addAll(mapEntries)
            })
        }

        return root
    }

    static private ObjectNode resolveSnomedConcept(CodeEntry codeEntry){
        def objectMapper = new ObjectMapper()
        def encodedCode = URLEncoder.encode(codeEntry.getCode(), "UTF-8").replaceAll("%22", "")
        def get = (HttpURLConnection) new URL("https://ontoserver.imi.uni-luebeck.de/fhir/ValueSet/" +
                "\$expand?url=http://snomed.info/sct?fhir_vs=ecl/(%3C%3C+${encodedCode})").openConnection()
        get.setRequestProperty("Accept", "application/json")

        if(get.responseCode == 200){
            def body =  get.inputStream.getText("UTF-8")
            def json = objectMapper.readTree(body)
            def codes = json.get("expansion").get("contains")

            def entry = objectMapper.createObjectNode().tap {
                set("system", new TextNode(codeEntry.getSystem()))
                set("code", new TextNode(codeEntry.getCode()))
                set("display", new TextNode(codeEntry.getDisplay()))
                set("childrenOrSelf", codes)
            }

            return entry
        }
        else{
            throw new RuntimeException("Request failed! Details:\n${get.errorStream.getText("UTF-8")}")
        }
    }

    static private class ValueSet {

        private String name
        private List<CodeEntry> values

        ValueSet(String name){
            this.name = name
            this.values = new ArrayList<>()
        }

        List<CodeEntry> getValues() {
            return values
        }

        void setValues(List<CodeEntry> values) {
            this.values = values
        }

        void addValue(CodeEntry value){
            this.values.add(value)
        }

        String getName() {
            return name
        }

        void setName(String name) {
            this.name = name
        }
    }

    static private class CodeEntry{

        private String system
        private String code
        private String display

        CodeEntry(){
            this.system = null
            this.code = null
            this.display = null
        }

        CodeEntry(String system, String code, String display){
            this.system = system
            this.code = code
            this.display = display
        }

        String getSystem() {
            return system
        }

        void setSystem(String system) {
            this.system = system
        }

        String getCode() {
            return code
        }

        void setCode(String code) {
            this.code = code
        }

        String getDisplay() {
            return display
        }

        void setDisplay(String display) {
            this.display = display
        }

    }

}
