/*CopyrightHere*/
package repast.simphony.ws;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import javax.websocket.Session;

import repast.simphony.batch.BatchScenarioLoader;
import repast.simphony.engine.controller.DefaultController;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultRunEnvironmentBuilder;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.scenario.ScenarioLoadException;
import repast.simphony.scenario.ScenarioLoader;

/**
 * Runs a single run of a simulation.
 * 
 * @author Nick Collier
 */
public class WSRunner implements RunListener, Runnable {

  //TODO private static MessageCenter msgCenter = MessageCenter.getMessageCenter(WSRunner.class);


  private RunEnvironmentBuilder runEnvironmentBuilder;
  protected ORBController controller;
  protected boolean pause = false;
  protected Object monitor = new Object();
  
  private boolean initRequired = true;
  private boolean startSim = true;
  private BlockingQueue<SimCommand> cmdQueue;
  private BlockingQueue<String> statusQueue;
  private Parameters params;

  public WSRunner(File scenarioDir, BlockingQueue<SimCommand> cmdQueue, BlockingQueue<String> statusQueue, Parameters params, Session session) throws ScenarioLoadException {
    this.cmdQueue = cmdQueue;
    this.statusQueue = statusQueue;
    this.params = params;
    ScheduleRunner scheduleRunner = new ScheduleRunner();
    scheduleRunner.addRunListener(this);
    runEnvironmentBuilder = new DefaultRunEnvironmentBuilder(scheduleRunner, false);
    controller = new ORBController(runEnvironmentBuilder, session);
    controller.setScheduleRunner(scheduleRunner);
    init(scenarioDir);
  }

  private void init(File scenarioDir) throws ScenarioLoadException {
    if (scenarioDir.exists()) {
      ScenarioLoader loader = new BatchScenarioLoader(scenarioDir);
      ControllerRegistry registry = loader.load(runEnvironmentBuilder);
      controller.setControllerRegistry(registry);
    } else {
     // msgCenter.error("Scenario not found", new IllegalArgumentException("Invalid scenario "
     //     + scenarioDir.getAbsolutePath()));
      System.err.println("Scenario not found");
    }
  }

  public void initSim() {
      if (initRequired) {
        controller.batchInitialize();
        params = checkForSeed();
        controller.runParameterSetters(params);
        controller.setRunNumber(1);
        controller.runInitialize(params);
        initRequired = false;
      }
  }
  
  public void start() {
    if (initRequired) {
      initSim();
    }
    
    if (startSim) {
      controller.execute();
      startSim = false;
    } else {
      controller.getScheduleRunner().setPause(false);
    }
  }
  
  public void run() {
    while(true) {
      try {
        System.out.println("waiting on command");
        SimCommand cmd = cmdQueue.take();
        System.out.println(cmd);
        if (cmd == SimCommand.START) {
          start();
        } else if (cmd == SimCommand.PAUSE) {
          pause();
        } else if (cmd == SimCommand.STOP) {
          stop();
        } else if (cmd == SimCommand.EXIT) {
          break;
        }
      } catch (InterruptedException e) {
        // TODO handle
        e.printStackTrace();
      }
    }
    
    try {
      statusQueue.put("DONE");
    } catch (InterruptedException e) {
      // TODO handle
      e.printStackTrace();
    }
  }
  
  public void pause() {
    controller.getScheduleRunner().setPause(true);
  }
  
 
  public void stop() {
    controller.getScheduleRunner().stop();
  }
  
  public void reset() {
    initRequired = true;
    startSim = true;
    controller.runCleanup();
    controller.batchCleanup();
  }

  private Parameters checkForSeed() {

    if (!params.getSchema().contains(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
      ParametersCreator creator = new ParametersCreator();
      creator.addParameters(params);
      creator.addParameter(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, Integer.class,
          (int) System.currentTimeMillis(), false);
      params = creator.createParameters();
    }

    return params;
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
    try {
      controller.runCleanup();
      controller.batchCleanup();
      // put EXIT in queue where Run will 
      // take it and exit
      cmdQueue.put(SimCommand.EXIT);
    } catch (InterruptedException ex) {
      // TODO HANDLE
      ex.printStackTrace();
    }
  }

  private static class ORBController extends DefaultController {

    private Session session;
    private int runNumber;

    /**
     * @param runEnvironmentBuilder
     */
    public ORBController(RunEnvironmentBuilder runEnvironmentBuilder, Session session) {
      super(runEnvironmentBuilder);
      this.session = session;
    }

    public void setRunNumber(int runNumber) {
      this.runNumber = runNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.DefaultController#prepare()
     */
    @Override
    protected boolean prepare() {
      boolean retVal = super.prepare();
      RunState runState = this.getCurrentRunState();
      runState.addToRegistry("WEB_SOCKET_SESSION", session);
      this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
      return retVal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * repast.simphony.engine.controller.DefaultController#prepareForNextRun()
     */
    @Override
    protected void prepareForNextRun() {
      super.prepareForNextRun();
      this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
    }
  }
}
