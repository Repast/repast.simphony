/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.NonModelAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;

/**
 * Abstract implementation of a DataSetManager. This does scheduling and data
 * collection on the master context.
 * 
 * @author Nick Collier
 */
public abstract class AbstractDataSetManager implements DataSetManager, RunListener {

  @SuppressWarnings("rawtypes")
  protected static class ObjList implements ContextListener, SizedIterable<Object> {

    Class<?> targetType;
    Set<Object> objs = new HashSet<Object>();

    public ObjList(Class<?> targetType) {
      this.targetType = targetType;
    }

    @SuppressWarnings("unchecked")
    void init(Context<?> context) {
      if (targetType.isAssignableFrom(context.getClass())) {
        objs.add(context);
      } else {
        for (Object obj : context) {
          if (targetType.isAssignableFrom(obj.getClass())) {
            objs.add(obj);
          }
        }
      }
      context.addContextListener(this);
    }

    @SuppressWarnings("unchecked")
    void reset(Context<?> context) {
      context.removeContextListener(this);
      objs.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.context.ContextListener#eventOccured(repast.simphony.
     * context.ContextEvent)
     */
    @Override
    public void eventOccured(ContextEvent ev) {
      boolean isMatch = targetType.isAssignableFrom(ev.getTarget().getClass());
      if (isMatch) {
        if (ev.getType() == ContextEvent.ADDED)
          objs.add(ev.getTarget());
        else if (ev.getType() == ContextEvent.REMOVED)
          objs.remove(ev.getTarget());
      }
    }

    public int size() {
      return objs.size();
    }

    public Iterator<Object> iterator() {
      return objs.iterator();
    }
  }

  @NonModelAction
  protected static class ScheduledDataSet implements IAction {

    DataSet dataSet;
    ScheduleParameters scheduleParams;
    Map<Class<?>, SizedIterable<?>> objMap;
    boolean atEnd;

    /**
     * @param set
     * @param scheduleParams
     */
    ScheduledDataSet(DataSet set, ScheduleParameters scheduleParams, boolean atEnd) {
      this.dataSet = set;
      this.scheduleParams = scheduleParams;
      this.atEnd = atEnd;
    }

    void reset(Context<?> context) {
      for (SizedIterable<?> si : objMap.values()) {
        if (si instanceof ObjList)
          ((ObjList) si).reset(context);
      }
    }

    @Override
    public void execute() {
      dataSet.record(objMap);
    }
  }

  private static SizedIterable<Object> NULL_SIZED_ITERABLE = new SizedIterable<Object>() {

    public int size() {
      return 0;
    }

    public Iterator<Object> iterator() {
      return null;
    }
  };

  private TickCountDataSource tickCountDataSource;
  private RandomSeedDataSource rndSeedDataSource;
  private BatchRunDataSource batchRunDataSource;

  protected Map<String, DataSetBuilder<?>> builders = new HashMap<String, DataSetBuilder<?>>();
  protected List<ScheduledDataSet> dataSets = new ArrayList<ScheduledDataSet>();

  public AbstractDataSetManager() {
    tickCountDataSource = new TickCountDataSource();
    rndSeedDataSource = new RandomSeedDataSource();
    batchRunDataSource = new BatchRunDataSource();
  }

  /**
   * Gets the BatchRunDataSource that this manager will auto update with the
   * current run number.
   * 
   * @return the BatchRunDataSource that this manager will auto update with the
   *         current run number.
   */
  public BatchRunDataSource getBatchRunDataSource() {
    return batchRunDataSource;
  }

  public TickCountDataSource getTickCountDataSource() {
    return tickCountDataSource;
  }

  public RandomSeedDataSource getRandomSeedDataSource() {
    return rndSeedDataSource;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.data2.DataSetManager#addDataSet(repast.simphony.data2.DataSet
   * )
   */
  @Override
  public void addDataSet(DataSet dataSet, ScheduleParameters scheduleParams, boolean atEnd) {
    dataSets.add(new ScheduledDataSet(dataSet, scheduleParams, atEnd));
  }
  
  /**
   * Notifies the DataSets managed by this DataSetManager to tell
   * their DataSinks to flush any buffered data.
   */
  public void flush() {
    for (ScheduledDataSet dataSet : dataSets) {
      dataSet.dataSet.flush();
    }
  }

  /**
   * Clears this data set manager of any added datasets.
   */
  public void clearDataSets() {
    dataSets.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSetManager#runStarted(int,
   * repast.simphony.engine.schedule.ISchedule)
   */
  @Override
  public void runStarted(RunState runState, Object contextId, Parameters parameters) {
    runState.getScheduleRegistry().getScheduleRunner().addRunListener(this);
    
    tickCountDataSource.resetSchedule(runState.getScheduleRegistry().getModelSchedule());
    rndSeedDataSource.resetSeed((Integer) parameters
        .getValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME));
    batchRunDataSource.resetBatchRun(runState.getRunInfo().getRunNumber());
  
    Map<Class<?>, SizedIterable<?>> listMap = new HashMap<Class<?>, SizedIterable<?>>();
    Context<?> context = runState.getMasterContext().findContext(contextId);
    for (ScheduledDataSet source : dataSets) {
      for (Class<?> sourceType : source.dataSet.getSourceTypes()) {
        if (!listMap.containsKey(sourceType)) {
          // void is a flag indicating that the DataSource will
          // ignore any objects passed into its get method. Consequently,
          // we just pass in placeholder SizedIterator
          if (sourceType.equals(void.class)) {
            listMap.put(sourceType, NULL_SIZED_ITERABLE);
          } else {
            ObjList list = new ObjList(sourceType);
            list.init(context);
            listMap.put(sourceType, list);
          }
        }
        source.objMap = listMap;
      }

      ISchedule schedule = runState.getScheduleRegistry().getModelSchedule(); 
      schedule.schedule(source.scheduleParams, source);
      if (source.atEnd) schedule.schedule(ScheduleParameters.createAtEnd(ScheduleParameters.LAST_PRIORITY), source);
    }
  }

  public void runEnded(RunState runState, Object contextId) {
    runState.getScheduleRegistry().getScheduleRunner().removeRunListener(this);
    for (ScheduledDataSet source : dataSets) {
      source.reset(runState.getMasterContext());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.data2.DataSetManager#addDataSetBuilder(repast.simphony.
   * data2.builder.DataSetBuilder)
   */
  @Override
  public void addDataSetBuilder(DataSetBuilder<?> builder) {
    builders.put(builder.getId(), builder);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.data2.DataSetManager#getDataSetBuilder(java.lang.String)
   */
  @Override
  public DataSetBuilder<?> getDataSetBuilder(String id) {
    return builders.get(id);
  }

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.RunListener#stopped()
   */
  @Override
  public void stopped() {
    flush();
  }

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.RunListener#paused()
   */
  @Override
  public void paused() {
    flush();
  }

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.RunListener#started()
   */
  @Override
  public void started() {}

  /* (non-Javadoc)
   * @see repast.simphony.engine.environment.RunListener#restarted()
   */
  @Override
  public void restarted() {}
  
}
