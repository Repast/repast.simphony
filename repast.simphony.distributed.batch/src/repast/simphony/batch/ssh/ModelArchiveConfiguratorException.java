/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Exception thrown by the BatchParameterChunker.
 * 
 * @author Nick Collier
 */
public class ModelArchiveConfiguratorException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   * @param cause
   */
  public ModelArchiveConfiguratorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public ModelArchiveConfiguratorException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public ModelArchiveConfiguratorException(Throwable cause) {
    super(cause);
  }
}
