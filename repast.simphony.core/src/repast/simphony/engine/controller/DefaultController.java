/*CopyrightHere*/
package repast.simphony.engine.controller;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunInfo;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.Runner;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.DefaultRandomRegistry;
import repast.simphony.util.collections.Tree;
import repast.simphony.util.collections.TreeVisitor;
import simphony.util.messages.MessageCenter;

/**
 * A default implementation of a Controller. This performs all the functionality
 * specified in the {@link repast.simphony.engine.controller.Controller}
 * interface.
 * 
 * @author Jerry Vos
 */
public class DefaultController implements Controller {
  private static final int FIRST_RUN_NUMBER = 1;

  private static final long serialVersionUID = -202102336096207785L;

  private static final MessageCenter msgCenter = MessageCenter
      .getMessageCenter(DefaultController.class);

  Integer runNumber;

  class DefaultActionComparator implements Comparator<ControllerAction> {

    Object contextID;

    public DefaultActionComparator(Object contextID) {
      this.contextID = contextID;
    }

    public int compare(ControllerAction o1, ControllerAction o2) {
      // make sure the random loader is first, followed by the data loader,
      // followed by data sets

      // make sure the random stream creators are always first
      ControllerAction randomRoot = actionRegistry.findAction(contextID,
          ControllerActionConstants.RANDOM_LOADER_ROOT);
      if (randomRoot != null) {
        if (o1.equals(randomRoot))
          return -1;
        if (o2.equals(randomRoot))
          return 1;
      }

      // make sure the data loader action is alway second
      ControllerAction dataLoaderRoot = actionRegistry.findAction(contextID,
          ControllerActionConstants.DATA_LOADER_ROOT);
      if (dataLoaderRoot != null) {
        if (o1.equals(dataLoaderRoot))
          return -1;
        if (o2.equals(dataLoaderRoot))
          return 1;
      }

      // make sure the data set actions are always third
      ControllerAction dataSetRoot = actionRegistry.findAction(contextID,
          ControllerActionConstants.DATA_SET_ROOT);
      if (dataSetRoot != null) {
        if (o1.equals(dataSetRoot))
          return -1;
        if (o2.equals(dataSetRoot))
          return 1;
      }

      // make sure the data init action is always last
      // this way we know all the sources and sinks have been
      // defined and we can then combine them.
      ControllerAction dataInitRoot = actionRegistry.findAction(contextID,
          ControllerActionConstants.DATA_INIT_ROOT);
      if (dataInitRoot != null) {
        if (o1.equals(dataInitRoot))
          return 1;
        if (o2.equals(dataInitRoot))
          return -1;
      }

      // keep the order as is.
      return -1;
    }
  }

  private RunEnvironmentBuilder runEnvironmentBuilder;
  private ControllerRegistry actionRegistry;

  // TODO: probably at some point always have a run state exist, but at some
  // points (before the first run, after running) have it be set as invalid
  // or as finished
  private RunState currentRunState;

  private boolean runStateSetInBatch = false;

  // these are incremented in batch/runInitialize
  // AFTER prepare has been called.
  private int nextBatchNumber = FIRST_RUN_NUMBER;
  private int nextRunNumber = FIRST_RUN_NUMBER;
  // used to track the batch number for the current
  // set of runs.
  private int currentBatchNumber = FIRST_RUN_NUMBER;

  private Runner scheduleRunner = new DefaultScheduleRunner();

  private RunEnvironment currentRunEnvironment;

  /**
   * TODO: fill this in
   * 
   * @param runEnvironmentBuilder
   */
  public DefaultController(RunEnvironmentBuilder runEnvironmentBuilder) {
    this.runEnvironmentBuilder = runEnvironmentBuilder;
  }

  public DefaultController(RunEnvironmentBuilder runEnvironmentBuilder, int runNumber) {
    this.runEnvironmentBuilder = runEnvironmentBuilder;
    this.runNumber = runNumber;
  }

  /**
   * Sets the registry of {@link ControllerAction}s that the controller will
   * run.
   * 
   * @param controllerRegistry
   *          a registry used to find actions to run
   */
  public void setControllerRegistry(ControllerRegistry controllerRegistry) {
    this.actionRegistry = controllerRegistry;
  }

