/**
 * 
 */
package repast.simphony.systemdynamics.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

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

  public void run(String root, String uuid) {
    File f = new File(root);
    process(f, uuid);
  }

  private IFile getIFile(File file) {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IPath location = Path.fromOSString(file.getAbsolutePath());
    return workspace.getRoot().getFileForLocation(location);
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
        IJavaElement element = JavaCore.create(ifile);
        if (element != null && element.getElementType() == IJavaElement.COMPILATION_UNIT) {
          try {
            if (((ICompilationUnit) element).getTypes() != null
                && ((ICompilationUnit) element).getTypes().length > 0) {
              IType type = ((ICompilationUnit) element).getTypes()[0];
              // TODO change this annotation!
              IAnnotation ann = type.getAnnotation("GeneratedFor");
              if (ann != null && ann.getMemberValuePairs()[0].getValue().equals(uuid)) {
                element = null;
                ifile.delete(true, null);
              }
            }
          } catch (JavaModelException e) {
            // just ignore any errors and continue.
          } catch (CoreException ex) {
            // just ignore and continue
          }
        }
      }
    }
  }
}
