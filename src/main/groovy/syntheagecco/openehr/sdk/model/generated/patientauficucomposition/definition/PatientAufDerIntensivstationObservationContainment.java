package syntheagecco.openehr.sdk.model.generated.patientauficucomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

public class PatientAufDerIntensivstationObservationContainment extends Containment {
  public SelectAqlField<PatientAufDerIntensivstationObservation> PATIENT_AUF_DER_INTENSIVSTATION_OBSERVATION = new AqlFieldImp<PatientAufDerIntensivstationObservation>(PatientAufDerIntensivstationObservation.class, "", "PatientAufDerIntensivstationObservation", PatientAufDerIntensivstationObservation.class, this);

  public SelectAqlField<String> NAME_DER_AKTIVITAET_VALUE = new AqlFieldImp<String>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0004]/value|value", "nameDerAktivitaetValue", String.class, this);

  public SelectAqlField<NullFlavour> NAME_DER_AKTIVITAET_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0004]/null_flavour|defining_code", "nameDerAktivitaetNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<VorhandenseinDefiningCode> VORHANDENSEIN_DEFINING_CODE = new AqlFieldImp<VorhandenseinDefiningCode>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0005]/value|defining_code", "vorhandenseinDefiningCode", VorhandenseinDefiningCode.class, this);

  public SelectAqlField<NullFlavour> VORHANDENSEIN_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0005]/null_flavour|defining_code", "vorhandenseinNullFlavourDefiningCode", NullFlavour.class, this);

  public ListSelectAqlField<Cluster> DETAILLIERTE_ANGABEN_ZUR_AKTIVITAET = new ListAqlFieldImp<Cluster>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/data[at0003]/items[at0022]/items[at0036]", "detaillierteAngabenZurAktivitaet", Cluster.class, this);

  public SelectAqlField<TemporalAccessor> TIME_VALUE = new AqlFieldImp<TemporalAccessor>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/events[at0002]/time|value", "timeValue", TemporalAccessor.class, this);

  public SelectAqlField<TemporalAccessor> ORIGIN_VALUE = new AqlFieldImp<TemporalAccessor>(PatientAufDerIntensivstationObservation.class, "/data[at0001]/origin|value", "originValue", TemporalAccessor.class, this);

  public ListSelectAqlField<Cluster> ERWEITERUNG = new ListAqlFieldImp<Cluster>(PatientAufDerIntensivstationObservation.class, "/protocol[at0007]/items[at0021]", "erweiterung", Cluster.class, this);

  public SelectAqlField<PartyProxy> SUBJECT = new AqlFieldImp<PartyProxy>(PatientAufDerIntensivstationObservation.class, "/subject", "subject", PartyProxy.class, this);

  public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(PatientAufDerIntensivstationObservation.class, "/language", "language", Language.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(PatientAufDerIntensivstationObservation.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  private PatientAufDerIntensivstationObservationContainment() {
    super("openEHR-EHR-OBSERVATION.management_screening.v0");
  }

  public static PatientAufDerIntensivstationObservationContainment getInstance() {
    return new PatientAufDerIntensivstationObservationContainment();
  }
}
