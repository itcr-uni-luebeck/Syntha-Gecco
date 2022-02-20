package syntheagecco.openehr.sdk.model.generated.patientauficucomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-OBSERVATION.management_screening.v0")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:50.370936500+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class PatientAufDerIntensivstationObservation implements EntryEntity {
  /**
   * Path: Registereintrag/Patient auf der Intensivstation/Beliebiges Ereignis/Management-/Behandlungsaktivität/Name der Aktivität
   * Description: Name der geprüften Management- oder Behandlungsaktivität.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0004]/value|value")
  private String nameDerAktivitaetValue;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/History/Beliebiges Ereignis/Tree/Management-/Behandlungsaktivität/Name der Aktivität/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0004]/null_flavour|defining_code")
  private NullFlavour nameDerAktivitaetNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/Beliebiges Ereignis/Management-/Behandlungsaktivität/Vorhandensein
   * Description: Aktueller Status der spezifischen Aktivität.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0005]/value|defining_code")
  private VorhandenseinDefiningCode vorhandenseinDefiningCode;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/History/Beliebiges Ereignis/Tree/Management-/Behandlungsaktivität/Vorhandensein/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0005]/null_flavour|defining_code")
  private NullFlavour vorhandenseinNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/Beliebiges Ereignis/Management-/Behandlungsaktivität/Detaillierte Angaben zur Aktivität
   * Description: Zusätzliche detaillierte Angaben zu der spezifischen Aktivität.
   * Comment: Zum Beispiel: Details zur Sauerstofftherapie.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0036]")
  private List<Cluster> detaillierteAngabenZurAktivitaet;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/Beliebiges Ereignis/time
   */
  @Path("/data[at0001]/events[at0002]/time|value")
  private TemporalAccessor timeValue;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/origin
   */
  @Path("/data[at0001]/origin|value")
  private TemporalAccessor originValue;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/Erweiterung
   * Description: Zusätzliche Informationen zur Erfassung lokaler Inhalte oder Anpassung an andere Referenzmodelle/Formalismen.
   * Comment: Zum Beispiel: Lokaler Informationsbedarf oder zusätzliche Metadaten zur Anpassung an FHIR-Ressourcen oder CIMI-Modelle.
   */
  @Path("/protocol[at0007]/items[at0021]")
  private List<Cluster> erweiterung;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/subject
   */
  @Path("/subject")
  private PartyProxy subject;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: Registereintrag/Patient auf der Intensivstation/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setNameDerAktivitaetValue(String nameDerAktivitaetValue) {
     this.nameDerAktivitaetValue = nameDerAktivitaetValue;
  }

  public String getNameDerAktivitaetValue() {
     return this.nameDerAktivitaetValue ;
  }

  public void setNameDerAktivitaetNullFlavourDefiningCode(
      NullFlavour nameDerAktivitaetNullFlavourDefiningCode) {
     this.nameDerAktivitaetNullFlavourDefiningCode = nameDerAktivitaetNullFlavourDefiningCode;
  }

  public NullFlavour getNameDerAktivitaetNullFlavourDefiningCode() {
     return this.nameDerAktivitaetNullFlavourDefiningCode ;
  }

  public void setVorhandenseinDefiningCode(VorhandenseinDefiningCode vorhandenseinDefiningCode) {
     this.vorhandenseinDefiningCode = vorhandenseinDefiningCode;
  }

  public VorhandenseinDefiningCode getVorhandenseinDefiningCode() {
     return this.vorhandenseinDefiningCode ;
  }

  public void setVorhandenseinNullFlavourDefiningCode(
      NullFlavour vorhandenseinNullFlavourDefiningCode) {
     this.vorhandenseinNullFlavourDefiningCode = vorhandenseinNullFlavourDefiningCode;
  }

  public NullFlavour getVorhandenseinNullFlavourDefiningCode() {
     return this.vorhandenseinNullFlavourDefiningCode ;
  }

  public void setDetaillierteAngabenZurAktivitaet(List<Cluster> detaillierteAngabenZurAktivitaet) {
     this.detaillierteAngabenZurAktivitaet = detaillierteAngabenZurAktivitaet;
  }

  public List<Cluster> getDetaillierteAngabenZurAktivitaet() {
     return this.detaillierteAngabenZurAktivitaet ;
  }

  public void setTimeValue(TemporalAccessor timeValue) {
     this.timeValue = timeValue;
  }

  public TemporalAccessor getTimeValue() {
     return this.timeValue ;
  }

  public void setOriginValue(TemporalAccessor originValue) {
     this.originValue = originValue;
  }

  public TemporalAccessor getOriginValue() {
     return this.originValue ;
  }

  public void setErweiterung(List<Cluster> erweiterung) {
     this.erweiterung = erweiterung;
  }

  public List<Cluster> getErweiterung() {
     return this.erweiterung ;
  }

  public void setSubject(PartyProxy subject) {
     this.subject = subject;
  }

  public PartyProxy getSubject() {
     return this.subject ;
  }

  public void setLanguage(Language language) {
     this.language = language;
  }

  public Language getLanguage() {
     return this.language ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
