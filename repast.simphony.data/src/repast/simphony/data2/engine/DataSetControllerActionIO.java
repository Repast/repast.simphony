package repast.simphony.data2.engine;

import java.io.File;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DescriptorActionLoader;
import repast.simphony.scenario.FastMethodConvertor;

import com.thoughtworks.xstream.XStream;

/**
 * ActionIO class that loads and saves DataSetComponentControllerActions from 
 * and to DataSetDescriptors. 
 * 
 * @author Nick Collier
 */
public class DataSetControllerActionIO extends
    AbstractDescriptorControllerActionIO<DataSetComponentControllerAction, DataSetDescriptor> {

  public static class DataSetActionLoader extends DescriptorActionLoader<DataSetDescriptor> {

    public DataSetActionLoader(File file, Object contextID) {
      super(file, contextID, DataSetDescriptor.class, ControllerActionConstants.DATA_SET_ROOT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#prepare(com.thoughtworks.
     * xstream.XStream)
     */
    @Override
    protected void prepare(XStream xstream) {
      xstream.registerConverter(new FastMethodConvertor(xstream));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#createAction(java.lang.Object
     * )
     */
    @Override
    protected ControllerAction createAction(DataSetDescriptor data) {
      return new DataSetComponentControllerAction(data);
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

  public DataSetControllerActionIO() {
    super(DataSetComponentControllerAction.class, DataSetDescriptor.class);
  }
  
  public String getSerializationID() {
    return "repast.simphony.action.data_set";
}

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new DataSetActionLoader(actionFile, contextID);
  }
}
