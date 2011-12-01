package repast.simphony.relogo.ide;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.actions.AbstractOpenWizardAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import repast.simphony.relogo.ide.wizards.NewPatchWizard;

public class NewPatchWizardAction extends AbstractOpenWizardAction implements
		IWorkbenchWindowActionDelegate {

	@Override
	protected INewWizard createWizard() throws CoreException {
		return new NewPatchWizard();
	}
	
	@Override
	protected boolean doCreateProjectFirstOnEmptyWorkspace(Shell shell) {
		return true;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		setShell(window.getShell());

	}

	@Override
	public void run(IAction action) {
		super.run();

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			setSelection((IStructuredSelection) selection);
		} else {
			setSelection(StructuredSelection.EMPTY);
		}

	}

}
