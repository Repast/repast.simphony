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

  /**
   * Executes the specified command in the background.
   * 
   * @param cmd
   * @throws JSchException
   */
  public void executeBackgroundCommand(String cmd, boolean isRemoteWindows) throws JSchException {
    Channel channel = null;
    try {
      channel = session.openChannel("exec");
      ((ChannelExec) channel).setPty(false);
      
      if (isRemoteWindows) {
      	 ((ChannelExec) channel).setCommand(cmd);
      }
      else {
      	// TODO maybe remove 2>&1 as that is bash specific
      	((ChannelExec) channel).setCommand(cmd + " > /dev/null 2>&1 &");
      }
      channel.setInputStream(null);
      channel.connect();

    } finally {
      if (channel != null)
        channel.disconnect();
    }
  }

  /*
   * Lists the non-directory contents the specified remote directory and places the
   * results into the files parameters. If recurse is true, then
   * this will recurse into any directories and list the contents
   * of those directories.
   * 
   * @param sftp
   * @param remoteDir
   * @param files
   * @param recurse
   * @throws SftpException
   */
  @SuppressWarnings("unchecked")
  private void listRemoteDirectory(ChannelSftp sftp, String remoteDir, List<String> files,
      boolean recurse) throws SftpException {

    Vector<ChannelSftp.LsEntry> entries = sftp.ls(remoteDir);
    List<String> subs = new ArrayList<>();
    for (ChannelSftp.LsEntry entry : entries) {
      String filename = entry.getFilename();
      if (!filename.equals(".") && !filename.equals("..")) {
        if (recurse && entry.getAttrs().isDir())
          subs.add(remoteDir + "/" + filename);
        
        files.add(remoteDir + "/" + filename);
      }
    }

    for (String sub : subs) {
      listRemoteDirectory(sftp, sub, files, true);
    }
  }

  /**
   * Returns the listing of the specified remote directory as a list of Strings.
   * 
   * @param remoteDir
   * @return
   * @throws JSchException
   * @throws SftpException
   */
  public List<String> listRemoteDirectory(String remoteDir) throws JSchException, SftpException {
    return listRemoteDirectory(remoteDir, false);
  }

  /**
   * Returns the file listing of the specified remote directory as a list of Strings
   * with an option to recurse into any directories that are found. If recurse is true
   * then the files in the subdirectories will be returned with that subdirectory
   * prefixed to the file name (e.g. output/file1).
   * 
   * @param remoteDir
   * @return
   * @throws JSchException
   * @throws SftpException
   */
  public List<String> listRemoteDirectory(String remoteDir, boolean recurse) throws JSchException,
      SftpException {
    List<String> out = new ArrayList<String>();
    ChannelSftp sftp = null;
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      listRemoteDirectory(sftp, remoteDir, out, recurse);
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }

    return out;
  }

  /**
   * Execute the specifed command on the remote machine and 
   * wait for the resutl.
   * 
   * @param cmd
   * @param level
   * @return
   * @throws JSchException
   */
  public int executeCmd(String cmd, Level level) throws JSchException {
    Channel channel = null;
    try {
      channel = session.openChannel("exec");

      ((ChannelExec) channel).setCommand(cmd);
      channel.setInputStream(null);

      ((ChannelExec) channel).setErrStream(new PrintStream(new LoggingOutputStream(logger, level)));
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
   * Execute the specified command on the remote machine and append any
   * output to the StringBuilder. If setErrStream is true then errors
   * will be directory to a LoggingOutputStream.
   * 
   * @param cmd
   * @param builder
   * @param setErrStream
   * @return
   * @throws JSchException
   * @throws IOException
   */
  public int executeCmd(String cmd, StringBuilder builder, boolean setErrStream)
      throws JSchException, IOException {
    Channel channel = null;
    int exitStatus = 0;
    try {
      channel = session.openChannel("exec");
      ((ChannelExec) channel).setCommand(cmd);
      channel.setInputStream(null);

      if (setErrStream)
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

  /**
   * Compies the specified file to the home directory of the user 
   * used to log in to the remote.
   * 
   * @param localFile
   * @throws JSchException
   * @throws SftpException
   */
  public void copyFileToRemote(File localFile) throws JSchException, SftpException {
    ChannelSftp sftp = null;
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      sftp.put(localFile.getAbsolutePath().replace("\\", "/"), localFile.getName());
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }
  }

  /**
   * Copies the specified file to the specified remote directory.
   * 
   * @param localFile
   * @param remoteDirectory
   * @throws JSchException
   * @throws SftpException
   */
  public void copyFileToRemote(File localFile, String remoteDirectory) throws JSchException,
      SftpException {
    ChannelSftp sftp = null;
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      sftp.cd(remoteDirectory);
      sftp.put(localFile.getAbsolutePath().replace("\\", "/"), localFile.getName());
    } finally {
      if (sftp != null)
        sftp.exit();
      sftp.disconnect();
    }
  }

  /**
   * Copies the specified remote file to the specified local directory. If preserveRemotePath
   * is true then path will be preserved when the file is copied. For example, assuming a local
   * directory of "foo", then copying "output/out.txt" will create "foo/output/out.txt".  
   *  
   * @param localDir
   * @param remoteFile
   * @param preserveRemotePath
   * @return
   * @throws SftpException
   * @throws JSchException
   */
  public File copyFileFromRemote(String localDir, File remoteFile, boolean preserveRemotePath) throws SftpException,
      JSchException {
    List<File> files = new ArrayList<File>();
    files.add(remoteFile);
    return copyFilesFromRemote(localDir, files, preserveRemotePath).get(0);
  }

  /**
   * Copies files from the remote to the local directory preserving the directory 
   * structure. If preserveRemotePath
   * is true then path will be preserved when the file is copied. For example, assuming a local
   * directory of "foo", then copying "output/out.txt" will create "foo/output/out.txt".  
   *  
   * 
   * @param localDir
   * @param remoteFiles
   * @return
   * @throws SftpException
   * @throws JSchException
   */
  public List<File> copyFilesFromRemote(String localDir, List<File> remoteFiles, boolean preserveRemotePath)
      throws SftpException, JSchException {
    ChannelSftp sftp = null;
    List<File> out = new ArrayList<File>();
    try {
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
      File ld = new File(localDir);
      ld.mkdirs();

      for (File remoteFile : remoteFiles) {
        String path = preserveRemotePath ? remoteFile.getPath() : remoteFile.getName();
        File dst = new File(localDir, path);
        dst.getParentFile().mkdirs();
        //System.out.printf("copying %s to %s%n", remoteFile.getPath(), dst.toString());
        sftp.get(remoteFile.getPath().replace("\\", "/"), dst.getPath().replace("\\", "/"));

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
