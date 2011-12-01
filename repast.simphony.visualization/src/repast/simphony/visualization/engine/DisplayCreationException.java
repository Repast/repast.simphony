/**
 * 
 */
package repast.simphony.visualization.engine;

/**
 * @author Nick Collier
 */
public class DisplayCreationException extends Exception {

  private static final long serialVersionUID = 1L;

  
  /**
   * @param cause
   */
  public DisplayCreationException(Throwable cause) {
    super("Error while creating display.", cause);
  }
}
