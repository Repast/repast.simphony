package repast.simphony.statecharts.sheets;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.TriggerTypes;
import org.eclipse.wb.swt.SWTResourceManager;

public class TransitionSheet extends FocusFixComposite implements BindableFocusableSheet {

  private static final String[] MESSAGE_TYPES = { "When Message Meets a Condition",
      "When Message Equals ...", "When Message is of the Specified Class", "Always" };

  private static final String[] CLASS_TYPES = { "String", "int", "long", "float", "double",
      "boolean" };

  private static final int CONDITION_INDEX = 3;

  private Text idTxt, onTransitionTxt, guardTxt;
  private LanguageButtonsGroup buttonGroup;
  private Combo cmbTriggerType;
  private Text priorityTxt;
  private Text txtAlwaysPolling;
  private Text txtTimedTime, txtExpRate, txtProbProb, txtProbPolling, txtCondPolling, txtCondCond,
      txtMessagePolling;

  private EMFDataBindingContext bindingContext;
  private Binding pollingBinding;
  private EObject object;

  private StackLayout triggerLayout;
  private Composite[] triggerComps = new Composite[6];
  private Text txtMessage;
  private Label lblMessage, lblMessageClass;
  private Combo cmbMessageType, cmbMessageClass;

  private Button btnIsDefaultOut;
  private Button btnSelfTransition;

  private CTabItem tbtmTrigger;

  private CTabItem tbtmGuard;

  private CTabItem tbtmOnTrans;

  private CTabFolder tabFolder;

  private Composite compTrigger;

  private Composite compGuard;

  private Composite compTrans;

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

    createTriggerSection(toolkit, tabFolder);
    createGuardSection(toolkit, tabFolder);
    createTransitionSection(toolkit, tabFolder);

    cmbTriggerType.select(0);
    cmbMessageType.select(3);
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

    Button btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");

    Button btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");

