/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditor;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

/**
 * AbstractActionHandler implementation that finds the SystemModel
 * on a refresh. 
 * 
 * @author Nick Collier
 */
public abstract class AbstractToolbarAction extends AbstractActionHandler {
  
  protected AbstractToolbarAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
  }

  protected SystemModel model;

  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress#refresh()
   */
  @Override
  public void refresh() {
    IEditorPart editor = getWorkbenchPage().getActiveEditor();
    model = null;
    if (editor != null && editor instanceof SystemdynamicsDiagramEditor) {
      model = (SystemModel) ((SystemdynamicsDiagramEditor)editor).getDiagram().getElement();
    } 
    setEnabled(model != null);
  }
}
