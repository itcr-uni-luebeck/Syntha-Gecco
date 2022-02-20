import ca.uhn.fhir.parser.IParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.nedap.archie.aom.primitives.CTemporal
import com.nedap.archie.rm.composition.Composition
import com.nedap.archie.rm.generic.PartyIdentified
import com.nedap.archie.rm.support.identification.GenericId
import com.nedap.archie.rm.support.identification.PartyRef
import groovy.cli.picocli.OptionAccessor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.shareddefinition.Language
import org.ehrbase.client.classgenerator.shareddefinition.Setting
import org.ehrbase.client.classgenerator.shareddefinition.Territory
import org.ehrbase.client.flattener.Unflattener
import org.ehrbase.client.openehrclient.VersionUid
import org.ehrbase.serialisation.jsonencoding.CanonicalJson
import org.hl7.elm.r1.Case
import org.mitre.synthea.engine.Generator
import org.mitre.synthea.engine.Generator.GeneratorOptions
import org.mitre.synthea.helpers.Config
import syntheagecco.config.SyntheaGeccoConfig
import syntheagecco.extraction.JSONExtractor
import syntheagecco.extraction.model.CaseInformation
import syntheagecco.fhir.FhirCaseBuilder
import syntheagecco.fhir.model.FhirGeccoCase
import syntheagecco.fhir.validation.FHIRInstanceValidator
import syntheagecco.fhir.validation.FhirFileValidator
import syntheagecco.openehr.sdk.CompositionProvider
import syntheagecco.openehr.sdk.CompositionProviderConfig
import syntheagecco.openehr.sdk.OtpCaseBuilder
import syntheagecco.openehr.sdk.client.EhrBaseClient
import syntheagecco.openehr.sdk.client.TemplateProviderImp
import syntheagecco.openehr.sdk.model.OptGeccoCase
import syntheagecco.utility.FileManipulation
import syntheagecco.extraction.mapping.MultiListMap

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

import groovy.cli.picocli.CliBuilder

class Main {

    @SuppressWarnings('GroovyConstantNamingConvention')
    private final static Logger logger = LogManager.getLogger(Main.class)
    // Path.of should use the system directory separator (`\\` on Windows, `/` on Linux/MacOS/etc
    @SuppressWarnings('GroovyConstantNamingConvention')
    private final static String SYNTHEA_EXPORTER_DIRECTORY = Path.of("src", "main", "resources", "output").toString()

    static class ArgParser {

        private class Defaults {
            final static String POPULATION = "100"
            final static String SEED = System.currentTimeMillis().toString()
            final static String NO_VALIDATION = SyntheaGeccoConfig.ValidationStrategy.NO_VALIDATION.valueOf()
            class OpenEhr {
                class Composer {
                    @SuppressWarnings('GroovyConstantNamingConvention')
                    final static String REF_ID_VALUE = "synthea-gecco"
                    @SuppressWarnings('GroovyConstantNamingConvention')
                    final static String REF_ID_SCHEME = "synthea-gecco"
                    @SuppressWarnings('GroovyConstantNamingConvention')
                    final static String REF_NAMESPACE = "synthea"
                    final static String NAME = "synthea-gecco"
                }
            }
        }

