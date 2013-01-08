/**
 * 
 */
package repast.simphony.statecharts.edit.parts;

import org.eclipse.gef.DragTracker;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.swt.graphics.Image;

import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * Popup edit policy for the State machine.
 * 
 * @author Nick Collier
 */
public class StateMachinePopupBarEditPolicy extends PopupBarEditPolicy {

  /**
   * This prevents the initial state marker from being added to the popup menu.
   */
  @Override
  protected void addPopupBarDescriptor(IElementType elementType, Image theImage,
      DragTracker theTracker, String theTip) {
    if (!elementType.equals(StatechartElementTypes.PseudoState_2005)) {
      super.addPopupBarDescriptor(elementType, theImage, theTracker, theTip);
    }
  }
}
