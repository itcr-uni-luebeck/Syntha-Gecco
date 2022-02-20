package syntheagecco.extraction

enum OpenEhrProcedureCategory {

    DIAGNOSTIC_PROCEDURE(0), IMAGING(1), THERAPEUTIC_PROCEDURE_PROCEDURE(2), OTHER_CATEGORY(3),
    RESPIRATORY_THERAPY_PROCEDURE(4), ARTIFICIAL_RESPIRATION_PROCEDURE(5), SURGICAL_PROCEDURE(6),
    OXYGEN_ADMINISTRATION_BY_NASAL_CANNULA_PROCEDURE(7), ADMINISTRATION_OF_MEDICINE(8),
    PROCEDURES_RELATING_TO_POSITIONING_AND_SUPPORT_PROCEDURE(9), NONINVASIVE_VENTILATION_PROCEDURE(10)

    private int code

    private static final Map<Integer, OpenEhrProcedureCategory> lookup = new HashMap<>()

    static {
        values().each {value ->
            lookup[value.code] = value
        }
    }

    OpenEhrProcedureCategory(int code){
        this.code = code
    }

    int getCode(){
        return code
    }

    static OpenEhrProcedureCategory findCategory(int code){
        return lookup[code]
    }

}