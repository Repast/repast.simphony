/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import repast.simphony.batch.parameter.ParametersToInput;
import simphony.util.messages.MessageCenter;

/**
 * Starts X number of simphony batch intances on the same machine that this is
 * run on. Each instance is run on a separate JVM.
 * 
 * @author Nick Collier
 */
public class LocalDriver {

  private static MessageCenter msg = MessageCenter.getMessageCenter(LocalDriver.class);

  private static class Instance {

    int id;
    File wd;

    public Instance(int id, File wd) {
      this.id = id;
      this.wd = wd;
    }
  }

  private static class ProcessRunner implements Callable<Void> {

    ProcessOutputWriter writer;
    ProcessBuilder builder;

    public ProcessRunner(ProcessBuilder builder, File log) {
      writer = new ProcessOutputWriter(log, true);
      this.builder = builder;
    }

    public Void call() throws IOException {
      Process process = builder.start();
      // the writer blocks reading the output from
      // the process.
      writer.captureOutput(process);
      return null;
    }
  }

  private ExecutorService executor;
  private List<Future<Void>> futures;
  private List<Instance> instances = new ArrayList<Instance>();

  private File unrollXMLParamsIntoFile(File workingDir, File batchParamFile) throws IOException {
    ParametersToInput toInput;
    try {
      toInput = new ParametersToInput(batchParamFile);
    } catch (ParserConfigurationException ex) {
      throw new IOException(ex);
    } catch (SAXException ex) {
      throw new IOException(ex);
    }

    File input = new File(workingDir, "batch_parameters_input.txt");
    toInput.formatForInput(input, new File(workingDir, "parameters_for_run.csv"));
    return input;
  }
  
