package repast.simphony.statecharts.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class TransitionItemSemanticEditPolicy extends StatechartBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public TransitionItemSemanticEditPolicy() {
    super(StatechartElementTypes.Transition_4001);
  }

  /**
   * @generated
   */
  protected Command getDestroyElementCommand(DestroyElementRequest req) {
    return getGEFWrapper(new DestroyElementCommand(req));
  }

}
