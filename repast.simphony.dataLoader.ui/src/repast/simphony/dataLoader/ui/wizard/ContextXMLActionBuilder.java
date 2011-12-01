package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.engine.ContextXMLDataLoaderAction;
import repast.simphony.scenario.Scenario;

/**
 * Builds an action that does the ContextBuilding.
 *
 * @author Nick Collier
 */
public class ContextXMLActionBuilder implements ContextActionBuilder {

  /**
   * Gets an action that will create a context when run.
   *
   * @param scenario
   * @return an action that will create a context when run.
   */
  public DataLoaderControllerAction getAction(Scenario scenario, Object parentContextID) {
    return new ContextXMLDataLoaderAction(scenario.getContext());
  }
}
