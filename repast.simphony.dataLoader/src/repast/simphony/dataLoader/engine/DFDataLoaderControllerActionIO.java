package repast.simphony.dataLoader.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.scenario.DefaultControllerActionIO;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.scenario.ActionLoader;

import java.io.File;
import java.io.Serializable;

/**
 * A ControllerActionIO for loading / saving DelimitedFileDataLoaderControllerAction-s.
 * 
 * @author Nick Collier
 */
public class DFDataLoaderControllerActionIO extends DefaultControllerActionIO<DFDataLoaderControllerAction> {

	public DFDataLoaderControllerActionIO() {
		super(DFDataLoaderControllerAction.class, ControllerActionConstants.DATA_SET_ROOT);
	}

  public ActionSaver getActionSaver() {
		return new DFDataLoaderActionSaver();
	}

	public ActionLoader getActionLoader(File actionFile, Object contextID) {
		return new DFDataLoaderActionLoader(actionFile, contextID);
	}

}
