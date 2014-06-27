/**
 * 
 */
package repast.simphony.chart2.engine;

import java.util.List;

import repast.simphony.chart2.BatchUpdateXYSeries;
import repast.simphony.chart2.LineChartCreator;
import repast.simphony.chart2.XYDataSinkSourceSeries;
import repast.simphony.chart2.XYDataSinkValueSeries;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import simphony.util.messages.MessageCenter;

/**
 * ControllerAction responsible for creating DataSets from DataSetDescriptors.
 * 
 * @author Nick Collier
 */
public class TimeSeriesComponentControllerAction extends DefaultControllerAction implements
    DescriptorControllerAction<TimeSeriesChartDescriptor>, RunListener {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(TimeSeriesComponentControllerAction.class);

  private TimeSeriesChartDescriptor descriptor;
  private LineChartCreator chartCreator;
  private BatchUpdateXYSeries chartData;
  private boolean added = false;

  public TimeSeriesComponentControllerAction(TimeSeriesChartDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.engine.controller.DescriptorControllerAction#getDescriptor
   * ()
   */
  @Override
  public TimeSeriesChartDescriptor getDescriptor() {
    return descriptor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.engine.environment.DefaultControllerAction#batchInitialize
   * (repast.simphony.engine.environment.RunState, java.lang.Object)
   */
  @Override
  public void batchInitialize(RunState runState, Object contextId) {
    DataSetRegistry registry = (DataSetRegistry) runState
        .getFromRegistry(DataConstants.REGISTRY_KEY);
    DataSetManager manager = registry.getDataSetManager(contextId);
    DataSetBuilder<?> builder = manager.getDataSetBuilder(descriptor.getDataSet());

    if (builder == null) {
      msgCenter.error("Error while creating Charts. Chart DataSet '" + descriptor.getDataSet()
          + "' not found", new NullPointerException());
      return;
    }

    // create a chart data sink to feed data from the dataset to the chart.
    chartData = new BatchUpdateXYSeries(descriptor.getPlotRangeLength());
    List<String> sourceIds = descriptor.getSeriesIds();
    if (builder.isAggregate()) {
      XYDataSinkSourceSeries sink = new XYDataSinkSourceSeries(chartData, sourceIds);
      builder.addDataSink(sink);
    } else {

      // non-aggregate uses the seriesId as a data value that
      // will create a series for each unique value it gets from the
      // source with that id.
      String seriesId = sourceIds.get(0);
      for (String dataValueId : descriptor.dataValueIds()) {
        // String dataValueId = descriptor.getDataValueId();
        XYDataSinkValueSeries sink = new XYDataSinkValueSeries(chartData, seriesId, dataValueId);
        builder.addDataSink(sink);
      }
    }

    if (!added) {
      runState.getScheduleRegistry().getScheduleRunner().addRunListener(this);
      added = true;
    }
    chartCreator = new LineChartCreator(chartData);
    runState.getGUIRegistry().addComponent(chartCreator.createChartComponent(descriptor),
        GUIRegistryType.CHART, descriptor.getChartTitle());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.engine.environment.DefaultControllerAction#runCleanup(repast
   * .simphony.engine.environment.RunState, java.lang.Object)
   */
  @Override
  public void runCleanup(RunState runState, Object contextId) {
    chartCreator.reset();
    chartData = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.environment.RunListener#stopped()
   */
  @Override
  public void stopped() {
    if (chartData != null)
      chartData.setRunning(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.environment.RunListener#paused()
   */
  @Override
  public void paused() {
    if (chartData != null)
      chartData.setRunning(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.environment.RunListener#started()
   */
  @Override
  public void started() {
    if (chartData != null)
      chartData.setRunning(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.environment.RunListener#restarted()
   */
  @Override
  public void restarted() {
    if (chartData != null)
      chartData.setRunning(true);
  }
}
