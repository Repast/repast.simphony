/**
 * 
 */
package repast.simphony.statecharts.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.gmf.runtime.notation.Edge;

import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.TriggerTypes;

/**
 * Validates Transitions.
 * 
 * @author Nick Collier
 */
public class TransitionValidator {

  public IStatus checkForWarnings(IValidationContext ctx) {
    return ctx.createSuccessStatus();
  }

  private boolean isEmpty(Transition trans, TriggerTypes type, String value) {
    return trans.getTriggerType().equals(type) && (value == null || value.trim().length() == 0);
  }

  private IStatus validateTrigger(IValidationContext ctx) {
    Edge edge = (Edge) ctx.getTarget();
    Transition trans = (Transition) edge.getElement();

    if (!trans.isDefaultTransition()) {

      if (isEmpty(trans, TriggerTypes.TIMED, trans.getTriggerTimedCode())) {
        return ctx.createFailureStatus("Transition is missing required trigger time code");
      }

      if (isEmpty(trans, TriggerTypes.PROBABILITY, trans.getTriggerProbCode())) {
        return ctx.createFailureStatus("Transition is missing required probability code");
      }

      if (isEmpty(trans, TriggerTypes.CONDITION, trans.getTriggerConditionCode())) {
        return ctx.createFailureStatus("Transition is missing required trigger condition code");
      }

      if (isEmpty(trans, TriggerTypes.EXPONENTIAL, trans.getTriggerExpRateCode())) {
        return ctx
            .createFailureStatus("Transition is missing required trigger exponential decay rate code");
      }

      if (trans.getTriggerType().equals(TriggerTypes.MESSAGE)) {
        MessageCheckerTypes messageType = trans.getMessageCheckerType();
        if (!messageType.equals(MessageCheckerTypes.ALWAYS)
            && (trans.getMessageCheckerClass() == null || trans.getMessageCheckerClass().trim()
                .length() == 0)) {
          return ctx.createFailureStatus("Transition is missing required trigger message class");
        }

        if ((messageType.equals(MessageCheckerTypes.CONDITIONAL) || messageType
            .equals(MessageCheckerTypes.EQUALS))
            && (trans.getMessageCheckerCode() == null || trans.getMessageCheckerCode().trim()
                .length() == 0)) {
          return ctx
              .createFailureStatus("Transition is missing required trigger message condition or equals code");
        }
      }
    }

    return ctx.createSuccessStatus();
  }

  public IStatus checkForErrors(IValidationContext ctx) {
    IStatus status = validateTrigger(ctx);
    if (!status.isOK())
      return status;
    return validateID(ctx);
  }

  private IStatus validateID(IValidationContext ctx) {
    Edge edge = (Edge) ctx.getTarget();
    Transition trans = (Transition) edge.getElement();
    if (trans.getId().trim().length() == 0)
      return ctx.createFailureStatus("Transition is missing required ID");
    return ctx.createSuccessStatus();
  }

}
