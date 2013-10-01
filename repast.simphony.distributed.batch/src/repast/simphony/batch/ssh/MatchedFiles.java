/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of files that match some specified file pattern.
 * 
 * @see OutputFinder
 * 
 * @author Nick Collier
 */
public class MatchedFiles {

  private OutputPattern pattern;
  private PathMatcher matcher;
  private List<File> files = new ArrayList<>();

  public MatchedFiles(OutputPattern pattern) {
    this.pattern = pattern;
    matcher = FileSystems.getDefault().getPathMatcher(pattern.getPattern());
  }

  /**
   * Gets whether or not this MatchedFiles collection has any files.
   * 
   * @return true if this has no files, otherwise false.
   */
  public boolean isEmpty() {
    return files.isEmpty();
  }

  /**
   * Gets the OutputPattern associated with this MatchedFiles.
   * 
   * @return
   */
  public OutputPattern getPattern() {
    return pattern;
  }

  /**
   * Gets whether or not the path matches this MatchedFiles pattern.
   * 
   * @param path
   * @return true if there is a match otherwise false.
   */
  public boolean matches(Path path) {
    return matcher.matches(path);
    // System.out.println("matching: \n\t" + path + "\n\t" + pattern);
    // if (matcher.matches(path)) {
    // System.out.println("matched: \n\t" + path + "\n\t" + pattern);
    // return true;
    // }
    // return false;
  }

  /**
   * Adds the specified file as a match.
   * 
   * @param file
   */
  public void addFile(File file) {
    files.add(file);
  }

  /**
   * Gets a List of the matched files.
   * 
   * @return a List of the matched files.
   */
  public List<File> getFiles() {
    return new ArrayList<File>(files);
  }

  /**
   * Adds all the files as matched files.
   * 
   * @param files
   */
  public void addAllFiles(Collection<File> files) {
    this.files.addAll(files);
  }

  private void process(BufferedWriter out, File file, boolean skip) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = null;
      if (skip)
        reader.readLine();
      while ((line = reader.readLine()) != null) {
        out.write(line);
        out.write("\n");
      }
    }
  }
  
  private void renamePath(Path path) throws IOException {
    if (path.toFile().exists()) {
      // rename the output directory appending the current time stamp.
      String suffix = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss").format(new Date());
      Files.move(path,
          path.resolveSibling(path.getFileName().toFile().getName() + "_" + suffix));
    }
  }

  private void copyFiles(String outputDir) throws IOException {
    Path outDir = FileSystems.getDefault().getPath(outputDir, pattern.getPath());
    renamePath(outDir);
    outDir.toFile().mkdirs();

    Map<String, Integer> suffixMap = new HashMap<>();
    for (File file : files) {
      String name = file.getName();
      Integer count = suffixMap.get(name);
      if (count == null) {
        count = new Integer(0);
      }
      suffixMap.put(name, count.intValue() + 1);
      String suffix = "_" + (count.intValue() + 1);
      int index = name.lastIndexOf(".");
      if (index == -1) {
        name = name + suffix;
      } else {
        name = name.substring(0, index) + suffix + name.substring(index);
      }
      Path target = new File(outDir.toFile(), name).toPath();
      Files.copy(file.toPath(), target);
    }
  }

  /**
   * Aggregates all the files into this MatchedFiles output file written to the
   * specified directory.
   * 
   * @param outputDir
   */
  public void aggregateOutput(String outputDir) throws IOException {
    if (pattern.isConcatenate()) {
      File f = new File(outputDir, pattern.getPath());
      renamePath(f.toPath());
      if (!f.getParentFile().exists()) {
        f.getParentFile().mkdirs();
      }

      try (BufferedWriter out = new BufferedWriter(new FileWriter(f))) {
        boolean skip = false;
        for (File file : files) {
          process(out, file, skip);
          skip = pattern.isHeader();
        }
      }
    } else {
      // copy all the files to the output path
      copyFiles(outputDir);

    }
  }
}