  /**
   * Retrieves the registry of {@link ControllerAction}s that the controller
   * will run.
   * 
   * @return a registry used to find actions to run
   */
  public ControllerRegistry getControllerRegistry() {
    return actionRegistry;
  }

  // public void setLoggingRegistryFactory(
  // LoggingRegistryFactory loggingRegistryFactory) {
  // this.loggingRegistryFactory = loggingRegistryFactory;
  // }

  /**
   * Sets the ScheduleRunner that will be used to execute the model's schedule
   * found through the RunState. This is used in the execute() phase of the
   * controller.
   * 
   * @param scheduleRunner
   *          the ScheduleRunner that will execute the model's schedule
   */
  public void setScheduleRunner(Runner scheduleRunner) {
    this.scheduleRunner = scheduleRunner;
  }

  public Runner getScheduleRunner() {
    return this.scheduleRunner;
  }

  /**
   * Calls the batchInitialize method on all the actions this registry contains,
   * with runState as an argument.
   * 
   * @param runState
   *          the state to pass to the actions
   */
  private void batchInitialize(final RunState runState, final Object contextId) {
    Tree<ControllerAction> tree = actionRegistry.getActionTree(contextId);
    tree.sortChildren(new DefaultActionComparator(contextId));
    tree.preOrderTraversal(new TreeVisitor<ControllerAction>() {
      public void visit(ControllerAction node) {
        node.batchInitialize(runState, contextId);
      }
    });
    
    for (Object id : actionRegistry.getContextIdTree().getChildren(contextId)) {
      batchInitialize(runState, id);
    }
  }

  /**
   * Calls the runInitialize method on all the actions this registry contains,
   * with runState as an argument.
   * 
   * @param runState
   *          the state to pass to the actions
   */
  private void runInitialize(final RunState runState, final Object contextTypeId,
      final Object contextInstanceId, final Parameters runParams) {
    Tree<ControllerAction> tree = actionRegistry.getActionTree(contextTypeId);
    tree.sortChildren(new DefaultActionComparator(contextTypeId));
    tree.preOrderTraversal(new TreeVisitor<ControllerAction>() {
      public void visit(ControllerAction node) {
        node.runInitialize(runState, contextInstanceId, runParams);
      }
    });
  }

  /**
   * Calls the runInitialize method on all the actions this registry contains,
   * with runState as an argument.
   * 
   * @param runState
   *          the state to pass to the actions
   */
  private void runInitialize(final RunState runState, final Context context,
      final Parameters runParams) {
    Tree<ControllerAction> tree = actionRegistry.getActionTree(context.getTypeID());
    // if the tree == null and the context type id hasn't been explicitly set
    // then user probably didn't pass an id to the context when creating it.
    // if just the tree == null then probably a context added but not part of
    // the
    // score file.
    if (tree == null && context.getTypeID().toString().indexOf(Context.SYN_CONTEXT_PREFIX) != -1) {
      msgCenter
          .warn("Unable to initialize a subcontext: check that subcontext constructors are passed"
              + " the correct name");
      throw new RuntimeException("Unable to initialize an unfound context");

    } else if (tree != null) {
      tree.sortChildren(new DefaultActionComparator(context.getTypeID()));
      tree.preOrderTraversal(new TreeVisitor<ControllerAction>() {
        public void visit(ControllerAction node) {
          node.runInitialize(runState, context.getId(), runParams);
        }
      });
    }

    Iterable<Context> subIter = context.getSubContexts();
    for (Context<Object> sub : subIter) {
      runInitialize(runState, sub, runParams);
    }
  }

  /**
   * Calls the batchCleanup method on all the actions this registry contains,
   * with runState as an argument.
   * 
   * @param runState
   *          the state to pass to the actions
   */
  public void batchCleanup(final RunState runState, final Object contextId) {
    Tree<ControllerAction> tree = actionRegistry.getActionTree(contextId);
    tree.preOrderTraversal(new TreeVisitor<ControllerAction>() {
      public void visit(ControllerAction node) {
        node.batchCleanup(runState, contextId);
      }
    });
    
    for (Object id : actionRegistry.getContextIdTree().getChildren(contextId)) {
      batchCleanup(runState, id);
    }
  }

