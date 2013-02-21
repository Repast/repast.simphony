package repast.simphony.systemdynamics.diagram.part;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

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
   * @generated NOT
   * 
   */
  private IResource extractSelection() {
    if (!(selection instanceof IStructuredSelection))
      return null;
    IStructuredSelection ss = (IStructuredSelection) selection;
    Object element = ss.getFirstElement();
    if (element instanceof IResource)
      return (IResource) element;
    if (!(element instanceof IAdaptable))
      return null;
    IAdaptable adaptable = (IAdaptable) element;
    Object adapter = adaptable.getAdapter(IResource.class);
    return (IResource) adapter;
  }

  /**
   * @generated NOT
   */
  public void addPages() {
    IResource resource = extractSelection();
    IProject project = null;
    if (resource != null) {
      project = resource.getProject();
    }

    diagramModelFilePage = new SystemdynamicsCreationWizardPage(
        "DiagramModelFile", project, getSelection(), "rsd"); //$NON-NLS-1$ //$NON-NLS-2$
    diagramModelFilePage.setTitle(Messages.SystemdynamicsCreationWizard_DiagramModelFilePageTitle);
    diagramModelFilePage
        .setDescription(Messages.SystemdynamicsCreationWizard_DiagramModelFilePageDescription);
    addPage(diagramModelFilePage);
  }

  /**
   * @generated NOT
   */
  public boolean performFinish() {
    IRunnableWithProgress op = new OnFinish();
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
  
  private class OnFinish extends WorkspaceModifyOperation {
    
    protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
      diagram = SystemdynamicsDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(),
          monitor);
      initModel();
      if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
        try {
          SystemdynamicsDiagramEditorUtil.openDiagram(diagram);
        } catch (PartInitException e) {
          ErrorDialog.openError(getContainer().getShell(),
              Messages.SystemdynamicsCreationWizardOpenEditorError, null, e.getStatus());
        }
      }
    }
    
    private void initModel() {
      if (diagram != null) {
        SystemModel model = null;
        for (EObject obj : diagram.getContents()) {
          if (obj.eClass().equals(SDModelPackage.Literals.SYSTEM_MODEL)) {
            model = (SystemModel)obj;
            break;
          }
        }
        
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
        Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(model,
            SDModelPackage.Literals.SYSTEM_MODEL__CLASS_NAME, diagramModelFilePage.getClassName()));
        domain.getCommandStack().execute(cmd);
        
        cmd = domain.createCommand(SetCommand.class, new CommandParameter(model,
            SDModelPackage.Literals.SYSTEM_MODEL__PACKAGE, diagramModelFilePage.getPackage()));
        domain.getCommandStack().execute(cmd);
        
        try {
          diagram.save(SystemdynamicsDiagramEditorUtil.getSaveOptions());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
  }
}
