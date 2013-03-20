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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.util.SDModelUtils;

/**
 * Property sheet for Stock variables.
 * 
 * @author Nick Collier
 */
public class StockSheet extends VariableSheet {
  
  private class WhoHadFocus extends FocusAdapter {

    @Override
    public void focusGained(FocusEvent e) {
      if (e.getSource().equals(txtInitVal)) initValHadFocus = true;
      else if (e.getSource().equals(txtEquation)) initValHadFocus = false;
    }
  }
  

  private StyledText txtInitVal;
  private boolean initValHadFocus = false;

  public StockSheet(FormToolkit toolkit, Composite parent) {
    super(toolkit, parent);
    WhoHadFocus who = new WhoHadFocus();
    txtInitVal.addFocusListener(who);
    txtEquation.addFocusListener(who);
  }

  
  @Override
  protected void createEquation(Composite parent, FormToolkit toolkit) {
    super.createEquation(parent, toolkit);
    lblEq.setText(" = \nINTEG(");
    Label lblNewLabel = new Label(parent, SWT.NONE);
    toolkit.adapt(lblNewLabel, true, true);
    lblNewLabel.setText("Initial\nValue:");
    
    txtInitVal = new StyledText(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
    txtInitVal.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.BOLD));
    txtInitVal.setTopMargin(4);
    txtInitVal.setLeftMargin(4);
    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    gd.heightHint = 25;
    txtInitVal.setLayoutData(gd);
    //txtInitVal.addVerifyListener(new DoubleVerifier());
    gd.heightHint = 25;
    //Bug383650Fix.applyFix(txtInitVal);
  }

  @Override
  protected StyledText getEquationControl() {
    if (initValHadFocus) return txtInitVal;
    return txtEquation;
  }

  @Override
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    super.bindModel(context, eObject);
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(200, txtInitVal),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            SDModelPackage.Literals.STOCK__INITIAL_VALUE).observe(eObject));
        //createUpdateValueStrategy(new StringToDoubleConverter()),
        //createUpdateValueStrategy(new DoubleToStringConverter()));
  }

  @Override
  protected void updateVariables(EObject eObj) {
    super.updateVariables(eObj);
    Stock stock = ((Stock) eObj);

    java.util.List<Rate> ins = SDModelUtils.getIncomingRates(stock);
    java.util.List<Rate> outs = SDModelUtils.getOutgoingRates(stock);
    java.util.List<Variable> vars = SDModelUtils.getIncomingVariables(stock);

    for (Rate rate : ins) {
      varList.add(rate.getName());
      varMap.put(rate.getName(), rate);
    }
    for (Rate rate : outs) {
      varList.add(rate.getName());
      varMap.put(rate.getName(), rate);
    }

    if (stock.getEquation().trim().length() == 0 && ins.size() == 1 && outs.size() == 1) {
      txtEquation.setText(ins.get(0).getName() + " - " + outs.get(0).getName());
    }
    
    for (Variable var : vars) {
      varList.add(var.getName());
      varMap.put(var.getName(), var);
    }

    lstVar.setItems(varList.toArray(new String[0]));
  }
}
