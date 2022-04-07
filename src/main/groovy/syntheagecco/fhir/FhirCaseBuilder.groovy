package syntheagecco.fhir

import com.fasterxml.jackson.databind.JsonNode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hl7.elm.r1.Case
import org.hl7.fhir.r4.model.Age
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Consent
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Extension
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.MedicationStatement
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Procedure
import org.hl7.fhir.r4.model.Quantity
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SPatient
import syntheagecco.fhir.model.FhirGeccoCase

class FhirCaseBuilder {

    private static final Logger logger = LogManager.getLogger(FhirCaseBuilder.class)

    private SyntheaGeccoConfig config

    FhirCaseBuilder(){}

    FhirCaseBuilder(SyntheaGeccoConfig config){
        this.config = config
    }

    SyntheaGeccoConfig getConfig() {
        return config
    }

    void setConfig(SyntheaGeccoConfig config) {
        this.config = config
    }

    FhirGeccoCase buildFhirGeccoCase(CaseInformation caseInfo){
        FhirGeccoCase geccoCase = new FhirGeccoCase()

        logger.debug("[§]Current Patient: ${caseInfo.getPatient().getId()}")
        logger.debug("Onset: ${caseInfo.getOnset()}")
        logger.debug("Abatement: ${caseInfo.getAbatement()}")

        //First: Create Patient resource
        def patient = this.buildPatientResource(caseInfo.getPatient(), caseInfo)
        geccoCase.setPatient(patient)

        //Create Consent resource instance
        geccoCase.setConsent(this.buildConsentResource(caseInfo, patient))

        //Run the FHIR builders for each category
        //TODO: Make builder methods static
        FhirAnamnesisBuilder anBuilder = new FhirAnamnesisBuilder(this.config)
        anBuilder.buildResources(caseInfo, geccoCase)

        FhirDemographicsBuilder demBuilder = new FhirDemographicsBuilder(this.config)
        demBuilder.buildResources(caseInfo, geccoCase)

        FhirComplicationsBuilder comBuilder = new FhirComplicationsBuilder(this.config)
        comBuilder.buildResources(caseInfo, geccoCase)

        FhirLaboratoryValuesBuilder labBuilder = new FhirLaboratoryValuesBuilder(this.config)
        labBuilder.buildResources(caseInfo, geccoCase)

        FhirMedicationBuilder medBuilder = new FhirMedicationBuilder(this.config)
        medBuilder.buildResources(caseInfo, geccoCase)

        FhirSymptomsBuilder symBuilder = new FhirSymptomsBuilder(this.config)
        symBuilder.buildResources(caseInfo, geccoCase)

        FhirTherapyBuilder thBuilder = new FhirTherapyBuilder(this.config)
        thBuilder.buildResources(caseInfo, geccoCase)

        FhirVitalSignsBuilder vitBuilder = new FhirVitalSignsBuilder(this.config)
        vitBuilder.buildResources(caseInfo, geccoCase)

        FhirStudyEnrollmentBuilder studBuilder = new FhirStudyEnrollmentBuilder(this.config)
        studBuilder.buildResources(caseInfo, geccoCase)

        FhirDischargeOutcomeBuilder discBuilder = new FhirDischargeOutcomeBuilder(this.config)
        discBuilder.buildResources(caseInfo, geccoCase)

        buildFhirCaseBundle(geccoCase)

        return geccoCase
    }

