/**
 * 
 */
package repast.simphony.statecharts.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * Utility class for working with statecharts models.
 * 
 * @author Nick Collier
 */
public class StatechartsModelUtil {
  
  public static StateMachine findStateMachine(EObject self) {
    EObject parent = self.eContainer();
    while (parent != null && !(parent instanceof StateMachine)) {
      parent = parent.eContainer();
    }
    return (StateMachine)parent;
  }
  
 
  /**
   * Gets whether or not the specified EObject has any
   * incoming transitions.
   * 
   * @param obj
   * @return true if the specified obj has an incoming transition 
   * otherwise false.
   */
  public static boolean hasIncoming(EObject obj) {
    StateMachine machine = StatechartsModelUtil.findStateMachine(obj);
    for (Transition transition : machine.getTransitions()) {
      if (transition.getTo().equals(obj)) return true;
    }
    return false;
  }
  
  /**
   * Gets all the Transitions for which the specified obj is the target.
   * 
   * @param obj
   * @return a list of Transitions for which the specified obj is the target.
   */
  public static List<Transition> getIncoming(EObject obj) {
    List<Transition> trans = new ArrayList<Transition>();
    StateMachine machine = StatechartsModelUtil.findStateMachine(obj);
    for (Transition transition : machine.getTransitions()) {
      if (transition.getTo().equals(obj)) {
        trans.add(transition);
      }
    }
    
    return trans;
  }
  
  /**
   * Gets all the Transitions for which the specified obj is the source.
   * 
   * @param obj
   * @return a list of Transitions for which the specified obj is the source.
   */
  public static List<Transition> getOutgoing(EObject obj) {
    List<Transition> trans = new ArrayList<Transition>();
    StateMachine machine = StatechartsModelUtil.findStateMachine(obj);
    for (Transition transition : machine.getTransitions()) {
      if (transition.getFrom().equals(obj)) {
        trans.add(transition);
      }
    }
    
    return trans;
  }
  
  public static LanguageTypes getDefaultLanguage(EObject self) {
    EObject parent = findStateMachine(self);
    if (parent == null) {
      return LanguageTypes.JAVA;
    }

    return ((StateMachine) parent).getLanguage();
  }

  /**
   * Gets the next free id from the StateMachine.
   */
  public static int getNextID(EObject self) {
    EObject parent = findStateMachine(self);
    if (parent == null) {
      return new Integer(0);
    }

    return ((StateMachine) parent).getNextID();
  }

}
