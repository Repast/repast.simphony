/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

  private String match;
  private String svgPath;

  /**
   * Cleans the directories starting at root of all the java and groovy with 
   * the specified uuid and any svg files that match svgPath
   * 
   * @param root
   * @param svgPath
   * @param uuid
   */
  public void run(String root, String svgPath, String uuid) {
    this.match = "@GeneratedFor(\"" + uuid + "\")";
    this.svgPath = svgPath;
    File f = new File(root);
    process(f, uuid);
  }

  private IFile getIFile(File file) {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IPath location = Path.fromOSString(file.getAbsolutePath());
    return workspace.getRoot().getFileForLocation(location);
  }

  private boolean doDelete(File file, String uuid) {
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = in.readLine()) != null) {
        if (line.contains(match))
          return true;
      }
    } catch (IOException ex) {

    } finally {
      try {
        if (in != null)
          in.close();
      } catch (IOException e) {
      }
    }

    return false;
  }

  private void process(File file, String uuid) {
    if (!excludes.contains(file.getName())) {
      if (file.isDirectory()) {
        for (File child : file.listFiles()) {
          process(child, uuid);
          if (file.list().length == 0) {
            file.delete();
          }
        }
      } else {
        IFile ifile = getIFile(file);
        if (ifile.getFileExtension().equals("java") || ifile.getFileExtension().equals("groovy")) {
          if (doDelete(file, uuid))
            try {
              ifile.delete(true, null);
            } catch (CoreException e) {
            }
        } else if (ifile.getProjectRelativePath().toString().equals(svgPath)) {
          try {
            ifile.delete(true, null);
          } catch (CoreException e) {
          }
        }
      }
    }
  }
}
