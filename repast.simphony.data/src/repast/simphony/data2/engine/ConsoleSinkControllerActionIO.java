/**
 * 
 */
package repast.simphony.data2.engine;

import java.io.File;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DescriptorActionLoader;

/**
 * @author Nick Collier
 */
public class ConsoleSinkControllerActionIO extends
    AbstractDescriptorControllerActionIO<ConsoleSinkComponentControllerAction, ConsoleSinkDescriptor> {

  public static class ConsoleSinkActionLoader extends DescriptorActionLoader<ConsoleSinkDescriptor> {

    public ConsoleSinkActionLoader(File file, Object contextID) {
      super(file, contextID, ConsoleSinkDescriptor.class, ControllerActionConstants.OUTPUTTER_ROOT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#createAction(java.lang.Object
     * )
     */
    @Override
    protected ControllerAction createAction(ConsoleSinkDescriptor data) {
      return new ConsoleSinkComponentControllerAction(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.scenario.ObjectActionLoader#getClassLoader()
     */
    @Override
    protected ClassLoader getClassLoader() {
      return getClass().getClassLoader();
    }
  }

  public ConsoleSinkControllerActionIO() {
    super(ConsoleSinkComponentControllerAction.class, ConsoleSinkDescriptor.class);
  }

  public String getSerializationID() {
    return "repast.simphony.action.console_sink";
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new ConsoleSinkActionLoader(actionFile, contextID);
  }
}
