package repast.simphony.batch.gui;

/**
 * Parameter types.
 * 
 * @author Nick Collier
 */
public enum ParameterType {
  
  NUMBER("Number Range"), LIST("Space Separated List"), CONSTANT("Constant"), RANDOM("Random");

  private String label;

  ParameterType(String label) {
    this.label = label;
  }

  public String toString() {
    return label;
  }
  
  public static ParameterType parse(String val) {
    if (val.equals("constant")) return CONSTANT;
    if (val.equals("list")) return LIST;
    if (val.equals("number")) return NUMBER;
    
    return null;
  }
}