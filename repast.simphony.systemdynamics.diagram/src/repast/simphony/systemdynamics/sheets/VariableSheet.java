package repast.simphony.systemdynamics.sheets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.subscripts.Equation;
import repast.simphony.systemdynamics.subscripts.EquationCreator;
import repast.simphony.systemdynamics.subscripts.VariableBlock;
import repast.simphony.systemdynamics.util.SDModelUtils;

public class VariableSheet extends Composite {

  private Text txtId;
  protected StyledText txtEquation;
  private Combo cmbUnits;
  protected Combo cmbType, cmbFuncType;
  protected List lstVar, lstFunc, lstSub;
  private Text txtComment;
  protected Map<String, Variable> varMap = new HashMap<String, Variable>();
  protected java.util.List<String> subList = new ArrayList<String>();
  protected java.util.List<String> varList = new ArrayList<String>();
  //protected Map<String, Subscript> subMap = new HashMap<String, Subscript>();
  private EObject eObj;
  private CTabFolder tabFolder;
  private Composite comp;
  private ComboViewer cmbSubViewer;
  private Text text;

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
    gl_composite_2.verticalSpacing = 3;
    gl_composite_2.marginHeight = 0;
    gl_composite_2.marginWidth = 0;
    composite_2.setLayout(gl_composite_2);

    cmbFuncType = new Combo(composite_2, SWT.READ_ONLY);
    GridData gd_cmbFuncType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmbFuncType.verticalIndent = 4;
    cmbFuncType.setLayoutData(gd_cmbFuncType);
    cmbFuncType.setItems(FunctionManager.getInstance().getFunctionSetNames());
    cmbFuncType.select(0);

    lstFunc = new List(composite_2, SWT.BORDER);
    lstFunc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(lstFunc, true, true);
    fillListFunc();

    SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);
    toolkit.adapt(sashForm_1);
    toolkit.paintBordersFor(sashForm_1);

    comp = new Composite(sashForm_1, SWT.NONE);
    comp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    createEquation(comp, toolkit);

    Composite composite = new Composite(sashForm_1, SWT.NONE);
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.marginWidth = 0;
    gl_composite.marginHeight = 0;
    composite.setLayout(gl_composite);
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);

    tabFolder = new CTabFolder(composite, SWT.BORDER);
    tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(tabFolder);
    toolkit.paintBordersFor(tabFolder);
    tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
        SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

    CTabItem tbVariables = new CTabItem(tabFolder, SWT.NONE);
    tbVariables.setText("Variables");

    lstVar = new List(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    toolkit.adapt(lstVar, true, true);
    tbVariables.setControl(lstVar);
   
    CTabItem tbSubscripts = new CTabItem(tabFolder, SWT.NONE);
    tbSubscripts.setText("Subscripts");
    Composite c1 = new Composite(tabFolder, SWT.NONE);
    c1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    GridLayout gl_c1 = new GridLayout(1, false);
    gl_c1.marginTop = 5;
    gl_c1.marginHeight = 0;
    gl_c1.marginWidth = 0;
    c1.setLayout(gl_c1);
    
    cmbSubViewer = new ComboViewer(c1, SWT.READ_ONLY);
    cmbSubViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    cmbSubViewer.setContentProvider(ArrayContentProvider.getInstance());
    cmbSubViewer.setLabelProvider(new SubComboElementProvider());
    cmbSubViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        cmbSubSelected();
      }
    });
    //toolkit.adapt(cmbSub);
    //toolkit.paintBordersFor(cmbSub);
    lstSub = new List(c1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    lstSub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(lstSub, true, true);
    tbSubscripts.setControl(c1);
    sashForm_1.setWeights(new int[] { 5, 1 });

    sashForm.setWeights(new int[] { 1, 4 });

    ExpandableComposite xpndblcmpstNewExpandablecomposite = toolkit.createExpandableComposite(this,
        ExpandableComposite.TWISTIE);
    xpndblcmpstNewExpandablecomposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
        false, 1, 1));
    xpndblcmpstNewExpandablecomposite.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
        SWT.NORMAL));
    toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite);
    xpndblcmpstNewExpandablecomposite.setText("Comment");
    xpndblcmpstNewExpandablecomposite.setExpanded(true);

    Composite composite_3 = toolkit.createComposite(xpndblcmpstNewExpandablecomposite, SWT.NONE);
    toolkit.paintBordersFor(composite_3);
    xpndblcmpstNewExpandablecomposite.setClient(composite_3);
    composite_3.setLayout(new GridLayout(1, false));

    txtComment = toolkit.createText(composite_3, "New Text", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    txtComment.setText("");
    GridData gd_txtComment = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    gd_txtComment.heightHint = 40;
    txtComment.setLayoutData(gd_txtComment);

    addListeners();
  }
  
  protected void createEquation(Composite parent, FormToolkit toolkit) {
    comp.setLayout(new GridLayout(2, false));
    
    Label lblNewLabel = new Label(comp, SWT.NONE);
    toolkit.adapt(lblNewLabel, true, true);
    lblNewLabel.setText("LHS:");
    
    text = new Text(comp, SWT.BORDER);
    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(text, true, true);
    
    Label lblEq = new Label(comp, SWT.NONE);
    lblEq.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
    toolkit.adapt(lblEq, true, true);
    lblEq.setText("=");
    txtEquation = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL);
    GridData gd_txtEquation = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    gd_txtEquation.widthHint = 401;
    txtEquation.setLayoutData(gd_txtEquation);
    txtEquation.setAlwaysShowScrollBars(false);
    txtEquation.setTopMargin(4);
    txtEquation.setLeftMargin(4);
    txtEquation.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.BOLD));
    txtEquation.setText("");
    toolkit.adapt(txtEquation);
  }

  private void fillListFunc() {
    FunctionManager fm = FunctionManager.getInstance();
    lstFunc.setItems(fm.getFunctionNames(cmbFuncType.getItem(cmbFuncType.getSelectionIndex())));
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

  private String formatSubscripts(Variable var) {
    java.util.List<String> subs = var.getSubscripts();
    if (subs.isEmpty())
      return "";
    StringBuilder buf = new StringBuilder("[");
    for (int i = 0; i < subs.size(); ++i) {
      if (i > 0)
        buf.append(", ");
      buf.append(subs.get(i));
    }
    buf.append("]");
    return buf.toString();
  }
  
  protected StyledText getEquationControl() {
    return txtEquation;
  }

  private void varSelected() {
    if (lstVar.getSelectionIndex() != -1) {
      String name = lstVar.getSelection()[0];
      Variable var = varMap.get(name);
      StyledText txtControl = getEquationControl();
      int offset = txtControl.getSelection().x;
      String txtToInsert = name + formatSubscripts(var);
      txtControl.insert(txtToInsert);
      txtControl.setCaretOffset(offset + txtToInsert.length());
      txtControl.setFocus();
    }
  }

  private void subSelected() {
    if (lstSub.getSelectionIndex() != -1) {
      StyledText txtControl = getEquationControl();
      EquationCreator eqc = new EquationCreator(txtControl.getText().trim());
      Equation eq = eqc.createEquation(SDModelUtils.getVarNames((Variable) eObj));
      int pos = txtControl.getCaretOffset();
      VariableBlock vb = null;
      for (VariableBlock block : eq.getBlocks()) {
        if (block.getBlockStart() <= pos && block.getBlockEnd() >= pos) {
          vb = block;
          break;
        }
      }

      if (vb != null) {
        vb.addSubscript(lstSub.getSelection()[0], pos);
        txtControl.setText(eq.getText());
        txtControl.setFocus();
        txtControl.setCaretOffset(pos);
      }
    }
  }

  private void funcTypeSelected() {
    int index = cmbFuncType.getSelectionIndex();
    if (index != -1) {
      lstFunc.setItems(FunctionManager.getInstance().getFunctionNames(cmbFuncType.getItem(index)));
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
        getVarTypeString(VariableType.STOCK), getVarTypeString(VariableType.LOOKUP) });

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

    lstSub.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        subSelected();
      }
    });

    cmbFuncType.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        funcTypeSelected();
      }
    });
    /*
     * Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
     * DropTarget target = new DropTarget(txtEquation, DND.DROP_COPY |
     * DND.DROP_DEFAULT); target.setTransfer(types); target.addDropListener(new
     * DropTargetAdapter() {
     * 
     * @Override public void dragEnter(DropTargetEvent event) { if (event.detail
     * == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY; }
     * 
     * @Override public void drop(DropTargetEvent event) {
     * txtEquation.insert(event.data.toString()); } });
     * 
     * DragSource src = new DragSource(lstVar, DND.DROP_COPY);
     * src.setTransfer(types); src.addDragListener(new DragSourceListener() {
     * 
     * @Override public void dragStart(DragSourceEvent event) { if
     * (lstVar.getSelectionIndex() == -1) event.doit = false; }
     * 
     * @Override public void dragSetData(DragSourceEvent event) { if
     * (TextTransfer.getInstance().isSupportedType(event.dataType)) { event.data
     * = lstVar.getSelection()[0]; } }
     * 
     * @Override public void dragFinished(DragSourceEvent event) { } });
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
    eObj = eObject;
    java.util.List<String> allUnits = SDModelUtils.getAllUnits(eObject);
    Collections.sort(allUnits);
    cmbUnits.setItems(allUnits.toArray(new String[0]));

    FunctionManager.getInstance().clearLookups();
    cmbFuncType.select(0);
    fillListFunc();

    bind(context, eObject, SDModelPackage.Literals.VARIABLE__EQUATION, txtEquation);
    bind(context, eObject, SDModelPackage.Literals.VARIABLE__NAME, txtId);
    bind(context, eObject, SDModelPackage.Literals.VARIABLE__COMMENT, txtComment);
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

    lstFunc.setEnabled(!((Variable) eObject).getType().equals(VariableType.LOOKUP));
    updateVariables(eObject);
    updateSubscripts(eObject);

    tabFolder.setSelection(0);
  }

  protected void updateVariables(EObject eObj) {
    FunctionManager.getInstance().clearLookups();
    varMap.clear();
    varList.clear();

    Variable var = ((Variable) eObj);

    if (var.getType() == VariableType.RATE || var.getType() == VariableType.AUXILIARY) {
      java.util.List<Variable> vars = SDModelUtils.getIncomingVariables(var);

      for (Variable v : vars) {
        if (v.getType().equals(VariableType.LOOKUP)) {
          FunctionManager.getInstance().addLookup(v.getName());
        } else {
          varList.add(v.getName());
          varMap.put(v.getName(), v);
        }
      }
      lstVar.setItems(varList.toArray(new String[0]));
    }
  }

  protected void updateSubscripts(EObject eObj) {
    java.util.List<Subscript> subs = ((SystemModel) eObj.eContainer()).getSubscripts();
    
    java.util.List<SubComboElement> cmbItems = new ArrayList<SubComboElement>();
    SubComboElement rangeItem = new SubComboElement("Range");
    cmbItems.add(rangeItem);
    SubComboElement allElementsItem = new SubComboElement("All Elements");
    
    for (Subscript sub : subs) {
      rangeItem.addString(sub.getName());
      allElementsItem.addSubscript(sub);
      SubComboElement item = new SubComboElement(sub.getName() + " Elements");
      item.addSubscript(sub);
      cmbItems.add(item);
    }

    cmbItems.add(allElementsItem);
    cmbSubViewer.setInput(cmbItems.toArray());
    cmbSubViewer.getCombo().select(0);
    cmbSubSelected();
  }
  
  private void cmbSubSelected() {
    IStructuredSelection selection = (IStructuredSelection) cmbSubViewer.getSelection();
    SubComboElement element = (SubComboElement) selection.getFirstElement();
    lstSub.setItems(element.getItems());
  }
  
  private class SubComboElement {
     String label;
     java.util.List<String> items = new ArrayList<String>();
    
    public SubComboElement(String label) {
      this.label = label;
    }
    
    public void addSubscript(Subscript sub) {
      items.addAll(sub.getElements());
    }
    
    public void addString(String val) {
      items.add(val);
    }
    
    public String[] getItems() {
      return items.toArray(new String[]{});
    }
    
    public String getLabel() {
      return label;
    }
  }
  
  private class SubComboElementProvider extends LabelProvider {

    @Override
    public String getText(Object element) {
      return ((SubComboElement)element).getLabel();
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
