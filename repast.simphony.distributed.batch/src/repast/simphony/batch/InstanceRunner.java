/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.parameter.ParameterLineParser;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ScenarioLoadException;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

/**
 * @author Nick Collier
 */
public class InstanceRunner {

  private static MessageCenter msg = MessageCenter.getMessageCenter(InstanceRunner.class);

  private ParameterLineParser lineParser;
  private OneRunBatchRunner runner;
  private String id;
  private RunningStatus status = RunningStatus.OK;

  public InstanceRunner(String id) throws IOException {
    this.id = id;
    Properties props = new Properties();
    File in = new File("../MessageCenter.log4j.properties");
    props.load(new FileInputStream(in));
    PropertyConfigurator.configure(props);

    MessageCenter.addMessageListener(new MessageEventListener() {
      public void messageReceived(MessageEvent evt) {
        Level level = evt.getLevel();
        if (level == Level.ERROR || level == Level.WARN || level == Level.FATAL) {
          if (level == Level.WARN && status == RunningStatus.OK)
            status = RunningStatus.WARN;
          else if (level != Level.WARN)
            status = RunningStatus.FAILURE;
          writeMessage(evt);

        }
      }
    });
  }

  private void writeMessage(MessageEvent evt) {
    String fname = status.toString();
    File file = new File(fname + "_" + id);
    boolean append = file.exists();
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new FileWriter(file, append));
      writer.append(evt.getMessage().toString());
      writer.append("\n");
      if (evt.getThrowable() != null) {
        evt.getThrowable().printStackTrace(writer);
      }
    } catch (IOException ex) {
      if (evt.getThrowable() != null) {
        evt.getThrowable().printStackTrace();
      }
      ex.printStackTrace();
    } finally {
      if (writer != null)
        writer.close();
    }

  }

  public void configure(String paramFile, String scenarioDir) throws IOException,
      ScenarioLoadException {
    File scenario = new File(scenarioDir);
    File params = new File(paramFile);
    lineParser = new ParameterLineParser(params.toURI());
    runner = new OneRunBatchRunner(scenario);
  }

  public void run(String lines) throws ScenarioLoadException {
    runner.batchInit();

    BufferedReader reader = new BufferedReader(new StringReader(lines));
    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        //msg.info("Params: " + line);
        Parameters params = lineParser.parse(line);
        int runNum = (Integer) params.getValue(BatchConstants.BATCH_RUN_PARAM_NAME);
        runner.run(runNum, params);
        if (status == RunningStatus.FAILURE)
          break;
      }
    } catch (IOException ex) {
      // this is reading string so its not going to IOExcept
      ex.printStackTrace();
    }

    runner.batchCleanup();
  }

  // arg[0] is the xml parameter file
  // arg[1] is the scenario directory
  // arg[2] is the input line(s)
  // arg[3] is the id of the instance
  public static void main(String[] args) {
    try {
      InstanceRunner runner = new InstanceRunner(args[3]);
      runner.configure(args[0], args[1]);
      runner.run(args[2]);
    } catch (Throwable ex) {
      msg.error("Error while running model", ex);
    }
  }
}
