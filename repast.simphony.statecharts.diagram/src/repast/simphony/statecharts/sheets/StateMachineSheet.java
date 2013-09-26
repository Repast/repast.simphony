package repast.simphony.statecharts.sheets;

import org.eclipse.core.databinding.UpdateValueStrategy;
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

import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * Property sheet for editing the root statechart properties.
 * 
 * @author Nick Collier
 */
public class StateMachineSheet extends Composite implements BindableFocusableSheet {

  private Text idTxt;
  private LanguageButtonsGroup buttonGroup;

  private Text txtClass;
  private Text txtPackage;
  private Text txtAgent;
  private Text priorityTxt;

  public StateMachineSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NONE);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(1, false));

    Composite composite_2 = new Composite(this, SWT.NONE);
    toolkit.adapt(composite_2);
    toolkit.paintBordersFor(composite_2);
    composite_2.setLayout(new GridLayout(4, false));

    Label lblId = new Label(composite_2, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("Name:");

    idTxt = new Text(composite_2, SWT.BORDER);
    GridData gd_idFld = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_idFld.widthHint = 200;
    idTxt.setLayoutData(gd_idFld);
    toolkit.adapt(idTxt, true, true);

    Label lblPriority = new Label(composite_2, SWT.NONE);
    lblPriority.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblPriority, true, true);
    lblPriority.setText("Priority:");

    priorityTxt = new Text(composite_2, SWT.BORDER);
    GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_text.widthHint = 60;
    priorityTxt.setLayoutData(gd_text);
    toolkit.adapt(priorityTxt, true, true);
    Bug383650Fix.applyFix(priorityTxt);
    priorityTxt.addVerifyListener(new DoubleVerifier());

    Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(label, true, true);

    Composite composite = new Composite(this, SWT.NONE);
    GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    gd_composite.heightHint = 183;
    composite.setLayoutData(gd_composite);
    GridLayout gl_composite = new GridLayout(2, false);
    gl_composite.verticalSpacing = 8;
    gl_composite.horizontalSpacing = 4;
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
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
    gd_composite_1.widthHint = 353;
    composite_1.setLayoutData(gd_composite_1);
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);

    Label lblLanguage = new Label(composite_1, SWT.NONE);
    lblLanguage.setBounds(0, 0, 59, 14);
    toolkit.adapt(lblLanguage, true, true);
    lblLanguage.setText("Default Language:");
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
    lblOnEnter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblOnEnter, true, true);
    lblOnEnter.setText("Class Name:");

    txtClass = new Text(composite, SWT.BORDER);
    txtClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtClass, true, true);
    Bug383650Fix.applyFix(txtClass);

    Label lblAgent = new Label(composite, SWT.NONE);
    lblAgent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblAgent, true, true);
    lblAgent.setText("Package:");

    txtPackage = new Text(composite, SWT.BORDER);
    txtPackage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtPackage, true, true);
    Bug383650Fix.applyFix(txtPackage);

    Label lblAgentType = new Label(composite, SWT.NONE);
    lblAgentType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblAgentType, true, true);
    lblAgentType.setText("Agent Class:");

    txtAgent = new Text(composite, SWT.BORDER);
    txtAgent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtAgent, true, true);
    Bug383650Fix.applyFix(txtAgent);
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
        StatechartPackage.Literals.STATE_MACHINE__ID);
    ISWTObservableValue observe = WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(
        400, idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    UpdateValueStrategy mToT = new UpdateValueStrategy();
    mToT.setConverter(new DoubleToStringConverter());
    UpdateValueStrategy tToM = new UpdateValueStrategy();
    tToM.setConverter(new StringToDoubleConverter());
    context.bindValue(WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, priorityTxt),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.STATE_MACHINE__PRIORITY).observe(eObject), tToM, mToT);

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, txtClass),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.STATE_MACHINE__CLASS_NAME).observe(eObject));

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, txtPackage),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.STATE_MACHINE__PACKAGE).observe(eObject));

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, txtAgent),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.STATE_MACHINE__AGENT_TYPE).observe(eObject));

    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.STATE_MACHINE__LANGUAGE);
  }
}
