/**
 * 
 */
package repast.simphony.scenario;

/**
 * Event used to convey data about a scenario change.
 * @author Nick Collier
 */
public class ScenarioChangedEvent {
  
  private Object source;
  private String property;
  
  public ScenarioChangedEvent(Object source, String property) {
    this.source = source;
    this.property = property;
  }

  /**
   * Gets the source of the scenario change.
   * 
   * @return the source
   */
  public Object getSource() {
    return source;
  }

  /**
   * Gets the name of the scenario property that has changed.
   * 
   * @return the property
   */
  public String getProperty() {
    return property;
  }
}
