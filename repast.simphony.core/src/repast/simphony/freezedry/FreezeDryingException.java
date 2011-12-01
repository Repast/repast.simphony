package repast.simphony.freezedry;

/**
 * An exception thrown when there is a problem while working with the freeze drying utilities.
 */
public class FreezeDryingException extends Exception {
	private static final long serialVersionUID = 1L;

	public FreezeDryingException(Throwable cause) {
		super(cause);
	}

	public FreezeDryingException(String message, Throwable cause) {
		super(message, cause);
	}

	public FreezeDryingException() {
		super();
	}

	public FreezeDryingException(String message) {
		super(message);
	}
}
