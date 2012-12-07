/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Exception thrown by the BatchParameterChunker.
 * 
 * @author Nick Collier
 */
public class BatchParameterChunkerException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   * @param cause
   */
  public BatchParameterChunkerException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public BatchParameterChunkerException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public BatchParameterChunkerException(Throwable cause) {
    super(cause);
  }
}
