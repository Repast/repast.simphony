package repast.simphony.statecharts.sheets;

import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.statecharts.editor.CodePropertyEditor;
import repast.simphony.statecharts.editor.CodeUpdateStrategy;
import repast.simphony.statecharts.editor.EditorSupport;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.StatechartPackage;

public class FinalStateSheet extends FocusFixComposite implements BindableFocusableSheet {

  private Text idTxt;
  private EditorSupport support = new EditorSupport();
  private LanguageButtonsGroup buttonGroup;

  public FinalStateSheet(FormToolkit toolkit, Composite parent) {
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
    GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
    gd_composite.heightHint = 183;
    composite.setLayoutData(gd_composite);
    GridLayout gl_composite = new GridLayout(1, false);
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
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
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

    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");
    
    CodePropertyEditor onEnterEditor = support.createEditor();
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
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.swt.widgets.Widget#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();
    try {
      support.dispose();
    } catch (CoreException ex) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editor", ex);
    }
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

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(
        400, idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    if (support.getEditor(0).getEditorInput() == null) support.init((AbstractState)eObject);

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, support.getEditor(0).getTextWidget()),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER).observe(eObject), null, 
            new CodeUpdateStrategy(support.getEditor(0).getJavaSourceViewer()));

    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);
  }
}
