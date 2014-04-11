package repast.simphony.visualization.engine;

import java.io.File;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DescriptorActionLoader;
import repast.simphony.scenario.FastMethodConvertor;
import repast.simphony.scenario.Scenario;

import com.thoughtworks.xstream.XStream;

/**
 * ControllerActionIO for responsible for saving and loading component display
 * actions.
 * 
 * @author Nick Collier
 */
public class DisplayControllerActionIO extends
    AbstractDescriptorControllerActionIO<DisplayComponentControllerAction, DisplayDescriptor> {

  public static class DisplayActionLoader extends DescriptorActionLoader<DisplayDescriptor> {

    public DisplayActionLoader(File file, Object contextID) {
      super(file, contextID, DisplayDescriptor.class, ControllerActionConstants.VIZ_ROOT);
    }

    @Override
    protected void prepare(XStream xstream) {
      xstream.registerConverter(new FastMethodConvertor(xstream));
    }

    @Override
    protected ControllerAction createAction(DisplayDescriptor descriptor, Scenario scenario) {
    	DisplayDescriptorValidator validator = new DisplayDescriptorValidator();
    	
    	descriptor = validator.validateDescriptor(descriptor);
    	
    	descriptor.addScenarioChangedListener(scenario);
      return new DisplayComponentControllerAction(descriptor);
    }

    @Override
    protected ClassLoader getClassLoader() {
      return getClass().getClassLoader();
    }
  }

  public DisplayControllerActionIO() {
    super(DisplayComponentControllerAction.class, DisplayDescriptor.class);
  }

  public String getSerializationID() {
    return "repast.simphony.action.display";
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new DisplayActionLoader(actionFile, contextID);
  }
}
