package org.uzl.syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import com.nedap.archie.rm.datavalues.DvCodedText;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.747301300+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_CODED_TEXT")
public class ProLaboranalytTestmethodeDvCodedText implements RMEntity, ProLaboranalytTestmethodeChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("")
  private DvCodedText testmethode;

  public void setTestmethode(DvCodedText testmethode) {
     this.testmethode = testmethode;
  }

  public DvCodedText getTestmethode() {
     return this.testmethode ;
  }
}
