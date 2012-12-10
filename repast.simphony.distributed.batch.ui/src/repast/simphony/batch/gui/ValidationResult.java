/**
 * 
 */
package repast.simphony.batch.gui;

/**
 * Encapsualtes the result of a validating an input field.
 * 
 * @author Nick Collier
 */
public class ValidationResult {
  
  
  public static ValidationResult SUCCESS = new ValidationResult(""); 
  
  
  private String message = "";
  
  public ValidationResult(String message) {
    this.message = message;
  }
 
  public String getMessage() {
    return message;
  }
}
