/**
 * 
 */
package repast.simphony.statecharts.sheets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

import repast.simphony.statecharts.scmodel.LanguageTypes;

/**
 * Manages the 3 language buttons.
 * 
 * @author Nick Collier
 */
public class LanguageButtonsGroup {

  private Button btnJava, btnRelogo, btnGroovy;
  private LanguageTypes selectedType;
  private List<SelectionListener> listeners = new ArrayList<>();
  private EObject obj;
  private EStructuralFeature feature;

  public LanguageButtonsGroup(Button btnJava, Button btnRelogo, Button btnGroovy) {
    this.btnGroovy = btnGroovy;
    this.btnJava = btnJava;
    this.btnRelogo = btnRelogo;
    
    addListener();
  }
  
  public void addListener(SelectionListener listener) {
    listeners.add(listener);
  }
  
  public LanguageTypes getSelectedType() {
    return selectedType;
  }
  
  private void initLanguageButton() {
    btnJava.setSelection(selectedType.equals(LanguageTypes.JAVA));
    btnRelogo.setSelection(selectedType.equals(LanguageTypes.RELOGO));
    btnGroovy.setSelection(selectedType.equals(LanguageTypes.GROOVY));
  }
  
  //@SuppressWarnings("unchecked")
  public void bindModel(EMFDataBindingContext context, EObject eObject, EAttribute attributeToBind) {
    obj = eObject;
    feature = eObject.eClass().getEStructuralFeature(attributeToBind.getFeatureID());
    selectedType = (LanguageTypes)eObject.eGet(feature);
    initLanguageButton();

    /*
    UpdateValueStrategy targetToModel = new UpdateValueStrategy();
    BooleanToLanguageConverter converter = new BooleanToLanguageConverter();
    targetToModel.setConverter(converter);
    */
    
    /*
    SelectObservableValue<LanguageTypes> langTypesObs = new SelectObservableValue<LanguageTypes>(LanguageTypes.class);
    IObservableValue btnJavaSelection = WidgetProperties.selection().observe(btnJava);
    langTypesObs.addOption(LanguageTypes.JAVA, btnJavaSelection);
    
    IObservableValue btnGrooovySelection = WidgetProperties.selection().observe(btnGroovy);
    langTypesObs.addOption(LanguageTypes.GROOVY, btnGrooovySelection);
    
    IObservableValue btnRelogoSelection = WidgetProperties.selection().observe(btnRelogo);
    langTypesObs.addOption(LanguageTypes.RELOGO, btnRelogoSelection);
    context.bindValue(langTypesObs, EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        attributeToBind).observe(eObject));
    */
   
    /*
    LanguageToBooleanConverter modelToTargetConverter = new LanguageToBooleanConverter();
    context.bindValue(
        WidgetProperties.selection().observe(btnJava),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            attributeToBind).observe(eObject), targetToModel,
        new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));

    context.bindValue(
        WidgetProperties.selection().observe(btnGroovy),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            attributeToBind).observe(eObject), targetToModel,
        new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));

    context.bindValue(
        WidgetProperties.selection().observe(btnRelogo),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            attributeToBind).observe(eObject), targetToModel,
        new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
        */

  }
  
  private void fireEvent(SelectionEvent evt) {
    for (SelectionListener listener : listeners) {
      listener.widgetSelected(evt);
    }
  }
  
  private void addListener() {
    SelectionListener listener = new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        LanguageTypes type = selectedType;
        if (e.getSource().equals(btnJava))
          selectedType = LanguageTypes.JAVA;
        else if (e.getSource().equals(btnRelogo))
          selectedType = LanguageTypes.RELOGO;
        else if (e.getSource().equals(btnGroovy))
          selectedType = LanguageTypes.GROOVY;
        
        // only fire if the type changes -- the first selection
        // is the "deselection" of the currently selected so
        // we need this.
        if (type != selectedType) {
          fireEvent(e);
          TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(obj);
          Command command = SetCommand.create(ted, obj, feature, selectedType);
          ted.getCommandStack().execute(command);
        }
      }
    };

    btnJava.addSelectionListener(listener);
    btnRelogo.addSelectionListener(listener);
    btnGroovy.addSelectionListener(listener);
  }

  /*
  private class BooleanToLanguageConverter extends Converter {
    
    public BooleanToLanguageConverter() {
       super(Boolean.class, LanguageTypes.class);
    }

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
  
private class LanguageToBooleanConverter extends Converter {
    
    public LanguageToBooleanConverter() {
       super(LanguageTypes.class, Boolean.class);
    }

    @Override
    public Object getFromType() {
      return LanguageTypes.class;
    }

    @Override
    public Object getToType() {
      return Boolean.class;
    }

    @Override
    public Object convert(Object fromObject) {
      return Boolean.valueOf(selectedType == (LanguageTypes)fromObject);
    }
  }
  */


}
