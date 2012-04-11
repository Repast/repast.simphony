/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.parameter.ParameterLineParser;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ScenarioLoadException;

/**
 * @author Nick Collier
 */
public class InstanceRunner {

  private ParameterLineParser lineParser;
  private OneRunBatchRunner runner;
  
  public InstanceRunner() throws IOException {
    Properties props = new Properties();
    props.load(new FileInputStream("../MessageCenter.log4j.properties"));
    PropertyConfigurator.configure(props);
  }

  public void configure(String paramFile, String scenarioDir) throws IOException, ScenarioLoadException {
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
        Parameters params = lineParser.parse(line);
        int runNum = (Integer) params.getValue(BatchConstants.BATCH_RUN_PARAM_NAME);
        runner.run(runNum, params);
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
  public static void main(String[] args) {
    try {
      InstanceRunner runner = new InstanceRunner();
      runner.configure(args[0], args[1]);
      runner.run(args[2]);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
