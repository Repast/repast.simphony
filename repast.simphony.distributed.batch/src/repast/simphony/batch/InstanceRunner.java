/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.parameter.ParameterLineParser;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ScenarioLoadException;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

/**
 * Runs a single instance of a simphony model in a batch run. This expects to be passed
 * the following arguments: 
 * <ol>
 * <li> -pxml <parameter xml file>
 * <li> -scenario <scenario directory>
 * <li> -id <instance id>
 * <li> optional -pinput <input file> if the parameter input is in a file.
 * </ul>
 * if no -pinput then last arg is expected to be a string in unrolled parameter format.
 * A parameter line hasthe format R\tP1\tV1,P2\tV2,P3\tV3,... 
 * R is the run number followed by a tab. P* and V* is a parameter name and value 
 * pair which are separated from each other by a tab and from other PV
 * pairs by a comma delimeter.<p>
 * 
 * The InstanceRunner will feed each line from the file / command line to the model and
 * run the model for that
 * parameter combination. When all the lines have been processed the batch run is finished.
 * If there are warnings or errors produced during the run then those will be written to 
 * a WARN or FAILURE file in the working directory. If there is an error, no more
 * lines will be read and the InstanceRunner will stop.
 * 
 * @author Nick Collier
 */
public class InstanceRunner {
  
  private static MessageCenter msg = MessageCenter.getMessageCenter(InstanceRunner.class);
  
  private static final String PXML = "pxml";
  private static final String ID = "id";
  private static final String SCENARIO = "scenario";
  private static final String PINPUT = "pinput";
  
  
  private String input;
  private boolean isFileInput = false;

  private ParameterLineParser lineParser;
  private OneRunBatchRunner runner;
  private String id;
  private RunningStatus status = RunningStatus.OK;
  
  private Options options;

  public InstanceRunner() throws IOException {
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
    
    initOptions();
  }
  
  @SuppressWarnings("static-access")
  private void initOptions() {
    options = new Options();
    Option help = new Option("help", "print this message");
    options.addOption(help);
    Option pxml = OptionBuilder.withArgName("file").
        hasArg().
        withDescription("use given parameter xml file").
        create(PXML);
    options.addOption(pxml);
    
    Option scenario = OptionBuilder.withArgName("directory").
        hasArg().
        withDescription("use given scenario").
        create(SCENARIO);
    options.addOption(scenario);
    
    Option id = OptionBuilder.withArgName("value").
        hasArg().
        withDescription("use given value as instance id").
        create(ID);
    options.addOption(id);
    
    Option pinput = OptionBuilder.withArgName("file").
        hasArg().
        withDescription("use given file as run parameter input").
        create(PINPUT);
    options.addOption(pinput);
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

  public void configure(String[] args) throws IOException, ScenarioLoadException {
    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse(options, args);
     
      if (line.hasOption(PXML)) {
        String paramFile = line.getOptionValue(PXML);
        File params = new File(paramFile);
        lineParser = new ParameterLineParser(params.toURI());
      } else {
        throw new ScenarioLoadException("Command line is missing required -pxml option");
      }
      
      if (line.hasOption(SCENARIO)) {
        File scenario = new File(line.getOptionValue(SCENARIO));
        runner = new OneRunBatchRunner(scenario);
      } else {
        throw new ScenarioLoadException("Command line is missing required -scenario option");
      }
      
      if (line.hasOption(ID)) {
        id = line.getOptionValue(ID);
      } else {
        throw new ScenarioLoadException("Command line is missing required -id option");
      }
      
      if (line.hasOption(PINPUT)) {
        input = line.getOptionValue(PINPUT);
        isFileInput = true;
      } else {
        String[] otherArgs = line.getArgs();
        input = otherArgs[otherArgs.length - 1];
      }
      
    } catch (ParseException ex) {
      throw new ScenarioLoadException("Error while parsing command line args", ex);
    }
    
  }

  public void run() throws ScenarioLoadException {
    runner.batchInit();
    
    String line = null;
    try (BufferedReader reader = new BufferedReader(isFileInput ? new FileReader(input) : new StringReader(input))) {
      
      while ((line = reader.readLine()) != null) {
        Parameters params = lineParser.parse(line);
        int runNum = (Integer) params.getValue(BatchConstants.BATCH_RUN_PARAM_NAME);
        runner.run(runNum, params);
        if (status == RunningStatus.FAILURE)
          break;
      }
    } catch (IOException ex) {
      throw new ScenarioLoadException("Error while reading parameter input", ex);
    } 

    runner.batchCleanup();
  }
  

  // args are -pxml <parameter xml file>
  // -scenario scenario directory
  // -id instance id
  // option -input <file> if the parameter input in a file
  // if no -input then last arg is expected to be a string in 
  // unrolled parameter format.
  public static void main(String[] args) {
    try {
      InstanceRunner runner = new InstanceRunner();
      runner.configure(args);
      runner.run();
    } catch (Throwable ex) {
      msg.error("Error while running model", ex);
    }
  }
}
