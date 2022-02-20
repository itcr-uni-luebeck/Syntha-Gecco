package syntheagecco.analysis.module

import syntheagecco.analysis.module.state.CodingState
import syntheagecco.analysis.module.state.State
import syntheagecco.analysis.module.state.StateType
import syntheagecco.analysis.query.Query
import syntheagecco.analysis.query.Queryable
import syntheagecco.analysis.query.StateQuery

import java.util.function.Supplier

class ModuleParserResponse implements Queryable<StateQuery> {

    private List<State> states

    ModuleParserResponse(){
        this.states = new ArrayList<>()
    }

    protected void addState(State state){
        this.states << state
    }

    protected void addAllStates(List<State> states){
        this.states.addAll(states)
    }

    List<State> getStates(){
        return this.states
    }

    List<State> getStates(StateType type){
        return states.parallelStream()
                .filter({ state -> state.type == type })
                .collect()
    }

    List<CodingState> getCodingStates(){
        return states.parallelStream()
                .filter({state -> state instanceof CodingState})
                .collect()
    }

    List<State> getStatesForModule(String name){
        return states.parallelStream()
                .filter({state -> state.origin == name})
                .collect()
    }

    @Override
    StateQuery query() {
        return new StateQuery({this.states.stream()})
    }

}
