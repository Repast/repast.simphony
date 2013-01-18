/**
 * 
 */
package repast.simphony.eclipse;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Configures a Repast Simphony project by setting the classpath, adding the
 * natures, etc.
 * 
 * @author Nick Collier, Michael North
 */
public class RSProjectConfigurator {

  /**
   * Configures a new project for Repast Simphony. This adds the GroovyRuntime to the project,
   * updates the classpath with the Repast Simphony classpath
   * 
   * @param project
   * @param monitor
   */
  public void configureNewProject(IJavaProject project, IProgressMonitor monitor) throws CoreException {
    
    GroovyRuntime.addGroovyRuntime(project.getProject());
    updateClasspath(project);
   
    // TODO update the natures
//    IProjectDescription description = project.getProject().getDescription();
//    String[] prevNatures = description.getNatureIds();
//    String[] newNatures = new String[prevNatures.length + 1];
//    System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
//    newNatures[prevNatures.length] = AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID;
//    description.setNatureIds(newNatures);
//    project.setDescription(description, IResource.FORCE, monitor);

    project.save(monitor, true);

  }
  
  private void updateClasspath(IJavaProject project) throws JavaModelException {
    IPath jarPath = new Path(RepastProjectClasspathContainer.JAR_CLASSPATH_DEFAULT);
    IClasspathEntry[] oldClassPathEntries = project.getRawClasspath();
    boolean found = false;
    for (int i = 0; i < oldClassPathEntries.length; i++) {
      final IClasspathEntry classPathEntry = oldClassPathEntries[i];
      if (classPathEntry.getPath().lastSegment().equals(jarPath.lastSegment())) {
        found = true;
        break;
      }
    }

    if (!found) {
      final IClasspathEntry[] newClassPathEntries = (IClasspathEntry[]) ArrayUtils.add(
          oldClassPathEntries, JavaCore.newContainerEntry(jarPath));
      project.setRawClasspath(newClassPathEntries, null);
    }
  }

}
