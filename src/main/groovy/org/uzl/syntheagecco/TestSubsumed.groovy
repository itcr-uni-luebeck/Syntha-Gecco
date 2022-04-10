package org.uzl.syntheagecco

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class TestSubsumed {

    static void main(String[] args){

        def xmlMapper = new XmlMapper()
        def xmlParser = new XmlParser()

        def codesA = [128283000, 74732009, 197480006, 35489007, 69322001, 49049000, 52448006, 24700007, 257277002, 84757009, 37796009, 440140008, 429993008]
        def codesB = [128283000, 74732009, 49049000, 24700007, 257277002, 84757009, 37796009, 440140008, 429993008]

        codesB.each {codeB ->
            codesA.each {codeA ->
                ((HttpURLConnection) new URL("https://ontoserver.imi.uni-luebeck.de/fhir/CodeSystem/" +
                        "\$subsumes?system=http://snomed.info/sct&codeA=${codeA}&codeB=${codeB}").openConnection()).with({
                    requestMethod = "GET"
                    doOutput = true

                    if(errorStream){
                        println errorStream.getText("UTF-8")
                    }
                    else{
                        //println(inputStream.getText("UTF-8"))
                        def response = xmlParser.parse(inputStream)
                        println response
                        /*
                        def value = response.get("parameter").get(0).get("valueCode").asText()
                        def subsumedBy = value != "not-subsumed"
                        if (!subsumedBy){
                            println("${codeA} not subsumed by ${codesB}")
                        }
                         */
                    }
                })
                //println new URL("https://ontoserver.imi.uni-luebeck.de/fhir/CodeSystem/" +
                //        "\$subsumes?system=http://snomed.info/sct&codeA=${codeA}&codeB=${codeB}").text
            }
        }

    }

}