    buttonGroup = new LanguageButtonsGroup(btnJava, btnRelogo, btnGroovy);
  }

  private void createGuardSection(FormToolkit toolkit, CTabFolder tabFolder) {
    tbtmGuard = new CTabItem(tabFolder, SWT.NONE);
    tbtmGuard.setText("Guard");

    compGuard = new Composite(tabFolder, SWT.NONE);
    tbtmGuard.setControl(compGuard);
    GridLayout gl_composite = new GridLayout(2, false);
    gl_composite.marginHeight = 3;
    compGuard.setLayout(gl_composite);
    toolkit.adapt(compGuard);
    toolkit.paintBordersFor(compGuard);

    guardTxt = new Text(compGuard, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    focusableControls.add(guardTxt);
    guardTxt.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_onExitTxt = new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1);
    gd_onExitTxt.heightHint = 140;
    guardTxt.setLayoutData(gd_onExitTxt);
    toolkit.adapt(guardTxt, true, true);
  }

  private void createTriggerSection(FormToolkit toolkit, CTabFolder tabFolder) {

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
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    toolkit.paintBordersFor(composite);

    createAlwaysComp(composite, toolkit);
    createTimeComp(composite, toolkit);
    createExpRateComp(composite, toolkit);
    createProbComp(composite, toolkit);
    createConditionComp(composite, toolkit);
    createMessageComp(composite, toolkit);
  }

  private void createMessageComp(Composite parent, FormToolkit toolkit) {
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
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
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
    cmbMessageClass.select(0);
    cmbMessageClass.setLayoutData(gd_cmbMessageClass);
    toolkit.adapt(cmbMessageClass);
    toolkit.paintBordersFor(cmbMessageClass);

    lblMessage = toolkit.createLabel(composite, "Condition:", SWT.NONE);

    txtMessage = toolkit.createText(composite, "New Text", SWT.V_SCROLL | SWT.MULTI);
    focusableControls.add(txtMessage);
    txtMessage.addTraverseListener(new CancelTraverseOnReturn());
    txtMessage.setText("");
    GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
    gd_text_1.heightHint = 40;
    txtMessage.setLayoutData(gd_text_1);
  }

  private void createProbComp(Composite parent, FormToolkit toolkit) {
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
    new Label(cmpProb, SWT.NONE);
    txtProbProb = toolkit.createText(cmpProb, "", SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
        | SWT.MULTI);
    focusableControls.add(txtProbProb);
    txtProbProb.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_txtTimedTime = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
    gd_txtTimedTime.heightHint = 40;
    txtProbProb.setLayoutData(gd_txtTimedTime);
  }

  private void createConditionComp(Composite parent, FormToolkit toolkit) {
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
    new Label(cmpCond, SWT.NONE);
    txtCondCond = toolkit.createText(cmpCond, "", SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
        | SWT.MULTI);
    focusableControls.add(txtCondCond);
    txtCondCond.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_txtTimedTime = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
    gd_txtTimedTime.heightHint = 40;
    txtCondCond.setLayoutData(gd_txtTimedTime);
  }

  private void createTimeComp(Composite parent, FormToolkit toolkit) {
    Composite cmpTimed = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[1] = cmpTimed;
    toolkit.paintBordersFor(cmpTimed);
    cmpTimed.setLayout(new GridLayout(1, false));

    toolkit.createLabel(cmpTimed, "Time:", SWT.NONE);
    txtTimedTime = toolkit.createText(cmpTimed, "", SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
        | SWT.MULTI);
    focusableControls.add(txtTimedTime);
    txtTimedTime.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_txtTimedTime = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
    gd_txtTimedTime.heightHint = 40;
    txtTimedTime.setLayoutData(gd_txtTimedTime);
  }

  private void createExpRateComp(Composite parent, FormToolkit toolkit) {
    Composite cmpExp = toolkit.createComposite(parent, SWT.NONE);
    triggerComps[4] = cmpExp;
    toolkit.paintBordersFor(cmpExp);
    cmpExp.setLayout(new GridLayout(1, false));

    toolkit.createLabel(cmpExp, "Exponential Decay Rate:", SWT.NONE);
    txtExpRate = toolkit.createText(cmpExp, "", SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
        | SWT.MULTI);
    focusableControls.add(txtExpRate);
    txtExpRate.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_txtTimedTime = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
    gd_txtTimedTime.heightHint = 40;
    txtExpRate.setLayoutData(gd_txtTimedTime);
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

  private void createTransitionSection(FormToolkit toolkit, CTabFolder tabFolder) {
    tbtmOnTrans = new CTabItem(tabFolder, SWT.NONE);
    tbtmOnTrans.setText("On Transition");

    compTrans = new Composite(tabFolder, SWT.NONE);
    compTrans.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    tbtmOnTrans.setControl(compTrans);
    compTrans.setLayout(new GridLayout(1, false));

    onTransitionTxt = new Text(compTrans, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL
        | SWT.CANCEL | SWT.MULTI);
    focusableControls.add(onTransitionTxt);
    onTransitionTxt.addTraverseListener(new CancelTraverseOnReturn());
    GridData gd_onTransitionTxt = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
    gd_onTransitionTxt.heightHint = 140;
    onTransitionTxt.setLayoutData(gd_onTransitionTxt);
    onTransitionTxt.setText("");
    toolkit.adapt(onTransitionTxt, true, true);
  }

  private void addListeners() {

    priorityTxt.addVerifyListener(new DoubleVerifier());

    guardTxt.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          e.detail = SWT.TRAVERSE_NONE;
        }
      }
    });

    onTransitionTxt.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          e.detail = SWT.TRAVERSE_NONE;
        }
      }
    });

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

    /*
     * sctnGuard.addExpansionListener(new IExpansionListener() {
     * 
     * @Override public void expansionStateChanging(ExpansionEvent e) {}
     * 
     * @Override public void expansionStateChanged(ExpansionEvent e) {
     * sctnGuard.getParent().s } });
     */
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
    if (object != null && ((Transition) object).isOutOfBranch()) {
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
    boolean outOfChoice = (object != null && ((Transition) object).isOutOfBranch());
    btnIsDefaultOut.setVisible(outOfChoice);
  }

  void doSelfCheck() {
    if (object != null) {
      Transition transition = (Transition) object;
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
    txtMessage.setEnabled(!(type.equals(MessageCheckerTypes.ALWAYS) || type
        .equals(MessageCheckerTypes.UNCONDITIONAL)));
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
  }

  private void triggerChanged() {
    int index = cmbTriggerType.getSelectionIndex();
    TriggerTypes ttype = TriggerTypeItem.getTriggerType(cmbTriggerType.getItem(index));
    updateBinding(ttype);
    triggerLayout.topControl = triggerComps[index];
    triggerComps[index].getParent().layout();
    triggerComps[index].getParent().getParent().layout();
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

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    bindingContext = context;
    pollingBinding = null;
    object = eObject;

    bindTextField(idTxt, StatechartPackage.Literals.TRANSITION__ID);
    bindTextField(onTransitionTxt, StatechartPackage.Literals.TRANSITION__ON_TRANSITION);
    bindTextField(guardTxt, StatechartPackage.Literals.TRANSITION__GUARD);

    bindTextField(priorityTxt, StatechartPackage.Literals.TRANSITION__PRIORITY,
        createUpdateValueStrategy(new StringToDoubleConverter()),
        createUpdateValueStrategy(new DoubleToStringConverter()));

    bindTriggerComponents(context, eObject);
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

  private void bindTriggerComponents(EMFDataBindingContext context, EObject eObject) {
    UpdateValueStrategy targetToModel = new UpdateValueStrategy();
    targetToModel.setConverter(new StringToTriggerType());
    UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
    modelToTarget.setConverter(new TriggerTypeToString());
    context.bindValue(
        WidgetProperties.selection().observe(cmbTriggerType),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.TRANSITION__TRIGGER_TYPE).observe(eObject), targetToModel,
        modelToTarget);

    bindTextField(txtTimedTime, StatechartPackage.Literals.TRANSITION__TRIGGER_TIMED_CODE);
    bindTextField(txtExpRate, StatechartPackage.Literals.TRANSITION__TRIGGER_EXP_RATE_CODE);
    bindTextField(txtProbProb, StatechartPackage.Literals.TRANSITION__TRIGGER_PROB_CODE);
    bindTextField(txtCondCond, StatechartPackage.Literals.TRANSITION__TRIGGER_CONDITION_CODE);

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

    bindTextField(txtMessage, StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CODE);
  }

  private Binding bindTextField(Text text, EAttribute attribute) {
    return bindingContext.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, text),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(object), attribute)
            .observe(object));
  }

  private Binding bindTextField(Text text, EAttribute attribute, UpdateValueStrategy targetToModel,
      UpdateValueStrategy modelToTarget) {
    return bindingContext.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, text),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(object), attribute)
            .observe(object), targetToModel, modelToTarget);
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

  private static class CancelTraverseOnReturn implements TraverseListener {
    public void keyTraversed(TraverseEvent e) {
      if (e.detail == SWT.TRAVERSE_RETURN) {
        e.doit = false;
        e.detail = SWT.TRAVERSE_NONE;
      }
    }
  }
}
