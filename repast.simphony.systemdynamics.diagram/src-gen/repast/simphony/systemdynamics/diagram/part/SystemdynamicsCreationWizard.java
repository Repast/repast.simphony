package repast.simphony.systemdynamics.diagram.part;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @generated
 */
public class SystemdynamicsCreationWizard extends Wizard implements INewWizard {

  /**
   * @generated
   */
  private IWorkbench workbench;

  /**
   * @generated
   */
  protected IStructuredSelection selection;

  /**
   * @generated
   */
  protected SystemdynamicsCreationWizardPage diagramModelFilePage;

  /**
   * @generated
   */
  protected Resource diagram;

  /**
   * @generated
   */
  private boolean openNewlyCreatedDiagramEditor = true;

  /**
   * @generated
   */
  public IWorkbench getWorkbench() {
    return workbench;
  }

  /**
   * @generated
   */
  public IStructuredSelection getSelection() {
    return selection;
  }

  /**
   * @generated
   */
  public final Resource getDiagram() {
    return diagram;
  }

  /**
   * @generated
   */
  public final boolean isOpenNewlyCreatedDiagramEditor() {
    return openNewlyCreatedDiagramEditor;
  }

  /**
   * @generated
   */
  public void setOpenNewlyCreatedDiagramEditor(boolean openNewlyCreatedDiagramEditor) {
    this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
  }

  /**
   * @generated
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
    this.selection = selection;
    setWindowTitle(Messages.SystemdynamicsCreationWizardTitle);
    setDefaultPageImageDescriptor(SystemdynamicsDiagramEditorPlugin
        .getBundledImageDescriptor("icons/wizban/NewSDModelWizard.gif")); //$NON-NLS-1$
    setNeedsProgressMonitor(true);
  }

  /**
   * @generated
   */
  public void addPages() {
    diagramModelFilePage = new SystemdynamicsCreationWizardPage(
        "DiagramModelFile", getSelection(), "rsd"); //$NON-NLS-1$ //$NON-NLS-2$
    diagramModelFilePage.setTitle(Messages.SystemdynamicsCreationWizard_DiagramModelFilePageTitle);
    diagramModelFilePage
        .setDescription(Messages.SystemdynamicsCreationWizard_DiagramModelFilePageDescription);
    addPage(diagramModelFilePage);
  }

  /**
   * @generated
   */
  public boolean performFinish() {
    IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

      protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
        diagram = SystemdynamicsDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(),
            monitor);
        if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
          try {
            SystemdynamicsDiagramEditorUtil.openDiagram(diagram);
          } catch (PartInitException e) {
            ErrorDialog.openError(getContainer().getShell(),
                Messages.SystemdynamicsCreationWizardOpenEditorError, null, e.getStatus());
          }
        }
      }
    };
    try {
      getContainer().run(false, true, op);
    } catch (InterruptedException e) {
      return false;
    } catch (InvocationTargetException e) {
      if (e.getTargetException() instanceof CoreException) {
        ErrorDialog.openError(getContainer().getShell(),
            Messages.SystemdynamicsCreationWizardCreationError, null,
            ((CoreException) e.getTargetException()).getStatus());
      } else {
        SystemdynamicsDiagramEditorPlugin.getInstance().logError(
            "Error creating diagram", e.getTargetException()); //$NON-NLS-1$
      }
      return false;
    }
    return diagram != null;
  }
}
