package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;

import repast.simphony.statecharts.scmodel.StatechartPackage;

public class PseudoStateSheet extends Composite {
  
  private Text idTxt;
  
  public PseudoStateSheet(FormToolkit toolkit, Composite parent, int style) {
    super(parent, style);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new RowLayout(SWT.HORIZONTAL));
    
    Label lblId = new Label(this, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");
    
    idTxt = new Text(this, SWT.BORDER);
    idTxt.setLayoutData(new RowData(199, SWT.DEFAULT));
    toolkit.adapt(idTxt, true, true);
  }
  
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(
        new int[] { SWT.FocusOut, SWT.DefaultSelection }).observe(idTxt);
    context.bindValue(observe, property.observe(eObject));
  }

}
