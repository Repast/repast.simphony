package repast.simphony.statecharts.sheets;

import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CODE;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CODE_IMPORTS;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_CONDITION_CODE;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_CONDITION_CODE_IMPORTS;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_EXP_RATE_CODE;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_EXP_RATE_CODE_IMPORTS;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_PROB_CODE;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_PROB_CODE_IMPORTS;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_TIMED_CODE;
import static repast.simphony.statecharts.scmodel.StatechartPackage.Literals.TRANSITION__TRIGGER_TIMED_CODE_IMPORTS;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import repast.simphony.statecharts.editor.EditorSupport;
import repast.simphony.statecharts.editor.StatechartCodeEditor;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.TriggerTypes;

public class TransitionSheet extends FocusFixComposite implements BindableFocusableSheet {

  private static final String TRIGGER_TIME_ID = "trigger.time.id";
  private static final String TRIGGER_EXP_ID = "trigger.exp.id";
  private static final String TRIGGER_PROB_ID = "trigger.prob.id";
  private static final String TRIGGER_COND_ID = "trigger.cond.id";
  private static final String TRIGGER_MESSAGE_ID = "trigger.message.id";
  private static final String GUARD_ID = "guard.id";
  private static final String ON_TRANS_ID = "on.trans.id";

  private static final String[] MESSAGE_TYPES = { "When Message Meets a Condition",
      "When Message Equals ...", "When Message is of the Specified Class", "Always" };

  private static final String[] CLASS_TYPES = { "Object", "String", "int", "long", "float",
      "double", "boolean" };

  private static final int CONDITION_INDEX = 3;

  private Text idTxt;
  private EditorSupport support = new EditorSupport();
  private LanguageButtonsGroup buttonGroup;
  private Combo cmbTriggerType;
  private Text priorityTxt;
  private Text txtAlwaysPolling;
  private Text txtProbPolling, txtCondPolling, txtMessagePolling;
  private MessageCheckerTypes currentType = null;

  private EMFDataBindingContext bindingContext;
  private Binding pollingBinding;
  private Transition transition;

  private StackLayout triggerLayout;
  private Composite[] triggerComps = new Composite[6];

  private Label lblMessage, lblMessageClass;
  private Combo cmbMessageType, cmbMessageClass;

  private Button btnIsDefaultOut;
  private Button btnSelfTransition;

  private CTabItem tbtmTrigger, tbtmGuard, tbtmOnTrans;
  private CTabFolder tabFolder;
  private Composite compTrigger, compGuard, compTrans;

  private LanguageTypes language;

  // we have to use the emf notification mechanism
  // for listening to language changes because
  // we use the state itself to get is current language
  private Adapter langNotify = new AdapterImpl() {
    @Override
    public void notifyChanged(Notification msg) {
      if (msg.getFeature() != null
          && msg.getFeature().equals(StatechartPackage.Literals.TRANSITION__TRIGGER_CODE_LANGUAGE)) {
        languageChanged();
      }
    }
  };

  private BindingSupport bindings;

