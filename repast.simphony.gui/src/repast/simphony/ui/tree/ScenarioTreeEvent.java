package repast.simphony.ui.tree;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/10 17:05:07 $
 */
public class ScenarioTreeEvent {

  private Object contextID;
  private Scenario scenario;
  private ControllerRegistry registry;
  private ScenarioTree tree;
  private int row;

  public ScenarioTreeEvent(Object contextID, Scenario scenario, ControllerRegistry registry,
      ScenarioTree tree, int row) {
    this.contextID = contextID;
    this.scenario = scenario;
    this.registry = registry;
    this.tree = tree;
    this.row = row;
  }

  public Object getContextID() {
    return contextID;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public ScenarioTree getTree() {
    return tree;
  }

  public ControllerRegistry getRegistry() {
    return registry;
  }

  public void addActionToTree(ControllerAction action) {
    tree.addControllerAction(row, action);
  }

  public void removeActionFromTree(ControllerAction action) {
    tree.removeNode(action);
  }
}
