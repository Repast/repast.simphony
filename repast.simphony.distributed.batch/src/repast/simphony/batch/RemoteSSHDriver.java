package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class RemoteSSHDriver {
  
  static class Remote {
    String user, host, password, file;
  }
  
  public void run(String propsFile) throws IOException {
    Map<String, Remote> remotes = loadRemotes(propsFile);
    for (Remote remote : remotes.values()) {
      File file = new File(remote.file);
      // TODO this should be logged
      System.out.println(String.format("Copying %s to %s@%s:~/%s ...", 
          file.getCanonicalPath(), remote.user, remote.host, file.getName()));
      copyFile(remote);
      System.out.println("Copying Finished.");
    }
    
    for (Remote remote : remotes.values()) {
      System.out.println(String.format("Running model on %s@%s ...", remote.user, remote.host));
      runModel(remote);
    }
  }
  
  private Map<String, Remote> loadRemotes(String propsFile) throws IOException{
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
      if (remote.user != null) throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.user = val.trim();
    } else if (type.equals("host")) {
      if (remote.host != null) throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.host = val.trim();
    } else if (type.equals("password")) {
      if (remote.password != null) throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.password = val.trim();
    } else if (type.equals("file")) {
      if (remote.file != null) throw new IOException(String.format("Duplicate property %s for %s", type, key));
      if (!new File(val.trim()).exists()) throw new IOException(val + " for " + key + " doesn't exist");
      remote.file = val.trim();
    }
  }
  
  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 3)
      throw new IOException("Invalid remote properties configuration for '" + key + "': expected remote.X.[host|user|password|file]");
    if (!vals[0].equals("remote"))
      throw new IOException("Invalid remote properties configuration:" + key);
    
    if (!(vals[2].equals("user") || vals[2].equals("host") || vals[2].equals("password") || vals[2].equals("file"))) {
      throw new IOException("Invalid remote properties configuration:" + key);
    }
  }

  public void runModel(Remote remote) {
    JSch jsch = new JSch();
    try {

      // use this to avoid "UnknownHostKey" errors.
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");

      Session session = jsch.getSession(remote.user, remote.host);
      session.setConfig(config);
      session.setPassword(remote.password);
      session.connect();

      Channel channel = session.openChannel("exec");
      File file = new File(remote.file);
      ZipFile zipFile = new ZipFile(file);
      
      ZipEntry entry = zipFile.entries().nextElement();
      String path = new File(entry.getName()).getPath();
      System.out.println("PATH: " + path);
      int index = path.indexOf(File.separator);
      index = index == -1 ? path.length() : index;
      path = path.substring(0, index);
      System.out.println("PATH: " + path);
      
      String javaCmd = String.format("unzip %s;cd %s; " +
      		"java -cp \"./lib/*\" repast.simphony.batch.LocalDriver local_batch_run.properties", file.getName(), path);
      ((ChannelExec) channel).setCommand(javaCmd);
      channel.setInputStream(null);
      
      ((ChannelExec) channel).setErrStream(System.err);
      InputStream in = channel.getInputStream();
      channel.connect();
      
      //channel.setOutputStream(System.out);

      // idea here is to poll for the channel to be closed,
      // and get any output from the command, if there is any.
      // exit status should 0 on success.
      byte[] tmp = new byte[1024];
      while (true) {
        while (in.available() > 0) {
          int i = in.read(tmp, 0, 1024);
          if (i < 0)
            break;
          System.out.print(new String(tmp, 0, i));
        }
        
        if (channel.isClosed()) {
          System.out.println("exit-status: " + channel.getExitStatus());
          break;
        }
        try {
          Thread.sleep(1000);
        } catch (Exception ee) {
        }
      }
      channel.disconnect();
      session.disconnect();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void copyFile(Remote remote) {
    JSch jsch = new JSch();
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
      e.printStackTrace();
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
      new RemoteSSHDriver().run(args[0]);
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }

}
