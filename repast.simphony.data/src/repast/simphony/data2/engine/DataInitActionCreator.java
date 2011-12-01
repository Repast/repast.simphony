package repast.simphony.data2.engine;

import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * Creates an action that will initial
 * 
 * @author Nick Collier
 */
public class DataInitActionCreator implements CompositeControllerActionCreator {

  public String getID() {
    return ControllerActionConstants.DATA_INIT_ROOT;
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
        DataSetRegistry registry = (DataSetRegistry) runState
            .getFromRegistry(DataConstants.REGISTRY_KEY);
        manager = registry.getDataSetManager(contextId);
        manager.batchStarted();
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
        // manager will be null if this a new run in non-batch mode, so get
        // the new manager, otherwise continue to use the existing one.
        if (manager == null) {
          DataSetRegistry registry = (DataSetRegistry) runState
              .getFromRegistry(DataConstants.REGISTRY_KEY);
          manager = registry.getDataSetManager(contextId);
        }
        manager.runStarted(runState, contextId, runParams);
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
        manager.batchEnded();
        manager = null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see
       * repast.simphony.engine.environment.DefaultControllerAction#runCleanup
       * (repast.simphony.engine.environment.RunState, java.lang.Object)
       */
      @Override
      public void runCleanup(RunState runState, Object contextId) {
        manager.runEnded(runState, contextId);
      }
    };
  }
}
