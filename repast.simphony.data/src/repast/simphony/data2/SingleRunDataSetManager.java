/**
 * 
 */
package repast.simphony.data2;

import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;

/**
 * DataSetManager for single (i.e. non-batch) runs.
 * 
 * @author Nick Collier
 */
public class SingleRunDataSetManager extends AbstractDataSetManager {

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#batchStarted()
   */
  @Override
  public void batchStarted() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#batchEnded()
   */
  @Override
  public void batchEnded() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#runEnded()
   */
  @Override
  public void runEnded(RunState runState, Object contextId) {
    for (ScheduledDataSet set : dataSets) {
      set.dataSet.close();
    }
    super.runEnded(runState, contextId);
    dataSets.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AbstractDataSetManager#runStarted(int,
   * repast.simphony.engine.schedule.ISchedule)
   */
  @Override
  public void runStarted(RunState runState, Object contextId, Parameters parameters) {
    DataSetRegistry registry = (DataSetRegistry) runState.getFromRegistry(DataConstants.REGISTRY_KEY);
    for (DataSetBuilder<?> builder : builders.values()) {
      DataSet dataSet = builder.create();
      addDataSet(dataSet, builder.getScheduleParameters());
      for (DataSink sink : dataSet.sinks()) {
        if (sink instanceof FileDataSink) {
          registry.addFileDataSink((FileDataSink)sink);
        }
      }
    }
    super.runStarted(runState, contextId, parameters);
    
    for (ScheduledDataSet set : dataSets) {
      set.dataSet.init();
      set.execute();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#isBatch()
   */
  @Override
  public boolean isBatch() {
    return false;
  }
}
