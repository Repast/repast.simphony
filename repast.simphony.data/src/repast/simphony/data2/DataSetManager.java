/**
 * 
 */
package repast.simphony.data2;

import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;

/**
 * Interface for classes that manage the lifecycle of DataSets.
 * 
 * @author Nick Collier
 */
public interface DataSetManager {

  /**
   * Gets the default data source for returning the tick count.
   * 
   * @return the default data source for returning the tick count.
   */
  TickCountDataSource getTickCountDataSource();

  /**
   * Gets the default data source for returning the current random seed.
   * 
   * @return the default data source for returning the current random seed.
   */
  RandomSeedDataSource getRandomSeedDataSource();

  /**
   * Gets the default data source for returning the current batch run number, or
   * null if not currently in batch run mode.
   * 
   * @return the default data source for returning the current batch run number,
   *         or null if not currently in batch run mode.
   */
  BatchRunDataSource getBatchRunDataSource();
  
  /**
   * Notifies the DataSets managed by this DataSetManager to tell
   * their DataSinks to flush any buffered data.
   */
  void flush();

  /**
   * Adds a DataSet to this DataSetManager.
   * 
   * @param dataSet
   *          the DataSet to add
   * @param scheduleParams
   *          the scheduling info for recording this dataset
   */
  void addDataSet(DataSet dataSet, ScheduleParameters scheduleParams);

  /**
   * Notifies this DataSetManager that the set of batch runs has started.
   */
  void batchStarted();

  /**
   * Notifies this DataSetManager that the set of batch runs has ended.
   */
  void batchEnded();

  /**
   * Notifies this DataSetManager that an individual run has started.
   * 
   * @param runState
   *          the current RunState -- run number etc can be retrieved from that
   * @param parameters
   *          the current model parameters
   */
  void runStarted(RunState runState, Object contextId, Parameters parameters);

  /**
   * Notifies this DataSetManager that an individual run has ended.
   * 
   * @param runState
   *          the RunState of the run that just ended.
   */
  void runEnded(RunState runState, Object contextId);

  /**
   * Gets whether or not this is a batch run.
   * 
   * @return true if this is batch run otherwise false.
   */
  boolean isBatch();
  
  /**
   * Adds a DataSetBuilder to this DataSetManager. 
   * 
   * @param builder
   */
  void addDataSetBuilder(DataSetBuilder<?> builder);
  
  /**
   * Gets the named DataSetBuilder.
   * 
   * @param id
   * 
   * @return the named DataSetBuilder.
   */
  DataSetBuilder<?> getDataSetBuilder(String id);

}
