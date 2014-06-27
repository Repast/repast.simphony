/**
 * 
 */
package repast.simphony.statecharts.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import repast.simphony.statecharts.generator.TemplateGenerator;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * Support for creating, initializing and disposing of CodePropertyEditors.
 * The creation of an editor is staggered. First an entry should be created
 * and the resulting Group placed in the GUI. Then when the code property
 * is bound in the propery sheet, the editor itself can be initialized
 * using one of the init* methods.
 * 
 * @author Nick Collier
 */
public class EditorSupport {

  private static interface GeneratorCall {
    IPath call(IProject proj, TemplateGenerator gen, Transition trans);
  }

  // individual data for each editor, including
  // its UI group, layout column span and the
  // editor itself.
  private static class EditorData {

    private StatechartCodeEditor editor;
    private Group group;
    private int colSpan;

    public EditorData(Group group, int colSpan) {
      this.group = group;
      this.colSpan = colSpan;
    }

    public void dispose() throws CoreException {
      if (editor != null) {
        editor.dispose();
        FileEditorInput input = (FileEditorInput) editor.getEditorInput();
        if (input != null)
          // deletes the template file
          input.getFile().delete(true, new NullProgressMonitor());
        editor = null;
      }
    }
  }

  private static int RETURN_VOID_OFFSET = 4;
  private static int RETURN_VALUE_OFFSET = 5;

  private Map<String, EditorData> editors = new HashMap<>();

  private IProject findProject() {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IFileEditorInput input = (IFileEditorInput) window.getActivePage().getActiveEditor()
        .getEditorInput();
    return input.getFile().getProject();
  }

  private void initEditorInput(IFile file, EditorData data, int lineOffset) {
    StatechartCodeEditor editor = data.editor;
    IFileEditorInput oldInput = (IFileEditorInput) editor.getEditorInput();
    IFile oldFile = null;
    if (oldInput != null) {
      oldFile = oldInput.getFile();
    }

    try {
      file.refreshLocal(IResource.DEPTH_ZERO, null);
    } catch (CoreException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error refreshing temporary edit file",
          e);
    }
    IFileEditorInput input = new FileEditorInput(file);

    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");
    editor.init(part.getSite(), input, lineOffset);

