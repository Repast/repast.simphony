/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Exception thrown by the RemoteStatus code.
 * 
 * @author Nick Collier
 */
public class RemoteStatusException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   * @param cause
   */
  public RemoteStatusException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public RemoteStatusException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public RemoteStatusException(Throwable cause) {
    super(cause);
  }
}
