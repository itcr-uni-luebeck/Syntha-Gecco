package org.uzl.syntheagecco.openehr.sdk.model.generated.geccoserologischerbefundcomposition.definition;

import java.time.temporal.Temporal;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:23.011816400+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_DATE")
public class ProAnalytQuantitativesErgebnisDvDate implements RMEntity, ProAnalytQuantitativesErgebnisChoice {
  /**
   * Path: GECCO_Serologischer Befund/Befund/Jedes Ereignis/Labortest-Panel/Pro Analyt/Quantitatives Ergebnis/Quantitatives Ergebnis
   * Description: (Mess-)Wert des Analyt-Resultats.
   */
  @Path("|value")
  private Temporal quantitativesErgebnisValue;

  public void setQuantitativesErgebnisValue(Temporal quantitativesErgebnisValue) {
     this.quantitativesErgebnisValue = quantitativesErgebnisValue;
  }

  public Temporal getQuantitativesErgebnisValue() {
     return this.quantitativesErgebnisValue ;
  }
}
