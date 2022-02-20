import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode
import syntheagecco.extraction.mapping.MultiListMap
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode

class GenerateOpenEhrMaps {

    static void main(String[] args){

        def objectMapper = new ObjectMapper()
        def dir = new File("src/main/resources/maps/openehr_category")
        def categoryCodes = new MultiListMap<String, NameDesProblemsDerDiagnoseDefiningCode>()

        println "[#]Querying descendants using ECL ..."

        /*
        NameDesProblemsDerDiagnoseDefiningCode.values().each {value ->
            def encodedCode = URLEncoder.encode(value.getCode(), "UTF-8")
            def get = (HttpURLConnection) new URL("https://ontoserver.imi.uni-luebeck.de/fhir/ValueSet/" +
                    "\$expand?url=http://snomed.info/sct?fhir_vs=ecl/(%3C%3C+${encodedCode})").openConnection()
            get.setRequestProperty("Accept", "application/json")

            if(get.responseCode == 200){
                def body =  get.inputStream.getText("UTF-8")
                def json = objectMapper.readTree(body)
                def codes = json.get("expansion").get("contains")

                def root = objectMapper.createObjectNode().tap {
                    set("system", new TextNode(value.getTerminologyId()))
                    set("code", new TextNode(value.getCode()))
                    set("description", new TextNode(value.getDescription()))
                    set("children", codes)
                }
                def writer = objectMapper.writerWithDefaultPrettyPrinter()
                writer.writeValue(new File(dir.getCanonicalPath() + "/diagnosis_category/${value.toString().toLowerCase()}.json"), root)

                //For later statistics
                if(codes != null){
                    for(int i = 0; i < codes.size(); i++){
                        def code = codes.get(i).get("code").asText()
                        categoryCodes.put(code, value)
                    }
                }
            }
            else{
                println get.errorStream.getText("UTF-8")
            }
        }
        */

        NameDerProzedurDefiningCode.values().each {value ->
            def encodedCode = URLEncoder.encode(value.getCode(), "UTF-8")
            def get = (HttpURLConnection) new URL("https://ontoserver.imi.uni-luebeck.de/fhir/ValueSet/" +
                    "\$expand?url=http://snomed.info/sct?fhir_vs=ecl/(%3C%3C+${encodedCode})").openConnection()
            get.setRequestProperty("Accept", "application/json")

            if(get.responseCode == 200){
                def body =  get.inputStream.getText("UTF-8")
                def json = objectMapper.readTree(body)
                def codes = json.get("expansion").get("contains")

                def root = objectMapper.createObjectNode().tap {
                    set("system", new TextNode(value.getTerminologyId()))
                    set("code", new TextNode(value.getCode()))
                    set("description", new TextNode(value.getDescription()))
                    set("children", codes)
                }
                def writer = objectMapper.writerWithDefaultPrettyPrinter()
                writer.writeValue(new File(dir.getCanonicalPath() + "/procedure_category/${value.toString().toLowerCase()}.json"), root)

            }
            else{
                println get.errorStream.getText("UTF-8")
            }
        }

        //Statistics
        def duplicates = new ArrayList<Map.Entry<String, List<NameDesProblemsDerDiagnoseDefiningCode>>>()
        categoryCodes.all.each {entry ->
            def values = entry.value
            if(values.size() > 1){
                duplicates << entry
            }
        }

        if(duplicates.size() == 0){
            println "[#]No categories are overlapping."
        }
        else{
            println "[#]There are overlapping categories: "
            println "   ${duplicates.size} affected codes"
            duplicates.each { Map.Entry<String, List<NameDesProblemsDerDiagnoseDefiningCode>> entry ->
                println "   ${entry.key}: ${entry.value.join(", ")}"
            }
        }
    }

}