        @SuppressWarnings('GrUnresolvedAccess')
        static OptionAccessor parseArgs(String[] args) {
            //https://joshdurbin.net/posts/2020-3-groovy-clibuilder/

            //noinspection GroovyLocalVariableNamingConvention
            def cli = new CliBuilder(header: "Synthea GECCO")
            cli.with {
                h longOpt: 'help', "Print this help text"
                p longOpt: 'population', args: 1, defaultValue: Defaults.POPULATION, type: Long, "Number of patients to generate, defaults to: $Defaults.POPULATION"
                s longOpt: 'seed', args: 1, defaultValue: Defaults.SEED, type: Long, "The seed for the Synthea RNG, defaults to hte current system time"
                cs longOpt: 'clinicianSeed', args: 1, defaultValue: Defaults.SEED, type: Long, "The seed for the Synthea RNG for clinicians, defaults to current system time"
                g longOpt: 'gender', type: String, defaultValue: "BOTH", "The allowed gender of generated patient, defaults to 'null' meaning both genders will be present"
                minA longOpt: 'minAge', args: 1, defaultValue: '0', type: String, "The minimum age at which a patient is generated, defaults to 0"
                maxA longOpt: 'maxAge', args: 1, defaultValue: "140", type: String, "The maximum age at which a patient is generated, defaults to 140"
                c longOpt: 'city', type: String, defaultValue: "null", "The city from which the patient demographic is retrieved, defaults to the default state value, look at the demographics.json file for city names"
                st longOpt: 'state', type: String, defaultValue: "Massachusetts", "The state from which the patient demographic is retrieved, defaults to Massachusetts, look at the demographics.json file for state names"
                refT longOpt: 'referenceTime', type: Long, defaultValue: Defaults.SEED, "The reference time for patient generation, default to the local system time (default seed)"
                v longOpt: 'validation', type: String, defaultValue: Defaults.NO_VALIDATION, "The validation strategy for the generated data, defaults to 'no_validation' meaning no validation is done"
                vRemote longOpt: 'remoteTerminologyValidation', type: String, defaultValue: "null", "The remote terminology server URL used for validation of the FHIR instances, defaults to null"
                gVersion longOpt: 'geccoVersion', type: String, defaultValue: "1.0.4", "The GECCO version of the generated FHIR ressource instances, defaults to 1.0.4"
                dPolicy longOpt: 'detectedFilePolicy', type: String, defaultValue: "delete_on_finish", "The policy determining the fate of the files generated by Synthea, defaults to 'delete_on_finish'"
                f longOpt: 'fhir', "Enables generation of FHIR related data. Enables by default"
                o longOpt: 'openEhr', "Enables generation of openEhr related data"
                oUpload longOpt: 'openEhrUpload', "if called, the resources will be uploaded to specified openEHR server that has to ne specified"
                oEndpoint longOpt: 'openEhrRepoEndpoint', type: String, defaultValue: "", "EHRbase server endpoint"
                oUser longOpt: 'openEhrRepoUsername', type: String, defaultValue: "", "EHRbase server OAuth user name"
                oPassw longOpt: 'openEhrRepoPassword', type: String, defaultValue: "", "EHRbase server OAuth pass word"
                ocIdValue longOpt: 'openEhrComposerIdValue', type: String, defaultValue: Defaults.OpenEhr.Composer.REF_ID_VALUE, "the name value of the composer ID within openEHR templates, defaults to: $Defaults.OpenEhr.Composer.REF_ID_VALUE"
                ocIdScheme longOpt: 'openEhrComposerIdScheme', type: String, defaultValue: Defaults.OpenEhr.Composer.REF_ID_SCHEME, "the scheme value of the composer ID within openEHR templates, defaults to: $Defaults.OpenEhr.Composer.REF_ID_SCHEME"
                ocNamespace longOpt: 'openEhrComposerNamespace', type: String, defaultValue: Defaults.OpenEhr.Composer.REF_NAMESPACE, "the namespace value of the composer within openEHR templates, defaults to: $Defaults.OpenEhr.Composer.REF_NAMESPACE"
                ocName longOpt: 'openEhrComposerName', type: String, defaultValue: Defaults.OpenEhr.Composer.NAME, "the scheme value of the composer within openEHR templates, defaults to: $Defaults.OpenEhr.Composer.NAME"
                ocTerritory longOpt: 'openEhrComposerTerritory', type: String, defaultValue: "DE", "the territory in which the composer resides, coded with the ISO_3166-1 code system, defaults to: DE"
                ocLanguage longOpt: 'openEhrComposerLanguage', type: String, defaultValue: "de", "the language which is used in the compositions, coded with the ISO_639-1 code system, defaults to: de"
            }

            def cliOptions = cli.parse(args)
            if (!cliOptions) {
                cli.usage()
                System.exit(-1)
            }

            if (cliOptions.help) {
                cli.usage()
                System.exit(0)
            }

            return cliOptions
        }
    }

