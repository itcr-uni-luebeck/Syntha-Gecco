package syntheagecco.openehr.sdk.model.generated.geccolaborbefundcomposition.definition;

import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-09-01T01:48:06.764294600+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_CODED_TEXT")
public class ProLaboranalytErgebnisStatusDvCodedText implements RMEntity, ProLaboranalytErgebnisStatusChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Jedes Ereignis/Pro Laboranalyt/Ergebnis-Status/Ergebnis-Status
   * Description: Status des Analyt-Ergebniswertes.
   */
  @Path("|defining_code")
  private ErgebnisStatusDefiningCode ergebnisStatusDefiningCode;

  public void setErgebnisStatusDefiningCode(ErgebnisStatusDefiningCode ergebnisStatusDefiningCode) {
     this.ergebnisStatusDefiningCode = ergebnisStatusDefiningCode;
  }

  public ErgebnisStatusDefiningCode getErgebnisStatusDefiningCode() {
     return this.ergebnisStatusDefiningCode ;
  }
}
