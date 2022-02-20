package syntheagecco.openehr.sdk.model.generated.geccoserologischerbefundcomposition.definition;

import com.nedap.archie.rm.datavalues.DvState;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:22.997822100+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_STATE")
public class ProAnalytQuantitativesErgebnisDvState implements RMEntity, ProAnalytQuantitativesErgebnisChoice {
  /**
   * Path: GECCO_Serologischer Befund/Befund/Jedes Ereignis/Labortest-Panel/Pro Analyt/Quantitatives Ergebnis/Quantitatives Ergebnis
   * Description: (Mess-)Wert des Analyt-Resultats.
   */
  @Path("")
  private DvState quantitativesErgebnis;

  public void setQuantitativesErgebnis(DvState quantitativesErgebnis) {
     this.quantitativesErgebnis = quantitativesErgebnis;
  }

  public DvState getQuantitativesErgebnis() {
     return this.quantitativesErgebnis ;
  }
}
