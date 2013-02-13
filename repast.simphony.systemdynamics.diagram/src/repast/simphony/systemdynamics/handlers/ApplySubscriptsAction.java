/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.sdmodel.Cloud;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * @author Nick Collier
 */
public class ApplySubscriptsAction extends AbstractActionHandler {

  private static String ID = "repast.simphony.diagram.ApplySubscriptsAction";
  private List<Variable> variables = new ArrayList<Variable>();

  public ApplySubscriptsAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
    setText("Apply Subscripts");
    setId(ID);
    setToolTipText("Apply Subscripts");
    setImageDescriptor(SystemdynamicsDiagramEditorPlugin
        .getBundledImageDescriptor("icons/obj16/build_tab.gif"));
  }

  // needs to true so that refresh is called when selections changes.
  public boolean isSelectionListener() {
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void refresh() {
    if (getWorkbenchPage().getActiveEditor() != null) {
      variables.clear();
      IStructuredSelection selection = getStructuredSelection();
      if (selection.size() > 0 && selection.getFirstElement() instanceof EditPart) {
        for (Iterator<Object> iter = selection.iterator(); iter.hasNext();) {
          Object obj = iter.next();
          if (obj instanceof EditPart) {
            Object model = ((View) ((EditPart) obj).getModel()).getElement();
            if (model instanceof Variable && !(model instanceof Cloud)) {
              variables.add((Variable) model);
            }
          }
        }
      }

      boolean enabled = false;
      if (variables.size() > 0) {
        SystemModel model = (SystemModel) variables.get(0).eContainer();
        enabled = model.getSubscripts().size() > 0;
      }
      setEnabled(enabled);
    }
  }

  private List<Subscript> lookupSubscripts(SystemModel model, List<String> names) {
    List<Subscript> subscripts = new ArrayList<Subscript>();
    List<Subscript> modelSubs = model.getSubscripts();
    for (String name : names) {
      for (Subscript sub : modelSubs) {
        if (sub.getName().equals(name)) subscripts.add(sub);
      }
    }
    
    return subscripts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org
   * .eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {
    ApplySubscriptsDialog dialog = new ApplySubscriptsDialog(getWorkbenchPage()
        .getWorkbenchWindow().getShell());
    SystemModel model = (SystemModel) variables.get(0).eContainer();
    dialog.init(model.getSubscripts());

    int ret = dialog.open();
    if (ret == IDialogConstants.OK_ID) {
      List<String> subscripts = dialog.getSubscripts();
      SubscriptApplier applier = new SubscriptApplier(lookupSubscripts(model, subscripts),
          variables);
      applier.run();
    }
  }
}
