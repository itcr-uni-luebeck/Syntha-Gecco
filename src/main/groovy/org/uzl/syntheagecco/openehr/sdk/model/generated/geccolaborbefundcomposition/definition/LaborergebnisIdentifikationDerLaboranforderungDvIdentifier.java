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
    date = "2021-09-01T01:48:06.782295600+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.5.0"
)
@OptionFor("DV_IDENTIFIER")
public class LaborergebnisIdentifikationDerLaboranforderungDvIdentifier implements RMEntity, LaborergebnisIdentifikationDerLaboranforderungChoice {
  /**
   * Path: Registereintrag/Laborergebnis/Details der Testanforderung/Identifikation der Laboranforderung/Identifikation der Laboranforderung
   * Description: Lokale Auftrags-ID des anfordernden/einsendenden Systems.
   */
  @Path("")
  private DvIdentifier identifikationDerLaboranforderung;

  public void setIdentifikationDerLaboranforderung(DvIdentifier identifikationDerLaboranforderung) {
     this.identifikationDerLaboranforderung = identifikationDerLaboranforderung;
  }

  public DvIdentifier getIdentifikationDerLaboranforderung() {
     return this.identifikationDerLaboranforderung ;
  }
}