    static class OpenEhrConfig {
        Boolean uploadToOpenEhr
        String repoEndpoint
        String repoUsername
        String repoPassword
        String composerRefIdValue
        String composerRefIdScheme
        String composerRefNamespace
        String composerName
        Territory territory
        Language language
        //TODO: If the patient was admitted to the hospital or ICU change this attribute accordingly
        Setting setting = Setting.SECONDARY_MEDICAL_CARE

        PartyIdentified getPartyProxy() {
            return new PartyIdentified().tap {
                name = this.composerName
                externalRef = new PartyRef().tap {
                    id = new GenericId().tap {
                        value = this.composerRefIdValue
                        scheme = this.composerRefIdScheme
                    }
                    namespace = this.composerRefNamespace
                    type = "ORGANISATION"
                }
            }
        }
    }

    static GeneratorOptions configureSyntheaOptions(OptionAccessor parsedArgs) {
        GeneratorOptions options = new GeneratorOptions()
        //1.4 is a heuristic value in order to increase the likelihood that there are enough covid cases in the
        //generated population
        options.population = (int) Math.ceil(parsedArgs.population*1.4)
        options.seed = parsedArgs.seed
        options.clinicianSeed = parsedArgs.clinicianSeed
        //gender, minAge, maxAge, city, state, referenceTime
        def gender = checkGender(parsedArgs.gender)
        options.gender = gender
        options.minAge = Integer.parseInt(parsedArgs.minAge)
        options.maxAge = Integer.parseInt(parsedArgs.maxAge)
        options.city = parsedArgs.city != "null" ? parsedArgs.city : null
        options.state = parsedArgs.state
        options.referenceTime = parsedArgs.referenceTime
        // the following options do *not* need to be exposed to the CLI
        Config.set("exporter.ccda.export", "false")
        Config.set("exporter.fhir.export", "true")
        Config.set("exporter.fhir_stu3.export", "false")
        Config.set("exporter.fhir_dstu2.export", "false")
        Config.set("exporter.subfolders_by_id_substring", "false")
        Config.set("exporter.baseDirectory", SYNTHEA_EXPORTER_DIRECTORY)
        Config.set("generate.database_type", "none")

        return options
    }

    private static String checkGender(String gender){
        assert gender == 'M' || gender == 'F' || gender == "BOTH": "Invalid gender string value. Possible values:\n" +
                "M - male\n" +
                "F - female\n" +
                "BOTH - both\n" +
                "If this options is not used both patients of both genders will be generated."
        return gender == "BOTH" ? null : gender
    }

    @SuppressWarnings('unused')
    static SyntheaGeccoConfig configureSyntheaGeccoOptions(OptionAccessor parsedArgs) {
        return new SyntheaGeccoConfig().tap {
            def version = SyntheaGeccoConfig.GeccoVersion.getVersion(parsedArgs.geccoVersion)
            def policy = SyntheaGeccoConfig.DetectedFilePolicy.getValue(parsedArgs.detectedFilePolicy)
            def strategy = SyntheaGeccoConfig.ValidationStrategy.getValue(parsedArgs.validation)
            def remoteTerminologyUrl = parsedArgs.remoteTerminologyValidation
            def fhirGeneration = parsedArgs.fhir
            def openEhrGeneration = parsedArgs.openEhr

            assert version != null: "String ${parsedArgs.geccoVersion} is not a correct GECCO Version argument"
            assert policy != null: "String ${parsedArgs.detectedFilePolicy} is not a correct Detected File Policy argument"
            assert strategy != null: "String ${parsedArgs.validation} is not a correct Validation Strategy argument"

            setGeccoVersion(version)
            setDetectedFilePolicy(policy)
            setValidationStrategy(strategy)
            setRemoteValidationUrl(remoteTerminologyUrl)
            setPopulation((int) parsedArgs.population)
            if(!(fhirGeneration||openEhrGeneration)||fhirGeneration) setGenerateFhir(true)
            if(openEhrGeneration) setGenerateOpenEhr(true)
        }
    }

