/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @author Nick Collier
 */
public class ValidateDiagramAction extends AbstractToolbarAction  {
  
  private static String ID = "repast.simphony.diagram.ValidateCodeAction";
 
  public ValidateDiagramAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
    setText("Check Syntax");
    setId(ID);
    setToolTipText("Check Model Syntax");
    setImageDescriptor(SystemdynamicsDiagramEditorPlugin.getBundledImageDescriptor("icons/obj16/complete_task.gif"));
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {
    System.out.println("checking syntax");
  }
}

 
