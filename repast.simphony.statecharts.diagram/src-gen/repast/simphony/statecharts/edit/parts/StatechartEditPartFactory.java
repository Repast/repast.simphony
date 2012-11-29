package repast.simphony.statecharts.edit.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.directedit.locator.CellEditorLocatorAccess;

import repast.simphony.statecharts.part.StatechartVisualIDRegistry;

/**
 * @generated
 */
public class StatechartEditPartFactory implements EditPartFactory {

  /**
   * @generated
   */
  public EditPart createEditPart(EditPart context, Object model) {
    if (model instanceof View) {
      View view = (View) model;
      switch (StatechartVisualIDRegistry.getVisualID(view)) {

      case StateMachineEditPart.VISUAL_ID:
        return new StateMachineEditPart(view);

      case StateEditPart.VISUAL_ID:
        return new StateEditPart(view);

      case StateNameEditPart.VISUAL_ID:
        return new StateNameEditPart(view);

      case CompositeStateEditPart.VISUAL_ID:
        return new CompositeStateEditPart(view);

      case CompositeStateNameEditPart.VISUAL_ID:
        return new CompositeStateNameEditPart(view);

      case PseudoStateEditPart.VISUAL_ID:
        return new PseudoStateEditPart(view);

      case PseudoState2EditPart.VISUAL_ID:
        return new PseudoState2EditPart(view);

      case PseudoState5EditPart.VISUAL_ID:
        return new PseudoState5EditPart(view);

      case FinalStateEditPart.VISUAL_ID:
        return new FinalStateEditPart(view);

      case State2EditPart.VISUAL_ID:
        return new State2EditPart(view);

      case StateName2EditPart.VISUAL_ID:
        return new StateName2EditPart(view);

      case CompositeState2EditPart.VISUAL_ID:
        return new CompositeState2EditPart(view);

      case CompositeStateName2EditPart.VISUAL_ID:
        return new CompositeStateName2EditPart(view);

      case PseudoState3EditPart.VISUAL_ID:
        return new PseudoState3EditPart(view);

      case PseudoState4EditPart.VISUAL_ID:
        return new PseudoState4EditPart(view);

      case FinalState2EditPart.VISUAL_ID:
        return new FinalState2EditPart(view);

      case HistoryEditPart.VISUAL_ID:
        return new HistoryEditPart(view);

      case History2EditPart.VISUAL_ID:
        return new History2EditPart(view);

      case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
        return new CompositeStateCompositeStateCompartmentEditPart(view);

      case CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID:
        return new CompositeStateCompositeStateCompartment2EditPart(view);

      case TransitionEditPart.VISUAL_ID:
        return new TransitionEditPart(view);

      }
    }
    return createUnrecognizedEditPart(context, model);
  }

  /**
   * @generated
   */
  private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
    // Handle creation of unrecognized child node EditParts here
    return null;
  }

  /**
   * @generated
   */
  public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
    return CellEditorLocatorAccess.INSTANCE.getTextCellEditorLocator(source);
  }

}
