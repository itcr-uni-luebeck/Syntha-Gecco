package syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-EVALUATION.absence.v2")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:00.545570900+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class UnbekannteDiagnoseEvaluation implements EntryEntity {
  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/Unbekannte Diagnose
   * Description: Positive Aussage, dass keine Informationen verfügbar sind.
   * Comment: Zum Beispiel: "Es liegen keine Informationen über Nebenwirkungen vor"; "Es liegen keine Informationen über Probleme oder Diagnosen vor"; "Es liegen keine Informationen über vorangegangene Verfahren vor"; oder "Es liegen keine Informationen über verwendete Medikamente vor".
   */
  @Path("/data[at0001]/items[at0002 and name/value='Unbekannte Diagnose']/value|defining_code")
  private NameDesProblemsDerDiagnoseDefiningCode unbekannteDiagnoseDefiningCode;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/Baum/Unbekannte Diagnose/null_flavour
   */
  @Path("/data[at0001]/items[at0002 and name/value='Unbekannte Diagnose']/null_flavour|defining_code")
  private NullFlavour unbekannteDiagnoseNullFlavourDefiningCode;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/Aussage über die fehlende Information
   * Description: Beschreibung des Grundes, warum keine Informationen vorhanden sind.
   * Comment: Zum Beispiel: Der Patient ist bewusstlos oder weigert sich Informationen preiszugeben. Die Codierung mit einer Terminologie wird empfohlen, wenn möglich.
   */
  @Path("/data[at0001]/items[at0005 and name/value='Aussage über die fehlende Information']/value|defining_code")
  private AussageUeberDieFehlendeInformationDefiningCode aussageUeberDieFehlendeInformationDefiningCode;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/Baum/Aussage über die fehlende Information/null_flavour
   */
  @Path("/data[at0001]/items[at0005 and name/value='Aussage über die fehlende Information']/null_flavour|defining_code")
  private NullFlavour aussageUeberDieFehlendeInformationNullFlavourDefiningCode;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/Erweiterung
   * Description: Zusätzliche Informationen zur Erfassung lokaler Inhalte oder Anpassung an andere Referenzmodelle/Formalismen.
   * Comment: Kommentar: Zum Beispiel: Lokaler Informationsbedarf oder zusätzliche Metadaten zur Anpassung an FHIR-Ressourcen oder CIMI-Modelle.
   */
  @Path("/protocol[at0003]/items[at0006]")
  private List<Cluster> erweiterung;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/subject
   */
  @Path("/subject")
  private PartyProxy subject;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: GECCO_Diagnose/Unbekannte Diagnose/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setUnbekannteDiagnoseDefiningCode(
      NameDesProblemsDerDiagnoseDefiningCode unbekannteDiagnoseDefiningCode) {
     this.unbekannteDiagnoseDefiningCode = unbekannteDiagnoseDefiningCode;
  }

  public NameDesProblemsDerDiagnoseDefiningCode getUnbekannteDiagnoseDefiningCode() {
     return this.unbekannteDiagnoseDefiningCode ;
  }

  public void setUnbekannteDiagnoseNullFlavourDefiningCode(
      NullFlavour unbekannteDiagnoseNullFlavourDefiningCode) {
     this.unbekannteDiagnoseNullFlavourDefiningCode = unbekannteDiagnoseNullFlavourDefiningCode;
  }

  public NullFlavour getUnbekannteDiagnoseNullFlavourDefiningCode() {
     return this.unbekannteDiagnoseNullFlavourDefiningCode ;
  }

  public void setAussageUeberDieFehlendeInformationDefiningCode(
      AussageUeberDieFehlendeInformationDefiningCode aussageUeberDieFehlendeInformationDefiningCode) {
     this.aussageUeberDieFehlendeInformationDefiningCode = aussageUeberDieFehlendeInformationDefiningCode;
  }

  public AussageUeberDieFehlendeInformationDefiningCode getAussageUeberDieFehlendeInformationDefiningCode(
      ) {
     return this.aussageUeberDieFehlendeInformationDefiningCode ;
  }

  public void setAussageUeberDieFehlendeInformationNullFlavourDefiningCode(
      NullFlavour aussageUeberDieFehlendeInformationNullFlavourDefiningCode) {
     this.aussageUeberDieFehlendeInformationNullFlavourDefiningCode = aussageUeberDieFehlendeInformationNullFlavourDefiningCode;
  }

  public NullFlavour getAussageUeberDieFehlendeInformationNullFlavourDefiningCode() {
     return this.aussageUeberDieFehlendeInformationNullFlavourDefiningCode ;
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