    static OpenEhrConfig configureOpenEhrOptions(OptionAccessor parsedArgs) {
        def territory = parseTerritory(parsedArgs.openEhrComposerTerritory)
        def language = parseLanguage(parsedArgs.openEhrComposerLanguage)

        return new OpenEhrConfig(
                uploadToOpenEhr: parsedArgs.openEhrUpload,
                repoEndpoint: parsedArgs.openEhrRepoEndpoint,
                repoUsername: parsedArgs.openEhrRepoUsername,
                repoPassword: parsedArgs.openEhrRepoPassword,
                composerRefIdValue: parsedArgs.openEhrComposerRefIdValue,
                composerRefIdScheme: parsedArgs.openEhrComposerRefIdScheme,
                composerRefNamespace: parsedArgs.openEhrComposerRefNamespace,
                composerName: parsedArgs.openEhrComposerName,
                language: language,
                territory: territory
        )
    }

    private static Territory parseTerritory(String territory){
        parseStringToEnumValue(territory, Territory.class, 'ISO_3166-1') as Territory
    }

    private static Language parseLanguage(String language){
        parseStringToEnumValue(language, Language.class, 'ISO_639-1') as Language
    }

    private static <E extends Enum<E>> E parseStringToEnumValue(String s, Class<E> e, String system){
        def set = EnumSet.allOf(e)
        def map = new HashMap<String, E>()
        set.each {value -> map[value.getCode()] = value}

        def code = map[s]
        assert code != null: "String '${s}' isn't a valid code for system ${system}"

        return code
    }

    static void configureConfigurationProvider(OpenEhrConfig openEhrConfig) {
        def providerConfig = new CompositionProviderConfig().tap {
            partyProxy = openEhrConfig.partyProxy
            territory = openEhrConfig.territory
            language = openEhrConfig.language
            setting = openEhrConfig.setting
        }
        CompositionProvider.setConfig(providerConfig)
    }

    static void runGenerator(GeneratorOptions syntheaOptions, SyntheaGeccoConfig syntheaGeccoConfig) {
        Generator generator = new Generator(syntheaOptions)
        logger.info("[#]Configuration:" +
                "\n\tGecco version: ${syntheaGeccoConfig.getGeccoVersion().toString()}")

        logger.info("[#]Running Synthea. Generating ${syntheaOptions.population == 1 ? "patient" : "patients"} ...")
        generator.run()
        logger.info("[#]Finished running Synthea.")
    }

    static List<CaseInformation> extractInformationFromSynthea(SyntheaGeccoConfig syntheaGeccoConfig) {
        //Extracting information from generated JSON files
        logger.info("[#]Processing JSON files ...")
        def extractor = new JSONExtractor(syntheaGeccoConfig)
        def cases = extractor.extractInformationFromJSON(SYNTHEA_EXPORTER_DIRECTORY)
        return cases
    }

    static void handleGeneratedSyntheaFiles(String pathToSyntheaFiles, SyntheaGeccoConfig config){
        //Last action: handle detected files based on detected file policy setting
        switch(config.getDetectedFilePolicy()) {
            case SyntheaGeccoConfig.DetectedFilePolicy.DELETE_ON_FINISH:
                FileManipulation.getFilesInDirRecursive(pathToSyntheaFiles, FileManipulation.FileExtension.JSON).each { File file ->
                    if (file.delete()) {
                        logger.debug("SUCCESS: ${file.name} deleted.")
                    } else {
                        logger.warn("FAILURE: Can't delete ${file.name}!")
                    }
                }
                break
            default:
                def newDir = new File("archive\\${System.currentTimeMillis().toString()}")
                if (!newDir.exists()) {
                    newDir.mkdirs()
                }
                FileManipulation.getFilesInDirRecursive(pathToSyntheaFiles, FileManipulation.FileExtension.JSON).each { File file ->
                    Files.move(file.toPath(), Paths.get(newDir.absolutePath, file.name),
                            StandardCopyOption.REPLACE_EXISTING)
                }
                break
        }
    }

