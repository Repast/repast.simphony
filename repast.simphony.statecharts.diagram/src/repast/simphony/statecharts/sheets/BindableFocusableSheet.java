/**
 * 
 */
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;

/**
 * Interface for property sheets that have a default focusable control
 * that should receive focus when the sheet is selected and bind their
 * controls to an EObject.
 * 
 * @author Nick Collier
 */
public interface BindableFocusableSheet {

  /**
   * Resets the focus on to the default component.
   */
  void resetFocus();
  
  /**
   * Binds the EObject to the controls on this sheet uing the specified context.
   * 
   * @param context
   * @param obj
   */
  void bindModel(EMFDataBindingContext context, EObject obj);
  
  /**
   * Disposes of any resources used by this sheet.
   */
  void dispose();
}
