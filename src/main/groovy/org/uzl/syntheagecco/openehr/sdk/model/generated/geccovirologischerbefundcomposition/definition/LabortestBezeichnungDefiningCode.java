package org.uzl.syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition;

import java.lang.String;
import org.ehrbase.client.classgenerator.EnumValueSet;

public enum LabortestBezeichnungDefiningCode implements EnumValueSet {
  DETECTION_OF_VIRUS_PROCEDURE("Detection of virus (procedure)", "", "http://snomed.info/sct", "122442008");

  private String value;

  private String description;

  private String terminologyId;

  private String code;

  LabortestBezeichnungDefiningCode(String value, String description, String terminologyId,
      String code) {
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
