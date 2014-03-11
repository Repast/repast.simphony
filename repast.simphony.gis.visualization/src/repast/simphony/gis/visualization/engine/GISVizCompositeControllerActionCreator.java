package repast.simphony.gis.visualization.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

public class GISVizCompositeControllerActionCreator implements
		CompositeControllerActionCreator {

	@Override
	public String getID() {
		return ControllerActionConstants.USER_PANEL_ROOT;
	}

	@Override
	public ControllerAction createControllerAction() {
		return new GeographyVizProjectionController();
	}

}
