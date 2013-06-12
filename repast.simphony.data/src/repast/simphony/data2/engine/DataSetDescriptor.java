/**
 * 
 */
package repast.simphony.data2.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.iterators.IteratorChain;

import repast.simphony.data2.AggregateOp;
import repast.simphony.data2.BatchRunDataSource;
import repast.simphony.data2.RandomSeedDataSource;
import repast.simphony.data2.TickCountDataSource;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.scenario.AbstractDescriptor;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * Descriptor that defines a single DataSet.
 * 
 * @author Nick Collier
 */
public class DataSetDescriptor extends AbstractDescriptor {

  public enum DataSetType {
    AGGREGATE, NON_AGGREGATE
  }

  private DataSetType type = DataSetType.AGGREGATE;
  private String sourceType;
  private boolean inclTick, inclBatchRun, inclRandomSeed;
  private ScheduleParameters scheduleParams = ScheduleParameters.createRepeating(1, 1,
      ScheduleParameters.LAST_PRIORITY);
  private boolean atEnd = false;

  private Map<String, MethodDataSourceDefinition> methodDataSources = new LinkedHashMap<String, MethodDataSourceDefinition>();
  private Map<String, CountSourceDefinition> countSources = new LinkedHashMap<String, CountSourceDefinition>();
  private Map<String, CustomDataSourceDefinition> customNADataSources = new LinkedHashMap<String, CustomDataSourceDefinition>();
  private Map<String, CustomDataSourceDefinition> customAggDataSources = new LinkedHashMap<String, CustomDataSourceDefinition>();

  public DataSetDescriptor(String name) {
    this(name, DataSetType.AGGREGATE);
  }

  public DataSetDescriptor(String name, DataSetType type) {
    super(name);
    this.type = type;
    inclTick = true;
  }

  public Iterable<String> dataSourceIds() {
    IteratorChain<String> chain = new IteratorChain<String>(methodDataSources.keySet().iterator());

    List<String> defaultSources = new ArrayList<String>();
    if (inclTick)
      defaultSources.add(TickCountDataSource.ID);
    if (inclBatchRun)
      defaultSources.add(BatchRunDataSource.ID);
    if (inclRandomSeed)
      defaultSources.add(RandomSeedDataSource.ID);
    chain.addIterator(defaultSources.iterator());

    if (type == DataSetType.AGGREGATE) {
      chain.addIterator(countSources.keySet().iterator());
      chain.addIterator(customAggDataSources.keySet().iterator());
    } else {
      chain.addIterator(customNADataSources.keySet().iterator());
    }

    return new IterableAdaptor<String>(chain);
  }

  /**
   * @return the scheduleParams
   */
  public ScheduleParameters getScheduleParameters() {
    return scheduleParams;
  }

  /**
   * Adds a NonAggregateDataSource by className. The specified class must
   * implement NonAggregateDataSource.
   * 
   * @param className
   */
  public void addNonAggregateDataSource(String id, String className) {
    customNADataSources.put(id, new CustomDataSourceDefinition(id, className));
    scs.fireScenarioChanged(this, "customNADataSources");
  }

  /**
   * Adds an AggregateDataSource by className. The specified class must
   * implementAggregateDataSource.
   * 
   * @param className
   */
  public void addAggregateDataSource(String id, String className) {
    customAggDataSources.put(id, new CustomDataSourceDefinition(id, className));
    scs.fireScenarioChanged(this, "customAggDataSources");
  }
  
  public void removeCustomDataSource(String id) {
    if (customAggDataSources.remove(id) != null) {
      scs.fireScenarioChanged(this, "customAggDataSources");
    }
    
    if (customNADataSources.remove(id) != null) {
      scs.fireScenarioChanged(this, "customNADataSources");
    }
  }

  public Iterable<CustomDataSourceDefinition> customAggDataSources() {
    return customAggDataSources.values();
  }

  public Iterable<CustomDataSourceDefinition> customNonAggDataSources() {
    return customNADataSources.values();
  }

  /**
   * @param scheduleParams
   *          the scheduleParams to set
   */
  public void setScheduleParameters(ScheduleParameters scheduleParams) {
    if (!this.scheduleParams.equals(scheduleParams)) {
      this.scheduleParams = scheduleParams;
      scs.fireScenarioChanged(this, "scheduleParameters");
    }
  }

  /**
   * @return the inclTick
   */
  public boolean includeTick() {
    return inclTick;
  }

  /**
   * @param inclTick
   *          the inclTick to set
   */
  public void setIncludeTick(boolean inclTick) {
    if (this.inclTick != inclTick) {
      this.inclTick = inclTick;
      scs.fireScenarioChanged(this, "inclTick");
    }
  }

