package org.uzl.syntheagecco.openehr.sdk.model.generated.koerpergroessecomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.datastructures.ItemTree;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.Double;
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
@Archetype("openEHR-EHR-OBSERVATION.height.v2")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:47.396292400+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class GroesseLaengeObservation implements EntryEntity {
  /**
   * Path: Registereintrag/Größe/Länge/Beliebiges Ereignis/Größe/Länge
   * Description: Die Länge des Körpers vom Scheitel bis zur Fußsohle.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0004]/value|magnitude")
  private Double groesseLaengeMagnitude;

  /**
   * Path: Registereintrag/Größe/Länge/Beliebiges Ereignis/Größe/Länge
   * Description: Die Länge des Körpers vom Scheitel bis zur Fußsohle.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0004]/value|units")
  private String groesseLaengeUnits;

  /**
   * Path: Registereintrag/Größe/Länge/History/Beliebiges Ereignis/Simple/Größe/Länge/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0004]/null_flavour|defining_code")
  private NullFlavour groesseLaengeNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Größe/Länge/Beliebiges Ereignis/Tree
   * Description: @ internal @
   */
  @Path("/data[at0001]/events[at0002]/state[at0013]")
  private ItemTree tree;

  /**
   * Path: Registereintrag/Größe/Länge/Beliebiges Ereignis/time
   */
  @Path("/data[at0001]/events[at0002]/time|value")
  private TemporalAccessor timeValue;

  /**
   * Path: Registereintrag/Größe/Länge/origin
   */
  @Path("/data[at0001]/origin|value")
  private TemporalAccessor originValue;

  /**
   * Path: Registereintrag/Größe/Länge/Gerät
   * Description: Beschreibung des Geräts, das zur Messung der Größe oder Länge verwendet wurde.
   */
  @Path("/protocol[at0007]/items[at0011]")
  private Cluster geraet;

  /**
   * Path: Registereintrag/Größe/Länge/Erweiterung
   * Description: Zusätzliche Informationen zur Erfassung lokaler Inhalte oder Anpassung an andere Referenzmodelle/Formalismen.
   * Comment: Zum Beispiel: Lokaler Informationsbedarf oder zusätzliche Metadaten zur Anpassung an FHIR-Ressourcen oder CIMI-Modelle.
   */
  @Path("/protocol[at0007]/items[at0022]")
  private List<Cluster> erweiterung;

  /**
   * Path: Registereintrag/Größe/Länge/subject
   */
  @Path("/subject")
  private PartyProxy subject;

  /**
   * Path: Registereintrag/Größe/Länge/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: Registereintrag/Größe/Länge/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setGroesseLaengeMagnitude(Double groesseLaengeMagnitude) {
     this.groesseLaengeMagnitude = groesseLaengeMagnitude;
  }

  public Double getGroesseLaengeMagnitude() {
     return this.groesseLaengeMagnitude ;
  }

  public void setGroesseLaengeUnits(String groesseLaengeUnits) {
     this.groesseLaengeUnits = groesseLaengeUnits;
  }

  public String getGroesseLaengeUnits() {
     return this.groesseLaengeUnits ;
  }

  public void setGroesseLaengeNullFlavourDefiningCode(
      NullFlavour groesseLaengeNullFlavourDefiningCode) {
     this.groesseLaengeNullFlavourDefiningCode = groesseLaengeNullFlavourDefiningCode;
  }

  public NullFlavour getGroesseLaengeNullFlavourDefiningCode() {
     return this.groesseLaengeNullFlavourDefiningCode ;
  }

  public void setTree(ItemTree tree) {
     this.tree = tree;
  }

  public ItemTree getTree() {
     return this.tree ;
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

  public void setGeraet(Cluster geraet) {
     this.geraet = geraet;
  }

  public Cluster getGeraet() {
     return this.geraet ;
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
