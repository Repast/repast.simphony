package repast.simphony.statecharts.part;

import org.eclipse.core.internal.events.ResourceChangeEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gmf.runtime.common.ui.services.marker.MarkerNavigationService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.texteditor.ITextEditorExtension3;

import repast.simphony.statecharts.navigator.StatechartNavigatorItem;
import repast.simphony.statecharts.validation.BadCodeFinder;

/**
 * @generated NOT
 */
// ITextEditorExtension3 is there so we can turn on smart editing for the
// code editors for the diagram object properties (e.g. OnEnter etc.).
public class StatechartDiagramEditor extends DiagramDocumentEditor implements IGotoMarker, ITextEditorExtension3 {

  /**
   * generated NOT
   */
  private static final int LIGHT_GRAY_RGB = 12632256;

  /**
   * @generated
   */
  public static final String ID = "repast.simphony.statecharts.part.StatechartDiagramEditorID"; //$NON-NLS-1$

  /**
   * @generated
   */
  public static final String CONTEXT_ID = "repast.simphony.statecharts.ui.diagramContext"; //$NON-NLS-1$
  
  class FocusSetter implements Runnable {

    @Override
    public void run() {
      IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .findView("org.eclipse.ui.views.PropertySheet");
      part.getSite().getPage().activate(part);
    }
  }
  
  
  private IPartListener2 partListener = new IPartListener2() {
    
      @Override
      public void partActivated(IWorkbenchPartReference partRef) {
        if (activatePropSheet) {
          activatePropSheet = false;
          Display.getCurrent().syncExec(new FocusSetter());
        }
      }

      @Override
      public void partBroughtToTop(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partClosed(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partDeactivated(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partOpened(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partHidden(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partVisible(IWorkbenchPartReference partRef) {
      }

      @Override
      public void partInputChanged(IWorkbenchPartReference partRef) {
      }
    };
  
  private IResourceChangeListener changeListener = new IResourceChangeListener() {
    
    private BadCodeFinder finder = new BadCodeFinder();
    
    @Override
    public void resourceChanged(IResourceChangeEvent event) {
      IResourceDelta delta = event.getDelta();
      try {
        finder.reset();
        delta.accept(finder, IResourceDelta.MARKERS);
      } catch (CoreException e) {
        StatechartDiagramEditorPlugin.getInstance().logError("Error while looking for bad code", e);
      }
      
      if (finder.foundBadCode()) {
        //System.out.println(StatechartDiagramEditor.this.getDiagramEditPart());
        //System.out.println(getDocumentProvider());
        //System.out.println(getDocumentProvider().getDocument(getEditorInput()).getContent());
        ValidateAction.runValidation(StatechartDiagramEditor.this.getDiagramEditPart(), 
            (View)getDocumentProvider().getDocument(getEditorInput()).getContent());
      }
    }
  };
  
  private boolean activatePropSheet = false;

  /**
   * @generated NOT
   */
  public StatechartDiagramEditor() {
    super(true);
    ResourcesPlugin.getWorkspace().addResourceChangeListener(changeListener, ResourceChangeEvent.POST_BUILD);
  }
  
  public void init(final IEditorSite site, final IEditorInput input)
      throws PartInitException {
    super.init(site, input);
    site.getPage().addPartListener(partListener);
    if (input instanceof IFileEditorInput) {
      // this initializes the compiler so it can be used more quickly
      // in the code property editors
      IJavaProject project = JavaCore.create(((IFileEditorInput) input).getFile().getProject());
      repast.simphony.statecharts.editor.Compiler.INSTANCE.getCompilerTask(project);
    }
  }
  
  
 
  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor#dispose()
   */
  @Override
  public void dispose() {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(changeListener);
    getEditorSite().getPage().removePartListener(partListener);
    super.dispose();
  }
  
  /**
   * @generated NOT
   */
  @Override
  public void doSave(IProgressMonitor monitor) {
    IViewPart part = getSite().getPage().findView("org.eclipse.ui.views.PropertySheet");
    activatePropSheet = part != null && getSite().getPage().getActivePart().equals(part);
    super.doSave(monitor);
  }

  /**
   * @generated
   */
  protected String getContextID() {
    return CONTEXT_ID;
  }
  
  /**
   * @generated
   */
  protected PaletteRoot createPaletteRoot(PaletteRoot existingPaletteRoot) {
    PaletteRoot root = super.createPaletteRoot(existingPaletteRoot);
    new StatechartPaletteFactory().fillPalette(root);
    return root;
  }

  /**
   * @generated
   */
  protected PreferencesHint getPreferencesHint() {
    return StatechartDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
  }

  /**
   * @generated
   */
  public String getContributorId() {
    return StatechartDiagramEditorPlugin.ID;
  }

  /**
   * @generated
   */
  @SuppressWarnings("rawtypes")
  public Object getAdapter(Class type) {
    if (type == IShowInTargetList.class) {
      return new IShowInTargetList() {
        public String[] getShowInTargetIds() {
          return new String[] { ProjectExplorer.VIEW_ID };
        }
      };
    }
    return super.getAdapter(type);
  }

  /**
   * @generated
   */
  protected IDocumentProvider getDocumentProvider(IEditorInput input) {
    if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {
      return StatechartDiagramEditorPlugin.getInstance().getDocumentProvider();
    }
    return super.getDocumentProvider(input);
  }

  /**
   * @generated
   */
  public TransactionalEditingDomain getEditingDomain() {
    IDocument document = getEditorInput() != null ? getDocumentProvider().getDocument(
        getEditorInput()) : null;
    if (document instanceof IDiagramDocument) {
      return ((IDiagramDocument) document).getEditingDomain();
    }
    return super.getEditingDomain();
  }

  /**
   * @generated
   */
  protected void setDocumentProvider(IEditorInput input) {
    if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {
      setDocumentProvider(StatechartDiagramEditorPlugin.getInstance().getDocumentProvider());
    } else {
      super.setDocumentProvider(input);
    }
  }

  /**
   * @generated
   */
  public void gotoMarker(IMarker marker) {
    MarkerNavigationService.getInstance().gotoMarker(this, marker);
  }

  /**
   * @generated
   */
  public boolean isSaveAsAllowed() {
    return true;
  }

  /**
   * @generated
   */
  public void doSaveAs() {
    performSaveAs(new NullProgressMonitor());
  }

  /**
   * @generated
   */
  protected void performSaveAs(IProgressMonitor progressMonitor) {
    Shell shell = getSite().getShell();
    IEditorInput input = getEditorInput();
    SaveAsDialog dialog = new SaveAsDialog(shell);
    IFile original = input instanceof IFileEditorInput ? ((IFileEditorInput) input).getFile()
        : null;
    if (original != null) {
      dialog.setOriginalFile(original);
    }
    dialog.create();
    IDocumentProvider provider = getDocumentProvider();
    if (provider == null) {
      // editor has been programmatically closed while the dialog was open
      return;
    }
    if (provider.isDeleted(input) && original != null) {
      String message = NLS.bind(Messages.StatechartDiagramEditor_SavingDeletedFile,
          original.getName());
      dialog.setErrorMessage(null);
      dialog.setMessage(message, IMessageProvider.WARNING);
    }
    if (dialog.open() == Window.CANCEL) {
      if (progressMonitor != null) {
        progressMonitor.setCanceled(true);
      }
      return;
    }
    IPath filePath = dialog.getResult();
    if (filePath == null) {
      if (progressMonitor != null) {
        progressMonitor.setCanceled(true);
      }
      return;
    }
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IFile file = workspaceRoot.getFile(filePath);
    final IEditorInput newInput = new FileEditorInput(file);
    // Check if the editor is already open
    IEditorMatchingStrategy matchingStrategy = getEditorDescriptor().getEditorMatchingStrategy();
    IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
        .getActivePage().getEditorReferences();
    for (int i = 0; i < editorRefs.length; i++) {
      if (matchingStrategy.matches(editorRefs[i], newInput)) {
        MessageDialog.openWarning(shell, Messages.StatechartDiagramEditor_SaveAsErrorTitle,
            Messages.StatechartDiagramEditor_SaveAsErrorMessage);
        return;
      }
    }
    boolean success = false;
    try {
      provider.aboutToChange(newInput);
      getDocumentProvider(newInput).saveDocument(progressMonitor, newInput,
          getDocumentProvider().getDocument(getEditorInput()), true);
      success = true;
    } catch (CoreException x) {
      IStatus status = x.getStatus();
      if (status == null || status.getSeverity() != IStatus.CANCEL) {
        ErrorDialog.openError(shell, Messages.StatechartDiagramEditor_SaveErrorTitle,
            Messages.StatechartDiagramEditor_SaveErrorMessage, x.getStatus());
      }
    } finally {
      provider.changed(newInput);
      if (success) {
        setInput(newInput);
      }
    }
    if (progressMonitor != null) {
      progressMonitor.setCanceled(!success);
    }
  }

  /**
   * @generated
   */
  public ShowInContext getShowInContext() {
    return new ShowInContext(getEditorInput(), getNavigatorSelection());
  }

  /**
   * @generated
   */
  private ISelection getNavigatorSelection() {
    IDiagramDocument document = getDiagramDocument();
    if (document == null) {
      return StructuredSelection.EMPTY;
    }
    Diagram diagram = document.getDiagram();
    if (diagram == null || diagram.eResource() == null) {
      return StructuredSelection.EMPTY;
    }
    IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());
    if (file != null) {
      StatechartNavigatorItem item = new StatechartNavigatorItem(diagram, file, false);
      return new StructuredSelection(item);
    }
    return StructuredSelection.EMPTY;
  }

  /**
   * @generated NOT
   */
  protected void configureGraphicalViewer() {
    super.configureGraphicalViewer();
    DiagramEditorContextMenuProvider provider = new DiagramEditorContextMenuProvider(this,
        getDiagramGraphicalViewer());
    getDiagramGraphicalViewer().setContextMenu(provider);
    getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider,
        getDiagramGraphicalViewer());

    // idea here is to replace the poor defaults with something reasonable
    // unless the user has changed them him or herself.
    if (getDiagramGraphicalViewer() instanceof DiagramGraphicalViewer) {
      PreferenceStore prefStore = getWorkspaceViewerPreferenceStore();
      if (prefStore.contains(WorkspaceViewerProperties.GRIDLINECOLOR)
          // on OSX the light gray is almost invisible, so we want black
          && prefStore.getInt(WorkspaceViewerProperties.GRIDLINECOLOR) == LIGHT_GRAY_RGB && Platform.getOS().equals(Platform.OS_MACOSX)) {
        prefStore.setValue(WorkspaceViewerProperties.GRIDLINECOLOR, SWT.COLOR_BLACK);
      }

      if (prefStore.contains(WorkspaceViewerProperties.GRIDLINESTYLE)
          && prefStore.getInt(WorkspaceViewerProperties.GRIDLINESTYLE) == SWT.LINE_CUSTOM) {
        prefStore.setValue(WorkspaceViewerProperties.GRIDLINESTYLE, SWT.LINE_SOLID);
      }
      getWorkspaceViewerPreferenceStore().setValue(WorkspaceViewerProperties.GRIDORDER, false);
      ((DiagramGraphicalViewer) getDiagramGraphicalViewer())
          .hookWorkspacePreferenceStore(getWorkspaceViewerPreferenceStore());

    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditorExtension3#getInsertMode()
   */
  @Override
  public InsertMode getInsertMode() {
    return ITextEditorExtension3.SMART_INSERT;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditorExtension3#setInsertMode(org.eclipse.ui.texteditor.ITextEditorExtension3.InsertMode)
   */
  @Override
  public void setInsertMode(InsertMode mode) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditorExtension3#showChangeInformation(boolean)
   */
  @Override
  public void showChangeInformation(boolean show) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditorExtension3#isChangeInformationShowing()
   */
  @Override
  public boolean isChangeInformationShowing() {
    return false;
  }
  
  

}
