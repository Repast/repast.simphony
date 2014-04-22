package repast.simphony.visualization.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * Creates a the composite action on which to hang individual component display actions.
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DisplayCompositeActionCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.VIZ_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}

	@Override
	public boolean isMasterOnly() {
		return false;
	}
}
