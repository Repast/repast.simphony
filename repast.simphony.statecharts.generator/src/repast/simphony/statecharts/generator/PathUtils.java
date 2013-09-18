/**
 * 
 */
package repast.simphony.statecharts.generator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author Nick Collier
 */
public class PathUtils {

  /**
   * Creates the specified source path in the specified project. The path will
   * be created and added to the project's build path.
   * 
   * @param project
   *          the project to add the path to
   * @param srcPath
   *          the name of the path
   * @return the created path.
   * @throws CoreException 
   */
  public static IPath createSrcPath(IProject project, String srcPath, IProgressMonitor monitor) throws CoreException {
    IJavaProject javaProject = JavaCore.create(project);

    // workspace relative
    IPath path = javaProject.getPath().append(srcPath + "/");
    // project relative
    IFolder folder = project.getFolder(srcPath);

    if (!folder.exists()) {
      // creates within the project
      folder.create(true, true, monitor);
      IClasspathEntry[] entries = javaProject.getRawClasspath();
      boolean found = false;
      for (IClasspathEntry entry : entries) {
        if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE && entry.getPath().equals(srcPath)) {
          found = true;
          break;
        }
      }

      if (!found) {
        IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
        System.arraycopy(entries, 0, newEntries, 0, entries.length);
        IClasspathEntry srcEntry = JavaCore.newSourceEntry(path, null);
        newEntries[entries.length] = srcEntry;
        javaProject.setRawClasspath(newEntries, null);
      }
    }
    return path;
  }

}
