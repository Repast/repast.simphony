package repast.simphony.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteSSHDriver {

  private static Logger logger = Logger.getLogger(RemoteSSHDriver.class);
 
  public void run(String propsFile) throws IOException {
    List<Remote> remotes = new RemotePropsParser().parse(propsFile);
    String remoteDir = "simphony_model_" + System.currentTimeMillis();
    for (Remote remote : remotes) {
      File file = new File(remote.getModelArchive());
      logger.info(String.format("%n\tCopying %s to %n\t%s@%s:~/%s ...", file.getCanonicalPath(),
          remote.getUser(), remote.getHost(), file.getName()));
      copyFile(remote);
      logger.info("Copying Finished.");
    }

    for (Remote remote : remotes) {
      runModel(remote, remoteDir);
    }

    pollForDone(remoteDir, remotes);
    concatenateOutput(remoteDir, remotes);
    logger.info("Finished");
  }

  private void concatenateOutput(String remoteDir, Collection<Remote> remotes) {
    try {
      for (Remote remote : remotes) {
        
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void pollForDone(String remoteDir, Collection<Remote> remotes) {

    List<Future<Void>> futures = new ArrayList<Future<Void>>();
    ExecutorService executor = null;
    try {
      executor = Executors.newFixedThreadPool(remotes.size());
      for (Remote remote : remotes) {
        RemoteDonePoller poller = new RemoteDonePoller(remote, remoteDir);
        futures.add(executor.submit(poller));
      }

      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }

    } finally {
      if (executor != null)
        executor.shutdown();
    }
  }

  private int executeCmd(Session session, String cmd) throws JSchException, IOException {
    StringBuilder builder = new StringBuilder();
    int exitStatus = SSHUtil.executeCmd(session, cmd, builder);
    if (builder.length() > 0)
      logger.info(session.getHost() + " REMOTE OUTPUT START:\n" + builder.toString()
          + "REMOTE OUTPUT END");
    return exitStatus;
  }

  public void runModel(Remote remote, String remoteDir) {
    Session session = null;
    try {
      session = SSHUtil.connect(remote);
      File file = new File(remote.getModelArchive());
      // mkdir and unzip
      String cmd = String.format("mkdir %s", remoteDir);
      int exitStatus = executeCmd(session, cmd);

      if (exitStatus != 0) {
        // mkdir will fail if the directory already exists
        // error will be printed to the log
        return;
      }

      logger.info(String.format("Unzipping model on %s@%s ...", remote.getUser(), remote.getHost()));
      cmd = String.format("mv %s %s;cd %s; unzip -n %s", file.getName(), remoteDir, remoteDir,
          file.getName());

      exitStatus = executeCmd(session, cmd);
      // if mv or unzip fails
      // error will be printed to the log
      if (exitStatus != 0) {
        return;
      }

      logger.info(String.format("Running model on %s@%s ...", remote.getUser(), remote.getHost()));
      cmd = String
          .format(
              "cd %s; nohup java -cp \"./lib/*\" repast.simphony.batch.LocalDriver local_batch_run.properties ",
              remoteDir);
      // executes in the background, this session will disconnect
      SSHUtil.executeBackgroundCommand(session, cmd);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);

    } finally {
      if (session != null)
        session.disconnect();
    }
  }

  private void copyFile(Remote remote) {
    try {
      Session session = SSHUtil.connect(remote);
      SSHUtil.copyFileToRemote(session, remote.getModelArchive());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      File in = new File("./config/SSH.MessageCenter.log4j.properties");
      props.load(new FileInputStream(in));
      PropertyConfigurator.configure(props);

      new RemoteSSHDriver().run(args[0]);
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }

}
