package org.uzl.syntheagecco.analysis.module

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.uzl.syntheagecco.analysis.module.state.Coding
import org.uzl.syntheagecco.analysis.module.state.CodingState
import org.uzl.syntheagecco.analysis.module.state.State
import org.uzl.syntheagecco.analysis.module.state.StateType

class ModuleParser {

    private final ObjectMapper objectMapper

    ModuleParser(){
        this.objectMapper = new ObjectMapper()
    }

    ModuleParserResponse parseAll(List<File> files){
        def response = new ModuleParserResponse()
        files.each {file ->
            response.addAllStates(parse(file))
        }
        return response
    }

    List<State> parse(File file){
        def states = []
        def root = objectMapper.readTree(file)
        def origin = root.get("name").asText()
        root.get("states").each {state ->
            states << parseState(state, origin)
        }
        return states
    }

    private State parseState(JsonNode state, String origin){
        def type = StateType.forName(state.get("type").asText())
        def jsonCodings = state.get("codes")
        def codings = new ArrayList<Coding>()
        if(jsonCodings != null){
            jsonCodings.each {coding ->
                codings << new Coding(
                        coding.get("system").asText(),
                        coding.get("code").asText(),
                        coding.get("display").asText()
                )
            }
            return new CodingState(type, origin, codings)
        }
        return new State(type, origin)
    }

}
