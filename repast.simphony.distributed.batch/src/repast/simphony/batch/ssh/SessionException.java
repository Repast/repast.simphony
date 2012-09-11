/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Exception used to wrap errors that occur during
 * Session execution.
 * 
 * @author Nick Collier
 */
public class SessionException extends Exception {

    private static final long serialVersionUID = 1L;

    public SessionException(String msg, Throwable t) {
      super(msg, t);
    }

    public SessionException(String msg) {
      super(msg);
    }
}
