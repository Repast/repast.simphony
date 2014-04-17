/*CopyrightHere*/
package repast.simphony.plugin;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;

public class MiscCompositeActionCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.MISC_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}

	@Override
	public boolean isMasterOnly() {
		return false;
	}

}
