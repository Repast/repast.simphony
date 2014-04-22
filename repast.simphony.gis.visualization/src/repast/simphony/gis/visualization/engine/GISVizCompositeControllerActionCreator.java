package repast.simphony.gis.visualization.engine;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

public class GISVizCompositeControllerActionCreator implements
		CompositeControllerActionCreator {

	String ID = "repast.controller.action.gis.viz";
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public ControllerAction createControllerAction() {
		return new GeographyVizProjectionController();
	}

	@Override
	public boolean isMasterOnly() {
		return true;
	}
}