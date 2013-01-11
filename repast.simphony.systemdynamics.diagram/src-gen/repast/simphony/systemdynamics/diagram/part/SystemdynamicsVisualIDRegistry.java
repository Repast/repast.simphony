package repast.simphony.systemdynamics.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.structure.DiagramStructure;

import repast.simphony.systemdynamics.diagram.edit.parts.CloudEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.InfluenceLinkEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateNameEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.StockEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.StockNameEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.Variable2EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableName2EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableNameEditPart;
import repast.simphony.systemdynamics.diagram.expressions.SystemdynamicsOCLFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class SystemdynamicsVisualIDRegistry {

  /**
   * @generated
   */
  private static final String DEBUG_KEY = "repast.simphony.systemdynamics.diagram/debug/visualID"; //$NON-NLS-1$

  /**
   * @generated
   */
  public static int getVisualID(View view) {
    if (view instanceof Diagram) {
      if (SystemModelEditPart.MODEL_ID.equals(view.getType())) {
        return SystemModelEditPart.VISUAL_ID;
      } else {
        return -1;
      }
    }
    return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
        .getVisualID(view.getType());
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
        SystemdynamicsDiagramEditorPlugin.getInstance().logError(
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
    if (SDModelPackage.eINSTANCE.getSystemModel().isSuperTypeOf(domainElement.eClass())
        && isDiagram((SystemModel) domainElement)) {
      return SystemModelEditPart.VISUAL_ID;
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
    String containerModelID = repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
        .getModelID(containerView);
    if (!SystemModelEditPart.MODEL_ID.equals(containerModelID)) {
      return -1;
    }
    int containerVisualID;
    if (SystemModelEditPart.MODEL_ID.equals(containerModelID)) {
      containerVisualID = repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .getVisualID(containerView);
    } else {
      if (containerView instanceof Diagram) {
        containerVisualID = SystemModelEditPart.VISUAL_ID;
      } else {
        return -1;
      }
    }
    switch (containerVisualID) {
    case SystemModelEditPart.VISUAL_ID:
      if (SDModelPackage.eINSTANCE.getVariable().isSuperTypeOf(domainElement.eClass())
          && isVariable_2001((Variable) domainElement)) {
        return VariableEditPart.VISUAL_ID;
      }
      if (SDModelPackage.eINSTANCE.getCloud().isSuperTypeOf(domainElement.eClass())) {
        return CloudEditPart.VISUAL_ID;
      }
      if (SDModelPackage.eINSTANCE.getStock().isSuperTypeOf(domainElement.eClass())
          && isStock_2003((Stock) domainElement)) {
        return StockEditPart.VISUAL_ID;
      }
      if (SDModelPackage.eINSTANCE.getVariable().isSuperTypeOf(domainElement.eClass())
          && isVariable_2004((Variable) domainElement)) {
        return Variable2EditPart.VISUAL_ID;
      }
      break;
    }
    return -1;
  }

  /**
   * @generated
   */
  public static boolean canCreateNode(View containerView, int nodeVisualID) {
    String containerModelID = repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
        .getModelID(containerView);
    if (!SystemModelEditPart.MODEL_ID.equals(containerModelID)) {
      return false;
    }
    int containerVisualID;
    if (SystemModelEditPart.MODEL_ID.equals(containerModelID)) {
      containerVisualID = repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .getVisualID(containerView);
    } else {
      if (containerView instanceof Diagram) {
        containerVisualID = SystemModelEditPart.VISUAL_ID;
      } else {
        return false;
      }
    }
    switch (containerVisualID) {
    case SystemModelEditPart.VISUAL_ID:
      if (VariableEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (CloudEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (StockEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      if (Variable2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case VariableEditPart.VISUAL_ID:
      if (VariableNameEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case StockEditPart.VISUAL_ID:
      if (StockNameEditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case Variable2EditPart.VISUAL_ID:
      if (VariableName2EditPart.VISUAL_ID == nodeVisualID) {
        return true;
      }
      break;
    case RateEditPart.VISUAL_ID:
      if (RateNameEditPart.VISUAL_ID == nodeVisualID) {
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
    if (SDModelPackage.eINSTANCE.getRate().isSuperTypeOf(domainElement.eClass())) {
      return RateEditPart.VISUAL_ID;
    }
    if (SDModelPackage.eINSTANCE.getInfluenceLink().isSuperTypeOf(domainElement.eClass())) {
      return InfluenceLinkEditPart.VISUAL_ID;
    }
    return -1;
  }

  /**
   * User can change implementation of this method to handle some specific
   * situations not covered by default logic.
   * 
   * @generated
   */
  private static boolean isDiagram(SystemModel element) {
    return true;
  }

  /**
   * @generated
   */
  private static boolean isVariable_2001(Variable domainElement) {
    Object result = SystemdynamicsOCLFactory.getExpression(0,
        SDModelPackage.eINSTANCE.getVariable(), null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isStock_2003(Stock domainElement) {
    Object result = SystemdynamicsOCLFactory.getExpression(2, SDModelPackage.eINSTANCE.getStock(),
        null).evaluate(domainElement);
    return result instanceof Boolean && ((Boolean) result).booleanValue();
  }

  /**
   * @generated
   */
  private static boolean isVariable_2004(Variable domainElement) {
    Object result = SystemdynamicsOCLFactory.getExpression(4,
        SDModelPackage.eINSTANCE.getVariable(), null).evaluate(domainElement);
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
    return false;
  }

  /**
   * @generated
   */
  public static boolean isSemanticLeafVisualID(int visualID) {
    switch (visualID) {
    case SystemModelEditPart.VISUAL_ID:
      return false;
    case VariableEditPart.VISUAL_ID:
    case CloudEditPart.VISUAL_ID:
    case StockEditPart.VISUAL_ID:
    case Variable2EditPart.VISUAL_ID:
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
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .getVisualID(view);
    }

    /**
     * @generated
     */
    @Override
    public String getModelID(View view) {
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .getModelID(view);
    }

    /**
     * @generated
     */
    @Override
    public int getNodeVisualID(View containerView, EObject domainElement) {
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .getNodeVisualID(containerView, domainElement);
    }

    /**
     * @generated
     */
    @Override
    public boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate) {
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .checkNodeVisualID(containerView, domainElement, candidate);
    }

    /**
     * @generated
     */
    @Override
    public boolean isCompartmentVisualID(int visualID) {
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .isCompartmentVisualID(visualID);
    }

    /**
     * @generated
     */
    @Override
    public boolean isSemanticLeafVisualID(int visualID) {
      return repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry
          .isSemanticLeafVisualID(visualID);
    }
  };

}
