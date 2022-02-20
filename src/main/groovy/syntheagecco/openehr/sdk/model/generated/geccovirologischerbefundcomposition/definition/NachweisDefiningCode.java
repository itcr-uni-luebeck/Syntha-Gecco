package syntheagecco.openehr.sdk.model.generated.geccovirologischerbefundcomposition.definition;

import java.lang.String;
import org.ehrbase.client.classgenerator.EnumValueSet;

public enum NachweisDefiningCode implements EnumValueSet {
  NOT_DETECTED_QUALIFIER_VALUE("Not detected (qualifier value)", "", "http://snomed.info/sct", "260415000"),

  INCONCLUSIVE_QUALIFIER_VALUE("Inconclusive (qualifier value)", "", "http://snomed.info/sct", "419984006"),

  DETECTED_QUALIFIER_VALUE("Detected (qualifier value)", "", "http://snomed.info/sct", "260373001");

  private String value;

  private String description;

  private String terminologyId;

  private String code;

  NachweisDefiningCode(String value, String description, String terminologyId, String code) {
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
