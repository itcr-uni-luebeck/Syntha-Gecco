package syntheagecco.openehr.sdk.model.generated.befundderblutgasanalysecomposition.definition;

import com.nedap.archie.rm.datavalues.DvState;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:47:51.455122700+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_STATE")
public class KohlendioxidpartialdruckTestmethodeDvState implements RMEntity, KohlendioxidpartialdruckTestmethodeChoice {
  /**
   * Path: Befund der Blutgasanalyse/Laborergebnis/Jedes Ereignis/Kohlendioxidpartialdruck/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("")
  private DvState testmethode;

  public void setTestmethode(DvState testmethode) {
     this.testmethode = testmethode;
  }

  public DvState getTestmethode() {
     return this.testmethode ;
  }
}
