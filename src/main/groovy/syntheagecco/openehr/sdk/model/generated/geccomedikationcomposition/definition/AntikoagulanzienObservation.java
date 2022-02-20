package syntheagecco.openehr.sdk.model.generated.geccomedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
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
@Archetype("openEHR-EHR-OBSERVATION.medication_statement.v0")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:10.204035900+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class AntikoagulanzienObservation implements EntryEntity {
  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/Arzneimittel-Name
   * Description: Name des Medikaments, des Impfstoffs oder eines anderen therapeutischen / verschreibungsfähigen Mittels.
   * Comment: Es wird dringend empfohlen, das „Arzneimittel“ mit einer Terminologie zu codieren, die nach Möglichkeit Entscheidungsunterstützung auslösen kann. Das Ausmaß der Codierung kann vom einfachen generischen oder Handelsnamen des Medikamentes bis hin zu strukturierten Angaben über die tatsächlich zu verwendende Medikamentenpackung variieren. Die Freitexteingabe sollte nur verwendet werden, wenn keine geeignete Terminologie verfügbar ist.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0006]/value|defining_code")
  private ArzneimittelNameDefiningCode4 arzneimittelNameDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/History/Beliebiges Ereignis/Tree/Arzneimittel-Name/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0006]/null_flavour|defining_code")
  private NullFlavour arzneimittelNameNullFlavourDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/Herstellungsdetails
   * Description: Strukturierte Angaben zur Gesamtzubereitung einschließlich Wirkstärke, Verabreichungsform und Inhaltsstoffe.
   * Comment: Verwenden Sie diesen SLOT, in dem die detaillierte Beschreibung des angeordneten Arzneimittels explizit angegeben werden muss. Zum Beispiel: Form, Wirkstärke, Verdünnungsmittel oder Mischung von Wirkstoffen.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0007]")
  private List<Cluster> herstellungsdetails;

  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/Status
   * Description: Der Status der Anwendung des Medikaments.
   * Comment: Zum Beispiel: Das Medikament wird immer noch aktiv eingenommen oder eine Antibiotikatherapie wurde abgeschlossen.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0008]/value|defining_code")
  private StatusDefiningCode treeStatusDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/History/Beliebiges Ereignis/Tree/Status/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0008]/null_flavour|defining_code")
  private NullFlavour statusNullFlavourDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/Strukturierte Dosis- und Zeitangaben
   * Description: Details zu strukturierten Dosis- und Zeitangaben.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0022]")
  private List<Cluster> strukturierteDosisUndZeitangaben;

  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/Grund
   * Description: Der klinische Grund für die Anwendung des Medikaments.
   * Comment: Zum Beispiel: "Angina". Die Codierung der klinischen Indikation mit einer Terminologie wird nach Möglichkeit bevorzugt. Dieses Datenelement kann mehrfach vorkommen.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0023 and name/value='Grund']/value|defining_code")
  private GrundDefiningCode grundDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/History/Beliebiges Ereignis/Tree/Grund/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0023 and name/value='Grund']/null_flavour|defining_code")
  private NullFlavour grundNullFlavourDefiningCode;

  /**
   * Path: Medikation/Antikoagulanzien/Beliebiges Ereignis/time
   */
  @Path("/data[at0001]/events[at0002]/time|value")
  private TemporalAccessor timeValue;

  /**
   * Path: Medikation/Antikoagulanzien/origin
   */
  @Path("/data[at0001]/origin|value")
  private TemporalAccessor originValue;

  /**
   * Path: Medikation/Antikoagulanzien/Status
   * Description: Ein Code, der die Beurteilung des Patienten oder einer anderen Beurteilungsquelle über den Status des angewendeten Medikaments darstellt, mit dem sich diese Angabe befasst. Im Allgemeinen wird dies aktiv oder abgeschlossen sein.
   */
  @Path("/protocol[at0004]/items[openEHR-EHR-CLUSTER.medication_status_fhir.v0 and name/value='Status']")
  private StatusCluster itemTreeStatus;

  /**
   * Path: Medikation/Antikoagulanzien/subject
   */
  @Path("/subject")
  private PartyProxy subject;

  /**
   * Path: Medikation/Antikoagulanzien/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: Medikation/Antikoagulanzien/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setArzneimittelNameDefiningCode(
      ArzneimittelNameDefiningCode4 arzneimittelNameDefiningCode) {
     this.arzneimittelNameDefiningCode = arzneimittelNameDefiningCode;
  }

  public ArzneimittelNameDefiningCode4 getArzneimittelNameDefiningCode() {
     return this.arzneimittelNameDefiningCode ;
  }

  public void setArzneimittelNameNullFlavourDefiningCode(
      NullFlavour arzneimittelNameNullFlavourDefiningCode) {
     this.arzneimittelNameNullFlavourDefiningCode = arzneimittelNameNullFlavourDefiningCode;
  }

  public NullFlavour getArzneimittelNameNullFlavourDefiningCode() {
     return this.arzneimittelNameNullFlavourDefiningCode ;
  }

  public void setHerstellungsdetails(List<Cluster> herstellungsdetails) {
     this.herstellungsdetails = herstellungsdetails;
  }

  public List<Cluster> getHerstellungsdetails() {
     return this.herstellungsdetails ;
  }

  public void setTreeStatusDefiningCode(StatusDefiningCode treeStatusDefiningCode) {
     this.treeStatusDefiningCode = treeStatusDefiningCode;
  }

  public StatusDefiningCode getTreeStatusDefiningCode() {
     return this.treeStatusDefiningCode ;
  }

  public void setStatusNullFlavourDefiningCode(NullFlavour statusNullFlavourDefiningCode) {
     this.statusNullFlavourDefiningCode = statusNullFlavourDefiningCode;
  }

  public NullFlavour getStatusNullFlavourDefiningCode() {
     return this.statusNullFlavourDefiningCode ;
  }

  public void setStrukturierteDosisUndZeitangaben(List<Cluster> strukturierteDosisUndZeitangaben) {
     this.strukturierteDosisUndZeitangaben = strukturierteDosisUndZeitangaben;
  }

  public List<Cluster> getStrukturierteDosisUndZeitangaben() {
     return this.strukturierteDosisUndZeitangaben ;
  }

  public void setGrundDefiningCode(GrundDefiningCode grundDefiningCode) {
     this.grundDefiningCode = grundDefiningCode;
  }

  public GrundDefiningCode getGrundDefiningCode() {
     return this.grundDefiningCode ;
  }

  public void setGrundNullFlavourDefiningCode(NullFlavour grundNullFlavourDefiningCode) {
     this.grundNullFlavourDefiningCode = grundNullFlavourDefiningCode;
  }

  public NullFlavour getGrundNullFlavourDefiningCode() {
     return this.grundNullFlavourDefiningCode ;
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

  public void setItemTreeStatus(StatusCluster itemTreeStatus) {
     this.itemTreeStatus = itemTreeStatus;
  }

  public StatusCluster getItemTreeStatus() {
     return this.itemTreeStatus ;
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
