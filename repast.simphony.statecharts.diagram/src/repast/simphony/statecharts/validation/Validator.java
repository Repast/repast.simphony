/**
 * 
 */
package repast.simphony.statecharts.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.util.StatechartsModelUtil;

/**
 * Static methods that do some validation common to all diagram elements. 
 * 
 * @author Nick Collier
 */
public class Validator {
  
  private static Set<String> badCode = new HashSet<>();
  
  private static String getName(EObject obj) {
    if (obj.eClass().equals(StatechartPackage.Literals.COMPOSITE_STATE)) return "Composite State";
    if (obj.eClass().equals(StatechartPackage.Literals.STATE)) return "State";
    if (obj.eClass().equals(StatechartPackage.Literals.HISTORY)) return "History";
    if (obj.eClass().equals(StatechartPackage.Literals.TRANSITION)) return "Transition";
    if (obj.eClass().equals(StatechartPackage.Literals.FINAL_STATE)) return "Final State";
    if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)) {
      PseudoState state = (PseudoState)obj;
      if (state.getType().equals(PseudoStateTypes.CHOICE)) return "Choice";
      if (state.getType().equals(PseudoStateTypes.INITIAL)) return "Initial State Marker";
      if (state.getType().equals(PseudoStateTypes.ENTRY)) return "Entry State Marker";
    }
    
    
    return "Element";
  }
  
  /**
   * Checks that the specified state has an id. If not, then an error message
   * is construted using the state name.
   * 
   * @param ctx
   * @param state
   * @param name
   * @return a successful IStatus if the check is passed, otherwise an IStatus with 
   * the severity specified in the IValidationContext.
   */
  public static IStatus validateID(IValidationContext ctx, AbstractState state) {
    if (state.getId().trim().length() == 0) {
      return ctx.createFailureStatus(getName(state) + " is missing required ID");
    }
    return ctx.createSuccessStatus();
  }
  
  private static boolean needsIncoming(AbstractState state) {
    if (state.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)) {
      return  ((PseudoState)state).getType().equals(PseudoStateTypes.CHOICE);
    }
    return true;
  }
  
  public static void addBadCodeUUID(String uuid) {
    badCode.add(uuid);
  }
  
  public static void removeBadCodeUUID(String uuid) {
    badCode.remove(uuid);
  }
  
  /**
   * Checks if the specified state has been flagged as having bad onEnter / onExit
   * code. Returns 
   * @param ctx
   * @param state
   * @return
   */
  public static IStatus validateCode(IValidationContext ctx, AbstractState state) {
    if (badCode.contains(state.getUuid())) return ctx.createFailureStatus(getName(state) + " has invalid onEnter or onExit code.");
    return ctx.createSuccessStatus();
  }
  
  public static boolean isCodeValid(AbstractState state) {
    return !badCode.contains(state.getUuid());
  }
  
  public static boolean isCodeValid(Transition trans) {
    return !badCode.contains(trans.getUuid());
  }
  
  /**
   * Checks if the specified state has any incoming transitions.
   * 
   * @param ctx
   * @param state
   * @param name
   * @return a successful IStatus if the check is passed, otherwise an IStatus with 
   * the severity specified in the IValidationContext.
   */
  public static IStatus validateIncoming(IValidationContext ctx, AbstractState state) {
    if (needsIncoming(state) && !StatechartsModelUtil.hasIncoming(state))
      return ctx.createFailureStatus(getName(state) + " has no incoming transitions");
    
    return ctx.createSuccessStatus();
  }

}
