package repast.simphony.userpanel.ui;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

public class UserPanelCompositeControllerActionCreator implements
		CompositeControllerActionCreator {

	@Override
	public ControllerAction createControllerAction() {
		// Null implementation
		return new DefaultControllerAction();
	}

	@Override
	public String getID() {
		return ControllerActionConstants.USER_PANEL_ROOT;
	}

	@Override
	public boolean isMasterOnly() {
		return true;
	}
}