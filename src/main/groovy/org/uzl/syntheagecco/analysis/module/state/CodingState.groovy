package org.uzl.syntheagecco.analysis.module.state

class CodingState extends State{

    private List<Coding> codings

    CodingState(StateType type, String origin, List<Coding> codings){
        super(type, origin)
        this.codings = codings
    }

    List<Coding> getCodings() {
        return codings
    }

    CodingState setCodings(List<Coding> codings) {
        this.codings = codings
        return this
    }

    CodingState addCoding(Coding coding){
        this.codings << coding
        return this
    }

}
