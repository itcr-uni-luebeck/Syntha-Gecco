package syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import java.lang.Double;
import java.lang.String;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Choice;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.laboratory_test_analyte.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:47:51.433123700+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class KohlendioxidpartialdruckCluster implements LocatableEntity {
  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Untersuchter Analyt
   * Description: Der Name des untersuchten Analyts.
   * Comment: Der Wert dieses Elements wird normalerweise, meist durch eine Spezialisierung, in einem Template oder zur Laufzeit der Anwendung geliefert, um den aktuellen Analyt wiederzugeben. Zum Beispiel: 'Natrium im Serum', 'Hämoglobin'. 
   * Die Codierung mit einer externen Terminologie, wie LOINC, NPU, SNOMED-CT oder lokalen Labor-Terminologien wird dringend empfohlen.
   */
  @Path("/items[at0024]/value|defining_code")
  private UntersuchterAnalytDefiningCode untersuchterAnalytDefiningCode;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Event Series/Jedes Ereignis/Tree/Kohlendioxidpartialdruck/Untersuchter Analyt/null_flavour
   */
  @Path("/items[at0024]/null_flavour|defining_code")
  private NullFlavour untersuchterAnalytNullFlavourDefiningCode;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Analyt-Ergebnis
   * Description: (Mess-)Wert des Analyt-Resultats.
   * Comment: z.B. "7,3 mmol/l", "Erhöht". Der "Any"-Datentyp wird dann durch eine Spezialisierung, eine Vorlage oder zur Laufzeit der Anwendung auf einen passenden Datentyp eingeschränkt werden müssen, um das aktuelle Analyt-Ergebnis wiederzugeben. Der "Quantity"-Datentyp hat Referenzmodell-Attribute, wie Kennungen für normal/abnormal, Referenzbereiche und Näherungen - für weitere Details s. https://specifications.openehr.org/releases/RM/latest/data_types.html#_dv_quantity_class .
   */
  @Path("/items[at0001]/value|magnitude")
  private Double analytErgebnisMagnitude;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Analyt-Ergebnis
   * Description: (Mess-)Wert des Analyt-Resultats.
   * Comment: z.B. "7,3 mmol/l", "Erhöht". Der "Any"-Datentyp wird dann durch eine Spezialisierung, eine Vorlage oder zur Laufzeit der Anwendung auf einen passenden Datentyp eingeschränkt werden müssen, um das aktuelle Analyt-Ergebnis wiederzugeben. Der "Quantity"-Datentyp hat Referenzmodell-Attribute, wie Kennungen für normal/abnormal, Referenzbereiche und Näherungen - für weitere Details s. https://specifications.openehr.org/releases/RM/latest/data_types.html#_dv_quantity_class .
   */
  @Path("/items[at0001]/value|units")
  private String analytErgebnisUnits;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Event Series/Jedes Ereignis/Tree/Kohlendioxidpartialdruck/Analyt-Ergebnis/null_flavour
   */
  @Path("/items[at0001]/null_flavour|defining_code")
  private NullFlavour analytErgebnisNullFlavourDefiningCode;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Analyseergebnis-Details
   * Description: Weitere Details zu einem einzelnen Ergebnis.
   */
  @Path("/items[at0014]")
  private List<Cluster> analyseergebnisDetails;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Event Series/Jedes Ereignis/Tree/Kohlendioxidpartialdruck/Testmethode/null_flavour
   */
  @Path("/items[at0028]/null_flavour|defining_code")
  private NullFlavour testmethodeNullFlavourDefiningCode;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Ergebnis-Status
   * Description: Status des Analyseergebnisses.
   * Comment: Die Werte wurden analog zum HL7 FHIR Diagnostic Report gewählt, die wiederum aus der HL7-Praxis stammen. Andere Codes/Ausdrücke können über den Text 'choice' verwendet werden.
   */
  @Path("/items[at0005]")
  private List<KohlendioxidpartialdruckErgebnisStatusElement> ergebnisStatus;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("/items[at0028]/value")
  @Choice
  private KohlendioxidpartialdruckTestmethodeChoice testmethode;

  public void setUntersuchterAnalytDefiningCode(
      UntersuchterAnalytDefiningCode untersuchterAnalytDefiningCode) {
     this.untersuchterAnalytDefiningCode = untersuchterAnalytDefiningCode;
  }

  public UntersuchterAnalytDefiningCode getUntersuchterAnalytDefiningCode() {
     return this.untersuchterAnalytDefiningCode ;
  }

  public void setUntersuchterAnalytNullFlavourDefiningCode(
      NullFlavour untersuchterAnalytNullFlavourDefiningCode) {
     this.untersuchterAnalytNullFlavourDefiningCode = untersuchterAnalytNullFlavourDefiningCode;
  }

  public NullFlavour getUntersuchterAnalytNullFlavourDefiningCode() {
     return this.untersuchterAnalytNullFlavourDefiningCode ;
  }

  public void setAnalytErgebnisMagnitude(Double analytErgebnisMagnitude) {
     this.analytErgebnisMagnitude = analytErgebnisMagnitude;
  }

  public Double getAnalytErgebnisMagnitude() {
     return this.analytErgebnisMagnitude ;
  }

  public void setAnalytErgebnisUnits(String analytErgebnisUnits) {
     this.analytErgebnisUnits = analytErgebnisUnits;
  }

  public String getAnalytErgebnisUnits() {
     return this.analytErgebnisUnits ;
  }

  public void setAnalytErgebnisNullFlavourDefiningCode(
      NullFlavour analytErgebnisNullFlavourDefiningCode) {
     this.analytErgebnisNullFlavourDefiningCode = analytErgebnisNullFlavourDefiningCode;
  }

  public NullFlavour getAnalytErgebnisNullFlavourDefiningCode() {
     return this.analytErgebnisNullFlavourDefiningCode ;
  }

  public void setAnalyseergebnisDetails(List<Cluster> analyseergebnisDetails) {
     this.analyseergebnisDetails = analyseergebnisDetails;
  }

  public List<Cluster> getAnalyseergebnisDetails() {
     return this.analyseergebnisDetails ;
  }

  public void setTestmethodeNullFlavourDefiningCode(
      NullFlavour testmethodeNullFlavourDefiningCode) {
     this.testmethodeNullFlavourDefiningCode = testmethodeNullFlavourDefiningCode;
  }

  public NullFlavour getTestmethodeNullFlavourDefiningCode() {
     return this.testmethodeNullFlavourDefiningCode ;
  }

  public void setErgebnisStatus(
      List<KohlendioxidpartialdruckErgebnisStatusElement> ergebnisStatus) {
     this.ergebnisStatus = ergebnisStatus;
  }

  public List<KohlendioxidpartialdruckErgebnisStatusElement> getErgebnisStatus() {
     return this.ergebnisStatus ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }

  public void setTestmethode(KohlendioxidpartialdruckTestmethodeChoice testmethode) {
     this.testmethode = testmethode;
  }

  public KohlendioxidpartialdruckTestmethodeChoice getTestmethode() {
     return this.testmethode ;
  }
}
