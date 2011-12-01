/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import simphony.util.messages.MessageCenter;

/**
 * ControllerAction responsible for creating DataSets from DataSetDescriptors.
 * 
 * @author Nick Collier
 */
public class FileSinkComponentControllerAction extends DefaultControllerAction implements
    DescriptorControllerAction<FileSinkDescriptor> {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(FileSinkComponentControllerAction.class);

  private FileSinkDescriptor descriptor;

  public FileSinkComponentControllerAction(FileSinkDescriptor descriptor) {
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
  public FileSinkDescriptor getDescriptor() {
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
    // in batch this is called before the whole batch of runs
    // in non-batch this is called before each run
    DataSetRegistry registry = (DataSetRegistry)runState.getFromRegistry(DataConstants.REGISTRY_KEY);
    DataSetManager manager = registry.getDataSetManager(contextId);
    DataSetBuilder<?> builder = manager.getDataSetBuilder(descriptor.getDataSet());

    if (builder == null) {
      msgCenter.error("Error while creating FileOutputter. DataSet '" + descriptor.getDataSet()
          + "' not found", new NullPointerException());
    } else {
      builder.defineFileDataSink(descriptor.getName(), descriptor.getFileName(), descriptor.getDelimiter(),
          descriptor.getFormat(), descriptor.isAddTimeStamp(), descriptor.getSourceIds());
    }
  }
}