    private void buildFhirCaseBundle(FhirGeccoCase geccoCase){
        Bundle bundle = new Bundle()
        bundle.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/gecco-bundle")
        bundle.setType(Bundle.BundleType.TRANSACTION)

        //Patient
        Patient patient = geccoCase.getPatient()
        bundle.addEntry()
            .setFullUrl("Patient/" + patient.getIdElement().getValue())
            .setResource(patient)
            .getRequest()
                .setUrl("Patient")
                .setMethod(Bundle.HTTPVerb.POST)

        //Consent
        Consent consent = geccoCase.getConsent()
        bundle.addEntry()
            .setFullUrl("Consent/" + consent.getIdElement().getValue())
            .setResource(consent)
            .getRequest()
                .setUrl("Consent")
                .setMethod(Bundle.HTTPVerb.POST)

        //Conditions
        for(Condition con : geccoCase.getConditions()){
            bundle.addEntry()
                    .setFullUrl("Condition/" + con.getIdElement().getValue())
                    .setResource(con)
                    .getRequest()
                    .setUrl("Condition")
                    .setMethod(Bundle.HTTPVerb.POST)
        }

        //Procedures
        for(Procedure proc : geccoCase.getProcedures()){
            bundle.addEntry()
                    .setFullUrl("Procedure/" + proc.getIdElement().getValue())
                    .setResource(proc)
                    .getRequest()
                    .setUrl("Procedure")
                    .setMethod(Bundle.HTTPVerb.POST)
        }

        //Observations
        for(Observation obs : geccoCase.getObservations()){
            bundle.addEntry()
                    .setFullUrl("Observation/" + obs.getIdElement().getValue())
                    .setResource(obs)
                    .getRequest()
                    .setUrl("Observation")
                    .setMethod(Bundle.HTTPVerb.POST)
        }

        //MedicationStatements
        for(MedicationStatement medStmt : geccoCase.getMedicationStatements()){
            bundle.addEntry()
                    .setFullUrl("MedicationStatement/" + medStmt.getIdElement().getValue())
                    .setResource(medStmt)
                    .getRequest()
                    .setUrl("MedicationStatement")
                    .setMethod(Bundle.HTTPVerb.POST)
        }

        //Immunizations
        geccoCase.getImmunizations().each {immunization ->
            bundle.addEntry()
                    .setFullUrl("Immunization/${immunization.getIdElement().getValue()}")
                    .setResource(immunization)
                    .getRequest()
                    .setUrl("Immunization")
                    .setMethod(Bundle.HTTPVerb.POST)
        }

        geccoCase.setBundle(bundle)
    }

    private Patient buildPatientResource(SPatient patient, CaseInformation caseInfo){
        Patient pat = new Patient()

        //Id
        pat.setId(patient.getId())

        //Profile
        pat.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/Patient")

        //Extensions
        Extension ethnicGroup = new Extension(), age = new Extension()
        //Ethnic group
        ethnicGroup.setUrl("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/ethnic-group")
        switch (patient.getEthnicGroup().getCode()){
            case "2135-2":
                //Non Hispanic or Latino
                ethnicGroup.setValue(new Coding("urn:oid:2.16.840.1.113883.6.238", "2135-2",
                        "Hispanic or Latino"))
                break
            default:
                if(this.config.getGeccoVersion() == SyntheaGeccoConfig.GeccoVersion.V1_0_3){
                    ethnicGroup.setValue(new Coding("http://snomed.info/sct", "186019001",
                            "Other ethnic, mixed origin (ethnic group)"))
                }
                else{
                    ethnicGroup.setValue(new Coding("http://snomed.info/sct", "26242008",
                            "Mixed (qualifier value)"))
                }
                break
        }
        pat.addExtension(ethnicGroup)

        //Age
        def ageInYears = patient.getAge().getYears()
        def patAge = new Age()
        if(ageInYears > 0) patAge.setValue(ageInYears).setUnit("years").setSystem("http://unitsofmeasure.org").setCode("a")
        else patAge.setValue(patient.getAge().getMonths()).setUnit("months").setSystem("http://unitsofmeasure.org").setCode("mo")

        age.setUrl("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/age")
        age.addExtension(new Extension()
                .setUrl("dateTimeOfDocumentation")
                //The age is calculated at the onset of the disease
                .setValue(new DateTimeType(caseInfo.getOnset()))
        ).addExtension(
                new Extension()
                .setUrl("age")
                .setValue(patAge)
        )
        pat.addExtension(age)

        //Birth date
        pat.setBirthDate(patient.getBirthDate())

        return  pat
    }

    private Consent buildConsentResource(CaseInformation caseInfo, Patient patient){
        def con = new Consent()

        //ID
        con.setId(UUID.randomUUID().toString())

        //Profile
        con.getMeta().addProfile("http://fhir.de/ConsentManagement/StructureDefinition/Consent")

        //Status
        con.setStatus(Consent.ConsentState.ACTIVE)

        //Scope of use
        con.setScope(new CodeableConcept(
                new Coding(
                        "http://terminology.hl7.org/CodeSystem/consentscope",
                        "research",
                        "Research"
                )
        ))

        //Category
        con.addCategory(new CodeableConcept(
                new Coding(
                        "http://loinc.org",
                        "57016-8",
                        "Privacy policy acknowledgement Document"
                )
        ))

        //Patient reference
        con.setPatient(new Reference(patient))

        /*Timestamp of consent agreement is set to the onset date time since it is equal to the recorded date time and
        * thus usable as a "realistic" value for this entry*/
        con.setDateTime(caseInfo.getOnset())

        return con
    }

}
