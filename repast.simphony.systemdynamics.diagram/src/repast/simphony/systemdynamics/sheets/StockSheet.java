/**
 * 
 */
package repast.simphony.systemdynamics.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.util.SDModelUtils;

/**
 * Property sheet for Stock variables.
 * 
 * @author Nick Collier
 */
public class StockSheet extends VariableSheet {

  private Text txtInitVal;

  public StockSheet(FormToolkit toolkit, Composite parent) {
    super(toolkit, parent);
  }

  @Override
  protected void addHeader(FormToolkit toolkit, Composite parent) {
    super.addHeader(toolkit, parent);
    Label lblInitVal = new Label(parent, SWT.NONE);
    toolkit.adapt(lblInitVal, true, true);
    lblInitVal.setText("Initial Value:");

    txtInitVal = new Text(parent, SWT.BORDER);
    GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
    gd.widthHint = 140;
    txtInitVal.setLayoutData(gd);
    txtInitVal.addVerifyListener(new DoubleVerifier());
    toolkit.adapt(txtInitVal, true, true);
    Bug383650Fix.applyFix(txtInitVal);
  }

  @Override
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    super.bindModel(context, eObject);
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(200, txtInitVal),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            SDModelPackage.Literals.STOCK__INITIAL_VALUE).observe(eObject),
        createUpdateValueStrategy(new StringToDoubleConverter()),
        createUpdateValueStrategy(new DoubleToStringConverter()));
  }

  @Override
  protected void updateVariables(EObject eObj) {
    Stock stock = ((Stock) eObj);
    lstVar.setItems(new String[] {});
    java.util.List<Rate> ins = SDModelUtils.getIncomingRates(stock);
    java.util.List<Rate> outs = SDModelUtils.getOutgoingRates(stock);
    String[] items = new String[ins.size() + outs.size()];

    int i = 0;
    for (Rate rate : ins) {
      items[i++] = rate.getName();
    }
    for (Rate rate : outs) {
      items[i++] = rate.getName();
    }
    lstVar.setItems(items);

    if (stock.getEquation().trim().length() == 0 && ins.size() == 1 && outs.size() == 1) {
      txtEquation.setText(ins.get(0).getName() + " - " + outs.get(0).getName());
    }
  }
}
