/**
 * 
 */
package repast.simphony.data2.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.data2.BatchParamMapFileWriter;
import repast.simphony.data2.BatchRunDataSource;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.data2.builder.FileDataSinkBuilder;
import repast.simphony.data2.builder.FileNameFormatter;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
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
  private List<BatchParamMapFileWriter> writers = new ArrayList<BatchParamMapFileWriter>();

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
  
  

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.DefaultControllerAction#runInitialize(repast.simphony.engine.environment.RunState, java.lang.Object)
   */
  @Override
  public void runInitialize(RunState runState, Object contextId, Parameters params) {
    for (BatchParamMapFileWriter writer : writers) {
      writer.runStarted();
    }
  }

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.DefaultControllerAction#batchCleanup(repast.simphony.engine.environment.RunState, java.lang.Object)
   */
  @Override
  public void batchCleanup(RunState runState, Object contextId) {
    writers.clear();
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
      msgCenter.error("Error while creating FileDataSink. DataSet '" + descriptor.getDataSet()
          + "' not found", new NullPointerException());
    } else {
      
      FileNameFormatter fnFormatter = new FileNameFormatter(descriptor.getFileName(), descriptor.isAddTimeStamp());
      FileDataSinkBuilder sinkBuilder = new FileDataSinkBuilder(descriptor.getName(), fnFormatter, descriptor.getDelimiter(),
          descriptor.getFormat());
      
      // if the file sink doesn't have the batch run data source
      // and we are in batch mode, then add the batch run data source.
      // Note: the BatchRunDataSource will be automatically added in the DataSetComponentControllerAction
      // when we are in batch mode.
      if (runState.getRunInfo().isBatch() && !descriptor.getSourceIds().contains(BatchRunDataSource.ID)) {
        sinkBuilder.addSource(BatchRunDataSource.ID);
      }
      
      for (String sourceId : descriptor.getSourceIds()) {
        sinkBuilder.addSource(sourceId);
      }
      
      builder.addFileDataSinkBuilder(sinkBuilder);
      
      if (runState.getRunInfo().isBatch()) {
        BatchParamMapFileWriter writer = new BatchParamMapFileWriter(manager.getBatchRunDataSource(), fnFormatter, 
            descriptor.getDelimiter(), descriptor.getFormat());
        builder.addDataSink(writer);
        writers.add(writer);
      }
    }
  }
}
