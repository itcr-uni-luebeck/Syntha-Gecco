package org.uzl.syntheagecco.analysis.module

import org.uzl.syntheagecco.analysis.module.state.CodingState
import org.uzl.syntheagecco.analysis.module.state.State
import org.uzl.syntheagecco.analysis.module.state.StateType
import org.uzl.syntheagecco.analysis.query.Queryable
import org.uzl.syntheagecco.analysis.query.StateQuery

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
