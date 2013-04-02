/**
 * 
 */
package repast.simphony.statecharts.filter;

import org.eclipse.jface.viewers.IFilter;

import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * Filters out transitions to show in the TransitionSheet. 
 * 
 * @author Nick Collier
 */
public class TransitionFilter implements IFilter {

  @Override
  public boolean select(Object toTest) {
    if (toTest instanceof TransitionEditPart) {
      Transition transition = (Transition)((TransitionEditPart)toTest).resolveSemanticElement();
      AbstractState from = transition.getFrom();
      return !(from.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE) && 
          (((PseudoState)from).getType().equals(PseudoStateTypes.INITIAL) || ((PseudoState)from).getType().equals(PseudoStateTypes.ENTRY)));
    } else {
      return false;
    }
  }
}
