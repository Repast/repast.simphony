package repast.simphony.statecharts.sheets;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.statecharts.editor.EditorSupport;
import repast.simphony.statecharts.editor.StatechartCodeEditor;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;

public class StateSheet extends FocusFixComposite implements BindableFocusableSheet {

  private static String ON_ENTER_ID = "on.enter.id";
  private static String ON_EXIT_ID = "on.exit.id";

  private Text idTxt;
  private EditorSupport edSupport = new EditorSupport();

  private LanguageButtonsGroup buttonGroup;
  private LanguageTypes language;
  private AbstractState state;

  // we have to use the emf notification mechanism
  // for listening to language changes because
  // we use the state itself to determine the current language
  // if we get notified via the button, that occurs before
  // the state's language property has been updated
  private Adapter langNotify = new AdapterImpl() {
    @Override
    public void notifyChanged(Notification msg) {
      if (msg.getFeature() != null
          && msg.getFeature().equals(StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE)) {
        languageChanged();
      }
    }
  };

  private BindingSupport binding;

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
    // gd_composite.heightHint = 183;
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

    edSupport.createEntry(ON_ENTER_ID, composite);
    edSupport.createEntry(ON_EXIT_ID, composite);
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

  private void languageChanged() {
    LanguageTypes newLang = buttonGroup.getSelectedType();
    if (newLang != language) {
      language = newLang;
      edSupport.resetStateOnEditor(ON_ENTER_ID, state);
      edSupport.resetStateOnEditor(ON_EXIT_ID, state);

      binding.removeBindings();
      StatechartCodeEditor editor = edSupport.getEditor(ON_ENTER_ID);
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER, editor.getCodeViewer());
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER_IMPORTS,
          editor.getImportViewer());

      editor = edSupport.getEditor(ON_EXIT_ID);
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT, editor.getCodeViewer());
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT_IMPORTS,
          editor.getImportViewer());
    }
  }

  /**
   * Disposes of the resources, editors, etc. used by this property sheet.
   */
  public void dispose() {
    super.dispose();
    state.eAdapters().remove(langNotify);
    try {
      edSupport.dispose();
    } catch (CoreException ex) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editor", ex);
    }
  }

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    // this can happen if switch between elements of the same
    // type -- the eObject is changed but the property sheet
    // is not disposed
    if (state != null && !eObject.equals(state)) {
      state.eAdapters().remove(langNotify);
      if (state.getLanguage() != ((AbstractState)eObject).getLanguage()) {
        // if the language is different the dispose of the editors
        // and start again.
        try {
          edSupport.disposeAllEditors();
        } catch (CoreException e) {
          StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editors", e);
        }
      }
    }
    
    state = (AbstractState) eObject;
    language = state.getLanguage();

    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(
        400, idTxt);

    context.bindValue(observe, property.observe(eObject));
    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);

    
    edSupport.initStateOnEditor(ON_ENTER_ID, state);
    edSupport.initStateOnEditor(ON_EXIT_ID, state);

    state.eAdapters().add(langNotify);

    StatechartCodeEditor editor = edSupport.getEditor(ON_ENTER_ID);
    binding = new BindingSupport(context, eObject);
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER, editor.getCodeViewer());
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER_IMPORTS,
        editor.getImportViewer());

    editor = edSupport.getEditor(ON_EXIT_ID);
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT, editor.getCodeViewer());
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT_IMPORTS,
        editor.getImportViewer());
  }
}
