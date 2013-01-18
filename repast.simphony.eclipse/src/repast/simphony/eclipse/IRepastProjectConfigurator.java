/**
 * 
 */
package repast.simphony.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * Interface for classes that can configure a project, adding and removing natures for example.
 * This is used by plugins to add / remove natures etc. from repast projects. 
 * 
 * @author Nick Collier
 */
public interface IRepastProjectConfigurator {
  
  /**
   * Configures the specified project.
   * 
   * @param project
   */
  void configureProject(IProject project) throws CoreException;
  
  /**
   * Removes configuration from the specified project.
   * 
   * @param project
   */
  void deconfigureProject(IProject project) throws CoreException;

}
