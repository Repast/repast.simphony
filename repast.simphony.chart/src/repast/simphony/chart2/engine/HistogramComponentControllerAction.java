/**
 * 
 */
package repast.simphony.chart2.engine;

import repast.simphony.chart2.AbstractHistogramDataset;
import repast.simphony.chart2.DynamicHistogramDataset;
import repast.simphony.chart2.HistogramChartCreator;
import repast.simphony.chart2.HistogramDataSink;
import repast.simphony.chart2.StaticHistogramDataset;
import repast.simphony.chart2.engine.HistogramChartDescriptor.HistogramType;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.engine.environment.RunState;
import simphony.util.messages.MessageCenter;

/**
 * ControllerAction responsible for creating DataSets from DataSetDescriptors.
 * 
 * @author Nick Collier
 */
public class HistogramComponentControllerAction extends DefaultControllerAction implements
    DescriptorControllerAction<HistogramChartDescriptor> {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(HistogramComponentControllerAction.class);

  private HistogramChartDescriptor descriptor;
  private HistogramChartCreator chartCreator;

  public HistogramComponentControllerAction(HistogramChartDescriptor descriptor) {
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
  public HistogramChartDescriptor getDescriptor() {
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
    DataSetRegistry registry = (DataSetRegistry)runState.getFromRegistry(DataConstants.REGISTRY_KEY);
    DataSetManager manager = registry.getDataSetManager(contextId);
    
    DataSetBuilder<?> builder = manager.getDataSetBuilder(descriptor.getDataSet());

    if (builder == null) {
      msgCenter.error("Error while creating Charts. Chart DataSet '" + descriptor.getDataSet()
          + "' not found", new NullPointerException());
      return;
    }

    HistogramDataSink sink = null;
    AbstractHistogramDataset chartData = null;
    String sourceId = descriptor.getSourceId();
    
    if (descriptor.getHistType() == HistogramType.DYNAMIC) {
      chartData = new DynamicHistogramDataset(sourceId,
          descriptor.getBinCount());
      sink = new HistogramDataSink(sourceId, chartData);
      
    } else {
      chartData = new StaticHistogramDataset(sourceId, descriptor.getMin(), 
          descriptor.getMax(), descriptor.getBinCount(),
          descriptor.getOutOfRangeHandling());
      sink = new HistogramDataSink(sourceId, chartData);
    }
    
    builder.addDataSink(sink);
    
    chartCreator = new HistogramChartCreator(chartData);
    runState.getGUIRegistry().addComponent(chartCreator.createChartComponent(descriptor),
        GUIRegistryType.CHART, descriptor.getChartTitle());
  }
}