  /**
   * Calls the runCleanup method on all the actions this registry contains, with
   * runState as an argument.
   * 
   * @param runState
   *          the state to pass to the actions
   */
  public void runCleanup(final RunState runState, final Object contextId) {
    Tree<ControllerAction> tree = actionRegistry.getActionTree(contextId);
    tree.preOrderTraversal(new TreeVisitor<ControllerAction>() {
      public void visit(ControllerAction node) {
        node.runCleanup(runState, contextId);
      }
    });

    for (Object id : actionRegistry.getContextIdTree().getChildren(contextId)) {
      runCleanup(runState, id);
    }

    runState.setGUIRegistry(null);
  }

  /**
   * Executes the specified RunState with the ScheduleRunner.
   * 
   * @param toExecuteOn
   *          the RunState to execute
   * @see Runner
   */
  public void execute(RunState toExecuteOn) {
    scheduleRunner.execute(toExecuteOn);
  }

  /**
   * Executes the current RunState of the controller. The current RunState is
   * created when the {@link #batchInitialize()} or
   * {@link #runInitialize(repast.simphony.parameter.Parameters)} methods are
   * called.
   */
  public void execute() {
    if (currentRunState != null) {
      execute(currentRunState);
    } else {
      RuntimeException ex = new RuntimeException("Attempting to "
          + "execute the controller without having setup the current "
          + "RunState. One of the initialize methods must be called "
          + "before calling this method.");
      msgCenter.error("DefaultController.execute: Error, attempting to "
          + "execute the controller without having setup the current "
          + "RunState. One of the initialize methods must be called "
          + "before calling this method.", ex);
    }

  }

  /**
   * Calls the runInitialize method of this class with all the contexts in the
   * controller registry in the order they are returned by that registry.
   * <p/>
   * <p/>
   * This will prepare the controller, creating a new run state object for the
   * next set of runs (batch or not) if the RunState has been invalidated.
   * Calling the {@link #batchCleanup()} or {@link #cleanup()} method is
   * required to invalidate the RunState in prepartion for another set of runs.
   */
  public void batchInitialize() {
    currentBatchNumber = nextBatchNumber;
    nextRunNumber = FIRST_RUN_NUMBER;
    prepare();
    // if(runNumber!=null) {
    // currentRunState.getRunInfo().setRunNumber(runNumber);
    // }
    nextBatchNumber++;
    runStateSetInBatch = true;
    batchInitialize(currentRunState, actionRegistry.getMasterContextId());
  }

  /**
   * Calls the runInitialize method of this class with all the contexts in the
   * controller registry in the order they are returned by that registry.
   * <p/>
   * <p/>
   * This will prepare the controller, creating a new run state object for the
   * next set of runs (batch or not) if the RunState has been invalidated.
   * Calling the {@link #runCleanup()} or {@link #cleanup()} method is required
   * to invalidate the RunState in prepartion for another set of runs.
   * 
   * @return the current initialized RunState.
   */
  public RunState runInitialize(Parameters params) {
    if (nextRunNumber == FIRST_RUN_NUMBER && !prepare()) {
      runStateSetInBatch = false;
    }

    if (nextRunNumber != FIRST_RUN_NUMBER) {
      // must setup for the next in a series of runs
      prepareForNextRun();
    }

    // must come after prepare
    nextRunNumber++;

    // initializes the master context
    Object masterContextId = actionRegistry.getMasterContextId();
    loadRandomLoader();

    // TODO how not to assume the master context id and the master context type
    // id are the same
    runInitialize(currentRunState, masterContextId, masterContextId, params);
    Iterable<Context> subIter = currentRunState.getMasterContext().getSubContexts();
    for (Context<Object> sub : subIter) {
      runInitialize(currentRunState, sub, params);
    }
    scheduleRunner.init();
    return currentRunState;
  }

  // makes sure that we have an action that will setup the default random
  // stream's seed
  protected void loadRandomLoader() {
    ControllerAction randomRoot = actionRegistry.findAction(actionRegistry.getMasterContextId(),
        ControllerActionConstants.RANDOM_LOADER_ROOT);
    if (randomRoot == null) {
      randomRoot = new DefaultControllerAction();
      actionRegistry.addAction(actionRegistry.getMasterContextId(), null, randomRoot);
    }

    Tree<ControllerAction> tree = actionRegistry.getActionTree(actionRegistry.getMasterContextId());

    Collection<ControllerAction> children = tree.getChildren(randomRoot);

    if (children.size() == 0) {
      tree.addNode(randomRoot, new DefaultRandomStreamAction());
    }
  }

