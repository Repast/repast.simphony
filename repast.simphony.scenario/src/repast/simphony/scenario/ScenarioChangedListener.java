/**
 * 
 */
package repast.simphony.scenario;

/**
 * Interface for classes that need to be notified if a scenario has changed.
 * 
 * @author Nick Collier
 */
public interface ScenarioChangedListener {
  
  /**
   * Called whenever the Scenario has changed.
   * 
   * @param evt
   */
  void scenarioChanged(ScenarioChangedEvent evt);

}