    static List<FhirGeccoCase> createFhirResources(List<CaseInformation> cases, SyntheaGeccoConfig syntheaGeccoConfig) {
        logger.info("[#]Creating FHIR resources ...")
        def fhirCaseBuilder = new FhirCaseBuilder(syntheaGeccoConfig)
        List<FhirGeccoCase> fhirGeccoCases = new ArrayList<>()
        cases.each { caseInfo ->
            fhirGeccoCases << fhirCaseBuilder.buildFhirGeccoCase(caseInfo)
        }
        return fhirGeccoCases
    }

    static List<OptGeccoCase> createOpenEhrResources(List<CaseInformation> cases, SyntheaGeccoConfig syntheaGeccoConfig) {
        logger.info("[#]Creating openEHR operational template instances ...")
        def openEhrCaseBuilder = new OtpCaseBuilder(syntheaGeccoConfig)
        List<OptGeccoCase> optGeccoCases = new ArrayList<>()
        cases.each { caseInfo ->
            optGeccoCases << openEhrCaseBuilder.buildOpenEhrGeccoCase(caseInfo)
        }
        return optGeccoCases
    }

    static void writeFhirResources(List<FhirGeccoCase> fhirGeccoCases, SyntheaGeccoConfig syntheaGeccoConfig, Path outputPath) {
        //Writing json files containing FHIR resources to output folder
        logger.info("[#]Writing FHIR resources as JSON files ...")
        IParser jsonParser = syntheaGeccoConfig.getFhirContext().newJsonParser().setPrettyPrint(true)
        fhirGeccoCases.each { fhirGeccoCase ->
            def fileName = fhirGeccoCase.getPatient().getId()
            def bundle = fhirGeccoCase.getBundle()
            def bundleString = jsonParser.encodeResourceToString(bundle)
            FileManipulation.writeJsonString(bundleString, fileName, outputPath.resolve("fhir").toString())
        }
    }

    static MultiListMap<UUID, VersionUid> commitOpenEhrCompositions(List<OptGeccoCase> optGeccoCases, OpenEhrConfig openEhrConfig) {
        logger.info("[#]Committing OPT instances to ehrBase server ...")
        def client = new EhrBaseClient(openEhrConfig.repoEndpoint, openEhrConfig.repoUsername, openEhrConfig.repoPassword)
        def ehrMap = new MultiListMap<UUID, VersionUid>()
        //def closure = {geccoCase -> client.createEhrAndCommitCompositions(geccoCase.getCompositions(), ehrMap)}
        def threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        try {
            List<Future> futures = optGeccoCases.collect { geccoCase ->
                threadPool.submit({ ->
                    client.createEhrAndCommitCompositions(geccoCase.getCompositions(), ehrMap)
                } as Callable)
            }
            futures.each { it.get() }
        }
        finally {
            threadPool.shutdown()
        }
        return ehrMap
    }

    static void writeOpenEhrCompositions(MultiListMap<UUID, Object> ehrMap, Path outputPath) {
        def objectMapper = new ObjectMapper()
        def writer = objectMapper.writerWithDefaultPrettyPrinter()
        def openEhrDir = outputPath.resolve("openehr").toFile()
        if (!openEhrDir.exists()) openEhrDir.mkdir()
        ehrMap.entrySet().each { entry ->
            def json = objectMapper.createObjectNode(), compositions = objectMapper.createArrayNode()
            def ehrId = entry.key.toString()
            json.put("ehrId", ehrId)
            entry.value.each { versionUid ->
                compositions.add(objectMapper.createObjectNode().put("versionUid", versionUid.toString()))
            }
            json.set("compositions", compositions)
            writer.writeValue(new File(openEhrDir.getCanonicalPath() + "/${ehrId}.json"), json)
        }
    }

