package syntheagecco.extraction

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger;

class ResourceDateChecker {

    private final static Logger logger = LogManager.getLogger(ResourceDateChecker.class)

    private final Date onsetDateTime
    private final Date abatementDateTime

    ResourceDateChecker(Date onsetDateTime, Date abatementDateTime){
        this.onsetDateTime = onsetDateTime
        this.abatementDateTime = abatementDateTime
    }

    boolean confirmDates(Date start, Date end, GeccoCategory resourceCategory){
        switch(resourceCategory){
            case GeccoCategory.CHRONIC_LUNG_DISEASE:
            case GeccoCategory.CARDIOVASCULAR_DISEASE:
            case GeccoCategory.CHRONIC_LIVER_DISEASE:
            case GeccoCategory.RHEUMATOLOGICAL_IMMUNOLOGICAL_DISEASE:
            case GeccoCategory.HIV_INFECTION:
            case GeccoCategory.TISSUE_ORGAN_RECIPIENT:
            case GeccoCategory.DIABETES_MELLITUS:
            case GeccoCategory.MALIGNANT_NEOPLASTIC_DISEASE:
            case GeccoCategory.TOBACCO_SMOKING_STATUS:
            case GeccoCategory.CHRONIC_NEURO_MENTAL_DISEASE:
            case GeccoCategory.RESPIRATORY_THERAPY:
            case GeccoCategory.CHRONIC_KIDNEY_DISEASE:
            case GeccoCategory.GASTROINTESTINAL_ULCERS:
            case GeccoCategory.RESUSCITATION_STATUS:
            case GeccoCategory.PREGNANCY:
                return start.before(onsetDateTime) && (end == null || end.after(onsetDateTime))
            case GeccoCategory.COMPLICATIONS:
            case GeccoCategory.SYMPTOM:
            case GeccoCategory.DIALYSIS_HEMOFILTRATION:
            case GeccoCategory.APHERESIS:
            case GeccoCategory.PRONE_POSITION:
            case GeccoCategory.ICU_ADMISSION:
            case GeccoCategory.HOSP_ADMISSION:
            case GeccoCategory.VENTILATION_TYPE:
                return (start == onsetDateTime || start.after(onsetDateTime)) &&
                        (end == null || end.before(abatementDateTime) || end == abatementDateTime)
            case GeccoCategory.BODY_HEIGHT:
            case GeccoCategory.BODY_WEIGHT:
                return start.before(abatementDateTime) &&
                        (end == null || end.before(abatementDateTime) || end == abatementDateTime)
            default:
                logger.warn("GeccoCategory '${resourceCategory.toString()}' has yet to be assigned to a fitting date " +
                        "interval!")
                return false
        }
    }

    boolean confirmDate(Date performed, GeccoCategory resourceCategory){
        switch (resourceCategory){
            case GeccoCategory.LABORATORY_VALUE:
            case GeccoCategory.PCR_TEST:
            case GeccoCategory.ANTIBODY_TEST:
            case GeccoCategory.PACO2:
            case GeccoCategory.PAO2:
            case GeccoCategory.FIO2:
            case GeccoCategory.RESPIRATORY_RATE:
            case GeccoCategory.BLOOD_PRESSURE:
            case GeccoCategory.HEART_RATE:
            case GeccoCategory.BODY_TEMPERATURE:
            case GeccoCategory.OXYGEN_SATURATION:
            case GeccoCategory.PH:
            case GeccoCategory.COVID_19_THERAPY:
            case GeccoCategory.ACE_INHIBITORS:
            case GeccoCategory.IMMUNOGLOBULINS:
            case GeccoCategory.ANTICOAGULATION:
            case GeccoCategory.BLOOD_GAS_PANEL:
                return performed.after(onsetDateTime) &&
                        (performed.before(abatementDateTime) || performed == abatementDateTime)
            case GeccoCategory.BODY_HEIGHT:
            case GeccoCategory.BODY_WEIGHT:
                return performed.before(abatementDateTime)
            case GeccoCategory.TOBACCO_SMOKING_STATUS:
            case GeccoCategory.IMMUNIZATION_STATUS:
                return performed.before(onsetDateTime)
            default:
                logger.warn("GeccoCategory '${resourceCategory.toString()}' has yet to be assigned to a fitting date " +
                        "interval!")
                return false
        }
    }

}
