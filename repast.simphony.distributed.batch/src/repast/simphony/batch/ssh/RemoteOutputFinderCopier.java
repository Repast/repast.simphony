/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.batch.BatchConstants;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Copies the output from remote ssh runs into a local directory.
 * 
 * @author Nick Collier
 */
public class RemoteOutputFinderCopier extends OutputFinder {
  
  private void findOutputFiles(SSHSession session, List<Instance> instances) throws JSchException,
      SftpException {

    for (Instance instance : instances) {
      List<String> files = session.listRemoteDirectory(instance.getDirectory());
      findFiles(files, instance);
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
  public List<File> run(RemoteSession remote, String remoteDir, String localDir)
      throws StatusException {
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
      throw new StatusException(msg, e);

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", remote.getId());
      throw new StatusException(msg, e);

   

    } finally {
      if (session != null)
        session.disconnect();
    }

    return out;

  }
}
