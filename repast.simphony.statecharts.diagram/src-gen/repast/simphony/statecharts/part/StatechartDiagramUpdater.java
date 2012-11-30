package repast.simphony.statecharts.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.update.DiagramUpdater;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
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
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.providers.StatechartElementTypes;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.FinalState;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * @generated
 */
public class StatechartDiagramUpdater {

  /**
   * @generated
   */
  public static List<StatechartNodeDescriptor> getSemanticChildren(View view) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case StateMachineEditPart.VISUAL_ID:
      return getStateMachine_1000SemanticChildren(view);
    case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
      return getCompositeStateCompositeStateCompartment_7001SemanticChildren(view);
    case CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID:
      return getCompositeStateCompositeStateCompartment_7002SemanticChildren(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartNodeDescriptor> getStateMachine_1000SemanticChildren(View view) {
    if (!view.isSetElement()) {
      return Collections.emptyList();
    }
    StateMachine modelElement = (StateMachine) view.getElement();
    LinkedList<StatechartNodeDescriptor> result = new LinkedList<StatechartNodeDescriptor>();
    for (Iterator<?> it = modelElement.getStates().iterator(); it.hasNext();) {
      AbstractState childElement = (AbstractState) it.next();
      int visualID = StatechartVisualIDRegistry.getNodeVisualID(view, childElement);
      if (visualID == StateEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == CompositeStateEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoStateEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState5EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == FinalStateEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
    }
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartNodeDescriptor> getCompositeStateCompositeStateCompartment_7001SemanticChildren(
      View view) {
    if (false == view.eContainer() instanceof View) {
      return Collections.emptyList();
    }
    View containerView = (View) view.eContainer();
    if (!containerView.isSetElement()) {
      return Collections.emptyList();
    }
    CompositeState modelElement = (CompositeState) containerView.getElement();
    LinkedList<StatechartNodeDescriptor> result = new LinkedList<StatechartNodeDescriptor>();
    for (Iterator<?> it = modelElement.getChildren().iterator(); it.hasNext();) {
      AbstractState childElement = (AbstractState) it.next();
      int visualID = StatechartVisualIDRegistry.getNodeVisualID(view, childElement);
      if (visualID == State2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == CompositeState2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState3EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState4EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == FinalState2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == HistoryEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == History2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
    }
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartNodeDescriptor> getCompositeStateCompositeStateCompartment_7002SemanticChildren(
      View view) {
    if (false == view.eContainer() instanceof View) {
      return Collections.emptyList();
    }
    View containerView = (View) view.eContainer();
    if (!containerView.isSetElement()) {
      return Collections.emptyList();
    }
    CompositeState modelElement = (CompositeState) containerView.getElement();
    LinkedList<StatechartNodeDescriptor> result = new LinkedList<StatechartNodeDescriptor>();
    for (Iterator<?> it = modelElement.getChildren().iterator(); it.hasNext();) {
      AbstractState childElement = (AbstractState) it.next();
      int visualID = StatechartVisualIDRegistry.getNodeVisualID(view, childElement);
      if (visualID == State2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == CompositeState2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState3EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == PseudoState4EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == FinalState2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == HistoryEditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == History2EditPart.VISUAL_ID) {
        result.add(new StatechartNodeDescriptor(childElement, visualID));
        continue;
      }
    }
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getContainedLinks(View view) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case StateMachineEditPart.VISUAL_ID:
      return getStateMachine_1000ContainedLinks(view);
    case StateEditPart.VISUAL_ID:
      return getState_2003ContainedLinks(view);
    case CompositeStateEditPart.VISUAL_ID:
      return getCompositeState_2004ContainedLinks(view);
    case PseudoStateEditPart.VISUAL_ID:
      return getPseudoState_2005ContainedLinks(view);
    case PseudoState2EditPart.VISUAL_ID:
      return getPseudoState_2006ContainedLinks(view);
    case PseudoState5EditPart.VISUAL_ID:
      return getPseudoState_2007ContainedLinks(view);
    case FinalStateEditPart.VISUAL_ID:
      return getFinalState_2008ContainedLinks(view);
    case State2EditPart.VISUAL_ID:
      return getState_3001ContainedLinks(view);
    case CompositeState2EditPart.VISUAL_ID:
      return getCompositeState_3002ContainedLinks(view);
    case PseudoState3EditPart.VISUAL_ID:
      return getPseudoState_3003ContainedLinks(view);
    case PseudoState4EditPart.VISUAL_ID:
      return getPseudoState_3006ContainedLinks(view);
    case FinalState2EditPart.VISUAL_ID:
      return getFinalState_3007ContainedLinks(view);
    case HistoryEditPart.VISUAL_ID:
      return getHistory_3008ContainedLinks(view);
    case History2EditPart.VISUAL_ID:
      return getHistory_3009ContainedLinks(view);
    case TransitionEditPart.VISUAL_ID:
      return getTransition_4001ContainedLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getIncomingLinks(View view) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case StateEditPart.VISUAL_ID:
      return getState_2003IncomingLinks(view);
    case CompositeStateEditPart.VISUAL_ID:
      return getCompositeState_2004IncomingLinks(view);
    case PseudoStateEditPart.VISUAL_ID:
      return getPseudoState_2005IncomingLinks(view);
    case PseudoState2EditPart.VISUAL_ID:
      return getPseudoState_2006IncomingLinks(view);
    case PseudoState5EditPart.VISUAL_ID:
      return getPseudoState_2007IncomingLinks(view);
    case FinalStateEditPart.VISUAL_ID:
      return getFinalState_2008IncomingLinks(view);
    case State2EditPart.VISUAL_ID:
      return getState_3001IncomingLinks(view);
    case CompositeState2EditPart.VISUAL_ID:
      return getCompositeState_3002IncomingLinks(view);
    case PseudoState3EditPart.VISUAL_ID:
      return getPseudoState_3003IncomingLinks(view);
    case PseudoState4EditPart.VISUAL_ID:
      return getPseudoState_3006IncomingLinks(view);
    case FinalState2EditPart.VISUAL_ID:
      return getFinalState_3007IncomingLinks(view);
    case HistoryEditPart.VISUAL_ID:
      return getHistory_3008IncomingLinks(view);
    case History2EditPart.VISUAL_ID:
      return getHistory_3009IncomingLinks(view);
    case TransitionEditPart.VISUAL_ID:
      return getTransition_4001IncomingLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getOutgoingLinks(View view) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case StateEditPart.VISUAL_ID:
      return getState_2003OutgoingLinks(view);
    case CompositeStateEditPart.VISUAL_ID:
      return getCompositeState_2004OutgoingLinks(view);
    case PseudoStateEditPart.VISUAL_ID:
      return getPseudoState_2005OutgoingLinks(view);
    case PseudoState2EditPart.VISUAL_ID:
      return getPseudoState_2006OutgoingLinks(view);
    case PseudoState5EditPart.VISUAL_ID:
      return getPseudoState_2007OutgoingLinks(view);
    case FinalStateEditPart.VISUAL_ID:
      return getFinalState_2008OutgoingLinks(view);
    case State2EditPart.VISUAL_ID:
      return getState_3001OutgoingLinks(view);
    case CompositeState2EditPart.VISUAL_ID:
      return getCompositeState_3002OutgoingLinks(view);
    case PseudoState3EditPart.VISUAL_ID:
      return getPseudoState_3003OutgoingLinks(view);
    case PseudoState4EditPart.VISUAL_ID:
      return getPseudoState_3006OutgoingLinks(view);
    case FinalState2EditPart.VISUAL_ID:
      return getFinalState_3007OutgoingLinks(view);
    case HistoryEditPart.VISUAL_ID:
      return getHistory_3008OutgoingLinks(view);
    case History2EditPart.VISUAL_ID:
      return getHistory_3009OutgoingLinks(view);
    case TransitionEditPart.VISUAL_ID:
      return getTransition_4001OutgoingLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getStateMachine_1000ContainedLinks(View view) {
    StateMachine modelElement = (StateMachine) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getContainedTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_2003ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_2004ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2005ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2006ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2007ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_2008ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_3001ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_3002ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3003ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3006ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_3007ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3008ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3009ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getTransition_4001ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_2003IncomingLinks(View view) {
    State modelElement = (State) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_2004IncomingLinks(View view) {
    CompositeState modelElement = (CompositeState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2005IncomingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2006IncomingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2007IncomingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_2008IncomingLinks(View view) {
    FinalState modelElement = (FinalState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_3001IncomingLinks(View view) {
    State modelElement = (State) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_3002IncomingLinks(View view) {
    CompositeState modelElement = (CompositeState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3003IncomingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3006IncomingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_3007IncomingLinks(View view) {
    FinalState modelElement = (FinalState) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3008IncomingLinks(View view) {
    History modelElement = (History) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3009IncomingLinks(View view) {
    History modelElement = (History) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Transition_4001(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getTransition_4001IncomingLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_2003OutgoingLinks(View view) {
    State modelElement = (State) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_2004OutgoingLinks(View view) {
    CompositeState modelElement = (CompositeState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2005OutgoingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2006OutgoingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_2007OutgoingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_2008OutgoingLinks(View view) {
    FinalState modelElement = (FinalState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getState_3001OutgoingLinks(View view) {
    State modelElement = (State) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getCompositeState_3002OutgoingLinks(View view) {
    CompositeState modelElement = (CompositeState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3003OutgoingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getPseudoState_3006OutgoingLinks(View view) {
    PseudoState modelElement = (PseudoState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getFinalState_3007OutgoingLinks(View view) {
    FinalState modelElement = (FinalState) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3008OutgoingLinks(View view) {
    History modelElement = (History) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getHistory_3009OutgoingLinks(View view) {
    History modelElement = (History) view.getElement();
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Transition_4001(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<StatechartLinkDescriptor> getTransition_4001OutgoingLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  private static Collection<StatechartLinkDescriptor> getContainedTypeModelFacetLinks_Transition_4001(
      StateMachine container) {
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    for (Iterator<?> links = container.getTransitions().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof Transition) {
        continue;
      }
      Transition link = (Transition) linkObject;
      if (TransitionEditPart.VISUAL_ID != StatechartVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      AbstractState dst = link.getTo();
      AbstractState src = link.getFrom();
      result.add(new StatechartLinkDescriptor(src, dst, link,
          StatechartElementTypes.Transition_4001, TransitionEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<StatechartLinkDescriptor> getIncomingTypeModelFacetLinks_Transition_4001(
      AbstractState target, Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
    for (EStructuralFeature.Setting setting : settings) {
      if (setting.getEStructuralFeature() != StatechartPackage.eINSTANCE.getTransition_To()
          || false == setting.getEObject() instanceof Transition) {
        continue;
      }
      Transition link = (Transition) setting.getEObject();
      if (TransitionEditPart.VISUAL_ID != StatechartVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      AbstractState src = link.getFrom();
      result.add(new StatechartLinkDescriptor(src, target, link,
          StatechartElementTypes.Transition_4001, TransitionEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<StatechartLinkDescriptor> getOutgoingTypeModelFacetLinks_Transition_4001(
      AbstractState source) {
    StateMachine container = null;
    // Find container element for the link.
    // Climb up by containment hierarchy starting from the source
    // and return the first element that is instance of the container class.
    for (EObject element = source; element != null && container == null; element = element
        .eContainer()) {
      if (element instanceof StateMachine) {
        container = (StateMachine) element;
      }
    }
    if (container == null) {
      return Collections.emptyList();
    }
    LinkedList<StatechartLinkDescriptor> result = new LinkedList<StatechartLinkDescriptor>();
    for (Iterator<?> links = container.getTransitions().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof Transition) {
        continue;
      }
      Transition link = (Transition) linkObject;
      if (TransitionEditPart.VISUAL_ID != StatechartVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      AbstractState dst = link.getTo();
      AbstractState src = link.getFrom();
      if (src != source) {
        continue;
      }
      result.add(new StatechartLinkDescriptor(src, dst, link,
          StatechartElementTypes.Transition_4001, TransitionEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  public static final DiagramUpdater TYPED_INSTANCE = new DiagramUpdater() {
    /**
     * @generated
     */
    @Override
    public List<StatechartNodeDescriptor> getSemanticChildren(View view) {
      return StatechartDiagramUpdater.getSemanticChildren(view);
    }

    /**
     * @generated
     */
    @Override
    public List<StatechartLinkDescriptor> getContainedLinks(View view) {
      return StatechartDiagramUpdater.getContainedLinks(view);
    }

    /**
     * @generated
     */
    @Override
    public List<StatechartLinkDescriptor> getIncomingLinks(View view) {
      return StatechartDiagramUpdater.getIncomingLinks(view);
    }

    /**
     * @generated
     */
    @Override
    public List<StatechartLinkDescriptor> getOutgoingLinks(View view) {
      return StatechartDiagramUpdater.getOutgoingLinks(view);
    }
  };

}
