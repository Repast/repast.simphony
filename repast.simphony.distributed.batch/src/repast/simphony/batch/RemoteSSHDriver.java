package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteSSHDriver {

  private static Logger logger = Logger.getLogger(RemoteSSHDriver.class);
  private JSch jsch = new JSch();

  static class Remote {
    String user, host, password, file;
  }

  public void run(String propsFile) throws IOException {
    Map<String, Remote> remotes = loadRemotes(propsFile);
    String remoteDir = "simphony_model_" + System.currentTimeMillis();
    for (Remote remote : remotes.values()) {
      File file = new File(remote.file);
      logger.info(String.format("%n\tCopying %s to %n\t%s@%s:~/%s ...", file.getCanonicalPath(),
          remote.user, remote.host, file.getName()));
      copyFile(remote);
      logger.info("Copying Finished.");
    }

    for (Remote remote : remotes.values()) {
      runModel(remote, remoteDir);
    }
    
    pollForDone(remoteDir, remotes.values());
    concatenateOutput(remoteDir, remotes.values());
    logger.info("Finished");
  }
  
  private void concatenateOutput(String remoteDir, Collection<Remote> remotes)  {
    for (Remote remote : remotes) {
      List<String> dirs = getInstanceDirectories(remote, remoteDir);
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
      if (executor != null) executor.shutdown();
    }

  }

  private Map<String, Remote> loadRemotes(String propsFile) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(propsFile));

    Map<String, Remote> remoteMap = new HashMap<String, Remote>();
    for (Object key : props.keySet()) {
      String[] vals = key.toString().trim().split("\\.");
      checkVals(key.toString(), vals);
      Remote remote = remoteMap.get(vals[1]);
      if (remote == null) {
        remote = new Remote();
        remoteMap.put(vals[1], remote);
      }
      setRemote(remote, key.toString(), vals[2], props.get(key).toString());
    }

    return remoteMap;
  }

  private void setRemote(Remote remote, String key, String type, String val) throws IOException {
    if (type.equals("user")) {
      if (remote.user != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.user = val.trim();
    } else if (type.equals("host")) {
      if (remote.host != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.host = val.trim();
    } else if (type.equals("password")) {
      if (remote.password != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.password = val.trim();
    } else if (type.equals("file")) {
      if (remote.file != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      if (!new File(val.trim()).exists())
        throw new IOException(val + " for " + key + " doesn't exist");
      remote.file = val.trim();
    }
  }

  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 3)
      throw new IOException("Invalid remote properties configuration for '" + key
          + "': expected remote.X.[host|user|password|file]");
    if (!vals[0].equals("remote"))
      throw new IOException("Invalid remote properties configuration:" + key);

    if (!(vals[2].equals("user") || vals[2].equals("host") || vals[2].equals("password") || vals[2]
        .equals("file"))) {
      throw new IOException("Invalid remote properties configuration:" + key);
    }
  }

  private void executeBackgroundCmd(Session session, String cmd) throws JSchException, IOException {
    Channel channel = null;
    try {
      channel = session.openChannel("exec");
      ((ChannelExec) channel).setPty(false);
      // TODO maybe remove 2>&1 as that is bash specific
      ((ChannelExec) channel).setCommand(cmd + " > /dev/null 2>&1 &");
      channel.setInputStream(null);
      channel.connect();
    } finally {
      if (channel != null)
        channel.disconnect();
    }
  }
  
  private List<String> getInstanceDirectories(Remote remote, String remoteDir) {
    Session session = null;
    try {
      // use this to avoid "UnknownHostKey" errors.
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");

      session = jsch.getSession(remote.user, remote.host);
      session.setConfig(config);
      session.setPassword(remote.password);
      session.connect();
      
      String cmd = String.format("ls %s", remoteDir);
      StringBuilder builder = new StringBuilder();
      int exitStatus = executeCmd(session, cmd, builder);
      logger.info(builder.toString());
      
      if (exitStatus != 0) {
        // mkdir will fail if the directory already exists
        // error will be printed to the log
        return null;
      }
      
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      
    } finally {
      if (session != null) session.disconnect();
    }
    
    return null;
    
  }
  

  private int executeCmd(Session session, String cmd, StringBuilder builder) throws JSchException, IOException {
    Channel channel = null;
    int exitStatus = 0;
    try {
      channel = session.openChannel("exec");
      ((ChannelExec) channel).setCommand(cmd);
      channel.setInputStream(null);

      ((ChannelExec) channel).setErrStream(new PrintStream(new LoggingOutputStream(logger,
          Level.ERROR)));
      InputStream in = channel.getInputStream();
      channel.connect();

      // idea here is to poll for the channel to be closed,
      // and get any output from the command, if there is any.
      // exit status should 0 on success.

      byte[] tmp = new byte[1024];
      while (true) {
        while (in.available() > 0) {
          int i = in.read(tmp, 0, 1024);
          if (i < 0)
            break;
          builder.append(new String(tmp, 0, i));
        }

        if (channel.isClosed()) {
          exitStatus = channel.getExitStatus();
          break;
        }
        try {
          Thread.sleep(1000);
        } catch (Exception ee) {
        }
      }

    } finally {
      if (channel != null)
        channel.disconnect();
    }

    return exitStatus;
  }
  
  private int executeCmd(Session session, String cmd) throws JSchException, IOException {
    StringBuilder builder = new StringBuilder();
    int exitStatus = executeCmd(session, cmd, builder);
    if (builder.length() > 0)
      logger.info(session.getHost() + " REMOTE OUTPUT START:\n" + builder.toString()
          + "REMOTE OUTPUT END");
    return exitStatus;
  }

  public void runModel(Remote remote, String remoteDir) {
    Session session = null;
    try {
      // use this to avoid "UnknownHostKey" errors.
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");

      session = jsch.getSession(remote.user, remote.host);
      session.setConfig(config);
      session.setPassword(remote.password);
      session.connect();

      File file = new File(remote.file);
      // mkdir and unzip
      String cmd = String.format("mkdir %s", remoteDir);
      int exitStatus = executeCmd(session, cmd);
      
      if (exitStatus != 0) {
        // mkdir will fail if the directory already exists
        // error will be printed to the log
        return;
      }

      logger.info(String.format("Unzipping model on %s@%s ...", remote.user, remote.host));
      cmd = String.format("mv %s %s;cd %s; unzip -n %s", file.getName(), remoteDir, remoteDir,
          file.getName());

      exitStatus = executeCmd(session, cmd);
      // if mv or unzip fails
      // error will be printed to the log
      if (exitStatus != 0) {
        return;
      }

      logger.info(String.format("Running model on %s@%s ...", remote.user, remote.host));
      cmd = String
          .format(
              "cd %s; nohup java -cp \"./lib/*\" repast.simphony.batch.LocalDriver local_batch_run.properties ",
              remoteDir);
      // executes in the background, this session will disconnect
      executeBackgroundCmd(session, cmd);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);

    } finally {
      if (session != null)
        session.disconnect();
    }

  }

  public void copyFile(Remote remote) {
    try {

      // use this to avoid "UnknownHostKey" erros.
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");

      Session session = jsch.getSession(remote.user, remote.host);
      session.setConfig(config);
      session.setPassword(remote.password);
      session.connect();

      File file = new File(remote.file);
      // this is the name of the file in the
      //
      String cmd = "scp -t " + file.getName();
      Channel channel = session.openChannel("exec");
      ((ChannelExec) channel).setCommand(cmd);

      OutputStream out = channel.getOutputStream();
      InputStream in = channel.getInputStream();

      channel.connect();

      if (checkAck(in) != 0) {
        return;
      }

      long size = file.length();
      // !!! DON'T SEND THE "/" IN THE FILENAME
      cmd = "C0644 " + size + " " + file.getName() + "\n";
      out.write(cmd.getBytes());
      out.flush();

      if (checkAck(in) != 0) {
        return;
      }

      FileInputStream fis = new FileInputStream(file);
      byte[] buf = new byte[1024];
      while (true) {
        int len = fis.read(buf, 0, buf.length);
        if (len <= 0)
          break;
        out.write(buf, 0, len);
      }
      fis.close();
      // send '\0'
      buf[0] = 0;
      out.write(buf, 0, 1);
      out.flush();

      if (checkAck(in) != 0) {
        return;
      }
      out.close();

      channel.disconnect();
      session.disconnect();

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private int checkAck(InputStream in) throws IOException {
    int b = in.read();
    if (b == 0 || b == -1)
      return b;
    else {
      // print out the error message;
      StringBuilder builder = new StringBuilder();
      String line = null;
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      // b == 1 error, b == 2 fatal error
      System.out.println(builder.toString());
    }

    return b;

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
