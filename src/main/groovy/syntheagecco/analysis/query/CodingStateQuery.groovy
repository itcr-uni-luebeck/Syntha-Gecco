package syntheagecco.analysis.query

import syntheagecco.analysis.module.state.Coding
import syntheagecco.analysis.module.state.CodingState

import java.util.function.Supplier
import java.util.stream.Stream

class CodingStateQuery extends StateQuery<CodingState, CodingStateQuery> {

    CodingStateQuery(Stream<CodingState> states){
        super(states, CodingState.class, CodingStateQuery.class)
    }

    CodingStateQuery(Supplier<Stream<CodingState>> states){
        super({states}, CodingState.class, CodingStateQuery.class)
    }

    CodingStateQuery(Supplier<Stream<CodingState>> states, Class<? extends CodingState> classT, Class<? extends CodingStateQuery> classU){
        super(states, classT, classU)
    }

    CodingStateQuery filterByCoding(Coding coding){
        return filterByCoding(coding.system, coding.code)
    }

    CodingStateQuery filterByCoding(String system, String code){
        return filter {state -> state.codings.any{it -> it.system == system && it.code == code}}
    }

    CodingStateQuery filterBySystem(String system){
        return filter {state -> state.codings.any{it.system == system}}
    }

    CodingStateQuery filterBySystem(String... systems){
        return filter {state -> state.codings.any{it -> systems.contains(it.system)}}
    }

    CodingStateQuery filterByCode(String code){
        return filter {state -> state.codings.any{it.code == code}}
    }

    CodingStateQuery filterByCode(String... codes){
        return filter {state -> state.codings.any{it -> codes.contains(it.code)}}
    }

    CodingStateQuery filterByDisplay(String display){
        return filter {state -> state.codings.any{it.display == display}}
    }

    CodingStateQuery filterByDisplay(String... displays){
        return filter {state -> state.codings.any{it -> displays.contains(it.display)}}
    }

}
