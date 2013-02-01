package repast.simphony.systemdynamics;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * Project nature for system dynamics. This adds the 
 * system dynamics builder to the project.
 * 
 * @author Nick Collier
 */
public class SystemDynamicsProjectNature implements IProjectNature {
  
  public static final String NATURE_ID = SystemdynamicsDiagramEditorPlugin.ID + ".repast_simphony_systemdynamics_nature";
  
  private IProject project;

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#configure()
   */
  @Override
  public void configure() throws CoreException {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#deconfigure()
   */
  @Override
  public void deconfigure() throws CoreException {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#getProject()
   */
  @Override
  public IProject getProject() {
    return project;
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
   */
  @Override
  public void setProject(IProject project) {
    this.project = project;
  }
}
