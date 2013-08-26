package repast.simphony.statecharts.sheets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;

import repast.simphony.statecharts.editor.CodePropertyEditor;
import repast.simphony.statecharts.generator.TemplateGenerator;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.StatechartPackage;

public class StateSheet extends FocusFixComposite implements BindableFocusableSheet {

  private Text idTxt;
  private CodePropertyEditor onEnterEditor;
  private CodePropertyEditor onExitEditor;

  private LanguageButtonsGroup buttonGroup;

  public StateSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NONE);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(3, false));

    Label lblId = new Label(this, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");

    idTxt = new Text(this, SWT.BORDER);
    focusableControls.add(idTxt);
    GridData gd_idFld = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    gd_idFld.widthHint = 200;
    idTxt.setLayoutData(gd_idFld);
    toolkit.adapt(idTxt, true, true);
    new Label(this, SWT.NONE);

    Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    toolkit.adapt(label, true, true);
    new Label(this, SWT.NONE);

    Composite composite = new Composite(this, SWT.NONE);
    //gd_composite.heightHint = 183;
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
    GridLayout gl_composite = new GridLayout(2, false);
    gl_composite.horizontalSpacing = 12;
    gl_composite.marginHeight = 0;
    composite.setLayout(gl_composite);
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);

    Composite composite_1 = new Composite(composite, SWT.NONE);
    GridLayout gl_composite_1 = new GridLayout(5, false);
    gl_composite_1.verticalSpacing = 0;
    gl_composite_1.horizontalSpacing = 0;
    gl_composite_1.marginHeight = 0;
    gl_composite_1.marginWidth = 0;
    composite_1.setLayout(gl_composite_1);
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
    gd_composite_1.widthHint = 353;
    composite_1.setLayoutData(gd_composite_1);
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);

    Label lblLanguage = new Label(composite_1, SWT.NONE);
    lblLanguage.setBounds(0, 0, 59, 14);
    toolkit.adapt(lblLanguage, true, true);
    lblLanguage.setText("Language:");
    new Label(composite_1, SWT.NONE);

    Button btnJava = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnJava, true, true);
    btnJava.setText("Java");

    Button btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");

    Button btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");

    buttonGroup = new LanguageButtonsGroup(btnJava, btnRelogo, btnGroovy);

    Label lblOnEnter = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnEnter, true, true);
    lblOnEnter.setText("On Enter:");

    Label lblOnExit = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnExit, true, true);
    lblOnExit.setText("On Exit:");
    
    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");

    onEnterEditor = new CodePropertyEditor();
    Group group = new Group(composite, SWT.BORDER);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    group.setLayoutData(data);
    
    GridLayout grpLayout = new GridLayout(1, true);
    grpLayout.verticalSpacing = 0;
    grpLayout.horizontalSpacing = 0;
    grpLayout.marginHeight = 0;
    grpLayout.marginWidth = 0;
    
    group.setLayout(grpLayout);
    onEnterEditor.createPartControl(part.getSite(), group);
    StyledText widget = onEnterEditor.getTextWidget();
    data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    //data.heightHint = -1;
    widget.getParent().setLayoutData(data);
    group.setLayoutData(data);
    focusableControls.add(widget);

    onExitEditor = new CodePropertyEditor();
    group = new Group(composite, SWT.BORDER);
    data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    group.setLayoutData(data);
    
    grpLayout = new GridLayout(1, true);
    grpLayout.verticalSpacing = 0;
    grpLayout.horizontalSpacing = 0;
    grpLayout.marginHeight = 0;
    grpLayout.marginWidth = 0;
    
    group.setLayout(grpLayout);
    onExitEditor.createPartControl(part.getSite(), group);
    new Label(composite, SWT.NONE);
    widget = onExitEditor.getTextWidget();
    GridData gd_onExitTxt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    //gd_onExitTxt.heightHint = -1;
    gd_onExitTxt.horizontalIndent = 1;
    widget.getParent().setLayoutData(gd_onExitTxt);
    group.setLayoutData(gd_onExitTxt);
    focusableControls.add(widget);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.statecharts.sheets.BindableFocusableSheet#resetFocus()
   */
  @Override
  public void resetFocus() {
    idTxt.setFocus();
  }
  
  private void initEditorInput(CodePropertyEditor editor, AbstractState state) {
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
      StatechartDiagramEditorPlugin.getInstance().logError("Error refreshing temporary edit file", e);
    }
    input = new FileEditorInput(file);
    
    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");
    editor.init(part.getSite(), input);
  }
  
  private void disposeEditor(CodePropertyEditor editor) throws CoreException {
    //editor.hasErrors();
    editor.dispose();
    FileEditorInput input = (FileEditorInput) editor.getEditorInput();
    if (input != null) input.getFile().delete(true, new NullProgressMonitor());
  }
  
  /**
   * Disposes of the resources, editors, etc. used by this property sheet.
   */
  public void dispose() {
    try {
      disposeEditor(onEnterEditor);
      disposeEditor(onExitEditor);
    } catch (CoreException ex) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editor", ex);
    }
  }

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(
        400, idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    if (onEnterEditor.getEditorInput() == null) initEditorInput(onEnterEditor, (AbstractState)eObject);
    if (onExitEditor.getEditorInput() == null) initEditorInput(onExitEditor, (AbstractState)eObject);
    
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, onEnterEditor.getTextWidget()),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER).observe(eObject));

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, onExitEditor.getTextWidget()),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT).observe(eObject));

    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);
  }
}
