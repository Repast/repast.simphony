package repast.simphony.ws;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import repast.simphony.batch.ProcessOutputWriter;

/**
 * Launches a simphony model in another process.
 * 
 * @author Nick Collier
 */
public class ModelLauncher {

  private static class ProcessRunner implements Callable<Void> {

    ProcessOutputWriter writer;
    ProcessBuilder builder;

    public ProcessRunner(ProcessBuilder builder, File log) {
      writer = new ProcessOutputWriter(log, true);
      this.builder = builder;
    }

    public Void call() throws IOException {
      try {
        Process process = builder.start();
        // the writer blocks reading the output from
        // the process.
        writer.captureOutput(process);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      }

      return null;
    }
  }

  public void run(Properties props) throws IOException {

    // load the message center log4j properties.
    // Properties props = new Properties();
    // File in = new File("./MessageCenter.log4j.properties");
    // props.load(new FileInputStream(in));
    // PropertyConfigurator.configure(props);

    File wd = new File(props.getProperty(WSConstants.WORKING_DIRECTORY)).getCanonicalFile();
    File scenario = new File(props.getProperty(WSConstants.SCENARIO_DIRECTORY)).getCanonicalFile();
    File libDir = new File(props.getProperty(WSConstants.REPAST_LIB_DIRECTORY)).getCanonicalFile();
    String vmArgs = props.getProperty(WSConstants.VM_ARGS, "");
    String identity = props.getProperty(WSConstants.IDENTITY);
    String host = props.getProperty(WSConstants.HOST);
    ProcessRunner runner = createRunner(vmArgs, libDir, scenario, wd, identity, host);
    FutureTask<Void> task = new FutureTask<>(runner);
    new Thread(task).start();
  }

  private ProcessRunner createRunner(String vmArgs, File libDir, File scenarioFile,
      File workingDirectory, String identity, String host) throws IOException {
    ProcessBuilder builder = new ProcessBuilder();
    builder.directory(workingDirectory);
    String cp = libDir.getCanonicalPath() + "/*" + ":" + workingDirectory.getCanonicalPath()
        + "/bin" + ":" + workingDirectory.getCanonicalPath() + "/lib/*";

    if (vmArgs.length() > 0)
      builder.command("java", vmArgs, "-cp", cp, "repast.simphony.ws.OneRunRunner",
          workingDirectory.getCanonicalPath(), scenarioFile.getCanonicalPath(), identity, host);
    else
      builder.command("java", "-cp", cp, "repast.simphony.ws.OneRunRunner",
          workingDirectory.getCanonicalPath(), scenarioFile.getCanonicalPath(), identity, host);

    System.out.println(String.join(",", builder.command()));

    builder.redirectErrorStream(true);
    ProcessRunner runner = new ProcessRunner(builder, new File(workingDirectory, "instance.log"));
    return runner;
  }

}
