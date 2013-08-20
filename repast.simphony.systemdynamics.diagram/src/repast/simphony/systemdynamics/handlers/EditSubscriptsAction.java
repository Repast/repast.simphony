/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;

/**
 * @author Nick Collier
 */
public class EditSubscriptsAction extends AbstractToolbarAction  {
  
  private static String ID = "repast.simphony.diagram.EditSubscriptsAction";
  
  public EditSubscriptsAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
    setText("Edit Subscripts");
    setId(ID);
    setToolTipText("Edit Subscripts");
    setImageDescriptor(SystemdynamicsDiagramEditorPlugin.getBundledImageDescriptor("icons/obj16/th_horizontal.gif"));
  }


  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {
    EditSubscriptsDialog dialog = new EditSubscriptsDialog(getWorkbenchPage().getWorkbenchWindow().getShell(),
        model);
    int ret = dialog.open();
    if (ret == IDialogConstants.OK_ID) {
      TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
      Command cmd = domain.createCommand(RemoveCommand.class, new CommandParameter(model,
          SDModelPackage.Literals.SYSTEM_MODEL__SUBSCRIPTS, model.getSubscripts()));
      domain.getCommandStack().execute(cmd);
      cmd = domain.createCommand(AddCommand.class, new CommandParameter(model,
          SDModelPackage.Literals.SYSTEM_MODEL__SUBSCRIPTS, dialog.getSubscripts()));
      domain.getCommandStack().execute(cmd);
    }
  }
}

 
