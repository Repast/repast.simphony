package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.XStream;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.AbstractActionLoader;
import repast.simphony.scenario.Scenario;

import java.io.File;
import java.io.Reader;

/**
 * ActionLoader for xml serialized file based data loaders.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class XMLDataLoaderActionLoader extends AbstractActionLoader {

  public XMLDataLoaderActionLoader(File file, Object contextID) {
    super(file, contextID);
  }

  public void performLoad(Reader reader, XStream xstream, Scenario scenario, ControllerRegistry registry) {
    String fileName = (String) xstream.fromXML(reader);
    XMLDataLoaderAction action = new XMLDataLoaderAction(fileName, scenario);
    ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_LOADER_ROOT);
    registry.addAction(contextID, parent, action);
  }
}