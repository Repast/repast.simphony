/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import repast.simphony.batch.LoggingOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @author Nick Collier
 */
public class SSHSession {
  
  private static Logger logger = Logger.getLogger(SSHSession.class);

  private Session session;

  public SSHSession(Session session) {
    this.session = session;
  }

  public void executeBackgroundCommand(String cmd) throws JSchException {
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

  @SuppressWarnings("unchecked")
  public List<String> listRemoteDirectory(String remoteDir) throws JSchException,
      SftpException {
    List<String> out = new ArrayList<String>();
    ChannelSftp sftp = null;
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      Vector<ChannelSftp.LsEntry> entries = sftp.ls(remoteDir);
      for (ChannelSftp.LsEntry entry : entries) {
        out.add(entry.getFilename());
      }
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }

    return out;
  }

  public int executeCmd(String cmd) throws JSchException {
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
  public int executeCmd(String cmd, StringBuilder builder) throws JSchException,
      IOException {
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

  public void copyFileToRemote(File localFile) throws JSchException, SftpException {
    ChannelSftp sftp = null;
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      sftp.put(localFile.getAbsolutePath(), localFile.getName());
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }
  }

  public File copyFileFromRemote(String localDir, File remoteFile)
      throws SftpException, JSchException {
    List<File> files = new ArrayList<File>();
    files.add(remoteFile);
    return copyFilesFromRemote(localDir, files).get(0);
  }

  public List<File> copyFilesFromRemote(String localDir, List<File> remoteFiles)
      throws SftpException, JSchException {
    ChannelSftp sftp = null;
    List<File> out = new ArrayList<File>();
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      File ld = new File(localDir);
      ld.mkdirs();

      for (File remoteFile : remoteFiles) {
        // System.out.printf("copying %s to %s%n", remoteFile.getPath(),
        // localDir + "/" + remoteFile.getName());
        File dst = new File(localDir + "/" + remoteFile.getName());
        sftp.get(remoteFile.getPath(), dst.getPath());
        out.add(dst);
      }
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }

    return out;
  }
  
  public void disconnect() {
    session.disconnect();
  }

}
