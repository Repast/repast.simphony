package repast.simphony.statecharts.sheets;

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

public class HistoryStateSheet extends Composite {
  
  private Text idTxt;
  private Button btnShallow;
  
  public HistoryStateSheet(FormToolkit toolkit, Composite parent, int style) {
    super(parent, style);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    GridLayout gridLayout = new GridLayout(3, false);
    setLayout(gridLayout);
    
    Label lblId = new Label(this, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");
    
    idTxt = new Text(this, SWT.BORDER);
    GridData gd_idTxt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_idTxt.widthHint = 200;
    idTxt.setLayoutData(gd_idTxt);
    toolkit.adapt(idTxt, true, true);
    
    btnShallow = new Button(this, SWT.CHECK);
    GridData gd_btnShallow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnShallow.horizontalIndent = 9;
    btnShallow.setLayoutData(gd_btnShallow);
    btnShallow.setText("Shallow");
    toolkit.adapt(btnShallow, true, true);
  }
  
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(
        new int[] { SWT.FocusOut, SWT.DefaultSelection }).observe(idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    context
    .bindValue(
        WidgetProperties.selection().observe(btnShallow),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.HISTORY__SHALLOW).observe(eObject));
  }

}
