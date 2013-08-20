/**
 * 
 */
package repast.simphony.systemdynamics;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import repast.simphony.eclipse.IRepastProjectConfigurator;
import repast.simphony.eclipse.util.Utilities;

/**
 * Project configurator for system dynamics. This adds the system dynamics natures to
 * repast projects.
 * 
 * @author Nick Collier
 */
public class ProjectConfigurator implements IRepastProjectConfigurator {

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastProjectConfigurator#configureProject(org.eclipse.core.resources.IProject)
   */
  @Override
  public void configureProject(IProject project) throws CoreException {
    Utilities.addNature(project, SystemDynamicsProjectNature.NATURE_ID);
  }

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastProjectConfigurator#deconfigureProject(org.eclipse.core.resources.IProject)
   */
  @Override
  public void deconfigureProject(IProject project) throws CoreException {
    Utilities.removeNature(project, SystemDynamicsProjectNature.NATURE_ID);
  }
}
