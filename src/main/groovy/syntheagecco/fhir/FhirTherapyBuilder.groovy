package syntheagecco.fhir

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Period
import org.hl7.fhir.r4.model.Procedure
import org.hl7.fhir.r4.model.Reference
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.extraction.model.SEncounter
import syntheagecco.extraction.model.SProcedure
import syntheagecco.fhir.model.FhirGeccoCase

class FhirTherapyBuilder extends BaseResourceBuilder{

    FhirTherapyBuilder(){
        super()
    }

    FhirTherapyBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, FhirGeccoCase geccoCase) {
        Patient patient = geccoCase.getPatient()

        //Procedures
        List<Procedure> procedures = geccoCase.getProcedures()
        procedures.addAll(this.buildDialysisHemofiltrationResources(caseInfo.getHemofiltrationProcedures(), patient))
        procedures.addAll(this.buildApheresisResources(caseInfo.getApheresisProcedures(), patient))
        procedures.addAll(this.buildPronePositionResources(caseInfo.getPronePositionProcedures(), patient))
        /*
        if(caseInfo.getVentilationType()){
            procedures.add(this.buildVentilationTypeResource(caseInfo.getVentilationType(), patient))
        }
        */

        //Observations
        geccoCase.getObservations().add(this.buildIntensiveCareUnitResource(caseInfo.getPresenceInIU(),
                caseInfo.getAbatement(), patient))
    }

    private List<Procedure> buildDialysisHemofiltrationResources(List<SProcedure> procedures, Patient patient){
        List<Procedure> procList = new ArrayList<>()

        for(SProcedure procedure : procedures){
            Procedure proc = new Procedure()

            //Id
            proc.setId(procedure.getId())

            //Identifier
            proc.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${procedure.getId()}_DialysisHemofiltration")
            )

            //Profile
            proc.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/dialysis")

            //Status
            proc.setStatus(Procedure.ProcedureStatus.COMPLETED)

            //Category
            proc.setCategory(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "277132007", "Therapeutic procedure (procedure)")
            ))

            //Code
            proc.setCode(new CodeableConcept(procedure.getCode()))

            //Subject reference
            proc.setSubject(new Reference("Patient/" + patient.getId()))

            //Performed date time
            proc.setPerformed(new Period().setStart(procedure.getStart()).setEnd(procedure.getEnd()))

            procList.add(proc)
        }

        return procList
    }

    private List<Procedure> buildApheresisResources(List<SProcedure> procedures, Patient patient){
        List<Procedure> procList = new ArrayList<>()

        for(SProcedure procedure : procedures){
            Procedure proc = new Procedure()

            //Id
            proc.setId(procedure.getId())

            //Identifier
            proc.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${procedure.getId()}_Apheresis")
            )

            //Profile
            proc.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/apheresis")

            //Status
            proc.setStatus(Procedure.ProcedureStatus.COMPLETED)

            //Category
            proc.setCategory(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "277132007", "Therapeutic procedure (procedure)")
            ))

            //Code
            proc.setCode(new CodeableConcept(procedure.getCode()))

            //Subject reference
            proc.setSubject(new Reference("Patient/" + patient.getId()))

            //Performed date time
            proc.setPerformed(new Period().setStart(procedure.getStart()).setEnd(procedure.getEnd()))

            procList.add(proc)
        }

        return procList
    }

    private List<Procedure> buildPronePositionResources(List<SProcedure> procedures, Patient patient){
        List<Procedure> procList = new ArrayList<>()

        for(SProcedure procedure : procedures){
            Procedure proc = new Procedure()

            //Id
            proc.setId(procedure.getId())

            //Identifier
            proc.addIdentifier(new Identifier()
                    .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                    .setValue("${procedure.getId()}_PronePosition")
            )

            //Profile
            proc.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/prone-position")

            //Status
            proc.setStatus(Procedure.ProcedureStatus.COMPLETED)

            //Category
            proc.setCategory(new CodeableConcept(
                    new Coding("http://snomed.info/sct", "225287004",
                            "Procedures relating to positioning and support (procedure)")
            ))

            //Code
            proc.setCode(new CodeableConcept(procedure.getCode()))

            //Subject reference
            proc.setSubject(new Reference("Patient/" + patient.getId()))

            //Performed date time
            proc.setPerformed(new Period().setStart(procedure.getStart()).setEnd(procedure.getEnd()))

            procList.add(proc)
        }

        return procList
    }

    //TODO: ECMO

    private Observation buildIntensiveCareUnitResource(SEncounter icuAdm, Date abatement, Patient patient){
        Observation obs = new Observation()

        //Id
        obs.setId(UUID.randomUUID().toString())

        //Identifier
        obs.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${obs.getId()}_IntensiveCareUnit")
        )

        //Profile
        obs.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/patient-in-icu")

        //Status: always final
        obs.setStatus(Observation.ObservationStatus.FINAL)

        //Category
        obs.addCategory(new CodeableConcept(
                new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "survey",
                        "Survey")
        ))

        //Code
        Coding coding
        SyntheaGeccoConfig.GeccoVersion version = this.config.getGeccoVersion()
        if(version == SyntheaGeccoConfig.GeccoVersion.V1_0_3){
            coding = new Coding("https://www.netzwerk-universitaetsmedizin.de/fhir/CodeSystem/ecrf-parameter-codes",
                    "01", "Is the patient in the intensive care unit?")
        }
        else{
            coding = new Coding("http://loinc.org", "95420-6",
                    "Whether the patient was admitted to intensive care unit (ICU) for condition of interest")
        }
        obs.setCode(new CodeableConcept(coding))

        //Subject reference
        obs.setSubject(new Reference("Patient/" + patient.getId()))

        Coding value = new Coding().setSystem("http://snomed.info/sct")
        if(icuAdm){
            //EffectiveDateTime
            obs.setEffective(new DateTimeType(icuAdm.getStart()))

            //Value
            obs.setValue(new CodeableConcept(value.setCode("373066001").setDisplay("Yes (qualifier value)")))
        }
        else {
            //EffectiveDateTime
            obs.setEffective(new DateTimeType(abatement))

            //Value
            obs.setValue(new CodeableConcept(value.setCode("373067005").setDisplay("No (qualifier value)")))
        }

        return obs
    }

    private Procedure buildVentilationTypeResource(SProcedure procedure, Patient patient){
        Procedure proc = new Procedure()

        //Id
        proc.setId(procedure.getId())

        //Identifier
        proc.addIdentifier(new Identifier()
                .setSystem("https://fhir.imi.uni-luebeck.de/fhir")
                .setValue("${procedure.getId()}_VentilationType")
        )

        //Profile
        proc.getMeta().addProfile("https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/respiratory-therapies")

        //Status: always 'completed'
        proc.setStatus(Procedure.ProcedureStatus.COMPLETED)

        //Category
        proc.setCategory(new CodeableConcept(
                new Coding("http://snomed.info/sct", "277132007", "Therapeutic procedure (procedure)")
        ))

        //Code: includes all SNOMEDCT code where concept isA 53950000
        Coding code = procedure.getCode()
        proc.setCode(new CodeableConcept(code))

        //Subject reference
        proc.setSubject(new Reference("Patient/" + patient.getId()))

        //Performed date time
        proc.setPerformed(new Period().setStart(procedure.getStart()).setEnd(procedure.getEnd()))

        //Used item code
        switch (code.getCode()){
            case "112798008":
                proc.addUsedCode(new CodeableConcept(
                        new Coding("http://snomed.info/sct", "26412008",
                                "Endotracheal tube, device (physical object)")
                ))
                break
        }

        return proc
    }

}
