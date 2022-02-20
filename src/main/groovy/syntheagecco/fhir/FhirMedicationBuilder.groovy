package syntheagecco.fhir

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.MedicationStatement
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SMedicationStatement
import syntheagecco.fhir.model.FhirGeccoCase

class FhirMedicationBuilder extends BaseResourceBuilder{

    private static final Logger logger = LogManager.getLogger(FhirMedicationBuilder.class)

    FhirMedicationBuilder(){
        super()
    }

    FhirMedicationBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        Patient patient = geccoCase.getPatient()

        //MedicationStatements
        List<MedicationStatement> medicationStatements = geccoCase.getMedicationStatements()
        medicationStatements.addAll(this.buildCovid19TherapyResources(caseInfo.getCovid19Therapies(), patient))
        medicationStatements.addAll(this.buildAnticoagulationResources(caseInfo.getAnticoagulationMeds(), patient))
    }

    private List<MedicationStatement> buildCovid19TherapyResources(List<SMedicationStatement> medStatements, Patient patient){
        List<MedicationStatement> medList = new ArrayList<>()

        for(SMedicationStatement medStatement : medStatements){
            MedicationStatement med = new MedicationStatement()

            //Id
            med.setId(medStatement.getId())

            //Identifier
            med.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${medStatement.getId()}_Covid19Therapy")
            )

            //Profile
            med.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/pharmacological-therapy")

            //Status
            med.setStatus(MedicationStatement.MedicationStatementStatus.COMPLETED)

            //Effective date time
            med.setEffective(new DateTimeType(medStatement.getEffective()))

            //Subject reference
            med.setSubject(new Reference("Patient/" + patient.getId()))

            //Medication code
            CodeableConcept medCode = new CodeableConcept()
            switch (medStatement.getCode()){
                case "198440":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "N02BE01", "Paracetamol"))
                    break
                case "242969":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "C01CA03", "Norephinephrine"))
                    break
                case "360110":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "L04AA10", "Sirolimus"))
                    break
                case "597730":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "J05AR10", "Lopinavir und Ritonavir"))
                    break
                case "2284960":
                    medCode.addCoding(new Coding("http://snomed.info/sct", "870518005", "Product containing remdesivir (medicinal product)"))
                    break
                case "1657981":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "L04AC07", "Toclizumab"))
                    break
                case "979092":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "P01BA02", "Hydroxychloroquine"))
                    medCode.addCoding(new Coding("http://snomed.info/sct", "83490000", "Product containing hydroxychloroquine (medicinal product)"))
                    break
                case "1116758":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "P01BA01", "Chloroquine"))
                    break
                case "727711":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "L04AC03", "Anakinra"))
                    break
                default:
                    logger.warn("Code '${medStatement.getCode()}' doesn't match yet was put into category 'COVID19_THERAPY'!")
                    break
            }
            medCode.setText(medStatement.getDisplay())
            med.setMedication(medCode)

            medList.add(med)
        }

        return medList
    }

    private List<MedicationStatement> buildAnticoagulationResources(List<SMedicationStatement> medStatements, Patient patient){
        List<MedicationStatement> medList = new ArrayList<>()

        for(SMedicationStatement medStatement : medStatements){
            MedicationStatement med = new MedicationStatement()

            //Id
            med.setId(medStatement.getId())

            //Identifier
            med.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${medStatement.getId()}_Anticoagulation")
            )

            //Profile
            med.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/pharmacological-therapy-anticoagulants")

            //Status
            med.setStatus(MedicationStatement.MedicationStatementStatus.COMPLETED)

            //Effective date time
            med.setEffective(new DateTimeType(medStatement.getEffective()))

            //Subject reference
            med.setSubject(new Reference("Patient/" + patient.getId()))

            //Medication code
            CodeableConcept medCode = new CodeableConcept()
            switch (medStatement.getCode()){
                case "1659263":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "B01AB01", "Heparin"))
                    break
                case "854228":
                    medCode.addCoding(new Coding("http://fhir.de/CodeSystem/dimdi/atc", "B01AB05", "Enoxaparin"))
                    break
                default:
                    logger.warn("Code '${medStatement.getCode()}' doesn't match yet was put into category 'ANTICOAGULATION'!")
                    break
            }
            medCode.setText(medStatement.getDisplay())
            med.setMedication(medCode)

            medList.add(med)
        }

        return medList
    }

}