  public TransitionSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NO_FOCUS);

    setLayout(new GridLayout(1, false));
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);

    createHeaderSection(toolkit);

    tabFolder = new CTabFolder(this, SWT.FLAT);
    tabFolder.setTabHeight(20);
    GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    gd_tabFolder.verticalIndent = 4;
    tabFolder.setLayoutData(gd_tabFolder);
    toolkit.adapt(tabFolder);
    toolkit.paintBordersFor(tabFolder);
    tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
        SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("org.eclipse.ui.views.PropertySheet");
    IWorkbenchPartSite site = part.getSite();

    createTriggerSection(toolkit, tabFolder, site);
    createGuardSection(toolkit, tabFolder, site);
    createTransitionSection(toolkit, tabFolder, site);

    cmbTriggerType.select(0);
    cmbMessageType.select(3);
    cmbMessageClass.setText(CLASS_TYPES[0]);

    triggerLayout.topControl = triggerComps[0];
    addListeners();
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

  private void createHeaderSection(FormToolkit toolkit) {
    Composite composite_2 = toolkit.createComposite(this, SWT.NONE);
    composite_2.setLayout(new GridLayout(8, false));
    composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

    Label lblId = new Label(composite_2, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");

    idTxt = toolkit.createText(composite_2, "", SWT.NONE);
    focusableControls.add(idTxt);
    GridData gd_idTxt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_idTxt.widthHint = 200;
    idTxt.setLayoutData(gd_idTxt);

    Label lblPriority = new Label(composite_2, SWT.NONE);
    lblPriority.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblPriority, true, true);
    lblPriority.setText("Priority:");

    priorityTxt = toolkit.createText(composite_2, "", SWT.NONE);
    focusableControls.add(priorityTxt);
    GridData gd_priorityTxt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_priorityTxt.widthHint = 120;
    priorityTxt.setLayoutData(gd_priorityTxt);
    Bug383650Fix.applyFix(priorityTxt);

    btnIsDefaultOut = new Button(composite_2, SWT.CHECK);
    toolkit.adapt(btnIsDefaultOut, true, true);
    btnIsDefaultOut.setText("Default Transition");

    btnSelfTransition = new Button(composite_2, SWT.CHECK);
    toolkit.adapt(btnSelfTransition, true, true);
    btnSelfTransition.setText("Self Transition");
    new Label(composite_2, SWT.NONE);
    new Label(composite_2, SWT.NONE);

    Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(label, true, true);

    Composite composite_1 = new Composite(this, SWT.NONE);
    GridLayout gl_composite_1 = new GridLayout(5, false);
    gl_composite_1.verticalSpacing = 0;
    gl_composite_1.horizontalSpacing = 0;
    gl_composite_1.marginHeight = 0;
    gl_composite_1.marginWidth = 0;
    composite_1.setLayout(gl_composite_1);
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);

    Label lblLanguage = new Label(composite_1, SWT.NONE);
    lblLanguage.setBounds(0, 0, 59, 14);
    toolkit.adapt(lblLanguage, true, true);
    lblLanguage.setText("Language:");
    new Label(composite_1, SWT.NONE);

    Button btnJava = new Button(composite_1, SWT.RADIO);
    GridData gd_btnJava = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnJava.horizontalIndent = 5;
    btnJava.setLayoutData(gd_btnJava);
    toolkit.adapt(btnJava, true, true);
    btnJava.setText("Java");
    
    Button btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");
    
    Button btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");

    buttonGroup = new LanguageButtonsGroup(btnJava, btnRelogo, btnGroovy);
  }

  private void createGuardSection(FormToolkit toolkit, CTabFolder tabFolder, IWorkbenchPartSite site) {
    tbtmGuard = new CTabItem(tabFolder, SWT.NONE);
    tbtmGuard.setText("Guard");

    compGuard = new Composite(tabFolder, SWT.NONE);
    tbtmGuard.setControl(compGuard);
    GridLayout gl_composite = new GridLayout(2, false);
    gl_composite.marginHeight = 3;
    compGuard.setLayout(gl_composite);
    toolkit.adapt(compGuard);
    toolkit.paintBordersFor(compGuard);
    support.createEntry(GUARD_ID, compGuard);
  }

  private void createTriggerSection(FormToolkit toolkit, CTabFolder tabFolder,
      IWorkbenchPartSite site) {

    tbtmTrigger = new CTabItem(tabFolder, SWT.NONE);
    tbtmTrigger.setText("Trigger");

    compTrigger = new Composite(tabFolder, SWT.NONE);
    compTrigger.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    tbtmTrigger.setControl(compTrigger);
    toolkit.paintBordersFor(compTrigger);
    GridLayout gl_composite_3 = new GridLayout(2, false);
    compTrigger.setLayout(gl_composite_3);

    Label lblTriggerType = new Label(compTrigger, SWT.NONE);
    lblTriggerType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblTriggerType, true, true);
    lblTriggerType.setText("Trigger Type:");

    cmbTriggerType = new Combo(compTrigger, SWT.READ_ONLY);
    GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_combo.widthHint = 220;
    cmbTriggerType.setLayoutData(gd_combo);
    cmbTriggerType.setItems(new String[] { TriggerTypeItem.ALWAYS.toString(),
        TriggerTypeItem.TIMED.toString(), TriggerTypeItem.PROBABILITY.toString(),
        TriggerTypeItem.CONDITION.toString(), TriggerTypeItem.EXP_RATE.toString(),
        TriggerTypeItem.MESSAGE.toString() });

    toolkit.adapt(cmbTriggerType);
    toolkit.paintBordersFor(cmbTriggerType);

    Composite composite = toolkit.createComposite(compTrigger, SWT.NONE);
    triggerLayout = new StackLayout();
    composite.setLayout(triggerLayout);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    toolkit.paintBordersFor(composite);

    createAlwaysComp(composite, toolkit);
    createTimeComp(composite, toolkit, site);
    createExpRateComp(composite, toolkit, site);
    createProbComp(composite, toolkit, site);
    createConditionComp(composite, toolkit, site);
    createMessageComp(composite, toolkit, site);
  }

  private void createMessageComp(Composite parent, FormToolkit toolkit, IWorkbenchPartSite site) {
    Composite cmpMessage = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[5] = cmpMessage;
    toolkit.paintBordersFor(cmpMessage);
    GridLayout gl_cmpMessage = new GridLayout(6, false);
    gl_cmpMessage.marginWidth = 0;
    cmpMessage.setLayout(gl_cmpMessage);

    Label label = toolkit.createLabel(cmpMessage, "Polling Time:", SWT.NONE);
    label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

    txtMessagePolling = toolkit.createText(cmpMessage, "1", SWT.NONE);
    focusableControls.add(txtMessagePolling);
    txtMessagePolling.addVerifyListener(new DoubleVerifier());
    GridData gd_txtAlwaysPolling = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_txtAlwaysPolling.widthHint = 120;
    txtMessagePolling.setLayoutData(gd_txtAlwaysPolling);
    Bug383650Fix.applyFix(txtMessagePolling);

    Label lblTriggerWhen = new Label(cmpMessage, SWT.NONE);
    GridData gd_lblTriggerWhen = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
    gd_lblTriggerWhen.horizontalIndent = 13;
    lblTriggerWhen.setLayoutData(gd_lblTriggerWhen);
    toolkit.adapt(lblTriggerWhen, true, true);
    lblTriggerWhen.setText("Trigger");

    cmbMessageType = new Combo(cmpMessage, SWT.READ_ONLY);
    GridData gd_cmbMessageType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbMessageType.widthHint = 250;
    cmbMessageType.setLayoutData(gd_cmbMessageType);
    cmbMessageType.setItems(MESSAGE_TYPES);
    toolkit.adapt(cmbMessageType);
    toolkit.paintBordersFor(cmbMessageType);
    new Label(cmpMessage, SWT.NONE);

    Composite composite = toolkit.createComposite(cmpMessage, SWT.NONE);
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.marginHeight = 0;
    gl_composite.marginLeft = 12;
    composite.setLayout(gl_composite);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
    toolkit.paintBordersFor(composite);

    Composite composite_1 = new Composite(composite, SWT.NONE);
    composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);
    GridLayout gl_composite_1 = new GridLayout(2, false);
    gl_composite_1.marginWidth = 0;
    composite_1.setLayout(gl_composite_1);

    lblMessageClass = toolkit.createLabel(composite_1, "Message Class:", SWT.NONE);
    cmbMessageClass = new Combo(composite_1, SWT.NONE);
    GridData gd_cmbMessageClass = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbMessageClass.widthHint = 200;
    cmbMessageClass.setItems(CLASS_TYPES);
    cmbMessageClass.setLayoutData(gd_cmbMessageClass);
    toolkit.adapt(cmbMessageClass);
    toolkit.paintBordersFor(cmbMessageClass);

    lblMessage = toolkit.createLabel(composite, "Condition:", SWT.NONE);
    support.createEntry(TRIGGER_MESSAGE_ID, composite);
  }

  private void createProbComp(Composite parent, FormToolkit toolkit, IWorkbenchPartSite site) {
    Composite cmpProb = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[2] = cmpProb;
    toolkit.paintBordersFor(cmpProb);
    cmpProb.setLayout(new GridLayout(2, false));

    toolkit.createLabel(cmpProb, "Polling Time:", SWT.NONE);

    txtProbPolling = toolkit.createText(cmpProb, "1", SWT.NONE);
    focusableControls.add(txtProbPolling);
    txtProbPolling.addVerifyListener(new DoubleVerifier());
    GridData gd_txtAlwaysPolling = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_txtAlwaysPolling.widthHint = 120;
    txtProbPolling.setLayoutData(gd_txtAlwaysPolling);
    Bug383650Fix.applyFix(txtProbPolling);

    toolkit.createLabel(cmpProb, "Probability:", SWT.NONE);
    support.createEntry(TRIGGER_PROB_ID, cmpProb, 2);
  }

  private void createConditionComp(Composite parent, FormToolkit toolkit, IWorkbenchPartSite site) {
    Composite cmpCond = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[3] = cmpCond;
    toolkit.paintBordersFor(cmpCond);
    cmpCond.setLayout(new GridLayout(2, false));

    toolkit.createLabel(cmpCond, "Polling Time:", SWT.NONE);

    txtCondPolling = toolkit.createText(cmpCond, "1", SWT.NONE);
    focusableControls.add(txtCondPolling);
    txtCondPolling.addVerifyListener(new DoubleVerifier());
    GridData gd_txtAlwaysPolling = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_txtAlwaysPolling.widthHint = 120;
    txtCondPolling.setLayoutData(gd_txtAlwaysPolling);
    Bug383650Fix.applyFix(txtCondPolling);

    toolkit.createLabel(cmpCond, "Condition:", SWT.NONE);
    support.createEntry(TRIGGER_COND_ID, cmpCond, 2);
  }

  private void createTimeComp(Composite parent, FormToolkit toolkit, IWorkbenchPartSite site) {
    Composite cmpTimed = toolkit.createComposite(parent, SWT.NONE);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    cmpTimed.setLayoutData(data);
    triggerComps[1] = cmpTimed;
    toolkit.paintBordersFor(cmpTimed);
    cmpTimed.setLayout(new GridLayout(1, false));

    toolkit.createLabel(cmpTimed, "Time:", SWT.NONE);
    support.createEntry(TRIGGER_TIME_ID, cmpTimed);
  }

  private void createExpRateComp(Composite parent, FormToolkit toolkit, IWorkbenchPartSite site) {
    Composite cmpExp = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[4] = cmpExp;
    toolkit.paintBordersFor(cmpExp);
    cmpExp.setLayout(new GridLayout(1, false));

    toolkit.createLabel(cmpExp, "Exponential Decay Rate:", SWT.NONE);
    support.createEntry(TRIGGER_EXP_ID, cmpExp);
  }

  private void createAlwaysComp(Composite parent, FormToolkit toolkit) {
    Composite cmpAlways = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[0] = cmpAlways;
    toolkit.paintBordersFor(cmpAlways);
    cmpAlways.setLayout(new GridLayout(2, false));

    Label lblPollingTime = toolkit.createLabel(cmpAlways, "Polling Time:", SWT.NONE);
    lblPollingTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

    txtAlwaysPolling = toolkit.createText(cmpAlways, "1", SWT.NONE);
    focusableControls.add(txtAlwaysPolling);
    txtAlwaysPolling.addVerifyListener(new DoubleVerifier());
    GridData gd_txtAlwaysPolling = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txtAlwaysPolling.widthHint = 120;
    txtAlwaysPolling.setLayoutData(gd_txtAlwaysPolling);
    Bug383650Fix.applyFix(txtAlwaysPolling);
  }

  private void createTransitionSection(FormToolkit toolkit, CTabFolder tabFolder,
      IWorkbenchPartSite site) {
    tbtmOnTrans = new CTabItem(tabFolder, SWT.NONE);
    tbtmOnTrans.setText("On Transition");

    compTrans = new Composite(tabFolder, SWT.NONE);
    compTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    tbtmOnTrans.setControl(compTrans);
    compTrans.setLayout(new GridLayout(1, false));
    support.createEntry(ON_TRANS_ID, compTrans);
  }

  private void addListeners() {

    priorityTxt.addVerifyListener(new DoubleVerifier());

    cmbTriggerType.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        triggerChanged();
      }
    });

    cmbMessageType.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        messageTypeChanged();
      }
    });

    btnIsDefaultOut.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        defaultOutChanged(btnIsDefaultOut.getSelection());
      }
    });
  }

  private void resetTabs() {
    if (!tbtmOnTrans.isDisposed()) {
      tbtmOnTrans.dispose();
    }

    if (tbtmTrigger.isDisposed()) {
      tbtmTrigger = new CTabItem(tabFolder, SWT.NONE);
      tbtmTrigger.setText("Trigger");
      tbtmTrigger.setControl(compTrigger);
    }

    if (tbtmGuard.isDisposed()) {
      tbtmGuard = new CTabItem(tabFolder, SWT.NONE);
      tbtmGuard.setText("Guard");
      tbtmGuard.setControl(compGuard);
    }

    if (tbtmOnTrans.isDisposed()) {
      tbtmOnTrans = new CTabItem(tabFolder, SWT.NONE);
      tbtmOnTrans.setText("On Transition");
      tbtmOnTrans.setControl(compTrans);
    }
    tabFolder.setSelection(0);
  }

  void defaultOutChanged(boolean isSelected) {
    // default out from branch cannot have any triggers
    if (transition != null && ((Transition) transition).isOutOfBranch()) {
      if (isSelected) {

        if (!tbtmTrigger.isDisposed()) {
          tbtmTrigger.dispose();
        }

        if (!tbtmGuard.isDisposed()) {
          tbtmGuard.dispose();
        }
        tabFolder.setSelection(0);
        cmbTriggerType.setEnabled(true);
      } else {
        // add them all so the order will stay the same
        resetTabs();

        // can only have a conditional trigger
        cmbTriggerType.select(CONDITION_INDEX);
        cmbTriggerType.setEnabled(false);
        triggerChanged();
      }
    } else {
      // this block handles when we've switched to a non choice transition
      // from a choice transition.
      if (tbtmTrigger.isDisposed()) {
        resetTabs();
      }
      cmbTriggerType.setEnabled(true);
      tabFolder.getTabList()[0].setEnabled(true);
      tbtmTrigger.setText("Trigger");

      tabFolder.getTabList()[1].setEnabled(true);
      tbtmGuard.setText("Guard");
    }
  }

  void doOutOfChoiceCheck() {
    boolean outOfChoice = (transition != null && ((Transition) transition).isOutOfBranch());
    btnIsDefaultOut.setVisible(outOfChoice);
  }

  void doSelfCheck() {
    if (transition != null) {
      if (transition.getFrom() != null && transition.getTo() != null)
        btnSelfTransition.setVisible(transition.getFrom().equals(transition.getTo()));
      else {
        btnSelfTransition.setVisible(false);
        btnSelfTransition.setSelection(false);
      }
    } else {
      btnSelfTransition.setSelection(false);
      btnSelfTransition.setVisible(false);
    }
  }

  private void messageTypeChanged() {
    MessageCheckerTypes type = MessageCheckerTypes.get(cmbMessageType.getSelectionIndex());
    StatechartCodeEditor editor = support.getEditor(TRIGGER_MESSAGE_ID);
    if (editor != null) {
      StyledText txtMessage = editor.getCodeViewer().getTextWidget();
      if (support.getEditor(TRIGGER_MESSAGE_ID).getEditorInput() != null && type != currentType) {
        String txt = txtMessage.getText();
        // input != null so message trigger type is active in the UI and
        // the type has been changed, so we need to reset the editor input
        // to appropriate template file.
        if (type == MessageCheckerTypes.EQUALS) {
          support.resetTriggerMEEditorInput(TRIGGER_MESSAGE_ID, transition);
        } else if (type == MessageCheckerTypes.CONDITIONAL) {
          support.resetTriggerMCEditorInput(TRIGGER_MESSAGE_ID, transition);
        }
        // the re-init clears the text so we add it back in here
        support.getEditor(TRIGGER_MESSAGE_ID).getCodeViewer().ignoreAutoIndent(true);
        txtMessage.setText(txt);
        support.getEditor(TRIGGER_MESSAGE_ID).getCodeViewer().ignoreAutoIndent(false);
      }

      txtMessage.setEnabled(!(type.equals(MessageCheckerTypes.ALWAYS) || type
          .equals(MessageCheckerTypes.UNCONDITIONAL)));
      if (!txtMessage.isEnabled()) {
        txtMessage.setText("");
      }
      
      Color color = (txtMessage.isEnabled() ? ColorConstants.black : Display.getDefault()
          .getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));

      lblMessage.setForeground(color);

      cmbMessageClass.setEnabled(!type.equals(MessageCheckerTypes.ALWAYS));
      color = (cmbMessageClass.isEnabled() ? ColorConstants.black : Display.getDefault()
          .getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
      lblMessageClass.setForeground(color);

      if (type.equals(MessageCheckerTypes.CONDITIONAL))
        lblMessage.setText("Condition:");
      else if (type.equals(MessageCheckerTypes.EQUALS))
        lblMessage.setText("Equals:");
      
      lblMessage.getParent().layout(true);
      currentType = type;
    }
  }

  private void triggerChanged() {
    int index = cmbTriggerType.getSelectionIndex();
    TriggerTypes ttype = TriggerTypeItem.getTriggerType(cmbTriggerType.getItem(index));
    initEditor(ttype);
    updateBinding(ttype);
    triggerLayout.topControl = triggerComps[index];
    triggerComps[index].getParent().layout();
    triggerComps[index].getParent().getParent().layout();

    if (cmbMessageClass.getSelectionIndex() == -1) {
      cmbMessageClass.select(0);
    }
    
    if (ttype.equals(TriggerTypes.MESSAGE)) {
      messageTypeChanged();
    }
  }

  private void initEditor(TriggerTypes ttype) {
    if (ttype.equals(TriggerTypes.CONDITION)) {
      // initialize the editor (create the styled text etc.) if its not
      // initialized and
      // create the template file if the editorInput is null
      support.initTriggerConditionEditor(TRIGGER_COND_ID, transition);
      bindEditorComponent(TRIGGER_COND_ID, TRANSITION__TRIGGER_CONDITION_CODE,
          TRANSITION__TRIGGER_CONDITION_CODE_IMPORTS);

    } else if (ttype.equals(TriggerTypes.EXPONENTIAL)) {
      support.initTriggerDblEditor(TRIGGER_EXP_ID, transition);
      bindEditorComponent(TRIGGER_EXP_ID, TRANSITION__TRIGGER_EXP_RATE_CODE,
          TRANSITION__TRIGGER_EXP_RATE_CODE_IMPORTS);

    } else if (ttype.equals(TriggerTypes.PROBABILITY)) {
      support.initTriggerDblEditor(TRIGGER_PROB_ID, transition);
      bindEditorComponent(TRIGGER_PROB_ID, TRANSITION__TRIGGER_PROB_CODE,
          TRANSITION__TRIGGER_PROB_CODE_IMPORTS);

    } else if (ttype.equals(TriggerTypes.TIMED)) {
      support.initTriggerDblEditor(TRIGGER_TIME_ID, transition);
      bindEditorComponent(TRIGGER_TIME_ID, TRANSITION__TRIGGER_TIMED_CODE,
          TRANSITION__TRIGGER_TIMED_CODE_IMPORTS);

    } else if (ttype.equals(TriggerTypes.MESSAGE)) {
      MessageCheckerTypes type = MessageCheckerTypes.get(cmbMessageType.getSelectionIndex());
      if (type == MessageCheckerTypes.EQUALS) {
        support.initTriggerMEEditor(TRIGGER_MESSAGE_ID, transition);
      } else if (type == MessageCheckerTypes.CONDITIONAL) {
        support.initTriggerMCEditor(TRIGGER_MESSAGE_ID, transition);
      }
      bindEditorComponent(TRIGGER_MESSAGE_ID, TRANSITION__MESSAGE_CHECKER_CODE,
          TRANSITION__MESSAGE_CHECKER_CODE_IMPORTS);
    }
  }

  private void updateBinding(TriggerTypes ttype) {
    if (bindingContext != null) {
      if (pollingBinding != null)
        bindingContext.removeBinding(pollingBinding);

      Text txt = null;
      if (ttype.equals(TriggerTypes.CONDITION))
        txt = txtCondPolling;
      else if (ttype.equals(TriggerTypes.ALWAYS))
        txt = txtAlwaysPolling;
      else if (ttype.equals(TriggerTypes.PROBABILITY))
        txt = txtProbPolling;
      else if (ttype.equals(TriggerTypes.MESSAGE))
        txt = txtMessagePolling;

      if (txt != null) {
        pollingBinding = bindTextField(txt, StatechartPackage.Literals.TRANSITION__TRIGGER_TIME,
            createUpdateValueStrategy(new StringToDoubleConverter()),
            createUpdateValueStrategy(new DoubleToStringConverter()));
      }
    }
  }

  private UpdateValueStrategy createUpdateValueStrategy(IConverter converter) {
    UpdateValueStrategy strategy = new UpdateValueStrategy();
    strategy.setConverter(converter);
    return strategy;
  }

  public void dispose() {
    super.dispose();
    try {
      transition.eAdapters().remove(langNotify);
      support.dispose();
    } catch (CoreException ex) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editor", ex);
    }
  }

  private void languageChanged() {
    LanguageTypes newLang = buttonGroup.getSelectedType();
    // only switch if switching from Java to something else
    // switching between Groovy and ReLogo doesn't require an editor switch
    if (newLang != language && (newLang == LanguageTypes.JAVA || language == LanguageTypes.JAVA)) {
      
      support.resetTriggerDblEditor(TRIGGER_TIME_ID, transition);
      support.resetTriggerDblEditor(TRIGGER_EXP_ID, transition);
      support.resetTriggerDblEditor(TRIGGER_PROB_ID, transition);
      support.resetTriggerConditionEditor(TRIGGER_COND_ID, transition);
      MessageCheckerTypes type = MessageCheckerTypes.get(cmbMessageType.getSelectionIndex());
      if (type == MessageCheckerTypes.EQUALS)
        support.resetTriggerMEEditor(TRIGGER_MESSAGE_ID, transition);
      else if (type == MessageCheckerTypes.CONDITIONAL)
        support.resetTriggerMCEditor(TRIGGER_MESSAGE_ID, transition);
      
      int index = cmbTriggerType.getSelectionIndex();
      TriggerTypes ttype = TriggerTypeItem.getTriggerType(cmbTriggerType.getItem(index));
      if (ttype == TriggerTypes.MESSAGE) {
        messageTypeChanged();
      }

      support.resetGuardEditor(GUARD_ID, transition);
      support.resetOnTransEditor(ON_TRANS_ID, transition);

      bindings.removeBindings();
      StatechartCodeEditor editor = support.getEditor(ON_TRANS_ID);
      bindings.bind(StatechartPackage.Literals.TRANSITION__ON_TRANSITION, editor.getCodeViewer());
      bindings.bind(StatechartPackage.Literals.TRANSITION__ON_TRANSITION_IMPORTS,
          editor.getImportViewer());

      editor = support.getEditor(GUARD_ID);
      bindings.bind(StatechartPackage.Literals.TRANSITION__GUARD, editor.getCodeViewer());
      bindings.bind(StatechartPackage.Literals.TRANSITION__GUARD_IMPORTS, editor.getImportViewer());
      triggerChanged();
    }
    language = newLang;
  }

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    if (transition != null && !eObject.equals(transition)) {
      transition.eAdapters().remove(langNotify);
      if (transition.getTriggerCodeLanguage() != ((Transition)eObject).getTriggerCodeLanguage()) {
        // if the language is different the dispose of the editors
        // and start again.
        try {
          support.disposeAllEditors();
        } catch (CoreException e) {
          StatechartDiagramEditorPlugin.getInstance().logError("Error while disposing of editors", e);
        }
      }
    }
    
    transition = (Transition) eObject;
    language = transition.getTriggerCodeLanguage();
    transition.eAdapters().add(langNotify);
    
    bindingContext = context;
    pollingBinding = null;
    
    bindTextField(idTxt, StatechartPackage.Literals.TRANSITION__ID);

    support.initGuardEditor(GUARD_ID, transition);
    support.initOnTransEditor(ON_TRANS_ID, transition);

    bindings = new BindingSupport(context, eObject);
    StatechartCodeEditor editor = support.getEditor(ON_TRANS_ID);
    bindings.bind(StatechartPackage.Literals.TRANSITION__ON_TRANSITION, editor.getCodeViewer());
    bindings.bind(StatechartPackage.Literals.TRANSITION__ON_TRANSITION_IMPORTS,
        editor.getImportViewer());

    editor = support.getEditor(GUARD_ID);
    bindings.bind(StatechartPackage.Literals.TRANSITION__GUARD, editor.getCodeViewer());
    bindings.bind(StatechartPackage.Literals.TRANSITION__GUARD_IMPORTS, editor.getImportViewer());

    bindTextField(priorityTxt, StatechartPackage.Literals.TRANSITION__PRIORITY,
        createUpdateValueStrategy(new StringToDoubleConverter()),
        createUpdateValueStrategy(new DoubleToStringConverter()));

    bindOtherComponents(context, eObject);
    triggerChanged();
    messageTypeChanged();

    buttonGroup.bindModel(context, eObject,
        StatechartPackage.Literals.TRANSITION__TRIGGER_CODE_LANGUAGE);

    context.bindValue(
        WidgetProperties.selection().observe(btnIsDefaultOut),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__DEFAULT_TRANSITION).observe(eObject));
    doOutOfChoiceCheck();
    defaultOutChanged(((Transition) eObject).isDefaultTransition());

    context.bindValue(
        WidgetProperties.selection().observe(btnSelfTransition),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__SELF_TRANSITION).observe(eObject));
    doSelfCheck();

    for (int i = 0; i < tabFolder.getTabList().length; ++i) {
      if (tabFolder.getTabList()[i].isEnabled()) {
        tabFolder.setSelection(i);
        break;
      }
    }
  }

  private void bindOtherComponents(EMFDataBindingContext context, EObject eObject) {
    UpdateValueStrategy targetToModel = new UpdateValueStrategy();
    targetToModel.setConverter(new StringToTriggerType());
    UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
    modelToTarget.setConverter(new TriggerTypeToString());
    context.bindValue(
        WidgetProperties.selection().observe(cmbTriggerType),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__TRIGGER_TYPE).observe(eObject), targetToModel,
        modelToTarget);

    context.bindValue(
        WidgetProperties.selection().observe(cmbMessageType),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_TYPE).observe(eObject),
        createUpdateValueStrategy(new StringToMessageType()),
        createUpdateValueStrategy(new MessageTypeToString()));

    context.bindValue(
        WidgetProperties.text().observe(cmbMessageClass),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CLASS).observe(eObject));
  }

  private void bindEditorComponent(String editorId, EStructuralFeature codeFeature,
      EStructuralFeature importFeature) {
    if (!bindings.isBound(codeFeature)) {
      StatechartCodeEditor editor = support.getEditor(editorId);
      bindings.bind(codeFeature, editor.getCodeViewer());
      bindings.bind(importFeature, editor.getImportViewer());
    }
  }

  private Binding bindTextField(Text text, EAttribute attribute) {
    return bindingContext.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, text),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(transition), attribute).observe(
            transition));
  }

  private Binding bindTextField(Text text, EAttribute attribute, UpdateValueStrategy targetToModel,
      UpdateValueStrategy modelToTarget) {
    return bindingContext.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, text),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(transition), attribute).observe(
            transition), targetToModel, modelToTarget);
  }

  private static class TriggerTypeToString implements IConverter {

    @Override
    public Object getFromType() {
      return TriggerTypes.class;
    }

    @Override
    public Object getToType() {
      return String.class;
    }

    @Override
    public Object convert(Object fromObject) {
      return TriggerTypeItem.getName((TriggerTypes) fromObject);
    }

  }

  private static class StringToTriggerType implements IConverter {

    @Override
    public Object getFromType() {
      return String.class;
    }

    @Override
    public Object getToType() {
      return TriggerTypes.class;
    }

    @Override
    public Object convert(Object fromObject) {
      return TriggerTypeItem.getTriggerType(fromObject.toString());
    }
  }

  private static class MessageTypeToString implements IConverter {

    @Override
    public Object getFromType() {
      return MessageCheckerTypes.class;
    }

    @Override
    public Object getToType() {
      return String.class;
    }

    @Override
    public Object convert(Object fromObject) {
      int index = ((MessageCheckerTypes) fromObject).getValue();
      return TransitionSheet.MESSAGE_TYPES[index];
    }
  }

  private static class StringToMessageType implements IConverter {

    @Override
    public Object getFromType() {
      return String.class;
    }

    @Override
    public Object getToType() {
      return MessageCheckerTypes.class;
    }

    @Override
    public Object convert(Object fromObject) {
      for (int i = 0; i < MESSAGE_TYPES.length; i++) {
        if (MESSAGE_TYPES[i].equals(fromObject.toString())) {
          return MessageCheckerTypes.get(i);
        }
      }
      return null;
    }
  }
}
