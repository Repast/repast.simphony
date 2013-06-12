/**
 * 
 */
package repast.simphony.chart2.engine;

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
public class HistogramControllerActionIO extends
    AbstractDescriptorControllerActionIO<HistogramComponentControllerAction, HistogramChartDescriptor> {

  public static class HistogramActionLoader extends DescriptorActionLoader<HistogramChartDescriptor> {

    public HistogramActionLoader(File file, Object contextID) {
      super(file, contextID, HistogramChartDescriptor.class, ControllerActionConstants.CHART_ROOT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.scenario.ObjectActionLoader#createAction(java.lang.Object
     * )
     */
    @Override
    protected ControllerAction createAction(HistogramChartDescriptor data, Scenario scenario) {
      data.addScenarioChangedListener(scenario);
      return new HistogramComponentControllerAction(data);
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

  public HistogramControllerActionIO() {
    super(HistogramComponentControllerAction.class, HistogramChartDescriptor.class);
  }

  public String getSerializationID() {
    return "repast.simphony.action.histogram_chart";
  }

  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new HistogramActionLoader(actionFile, contextID);
  }
}
