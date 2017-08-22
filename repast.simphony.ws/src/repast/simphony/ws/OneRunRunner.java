/*CopyrightHere*/
package repast.simphony.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

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
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioLoadException;
import repast.simphony.scenario.ScenarioLoader;

/**
 * Runs a single run of a simulation.
 * 
 * @author Nick Collier
 */
public class OneRunRunner implements RunListener {

  private class OneRunRunnerClient implements Runnable {

    public void run() {
      
      Poller poller = ctx.createPoller(1);
      poller.register(client, Poller.POLLIN);
    
      ZMsg msg = new ZMsg();
      msg.add(WSConstants.READY_JSON.getBytes());
      try {
        msg.send(client);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      System.out.println("Sent READY");

      while (true) {
        poller.poll(100);
        if (poller.pollin(0)) {
          System.out.println("RECV MESSAGE");
          ZMsg zmsg = ZMsg.recvMsg(client);
          // pop the address
          zmsg.pop();
          String content = zmsg.popString();
          System.out.println(content);
          zmsg.destroy();
          if (content.equals(WSConstants.START)) {
            start();
          } else if (content.equals(WSConstants.STOP)) {
            stop();
            break;
          } else if (content.equals(WSConstants.PAUSE)) {
            pause();
          }
        }
      }
      
      ctx.destroy();
    }
  }

  private RunEnvironmentBuilder runEnvironmentBuilder;
  protected ORBController controller;
  protected boolean pause = false;
  protected Object monitor = new Object();
  private ZContext ctx;
  private Socket client;

  private boolean initRequired = true;
  private boolean startSim = true;
  private Parameters params;

  public OneRunRunner(String root, String scenario, String identity, String host)
      throws ScenarioLoadException {
    try {
      initMessageCenter(root);
      initSocket(identity, host);
      loadParameters(scenario);
    } catch (IOException | ParserConfigurationException | SAXException | ScenarioLoadException ex) {
      ex.printStackTrace();
    }

    ScheduleRunner scheduleRunner = new ScheduleRunner();
    scheduleRunner.addRunListener(this);
    runEnvironmentBuilder = new DefaultRunEnvironmentBuilder(scheduleRunner, false);
    controller = new ORBController(runEnvironmentBuilder, client);
    controller.setScheduleRunner(scheduleRunner);
    init(new File(scenario));

    OneRunRunnerClient runnerClient = new OneRunRunnerClient();
    new Thread(runnerClient).start();
  }

  private void initSocket(String identity, String host) {
    ctx = new ZContext();
    client = ctx.createSocket(ZMQ.DEALER);
    client.setIdentity(identity.getBytes(ZMQ.CHARSET));
    client.connect("tcp://" + host);
  }

  private void initMessageCenter(String root) throws ScenarioLoadException {
    Properties props = new Properties();
    File in = new File(root, "MessageCenter.log4j.properties");
    try {
      props.load(new FileInputStream(in));
    } catch (IOException e) {
      throw new ScenarioLoadException(e);
    }
    PropertyConfigurator.configure(props);
  }

  private void loadParameters(String scenario)
      throws ParserConfigurationException, SAXException, IOException {
    String parameters = scenario + "/parameters.xml";
    ParametersParser parser = new ParametersParser(new File(parameters));
    params = parser.getParameters();
  }

  private void init(File scenarioDir) throws ScenarioLoadException {
    if (scenarioDir.exists()) {
      ScenarioLoader loader = new BatchScenarioLoader(scenarioDir);
      ControllerRegistry registry = loader.load(runEnvironmentBuilder);
      controller.setControllerRegistry(registry);
    } else {
      // msgCenter.error("Scenario not found", new
      // IllegalArgumentException("Invalid scenario "
      // + scenarioDir.getAbsolutePath()));
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
    // try {
    controller.runCleanup();
    controller.batchCleanup();
    // put EXIT in queue where Run will
    // take it and exit
    // cmdQueue.put(SimCommand.EXIT);
    // } catch (InterruptedException ex) {
    // TODO HANDLE
    // ex.printStackTrace();
    // }
  }

  private static class ORBController extends DefaultController {

    private int runNumber;
    private Socket socket;

    /**
     * @param runEnvironmentBuilder
     */
    public ORBController(RunEnvironmentBuilder runEnvironmentBuilder, Socket socket) {
      super(runEnvironmentBuilder);
      this.socket = socket;
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
      runState.addToRegistry("msg.socket", socket);
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

  public static void main(String[] args) {
    String root = args[0];
    String scenario = args[1];
    String identity = args[2];
    String host = args[3];
    try {
      OneRunRunner runner = new OneRunRunner(root, scenario, identity, host);
      //runner.start();
    } catch (ScenarioLoadException e) {
      // TODO post errors on zeromq
      e.printStackTrace();
    }
  }
}
