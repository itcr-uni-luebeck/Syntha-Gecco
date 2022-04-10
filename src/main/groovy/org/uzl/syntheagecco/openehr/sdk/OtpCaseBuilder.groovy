package org.uzl.syntheagecco.openehr.sdk


import com.nedap.archie.rm.generic.PartySelf
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SPatient
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.GECCOPersonendatenComposition
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.AdministrativesGeschlechtDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.AlterObservation
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.DatenZurGeburtCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.EthnischerHintergrundCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.EthnischerHintergrundDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.GeschlechtBeiDerGeburtDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.GeschlechtEvaluation
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.NamensartDefiningCode
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.PersonendatenAdminEntry
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.PersonennameCluster
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition.StatusDefiningCode
import org.uzl.syntheagecco.utility.DateManipulation

class OtpCaseBuilder {

    private static final Logger logger = LogManager.getLogger(OtpCaseBuilder.class)

    private SyntheaGeccoConfig config

    OtpCaseBuilder(){}

    OtpCaseBuilder(SyntheaGeccoConfig config){
        this.config = config
    }

    SyntheaGeccoConfig getConfig() {
        return config
    }

    void setConfig(SyntheaGeccoConfig config) {
        this.config = config
    }

    OptGeccoCase buildOpenEhrGeccoCase(CaseInformation caseInfo){
        def geccoCase = new OptGeccoCase()
        def provider = CompositionProvider.getInstance()

        //First: Create Patient data template class
        geccoCase.addComposition(this.buildPatientData(caseInfo.getPatient(), caseInfo, provider))

        //Run the openEHR builders for each category
        def anBuilder = new OtpAnamnesisBuilder(this.config)
        anBuilder.buildResources(caseInfo, geccoCase)

        def demBuilder = new OptDemographicsBuilder(this.config)
        demBuilder.buildResources(caseInfo, geccoCase)

        def compBuilder = new OptComplicationsBuilder(this.config)
        compBuilder.buildResources(caseInfo, geccoCase)

        def labBuilder = new OptLaboratoryBuilder(this.config)
        labBuilder.buildResources(caseInfo, geccoCase)

        def medBuilder = new OptMedicationsBuilder(this.config)
        medBuilder.buildResources(caseInfo, geccoCase)

        def symBuilder = new OptSymptomsBuilder(this.config)
        symBuilder.buildResources(caseInfo, geccoCase)

        def vitBuilder = new OptVitalSignsBuilder(this.config)
        vitBuilder.buildResources(caseInfo, geccoCase)

        def discBuilder = new OptDischargeOutcomeBuilder(this.config)
        discBuilder.buildResources(caseInfo, geccoCase)

        return geccoCase
    }

    private CompositionEntity buildPatientData(SPatient patient, CaseInformation caseInfo, CompositionProvider provider){
        def pat = provider.getCompositionEntity(GECCOPersonendatenComposition.class)
        def config = CompositionProvider.getConfig()
        def patientSelf = new PartySelf().tap {it ->
            it.externalRef = config.getPartyProxy().getExternalRef()
        }

        pat.setStatusDefiningCode(StatusDefiningCode.FINAL)

        //TODO: Extract actual date time from first encounter resource; Currently: placeholder
        pat.setStartTimeValue(DateManipulation.localDateTimeFromDate(caseInfo.getOnset()))

        pat.setPersonendaten(new PersonendatenAdminEntry().with(true) {it ->
            subject = patientSelf

            language = config.getLanguage()

            personenname = [new PersonennameCluster().with(true) {name ->
                namensartDefiningCode = NamensartDefiningCode.REGISTRIERTER_NAME
                bevorzugterNameValue = true
                nameUnstrukturiertValue = "${patient.getGiven()} ${patient.getFamily()}"
                vornameValue = patient.getGiven()
                nachnameValue = patient.getGiven()
            }] as ArrayList<PersonennameCluster>

            datenZurGeburt = new DatenZurGeburtCluster().with(true) {birthData ->
                geburtsdatumValue = DateManipulation.localDateFromDate(patient.getBirthDate())
            } as DatenZurGeburtCluster

            //Ethnic background
            def ethnicBackground = new EthnischerHintergrundCluster()
            switch (patient.getEthnicGroup().getCode()){
                case "2135-2":
                    //Code for 'Hispanic or Latino'
                    ethnicBackground.setEthnischerHintergrundDefiningCode(EthnischerHintergrundDefiningCode.HISPANIC_OR_LATINO)
                    break
                default:
                    //Code for 'Other ethnic, mixed origin'
                    ethnicBackground.setEthnischerHintergrundDefiningCode(EthnischerHintergrundDefiningCode.OTHER_ETHNIC_MIXED_ORIGIN)
            }
            ethnischerHintergrund = [ethnicBackground]

        } as PersonendatenAdminEntry)

        //Sex assigned at birth and administrative gender
        pat.setGeschlecht(new GeschlechtEvaluation().tap {it ->
            subject = patientSelf
            language = config.getLanguage()

            switch(patient.getAssignedSex()){
                case "F":
                    administrativesGeschlechtDefiningCode = AdministrativesGeschlechtDefiningCode.FEMALE
                    geschlechtBeiDerGeburtDefiningCode = GeschlechtBeiDerGeburtDefiningCode.FEMALE
                    break
                case "M":
                    administrativesGeschlechtDefiningCode = AdministrativesGeschlechtDefiningCode.MALE
                    geschlechtBeiDerGeburtDefiningCode = GeschlechtBeiDerGeburtDefiningCode.MALE
                    break
                default:
                    administrativesGeschlechtDefiningCode = AdministrativesGeschlechtDefiningCode.UNKNOWN
                    geschlechtBeiDerGeburtDefiningCode = GeschlechtBeiDerGeburtDefiningCode.UNKNOWN
            }
        } as GeschlechtEvaluation)

        pat.setAlter(new AlterObservation().with(true) {age ->
            alterValue = patient.getAge()

            def date = DateManipulation.localDateTimeFromDate(caseInfo.getOnset())
            originValue = date
            timeValue = date

            subject = patientSelf
            language = config.getLanguage()
        } as AlterObservation)

        return pat
    }

}
