/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.batch.BatchConstants;

/**
 * Finds the output in the local instance directories. Output is identified
 * as those files that have the batch_param_map suffix, their corresponding 
 * output files and those that match user supplied patterns.
 * 
 * @author Nick Collier
 */
public class LocalOutputFinder extends OutputFinder {
  
  private static class FileGatherer extends SimpleFileVisitor<Path> {
    
    private List<String> files = new ArrayList<String>();
    private Path instanceDir;
    
    public FileGatherer(Path instanceDir) {
      this.instanceDir = instanceDir;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      files.add(instanceDir.relativize(file).toString());
      return FileVisitResult.CONTINUE;
    }
  }

  private List<MatchedFiles> findOutputFiles(List<String> instanceDirs) throws StatusException {
    EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
    List<MatchedFiles> matchers = createMatches(SystemUtils.IS_OS_WINDOWS);
    for (String dir : instanceDirs) {
      Path instanceDir = new File(dir).toPath();
      FileGatherer fileGatherer = new FileGatherer(instanceDir);
      try {
        Files.walkFileTree(instanceDir, opts, Integer.MAX_VALUE, fileGatherer);
      } catch (IOException ex) {
        throw new StatusException(ex);
      }
      
      findFiles(matchers, fileGatherer.files, dir);
    }
    return matchers;
  }

  public List<MatchedFiles> run(File instanceParentDirectory) throws StatusException {
    List<String> instances = new ArrayList<String>();
    for (File dir : instanceParentDirectory.listFiles()) {
      if (dir.getName().contains(BatchConstants.INSTANCE_DIR_PREFIX)) {
        instances.add(dir.getAbsolutePath());
      }
    }

    return findOutputFiles(instances);
  }
}
