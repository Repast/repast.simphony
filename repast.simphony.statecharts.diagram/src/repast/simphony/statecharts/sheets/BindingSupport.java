/**
 * 
 */
package repast.simphony.statecharts.sheets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;

import repast.simphony.statecharts.editor.CodeUpdateStrategy;
import repast.simphony.statecharts.editor.StatechartSourceViewer;

/**
 * Support class that makes it easier to bind editor related features to widgets.
 * 
 * @author Nick Collier
 */
public class BindingSupport {

  private EMFDataBindingContext context;
  private EObject eObject;
  private Map<EStructuralFeature, Binding> bindings = new HashMap<>();

  /**
   * Creates a BindingSupport that will bind the specified EObject using the
   * specified context.
   * 
   * @param context
   * @param eObject
   */
  public BindingSupport(EMFDataBindingContext context, EObject eObject) {
    this.context = context;
    this.eObject = eObject;
  }

  /**
   * Removes all the bindings created by this BindingSupport.
   */
  public void removeBindings() {
    for (Binding binding : bindings.values()) {
      context.removeBinding(binding);
    }
    bindings.clear();
  }
  
  /**
   * Gets whether or not the specified feature is bound.
   * @param feature
   * @return true if the feature is bound otherwise false.
   */
  public boolean isBound(EStructuralFeature feature) {
    return bindings.containsKey(feature);
  }

  /**
   * Binds the specified feature to the specified viewer.
   * 
   * @param feature
   * @param viewer
   */
  public void bind(EStructuralFeature feature, StatechartSourceViewer viewer) {
    Binding binding = context.bindValue(
        WidgetProperties.text(new int[] { SWT.Modify }).observe(viewer.getTextWidget()),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject), feature)
            .observe(eObject), null, new CodeUpdateStrategy(viewer));
    bindings.put(feature, binding);
  }
}
