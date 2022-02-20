package syntheagecco.extraction.model


import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType

import java.time.Period

class SPatient extends BaseResource {

    private Coding ethnicGroup
    private Date birthDate
    private Period age
    private Date ageRecordedDate
    private String assignedSex
    private String family
    private String given

    SPatient(){}

    Coding getEthnicGroup() {
        return ethnicGroup
    }

    void setEthnicGroup(Coding ethnicGroup) {
        this.ethnicGroup = ethnicGroup
    }

    Date getBirthDate() {
        return birthDate
    }

    void setBirthDate(Date birthDate) {
        this.birthDate = birthDate
    }

    Period getAge() {
        return age
    }

    void setAge(Period age) {
        this.age = age
    }

    Date getAgeRecordedDate() {
        return ageRecordedDate
    }

    void setAgeRecordedDate(Date ageRecordedDate) {
        this.ageRecordedDate = ageRecordedDate
    }

    String getAssignedSex() {
        return assignedSex
    }

    void setAssignedSex(String assignedSex) {
        this.assignedSex = assignedSex
    }

    String getFamily() {
        return family
    }

    void setFamily(String family) {
        this.family = family
    }

    String getGiven() {
        return given
    }

    void setGiven(String given) {
        this.given = given
    }

}
