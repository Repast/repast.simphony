/**
 * 
 */
package repast.simphony.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Support class for registering ScenarioChagedListeners and firing scenario changed events.
 * 
 * @author Nick Collier
 */
public class ScenarioChangedSupport {
  
  private List<ScenarioChangedListener> listeners = new ArrayList<>();
  
  /**
   * Adds a ScenarioChangedListener that will be notified when events are fired.
   * 
   * @param listener
   */
  public void addListener(ScenarioChangedListener listener) {
    listeners.add(listener);
  }
  
  /**
   * Fires a ScenarioChangeEvent to all the listeners. 
   * 
   * @param src
   * @param property
   */
  public void fireScenarioChanged(Object src, String property) {
    ScenarioChangedEvent evt = new ScenarioChangedEvent(src, property);
    for (ScenarioChangedListener listener : listeners) {
      listener.scenarioChanged(evt);
    }
  }
}
