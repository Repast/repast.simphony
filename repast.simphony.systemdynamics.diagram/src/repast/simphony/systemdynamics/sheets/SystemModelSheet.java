package repast.simphony.systemdynamics.sheets;

import java.util.Collections;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.util.SDModelUtils;

public class SystemModelSheet extends Composite {

  private Combo cmbUnits;
  private Text txtStart;
  private Text txtEnd;
  private Text txtStep, txtInterval;
  private Text txtClassName;
  private Text txtPackage;

  public SystemModelSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.H_SCROLL);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    GridLayout gridLayout = new GridLayout(2, false);
    setLayout(gridLayout);

    Composite composite = toolkit.createComposite(this, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    toolkit.paintBordersFor(composite);

    toolkit.createLabel(composite, "Model Properties", SWT.NONE);

    Label lblFoo = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
    lblFoo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(lblFoo, true, true);

    Composite composite_2 = toolkit.createComposite(this, SWT.NONE);
    GridLayout gl_composite_2 = new GridLayout(2, false);
    gl_composite_2.marginLeft = 6;
    composite_2.setLayout(gl_composite_2);
    composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    toolkit.paintBordersFor(composite_2);

    Label lblStartTime = new Label(composite_2, SWT.NONE);
    lblStartTime.setSize(63, 14);
    toolkit.adapt(lblStartTime, true, true);
    lblStartTime.setText("Start Time:");

    txtStart = new Text(composite_2, SWT.BORDER);
    txtStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtStart.setSize(325, 19);
    toolkit.adapt(txtStart, true, true);
    txtStart.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtStart);

    Label lblEndTime = new Label(composite_2, SWT.NONE);
    lblEndTime.setSize(58, 14);
    toolkit.adapt(lblEndTime, true, true);
    lblEndTime.setText("End Time:");

    txtEnd = new Text(composite_2, SWT.BORDER);
    txtEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    txtEnd.setSize(325, 19);
    toolkit.adapt(txtEnd, true, true);
    txtEnd.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtEnd);

    Label lblTimeStep = new Label(composite_2, SWT.NONE);
    lblTimeStep.setSize(110, 14);
    toolkit.adapt(lblTimeStep, true, true);
    lblTimeStep.setText("Integration Interval:");

    txtStep = new Text(composite_2, SWT.BORDER);
    txtStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(txtStep, true, true);
    txtStep.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtStep);

    Label lblInterval = new Label(composite_2, SWT.NONE);
    lblInterval.setSize(103, 14);
    toolkit.adapt(lblInterval, true, true);
    lblInterval.setText("Reporting Interval:");

    txtInterval = new Text(composite_2, SWT.BORDER);
    txtInterval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(txtInterval, true, true);
    txtInterval.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtInterval);

    Label lblUnits = new Label(composite_2, SWT.NONE);
    lblUnits.setSize(35, 14);
    toolkit.adapt(lblUnits, true, true);
    lblUnits.setText("Units:");

    cmbUnits = new Combo(composite_2, SWT.NONE);
    cmbUnits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(cmbUnits);
    toolkit.paintBordersFor(cmbUnits);

    Composite composite_1 = toolkit.createComposite(this, SWT.NONE);
    composite_1.setLayout(new GridLayout(2, false));
    composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    toolkit.paintBordersFor(composite_1);

    toolkit.createLabel(composite_1, "Code Properties", SWT.NONE);

    Label label = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(label, true, true);
    new Label(this, SWT.NONE);

    Composite composite_3 = toolkit.createComposite(this, SWT.NONE);
    composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    toolkit.paintBordersFor(composite_3);
    composite_3.setLayout(new GridLayout(2, false));

    Label lblClassName = toolkit.createLabel(composite_3, "Class Name:", SWT.NONE);
    lblClassName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

    txtClassName = toolkit.createText(composite_3, "New Text", SWT.NONE);
    txtClassName.setText("");
    txtClassName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Bug383650Fix.applyFix(txtClassName);

    Label lblPackage = toolkit.createLabel(composite_3, "Package:", SWT.NONE);
    lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

    txtPackage = toolkit.createText(composite_3, "New Text", SWT.NONE);
    txtPackage.setText("");
    txtPackage.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    Bug383650Fix.applyFix(txtPackage);
  }

  protected void bind(EMFDataBindingContext context, EObject eObject, EAttribute attribute,
      Widget widget, boolean isDouble) {
    if (isDouble) {
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, widget),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), attribute).observe(
            eObject), createUpdateValueStrategy(new StringToDoubleConverter()),
        createUpdateValueStrategy(new DoubleToStringConverter()));
    } else {
      context.bindValue(
          WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, widget),
          EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), attribute).observe(
              eObject));
    }
  }

  protected UpdateValueStrategy createUpdateValueStrategy(IConverter converter) {
    UpdateValueStrategy strategy = new UpdateValueStrategy();
    strategy.setConverter(converter);
    return strategy;
  }

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    java.util.List<String> allUnits = SDModelUtils.getAllUnits(eObject);
    Collections.sort(allUnits);
    cmbUnits.setItems(allUnits.toArray(new String[0]));

    context.bindValue(
        WidgetProperties.text().observe(cmbUnits),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            SDModelPackage.Literals.SYSTEM_MODEL__UNITS).observe(eObject));

    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__START_TIME, txtStart, true);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__END_TIME, txtEnd, true);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__TIME_STEP, txtStep, true);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__REPORTING_INTERVAL, txtInterval, true);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__CLASS_NAME, txtClassName, false);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__PACKAGE, txtPackage, false);
  }
}
