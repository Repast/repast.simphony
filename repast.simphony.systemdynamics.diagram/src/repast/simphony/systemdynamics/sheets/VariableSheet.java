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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.util.SDModelUtils;

public class VariableSheet extends Composite {

  private Text txtId;
  protected StyledText txtEquation;
  private Combo cmbUnits;
  private Combo cmbType;
  protected List lstVar, lstFunc;

  public VariableSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NONE);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(1, false));

    Composite composite_1 = new Composite(this, SWT.NONE);
    composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);
    composite_1.setLayout(new GridLayout(4, false));
    addHeader(toolkit, composite_1);

    Composite grpEquation = new Composite(this, SWT.NONE);
    grpEquation.setLayout(new GridLayout(2, false));
    grpEquation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(grpEquation);
    toolkit.paintBordersFor(grpEquation);

    Label lblEquation_1 = new Label(grpEquation, SWT.NONE);
    toolkit.adapt(lblEquation_1, true, true);
    lblEquation_1.setText("Equation");

    Label label = new Label(grpEquation, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(label, true, true);

    SashForm sashForm = new SashForm(grpEquation, SWT.NONE);
    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    toolkit.adapt(sashForm);
    toolkit.paintBordersFor(sashForm);

    Composite composite_2 = toolkit.createComposite(sashForm, SWT.NONE);
    toolkit.paintBordersFor(composite_2);
    GridLayout gl_composite_2 = new GridLayout(1, false);
    gl_composite_2.marginHeight = 0;
    gl_composite_2.marginWidth = 0;
    composite_2.setLayout(gl_composite_2);

    lstFunc = new List(composite_2, SWT.BORDER);
    lstFunc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(lstFunc, true, true);
    fillListFunc();

    SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);
    toolkit.adapt(sashForm_1);
    toolkit.paintBordersFor(sashForm_1);

    txtEquation = new StyledText(sashForm_1, SWT.BORDER | SWT.V_SCROLL);
    txtEquation.setAlwaysShowScrollBars(false);
    txtEquation.setTopMargin(4);
    txtEquation.setLeftMargin(4);
    txtEquation.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.BOLD));
    txtEquation.setText("");
    toolkit.adapt(txtEquation);

    Composite composite = new Composite(sashForm_1, SWT.NONE);
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.marginWidth = 0;
    gl_composite.marginHeight = 0;
    composite.setLayout(gl_composite);
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);

    lstVar = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    lstVar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(lstVar, true, true);
    
    sashForm_1.setWeights(new int[] { 4, 1 });
    sashForm.setWeights(new int[] { 1, 4 });

    addListeners();
  }
  
  private void fillListFunc() {
    FunctionManager fm = FunctionManager.getInstance();
    lstFunc.setItems(fm.getFunctionNames());
  }
  
  private void funcSelected() {
    if (lstFunc.getSelectionIndex() != -1) {
      String name = lstFunc.getSelection()[0];
      String pattern = FunctionManager.getInstance().getFunctionPattern(name); 
      String txt = name + pattern;
      int offset = txtEquation.getSelection().x;
      txtEquation.insert(txt);
      int selectionIndex = offset + name.length() + 1;
      txtEquation.setFocus();
      txtEquation.setSelection(selectionIndex, selectionIndex + 1);
    }
  }
  
  private void varSelected() {
    if (lstVar.getSelectionIndex() != -1) {
      String var = lstVar.getSelection()[0];
      int offset = txtEquation.getSelection().x;
      txtEquation.insert(var);
      txtEquation.setCaretOffset(offset + var.length());
      txtEquation.setFocus();
    }
  }

  protected void addHeader(FormToolkit toolkit, Composite parent) {
    Label lblName = new Label(parent, SWT.NONE);
    toolkit.adapt(lblName, true, true);
    lblName.setText("Name:");

    txtId = new Text(parent, SWT.BORDER);
    GridData gd_txtId = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
    gd_txtId.widthHint = 378;
    txtId.setLayoutData(gd_txtId);
    toolkit.adapt(txtId, true, true);

    Label lblType = new Label(parent, SWT.NONE);
    toolkit.adapt(lblType, true, true);
    lblType.setText("Type:");

    cmbType = new Combo(parent, SWT.READ_ONLY);
    GridData gd_cmbType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbType.widthHint = 140;
    cmbType.setLayoutData(gd_cmbType);
    cmbType.setItems(new String[] { getVarTypeString(VariableType.AUXILIARY),
        getVarTypeString(VariableType.CONSTANT), getVarTypeString(VariableType.RATE),
        getVarTypeString(VariableType.STOCK) });

    toolkit.adapt(cmbType);
    toolkit.paintBordersFor(cmbType);

    Label lblUnits = new Label(parent, SWT.NONE);
    toolkit.adapt(lblUnits, true, true);
    lblUnits.setText("Units:");

    cmbUnits = new Combo(parent, SWT.NONE);
    GridData gd_cmbUnits = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbUnits.widthHint = 200;
    cmbUnits.setLayoutData(gd_cmbUnits);
    toolkit.adapt(cmbUnits);
    toolkit.paintBordersFor(cmbUnits);
  }

  private void addListeners() {
    lstFunc.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        funcSelected();
      }
    });
    
    lstVar.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        varSelected();
      }
    });
    /*
    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
    DropTarget target = new DropTarget(txtEquation, DND.DROP_COPY | DND.DROP_DEFAULT);
    target.setTransfer(types);
    target.addDropListener(new DropTargetAdapter() {

      @Override
      public void dragEnter(DropTargetEvent event) {
        if (event.detail == DND.DROP_DEFAULT)
          event.detail = DND.DROP_COPY;
      }

      @Override
      public void drop(DropTargetEvent event) {
        txtEquation.insert(event.data.toString());
      }
    });
    
    DragSource src = new DragSource(lstVar, DND.DROP_COPY);
    src.setTransfer(types);
    src.addDragListener(new DragSourceListener() {
      @Override
      public void dragStart(DragSourceEvent event) {
        if (lstVar.getSelectionIndex() == -1)
          event.doit = false;
      }

      @Override
      public void dragSetData(DragSourceEvent event) {
        if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
          event.data = lstVar.getSelection()[0];
        }
      }

      @Override
      public void dragFinished(DragSourceEvent event) {
      }
    });
    */
    
  }

  private String getVarTypeString(VariableType type) {
    String str = type.getLiteral();
    return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
  }

  protected void bind(EMFDataBindingContext context, EObject eObject, EAttribute attribute,
      Widget widget) {
    context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observeDelayed(400, widget),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), attribute).observe(
            eObject));
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

    bind(context, eObject, SDModelPackage.Literals.VARIABLE__EQUATION, txtEquation);
    bind(context, eObject, SDModelPackage.Literals.VARIABLE__NAME, txtId);
    context.bindValue(
        WidgetProperties.text().observe(cmbUnits),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            SDModelPackage.Literals.VARIABLE__UNITS).observe(eObject));

    context.bindValue(
        WidgetProperties.selection().observe(cmbType),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            SDModelPackage.Literals.VARIABLE__TYPE).observe(eObject),
        createUpdateValueStrategy(new StringToVarType()),
        createUpdateValueStrategy(new VarTypeToString()));

    updateVariables(eObject);
  }

  protected void updateVariables(EObject eObj) {
    Variable var = ((Variable) eObj);
    lstVar.setItems(new String[] {});

    if (var.getType() == VariableType.RATE || var.getType() == VariableType.AUXILIARY) {
      java.util.List<Variable> vars = SDModelUtils.getIncomingVariables(var);
      String[] items = new String[vars.size()];
      int i = 0;
      for (Variable v : vars) {
        items[i++] = v.getName();
      }
      lstVar.setItems(items);
    }
  }

  private class StringToVarType implements IConverter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    @Override
    public Object getFromType() {
      return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    @Override
    public Object getToType() {
      return VariableType.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.
     * Object)
     */
    @Override
    public Object convert(Object fromObject) {
      return VariableType.get(fromObject.toString().toLowerCase());
    }
  }

  private class VarTypeToString implements IConverter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    @Override
    public Object getFromType() {
      return VariableType.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    @Override
    public Object getToType() {
      return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.
     * Object)
     */
    @Override
    public Object convert(Object fromObject) {
      return getVarTypeString((VariableType) fromObject);
    }
  }
}
