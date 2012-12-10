/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;

/**
 * Base class for classes that find simphony model output.
 * 
 * @author Nick Collier
 */
public abstract class OutputFinder {

  protected static class Instance {
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

  protected static Logger logger = Logger.getLogger(RemoteOutputFinderCopier.class);

  /**
   * Looks through the list of String filenames for the one that ends with the
   * param map suffix. Then finds the output that matches that and adds both
   * those files to the instance parameter.
   * 
   * @param allFiles
   * @param instance
   */
  protected void findFiles(List<String> allFiles, Instance instance) {
    // find the batch parameter map file
    List<String> paramMapFiles = new ArrayList<String>();
    for (String file : allFiles) {
      if (file.contains(BatchConstants.PARAM_MAP_SUFFIX)) {
        paramMapFiles.add(file);
      }
    }

    // got the batch_param file, find the matching output file
    if (paramMapFiles.isEmpty())
      logger.warn("No model output found in " + instance.getDirectory());
    else {
      for (String file : allFiles) {
        for (String batchParamFile : paramMapFiles) {
          int index = batchParamFile.indexOf(BatchConstants.PARAM_MAP_SUFFIX);
          String matchFile = batchParamFile.substring(0, index - 1);
          if (file.startsWith(matchFile)) {
            instance.addFile(new File(instance.getDirectory(), file));
          }
        }
      }
    }
  }
}
