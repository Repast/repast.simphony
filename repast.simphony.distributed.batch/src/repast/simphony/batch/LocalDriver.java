/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import repast.simphony.batch.parameter.ParametersToInput;

/**
 * @author Nick Collier
 */
public class LocalDriver {

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

  public void run(String propsFile) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(propsFile));

    File batchParamFile = new File(props.getProperty(BatchConstants.BATCH_PARAM_FILE_PN))
        .getCanonicalFile();
    File wd = new File(props.getProperty(BatchConstants.WORKING_DIRECTORY_PN));
    ParametersToInput toInput;
    try {

      toInput = new ParametersToInput(batchParamFile);

    } catch (Exception ex) {
      throw new IOException(ex);
    }

    File input = new File(wd, "batch_parameters_input.txt");
    toInput.formatForInput(input, new File(wd, "parameters_for_run.csv"));

    int instanceCount = Integer.parseInt(props.getProperty(BatchConstants.INSTANCE_COUNT_PN, "-1"));
    if (instanceCount == -1) {
      instanceCount = Runtime.getRuntime().availableProcessors();
    }

    List<String> inputs = createInputArgs(instanceCount, input);

    File scenario = new File(props.getProperty(BatchConstants.SCENARIO_DIRECTORY_PN))
        .getCanonicalFile();
    File libDir = new File(props.getProperty(BatchConstants.REPAST_LIB_DIRECTORY_PN))
        .getCanonicalFile();

    futures = new ArrayList<Future<Void>>();
    executor = Executors.newFixedThreadPool(instanceCount);
    
    File file = new File("./DONE");
    file.delete();
    
    try {

      String hostname = InetAddress.getLocalHost().getCanonicalHostName();
      for (int i = 0; i < instanceCount; i++) {
        File subwd = new File(wd, "instance_" + (i + 1)).getCanonicalFile();
        subwd.mkdirs();
        String inputArg = inputs.get(i);
        runInstance(inputArg, libDir, batchParamFile, scenario, subwd, hostname + " " + (i + 1));
      }
      
      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (ExecutionException ex) {
          ex.getCause().printStackTrace();
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }
    } finally {
      file.createNewFile();
      executor.shutdown();
    }
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
    BufferedReader reader = new BufferedReader(new FileReader(input));
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (line.trim().length() > 0)
        list.add(line);
    }

    return list;
  }

  private void runInstance(String inputArg, File libDir, File batchParamFile, File scenarioFile,
      File workingDirectory, String id) throws IOException {
    ProcessBuilder builder = new ProcessBuilder();
    builder.directory(workingDirectory);
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
