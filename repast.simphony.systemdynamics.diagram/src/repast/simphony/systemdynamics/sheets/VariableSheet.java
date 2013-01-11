package repast.simphony.systemdynamics.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

public class VariableSheet extends Composite {

  private Text idTxt;
  private Text txtNewText;

  public VariableSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NONE);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(4, false));

    Label lblName = new Label(this, SWT.NONE);
    lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblName, true, true);
    lblName.setText("Name:");

    idTxt = new Text(this, SWT.BORDER);
    idTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
    toolkit.adapt(idTxt, true, true);
    
    Label lblType = new Label(this, SWT.NONE);
    lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblType, true, true);
    lblType.setText("Type:");
    
    Combo combo = new Combo(this, SWT.READ_ONLY);
    GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
    gd_combo.widthHint = 205;
    combo.setLayoutData(gd_combo);
    toolkit.adapt(combo);
    toolkit.paintBordersFor(combo);
    
    Label lblUnits = new Label(this, SWT.NONE);
    lblUnits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblUnits, true, true);
    lblUnits.setText("Units:");
    
    Combo combo_1 = new Combo(this, SWT.NONE);
    combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(combo_1);
    toolkit.paintBordersFor(combo_1);
    
    Group grpEquation = new Group(this, SWT.NONE);
    grpEquation.setText("Equation");
    grpEquation.setLayout(new GridLayout(2, false));
    grpEquation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
    toolkit.adapt(grpEquation);
    toolkit.paintBordersFor(grpEquation);
    
    txtNewText = toolkit.createText(grpEquation, "New Text", SWT.MULTI);
    txtNewText.setText("");
    GridData gd_txtNewText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    gd_txtNewText.heightHint = 200;
    txtNewText.setLayoutData(gd_txtNewText);
    
    Composite composite = new Composite(grpEquation, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));
    composite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);
    
    Label lblVariables = new Label(composite, SWT.NONE);
    toolkit.adapt(lblVariables, true, true);
    lblVariables.setText("Variables");
    
    List lstVar = new List(composite, SWT.BORDER);
    lstVar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
    toolkit.adapt(lstVar, true, true);
  }

 
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    /*
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.
        text(new int[] {SWT.Modify }).observeDelayed(400, idTxt);
    context.bindValue(observe, property.observe(eObject));

    context
        .bindValue(
            WidgetProperties.text(new int[] {SWT.Modify }).observeDelayed(400,
                onEnterTxt),
            EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
                StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER).observe(eObject));

    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify}).observeDelayed(400, onExitTxt),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT).observe(eObject));
    
    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);
    */
  }
}
