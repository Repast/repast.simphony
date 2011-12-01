/**
 * 
 */
package repast.simphony.scenario;

/**
 * Exception thrown during scenario loading. This
 * allows us to clean up the scenario loading code a bit.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ScenarioLoadException extends Exception {

  public ScenarioLoadException() {
    super();
  }

  public ScenarioLoadException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScenarioLoadException(String message) {
    super(message);
  }

  public ScenarioLoadException(Throwable cause) {
    super(cause);
  }
}
