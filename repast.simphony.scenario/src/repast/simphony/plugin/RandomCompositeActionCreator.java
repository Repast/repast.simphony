/*CopyrightHere*/
package repast.simphony.plugin;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;

public class RandomCompositeActionCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.RANDOM_LOADER_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}

}
