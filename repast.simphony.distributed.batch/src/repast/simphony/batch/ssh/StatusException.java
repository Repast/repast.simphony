/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Exception thrown by the RemoteStatus code.
 * 
 * @author Nick Collier
 */
public class StatusException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   * @param cause
   */
  public StatusException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public StatusException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public StatusException(Throwable cause) {
    super(cause);
  }
}
