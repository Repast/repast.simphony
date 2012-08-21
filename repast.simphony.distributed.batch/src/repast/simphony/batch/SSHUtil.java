package repast.simphony.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHUtil {

  private static Logger logger = Logger.getLogger(SSHUtil.class);

  public static Session connect(Remote remote) throws JSchException {

    JSch jsch = new JSch();

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    Session session = jsch.getSession(remote.getUser(), remote.getHost());
    session.setConfig(config);
    session.setPassword(remote.getPassword());
    session.connect();

    return session;
  }

  public static void executeBackgroundCommand(Session session, String cmd) throws JSchException {
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

  public static List<String> getRemoteDirectories(Session session, String remoteDir) throws JSchException {
    List<String> dirs = new ArrayList<String>();
    try {
      String cmd = String.format("ls %s", remoteDir);
      StringBuilder builder = new StringBuilder();
      int exitStatus = SSHUtil.executeCmd(session, cmd, builder);

      if (exitStatus != 0) {
        return null;
      }
      
      BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        dirs.add(remoteDir + "/" + line);
      }

    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
    
    return dirs;
  }

  public static int executeCmd(Session session, String cmd) throws JSchException {
    Channel channel = null;
    try {
      channel = session.openChannel("exec");

      ((ChannelExec) channel).setCommand(cmd);
      channel.setInputStream(null);

      ((ChannelExec) channel).setErrStream(new PrintStream(new LoggingOutputStream(logger,
          Level.ERROR)));
      channel.connect();

      while (!channel.isClosed()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
      }

      return channel.getExitStatus();
    } finally {
      if (channel != null)
        channel.disconnect();
    }
  }

  /**
   * 
   * @param session
   * @param cmd
   * @param builder
   *          output is appened to this builder
   * 
   * @return the commands exit status
   * @throws JSchException
   * @throws IOException
   */
  public static int executeCmd(Session session, String cmd, StringBuilder builder)
      throws JSchException, IOException {
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
  
  private static void copyFileFromRemote(Session session, String remoteFile, String localFile) {
    
    String command = "scp -f "+ remoteFile;
    Channel channel = session.openChannel("exec");
    ((ChannelExec)channel).setCommand(command);

    // get I/O streams for remote scp
    OutputStream out = channel.getOutputStream();
    InputStream in = channel.getInputStream();
    channel.connect();

    byte[] buf = new byte[1024];
    // send '\0'
    buf[0] = 0; 
    out.write(buf, 0, 1); 
    out.flush();

    while (true) {
      
      int c = SSHUtil.checkAck(in);
      if(c != 'C'){
        break;
      }

      // read '0644 '
      in.read(buf, 0, 5);

      long filesize=0L;
      while(true){
        if(in.read(buf, 0, 1)<0){
          // error
          break; 
        }
        if(buf[0]==' ')break;
        filesize=filesize*10L+(long)(buf[0]-'0');
      }

      String file=null;
      for(int i=0;;i++){
        in.read(buf, i, 1);
        if(buf[i]==(byte)0x0a){
          file=new String(buf, 0, i);
          break;
        }
      }

      //System.out.println("filesize="+filesize+", file="+file);

      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();

      // read a content of lfile
      fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
      int foo;
      while(true){
        if(buf.length<filesize) foo=buf.length;
        else foo=(int)filesize;
        foo=in.read(buf, 0, foo);
        if(foo<0){
          // error 
          break;
        }
        fos.write(buf, 0, foo);
        filesize-=foo;
        if(filesize==0L) break;
      }
      fos.close();
      fos=null;

      if(checkAck(in)!=0){
        System.exit(0);
      }

      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
    }

    
  }

  public static void copyFileToRemote(Session session, String localFile) throws IOException,
      JSchException {

    File file = new File(localFile);
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

  }

  public static int checkAck(InputStream in) throws IOException {
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
}
