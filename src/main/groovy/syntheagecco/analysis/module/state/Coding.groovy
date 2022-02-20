package syntheagecco.analysis.module.state

class Coding {

    private String system
    private String code
    private String display

    Coding(String system, String code){
        this.system = system
        this.code = code
    }

    Coding(String system, String code, String display){
        this.system = system
        this.code = code
        this.display = display

        Optional<CodingCreationError> optional = CodingCreationError.create(this)
        if(!optional.isEmpty()) throw optional.get()
    }

    String getSystem() {
        return system
    }

    Coding setSystem(String system) {
        this.system = system
        return this
    }

    String getCode() {
        return code
    }

    Coding setCode(String code) {
        this.code = code
        return this
    }

    String getDisplay() {
        return display
    }

    Coding setDisplay(String display) {
        this.display = display
        return this
    }

}
