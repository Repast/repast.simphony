package repast.simphony.plugin;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;

/**
 * @author Nick Collier
 */
public class ControllerActionExtensions {

	private List<CompositeControllerActionCreator> parents = new ArrayList<CompositeControllerActionCreator>();

	public ControllerActionExtensions() {
//		parents.add(new ScheduledActionsCreator());
//		parents.add(new RandomCompositeActionCreator());
	}

	public void addCompositeActionCreator(CompositeControllerActionCreator creator) {
		parents.add(creator);
	}

	public void addParentControllerActions(Object contextID, ControllerRegistry registry) {
		for (CompositeControllerActionCreator composite : parents) {

			if (composite.isMasterOnly()){
				if (contextID.equals(registry.getMasterContextId())){
					ControllerAction action = composite.createControllerAction();
					registry.addAction(contextID, null, action);
					registry.registerAction(contextID, composite.getID(), action);
				}
				else{
					// do nothing.
				}
			}
			else{
				ControllerAction action = composite.createControllerAction();
				registry.addAction(contextID, null, action);
				registry.registerAction(contextID, composite.getID(), action);
			}
		}
	}

	public Iterable<CompositeControllerActionCreator> parentActionCreators() {
		return parents;
	}

  /**
   *
   * @param name the class name
   * @return true if the parents contains a class with the specified name.
   */
  public boolean parentsContains(String name) {
    for (CompositeControllerActionCreator creator : parents) {
      if (creator.getClass().getName().equals(name)) return true;
    }
    return false;
  }
}
