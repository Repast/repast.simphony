/**
 * 
 */
package repast.simphony.chart2.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.scenario.DescriptorActionLoader;

import com.thoughtworks.xstream.XStream;

/**
 * @author Nick Collier
 */
public class TimeSeriesControllerActionIO extends
    AbstractDescriptorControllerActionIO<TimeSeriesComponentControllerAction, TimeSeriesChartDescriptor> {

  public static class TimeSeriesActionLoader extends DescriptorActionLoader<TimeSeriesChartDescriptor> {

    public TimeSeriesActionLoader(File file, Object contextID) {
      super(file, contextID, TimeSeriesChartDescriptor.class, ControllerActionConstants.CHART_ROOT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#createAction(java.lang.Object
     * )
     */
    @Override
    protected ControllerAction createAction(TimeSeriesChartDescriptor data) {
      return new TimeSeriesComponentControllerAction(data);
    }


    /* (non-Javadoc)
     * @see repast.simphony.scenario.ObjectActionLoader#prepare(com.thoughtworks.xstream.XStream)
     */
    @Override
    protected void prepare(XStream xstream) {
      xstream.alias("SeriesData", TimeSeriesChartDescriptor.SeriesData.class);
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

  public TimeSeriesControllerActionIO() {
    super(TimeSeriesComponentControllerAction.class, TimeSeriesChartDescriptor.class);
  }

  public String getSerializationID() {
    return "repast.simphony.action.time_series_chart";
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new TimeSeriesActionLoader(actionFile, contextID);
  }

  /* (non-Javadoc)
   * @see repast.simphony.scenario.AbstractDescriptorControllerActionIO#getActionSaver()
   */
  @SuppressWarnings("rawtypes")
  @Override
  public ActionSaver getActionSaver() {
    return new ActionSaver() {
      /* (non-Javadoc)
       * @see repast.simphony.scenario.ActionSaver#save(com.thoughtworks.xstream.XStream, repast.simphony.engine.environment.ControllerAction, java.lang.String)
       */
      @Override
      public void save(XStream xstream, ControllerAction action, String filename)
          throws IOException {
          xstream.alias("SeriesData", TimeSeriesChartDescriptor.SeriesData.class);
          Descriptor descriptor = ((TimeSeriesComponentControllerAction)action).getDescriptor();
          xstream.toXML(descriptor, new FileWriter(filename));
      }
    };
  }
  
  
}
