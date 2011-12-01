package repast.simphony.parameter;

/**
 * Exception thrown when illegally setting a parameter via Parameters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class IllegalParameterException extends RuntimeException {
	public IllegalParameterException() {
	}

	public IllegalParameterException(Throwable cause) {
		super(cause);
	}

	public IllegalParameterException(String message) {
		super(message);
	}

	public IllegalParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
