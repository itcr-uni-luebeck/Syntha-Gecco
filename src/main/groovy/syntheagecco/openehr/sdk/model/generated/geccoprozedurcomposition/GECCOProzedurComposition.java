package syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Id;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.annotations.Template;
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Category;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.ehrbase.client.openehrclient.VersionUid;
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.GeccoProzedurKategorieElement;
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.NichtDurchgefuehrteProzedurEvaluation;
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.ProzedurAction;
import syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.UnbekannteProzedurEvaluation;

@Entity
@Archetype("openEHR-EHR-COMPOSITION.registereintrag.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:16.551097500+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@Template("GECCO_Prozedur")
public class GECCOProzedurComposition implements CompositionEntity {
  /**
   * Path: GECCO_Prozedur/category
   */
  @Path("/category|defining_code")
  private Category categoryDefiningCode;

  /**
   * Path: GECCO_Prozedur/context/Erweiterung
   * Description: Ergänzende Angaben zum Registereintrag.
   */
  @Path("/context/other_context[at0001]/items[at0002]")
  private List<Cluster> erweiterung;

  /**
   * Path: GECCO_Prozedur/context/Kategorie
   * Description: Die Klassifikation des Registereintrags (z.B. Typ der Observation des FHIR-Profils).
   */
  @Path("/context/other_context[at0001]/items[at0005]")
  private List<GeccoProzedurKategorieElement> kategorie;

  /**
   * Path: GECCO_Prozedur/context/start_time
   */
  @Path("/context/start_time|value")
  private TemporalAccessor startTimeValue;

  /**
   * Path: GECCO_Prozedur/context/participations
   */
  @Path("/context/participations")
  private List<Participation> participations;

  /**
   * Path: GECCO_Prozedur/context/end_time
   */
  @Path("/context/end_time|value")
  private TemporalAccessor endTimeValue;

  /**
   * Path: GECCO_Prozedur/context/location
   */
  @Path("/context/location")
  private String location;

  /**
   * Path: GECCO_Prozedur/context/health_care_facility
   */
  @Path("/context/health_care_facility")
  private PartyIdentified healthCareFacility;

  /**
   * Path: GECCO_Prozedur/context/setting
   */
  @Path("/context/setting|defining_code")
  private Setting settingDefiningCode;

  /**
   * Path: GECCO_Prozedur/Prozedur
   * Description: Eine klinische Aktivität, die zur Früherkennung, Untersuchung, Diagnose, Heilung, Therapie, Bewertung oder in Hinsicht auf palliative Maßnahmen durchgeführt wird.
   */
  @Path("/content[openEHR-EHR-ACTION.procedure.v1]")
  private ProzedurAction prozedur;

  /**
   * Path: GECCO_Prozedur/Nicht durchgeführte Prozedur
   * Description: Ein Bericht über den Ausschluss eines/r Problems/Diagnose, familiäre Krankengeschichte, Medikation, Nebenwirkung/Allergens oder eines anderen klinischen Ereignisses, welche/s zur Zeit nicht oder noch nie vorhanden war.
   */
  @Path("/content[openEHR-EHR-EVALUATION.exclusion_specific.v1 and name/value='Nicht durchgeführte Prozedur']")
  private NichtDurchgefuehrteProzedurEvaluation nichtDurchgefuehrteProzedur;

  /**
   * Path: GECCO_Prozedur/Unbekannte Prozedur
   * Description: Aussage darüber, dass bestimmte Gesundheitsinformationen zum Zeitpukt der Erfassung nicht in der Krankenakte oder einem Schriftstück erfasst werden können, da keine Kenntnisse darüber vorhanden sind.
   */
  @Path("/content[openEHR-EHR-EVALUATION.absence.v2 and name/value='Unbekannte Prozedur']")
  private UnbekannteProzedurEvaluation unbekannteProzedur;

  /**
   * Path: GECCO_Prozedur/composer
   */
  @Path("/composer")
  private PartyProxy composer;

  /**
   * Path: GECCO_Prozedur/language
   */
  @Path("/language")
  private Language language;

  /**
   * Path: GECCO_Prozedur/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  /**
   * Path: GECCO_Prozedur/territory
   */
  @Path("/territory")
  private Territory territory;

  @Id
  private VersionUid versionUid;

  public void setCategoryDefiningCode(Category categoryDefiningCode) {
     this.categoryDefiningCode = categoryDefiningCode;
  }

  public Category getCategoryDefiningCode() {
     return this.categoryDefiningCode ;
  }

  public void setErweiterung(List<Cluster> erweiterung) {
     this.erweiterung = erweiterung;
  }

  public List<Cluster> getErweiterung() {
     return this.erweiterung ;
  }

  public void setKategorie(List<GeccoProzedurKategorieElement> kategorie) {
     this.kategorie = kategorie;
  }

  public List<GeccoProzedurKategorieElement> getKategorie() {
     return this.kategorie ;
  }

  public void setStartTimeValue(TemporalAccessor startTimeValue) {
     this.startTimeValue = startTimeValue;
  }

  public TemporalAccessor getStartTimeValue() {
     return this.startTimeValue ;
  }

  public void setParticipations(List<Participation> participations) {
     this.participations = participations;
  }

  public List<Participation> getParticipations() {
     return this.participations ;
  }

  public void setEndTimeValue(TemporalAccessor endTimeValue) {
     this.endTimeValue = endTimeValue;
  }

  public TemporalAccessor getEndTimeValue() {
     return this.endTimeValue ;
  }

  public void setLocation(String location) {
     this.location = location;
  }

  public String getLocation() {
     return this.location ;
  }

  public void setHealthCareFacility(PartyIdentified healthCareFacility) {
     this.healthCareFacility = healthCareFacility;
  }

  public PartyIdentified getHealthCareFacility() {
     return this.healthCareFacility ;
  }

  public void setSettingDefiningCode(Setting settingDefiningCode) {
     this.settingDefiningCode = settingDefiningCode;
  }

  public Setting getSettingDefiningCode() {
     return this.settingDefiningCode ;
  }

  public void setProzedur(ProzedurAction prozedur) {
     this.prozedur = prozedur;
  }

  public ProzedurAction getProzedur() {
     return this.prozedur ;
  }

  public void setNichtDurchgefuehrteProzedur(
      NichtDurchgefuehrteProzedurEvaluation nichtDurchgefuehrteProzedur) {
     this.nichtDurchgefuehrteProzedur = nichtDurchgefuehrteProzedur;
  }

  public NichtDurchgefuehrteProzedurEvaluation getNichtDurchgefuehrteProzedur() {
     return this.nichtDurchgefuehrteProzedur ;
  }

  public void setUnbekannteProzedur(UnbekannteProzedurEvaluation unbekannteProzedur) {
     this.unbekannteProzedur = unbekannteProzedur;
  }

  public UnbekannteProzedurEvaluation getUnbekannteProzedur() {
     return this.unbekannteProzedur ;
  }

  public void setComposer(PartyProxy composer) {
     this.composer = composer;
  }

  public PartyProxy getComposer() {
     return this.composer ;
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

  public void setTerritory(Territory territory) {
     this.territory = territory;
  }

  public Territory getTerritory() {
     return this.territory ;
  }

  public VersionUid getVersionUid() {
     return this.versionUid ;
  }

  public void setVersionUid(VersionUid versionUid) {
     this.versionUid = versionUid;
  }
}
