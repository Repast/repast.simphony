/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

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

 
  private ToDeleteFilter filter;
  
  public DirectoryCleaner(ToDeleteFilter filter) {
    this.filter = filter;
  }

  /**
   * Cleans the directories starting at root of all the java and groovy with 
   * the specified uuid and any svg files that match svgPath
   * 
   * @param root
   * @param svgPath
   * @param uuid
   */
  public void run(String root) {
    File f = new File(root);
    process(f);
  }

  private IFile getIFile(File file) {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IPath location = Path.fromOSString(file.getAbsolutePath());
    return workspace.getRoot().getFileForLocation(location);
  }

  private void process(File file) {
    if (!excludes.contains(file.getName())) {
      if (file.isDirectory()) {
        for (File child : file.listFiles()) {
          process(child);
          if (file.list().length == 0 && !file.getName().equals(CodeGenerator.SRC_GEN)) {
            file.delete();
          }
        }
      } else {
        IFile ifile = getIFile(file);
        if (filter.delete(ifile)) {
          try {
            ifile.delete(true, null);
          } catch (CoreException e) {
          }
        }
      }
    }
  }
}
