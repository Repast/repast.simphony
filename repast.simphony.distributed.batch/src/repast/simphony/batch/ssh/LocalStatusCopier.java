/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.batch.BatchConstants;
import repast.simphony.batch.RunningStatus;
import repast.simphony.util.FileUtils;

/**
 * Copies copies any failure and warn status messages from a remote
 * to a specified directory. 
 * 
 * @author Nick Collier
 */
public class LocalStatusCopier {
  
  public void run(LocalSession session, File instanceParentDirectory, String outDir) throws StatusException {
    try {
      List<File> filesToCopy = new ArrayList<File>();
      for (int i = 1; i <= session.getInstances(); i++) {
        RunningStatus status = session.getStatus(i);
        if (status != RunningStatus.OK) {
          File dir = new File(instanceParentDirectory,  BatchConstants.INSTANCE_DIR_PREFIX + i);
          for (String fname : dir.list()) {
            if (fname.startsWith(RunningStatus.FAILURE.toString()) || fname.startsWith(RunningStatus.WARN.toString())) {
              filesToCopy.add(new File(dir, fname));
            }
          }
        }
      }
      
      for (File file : filesToCopy) {
        String fname = file.getName();
        String status = fname.contains(RunningStatus.FAILURE.toString()) ? "_failure" : "_warn";
        int instance = Integer.parseInt(fname.substring(fname.indexOf("_") + 1, fname.length()));
        File newFile = new File(outDir, "localhost_" + instance + status + ".txt");
        FileUtils.copyFile(file, newFile);
      }

    } catch (IOException e) {
      String msg = String.format("Error copying status files to %s", outDir);
      throw new StatusException(msg, e);
    } 
  }

}
