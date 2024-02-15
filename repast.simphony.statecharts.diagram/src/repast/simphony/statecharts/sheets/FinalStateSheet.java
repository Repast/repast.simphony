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
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
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

public class FinalStateSheet extends FocusFixComposite implements BindableFocusableSheet {

  private static String ON_ENTER_ID = "on.enter.id";

  private Text idTxt;
  private EditorSupport support = new EditorSupport();
  private LanguageButtonsGroup buttonGroup;
  private LanguageTypes language;
  private AbstractState state;
  private BindingSupport binding;

  // we have to use the emf notification mechanism
  // for listening to language changes because
  // we use the state itself to get is current language
  private Adapter langNotify = new AdapterImpl() {
    @Override
    public void notifyChanged(Notification msg) {
      if (msg.getFeature() != null
          && msg.getFeature().equals(StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE)) {
        languageChanged();
      }
    }
  };

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
    GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1);
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
    
    Button btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");

    Button btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");

    buttonGroup = new LanguageButtonsGroup(btnJava, btnRelogo, btnGroovy);

    Label lblOnEnter = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnEnter, true, true);
    lblOnEnter.setText("On Enter:");

    support.createEntry(ON_ENTER_ID, composite);
  }
  
  private void languageChanged() {
    LanguageTypes newLang = buttonGroup.getSelectedType();
    // only switch if switching from Java to something else
    // switching between Groovy and ReLogo doesn't require an editor switch
    if (newLang != language && (newLang == LanguageTypes.JAVA || language == LanguageTypes.JAVA)) {
      support.resetStateOnEditor(ON_ENTER_ID, state);
      binding.removeBindings();
      StatechartCodeEditor editor = support.getEditor(ON_ENTER_ID);
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER, editor.getCodeViewer());
      binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER_IMPORTS, editor.getImportViewer());
    }
    language = newLang;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.widgets.Widget#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();
    try {
      state.eAdapters().remove(langNotify);
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
    if (state != null && !eObject.equals(state)) {
      state.eAdapters().remove(langNotify);
      if (state.getLanguage() != ((AbstractState)eObject).getLanguage()) {
        // if the language is different the dispose of the editors
        // and start again.
        try {
          support.disposeAllEditors();
        } catch (CoreException e) {
          StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editors", e);
        }
      }
    }
    
    state = (AbstractState)eObject;
    language = state.getLanguage();
    
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(
        400, idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    support.initStateOnEditor(ON_ENTER_ID, state);
    state.eAdapters().add(langNotify);

    binding = new BindingSupport(context, eObject);
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER, support.getEditor(ON_ENTER_ID)
        .getCodeViewer());
    binding.bind(StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER_IMPORTS, support.getEditor(ON_ENTER_ID)
        .getImportViewer());

    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);
  }
}