  private void writeInput(String input, File file) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(input);
    }
  }

  public void run(String propsFile) throws IOException {
    // load the message center log4j properties.
    Properties props = new Properties();
    File in = new File("./MessageCenter.log4j.properties");
    props.load(new FileInputStream(in));
    PropertyConfigurator.configure(props);

    props = new Properties();
    props.load(new FileReader(propsFile));

    File wd = new File(props.getProperty(BatchConstants.WORKING_DIRECTORY_PN));

    int instanceCount = Integer.parseInt(props.getProperty(BatchConstants.INSTANCE_COUNT_PN, "1"));
    File batchParamFile = new File(props.getProperty(BatchConstants.BATCH_PARAM_FILE_PN))
        .getCanonicalFile();

    List<String> inputs = null;
    if (props.getProperty(BatchConstants.UNROLLED_BATCH_PARAM_FILE_PN, "").length() > 0) {
      // unrolled input already exists so use that.
      inputs = createInputArgs(instanceCount,
          new File(props.getProperty(BatchConstants.UNROLLED_BATCH_PARAM_FILE_PN)));
    } else {
      File file = unrollXMLParamsIntoFile(wd, batchParamFile);
      inputs = createInputArgs(instanceCount, file);
    }

    File scenario = new File(props.getProperty(BatchConstants.SCENARIO_DIRECTORY_PN))
        .getCanonicalFile();
    File libDir = new File(props.getProperty(BatchConstants.REPAST_LIB_DIRECTORY_PN))
        .getCanonicalFile();

    futures = new ArrayList<Future<Void>>();
    executor = Executors.newFixedThreadPool(instanceCount);

    File file = new File("./" + BatchConstants.DONE_FILE_NAME);
    file.delete();

    String vmArgs = props.getProperty(BatchConstants.VM_ARGS, "");

    boolean mkSymLink = new File("./data").exists();

    try {
      for (int i = 0; i < instanceCount; i++) {
        int id = i + 1;
        File subwd = new File(wd, BatchConstants.INSTANCE_DIR_PREFIX + id).getCanonicalFile();
        subwd.mkdirs();
        if (mkSymLink)
          makeSymLink(subwd);
        instances.add(new Instance(id, subwd));
        String input = inputs.get(i);
        File inputFile = new File(subwd, "param_input.txt");
        writeInput(input, inputFile);
        runInstance(vmArgs, inputFile.getCanonicalPath(), libDir, batchParamFile, scenario, subwd, String.valueOf(id));
      }

      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (ExecutionException ex) {
          ex.getCause().printStackTrace();
          msg.error("", ex);
        } catch (InterruptedException ex) {
          ex.printStackTrace();
          msg.error("", ex);
        }
      }
    } finally {
      executor.shutdown();
      createStatusOutput();
      file.createNewFile();
    }
  }

  private void makeSymLink(File instanceDir) {

    int exitCode = 1;
    try {
      ProcessBuilder builder = new ProcessBuilder();
      builder.directory(instanceDir);
      builder.redirectErrorStream(true);

      // try mklink first,
      builder.command("cmd", "/c", "mklink", "/D", "/J",
          "\"" + new File(instanceDir, "data").getCanonicalPath() + "\"", "\""
              + new File(instanceDir.getParentFile(), "data").getCanonicalPath() + "\"");

      Process p = builder.start();
      exitCode = p.waitFor();

    } catch (InterruptedException ex) {
    } catch (IOException ex) {
      // catch this with no error -- assumed tried on non-windows machine
    }

    if (exitCode != 0) {
      try {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(instanceDir);
        builder.redirectErrorStream(true);

        builder.command("ln", "-s", new File(instanceDir.getParentFile(), "data")
            .getCanonicalPath(), new File(instanceDir, "data").getCanonicalPath());

        Process p = builder.start();
        exitCode = p.waitFor();

        if (exitCode != 0) {
          msg.error("Error while creating symlinks to data directory. Error = " + exitCode,
              new RuntimeException());
        }

      } catch (InterruptedException ex) {
      } catch (IOException ex) {
        msg.error("Error while creating symlinks to data directory", ex);
      }
    }
  }

  private void createStatusOutput() throws IOException {
    Properties props = new Properties();
    for (Instance instance : instances) {
      String suffix = "_" + instance.id;
      RunningStatus status = RunningStatus.OK;
      File file = new File(instance.wd, RunningStatus.FAILURE.toString() + suffix);
      if (file.exists()) {
        status = RunningStatus.FAILURE;
      } else {
        file = new File(instance.wd, RunningStatus.WARN.toString() + suffix);
        if (file.exists()) {
          status = RunningStatus.WARN;
        }
      }

      props.put(String.valueOf(instance.id), status.toString());
    }
    props.store(new FileOutputStream(BatchConstants.STATUS_OUTPUT_FILE), "");

  }

  private List<String> createInputArgs(int instances, File input) throws IOException {
    List<String> lines = createParameterStrings(input);
    List<StringBuilder> inputs = new ArrayList<StringBuilder>();
    for (int i = 0; i < instances; i++) {
      inputs.add(new StringBuilder());
    }

    int i = 0;
    for (String line : lines) {
      StringBuilder builder = inputs.get(i);
      builder.append(line);
      builder.append("\n");
      i++;
      if (i == instances)
        i = 0;
    }

    List<String> list = new ArrayList<String>();
    for (StringBuilder builder : inputs) {
      list.add(builder.toString());
    }
    return list;
  }

  private List<String> createParameterStrings(File input) throws IOException {
    List<String> list = new ArrayList<String>();
    try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.trim().length() > 0)
          list.add(line);
      }
    }

    return list;
  }

  private void runInstance(String vmArgs, String inputArg, File libDir, File batchParamFile,
      File scenarioFile, File workingDirectory, String id) throws IOException {
    ProcessBuilder builder = new ProcessBuilder();
    builder.directory(workingDirectory);

    if (vmArgs.length() > 0)
      builder.command("java", vmArgs, "-cp", libDir.getCanonicalPath() + "/*",
          "repast.simphony.batch.InstanceRunner", batchParamFile.getCanonicalPath(),
          scenarioFile.getCanonicalPath(), inputArg, id);
    else
      builder.command("java", "-cp", libDir.getCanonicalPath() + "/*",
          "repast.simphony.batch.InstanceRunner", batchParamFile.getCanonicalPath(),
          scenarioFile.getCanonicalPath(), inputArg, id);

    builder.redirectErrorStream(true);
    ProcessRunner runner = new ProcessRunner(builder, new File(workingDirectory, "instance.log"));
    futures.add(executor.submit(runner));
  }

  // args[0] is properties file path
  public static void main(String[] args) {
    LocalDriver driver = new LocalDriver();
    try {
      driver.run(args[0]);
    } catch (IOException e) {
      msg.error("", e);
    }
  }

}
