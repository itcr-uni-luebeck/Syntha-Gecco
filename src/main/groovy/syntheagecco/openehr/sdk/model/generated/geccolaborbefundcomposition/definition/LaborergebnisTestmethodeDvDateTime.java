package syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import java.time.temporal.TemporalAccessor;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.792302800+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_DATE_TIME")
public class LaborergebnisTestmethodeDvDateTime implements RMEntity, LaborergebnisTestmethodeChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit dem der Test durchgeführt wurde.
   */
  @Path("|value")
  private TemporalAccessor testmethodeValue;

  public void setTestmethodeValue(TemporalAccessor testmethodeValue) {
     this.testmethodeValue = testmethodeValue;
  }

  public TemporalAccessor getTestmethodeValue() {
     return this.testmethodeValue ;
  }
}
