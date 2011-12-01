package repast.simphony.dataLoader.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.scenario.DefaultControllerActionIO;
import repast.simphony.scenario.Scenario;
import repast.simphony.scenario.data.ContextData;

import com.thoughtworks.xstream.XStream;

/**
 * A ControllerActionIO for loading / saving ScoreDataLoaderActions
 * 
 * @author Nick Collier
 */
public class MIContextXMLDataLoaderControllerActionIO extends
    DefaultControllerActionIO<MIContextXMLDataLoaderAction> {

  private static class ScoreActionLoader implements ActionLoader {
    private Object contextID;

    private ScoreActionLoader(Object contextID) {
      this.contextID = contextID;
    }

    public void loadAction(XStream xstream, Scenario scenario, ControllerRegistry registry)
        throws FileNotFoundException {
      ContextData master = scenario.getContext();
      MIContextXMLDataLoaderAction action;
      ContextData data = master.find(contextID.toString());
      action = new MIContextXMLDataLoaderAction(data, scenario.getModelData());
      ControllerAction parent = registry.findAction(contextID,
          ControllerActionConstants.DATA_LOADER_ROOT);
      registry.addAction(contextID, parent, action);
    }
  }

  public MIContextXMLDataLoaderControllerActionIO() {
    super(MIContextXMLDataLoaderAction.class, null);
  }

  public ActionSaver getActionSaver() {
    return new ActionSaver() {
      // empty implementation because the a ContextXMLContextBuilder doesn't need any
      // persistent information beyond the context.xml file
      public void save(XStream xstream, ControllerAction action, String filename)
          throws IOException {
      }
    };
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new ScoreActionLoader(contextID);
  }
}