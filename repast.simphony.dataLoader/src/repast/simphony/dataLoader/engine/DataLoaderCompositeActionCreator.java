package repast.simphony.dataLoader.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * @author Nick Collier
 * 
 */
public class DataLoaderCompositeActionCreator implements CompositeControllerActionCreator {

	public String getID() {
		return ControllerActionConstants.DATA_LOADER_ROOT;
	}

	public ControllerAction createControllerAction() {
		return new DefaultControllerAction();
	}

	@Override
	public boolean isMasterOnly() {
		return false;
	}
}
