package repast.simphony.eclipse.ide;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.actions.AbstractOpenWizardAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class ImportSampleModelsAction extends AbstractOpenWizardAction implements
    IWorkbenchWindowActionDelegate {

  // private IJavaElement fCreatedElement;

  /*
   * public ImportSampleModelsAction() { super(); fCreatedElement= null; }
   */

  protected boolean doCreateProjectFirstOnEmptyWorkspace(Shell shell) {
    return true;
  }

  @Override
  public void run(IAction action) {

    try {
      INewWizard wizard = createWizard();
      wizard.init(PlatformUI.getWorkbench(), getSelection());
    } catch (CoreException e) {
      Shell shell = getShell();
      String title = NewWizardMessages.AbstractOpenWizardAction_createerror_title;
      String message = NewWizardMessages.AbstractOpenWizardAction_createerror_message;
      ExceptionHandler.handle(e, shell, title, message);
    }

  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      setSelection((IStructuredSelection) selection);
    } else {
      setSelection(StructuredSelection.EMPTY);
    }

  }

  @Override
  public void dispose() {
  }

  @Override
  public void init(IWorkbenchWindow window) {
    setShell(window.getShell());

  }

  @Override
  protected INewWizard createWizard() throws CoreException {
    return new ImportSampleModelsWizard();
  }

  // public IJavaElement getCreatedElement() {
  // return fCreatedElement;
  // }
}
