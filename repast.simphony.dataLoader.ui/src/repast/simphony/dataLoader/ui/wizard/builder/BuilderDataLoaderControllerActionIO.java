package repast.simphony.dataLoader.ui.wizard.builder;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.scenario.DefaultControllerActionIO;

/**
 * @author Nick Collier
 */
public class BuilderDataLoaderControllerActionIO extends DefaultControllerActionIO<BuilderDataLoaderControllerAction> {

	public BuilderDataLoaderControllerActionIO() {
		super(BuilderDataLoaderControllerAction.class, ControllerActionConstants.DATA_LOADER_ROOT);
	}
}
