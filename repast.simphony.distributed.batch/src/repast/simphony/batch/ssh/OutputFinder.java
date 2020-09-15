/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Base class for classes that find simphony model output. This works with
 * String rather than files because the remote ssh files are gathered as lists
 * of strings.
 * 
 * @author Nick Collier
 */
public abstract class OutputFinder {

  private List<OutputPattern> filePatterns = new ArrayList<OutputPattern>();

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

  protected static Logger logger = Logger.getLogger(OutputFinder.class);

  /**
   * Adds the specified pattern to the list of patterns used to find output. 
   * 
   * @param outputFileName
   *          the name into which all the output the matches the specified
   *          pattern is aggregated.
   * @param pattern
   *          the pattern to match
   */
  public void addPattern(OutputPattern pattern) {
    // patterns may be copied between OutputPatterns 
    // or patterns maybe reused. In that case, we don't
    // want to reprocess them.
    if (!pattern.getPattern().startsWith("glob:")) {
      pattern.setPattern(pattern.getPattern().replace("./", ""));
      pattern.setPattern(pattern.getPattern().replace(".\\", ""));
      pattern.setPattern("glob:" + pattern.getPattern());
    }
    filePatterns.add(pattern);
  }

  /**
   * Adds all the OutputPatterns in the collection as patterns to find.
   * 
   * @param patterns
   *          the patterns to add
   */
  public void addPatterns(Collection<OutputPattern> patterns) {
    for (OutputPattern pattern : patterns) {
      addPattern(pattern);
    }
  }

  protected List<MatchedFiles> createMatches(boolean useWindowsSeparators) {
    List<MatchedFiles> list = new ArrayList<>();
    // sort so the longer patterns are first -- in this way we should
    // match ModelOutput2 with ModelOutput2* rather than ModelOutput*
    Collections.sort(filePatterns, new Comparator<OutputPattern>() {
      @Override
      public int compare(OutputPattern o1, OutputPattern o2) {
        String p1 = o1.getPattern();
        String p2 = o2.getPattern();
        return p1.length() > p2.length() ? -1 : p1.length() == p2.length() ? 0 : 1;
      }
    });

    for (OutputPattern outPattern : filePatterns) {
      String pattern = outPattern.getPattern();
      if (useWindowsSeparators) {
        outPattern.setPattern(pattern.replace("/", "\\\\"));
      }
      list.add(new MatchedFiles(outPattern));
    }
    return list;
  }

  /**
   * Looks through the list of String filenames for the one that ends with the
   * param map suffix. Then finds the output that matches that and adds both
   * those files to the instance parameter.
   * 
   * @param allFiles
   * @param instance
   */
  protected void findFiles(List<MatchedFiles> matchers, List<String> allFiles, String instanceDir) {

    for (String file : allFiles) {
      for (MatchedFiles matcher : matchers) {
    	  // System.out.println("Attempting to match " + file);
        if (matcher.matches(new File(file).toPath())) {
          matcher.addFile(new File(instanceDir, file));
          // System.out.println("Matcher matched " + file);
          break;
        } 
      }
    }

    for (MatchedFiles matcher : matchers) {
      if (matcher.isEmpty()) {
        logger.warn("No model output found matching " + matcher.getPattern().getPattern() + " in "
            + instanceDir);
      }
    }

  }
}
