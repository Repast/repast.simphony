/**
 * 
 */
package repast.simphony.agents.designer.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import repast.simphony.eclipse.IRepastProjectConfigurator;
import repast.simphony.eclipse.util.Utilities;

/**
 * @author Nick Collier
 */
public class FlowchartProjectConfigurator implements IRepastProjectConfigurator {

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.eclipse.IRepastProjectConfigurator#configureProject(org
   * .eclipse.core.resources.IProject)
   */
  @Override
  public void configureProject(IProject project) throws CoreException {
    Utilities.addNature(project, AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.eclipse.IRepastProjectConfigurator#deconfigureProject(org
   * .eclipse.core.resources.IProject)
   */
  @Override
  public void deconfigureProject(IProject project)  throws CoreException {
    Utilities.removeNature(project, AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID);
  }
}
