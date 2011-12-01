package repast.simphony.data2.engine;

import repast.simphony.data2.BatchRunDataSetManager;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.SingleRunDataSetManager;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * Creates the parent action for the component actions that initialize data
 * collection from individual dataset descriptors.
 * 
 * @author Nick Collier
 */
public class DataSetsActionCreator implements CompositeControllerActionCreator {

  public String getID() {
    return ControllerActionConstants.DATA_SET_ROOT;
  }

  public ControllerAction createControllerAction() {
    return new DefaultControllerAction() {

      private DataSetManager manager;

      /*
       * (non-Javadoc)
       * 
       * @see
       * repast.simphony.engine.environment.DefaultControllerAction#batchInitialize
       * (repast.simphony.engine.environment.RunState, java.lang.Object)
       */
      @Override
      public void batchInitialize(RunState runState, Object contextId) {
        // add a single registry for all contexts
        DataSetRegistry registry = (DataSetRegistry) runState
            .getFromRegistry(DataConstants.REGISTRY_KEY);
        if (registry == null) {
          registry = new DataSetRegistry();
          runState.addToRegistry(DataConstants.REGISTRY_KEY, registry);
        }

        manager = runState.getRunInfo().isBatch() ? new BatchRunDataSetManager()
            : new SingleRunDataSetManager();
        registry.addDataSetManager(contextId, manager);

        if (!runState.getRunInfo().isBatch()) {
          // this is here so that the data is flushed after a stop
          // rather than waiting for the close on a reset
          runState
              .getScheduleRegistry()
              .getModelSchedule()
              .schedule(ScheduleParameters.createAtEnd(ScheduleParameters.LAST_PRIORITY),
                  new FlushAction(manager));
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see
       * repast.simphony.engine.environment.DefaultControllerAction#runInitialize
       * (repast.simphony.engine.environment.RunState, java.lang.Object,
       * repast.simphony.parameter.Parameters)
       */
      @Override
      public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
        DataSetRegistry registry = (DataSetRegistry) runState
            .getFromRegistry(DataConstants.REGISTRY_KEY);

        if (runState.getRunInfo().isBatch()) {
          // registry will be destroyed each run in batch mode.
          if (registry == null) {
            registry = new DataSetRegistry();
            runState.addToRegistry(DataConstants.REGISTRY_KEY, registry);
          }

          // registry will be destroyed each run so we need to add the managers
          // back in
          registry.addDataSetManager(contextId, manager);
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see
       * repast.simphony.engine.environment.DefaultControllerAction#batchCleanup
       * (repast.simphony.engine.environment.RunState, java.lang.Object)
       */
      @Override
      public void batchCleanup(RunState runState, Object contextId) {
        manager = null;
      }
    };
  }
}

class FlushAction implements IAction {

  DataSetManager manager;

  public FlushAction(DataSetManager manager) {
    this.manager = manager;
  }
  
  @Override
  public void execute() {
    manager.flush();
  }
}
