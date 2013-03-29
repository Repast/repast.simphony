package repast.simphony.statecharts.part;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * @generated
 */
public class StatechartCreationWizard extends Wizard implements INewWizard {

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
  protected StatechartCreationWizardPage diagramModelFilePage;

  /**
   * @generated NOT
   */
  protected StatechartCreationWizardPage0 modelPropsPage;

  /**
   * @generated
   */
  protected Resource diagram;

  /**
   * @generated
   */
  private boolean openNewlyCreatedDiagramEditor = true;

  /**
   * @generated NOT
   */
  private ICompilationUnit agent;

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
    setWindowTitle(Messages.StatechartCreationWizardTitle);
    setDefaultPageImageDescriptor(StatechartDiagramEditorPlugin
        .getBundledImageDescriptor("icons/wizban/NewStatechartWizard.gif")); //$NON-NLS-1$
    setNeedsProgressMonitor(true);
  }

  private IJavaElement getJavaElement() {
    IJavaElement jelem = null;
    if (selection != null && !selection.isEmpty()) {
      Object selectedElement = selection.getFirstElement();
      if (selectedElement instanceof IAdaptable) {
        IAdaptable adaptable = (IAdaptable) selectedElement;
        jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
      }
    }
    return jelem;
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

  private String getFile() {
    IResource resource = extractSelection();
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    String file = "";
    IProject project = null;

    if (resource != null) {
      project = resource.getProject();
    } else {
      IProject[] projects = root.getProjects();
      if (projects != null && projects.length > 0) {
        project = projects[0];
      }
    }

    if (project != null) {
      IPath path = project.getProjectRelativePath().append(
          project.getName() + "/statecharts/statechart.rsc");
      int counter = 1;
      while (root.getFile(path).exists()) {
        path = path.removeLastSegments(1).append("statechart" + counter + ".rsc");
        ++counter;
      }
      file = path.toPortableString();
    }

    return file;
  }

  /**
   * @generated NOT
   */
  public void addPages() {
    // is the selection an
    IResource resource = extractSelection();
    IProject project = null;
    if (resource != null) {
      project = resource.getProject();
    }

    IJavaElement javaE = getJavaElement();
    String agentType = "";
    if (javaE != null && javaE.getElementType() == IJavaElement.COMPILATION_UNIT) {
      agent = (ICompilationUnit) javaE;
      try {
        if (agent.getTypes().length == 0)
          agent = null;
        else {
          IType aType = agent.getTypes()[0];
          agentType = javaE.getParent().getElementName() + "." + aType.getElementName();
        }
      } catch (JavaModelException ex) {
        agent = null;
        // we don't need to worry about this,
        // as it indicates that the .java / .groovy file doesn't
        // contain a valid type, so we can just ignore then.
      }
    }

    modelPropsPage = new StatechartCreationWizardPage0("DiagramModelProps", project, agentType,
        getFile());
    modelPropsPage.setTitle("Statechart Diagram");
    modelPropsPage.setDescription("Create a new Statechart diagram.");
    addPage(modelPropsPage);

    // if the user selected an agent then the wizard should ask the user
    // where to put the statechart.
    /*
    IStructuredSelection selection = agent == null ? getSelection() : new StructuredSelection();
    diagramModelFilePage = new StatechartCreationWizardPage("DiagramModelFile", selection, "rsc"); //$NON-NLS-1$ //$NON-NLS-2$
    diagramModelFilePage.setTitle(Messages.StatechartCreationWizard_DiagramModelFilePageTitle);
    diagramModelFilePage
        .setDescription(Messages.StatechartCreationWizard_DiagramModelFilePageDescription);

    addPage(diagramModelFilePage);
     */
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
            Messages.StatechartCreationWizardCreationError, null,
            ((CoreException) e.getTargetException()).getStatus());
      } else {
        StatechartDiagramEditorPlugin.getInstance().logError(
            "Error creating diagram", e.getTargetException()); //$NON-NLS-1$
      }
      return false;
    }

    return diagram != null;
  }

  private class OnFinish extends WorkspaceModifyOperation {

    public OnFinish() {
      super(null);
    }

    private void initializeStateMachine() {
      StateMachine statemachine = null;
      for (EObject obj : diagram.getContents()) {
        if (obj.eClass().equals(StatechartPackage.Literals.STATE_MACHINE)) {
          statemachine = (StateMachine) obj;
          break;
        }
      }

      if (statemachine != null) {
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(statemachine);
        Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(statemachine,
            StatechartPackage.Literals.STATE_MACHINE__ID, modelPropsPage.getStatechartName()));
        domain.getCommandStack().execute(cmd);

        cmd = domain.createCommand(SetCommand.class, new CommandParameter(statemachine,
            StatechartPackage.Literals.STATE_MACHINE__CLASS_NAME, modelPropsPage.getClassName()));
        domain.getCommandStack().execute(cmd);

        cmd = domain.createCommand(
            SetCommand.class,
            new CommandParameter(statemachine,
                StatechartPackage.Literals.STATE_MACHINE__AGENT_TYPE, modelPropsPage
                    .getAgentClassName()));
        domain.getCommandStack().execute(cmd);

        cmd = domain.createCommand(SetCommand.class, new CommandParameter(statemachine,
            StatechartPackage.Literals.STATE_MACHINE__PACKAGE, modelPropsPage.getPackage()));
        domain.getCommandStack().execute(cmd);

        cmd = domain.createCommand(SetCommand.class, new CommandParameter(statemachine,
            StatechartPackage.Literals.STATE_MACHINE__LANGUAGE, modelPropsPage.getLanguage()));
        domain.getCommandStack().execute(cmd);

        try {
          diagram.save(repast.simphony.statecharts.part.StatechartDiagramEditorUtil
              .getSaveOptions());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    private void processAgent(IProgressMonitor monitor) {
      boolean agentIsOK = false;
      try {
        agentIsOK = agent != null && agent.isStructureKnown();
      } catch (JavaModelException ex) {
        agentIsOK = false;
      }

      if (agentIsOK) {
        try {
          StatechartCodeAdder adder = StatechartCodeAdderFactory.createCodeAdder(agent, monitor);
          adder.run(modelPropsPage.getStatechartName(), modelPropsPage.getPackage(),
              modelPropsPage.getClassName(), modelPropsPage.getAgentClassName());
        } catch (Throwable ex) {
          // ignore any code creation errors as re-throwing the error causes the rest of the 
          // chart creation to abort.
          StatechartDiagramEditorPlugin.getInstance().logError(
              "Error while inserting statechart code into agent", ex);
          //new CoreException(new Status(IStatus., StatechartDiagramEditorPlugin.ID, "Error while inserting statechart code into agent.", 
          //    ex));
        }
      }
    }

    protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
      diagram = StatechartDiagramEditorUtil.createDiagram(modelPropsPage.getURI(), monitor);
      initializeStateMachine();
      if (agent != null)
        processAgent(monitor);

      if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
        try {
          StatechartDiagramEditorUtil.openDiagram(diagram);
        } catch (PartInitException e) {
          ErrorDialog.openError(getContainer().getShell(),
              Messages.StatechartCreationWizardOpenEditorError, null, e.getStatus());
        }
      }
    }
  }
}
