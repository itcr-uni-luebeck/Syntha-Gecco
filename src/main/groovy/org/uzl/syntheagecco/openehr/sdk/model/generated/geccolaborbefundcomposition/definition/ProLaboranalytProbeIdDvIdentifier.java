package org.uzl.syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import com.nedap.archie.rm.datavalues.DvIdentifier;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.762297300+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_IDENTIFIER")
public class ProLaboranalytProbeIdDvIdentifier implements RMEntity, ProLaboranalytProbeIdChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt/Probe ID/Probe ID
   * Description: Kennung der Probe, die für das Analyseergebnis verwendet wurde.
   */
  @Path("")
  private DvIdentifier probeId;

  public void setProbeId(DvIdentifier probeId) {
     this.probeId = probeId;
  }

  public DvIdentifier getProbeId() {
     return this.probeId ;
  }
}