    static void validateResources(List<FhirGeccoCase> fhirGeccoCases, SyntheaGeccoConfig syntheaGeccoConfig, Path outputPath) {
        SyntheaGeccoConfig.ValidationStrategy strategy = syntheaGeccoConfig.getValidationStrategy()
        if (strategy == SyntheaGeccoConfig.ValidationStrategy.COMPLETE
                || strategy == SyntheaGeccoConfig.ValidationStrategy.RESOURCE_VALIDATION_ONLY) {
            //Resource validation
            logger.info("[#]Validating FHIR resources ...")
            def validCount = 0, totalCount = 0
            FHIRInstanceValidator validator = new FHIRInstanceValidator(syntheaGeccoConfig.getRemoteValidationUrl())
            fhirGeccoCases.each { fhirGeccoCase ->
                logger.debug("[§]Patient ID: ${fhirGeccoCase.getPatient().getId()}")
                def counts = validator.validateGeccoCase(fhirGeccoCase)
                validCount += counts[0]
                totalCount += counts[1]
            }
            def format = new DecimalFormat("#.000")
            def formatted = format.format((validCount / totalCount) * 100)
            logger.info("[#]${validCount}/${totalCount} (${formatted} %) of validated resources are valid.")
        }
        if (strategy == SyntheaGeccoConfig.ValidationStrategy.COMPLETE
                || strategy == SyntheaGeccoConfig.ValidationStrategy.FILE_VALIDATION_ONLY) {
            //File validation
            logger.info("[#]Validating FHIR files...")
            def outputDir = outputPath.toFile()
            def validationDir = outputPath.resolve("validation").toFile()
            validationDir.mkdirs()
            FhirFileValidator.validateBundles(outputDir.getAbsolutePath(), validationDir.getAbsolutePath())
            //FhirFileValidator.postFhirFilesToServer(outputDir.getAbsolutePath())
        }
    }

    static void runSyntheaGecco(GeneratorOptions generatorOptions, SyntheaGeccoConfig syntheaGeccoConfig, OpenEhrConfig openEhrConfig) {
        runGenerator(generatorOptions, syntheaGeccoConfig)
        def caseInformationList = extractInformationFromSynthea(syntheaGeccoConfig)
        handleGeneratedSyntheaFiles(SYNTHEA_EXPORTER_DIRECTORY, syntheaGeccoConfig)

        def cntCovidCases = caseInformationList.size()
        while(cntCovidCases < syntheaGeccoConfig.population){
            def delta = syntheaGeccoConfig.population - cntCovidCases
            generatorOptions.population = (int) Math.ceil(delta*1.4)
            runGenerator(generatorOptions, syntheaGeccoConfig)
            def additionalCases = extractInformationFromSynthea(syntheaGeccoConfig)
            caseInformationList.addAll(additionalCases.subList(0, Math.min(additionalCases.size(), delta)))
            cntCovidCases = caseInformationList.size()
            handleGeneratedSyntheaFiles(SYNTHEA_EXPORTER_DIRECTORY, syntheaGeccoConfig)
        }

        caseInformationList = caseInformationList.subList(0, syntheaGeccoConfig.population)

        def date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()
        def outputPath = Path.of("output", date)
        //Creating FHIR resource object instances
        if(syntheaGeccoConfig.getGenerateFhir()){
            def fhirGeccoCases = createFhirResources(caseInformationList, syntheaGeccoConfig)
            //Writing json files containing FHIR resources to output folder
            writeFhirResources(fhirGeccoCases, syntheaGeccoConfig, outputPath)

            validateResources(fhirGeccoCases, syntheaGeccoConfig, outputPath)
        }
        if(syntheaGeccoConfig.getGenerateOpenEhr()){
            //Creating openEHR template object instances
            def optGeccoCases = createOpenEhrResources(caseInformationList, syntheaGeccoConfig)
            //Committing OPT instances to ehrBase server, if enabled in params
            if (openEhrConfig.uploadToOpenEhr) {
                def ehrMap = commitOpenEhrCompositions(optGeccoCases, openEhrConfig)
                writeOpenEhrCompositions(ehrMap, outputPath)
            } else {
                writeUncommittedOpenEhrResources(caseInformationList, optGeccoCases, outputPath)
            }
        }
    }

