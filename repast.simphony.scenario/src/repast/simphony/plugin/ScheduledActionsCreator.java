package repast.simphony.plugin;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public class ScheduledActionsCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.SCHEDULE_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}
}
