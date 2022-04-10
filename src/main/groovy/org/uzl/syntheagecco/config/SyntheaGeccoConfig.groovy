package org.uzl.syntheagecco.config

import ca.uhn.fhir.context.FhirContext

class SyntheaGeccoConfig {

    private static SyntheaGeccoConfig config

    private GeccoVersion geccoVersion
    private FhirContext ctx
    private ValidationStrategy validationStrategy
    private DetectedFilePolicy detectedFilePolicy
    private ResourceFiltering resourceFiltering
    private String remoteValidationUrl
    private int population
    private boolean generateFhir
    private boolean generateOpenEhr
    private String outputDir

    private SyntheaGeccoConfig(){
        this.geccoVersion = GeccoVersion.V1_0_4
        this.ctx = FhirContext.forR4()
        this.validationStrategy = ValidationStrategy.NO_VALIDATION
        this.detectedFilePolicy = DetectedFilePolicy.ARCHIVE_ON_FINISH
        this.resourceFiltering = ResourceFiltering.ALL
        this.generateFhir = true
        this.generateOpenEhr = false
        this.outputDir = "output"
    }

    static SyntheaGeccoConfig getInstance(){
        if(config == null) config = new SyntheaGeccoConfig()
        return config
    }

    enum GeccoVersion{
        V1_0_3("1.0.3"), V1_0_4("1.0.4"), V1_0_5("1.0.5")

        private String version
        private static HashMap<String, GeccoVersion> map = new HashMap<>().tap {it ->
            GeccoVersion.values().each {value -> it[value.toString()] = value}
        }

        GeccoVersion(String version){
            this.version = version
        }

        String toString(){
            return this.version
        }

        static GeccoVersion getVersion(String version){
            return map[version]
        }
    }

    enum ValidationStrategy{
        NO_VALIDATION("no_validation"), RESOURCE_VALIDATION_ONLY("resource_validation_only"),
        FILE_VALIDATION_ONLY("file_validation_only"), COMPLETE("complete")

        private String value
        private static Map<String, ValidationStrategy> map = new HashMap<>().tap {it ->
            ValidationStrategy.values().each {strat -> it[strat.value] = strat}
        }

        ValidationStrategy(String value){
            this.value = value
        }

        String valueOf(){
            return this.value
        }

        static ValidationStrategy getValue(String strategy){
            return map[strategy]
        }
    }

    enum ResourceFiltering{
        ALL, HOSP_ADMISSION_ONLY, ICU_ADMISSION_ONLY
    }

    enum DetectedFilePolicy{
        DELETE_ON_FINISH('delete_on_finish'), ARCHIVE_ON_FINISH('archive_on_finish')

        private String value
        private static HashMap<String, DetectedFilePolicy> map = new HashMap<>().tap {it ->
            DetectedFilePolicy.values().each {policy -> it[policy.value] = policy}
        }

        DetectedFilePolicy(String value){
            this.value = value
        }

        static DetectedFilePolicy getValue(String policy){
            return map[policy]
        }

    }

    GeccoVersion getGeccoVersion() {
        return geccoVersion
    }

    void setGeccoVersion(GeccoVersion geccoVersion) {
        this.geccoVersion = geccoVersion
    }

    FhirContext getFhirContext() {
        return this.ctx
    }

    void setFhirContext(FhirContext ctx) {
        this.ctx = ctx
    }

    ValidationStrategy getValidationStrategy() {
        return this.validationStrategy
    }

    void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy
    }

    DetectedFilePolicy getDetectedFilePolicy() {
        return this.detectedFilePolicy
    }

    void setDetectedFilePolicy(DetectedFilePolicy detectedFilePolicy) {
        this.detectedFilePolicy = detectedFilePolicy
    }

    ResourceFiltering getResourceFiltering() {
        return this.resourceFiltering
    }

    void setResourceFiltering(ResourceFiltering resourceFiltering) {
        this.resourceFiltering = resourceFiltering
    }

    String getRemoteValidationUrl() {
        return remoteValidationUrl
    }

    void setRemoteValidationUrl(String remoteValidationUrl) {
        this.remoteValidationUrl = remoteValidationUrl
    }

    int getPopulation() {
        return population
    }

    void setPopulation(int population) {
        this.population = population
    }

    boolean getGenerateFhir() {
        return generateFhir
    }

    void setGenerateFhir(boolean generateFhir) {
        this.generateFhir = generateFhir
    }

    boolean getGenerateOpenEhr() {
        return generateOpenEhr
    }

    void setGenerateOpenEhr(boolean generateOpenEhr) {
        this.generateOpenEhr = generateOpenEhr
    }

    String getOutputDir() {
        return outputDir
    }

    void setOutputDir(String outputDir) {
        this.outputDir = outputDir
    }
}
