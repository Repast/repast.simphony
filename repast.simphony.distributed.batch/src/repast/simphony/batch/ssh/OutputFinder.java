/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

/**
 * Base class for classes that find simphony model output. This works with
 * String rather than files because the remote ssh files are gathered as lists
 * of strings.
 * 
 * @author Nick Collier
 */
public abstract class OutputFinder {

  private List<Pair<String, String>> filePatterns = new ArrayList<Pair<String, String>>();

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
   * Adds the specified pattern to the list of patterns used to find output. All
   * the output that matches this pattern will be aggregated into a file whose
   * name starts with the specified output file name plus a time stamp. The
   * patterns shuld relative to the directory in which the model runs. and
   * specified in glob style.
   * <ul>
   * <li>The * character matches zero or more characters of a name component
   * without crossing directory boundaries.
   * 
   * <li>The ** characters matches zero or more characters crossing directory
   * boundaries.
   * 
   * <li>The ? character matches exactly one character of a name component.
   * 
   * <li>The backslash character (\) is used to escape characters that would
   * otherwise be interpreted as special characters. The expression \\ matches a
   * single backslash and "\{" matches a left brace for example.
   * </ul>
   * 
   * For example **&#47;output/my_output.txt will match any file called
   * my_output.txt in an "output" directory where the parent of that "output"
   * directory can be anything.
   * 
   * @param outputFileName
   *          the name into which all the output the matches the specified
   *          pattern is aggregated.
   * @param pattern
   *          the pattern to match
   */
  public void addPattern(String outputFileName, String pattern) {
    pattern = pattern.replace("./", "");
    pattern = pattern.replace(".\\", "");
    filePatterns.add(Pair.of("glob:" + pattern, outputFileName));
  }

  protected List<MatchedFiles> createMatches(boolean useWindowsSeparators) {
    List<MatchedFiles> list = new ArrayList<>();
    // sort so the longer patterns are first -- in this way we should
    // match ModelOutput2 with ModelOutput2* rather than ModelOutput*
    Collections.sort(filePatterns, new Comparator<Pair<String, String>>() {
      @Override
      public int compare(Pair<String, String> o1, Pair<String, String> o2) {
        return o1.getLeft().length() > o2.getLeft().length() ? -1 : o1.getLeft().length() ==
            o2.getLeft().length() ? 0 : 1;
      }
    });
    
    for (Pair<String, String> pair : filePatterns) {
      String pattern = pair.getLeft();
      if (useWindowsSeparators) {
        pattern = pattern.replace("/", "\\\\");
      }
      list.add(new MatchedFiles(pair.getLeft(), pair.getRight()));
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
  protected void findFiles(List<MatchedFiles> matchers, List<String> allFiles, 
      String instanceDir) {
    
    for (String file : allFiles) {
      for (MatchedFiles matcher : matchers) {
        if (matcher.matches(new File(file).toPath())) {
          matcher.addFile(new File(instanceDir, file));
          break;
        }
      }
    }

    for (MatchedFiles matcher : matchers) {
      if (matcher.isEmpty()) {
        logger.warn("No model output found matching " + matcher.getPattern() + " in "
            + instanceDir);
      }
    }
    
  }
}
