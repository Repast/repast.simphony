package repast.simphony.data2;

/**
 * Exception for any data collection related issue. This is a runtime exception
 * that allows us to unify the exception handling.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class DataException extends RuntimeException {

  /**
	 * 
	 */
  public DataException() {
    super();
  }

  /**
   * @param arg0
   * @param arg1
   */
  public DataException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public DataException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   */
  public DataException(Throwable arg0) {
    super(arg0);
  }
}
