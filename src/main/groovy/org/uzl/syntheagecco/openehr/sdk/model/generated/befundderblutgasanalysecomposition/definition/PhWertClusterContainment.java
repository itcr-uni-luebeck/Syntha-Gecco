package org.uzl.syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import java.lang.Double;
import java.lang.String;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

public class PhWertClusterContainment extends Containment {
  public SelectAqlField<PhWertCluster> PH_WERT_CLUSTER = new AqlFieldImp<PhWertCluster>(PhWertCluster.class, "", "PhWertCluster", PhWertCluster.class, this);

  public SelectAqlField<UntersuchterAnalytDefiningCode3> UNTERSUCHTER_ANALYT_DEFINING_CODE = new AqlFieldImp<UntersuchterAnalytDefiningCode3>(PhWertCluster.class, "/items[at0024]/value|defining_code", "untersuchterAnalytDefiningCode", UntersuchterAnalytDefiningCode3.class, this);

  public SelectAqlField<NullFlavour> UNTERSUCHTER_ANALYT_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(PhWertCluster.class, "/items[at0024]/null_flavour|defining_code", "untersuchterAnalytNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<Double> ANALYT_ERGEBNIS_MAGNITUDE = new AqlFieldImp<Double>(PhWertCluster.class, "/items[at0001]/value|magnitude", "analytErgebnisMagnitude", Double.class, this);

  public SelectAqlField<String> ANALYT_ERGEBNIS_UNITS = new AqlFieldImp<String>(PhWertCluster.class, "/items[at0001]/value|units", "analytErgebnisUnits", String.class, this);

  public SelectAqlField<NullFlavour> ANALYT_ERGEBNIS_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(PhWertCluster.class, "/items[at0001]/null_flavour|defining_code", "analytErgebnisNullFlavourDefiningCode", NullFlavour.class, this);

  public ListSelectAqlField<Cluster> ANALYSEERGEBNIS_DETAILS = new ListAqlFieldImp<Cluster>(PhWertCluster.class, "/items[at0014]", "analyseergebnisDetails", Cluster.class, this);

  public SelectAqlField<NullFlavour> TESTMETHODE_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(PhWertCluster.class, "/items[at0028]/null_flavour|defining_code", "testmethodeNullFlavourDefiningCode", NullFlavour.class, this);

  public ListSelectAqlField<SauerstoffpartialdruckErgebnisStatusElement> ERGEBNIS_STATUS = new ListAqlFieldImp<SauerstoffpartialdruckErgebnisStatusElement>(PhWertCluster.class, "/items[at0005]", "ergebnisStatus", SauerstoffpartialdruckErgebnisStatusElement.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(PhWertCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  public SelectAqlField<KohlendioxidpartialdruckTestmethodeChoice> TESTMETHODE = new AqlFieldImp<KohlendioxidpartialdruckTestmethodeChoice>(PhWertCluster.class, "/items[at0028]/value", "testmethode", KohlendioxidpartialdruckTestmethodeChoice.class, this);

  private PhWertClusterContainment() {
    super("openEHR-EHR-CLUSTER.laboratory_test_analyte.v1");
  }

  public static PhWertClusterContainment getInstance() {
    return new PhWertClusterContainment();
  }
}
