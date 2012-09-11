/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import repast.simphony.batch.BatchConstants;

/**
 * Copies the output from remote ssh runs into a local directory.
 * 
 * @author Nick Collier
 */
public class LocalOutputFinder extends OutputFinder {

  private void findOutputFiles(List<Instance> instances) {

    for (Instance instance : instances) {
      List<String> files = Arrays.asList(new File(instance.getDirectory()).list());
      findFiles(files, instance);
    }
  }

  public List<File> run(File instanceParentDirectory) throws StatusException {
    List<File> out = new ArrayList<File>();

    List<Instance> instances = new ArrayList<Instance>();

    for (File dir : instanceParentDirectory.listFiles()) {
      if (dir.getName().contains(BatchConstants.INSTANCE_DIR_PREFIX)) {
        instances.add(new Instance(dir.getAbsolutePath()));
      }
    }

    findOutputFiles(instances);
    // instance dirs should now contain the output file
    for (Instance instance : instances) {
      out.addAll(instance.getFiles());
    }

    return out;

  }
}