  /**
   * @return the inclBatchRun
   */
  public boolean includeBatchRun() {
    return inclBatchRun;
  }

  /**
   * @param inclBatchRun
   *          the inclBatchRun to set
   */
  public void setIncludeBatchRun(boolean inclBatchRun) {
    if (this.inclBatchRun != inclBatchRun) {
      this.inclBatchRun = inclBatchRun;
      scs.fireScenarioChanged(this, "inclBatchRun");
    }
  }

  /**
   * @return the inclRandomSeed
   */
  public boolean includeRandomSeed() {
    return inclRandomSeed;
  }

  /**
   * @param inclRandomSeed
   *          the inclRandomSeed to set
   */
  public void setIncludeRandomSeed(boolean inclRandomSeed) {
    if (this.inclRandomSeed != inclRandomSeed) {
      this.inclRandomSeed = inclRandomSeed;
      scs.fireScenarioChanged(this, "inclRandomSeed");
    }
  }

  /**
   * @return the targetType
   */
  public String getSourceType() {
    return sourceType;
  }

  // ONLY FOR NON AGGREGATE COLLECTION
  public void setSourceType(String sourceType) {
    if (!this.sourceType.equals(sourceType)) {
      this.sourceType = sourceType;
      scs.fireScenarioChanged(this, "sourceType");
    }
  }

  /**
   * @return the type
   */
  public DataSetType getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(DataSetType type) {
    if (this.type != type) {
      this.type = type;
      scs.fireScenarioChanged(this, "type");
    }
  }

  public boolean isScheduleAtEnd() {
    return atEnd;
  }

  public void setScheduleAtEnd(boolean val) {
    if (this.atEnd != val) {
      atEnd = val;
      scs.fireScenarioChanged(this, "atEnd");
    }
  }

  private void checkId(String id) {
    if (methodDataSources.containsKey(id) || countSources.containsKey(id))
      throw new IllegalArgumentException("Duplicate data source ids ('" + id
          + "') are not not allowed.");
  }

  public void addMethodDataSource(String id, String className, String methodName) {
    checkId(id);
    methodDataSources.put(id, new MethodDataSourceDefinition(id, className, methodName));
    scs.fireScenarioChanged(this, "methodDataSources");
  }

  public void addAggregateMethodDataSource(String id, String className, String methodName,
      AggregateOp aggType) {
    if (type != DataSetType.AGGREGATE)
      throw new IllegalArgumentException(
          "Cannot add an aggregate data source in non-aggregate descriptor");
    checkId(id);
    MethodDataSourceDefinition mds = new MethodDataSourceDefinition(id, className, methodName);
    mds.setAggregateOp(aggType);
    methodDataSources.put(id, mds);
    scs.fireScenarioChanged(this, "methodDataSources");
  }

  public void addCountDataSource(String id, String className) {
    if (type != DataSetType.AGGREGATE)
      throw new IllegalArgumentException(
          "Cannot add an aggregate count data source in non-aggregate descriptor");
    checkId(id);
    countSources.put(id, new CountSourceDefinition(id, className));
    scs.fireScenarioChanged(this, "countDataSources");
  }

  public Iterable<CountSourceDefinition> countDataSources() {
    return countSources.values();
  }

  public void removeMethodDataSource(String id) {
    if (methodDataSources.containsKey(id)) {
      methodDataSources.remove(id);
      scs.fireScenarioChanged(this, "methodDataSources");
    }
  }
  
  public void removeCountDataSource(String id) {
    if (countSources.containsKey(id)) {
      countSources.remove(id);
      scs.fireScenarioChanged(this, "countDataSources");
    }
  }

  public Iterable<MethodDataSourceDefinition> methodDataSources() {
    return methodDataSources.values();
  }

  public void clearCountDataSources() {
    if (countSources.size() > 0) {
      countSources.clear();
      scs.fireScenarioChanged(this, "countDataSources");
    }
  }

  public void clearMethodDataSources() {
    if (methodDataSources.size() > 0) {
      methodDataSources.clear();
      scs.fireScenarioChanged(this, "methodDataSources");
    }
  }

  public void clearCustomDataSources() {
    if (customNADataSources.size() > 0) {
      customNADataSources.clear();
      scs.fireScenarioChanged(this, "customNADataSources");
    }
    if (customAggDataSources.size() > 0) {
      customAggDataSources.clear();
      scs.fireScenarioChanged(this, "customAggDataSources");
    }
  }
}
