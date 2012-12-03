/**
 * 
 */
package repast.simphony.statecharts.util;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.statecharts.scmodel.StateMachine;

/**
 * Utility class for working with statecharts models.
 * 
 * @author Nick Collier
 */
public class StatechartsModelUtil {

  /**
   * Gets the next free id from the StateMachine.
   */
  public static int getNextID(EObject self) {
    EObject parent = self.eContainer();
    while (parent != null && !(parent instanceof StateMachine)) {
      parent = parent.eContainer();
    }
    if (parent == null) {
      return new Integer(0);
    }

    return ((StateMachine) parent).getNextID();
  }

}
