/**
 * 
 */
package repast.simphony.agents.designer.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import repast.simphony.eclipse.IRepastProjectConfigurator;

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
    IProjectDescription description = project.getProject().getDescription();
    String[] prevNatures = description.getNatureIds();
    String[] newNatures = new String[prevNatures.length + 1];
    System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
    newNatures[prevNatures.length] = AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID;
    description.setNatureIds(newNatures);
    project.setDescription(description, IResource.FORCE, null);
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
    System.out.println("deconfigure");

  }

}
