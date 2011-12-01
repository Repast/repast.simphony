package repast.simphony.dataLoader.engine;

import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.scenario.DefaultControllerActionIO;

import java.io.File;

/**
 * A ControllerActionIO for loading / saving XMLDataLoaderActions
 *
 * @author Nick Collier
 */
public class XMLDataLoaderControllerActionIO extends DefaultControllerActionIO<XMLDataLoaderAction> {

  public XMLDataLoaderControllerActionIO() {
    super(XMLDataLoaderAction.class, null);
  }

  public ActionSaver getActionSaver() {
    return new XMLDataLoaderActionSaver();
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new XMLDataLoaderActionLoader(actionFile, contextID);
  }
}