package syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Choice;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-OBSERVATION.laboratory_test_result.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.584296800+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
public class LaborergebnisObservation implements EntryEntity {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Labortest-Kategorie
   * Description: Name der Laboruntersuchung, die an der/den Probe(n) durchgeführt wurde.
   * Comment: Ein Laborergebnis kann sich auf ein einzelnes Analyt oder eine Analytgruppe beziehen. Dazu zählen auch komplette Panel an Parametern. 
   * Es wird dringend empfohlen, die "Labortest-Bezeichnung" anhand einer Terminologie zu kodiereren, wie zum Beispiel LOINC oder SNOMED CT. Beispiel: "Glukose", "Harnstoff", "Abstrich", "Cortisol", "Leberbiopsie". Der Name kann u.U. auch das Probenmaterial oder den Patientenstatus (z.B. "Blutzuckerspiegel nüchtern") oder andere Informationen beinhalten wie "Kalium (Blutgas)".
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0005 and name/value='Labortest-Kategorie']/value|defining_code")
  private LabortestKategorieDefiningCode labortestKategorieDefiningCode;

  /**
   * Path: Registereintrag/Laborergebnis/Event Series/Jedes Ereignis/Tree/Labortest-Kategorie/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0005 and name/value='Labortest-Kategorie']/null_flavour|defining_code")
  private NullFlavour labortestKategorieNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Probe
   * Description: Eine physikalische Probe zur Erforschung, Untersuchung oder Analyse, die von einer Person entnommen wurde oder die sich auf die Person bezieht.
   * Comment: Zum Beispiel: Gewebe, Körperflüssigkeit oder Lebensmittel.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[openEHR-EHR-CLUSTER.specimen.v1]")
  private List<ProbeCluster> probe;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt
   * Description: Ergebnis einer Laboranalyse für einen bestimmten Analytwert.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[openEHR-EHR-CLUSTER.laboratory_test_analyte.v1 and name/value='Pro Laboranalyt']")
  private ProLaboranalytCluster proLaboranalyt;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Schlussfolgerung
   * Description: Beschreibung der wichtigsten Ergebnisse.
   * Comment: Zum Beispiel: "Das Muster lässt auf eine erhebliche Nierenfunktionsstörung schließen". Der Inhalt der Zusammenfassung unterscheidet sich je nach durchgeführter Untersuchung. Diese Zusammenfassung sollte mit der kodierten "Testdiagnose" übereinstimmen.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0057]/value|value")
  private String schlussfolgerungValue;

  /**
   * Path: Registereintrag/Laborergebnis/Event Series/Jedes Ereignis/Tree/Schlussfolgerung/null_flavour
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0057]/null_flavour|defining_code")
  private NullFlavour schlussfolgerungNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Strukturierte Testdiagnostik
   * Description: Eine strukturierte oder komplexe Diagnose für die Laboruntersuchung.
   * Comment: Zum Beispiel: Anatomische Pathologiediagnosen, die aus mehreren verschiedenen Schwerpunkten wie Morphologie, Ätiologie und Funktion zusammengesetzt sind.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0122]")
  private List<Cluster> strukturierteTestdiagnostik;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Multimedia-Darstellung
   * Description: Bild, Video oder Diagramm zur Visualisierung des Testergebnisses.
   * Comment: Mehrere Formate sind erlaubt - diese sollten aber einen äquivalenten klinischen Inhalt darstellen.
   */
  @Path("/data[at0001]/events[at0002]/data[at0003]/items[at0118]")
  private List<Cluster> multimediaDarstellung;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Strukturierte Erfassung der Störfaktoren
   * Description: Einzelheiten zu Problemen oder Umständen, die sich auf die genaue Interpretation der Messung oder des Prüfergebnisses auswirken.
   * Comment: Zum Beispiel: Letzte normale Menstruationsperiode (LNMP).
   */
  @Path("/data[at0001]/events[at0002]/state[at0112]/items[at0114]")
  private List<Cluster> strukturierteErfassungDerStoerfaktoren;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/time
   */
  @Path("/data[at0001]/events[at0002]/time|value")
  private TemporalAccessor timeValue;

  /**
   * Path: Registereintrag/Laborergebnis/origin
   */
  @Path("/data[at0001]/origin|value")
  private TemporalAccessor originValue;

  /**
   * Path: Registereintrag/Laborergebnis/Labor, welches den Untersuchungsauftrag annimmt
   * Description: Angaben zu dem Labor, das die Anfrage erhalten hat und die Hauptverantwortung für die Verwaltung der Berichterstattung über den Test trägt, auch wenn andere Labore bestimmte Aspekte ausführen.
   * Comment: Dieser Slot gibt die Details des Labors an, dass die Anforderung erhalten hat und die Gesamtverantwortung für die Berichterstellung des Tests trägt, selbst wenn andere Labore bestimmte Aspekte ausführen.
   *
   * Das Empfangslabor kann den Test entweder durchführen oder an ein anderes Labor verweisen. Wenn ein anderes Labor für die Durchführung der Tests mit bestimmten Analyten zuständig ist, ist zu erwarten, dass diese Details im SLOT 'Analyte result detail' innerhalb des Archetyps CLUSTER.laboratory_test_analyte enthalten sind.
   */
  @Path("/protocol[at0004]/items[at0017]")
  private Cluster laborWelchesDenUntersuchungsauftragAnnimmt;

  /**
   * Path: Registereintrag/Laborergebnis/Tree/Details der Testanforderung/Identifikation der Laboranforderung/null_flavour
   */
  @Path("/protocol[at0004]/items[at0094]/items[at0062 and name/value='Identifikation der Laboranforderung']/null_flavour|defining_code")
  private NullFlavour identifikationDerLaboranforderungNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Laborergebnis/Details der Testanforderung/Einsender
   * Description: Details über den Kliniker oder die Abteilung, die das Labortestergebnis angefordert hat.
   */
  @Path("/protocol[at0004]/items[at0094]/items[at0090]")
  private Cluster einsender;

  /**
   * Path: Registereintrag/Laborergebnis/Details der Testanforderung/Verteilerliste
   * Description: Details über weitere Kliniker oder Organisationen, die eine Kopie der Analyseergebnisse benötigen.
   * Comment: Die "Verteilerliste" dient nur zu Informationszwecken. Der Hauptempfänger des Berichts ist die Person, die dazu bestimmt ist, auf die Information zu reagieren.
   */
  @Path("/protocol[at0004]/items[at0094]/items[at0035]")
  private List<Cluster> verteilerliste;

  /**
   * Path: Registereintrag/Laborergebnis/Tree/Testmethode/null_flavour
   */
  @Path("/protocol[at0004]/items[at0121]/null_flavour|defining_code")
  private NullFlavour testmethodeNullFlavourDefiningCode;

  /**
   * Path: Registereintrag/Laborergebnis/Test Details
   * Description: Strukturierte Details über die beim Labortest verwendete Methodik, das Gerät oder die Auswertung.
   * Comment: Zum Beispiel: "Details der ELISA/Nephelometrie".
   */
  @Path("/protocol[at0004]/items[at0110]")
  private List<Cluster> testDetails;

  /**
   * Path: Registereintrag/Laborergebnis/Erweiterung
   * Description: Weitere Informationen, die erforderlich sind, um lokale Inhalte abzubilden oder das Modell an andere Referenzmodelle anzupassen.
   * Comment: Zum Beispiel: Lokaler Informationsbedarf oder zusätzliche Metadaten, um ein Mapping auf FHIR oder CIMI Modelle zu ermöglichen.
   */
  @Path("/protocol[at0004]/items[at0117]")
  private List<Cluster> erweiterung;

  /**
   * Path: Registereintrag/Laborergebnis/subject
   */
  @Path("/subject")
  private PartyProxy subject;

  /**
   * Path: Registereintrag/Laborergebnis/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: Registereintrag/Laborergebnis/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  /**
   * Path: Registereintrag/Laborergebnis/Details der Testanforderung/Identifikation der Laboranforderung
   * Description: Lokale Auftrags-ID des anfordernden/einsendenden Systems.
   */
  @Path("/protocol[at0004]/items[at0094]/items[at0062 and name/value='Identifikation der Laboranforderung']/value")
  @Choice
  private LaborergebnisIdentifikationDerLaboranforderungChoice identifikationDerLaboranforderung;

  /**
   * Path: Registereintrag/Laborergebnis/Testmethode
   * Description: Die Beschreibung der Methode, mit dem der Test durchgeführt wurde.
   */
  @Path("/protocol[at0004]/items[at0121]/value")
  @Choice
  private LaborergebnisTestmethodeChoice testmethode;

  public void setLabortestKategorieDefiningCode(
      LabortestKategorieDefiningCode labortestKategorieDefiningCode) {
     this.labortestKategorieDefiningCode = labortestKategorieDefiningCode;
  }

  public LabortestKategorieDefiningCode getLabortestKategorieDefiningCode() {
     return this.labortestKategorieDefiningCode ;
  }

  public void setLabortestKategorieNullFlavourDefiningCode(
      NullFlavour labortestKategorieNullFlavourDefiningCode) {
     this.labortestKategorieNullFlavourDefiningCode = labortestKategorieNullFlavourDefiningCode;
  }

  public NullFlavour getLabortestKategorieNullFlavourDefiningCode() {
     return this.labortestKategorieNullFlavourDefiningCode ;
  }

  public void setProbe(List<ProbeCluster> probe) {
     this.probe = probe;
  }

  public List<ProbeCluster> getProbe() {
     return this.probe ;
  }

  public void setProLaboranalyt(ProLaboranalytCluster proLaboranalyt) {
     this.proLaboranalyt = proLaboranalyt;
  }

  public ProLaboranalytCluster getProLaboranalyt() {
     return this.proLaboranalyt ;
  }

  public void setSchlussfolgerungValue(String schlussfolgerungValue) {
     this.schlussfolgerungValue = schlussfolgerungValue;
  }

  public String getSchlussfolgerungValue() {
     return this.schlussfolgerungValue ;
  }

  public void setSchlussfolgerungNullFlavourDefiningCode(
      NullFlavour schlussfolgerungNullFlavourDefiningCode) {
     this.schlussfolgerungNullFlavourDefiningCode = schlussfolgerungNullFlavourDefiningCode;
  }

  public NullFlavour getSchlussfolgerungNullFlavourDefiningCode() {
     return this.schlussfolgerungNullFlavourDefiningCode ;
  }

  public void setStrukturierteTestdiagnostik(List<Cluster> strukturierteTestdiagnostik) {
     this.strukturierteTestdiagnostik = strukturierteTestdiagnostik;
  }

  public List<Cluster> getStrukturierteTestdiagnostik() {
     return this.strukturierteTestdiagnostik ;
  }

  public void setMultimediaDarstellung(List<Cluster> multimediaDarstellung) {
     this.multimediaDarstellung = multimediaDarstellung;
  }

  public List<Cluster> getMultimediaDarstellung() {
     return this.multimediaDarstellung ;
  }

  public void setStrukturierteErfassungDerStoerfaktoren(
      List<Cluster> strukturierteErfassungDerStoerfaktoren) {
     this.strukturierteErfassungDerStoerfaktoren = strukturierteErfassungDerStoerfaktoren;
  }

  public List<Cluster> getStrukturierteErfassungDerStoerfaktoren() {
     return this.strukturierteErfassungDerStoerfaktoren ;
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

  public void setLaborWelchesDenUntersuchungsauftragAnnimmt(
      Cluster laborWelchesDenUntersuchungsauftragAnnimmt) {
     this.laborWelchesDenUntersuchungsauftragAnnimmt = laborWelchesDenUntersuchungsauftragAnnimmt;
  }

  public Cluster getLaborWelchesDenUntersuchungsauftragAnnimmt() {
     return this.laborWelchesDenUntersuchungsauftragAnnimmt ;
  }

  public void setIdentifikationDerLaboranforderungNullFlavourDefiningCode(
      NullFlavour identifikationDerLaboranforderungNullFlavourDefiningCode) {
     this.identifikationDerLaboranforderungNullFlavourDefiningCode = identifikationDerLaboranforderungNullFlavourDefiningCode;
  }

  public NullFlavour getIdentifikationDerLaboranforderungNullFlavourDefiningCode() {
     return this.identifikationDerLaboranforderungNullFlavourDefiningCode ;
  }

  public void setEinsender(Cluster einsender) {
     this.einsender = einsender;
  }

  public Cluster getEinsender() {
     return this.einsender ;
  }

  public void setVerteilerliste(List<Cluster> verteilerliste) {
     this.verteilerliste = verteilerliste;
  }

  public List<Cluster> getVerteilerliste() {
     return this.verteilerliste ;
  }

  public void setTestmethodeNullFlavourDefiningCode(
      NullFlavour testmethodeNullFlavourDefiningCode) {
     this.testmethodeNullFlavourDefiningCode = testmethodeNullFlavourDefiningCode;
  }

  public NullFlavour getTestmethodeNullFlavourDefiningCode() {
     return this.testmethodeNullFlavourDefiningCode ;
  }

  public void setTestDetails(List<Cluster> testDetails) {
     this.testDetails = testDetails;
  }

  public List<Cluster> getTestDetails() {
     return this.testDetails ;
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

  public void setIdentifikationDerLaboranforderung(
      LaborergebnisIdentifikationDerLaboranforderungChoice identifikationDerLaboranforderung) {
     this.identifikationDerLaboranforderung = identifikationDerLaboranforderung;
  }

  public LaborergebnisIdentifikationDerLaboranforderungChoice getIdentifikationDerLaboranforderung(
      ) {
     return this.identifikationDerLaboranforderung ;
  }

  public void setTestmethode(LaborergebnisTestmethodeChoice testmethode) {
     this.testmethode = testmethode;
  }

  public LaborergebnisTestmethodeChoice getTestmethode() {
     return this.testmethode ;
  }
}
