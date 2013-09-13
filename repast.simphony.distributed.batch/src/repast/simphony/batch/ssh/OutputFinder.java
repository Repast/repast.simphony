/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;

/**
 * Base class for classes that find simphony model output. This works with
 * String rather than files because the remote ssh files are gathered as lists
 * of strings.
 * 
 * @author Nick Collier
 */
public abstract class OutputFinder {

  private static final String FILE_SINK_FILE_PREFIX = "glob:{**/,}";
  private static final String PARAM_MAP_PATTERN = "glob:{**/,}*batch_param_map{.*,}";

  private List<String> filePatterns = new ArrayList<>();

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

  private String cleanMatchFile(String filename) {
    String ret = filename.replace("*", "\\*");
    ret = ret.replace("?", "\\?");
    ret = ret.replace("{", "\\{");
    ret = ret.replace("}", "\\}");
    ret = ret.replace("\\", "\\\\");
    return ret;
  }

  /**
   * Adds the specified pattern to the list of patterns used to find output. Any
   * files that match the pattern will considered as model output. The patterns
   * should all relative to the directory in which the model runs. The patterns
   * should be specified in glob style.
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
   * @param pattern
   */
  public void addPattern(String pattern) {
    filePatterns.add("glob:" + pattern);
  }

  /**
   * Looks through the list of String filenames for the one that ends with the
   * param map suffix. Then finds the output that matches that and adds both
   * those files to the instance parameter.
   * 
   * @param allFiles
   * @param instance
   */
  protected void findFiles(List<String> allFiles, Instance instance, boolean useWindowsSeparators) {
    List<String> fPatterns = new ArrayList<>(this.filePatterns);
    // find the batch parameter map file
    List<String> paramMapFiles = new ArrayList<String>();
    String mapPattern = useWindowsSeparators ? PARAM_MAP_PATTERN.replace("/", "\\\\")
        : PARAM_MAP_PATTERN;
    PathMatcher matcher = FileSystems.getDefault().getPathMatcher(mapPattern);
    for (String file : allFiles) {
      if (matcher.matches(new File(file).toPath())) {
        paramMapFiles.add(file);

        instance.addFile(new File(instance.getDirectory(), file));
        int index = file.indexOf(BatchConstants.PARAM_MAP_SUFFIX);
        String matchFile = file.substring(0, index - 1);
        index = file.lastIndexOf(".");
        String ext = "";
        if (index != -1) {
          ext = file.substring(index, file.length());
        }
        fPatterns.add(FILE_SINK_FILE_PREFIX + cleanMatchFile(matchFile) + ext);
      }
    }

    List<PathMatcher> matchers = new ArrayList<>();
    for (String pattern : fPatterns) {
      if (useWindowsSeparators) {
        matchers.add(FileSystems.getDefault().getPathMatcher(pattern.replace("/", "\\\\")));
      } else {
        matchers.add(FileSystems.getDefault().getPathMatcher(pattern));
      }
    }

    for (String file : allFiles) {
      for (PathMatcher pMatcher : matchers) {
        if (pMatcher.matches(new File(file).toPath())) {
          instance.addFile(new File(instance.getDirectory(), file));
          break;
        }
      }
    }

    if (instance.getFiles().isEmpty()) {
      logger.warn("No model output found in " + instance.getDirectory());
    }
  }
}