    if (oldFile != null && oldFile.exists()) {
      try {
        oldFile.getParent().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        oldFile.delete(IResource.FORCE, new NullProgressMonitor());
      } catch (CoreException e) {
        StatechartDiagramEditorPlugin.getInstance().logError("Error deleting temporary edit file",
            e);
      }
    }
  }

  /**
   * Resets the editor for any transition's trigger code property that
   * returns a boolean  by disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerConditionEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    boolean disposed = disposeEditor(data);
    if (disposed) {
      initTriggerConditionEditor(editorId, transition);
      data.group.layout(true);
      data.group.getParent().layout(true, true);
    }
  }

  /**
   * Initalizes the editor for any transition code property
   * that should return a boolean.
   * 
   * @param editorId
   * @param transition
   */
  public void initTriggerConditionEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateTriggerCondition(proj, trans);
      }
    }, RETURN_VALUE_OFFSET);
  }

  /**
   * Resets the editor for any transition's trigger code property that
   * returns a double  by disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerDblEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    boolean disposed = disposeEditor(data);
    if (disposed) {
      initTriggerDblEditor(editorId, transition);
      data.group.layout(true);
      data.group.getParent().layout(true, true);
    }
  }

  /**
   * Initalizes the editor for any transition code property
   * that should return a double.
   * 
   * @param editorId
   * @param transition
   */
  public void initTriggerDblEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateTriggerDbl(proj, trans);
      }
    }, RETURN_VALUE_OFFSET);
  }

  /**
   * Resets the editor for a transition's trigger message equals property by
   * disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerMEEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    boolean disposed = disposeEditor(data);
    if (disposed) {
      initTriggerMEEditor(editorId, transition);
      data.group.layout(true);
      data.group.getParent().layout(true, true);
    }
  }

  /**
   * Resets the editor input for a transition's message equals
   * trigger. This is necessary because the message equals trigger
   * and the message condition trigger share the same code property,
   * but need to use different template files.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerMEEditorInput(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateMessageEq(proj, trans);
      }
    }, RETURN_VALUE_OFFSET, true);

    EditorData data = getEditorData(editorId);
    data.group.layout(true);
    data.group.getParent().layout(true, true);
  }

  /**
   * Initalizes the editor for a transition's trigger message equals property.
   * 
   * @param editorId
   * @param transition
   */
  public void initTriggerMEEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateMessageEq(proj, trans);
      }
    }, RETURN_VALUE_OFFSET);
  }

  /**
   * Resets the editor for a transition's trigger message condition property by
   * disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerMCEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    boolean disposed = disposeEditor(data);
    if (disposed) {
      initTriggerMCEditor(editorId, transition);
      data.group.layout(true);
      data.group.getParent().layout(true, true);
    }
  }

  /**
   * Resets the editor input for a transition's message condition
   * trigger. This is necessary because the message condition trigger
   * and the message equals trigger share the same code property,
   * but need to use different template files.
   * 
   * @param editorId
   * @param transition
   */
  public void resetTriggerMCEditorInput(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateMessageCond(proj, trans);
      }
    }, RETURN_VALUE_OFFSET, true);

    EditorData data = getEditorData(editorId);
    data.group.layout(true);
    data.group.getParent().layout(true, true);
  }

  /**
   * Initalizes the editor for a transition's trigger message condition property.
   * 
   * @param editorId
   * @param transition
   */
  public void initTriggerMCEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateMessageCond(proj, trans);
      }
    }, RETURN_VALUE_OFFSET);
  }

  private void doInit(String editorId, Transition transition, GeneratorCall genCall, int offset) {
    doInit(editorId, transition, genCall, offset, false);
  }

  private void doInit(String editorId, Transition transition, GeneratorCall genCall, int offset,
      boolean resetInput) {
    // this will create the editor control etc. if its not already created
    EditorData data = getEditor(editorId, transition.getTriggerCodeLanguage());
    if (resetInput || data.editor.getEditorInput() == null) {
      // create the input temlate file if its doesn't already exist
      IProject proj = findProject();
      TemplateGenerator gen = new TemplateGenerator();
      IPath path = genCall.call(proj, gen, transition);

      IFile file = proj.getFile(path);
      initEditorInput(file, getEditorData(editorId), offset);
    }
  }

  /**
   * Resets the editor for a transition's Guard property by
   * disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetGuardEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    disposeEditor(data);
    initGuardEditor(editorId, transition);
    data.group.layout(true);
    data.group.getParent().layout(true, true);
  }

  /**
   * Initalizes the editor for a transition's Guard property.
   * 
   * @param editorId
   * @param transition
   */
  public void initGuardEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateGuard(proj, trans);
      }
    }, RETURN_VALUE_OFFSET);

  }

  /**
   * Resets the editor for a transition's OnTransition property by
   * disposing of the existing editor, initializing the new one,
   * and refreshing the GUI.
   * 
   * @param editorId
   * @param transition
   */
  public void resetOnTransEditor(String editorId, Transition transition) {
    EditorData data = getEditorData(editorId);
    disposeEditor(data);
    initOnTransEditor(editorId, transition);
    data.group.layout(true);
    data.group.getParent().layout(true, true);
  }

  /**
   * Initalizes the editor for a transition's OnTransition property.
   * 
   * @param editorId
   * @param transition
   */
  public void initOnTransEditor(String editorId, Transition transition) {
    doInit(editorId, transition, new GeneratorCall() {
      public IPath call(IProject proj, TemplateGenerator gen, Transition trans) {
        return gen.generateOnTrans(proj, trans);
      }
    }, RETURN_VOID_OFFSET);
  }

  private boolean disposeEditor(EditorData data) {
    boolean disposed = data.editor != null;
    try {
      data.dispose();
    } catch (CoreException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing editor", e);
    }
    
    data.editor = null;
    // group can be disposed if the sheet that shows the editor
    // is being disposed rather than the editor being reset due
    // to a language change
    if (!data.group.isDisposed()) {
      for (Control control : data.group.getChildren()) {
        control.dispose();
      }
    }
    return disposed;
  }

  /**
   * Resets the editior for the AbstractStates on* code properties.
   * This disposes of the existing editor, intializes a new one,
   * and refreshes the GUI.
   * @param editorId
   * @param state
   */
  public void resetStateOnEditor(String editorId, AbstractState state) {
    EditorData data = getEditorData(editorId);
    disposeEditor(data);
    initStateOnEditor(editorId, state);
    data.group.layout(true);
    data.group.getParent().layout(true, true);
  }

  // initializes the editor for an abstract state on* code property
  // this creates the appropriate template files, editor input and
  // intializes the editor with that input
  private void doInit(StatechartCodeEditor editor, AbstractState state) {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IFileEditorInput input = (IFileEditorInput) window.getActivePage().getActiveEditor()
        .getEditorInput();
    IProject proj = input.getFile().getProject();

    TemplateGenerator gen = new TemplateGenerator();
    IPath path = gen.run(proj, state);

    IFile file = proj.getFile(path);

    try {
      file.refreshLocal(IResource.DEPTH_ZERO, null);
    } catch (CoreException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error refreshing temporary edit file",
          e);
    }
    input = new FileEditorInput(file);

    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");
    editor.init(part.getSite(), input, RETURN_VOID_OFFSET);
  }

  /**
   * Disposes of all the editors and removes all the entries.
   * 
   * @throws CoreException
   */
  public void dispose() throws CoreException {
    disposeAllEditors();
    editors.clear();
  }

  /**
   * Disposes of all the editors but does not remove the entries.
   * New editor can be created using the init* methods without
   * recreating an entry.
   * 
   * @throws CoreException
   */
  public void disposeAllEditors() throws CoreException {
    for (EditorData data : editors.values()) {
      disposeEditor(data);
    }
  }

  /**
   * Creates an "entry" for the specified editor in 
   * this EditorSupport. 
   * 
   * @param editorId
   * @param parent
   * @param colSpan
   * @return the group into which the editor gui widget will
   * ultimately be placed.
   */
  public Group createEntry(String editorId, Composite parent, int colSpan) {
    Group group = new Group(parent, SWT.BORDER);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, colSpan, 1);
    group.setLayoutData(data);

    GridLayout grpLayout = new GridLayout(1, true);
    grpLayout.verticalSpacing = 0;
    grpLayout.horizontalSpacing = 0;
    grpLayout.marginHeight = 0;
    grpLayout.marginWidth = 0;

    group.setLayout(grpLayout);

    editors.put(editorId, new EditorData(group, colSpan));

    return group;
  }

  /**
   * Creates an "entry" for the specified editor in 
   * this EditorSupport. 
   * 
   * @param editorId
   * @param parent
   * @return the Group widget into which the editor will be placed
   * when its created.
   */
  public Group createEntry(String editorId, Composite parent) {
    return createEntry(editorId, parent, 1);
  }

  private EditorData getEditor(String editorId, LanguageTypes language) {
    EditorData data = getEditorData(editorId);
    if (data.editor == null) {

      if (language == LanguageTypes.JAVA) {
        data.editor = new StatechartJavaEditor();
      } else {
        data.editor = new StatechartGroovyEditor();
      }

      IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .findView("org.eclipse.ui.views.PropertySheet");
      data.editor.createPartControl(part.getSite(), data.group);
      // this makes sure the widgets fill the space
      StyledText widget = data.editor.getCodeViewer().getTextWidget();
      GridData gData = new GridData(SWT.FILL, SWT.FILL, true, true, data.colSpan, 1);
      widget.getParent().setLayoutData(gData);
      widget.setLayoutData(gData);
      data.editor.getImportViewer().getTextWidget().setLayoutData(gData);

      data.group.layout(true);
      data.group.getParent().layout(true, true);
    }

    return data;
  }

  /**
   * Initializes an editor for editing an abstract states onEnter / onExit
   * code properties.
   * 
   * @param editorId
   * @param state
   */
  public void initStateOnEditor(String editorId, AbstractState state) {
    EditorData data = getEditor(editorId, state.getLanguage());
    if (data.editor.getEditorInput() == null) {
      doInit(data.editor, state);
    }
  }

  private EditorData getEditorData(String editorId) {
    EditorData data = editors.get(editorId);
    if (data == null)
      throw new IllegalArgumentException("Entry for editor '" + editorId + "' not found.");
    return data;
  }

  public StatechartCodeEditor getEditor(String editorId) {
    return getEditorData(editorId).editor;
  }
}
