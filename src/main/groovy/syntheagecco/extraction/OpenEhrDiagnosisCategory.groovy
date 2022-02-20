package syntheagecco.extraction

enum OpenEhrDiagnosisCategory {

    PULMONARY_MEDICINE(0), VASULAR_MEDICINE(1), HEPATOLOGY(2), RHEUMATOLOGY(3),
    IMMUNOLOGY(4), INFECTIOUS_DISEASE(5), DIABETIC_MEDICINE(6), ONCOLOGY(7),
    GASTROENTOLOGY(8), TRANSPLANT_MEDICINE(9), COMPLICATION(10), NEUROLOGY(11),
    PSYCHIATRY(12), NEPHROLOGY(13), VENTILATION_STATUS(14)

    private int code

    private static final Map<Integer, OpenEhrDiagnosisCategory> lookup = new HashMap<>()

    static {
        values().each {value ->
            lookup[value.code] = value
        }
    }

    OpenEhrDiagnosisCategory(int code){
        this.code = code
    }

    int getCode(){
        return code
    }

    static OpenEhrDiagnosisCategory findCategory(int code){
        return lookup[code]
    }

}