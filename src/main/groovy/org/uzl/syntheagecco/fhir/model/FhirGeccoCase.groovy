package org.uzl.syntheagecco.fhir.model

import groovy.transform.TypeChecked
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Consent
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.MedicationStatement
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Procedure

class FhirGeccoCase {

    private Bundle bundle
    private Patient patient
    private Consent consent
    private List<Condition> conditions
    private List<Procedure> procedures
    private List<Observation> observations
    private List<MedicationStatement> medicationStatements
    private List<Immunization> immunizations

    FhirGeccoCase(){
        this.conditions = new ArrayList<>()
        this.procedures = new ArrayList<>()
        this.observations = new ArrayList<>()
        this.medicationStatements = new ArrayList<>()
        this.immunizations = new ArrayList<>()
    }

    Bundle getBundle() {
        return bundle
    }

    void setBundle(Bundle bundle) {
        this.bundle = bundle
    }

    Patient getPatient() {
        return patient
    }

    void setPatient(Patient patient) {
        this.patient = patient
    }

    Consent getConsent() {
        return consent
    }

    void setConsent(Consent consent) {
        this.consent = consent
    }

    List<Condition> getConditions() {
        return conditions
    }

    void setConditions(List<Condition> conditions) {
        this.conditions = conditions
    }

    void addCondition(Condition condition){
        this.conditions.add(condition)
    }

    List<Procedure> getProcedures() {
        return procedures
    }

    void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures
    }

    void addProcedure(Procedure procedure){
        this.procedures.add(procedure)
    }

    List<Observation> getObservations() {
        return observations
    }

    void setObservations(List<Observation> observations) {
        this.observations = observations
    }

    @TypeChecked
    void addObservation(Observation observation){
        this.observations.add(observation)
    }

    List<MedicationStatement> getMedicationStatements() {
        return medicationStatements
    }

    void setMedicationStatements(List<MedicationStatement> medicationStatements) {
        this.medicationStatements = medicationStatements
    }

    void addMedicationStatement(MedicationStatement medicationStatement){
        this.medicationStatements.add(medicationStatement)
    }

    List<Immunization> getImmunizations() {
        return immunizations
    }

    void setImmunizations(List<Immunization> immunizations) {
        this.immunizations = immunizations
    }

    void addImmunization(Immunization immunization){
        this.immunizations.add(immunization)
    }

}
