package org.uzl.syntheagecco.openehr.sdk.model.generated.geccopersonendatencomposition.definition;

import java.lang.String;
import org.ehrbase.client.classgenerator.EnumValueSet;

public enum AdministrativesGeschlechtDefiningCode implements EnumValueSet {
  UNKNOWN("Unknown", "", "http://hl7.org/fhir/administrative-gender", "unknown"),

  MALE("Male", "", "http://hl7.org/fhir/administrative-gender", "male"),

  FEMALE("Female", "", "http://hl7.org/fhir/administrative-gender", "female"),

  OTHER("Other", "", "http://hl7.org/fhir/administrative-gender", "other");

  private String value;

  private String description;

  private String terminologyId;

  private String code;

  AdministrativesGeschlechtDefiningCode(String value, String description, String terminologyId,
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
