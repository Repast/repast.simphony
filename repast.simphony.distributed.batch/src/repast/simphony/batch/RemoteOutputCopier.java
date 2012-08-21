/**
 * 
 */
package repast.simphony.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Copies the output from remote ssh runs into a local directory.
 * 
 * @author Nick Collier
 */
public class RemoteOutputCopier {
  
  private static Logger logger = Logger.getLogger(RemoteOutputCopier.class);
  private static String PARAM_MAP_SUFFIX = "batch_param_map";
  
  private List<String> findOutputFiles(Session session, List<String> instanceDirs) throws JSchException {
    
    List<String> outputFiles = new ArrayList<String>();
    for (String instanceDir : instanceDirs) {
      List<String> files = SSHUtil.getRemoteDirectories(session, instanceDir);
      String batchParamFile = null;
      for (String file : files) {
        if (file.contains(PARAM_MAP_SUFFIX)) {
          batchParamFile = file;
          break;
        }
      }
      // got the batch_param file, find the matching output file
      if (batchParamFile == null) logger.warn("No model output found in " + instanceDir);
      else {
        int index = batchParamFile.indexOf(PARAM_MAP_SUFFIX);
        String matchFile = batchParamFile.substring(0, index - 1);
        
        for (String file : files) {
          if (file.startsWith(matchFile)) {
            outputFiles.add(file);
          }
        }
      }
    }
    
    return outputFiles;
  }

  public void run(Remote remote, String remoteDir, String localDir) throws JSchException {
    Session session = null;
    try {
      session = SSHUtil.connect(remote);
      List<String> dirs = SSHUtil.getRemoteDirectories(session, remoteDir);
      List<String> instanceDirs = new ArrayList<String>();
      for (String dir : dirs) {
        if (dir.contains(BatchConstants.INSTANCE_DIR_PREFIX)) {
          instanceDirs.add(dir);
        }
      }
      
      List<String> outputFiles = findOutputFiles(session, instanceDirs);
      
    } finally {
      if (session != null) session.disconnect();
    }
    
  }
}
