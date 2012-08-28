/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Copies the output from remote ssh runs into a local directory.
 * 
 * @author Nick Collier
 */
public class RemoteOutputCopier {

  private static class Instance {
    String dir;
    List<File> files = new ArrayList<File>();

    public Instance(String dir) {
      this.dir = dir;
    }

    public String getDirectory() {
      return dir;
    }

    public void addFile(File file) {
      files.add(file);
    }

    public List<File> getFiles() {
      return files;
    }
  }

  private static Logger logger = Logger.getLogger(RemoteOutputCopier.class);

  private void findOutputFiles(SSHSession session, List<Instance> instances) throws JSchException,
      SftpException {

    for (Instance instance : instances) {
      List<String> files = session.listRemoteDirectory(instance.getDirectory());
      String batchParamFile = null;

      // find the batch parameter map file
      for (String file : files) {
        if (file.contains(BatchConstants.PARAM_MAP_SUFFIX)) {
          batchParamFile = file;
          break;
        }
      }

      // got the batch_param file, find the matching output file
      if (batchParamFile == null)
        logger.warn("No model output found in " + instance.getDirectory());
      else {
        int index = batchParamFile.indexOf(BatchConstants.PARAM_MAP_SUFFIX);
        String matchFile = batchParamFile.substring(0, index - 1);

        for (String file : files) {
          if (file.startsWith(matchFile)) {
            instance.addFile(new File(instance.getDirectory(), file));
          }
        }
      }
    }
  }

  /**
   * 
   * @param remote
   * @param remoteDir
   * @param localDir
   * 
   * @return a list of the copied files on the local machine.
   * 
   * @throws JSchException
   * @throws SftpException
   */
  public List<File> run(Remote remote, String remoteDir, String localDir)
      throws RemoteStatusException {
    SSHSession session = null;
    List<File> out = new ArrayList<File>();
    try {
      session = SSHSessionFactory.getInstance().create(remote);
      List<String> dirs = session.listRemoteDirectory(remoteDir);
      List<Instance> instances = new ArrayList<Instance>();
      for (String dir : dirs) {
        if (dir.contains(BatchConstants.INSTANCE_DIR_PREFIX)) {
          instances.add(new Instance(remoteDir + "/" + dir));
        }
      }

      findOutputFiles(session, instances);
      // instance dirs should now contain the output file
      for (Instance instance : instances) {
        out.addAll(session.copyFilesFromRemote(localDir + "/" + instance.getDirectory(),
            instance.getFiles()));
      }

    } catch (SftpException e) {
      String msg = String.format("Error while copying output files from %s", remote.getId());
      throw new RemoteStatusException(msg, e);

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", remote.getId());
      throw new RemoteStatusException(msg, e);

   

    } finally {
      if (session != null)
        session.disconnect();
    }

    return out;

  }
}
