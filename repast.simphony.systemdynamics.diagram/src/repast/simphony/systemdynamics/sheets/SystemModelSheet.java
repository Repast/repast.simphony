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
  private Text txtStep;

  public SystemModelSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.H_SCROLL);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(2, false));
    
    Label lblStartTime = new Label(this, SWT.NONE);
    lblStartTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblStartTime, true, true);
    lblStartTime.setText("Start Time:");
    
    txtStart = new Text(this, SWT.BORDER);
    txtStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtStart, true, true);
    txtStart.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtStart);
    
    Label lblEndTime = new Label(this, SWT.NONE);
    lblEndTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblEndTime, true, true);
    lblEndTime.setText("End Time:");
    
    txtEnd = new Text(this, SWT.BORDER);
    txtEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtEnd, true, true);
    txtEnd.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtEnd);
    
    Label lblTimeStep = new Label(this, SWT.NONE);
    lblTimeStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblTimeStep, true, true);
    lblTimeStep.setText("Time Step:");
    
    txtStep = new Text(this, SWT.BORDER);
    txtStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtStep, true, true);
    txtStep.addVerifyListener(new DoubleVerifier());
    Bug383650Fix.applyFix(txtStep);
    
    Label lblUnits = new Label(this, SWT.NONE);
    lblUnits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblUnits, true, true);
    lblUnits.setText("Units:");
    
    cmbUnits = new Combo(this, SWT.NONE);
    GridData gd_cmbUnits = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbUnits.widthHint = 200;
    cmbUnits.setLayoutData(gd_cmbUnits);
    toolkit.adapt(cmbUnits);
    toolkit.paintBordersFor(cmbUnits);
  }
  
  protected void bind(EMFDataBindingContext context, EObject eObject, EAttribute attribute,
      Widget widget) {
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, widget),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), attribute).observe(
            eObject), createUpdateValueStrategy(new StringToDoubleConverter()),
            createUpdateValueStrategy(new DoubleToStringConverter()));
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
    
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__START_TIME, txtStart);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__END_TIME, txtEnd);
    bind(context, eObject, SDModelPackage.Literals.SYSTEM_MODEL__TIME_STEP, txtStep);
  }

  
}
