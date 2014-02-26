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

  private List<MatchedFiles> findOutputFiles(SSHSession session, List<String> instanceDirs) throws JSchException,
      SftpException {

    // assumes remote file use "/" as separator so useWindowsSeparator is false
    List<MatchedFiles> matchers = createMatches(false);
    for (String dir : instanceDirs) {
      List<String> files = session.listRemoteDirectory(dir, true);
      // session returns file including instance directory, we want to remove
      // that and the trailing "/"
      List<String> fixedFiles = new ArrayList<>();
      for (String file : files) {
        fixedFiles.add(file.substring(dir.length() + 1));
        //System.out.println(fixedFiles.get(fixedFiles.size() - 1));
      }

      findFiles(matchers, fixedFiles, dir);
    }
    return matchers;
  }

  /**
   * 
   * @param remote
   * @param remoteDir
   * @param localDir
   * 
   * @return a list of the MatchedFiles copied to the local matchine.
   * 
   * @throws JSchException
   * @throws SftpException
   */
  public List<MatchedFiles> run(RemoteSession remote, String remoteDir, String localDir)
      throws StatusException {
    SSHSession session = null;
    
    List<MatchedFiles> localMatches  = new ArrayList<>();
    
    try {
      session = SSHSessionFactory.getInstance().create(remote);
      List<String> dirs = session.listRemoteDirectory(remoteDir);
      List<String> instances = new ArrayList<String>();
      for (String dir : dirs) {
        if (dir.contains(BatchConstants.INSTANCE_DIR_PREFIX)) {
          instances.add(dir);
        }
      }

      List<MatchedFiles> matches = findOutputFiles(session, instances);
      // instance dirs should now contain the output file
      for (MatchedFiles match : matches) {
        MatchedFiles newMatch = new MatchedFiles(match.getPattern());
        for (File file : session.copyFilesFromRemote(localDir, match.getFiles(), true)) {
          newMatch.addFile(file);
        }
        localMatches.add(newMatch);
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

    return localMatches;
  }
}
