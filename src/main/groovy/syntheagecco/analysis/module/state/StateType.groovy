package syntheagecco.analysis.module.state

enum StateType {

    INITIAL("Initial"), TERMINAL("Terminal"), SIMPLE("Simple"), MEDICATIONORDER("MedicationOrder"),
    ENCOUNTER("Encounter"), ENOUNTEREND("EncounterEnd"), CONDITIONONSET("ConditionOnset"),
    CONDITIONEND("ConditionEnd"), CALLSUBMODULE("CallSubmodule"), DEATH("Death"), DELAY("Delay"),
    CAREPLANSTART("CarePlanStart"), CAREPLANEND("CarePlanEnd"), DIAGNOSTICREPORT("DiagnosticReport"),
    OBSERVATION("Observation"), PROCEDURE("Procedure"), VITALSIGN("VitalSign"), SUPPLYLIST("SupplyList"),
    SETATTRIBUTE("SetAttribute"), MULTIOBSERVATION("MultiObservation"), COUNTER("Counter"),
    DEVICE("Device"), DEVICEEND("DeviceEnd"),

    private String name
    private static final HashMap<String, StateType> map = createMap()

    private StateType(String name){
        this.name = name
    }

    static forName(String name){
        return map[name]
    }

    private static HashMap<String, StateType> createMap(){
        HashMap<String, StateType> map = new HashMap<>()
        values().each {map[it.name] = it}
        return map
    }

}