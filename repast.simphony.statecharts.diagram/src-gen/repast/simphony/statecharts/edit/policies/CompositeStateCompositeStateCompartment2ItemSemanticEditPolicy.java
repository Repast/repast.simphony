package repast.simphony.statecharts.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

import repast.simphony.statecharts.edit.commands.CompositeState2CreateCommand;
import repast.simphony.statecharts.edit.commands.FinalState2CreateCommand;
import repast.simphony.statecharts.edit.commands.History2CreateCommand;
import repast.simphony.statecharts.edit.commands.HistoryCreateCommand;
import repast.simphony.statecharts.edit.commands.PseudoState3CreateCommand;
import repast.simphony.statecharts.edit.commands.PseudoState4CreateCommand;
import repast.simphony.statecharts.edit.commands.State2CreateCommand;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class CompositeStateCompositeStateCompartment2ItemSemanticEditPolicy extends
    StatechartBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public CompositeStateCompositeStateCompartment2ItemSemanticEditPolicy() {
    super(StatechartElementTypes.CompositeState_3002);
  }

  /**
   * @generated
   */
  protected Command getCreateCommand(CreateElementRequest req) {
    if (StatechartElementTypes.State_3001 == req.getElementType()) {
      return getGEFWrapper(new State2CreateCommand(req));
    }
    if (StatechartElementTypes.CompositeState_3002 == req.getElementType()) {
      return getGEFWrapper(new CompositeState2CreateCommand(req));
    }
    if (StatechartElementTypes.PseudoState_3003 == req.getElementType()) {
      return getGEFWrapper(new PseudoState3CreateCommand(req));
    }
    if (StatechartElementTypes.PseudoState_3006 == req.getElementType()) {
      return getGEFWrapper(new PseudoState4CreateCommand(req));
    }
    if (StatechartElementTypes.FinalState_3007 == req.getElementType()) {
      return getGEFWrapper(new FinalState2CreateCommand(req));
    }
    if (StatechartElementTypes.History_3008 == req.getElementType()) {
      return getGEFWrapper(new HistoryCreateCommand(req));
    }
    if (StatechartElementTypes.History_3009 == req.getElementType()) {
      return getGEFWrapper(new History2CreateCommand(req));
    }
    return super.getCreateCommand(req);
  }

}
