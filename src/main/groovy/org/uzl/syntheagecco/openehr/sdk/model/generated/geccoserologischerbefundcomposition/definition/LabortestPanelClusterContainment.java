package org.uzl.syntheagecco.openehr.sdk.model.generated.geccoserologischerbefundcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.SelectAqlField;

public class LabortestPanelClusterContainment extends Containment {
  public SelectAqlField<LabortestPanelCluster> LABORTEST_PANEL_CLUSTER = new AqlFieldImp<LabortestPanelCluster>(LabortestPanelCluster.class, "", "LabortestPanelCluster", LabortestPanelCluster.class, this);

  public SelectAqlField<ProAnalytCluster> PRO_ANALYT = new AqlFieldImp<ProAnalytCluster>(LabortestPanelCluster.class, "/items[openEHR-EHR-CLUSTER.laboratory_test_analyte.v1]", "proAnalyt", ProAnalytCluster.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(LabortestPanelCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  private LabortestPanelClusterContainment() {
    super("openEHR-EHR-CLUSTER.laboratory_test_panel.v0");
  }

  public static LabortestPanelClusterContainment getInstance() {
    return new LabortestPanelClusterContainment();
  }
}
