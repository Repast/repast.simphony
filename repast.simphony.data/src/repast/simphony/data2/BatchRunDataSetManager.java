/**
 * 
 */
package repast.simphony.data2;

import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;


/**
 * DataSetManager for batch runs.DataSets will be opened on batch started and
 * closed on batch ended.
 * 
 * @author Nick Collier
 */
public class BatchRunDataSetManager extends AbstractDataSetManager {

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#batchStarted()
   */
  @Override
  public void batchStarted() {
    for (DataSetBuilder<?> builder : builders.values()) {
      addDataSet(builder.create(), builder.getScheduleParameters(), builder.isScheduleAtEnd());
    }
    
    for (ScheduledDataSet set : dataSets) {
      set.dataSet.init();
    }
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.AbstractDataSetManager#runStarted(repast.simphony.engine.environment.RunState, java.lang.Object, repast.simphony.parameter.Parameters)
   */
  @Override
  public void runStarted(RunState runState, Object contextId, Parameters parameters) {
    super.runStarted(runState, contextId, parameters);
    for (ScheduledDataSet set : dataSets) {
//      set.execute();
    }
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.AbstractDataSetManager#runEnded(repast.simphony.engine.environment.RunState, java.lang.Object)
   */
  @Override
  public void runEnded(RunState runState, Object contextId) {
    flush();
    super.runEnded(runState, contextId);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#batchEnded()
   */
  @Override
  public void batchEnded() {
    for (ScheduledDataSet set : dataSets) {
      set.dataSet.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#isBatch()
   */
  @Override
  public boolean isBatch() {
    return true;
  }
}
