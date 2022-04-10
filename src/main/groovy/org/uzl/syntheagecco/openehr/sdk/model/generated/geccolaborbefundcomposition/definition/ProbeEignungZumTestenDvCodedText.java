package org.uzl.syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.671294800+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_CODED_TEXT")
public class ProbeEignungZumTestenDvCodedText implements RMEntity, ProbeEignungZumTestenChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Probe/Eignung zum Testen/Eignung zum Testen
   * Description: Angabe, ob die Probe für die Untersuchung geeignet war.
   */
  @Path("|defining_code")
  private EignungZumTestenDefiningCode eignungZumTestenDefiningCode;

  public void setEignungZumTestenDefiningCode(
      EignungZumTestenDefiningCode eignungZumTestenDefiningCode) {
     this.eignungZumTestenDefiningCode = eignungZumTestenDefiningCode;
  }

  public EignungZumTestenDefiningCode getEignungZumTestenDefiningCode() {
     return this.eignungZumTestenDefiningCode ;
  }
}