    static void writeUncommittedOpenEhrResources(List<CaseInformation> caseInformationList, List<OptGeccoCase> optGeccoCases, Path outputPath) {
        def optOutputDirectory = outputPath.resolve("uncommitted-openehr")
        def combinedInformation = [caseInformationList, optGeccoCases].transpose() // this creates pairs of list items
        // e.g. [[A, B, C], [1, 2, 3]] is transposed/zipped to [(A, 1), (B, 2), (C, 3)]
        // hence, we have case information together with the OPT templates
        def templateProvider = new TemplateProviderImp()
        combinedInformation.each { CaseInformation caseInfo, OptGeccoCase optGecco ->
            // we want an output directory per patient
            def caseOutputDirectory = optOutputDirectory.resolve("Patient-${caseInfo.patient.id}_${caseInfo.patient.given}_${caseInfo.patient.family}")
            caseOutputDirectory.toFile().mkdirs()
            logger.info("Writing to $caseOutputDirectory")
            optGecco.getCompositions().eachWithIndex { compositionEntity, index ->
                // code from: https://github.com/ehrbase/openEHR_SDK/blob/010647cc811788163d80e044d354f05f661e1675/client/src/main/java/org/ehrbase/client/openehrclient/defaultrestclient/DefaultRestCompositionEndpoint.java#L70
                // and https://github.com/ehrbase/openEHR_SDK/blob/1e26bf1eeee285b978335832f510ba12f008bbc3/serialisation/src/test/java/org/ehrbase/serialisation/jsonencoding/CanonicalJsonMarshallingTest.java#L131
                def composition = new Unflattener(templateProvider).unflatten(compositionEntity) as Composition
                def archetypeTemplateId = "${composition.archetypeDetails.archetypeId}_${composition.archetypeDetails.templateId}"
                // this gives us the composition as an instance of the Reference Model
                def canonicalJson = new CanonicalJson().marshal(composition)
                // and this gives a nice, canonical JSON string
                def filename = caseOutputDirectory.resolve("composition${index + 1}-${archetypeTemplateId}.json")
                filename.toFile().write(canonicalJson, "UTF-8")
            }
        }
    }

    static void main(String[] args) {
        /* TODO@paul this method was too long, according to the Groovy Styleguide (and the IntelliJ warning!).
            I agree, so I have refactored this method to call a bunch of new methods. */

        def parsedArgs = new ArgParser().parseArgs(args)
        def syntheaOptions = configureSyntheaOptions(parsedArgs)
        def syntheaGeccoConfig = configureSyntheaGeccoOptions(parsedArgs)
        def openEhrConfig = configureOpenEhrOptions(parsedArgs)
        configureConfigurationProvider(openEhrConfig)

        runSyntheaGecco(syntheaOptions, syntheaGeccoConfig, openEhrConfig)

        /*
        // legacy code commented out at time of refactor
        optGeccoCases.each {geccoCase ->
            client.createEhrAndCommitCompositions(geccoCase.getCompositions(), ehrMap)
        }
         */
        //Write EHR ID of created EHR with corresponding compositions to file


        //Writing flat JSON files representing openEHR template instances to output folder
        /*
        logger.info("[#]Writing openEHR template instances as flat JSON files ...")
        def cnt = 0
        openEhrGeccoCases.each {openEhrGeccoCase ->
            def bundle = openEhrCaseBuilder.buildBundle(openEhrGeccoCase)
            logger.debug("#_templates: ${bundle.getTemplates().size()}")
            bundle.getTemplates().each {entry ->
                def fileName = entry.getKey()
                def templateString = entry.getValue().toPrettyString()
                logger.debug(templateString)
                FileManipulation.writeJsonString(templateString, fileName, outputPath + "/openehr/${cnt}")
            }
            cnt++
        }
         */

        logger.info("[#]Done.")
    }

}
