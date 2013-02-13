package repast.simphony.systemdynamics.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.handlers.ApplySubscriptsAction;
import repast.simphony.systemdynamics.handlers.EditSubscriptsAction;
import repast.simphony.systemdynamics.handlers.GenerateCodeAction;
import repast.simphony.systemdynamics.handlers.ValidateDiagramAction;

/**
 * @generated
 */
public class SystemdynamicsDiagramActionBarContributor extends DiagramActionBarContributor {

  /**
   * @generated
   */
  protected Class<?> getEditorClass() {
    return SystemdynamicsDiagramEditor.class;
  }

  /**
   * @generated
   */
  protected String getEditorId() {
    return SystemdynamicsDiagramEditor.ID;
  }

  /**
   * @generated NOT
   */
  public void init(IActionBars bars, IWorkbenchPage page) {
    super.init(bars, page);
    // print preview
    IMenuManager fileMenu = bars.getMenuManager().findMenuUsingPath(
        IWorkbenchActionConstants.M_FILE);
    assert fileMenu != null;
    fileMenu.remove("pageSetupAction"); //$NON-NLS-1$

    bars.getToolBarManager().add(new EditSubscriptsAction(page));
    bars.getToolBarManager().add(new ApplySubscriptsAction(page));
    bars.getToolBarManager().add(new GenerateCodeAction(page));
    bars.getToolBarManager().add(new ValidateDiagramAction(page));
  }
}
