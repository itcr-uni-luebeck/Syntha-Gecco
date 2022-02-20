package syntheagecco.openehr.sdk.client

/**
 * This enum was created using the OperationalTemplateTestData class as a template
 * Page access: 2021-08-31 19:25
 * @link https://github.com/ehrbase/openEHR_SDK/blob/develop/test-data/src/main/java/org/ehrbase/test_data/operationaltemplate/OperationalTemplateTestData.java
 */
enum OperationalTemplateData {

    ATEMFREQUENZ("Atemfrequenz", "Atemfrequenz.opt"),
    BEATMUNGSWERTE("Beatmungswerte", "Beatmungswerte.opt"),
    BEFUND_DER_BLUTGASANALYSE("Befund der Blutgasanalyse", "Befund der Blutgasanalyse.opt"),
    BLUTDRUCK("Blutdruck", "Blutdruck.opt"),
    DNR_ANORDNUNG("DNR-Anordnung", "DNR-Anordnung.opt"),
    GECCO_DIAGNOSE("GECCO_Diagnose", "GECCO_Diagnose.opt"),
    GECCO_ENTLASSUNGSDATEN("GECCO_Entlassungsdaten", "GECCO_Entlassungsdaten.opt"),
    GECCO_LABORBEFUND("GECCO_Laborbefund", "GECCO_Laborbefund.opt"),
    GECCO_MEDICATION("GECCO_Medikation", "GECCO_Medikation.opt"),
    GECCO_PERSONENDATEN("GECCO_Personendaten", "GECCO_Personendaten.opt"),
    GECCO_Prozedur("GECCO_Prozedur", "GECCO_Prozedur.opt"),
    GECCO_RADIOLOGISCHER_BEFUND("GECCO_Radiologischer Befund", "GECCO_Radiologischer Befund.opt"),
    GECCO_SEROLOGISCHER_BEFUND("GECCO_Serologischer Befund", "GECCO_Serologischer Befund.opt"),
    GECCO_STUDIENTEILNAHME("GECCO_Studienteilnahme", "GECCO_Studienteilnahme.opt"),
    GECCO_VIROLOGISCHER_BEFUND("GECCO_Virologischer Befund", "GECCO_Virologischer Befund.opt"),
    HERZFREQUENZ("Herzfrequenz", "Herzfrequenz.opt"),
    IMPFSTATUS("Impfstatus", "Impfstatus.opt"),
    KLINISCHE_FRAILTY_SKALA("Klinische Frailty-Skala", "Klinische Frailty-Skala.opt"),
    KOERPERTEMPERATUR("Koerpertemperatur", "Koerpertemperatur.opt"),
    KOERPERGEWICHT("K\u00f6rpergewicht", "Körpergewicht.opt"),
    KOERPERGROESSE("K\u00f6rperg\u00f6\u00dfe", "Körpergröße.opt"),
    PATIENT_AUF_ICU("Patient auf ICU", "Patient auf ICU.opt"),
    PULSOXYMETRIE("Pulsoxymetrie", "Pulsoxymetrie.opt"),
    RAUCHERSTATUS("Raucherstatus", "Raucherstatus.opt"),
    REISEHISTORIE("Reisehistorie", "Reisehistorie.opt"),
    SARS_COV_2_EXPOSITION("SARS-CoV-2 Exposition", "SARS-CoV-2 Exposition.opt"),
    SCHWANGERSCHAFTSSTATUS("Schwangerschaftsstatus", "Schwangerschaftsstatus.opt"),
    SOFA("SOFA", "SOFA.opt"),
    SYMPTOM("Symptom", "Symptom.opt"),

    private String templateId
    private String fileName

    OperationalTemplateData(String templateId, String fileName){
        this.templateId = templateId
        this.fileName = fileName
    }

    InputStream getStream(){
        return getClass().getResourceAsStream("/openehr/opt/${fileName}")
    }

    static OperationalTemplateData findByTemplateId(String templateId){
        return Arrays.stream(values())
                .filter({ o -> (o.getTemplateId() == templateId) })
                .findAny()
                .orElse(null)
    }

    String getTemplateId() {
        return templateId
    }

    String getFileName() {
        return fileName
    }

}
