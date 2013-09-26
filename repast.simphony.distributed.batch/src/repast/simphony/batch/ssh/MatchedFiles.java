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
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of files that match some specified file pattern.
 * 
 * @see OutputFinder
 * 
 * @author Nick Collier
 */
public class MatchedFiles {
  
  private String pattern;
  private PathMatcher matcher;
  private String outputFile;
  private List<File> files = new ArrayList<>();
  
  public MatchedFiles(String pattern, String outputFile) {
    this.pattern = pattern;
    matcher = FileSystems.getDefault().getPathMatcher(pattern);
    this.outputFile = outputFile;
  }
  
  /**
   * Gets whether or not this MatchedFiles collection has
   * any files.
   * 
   * @return true if this has no files, otherwise false.
   */
  public boolean isEmpty() {
    return files.isEmpty();
  }
  
  /**
   * Gets the file pattern associated with this MatchedFiles.
   * 
   * @return
   */
  public String getPattern() {
    return pattern;
  }
  
  /**
   * Gets the output file in which to aggregate all the files
   * in this MatchedFiles.
   * 
   * @return the output file in which to aggregate all the files
   * in this MatchedFiles.
   */
  public String getOutputFile() {
    return outputFile;
  }
  
  /**
   * Gets whether or not the path matches this
   * MatchedFiles pattern.
   * 
   * @param path
   * @return true if there is a match otherwise false.
   */
  public boolean matches(Path path) {
    return matcher.matches(path);
//    System.out.println("matching: \n\t" + path + "\n\t" + pattern);
//    if (matcher.matches(path)) {
//      System.out.println("matched: \n\t" + path + "\n\t" + pattern);
//      return true;
//    }
//    return false;
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

  /**
   * Aggregates all the files into this MatchedFiles output file
   * written to the specified directory. 
   * 
   * @param outputDir
   */
  public void aggregateOutput(String outputDir) throws IOException {
    File f = new File(outputDir, outputFile);
    if (!f.getParentFile().exists()) {
      f.getParentFile().mkdirs();
    }
    try (BufferedWriter out = new BufferedWriter(new FileWriter(f))) {
      boolean skip = false;
      for (File file : files) {
        process(out, file, skip);
        skip = true;
      }
    }
  }
}
