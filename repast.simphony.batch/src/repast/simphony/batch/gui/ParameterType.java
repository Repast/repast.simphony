/**
 * 
 */
package repast.simphony.batch.gui;

/**
 * Enumeration of batch parameter types -- constant, numeric etc.
 * @author Nick Collier
 */
public enum ParameterType {
 
  CONSTANT() {
    public String toString() {
     return "Constant";
    }
  }, 
  
  NUMERIC_RANGE() {
    public String toString() {
     return "Numeric Range";
    }
  }, 
  
  LIST() {
    public String toString() {
     return "List";
    }
  }
}
