package syntheagecco.analysis.query

import syntheagecco.analysis.module.state.CodingState
import syntheagecco.analysis.module.state.State
import syntheagecco.analysis.module.state.StateType

import java.util.function.Supplier
import java.util.stream.Stream

class StateQuery<T extends State, U extends StateQuery> extends Query<T, U>{

    StateQuery(Stream<State> states){
        super({states}, State.class as Class<T>, StateQuery.class as Class<U>)
    }

    StateQuery(Supplier<Stream<State>> states){
        super(states, State.class as Class<T>, StateQuery.class as Class<U>)
    }

    StateQuery(Stream<T> states, Class<T> classT, Class<U> classU){
        super({states}, classT, classU)
    }

    StateQuery(Supplier<Stream<T>> states, Class<T> classT, Class<U> classU){
        super(states, classT, classU)
    }

    StateQuery filterByType(StateType type){
        return filter {state -> state.type == type}
    }

    StateQuery filterByType(StateType... types){
        return filter {state -> types.contains(state.type)}
    }

    StateQuery filterByOrigin(String origin){
        return filter {state -> state.origin == origin}
    }

    StateQuery filterByOrigin(String... origins){
        return filter {state -> origins.contains(state.origin)}
    }

}
