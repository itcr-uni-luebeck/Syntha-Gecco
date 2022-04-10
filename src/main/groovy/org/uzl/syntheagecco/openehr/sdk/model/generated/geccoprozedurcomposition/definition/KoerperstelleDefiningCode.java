package org.uzl.syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition;

import java.lang.String;
import org.ehrbase.client.classgenerator.EnumValueSet;

public enum KoerperstelleDefiningCode implements EnumValueSet {
  LUNG_STRUCTURE_BODY_STRUCTURE("Lung structure (body structure)", "", "SNOMED Clinical Terms", "39607008");

  private String value;

  private String description;

  private String terminologyId;

  private String code;

  KoerperstelleDefiningCode(String value, String description, String terminologyId, String code) {
    this.value = value;
    this.description = description;
    this.terminologyId = terminologyId;
    this.code = code;
  }

  public String getValue() {
     return this.value ;
  }

  public String getDescription() {
     return this.description ;
  }

  public String getTerminologyId() {
     return this.terminologyId ;
  }

  public String getCode() {
     return this.code ;
  }
}
