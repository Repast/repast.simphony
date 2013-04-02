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
import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * Descriptor that defines a single DataSet.
 * 
 * @author Nick Collier
 */
public class DataSetDescriptor implements Descriptor {

  public enum DataSetType {
    AGGREGATE, NON_AGGREGATE
  }

  private String name;
  private DataSetType type = DataSetType.AGGREGATE;
  private String sourceType;
  private boolean inclTick, inclBatchRun, inclRandomSeed;
  private ScheduleParameters scheduleParams = ScheduleParameters.createRepeating(1, 1, ScheduleParameters.LAST_PRIORITY);
  private boolean atEnd = false;
  
  private Map<String, MethodDataSourceDefinition> methodDataSources = new LinkedHashMap<String, MethodDataSourceDefinition>();
  private Map<String, CountSourceDefinition> countSources = new LinkedHashMap<String, CountSourceDefinition>();
  private Map<String, CustomDataSourceDefinition> customNADataSources = new LinkedHashMap<String, CustomDataSourceDefinition>();
  private Map<String, CustomDataSourceDefinition> customAggDataSources = new LinkedHashMap<String, CustomDataSourceDefinition>();
  
  public DataSetDescriptor(String name) {
    this(name, DataSetType.AGGREGATE);
  }
  
  public DataSetDescriptor(String name, DataSetType type) {
    this.name = name;
    this.type = type;
    inclTick = true;
  }
  
  public Iterable<String> dataSourceIds() {
    IteratorChain<String> chain = new IteratorChain<String>(methodDataSources.keySet().iterator());
    
    List<String> defaultSources = new ArrayList<String>();
    if (inclTick) defaultSources.add(TickCountDataSource.ID);
    if (inclBatchRun) defaultSources.add(BatchRunDataSource.ID);
    if (inclRandomSeed) defaultSources.add(RandomSeedDataSource.ID);
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
   * Adds a NonAggregateDataSource by className. The specified
   * class must implement NonAggregateDataSource.
   * 
   * @param className
   */
  public void addNonAggregateDataSource(String id, String className) {
    customNADataSources.put(id, new CustomDataSourceDefinition(id, className));
  }
  
  /**
   * Adds an AggregateDataSource by className. The specified
   * class must implementAggregateDataSource.
   * 
   * @param className
   */
  public void addAggregateDataSource(String id, String className) {
    customAggDataSources.put(id, new CustomDataSourceDefinition(id, className));
  }
  
  public Iterable<CustomDataSourceDefinition> customAggDataSources() {
    return customAggDataSources.values();
  }
  
  public Iterable<CustomDataSourceDefinition> customNonAggDataSources() {
    return customNADataSources.values();
  }

  /**
   * @param scheduleParams the scheduleParams to set
   */
  public void setScheduleParameters(ScheduleParameters scheduleParams) {
    this.scheduleParams = scheduleParams;
  }


  /**
   * @return the inclTick
   */
  public boolean includeTick() {
    return inclTick;
  }

  /**
   * @param inclTick the inclTick to set
   */
  public void setIncludeTick(boolean inclTick) {
    this.inclTick = inclTick;
  }


  /**
   * @return the inclBatchRun
   */
  public boolean includeBatchRun() {
    return inclBatchRun;
  }

  /**
   * @param inclBatchRun the inclBatchRun to set
   */
  public void setIncludeBatchRun(boolean inclBatchRun) {
    this.inclBatchRun = inclBatchRun;
  }

  /**
   * @return the inclRandomSeed
   */
  public boolean includeRandomSeed() {
    return inclRandomSeed;
  }



  /**
   * @param inclRandomSeed the inclRandomSeed to set
   */
  public void setIncludeRandomSeed(boolean inclRandomSeed) {
    this.inclRandomSeed = inclRandomSeed;
  }

  /**
   * @return the targetType
   */
  public String getSourceType() {
    return sourceType;
  }
  
  // ONLY FOR NON AGGREGATE COLLECTION
  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * @return the type
   */
  public DataSetType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(DataSetType type) {
    this.type = type;
  }
  
  public boolean isScheduleAtEnd() {
    return atEnd;
  }
  
  public void setScheduleAtEnd(boolean val) {
    atEnd = val;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }
  
  private void checkId(String id) {
    if (methodDataSources.containsKey(id) || countSources.containsKey(id)) 
      throw new IllegalArgumentException("Duplicate data source ids ('" + id + "') are not not allowed.");
  }

  public void addMethodDataSource(String id, String className, String methodName) {
    checkId(id);
    methodDataSources.put(id, new MethodDataSourceDefinition(id, className, methodName));
  }
  
  public void addAggregateMethodDataSource(String id, String className, String methodName, AggregateOp aggType) {
    if (type != DataSetType.AGGREGATE) throw new IllegalArgumentException("Cannot add an aggregate data source in non-aggregate descriptor");
    checkId(id);
    MethodDataSourceDefinition mds = new MethodDataSourceDefinition(id, className, methodName);
    mds.setAggregateOp(aggType);
    methodDataSources.put(id, mds);
  }
  
  public void addCountDataSource(String id, String className) {
    if (type != DataSetType.AGGREGATE) throw new IllegalArgumentException("Cannot add an aggregate count data source in non-aggregate descriptor");
    checkId(id);
    countSources.put(id, new CountSourceDefinition(id, className));
  }
  
  public Iterable<CountSourceDefinition> countDataSources() {
    return countSources.values();
  }
  
  public void removeMethodDataSource(String id) {
    methodDataSources.remove(id);
  }
  
  public Iterable<MethodDataSourceDefinition> methodDataSources() {
    return methodDataSources.values();
  }
  
  public void clearCountDataSources() {
    countSources.clear();
  }

  public void clearMethodDataSources() {
    methodDataSources.clear();
  }

  public void clearCustomDataSources() {
    customNADataSources.clear();
    customAggDataSources.clear();
  }
}
