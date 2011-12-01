package repast.simphony.dataLoader.engine;

import java.io.File;

import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DefaultControllerActionIO;
import repast.simphony.scenario.ActionSaver;

/**
 * A ControllerActionIO for loading / saving ClassNameDataLoaderAction-s.
 * 
 * @author Nick Collier
 */
public class CNDataLoaderControllerActionIO extends DefaultControllerActionIO<ClassNameDataLoaderAction> {

	public CNDataLoaderControllerActionIO() {
		super(ClassNameDataLoaderAction.class, null);
	}
	
	public ActionSaver getActionSaver() {
		return new CNDataLoaderActionSaver();
	}

	public ActionLoader getActionLoader(File actionFile, Object contextID) {
		return new CNDataLoaderActionLoader(actionFile, contextID);
	}
}
