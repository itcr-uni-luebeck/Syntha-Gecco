package org.uzl.syntheagecco.extraction

enum GeccoCategory {

    CHRONIC_LUNG_DISEASE(0), CARDIOVASCULAR_DISEASE(1), CHRONIC_LIVER_DISEASE(2),
    RHEUMATOLOGICAL_IMMUNOLOGICAL_DISEASE(3), HIV_INFECTION(4), TISSUE_ORGAN_RECIPIENT(5),
    DIABETES_MELLITUS(6), MALIGNANT_NEOPLASTIC_DISEASE(7), TOBACCO_SMOKING_STATUS(8),
    CHRONIC_NEURO_MENTAL_DISEASE(9), RESPIRATORY_THERAPY(10), CHRONIC_KIDNEY_DISEASE(11),
    GASTROINTESTINAL_ULCERS(12), IMMUNIZATION_STATUS(13), RESUSCITATION_STATUS(14),
    COMPLICATIONS(15), PREGNANCY(16), SYMPTOM(17), DIALYSIS_HEMOFILTRATION(18),
    APHERESIS(19), PRONE_POSITION(20), BODY_WEIGHT(21), BODY_HEIGHT(22),
    LABORATORY_VALUE(23), PCR_TEST(24), ANTIBODY_TEST(25), PACO2(26),
    PAO2(27), FIO2(28), RESPIRATORY_RATE(29), BLOOD_PRESSURE(30),
    HEART_RATE(32), BODY_TEMPERATURE(33),
    OXYGEN_SATURATION(34), PH(35), COVID_19_THERAPY(36), ACE_INHIBITORS(37),
    IMMUNOGLOBULINS(38), ANTICOAGULATION(39), ICU_ADMISSION(40), HOSP_ADMISSION(41),
    VENTILATION_TYPE(42), BLOOD_GAS_PANEL(43)

    private final int categoryIndex

    private static final Map<Integer, GeccoCategory> lookup = new HashMap<>()

    static {
        values().each {value ->
            lookup[value.categoryIndex] = value
        }
    }

    private GeccoCategory(int categoryIndex){
        this.categoryIndex = categoryIndex
    }

    int getCategoryIndex(){
        return this.categoryIndex
    }

    static GeccoCategory findCategory(int categoryIndex){
        return lookup[categoryIndex]
    }

}