package repast.simphony.statecharts.part;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.ui.IWorkbenchPart;

public class ShowPropsViewAction extends AbstractActionHandler {
  
  private IGraphicalEditPart part;

  public ShowPropsViewAction(IWorkbenchPart workbenchPart) {
    super(workbenchPart);
    setId("repast.simphony.statecharts.show_props_action");
    setText(DiagramUIMessages.ShowPropertiesViewAction_ActionLabelText);
    setToolTipText(DiagramUIMessages.ShowPropertiesViewAction_ActionToolTipText);
    setImageDescriptor(DiagramUIPluginImages.DESC_SHOW_PROPERTIES_VIEW);
    setHoverImageDescriptor(DiagramUIPluginImages.DESC_SHOW_PROPERTIES_VIEW);
  }

  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.diagram.ui.actions.ShowPropertiesViewAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {
    if (part != null) {
      part.getViewer().deselect(part);
      WorkbenchPartActivator.showPropertySheet();
      part.getViewer().select(part);
    } else {
      WorkbenchPartActivator.showPropertySheet();
    }
  }
  
  public void refresh() {
    Object selection = getStructuredSelection().getFirstElement();
    if (selection != null && selection instanceof IGraphicalEditPart) {
      part = (IGraphicalEditPart)selection;
    } else {
      part = null;
    }
  }

  @Override
  protected boolean isSelectionListener() {
    return true;
  }
}
