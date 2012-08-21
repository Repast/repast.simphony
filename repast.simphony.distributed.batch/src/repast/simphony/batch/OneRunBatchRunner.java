/*CopyrightHere*/
package repast.simphony.batch;

import java.io.File;

import repast.simphony.engine.controller.DefaultController;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultRunEnvironmentBuilder;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.SweeperProducer;
import repast.simphony.scenario.ScenarioLoadException;
import simphony.util.messages.MessageCenter;

/**
 * Runs a simulation in batch mode.
 * 
 * @author Nick Collier
 */
public class OneRunBatchRunner implements RunListener {

  private static MessageCenter msgCenter = MessageCenter.getMessageCenter(OneRunBatchRunner.class);

  private RunEnvironmentBuilder runEnvironmentBuilder;
  protected ORBController controller;
  protected boolean pause = false;
  protected Object monitor = new Object();
  protected SweeperProducer producer;

  public OneRunBatchRunner(File scenarioDir) throws ScenarioLoadException {
    AbstractRunner scheduleRunner = new BatchScheduleRunner();
    scheduleRunner.addRunListener(this);
    runEnvironmentBuilder = new DefaultRunEnvironmentBuilder(scheduleRunner, true);
    controller = new ORBController(runEnvironmentBuilder);
    controller.setScheduleRunner(scheduleRunner);
    init(scenarioDir);
  }

  private void init(File scenarioDir) throws ScenarioLoadException {
    if (scenarioDir.exists()) {
      BatchScenarioLoader loader = new BatchScenarioLoader(scenarioDir);
      ControllerRegistry registry = loader.load(runEnvironmentBuilder);
      controller.setControllerRegistry(registry);
    } else {
      msgCenter.error("Scenario not found", new IllegalArgumentException("Invalid scenario " + scenarioDir.getAbsolutePath()));
    }
  }
  
  public void batchInit() {
    controller.batchInitialize();
  }
  
  public void batchCleanup() {
    controller.batchCleanup();
  }
  
  public void run(int runNum, Parameters params) {
    pause = true;
    params = setupSweep(params);
    controller.runParameterSetters(params);
    controller.setRunNumber(runNum);
    controller.runInitialize(params);
    controller.execute();
    waitForRun();
    controller.runCleanup();
  }

  protected boolean keepRunning() {
    for (ParameterSetter setter : controller.getControllerRegistry().getParameterSetters()) {
      if (!setter.atEnd()) {
        return true;
      }
    }
    return false;
  }

  private Parameters setupSweep(Parameters params) {

    if (!params.getSchema().contains(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
      ParametersCreator creator = new ParametersCreator();
      creator.addParameters(params);
      creator.addParameter(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, Integer.class,
          (int) System.currentTimeMillis(), false);
      params = creator.createParameters();
    }

    return params;
  }

  protected void waitForRun() {
    msgCenter.info("Waiting");
    synchronized (monitor) {
      while (pause) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }
      }
    }
    msgCenter.info("Done Waiting");
  }

  protected void notifyMonitor() {
    synchronized (monitor) {
      monitor.notify();
    }
  }

  /**
   * Invoked when the current run has been paused.
   */
  public void paused() {
  }

  /**
   * Invoked when the current run has been restarted after a pause.
   */
  public void restarted() {
  }

  /**
   * Invoked when the current run has been started.
   */
  public void started() {
  }

  /**
   * Invoked when the current run has been stopped. This will stop this thread
   * from waiting for the current run to finish.
   */
  public void stopped() {
    pause = false;
    notifyMonitor();
    msgCenter.info("Stopped Called");
  }
  
  private static class ORBController extends DefaultController {
    
    private int runNumber;

    /**
     * @param runEnvironmentBuilder
     */
    public ORBController(RunEnvironmentBuilder runEnvironmentBuilder) {
      super(runEnvironmentBuilder);
    }
    
    public void setRunNumber(int runNumber) {
      this.runNumber = runNumber;
    }
    
    /* (non-Javadoc)
     * @see repast.simphony.engine.controller.DefaultController#prepare()
     */
    @Override
    protected boolean prepare() {
      boolean retVal = super.prepare();
      this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
      return retVal;
    }

    /* (non-Javadoc)
     * @see repast.simphony.engine.controller.DefaultController#prepareForNextRun()
     */
    @Override
    protected void prepareForNextRun() {
      super.prepareForNextRun();
      this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
    }
  }
}
