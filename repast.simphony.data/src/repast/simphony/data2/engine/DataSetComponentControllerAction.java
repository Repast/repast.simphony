/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.AggregateDataSource;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.NonAggregateDataSource;
import repast.simphony.data2.builder.AggregateDataSetBuilder;
import repast.simphony.data2.builder.NonAggregateDataSetBuilder;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import simphony.util.messages.MessageCenter;

/**
 * ControllerAction responsible for creating DataSets from DataSetDescriptors.
 * 
 * @author Nick Collier
 */
public class DataSetComponentControllerAction extends DefaultControllerAction implements
    DescriptorControllerAction<DataSetDescriptor> {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(DataSetComponentControllerAction.class);

  private DataSetDescriptor descriptor;

  public DataSetComponentControllerAction(DataSetDescriptor descriptor) {
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
  public DataSetDescriptor getDescriptor() {
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
    DataSetType type = descriptor.getType();
    try {

      if (type == DataSetType.NON_AGGREGATE) {
        NonAggregateDataSetBuilder builder = createNABuilder();
        manager.addDataSetBuilder(builder);
        if (descriptor.includeTick())
          builder.addDataSource(manager.getTickCountDataSource());
        if (runState.getRunInfo().isBatch() || descriptor.includeBatchRun())
          builder.addDataSource(manager.getBatchRunDataSource());
        if (descriptor.includeRandomSeed())
          builder.addDataSource(manager.getRandomSeedDataSource());
        builder.defineScheduleParameters(descriptor.getScheduleParameters());
      } else {
        AggregateDataSetBuilder builder = createAggregateBuilder();
        manager.addDataSetBuilder(builder);
        if (descriptor.includeTick())
          builder.addDataSource(manager.getTickCountDataSource());
        if (runState.getRunInfo().isBatch() || descriptor.includeBatchRun())
          builder.addDataSource(manager.getBatchRunDataSource());
        if (descriptor.includeRandomSeed())
          builder.addDataSource(manager.getRandomSeedDataSource());
        builder.defineScheduleParameters(descriptor.getScheduleParameters());
      }

    } catch (ClassNotFoundException ex) {
      msgCenter.warn("Error while creating DataSets", ex);
    }
  }

  private AggregateDataSetBuilder createAggregateBuilder() throws ClassNotFoundException {
    AggregateDataSetBuilder builder = new AggregateDataSetBuilder(descriptor.getName());
    for (MethodDataSourceDefinition def : descriptor.methodDataSources()) {
      Class<?> objType = Class.forName(def.getObjTargetClass(), false, this.getClass()
          .getClassLoader());
      builder.defineMethodDataSource(def.getId(), def.getAggregateOp(), objType,
          def.getMethodName());
    }

    for (CountSourceDefinition def : descriptor.countDataSources()) {
      Class<?> objType = Class.forName(def.getTypeName(), false, this.getClass().getClassLoader());
      builder.defineCountDataSource(def.getId(), objType);
    }

    for (CustomDataSourceDefinition def : descriptor.customAggDataSources()) {
      String className = def.getDataSourceClassName();
      try {
        Class<?> clazz = Class.forName(className, false, this.getClass().getClassLoader());

        if (!AggregateDataSource.class.isAssignableFrom(clazz)) {
          msgCenter.warn("Error while creating custom aggregate data source. '" + className
              + "' must implement AggregateDataSource.");
        }

        AggregateDataSource source = (AggregateDataSource) clazz.newInstance();
        builder.addDataSource(new AggregateDataSourceWrapper(def.getId(), source));

      } catch (Exception ex) {
        msgCenter.warn("Error while creating custom aggregate data source '" + className + "'", ex);
        break;
      }
    }

    return builder;
  }

  private NonAggregateDataSetBuilder createNABuilder() throws ClassNotFoundException {
    Class<?> clazz = Class.forName(descriptor.getSourceType(), false, this.getClass()
        .getClassLoader());
    NonAggregateDataSetBuilder builder = new NonAggregateDataSetBuilder(descriptor.getName(), clazz);
    for (MethodDataSourceDefinition def : descriptor.methodDataSources()) {
      Class<?> objType = Class.forName(def.getObjTargetClass(), false, this.getClass()
          .getClassLoader());
      builder.defineMethodDataSource(def.getId(), objType, def.getMethodName());
    }

    for (CustomDataSourceDefinition def : descriptor.customNonAggDataSources()) {
      String className = def.getDataSourceClassName();
      try {
        Class<?> dsClass = Class.forName(className, false, this.getClass().getClassLoader());

        if (!NonAggregateDataSource.class.isAssignableFrom(dsClass)) {
          msgCenter.warn("Error while creating custom non-aggregate data source. '" + className
              + "' must implement NonAggregateDataSource.");
        }

        NonAggregateDataSource source = (NonAggregateDataSource) dsClass.newInstance();
        builder.addDataSource(new NonAggregateDataSourceWrapper(def.getId(), source));

      } catch (Exception ex) {
        msgCenter.warn("Error while creating custom non aggregate data source '" + className + "'",
            ex);
        break;
      }
    }

    return builder;
  }
}
