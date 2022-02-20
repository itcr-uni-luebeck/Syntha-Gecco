package syntheagecco.analysis.module.state

class State {

    private StateType type
    private String origin

    State(StateType type, String origin){
        this.type = type
        this.origin = origin
    }

    StateType getType() {
        return type
    }

    void setType(StateType type) {
        this.type = type
    }

    String getOrigin() {
        return origin
    }

    State setOrigin(String origin) {
        this.origin = origin
        return this
    }

}
