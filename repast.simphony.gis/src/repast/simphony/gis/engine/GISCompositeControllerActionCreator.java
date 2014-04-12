package repast.simphony.gis.engine;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

public class GISCompositeControllerActionCreator implements
		CompositeControllerActionCreator {

	String ID = "repast.controller.action.gis";
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public ControllerAction createControllerAction() {
		return new GeographyProjectionController();
	}

}
