/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Cleans out a tree of directories.
 * 
 * @author Nick Collier
 */
public class DirectoryCleaner {

  private static Set<String> excludes = new HashSet<String>();
  static {
    excludes.add("CVS");
    excludes.add(".cvsignore");
    excludes.add(".svn");
    excludes.add(".gitignore");
  }

  public void run(String root) {
    File f = new File(root);
    delete(f);
  }

  private void delete(File file) {
    if (!excludes.contains(file.getName())) {
      if (file.isDirectory()) {
        for (File child : file.listFiles()) {
          delete(child);
          if (file.list().length == 0) {
            file.delete();
          }
        }
      } else {
        // file is not a directory
        file.delete();
      }
    }
  }
}
