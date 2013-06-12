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
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 */
public class FileSinkControllerActionIO extends
    AbstractDescriptorControllerActionIO<FileSinkComponentControllerAction, FileSinkDescriptor> {
  
  public static final String SERIALIZATION_ID = "repast.simphony.action.file_sink";

  public static class FileSinkActionLoader extends DescriptorActionLoader<FileSinkDescriptor> {

    public FileSinkActionLoader(File file, Object contextID) {
      super(file, contextID, FileSinkDescriptor.class, ControllerActionConstants.OUTPUTTER_ROOT);
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#createAction(java.lang.Object
     * )
     */
    @Override
    protected ControllerAction createAction(FileSinkDescriptor data, Scenario scenario) {
      data.addScenarioChangedListener(scenario);
      return new FileSinkComponentControllerAction(data);
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

  public FileSinkControllerActionIO() {
    super(FileSinkComponentControllerAction.class, FileSinkDescriptor.class);
  }

  public String getSerializationID() {
    return SERIALIZATION_ID;
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new FileSinkActionLoader(actionFile, contextID);
  }
}
