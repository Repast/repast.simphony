package repast.simphony.plugin;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;

/**
 * @author Nick Collier
 * 
 */
public class ScheduledActionsCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.SCHEDULE_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}

	@Override
	public boolean isMasterOnly() {
		return false;
	}
}
