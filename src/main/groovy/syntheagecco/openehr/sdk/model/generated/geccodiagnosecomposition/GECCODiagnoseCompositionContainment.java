package syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.client.classgenerator.shareddefinition.Category;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.AusgeschlosseneDiagnoseEvaluation;
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.KategorieDefiningCode;
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.StatusDefiningCode;
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.UnbekannteDiagnoseEvaluation;
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.VorliegendeDiagnoseEvaluation;

public class GECCODiagnoseCompositionContainment extends Containment {
  public SelectAqlField<GECCODiagnoseComposition> G_E_C_C_O_DIAGNOSE_COMPOSITION = new AqlFieldImp<GECCODiagnoseComposition>(GECCODiagnoseComposition.class, "", "GECCODiagnoseComposition", GECCODiagnoseComposition.class, this);

  public SelectAqlField<Category> CATEGORY_DEFINING_CODE = new AqlFieldImp<Category>(GECCODiagnoseComposition.class, "/category|defining_code", "categoryDefiningCode", Category.class, this);

  public ListSelectAqlField<Cluster> ERWEITERUNG = new ListAqlFieldImp<Cluster>(GECCODiagnoseComposition.class, "/context/other_context[at0001]/items[at0002]", "erweiterung", Cluster.class, this);

  public SelectAqlField<StatusDefiningCode> STATUS_DEFINING_CODE = new AqlFieldImp<StatusDefiningCode>(GECCODiagnoseComposition.class, "/context/other_context[at0001]/items[at0004]/value|defining_code", "statusDefiningCode", StatusDefiningCode.class, this);

  public SelectAqlField<NullFlavour> STATUS_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(GECCODiagnoseComposition.class, "/context/other_context[at0001]/items[at0004]/null_flavour|defining_code", "statusNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<KategorieDefiningCode> KATEGORIE_DEFINING_CODE = new AqlFieldImp<KategorieDefiningCode>(GECCODiagnoseComposition.class, "/context/other_context[at0001]/items[at0005]/value|defining_code", "kategorieDefiningCode", KategorieDefiningCode.class, this);

  public SelectAqlField<NullFlavour> KATEGORIE_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(GECCODiagnoseComposition.class, "/context/other_context[at0001]/items[at0005]/null_flavour|defining_code", "kategorieNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<TemporalAccessor> START_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(GECCODiagnoseComposition.class, "/context/start_time|value", "startTimeValue", TemporalAccessor.class, this);

  public ListSelectAqlField<Participation> PARTICIPATIONS = new ListAqlFieldImp<Participation>(GECCODiagnoseComposition.class, "/context/participations", "participations", Participation.class, this);

  public SelectAqlField<TemporalAccessor> END_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(GECCODiagnoseComposition.class, "/context/end_time|value", "endTimeValue", TemporalAccessor.class, this);

  public SelectAqlField<String> LOCATION = new AqlFieldImp<String>(GECCODiagnoseComposition.class, "/context/location", "location", String.class, this);

  public SelectAqlField<PartyIdentified> HEALTH_CARE_FACILITY = new AqlFieldImp<PartyIdentified>(GECCODiagnoseComposition.class, "/context/health_care_facility", "healthCareFacility", PartyIdentified.class, this);

  public SelectAqlField<Setting> SETTING_DEFINING_CODE = new AqlFieldImp<Setting>(GECCODiagnoseComposition.class, "/context/setting|defining_code", "settingDefiningCode", Setting.class, this);

  public SelectAqlField<VorliegendeDiagnoseEvaluation> VORLIEGENDE_DIAGNOSE = new AqlFieldImp<VorliegendeDiagnoseEvaluation>(GECCODiagnoseComposition.class, "/content[openEHR-EHR-EVALUATION.problem_diagnosis.v1]", "vorliegendeDiagnose", VorliegendeDiagnoseEvaluation.class, this);

  public SelectAqlField<AusgeschlosseneDiagnoseEvaluation> AUSGESCHLOSSENE_DIAGNOSE = new AqlFieldImp<AusgeschlosseneDiagnoseEvaluation>(GECCODiagnoseComposition.class, "/content[openEHR-EHR-EVALUATION.exclusion_specific.v1]", "ausgeschlosseneDiagnose", AusgeschlosseneDiagnoseEvaluation.class, this);

  public SelectAqlField<UnbekannteDiagnoseEvaluation> UNBEKANNTE_DIAGNOSE = new AqlFieldImp<UnbekannteDiagnoseEvaluation>(GECCODiagnoseComposition.class, "/content[openEHR-EHR-EVALUATION.absence.v2]", "unbekannteDiagnose", UnbekannteDiagnoseEvaluation.class, this);

  public SelectAqlField<PartyProxy> COMPOSER = new AqlFieldImp<PartyProxy>(GECCODiagnoseComposition.class, "/composer", "composer", PartyProxy.class, this);

  public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(GECCODiagnoseComposition.class, "/language", "language", Language.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(GECCODiagnoseComposition.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  public SelectAqlField<Territory> TERRITORY = new AqlFieldImp<Territory>(GECCODiagnoseComposition.class, "/territory", "territory", Territory.class, this);

  private GECCODiagnoseCompositionContainment() {
    super("openEHR-EHR-COMPOSITION.registereintrag.v1");
  }

  public static GECCODiagnoseCompositionContainment getInstance() {
    return new GECCODiagnoseCompositionContainment();
  }
}
