/**
 * 
 */
package repast.simphony.statecharts.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import repast.simphony.eclipse.IRepastProjectConfigurator;
import repast.simphony.eclipse.util.Utilities;

/**
 * Project configurator for statecharts plugin.
 * 
 * @author Nick Collier
 */
public class StatechartConfigurator implements IRepastProjectConfigurator {

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastProjectConfigurator#configureProject(org.eclipse.core.resources.IProject)
   */
  @Override
  public void configureProject(IProject project) throws CoreException {
    Utilities.addNature(project, StatechartNature.STATECHART_NATURE_ID);
  }

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastProjectConfigurator#deconfigureProject(org.eclipse.core.resources.IProject)
   */
  @Override
  public void deconfigureProject(IProject project) throws CoreException {
    Utilities.removeNature(project, StatechartNature.STATECHART_NATURE_ID);
  }
}
