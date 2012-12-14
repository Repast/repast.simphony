/**
 * 
 */
package repast.simphony.statecharts.util;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StateMachine;

/**
 * Utility class for working with statecharts models.
 * 
 * @author Nick Collier
 */
public class StatechartsModelUtil {
  
  private static EObject findParent(EObject self) {
    EObject parent = self.eContainer();
    while (parent != null && !(parent instanceof StateMachine)) {
      parent = parent.eContainer();
    }
    return parent;
  }
  
  public static LanguageTypes getDefaultLanguage(EObject self) {
    EObject parent = findParent(self);
    if (parent == null) {
      return LanguageTypes.JAVA;
    }

    return ((StateMachine) parent).getLanguage();
  }

  /**
   * Gets the next free id from the StateMachine.
   */
  public static int getNextID(EObject self) {
    EObject parent = findParent(self);
    if (parent == null) {
      return new Integer(0);
    }

    return ((StateMachine) parent).getNextID();
  }

}
