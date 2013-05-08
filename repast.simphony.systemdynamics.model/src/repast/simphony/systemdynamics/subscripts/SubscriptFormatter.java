/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

/**
 * @author Nick Collier
 */
public class SubscriptFormatter {
  
  private String eq = "";
  
  public SubscriptFormatter(String equation) {
    this.eq = equation;
  }
 
  /**
   * formats the specified subscript for adding at the specified location.
   * 
   * @param loc
   * @param subscript
   */
  public String format(int loc, String subscript) {
    
    String left = eq.substring(0, loc);
    String right = eq.substring(loc, eq.length());
    
    // is loc inside brackets.
    boolean inBrackets = false;
    int leftBIndex = left.lastIndexOf("[");
    if (leftBIndex != -1 && leftBIndex > left.lastIndexOf("]")) {
      int rightBIndex = right.indexOf("]");
      int index = right.indexOf("[");
      inBrackets = rightBIndex != -1 && (rightBIndex < index || index == -1);
    }
    
    if (inBrackets) {
    	 return "," + subscript;
    } else {
      return "[" + subscript + "]";
    }
  }
}