package repast.simphony.statecharts.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.structure.DiagramStructure;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateName2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateNameEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.FinalStateEditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState2EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState5EditPart;
import repast.simphony.statecharts.edit.parts.PseudoStateEditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.edit.parts.StateEditPart;
import repast.simphony.statecharts.edit.parts.StateMachineEditPart;
import repast.simphony.statecharts.edit.parts.StateName2EditPart;
import repast.simphony.statecharts.edit.parts.StateNameEditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.expressions.StatechartOCLFactory;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class StatechartVisualIDRegistry {

  /**
   * @generated
   */
  private static final String DEBUG_KEY = "repast.simphony.statecharts.diagram/debug/visualID"; //$NON-NLS-1$

  /**
   * @generated
   */
  public static int getVisualID(View view) {
    if (view instanceof Diagram) {
      if (StateMachineEditPart.MODEL_ID.equals(view.getType())) {
        return StateMachineEditPart.VISUAL_ID;
      } else {
        return -1;
      }
    }
    return repast.simphony.statecharts.part.StatechartVisualIDRegistry.getVisualID(view.getType());
  }

  /**
   * @generated
   */
  public static String getModelID(View view) {
    View diagram = view.getDiagram();
    while (view != diagram) {
      EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
      if (annotation != null) {
        return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
      }
      view = (View) view.eContainer();
    }
    return diagram != null ? diagram.getType() : null;
  }

  /**
   * @generated
   */
  public static int getVisualID(String type) {
    try {
      return Integer.parseInt(type);
    } catch (NumberFormatException e) {
      if (Boolean.TRUE.toString().equalsIgnoreCase(Platform.getDebugOption(DEBUG_KEY))) {
        StatechartDiagramEditorPlugin.getInstance().logError(
            "Unable to parse view type as a visualID number: " + type);
      }
    }
    return -1;
  }

  /**
   * @generated
   */
  public static String getType(int visualID) {
    return Integer.toString(visualID);
  }

  /**
   * @generated
   */
  public static int getDiagramVisualID(EObject domainElement) {
    if (domainElement == null) {
      return -1;
    }
    if (StatechartPackage.eINSTANCE.getStateMachine().isSuperTypeOf(domainElement.eClass())
        && isDiagram((StateMachine) domainElement)) {
      return StateMachineEditPart.VISUAL_ID;
    }
    return -1;
  }

  /**
   * @generated
   */
  public static int getNodeVisualID(View containerView, EObject domainElement) {
    if (domainElement == null) {
      return -1;
    }
    String containerModelID = repast.simphony.statecharts.part.StatechartVisualIDRegistry
        .getModelID(containerView);
    if (!StateMachineEditPart.MODEL_ID.equals(containerModelID)) {
      return -1;
    }
    int containerVisualID;
    if (StateMachineEditPart.MODEL_ID.equals(containerModelID)) {
      containerVisualID = repast.simphony.statecharts.part.StatechartVisualIDRegistry
          .getVisualID(containerView);
    } else {
      if (containerView instanceof Diagram) {
        containerVisualID = StateMachineEditPart.VISUAL_ID;
      } else {
        return -1;
      }
    }
    switch (containerVisualID) {
    case StateMachineEditPart.VISUAL_ID:
      if (StatechartPackage.eINSTANCE.getState().isSuperTypeOf(domainElement.eClass())
          && isState_2003((State) domainElement)) {
        return StateEditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getCompositeState().isSuperTypeOf(domainElement.eClass())) {
        return CompositeStateEditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_2005((PseudoState) domainElement)) {
        return PseudoStateEditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_2006((PseudoState) domainElement)) {
        return PseudoState2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_2007((PseudoState) domainElement)) {
        return PseudoState5EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getFinalState().isSuperTypeOf(domainElement.eClass())) {
        return FinalStateEditPart.VISUAL_ID;
      }
      break;
    case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
      if (StatechartPackage.eINSTANCE.getState().isSuperTypeOf(domainElement.eClass())
          && isState_3001((State) domainElement)) {
        return State2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getCompositeState().isSuperTypeOf(domainElement.eClass())) {
        return CompositeState2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_3003((PseudoState) domainElement)) {
        return PseudoState3EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_3006((PseudoState) domainElement)) {
        return PseudoState4EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getFinalState().isSuperTypeOf(domainElement.eClass())) {
        return FinalState2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getHistory().isSuperTypeOf(domainElement.eClass())
          && isHistory_3008((History) domainElement)) {
        return HistoryEditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getHistory().isSuperTypeOf(domainElement.eClass())
          && isHistory_3009((History) domainElement)) {
        return History2EditPart.VISUAL_ID;
      }
      break;
    case CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID:
      if (StatechartPackage.eINSTANCE.getState().isSuperTypeOf(domainElement.eClass())
          && isState_3001((State) domainElement)) {
        return State2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getCompositeState().isSuperTypeOf(domainElement.eClass())) {
        return CompositeState2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_3003((PseudoState) domainElement)) {
        return PseudoState3EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getPseudoState().isSuperTypeOf(domainElement.eClass())
          && isPseudoState_3006((PseudoState) domainElement)) {
        return PseudoState4EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getFinalState().isSuperTypeOf(domainElement.eClass())) {
        return FinalState2EditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getHistory().isSuperTypeOf(domainElement.eClass())
          && isHistory_3008((History) domainElement)) {
        return HistoryEditPart.VISUAL_ID;
      }
      if (StatechartPackage.eINSTANCE.getHistory().isSuperTypeOf(domainElement.eClass())
          && isHistory_3009((History) domainElement)) {
        return History2EditPart.VISUAL_ID;
      }
      break;
    }
    return -1;
  }

  /**
   * @generated
   */
  public static boolean canCreateNode(View containerView, int nodeVisualID) {
    String containerModelID = repast.simphony.statecharts.part.StatechartVisualIDRegistry
        .getModelID(containerView);
    if (!StateMachineEditPart.MODEL_ID.equals(containerModelID)) {
      return false;
    }
    int containerVisualID;
    if (StateMachineEditPart.MODEL_ID.equals(containerModelID)) {
      containerVisualID = repast.simphony.statecharts.part.StatechartVisualIDRegistry
          .getVisualID(containerView);
    } else {
      if (containerView instanceof Diagram) {
        containerVisualID = StateMachineEditPart.VISUAL_ID;
      } else {
        return false;
      }
    }
    switch (containerVisualID) {
    case StateMachineEditPart.VISUAL_ID:
      if (StateEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CompositeStateEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoStateEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState5EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (FinalStateEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case StateEditPart.VISUAL_ID:
      if (StateNameEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case CompositeStateEditPart.VISUAL_ID:
      if (CompositeStateNameEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case State2EditPart.VISUAL_ID:
      if (StateName2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case CompositeState2EditPart.VISUAL_ID:
      if (CompositeStateName2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
      if (State2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CompositeState2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState3EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState4EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (FinalState2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (HistoryEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (History2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID:
      if (State2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CompositeState2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState3EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (PseudoState4EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (FinalState2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (HistoryEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (History2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    }
    return false;
  }

  /**
   * @generated
   */
  public static int getLinkWithClassVisualID(EObject domainElement) {
    if (domainElement == null) {
      return -1;
    }
    if (StatechartPackage.eINSTANCE.getTransition().isSuperTypeOf(domainElement.eClass())) {
      return TransitionEditPart.VISUAL_ID;
    }
    return -1;
  }

  /**
   * User can change implementation of this method to handle some specific
   * situations not covered by default logic.
   * 
   * @generated
   */
  private static boolean isDiagram(StateMachine element) {
    return true;
  }

  /**
   * @generated
   */
  private static boolean isState_2003(State domainElement) {
    Object result = StatechartOCLFactory.getExpression(0, StatechartPackage.eINSTANCE.getState(),
        null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isPseudoState_2005(PseudoState domainElement) {
    Object result = StatechartOCLFactory.getExpression(1,
        StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isPseudoState_2006(PseudoState domainElement) {
    Object result = StatechartOCLFactory.getExpression(3,
        StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isPseudoState_2007(PseudoState domainElement) {
    Object result = StatechartOCLFactory.getExpression(7,
        StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isState_3001(State domainElement) {
    Object result = StatechartOCLFactory.getExpression(0, StatechartPackage.eINSTANCE.getState(),
        null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isPseudoState_3003(PseudoState domainElement) {
    Object result = StatechartOCLFactory.getExpression(1,
        StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isPseudoState_3006(PseudoState domainElement) {
    Object result = StatechartOCLFactory.getExpression(3,
        StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isHistory_3008(History domainElement) {
    Object result = StatechartOCLFactory.getExpression(5, StatechartPackage.eINSTANCE.getHistory(),
        null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isHistory_3009(History domainElement) {
    Object result = StatechartOCLFactory.getExpression(6, StatechartPackage.eINSTANCE.getHistory(),
        null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  public static boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate) {
    if (candidate == -1) {
      //unrecognized id is always bad
      return false;
    }
    int basic = getNodeVisualID(containerView, domainElement);
    return basic == candidate;
  }

  /**
   * @generated
   */
  public static boolean isCompartmentVisualID(int visualID) {
    switch (visualID) {
    case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
    case CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID:
      return true;
    default:
      break;
    }
    return false;
  }

  /**
   * @generated
   */
  public static boolean isSemanticLeafVisualID(int visualID) {
    switch (visualID) {
    case StateMachineEditPart.VISUAL_ID:
      return false;
    case StateEditPart.VISUAL_ID:
    case PseudoStateEditPart.VISUAL_ID:
    case PseudoState2EditPart.VISUAL_ID:
    case PseudoState5EditPart.VISUAL_ID:
    case FinalStateEditPart.VISUAL_ID:
    case State2EditPart.VISUAL_ID:
    case PseudoState3EditPart.VISUAL_ID:
    case PseudoState4EditPart.VISUAL_ID:
    case FinalState2EditPart.VISUAL_ID:
    case HistoryEditPart.VISUAL_ID:
    case History2EditPart.VISUAL_ID:
      return true;
    default:
      break;
    }
    return false;
  }

  /**
   * @generated
   */
  public static final DiagramStructure TYPED_INSTANCE = new DiagramStructure() {
    /**
     * @generated
     */
    @Override
    public int getVisualID(View view) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry.getVisualID(view);
    }

    /**
     * @generated
     */
    @Override
    public String getModelID(View view) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry.getModelID(view);
    }

    /**
     * @generated
     */
    @Override
    public int getNodeVisualID(View containerView, EObject domainElement) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry.getNodeVisualID(
          containerView, domainElement);
    }

    /**
     * @generated
     */
    @Override
    public boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry.checkNodeVisualID(
          containerView, domainElement, candidate);
    }

    /**
     * @generated
     */
    @Override
    public boolean isCompartmentVisualID(int visualID) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry
          .isCompartmentVisualID(visualID);
    }

    /**
     * @generated
     */
    @Override
    public boolean isSemanticLeafVisualID(int visualID) {
      return repast.simphony.statecharts.part.StatechartVisualIDRegistry
          .isSemanticLeafVisualID(visualID);
    }
  };

}
