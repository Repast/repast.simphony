package repast.simphony.statecharts.sheets;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;

public class StateSheet extends Composite {

  private Text idTxt;
  private Text onEnterTxt;
  private Text onExitTxt;
  private Button btnJava, btnRelogo, btnGroovy;
  
  private LanguageTypes selectedType;
  
  public StateSheet(FormToolkit toolkit, Composite parent, int style) {
    super(parent, style);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new GridLayout(3, false));

    Label lblId = new Label(this, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");

    idTxt = new Text(this, SWT.BORDER);
    GridData gd_idFld = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    gd_idFld.widthHint = 200;
    idTxt.setLayoutData(gd_idFld);
    toolkit.adapt(idTxt, true, true);
    new Label(this, SWT.NONE);

    Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    toolkit.adapt(label, true, true);
    new Label(this, SWT.NONE);

    Composite composite = new Composite(this, SWT.H_SCROLL);
    GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
    gd_composite.heightHint = 206;
    composite.setLayoutData(gd_composite);
    GridLayout gl_composite = new GridLayout(1, false);
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
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_composite_1.widthHint = 353;
    composite_1.setLayoutData(gd_composite_1);
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);
    
    Label lblLanguage = new Label(composite_1, SWT.NONE);
    lblLanguage.setBounds(0, 0, 59, 14);
    toolkit.adapt(lblLanguage, true, true);
    lblLanguage.setText("Language:");
    new Label(composite_1, SWT.NONE);
    
    btnJava = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnJava, true, true);
    btnJava.setText("Java");
    
    btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");
    
    btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");

    Label lblOnEnter = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnEnter, true, true);
    lblOnEnter.setText("On Enter:");

    onEnterTxt = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    onEnterTxt.setText("");
    onEnterTxt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    toolkit.adapt(onEnterTxt, true, true);

    Label lblOnExit = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnExit, true, true);
    lblOnExit.setText("On Exit:");

    onExitTxt = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    GridData gd_onExitTxt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    gd_onExitTxt.horizontalIndent = 1;
    onExitTxt.setLayoutData(gd_onExitTxt);
    toolkit.adapt(onExitTxt, true, true);

    onExitTxt.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          e.detail = SWT.TRAVERSE_NONE;
        }
      }
    });
    
    onEnterTxt.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          e.detail = SWT.TRAVERSE_NONE;
        }
      }
    });
    
    SelectionListener listener = new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        if (e.getSource().equals(btnJava)) selectedType = LanguageTypes.JAVA;
        else if (e.getSource().equals(btnRelogo)) selectedType = LanguageTypes.RELOGO;
        else if (e.getSource().equals(btnGroovy)) selectedType = LanguageTypes.GROOVY;
        
        System.out.println("type selected: " + selectedType);
      }
    };
    
    btnJava.addSelectionListener(listener);
    btnRelogo.addSelectionListener(listener);
    btnGroovy.addSelectionListener(listener);
  }
  
  private void initLanguageButton() {
    btnJava.setSelection(selectedType.equals(LanguageTypes.JAVA));
    btnRelogo.setSelection(selectedType.equals(LanguageTypes.RELOGO));
    btnGroovy.setSelection(selectedType.equals(LanguageTypes.GROOVY));
  }
  
  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(
        new int[] { SWT.FocusOut, SWT.DefaultSelection }).observe(idTxt);
    context.bindValue(observe, property.observe(eObject));
    
    context.bindValue(WidgetProperties.text(new int[]{SWT.FocusOut, SWT.DefaultSelection}).observe(onEnterTxt),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER).observe(eObject));
    
    context.bindValue(WidgetProperties.text(new int[] {SWT.FocusOut, SWT.DefaultSelection}).observe(onExitTxt),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ON_EXIT).observe(eObject));
    
    selectedType = ((AbstractState)eObject).getLanguage();
    initLanguageButton();
    
    UpdateValueStrategy targetToModel = new UpdateValueStrategy();
    BooleanToLanguageConverter converter = new BooleanToLanguageConverter();
    targetToModel.setConverter(converter);
    
    context.bindValue(WidgetProperties.selection().observe(btnJava),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE).observe(eObject), targetToModel, null);
    
    context.bindValue(WidgetProperties.selection().observe(btnGroovy),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE).observe(eObject), targetToModel, null);
    
    context.bindValue(WidgetProperties.selection().observe(btnRelogo),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE).observe(eObject), targetToModel, null);
        
    
    /*
    FeaturePath onEnter = null, onExit = null;
    if (eObject instanceof CompositeState) {
      onEnter = FeaturePath.fromList(StatechartPackage.Literals.COMPOSITE_STATE__ON_ENTER, StatechartPackage.Literals.ACTION__CODE);
      onExit = FeaturePath.fromList(StatechartPackage.Literals.COMPOSITE_STATE__ON_EXIT, StatechartPackage.Literals.ACTION__CODE);
    } else {
      onEnter = FeaturePath.fromList(StatechartPackage.Literals.STATE__ON_ENTER, StatechartPackage.Literals.ACTION__CODE);
      onExit = FeaturePath.fromList(StatechartPackage.Literals.STATE__ON_EXIT, StatechartPackage.Literals.ACTION__CODE);
    }
    
    context.bindValue(WidgetProperties.text(SWT.Modify).observe(onExitTxt), EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), 
        onExit).observe(eObject));
    context.bindValue(WidgetProperties.text(SWT.Modify).observe(onEnterTxt), EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        onEnter).observe(eObject));
        */
  }
  
  private class BooleanToLanguageConverter implements IConverter {
   
    @Override
    public Object getFromType() {
      return Boolean.class;
    }

    @Override
    public Object getToType() {
      return LanguageTypes.class;
    }

    @Override
    public Object convert(Object fromObject) {
      return selectedType;
    }
  }
}