  public Parameters runParameterSetters(Parameters params) {
    if (params == null) {
      params = new DefaultParameters();
    }

    for (Iterator<ParameterSetter> iterator = actionRegistry.getParameterSetters().iterator(); iterator
        .hasNext();) {
      ParameterSetter setter = iterator.next();
      setter.next(params);
    }

    // update the parameters
    this.runEnvironmentBuilder.setParameters(params);
    // if we're on the first run and in batch mode, we already have a run
    // environment setup, so we must update its params
    if (nextRunNumber == FIRST_RUN_NUMBER && currentRunEnvironment != null) {
      currentRunEnvironment.setParameters(params);
    }

    return params;
  }

  /**
   * Calls the runCleanup method of this class with all the contexts in the
   * controller registry in the order they are returned by that registry.
   * <p/>
   * <p/>
   * If the RunState object of this controller was created by the runInitialize
   * method, this method will then invalidate the RunState object.
   */
  public void runCleanup() {
    runCleanup(currentRunState, actionRegistry.getMasterContextId());
    if (!runStateSetInBatch) {
      cleanup();
    }
  }

  /**
   * Calls the batchCleanup method of this class with all the contexts in the
   * controller registry in the order they are returned by that registry.
   * <p/>
   * <p/>
   * This method will invalidate the RunState object.
   */
  public void batchCleanup() {
    batchCleanup(currentRunState, actionRegistry.getMasterContextId());
    cleanup();
  }

  /**
   * If the RunState object is invalidated (null) this will create a new
   * RunState object, using the next*Number fields and the model name of this
   * class. If the RunState is still valid (non-null) this will just return
   * false.
   * 
   * @return true if a new RunState object was created.
   */
  protected boolean prepare() {
    if (currentRunState == null) {
      currentRunEnvironment = runEnvironmentBuilder.createRunEnvironment();
      // setup the run state
      currentRunState = RunState.init();

      currentRunState.setRunInfo(new RunInfo(actionRegistry.getName(), currentBatchNumber,
          nextRunNumber, currentRunEnvironment.isBatch()));

      currentRunState.getScheduleRegistry().setModelSchedule(
          currentRunEnvironment.getCurrentSchedule());
      currentRunState.getScheduleRegistry().setScheduleRunner(scheduleRunner);

      currentRunState.setControllerRegistry(actionRegistry);

      currentRunState.setRandomRegistry(new DefaultRandomRegistry());

      return true;
    }

    return false;
  }

  protected void prepareForNextRun() {
    if (currentRunState == null) {
      currentRunEnvironment = runEnvironmentBuilder.createRunEnvironment();
      // setup the run state
      // TODO: decide if this is wanted (do we want to clear out all the
      // settings each run?)
      currentRunState = RunState.init();

      currentRunState.setRunInfo(new RunInfo(actionRegistry.getName(), currentBatchNumber,
          nextRunNumber, currentRunEnvironment.isBatch()));

      currentRunState.getScheduleRegistry().setModelSchedule(
          currentRunEnvironment.getCurrentSchedule());
      currentRunState.getScheduleRegistry().setScheduleRunner(scheduleRunner);

      currentRunState.setControllerRegistry(actionRegistry);

      currentRunState.setRandomRegistry(new DefaultRandomRegistry());
    }
  }

  /**
   * Invalidates the current {@link RunState}.
   */
  public void cleanup() {
    currentRunState = null;
  }

  /**
   * Gets the next run number. The next run number is the number assigned to the
   * next run when runInitialize is called.
   * 
   * @return the next run number. The next run number is the number assigned to
   *         the next run when runInitialize is called.
   */
  public int getNextRunNumber() {
    return nextRunNumber;
  }

  /**
   * Gets the next batch number. The next batch number is the number assigned to
   * the next batch when batchInitialize is called.
   * 
   * @return the next batch number. The next batch number is the number assigned
   *         to the next batch when batchInitialize is called.
   */
  public int getNextBatchNumber() {
    return nextBatchNumber;
  }

  /**
   * Retrieves the current RunState the controller is using.
   * 
   * @return the RunState the controller is currently using
   */
  public RunState getCurrentRunState() {
    return currentRunState;
  }

  public void setup() {
    // TODO Auto-generated method stub

  }
}
