package syntheagecco.fhir.validation

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport
import ca.uhn.fhir.validation.FhirValidator
import ca.uhn.fhir.validation.ResultSeverityEnum
import ca.uhn.fhir.validation.SingleValidationMessage
import ca.uhn.fhir.validation.ValidationResult
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator
import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.r4.model.CodeSystem
import org.hl7.fhir.r4.model.StructureDefinition
import org.hl7.fhir.r4.model.ValueSet
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.fhir.model.FhirGeccoCase
import syntheagecco.utility.FileManipulation

import java.nio.charset.StandardCharsets

class FHIRInstanceValidator {

    private final static Logger logger = LogManager.getLogger(FHIRInstanceValidator.class);

    private static SyntheaGeccoConfig config
    private FhirValidator validator
    private final FhirContext ctx = FhirContext.forR4()

    FHIRInstanceValidator(){
        init(null)
    }

    FHIRInstanceValidator(String remoteValidationUrl){
        init(remoteValidationUrl)
    }

    private void init(String remoteValidationUrl){
        this.validator = ctx.newValidator()
        ValidationSupportChain supportChain = new ValidationSupportChain()

        DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ctx)
        CommonCodeSystemsTerminologyService codeService = new CommonCodeSystemsTerminologyService(ctx)
        InMemoryTerminologyServerValidationSupport validationSupport = new InMemoryTerminologyServerValidationSupport(ctx)
        RemoteTerminologyServiceValidationSupport remoteSupport = new RemoteTerminologyServiceValidationSupport(ctx)

        // Add all support and service elements to the support chain
        supportChain.addValidationSupport(defaultSupport)
        supportChain.addValidationSupport(codeService)
        supportChain.addValidationSupport(validationSupport)
        //Unfortunately, this is how the CLI builder works
        if(remoteValidationUrl != "null"){
            logger.info("Using remote validation terminology server: ${remoteValidationUrl}")
            remoteSupport.setBaseUrl(remoteValidationUrl)
            supportChain.addValidationSupport(remoteSupport)
        }

        // Get KDS profiles from project resources folder
        def structureDefList = loadStructureDefinitions()
        // Get code systems
        def codeSysList = loadCodeSystems()
        // Get value sets
        def valueSetList = loadValueSets()
        // Get Extensions
        def extList = loadExtensions()

        PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ctx)
        // Custom structure definitions
        structureDefList.each {structureDef ->
            prePopulatedSupport.addStructureDefinition(structureDef)
        }
        // Custom code systems
        codeSysList.each {codeSys ->
            prePopulatedSupport.addCodeSystem(codeSys)
        }
        // Custom value sets
        valueSetList.each {valueSet ->
            prePopulatedSupport.addValueSet(valueSet)
        }
        // Custom extensions
        extList.each {ext ->
            prePopulatedSupport.addStructureDefinition(ext)
        }

        // Add PrePopulatedValidationSupport
        supportChain.addValidationSupport(prePopulatedSupport)

        CachingValidationSupport cachingChain = new CachingValidationSupport(supportChain)
        FhirInstanceValidator validatorModule = new FhirInstanceValidator(cachingChain)
        validator.registerValidatorModule(validatorModule)
    }

    int[] validateGeccoCase(FhirGeccoCase geccoCase){
        def countValidResources = 0, countTotalResources = 0

        //Patient
        if(validateAndPrint(geccoCase.getPatient())) countValidResources++
        countTotalResources++

        //Conditions
        geccoCase.getConditions().each {condition ->
            if(validateAndPrint(condition)) countValidResources++
            countTotalResources++
        }

        //Procedures
        geccoCase.getProcedures().each {procedure ->
            if(validateAndPrint(procedure)) countValidResources++
            countTotalResources++
        }

        //Observations
        geccoCase.getObservations().each {observation ->
            if(validateAndPrint(observation)) countValidResources++
            countTotalResources++
        }

        //Conditions
        geccoCase.getMedicationStatements().each {medicationStatement ->
            if(validateAndPrint(medicationStatement)) countValidResources++
            countTotalResources++
        }

        return [countValidResources, countTotalResources]
    }

    boolean validateAndPrint(IBaseResource resource) {
        def isValid = true
        def profiles = []
        resource.getMeta().getProfile().each {profile ->
            profiles << profile.getValue()
        }
        ValidationResult result = this.validator.validateWithResult(resource)
        List<SingleValidationMessage> messages = result.getMessages()
        if (result.isSuccessful()) {
            logger.debug("[?]Validation was successful!")
        } else {
            isValid = false
            logger.debug("[?]Validation failed! ID: ${resource.getIdElement().getValue()}")
            logger.debug(profiles.join(", "))
            messages.each {SingleValidationMessage message ->
                if (message.getSeverity().ordinal() > ResultSeverityEnum.INFORMATION.ordinal()) {
                    logger.debug("== Validation Message:")
                    logger.debug("---- Location: " + message.getLocationString())
                    logger.debug("---- Severity: " + message.getSeverity())
                    logger.debug("---- Message:  " + message.getMessage())
                }
            }
        }
        return isValid
    }

    // Idea: https://github.com/hapifhir/hapi-fhir/issues/552
    private String getProfileText(String pathToProfile) {
        def profileText = null
        try {
            profileText = IOUtils.toString(new FileInputStream(pathToProfile), StandardCharsets.UTF_8)
        } catch (IOException e) {
            logger.error(e.getMessage(), e)
        }
        return profileText
    }

    private List<StructureDefinition> loadStructureDefinitions(){
        return loadResourceDefinitions(StructureDefinition.class, "src/main/resources/fhir/structure_defs")
    }

    private List<ValueSet> loadValueSets(){
        return loadResourceDefinitions(ValueSet.class, "src/main/resources/fhir/value_sets")
    }

    private List<CodeSystem> loadCodeSystems(){
        return loadResourceDefinitions(CodeSystem.class, "src/main/resources/fhir/code_systems")
    }

    private List<StructureDefinition> loadExtensions(){
        return loadResourceDefinitions(StructureDefinition.class, "src/main/resources/fhir/extensions")
    }

    private <R> List<R> loadResourceDefinitions(Class<R> resourceClass, String pathToFolder){
        def fileList = FileManipulation.getFilesInDirRecursive(pathToFolder, FileManipulation.FileExtension.JSON)
        def resourceList = []
        fileList.each {file ->
            resourceList << getResourceDefinition(resourceClass, file.getCanonicalPath())
        }
        return resourceList
    }

    private <R> R getResourceDefinition(Class<R> resourceClass, String pathToProfile) {
        String profileText = this.getProfileText(pathToProfile)
        return this.ctx.newJsonParser().parseResource(resourceClass, profileText);
    }

    FhirContext getCtx() {
        return ctx
    }

}
