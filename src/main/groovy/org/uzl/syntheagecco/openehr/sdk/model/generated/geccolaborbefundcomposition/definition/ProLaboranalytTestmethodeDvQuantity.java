package org.uzl.syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import java.lang.Double;
import java.lang.String;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.752302800+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_QUANTITY")
public class ProLaboranalytTestmethodeDvQuantity implements RMEntity, ProLaboranalytTestmethodeChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("|magnitude")
  private Double testmethodeMagnitude;

  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("|units")
  private String testmethodeUnits;

  public void setTestmethodeMagnitude(Double testmethodeMagnitude) {
     this.testmethodeMagnitude = testmethodeMagnitude;
  }

  public Double getTestmethodeMagnitude() {
     return this.testmethodeMagnitude ;
  }

  public void setTestmethodeUnits(String testmethodeUnits) {
     this.testmethodeUnits = testmethodeUnits;
  }

  public String getTestmethodeUnits() {
     return this.testmethodeUnits ;
  }
}
