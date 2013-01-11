package repast.simphony.systemdynamics.diagram.part;

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

import repast.simphony.systemdynamics.diagram.edit.parts.CloudEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.InfluenceLinkEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.StockEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.Variable2EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableEditPart;
import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;
import repast.simphony.systemdynamics.sdmodel.Cloud;
import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * @generated
 */
public class SystemdynamicsDiagramUpdater {

  /**
   * @generated
   */
  public static List<SystemdynamicsNodeDescriptor> getSemanticChildren(View view) {
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case SystemModelEditPart.VISUAL_ID:
      return getSystemModel_1000SemanticChildren(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsNodeDescriptor> getSystemModel_1000SemanticChildren(View view) {
    if (!view.isSetElement()) {
      return Collections.emptyList();
    }
    SystemModel modelElement = (SystemModel) view.getElement();
    LinkedList<SystemdynamicsNodeDescriptor> result = new LinkedList<SystemdynamicsNodeDescriptor>();
    for (Iterator<?> it = modelElement.getVariables().iterator(); it.hasNext();) {
      Variable childElement = (Variable) it.next();
      int visualID = SystemdynamicsVisualIDRegistry.getNodeVisualID(view, childElement);
      if (visualID == VariableEditPart.VISUAL_ID) {
        result.add(new SystemdynamicsNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == CloudEditPart.VISUAL_ID) {
        result.add(new SystemdynamicsNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == StockEditPart.VISUAL_ID) {
        result.add(new SystemdynamicsNodeDescriptor(childElement, visualID));
        continue;
      }
      if (visualID == Variable2EditPart.VISUAL_ID) {
        result.add(new SystemdynamicsNodeDescriptor(childElement, visualID));
        continue;
      }
    }
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getContainedLinks(View view) {
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case SystemModelEditPart.VISUAL_ID:
      return getSystemModel_1000ContainedLinks(view);
    case VariableEditPart.VISUAL_ID:
      return getVariable_2001ContainedLinks(view);
    case CloudEditPart.VISUAL_ID:
      return getCloud_2002ContainedLinks(view);
    case StockEditPart.VISUAL_ID:
      return getStock_2003ContainedLinks(view);
    case Variable2EditPart.VISUAL_ID:
      return getVariable_2004ContainedLinks(view);
    case RateEditPart.VISUAL_ID:
      return getRate_4003ContainedLinks(view);
    case InfluenceLinkEditPart.VISUAL_ID:
      return getInfluenceLink_4004ContainedLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getIncomingLinks(View view) {
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case VariableEditPart.VISUAL_ID:
      return getVariable_2001IncomingLinks(view);
    case CloudEditPart.VISUAL_ID:
      return getCloud_2002IncomingLinks(view);
    case StockEditPart.VISUAL_ID:
      return getStock_2003IncomingLinks(view);
    case Variable2EditPart.VISUAL_ID:
      return getVariable_2004IncomingLinks(view);
    case RateEditPart.VISUAL_ID:
      return getRate_4003IncomingLinks(view);
    case InfluenceLinkEditPart.VISUAL_ID:
      return getInfluenceLink_4004IncomingLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getOutgoingLinks(View view) {
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case VariableEditPart.VISUAL_ID:
      return getVariable_2001OutgoingLinks(view);
    case CloudEditPart.VISUAL_ID:
      return getCloud_2002OutgoingLinks(view);
    case StockEditPart.VISUAL_ID:
      return getStock_2003OutgoingLinks(view);
    case Variable2EditPart.VISUAL_ID:
      return getVariable_2004OutgoingLinks(view);
    case RateEditPart.VISUAL_ID:
      return getRate_4003OutgoingLinks(view);
    case InfluenceLinkEditPart.VISUAL_ID:
      return getInfluenceLink_4004OutgoingLinks(view);
    }
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getSystemModel_1000ContainedLinks(View view) {
    SystemModel modelElement = (SystemModel) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getContainedTypeModelFacetLinks_Rate_4003(modelElement));
    result.addAll(getContainedTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2001ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getCloud_2002ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getStock_2003ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2004ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getRate_4003ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getInfluenceLink_4004ContainedLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2001IncomingLinks(View view) {
    Variable modelElement = (Variable) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_InfluenceLink_4004(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getCloud_2002IncomingLinks(View view) {
    Cloud modelElement = (Cloud) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Rate_4003(modelElement, crossReferences));
    result.addAll(getIncomingTypeModelFacetLinks_InfluenceLink_4004(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getStock_2003IncomingLinks(View view) {
    Stock modelElement = (Stock) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_Rate_4003(modelElement, crossReferences));
    result.addAll(getIncomingTypeModelFacetLinks_InfluenceLink_4004(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2004IncomingLinks(View view) {
    Variable modelElement = (Variable) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_InfluenceLink_4004(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getRate_4003IncomingLinks(View view) {
    Rate modelElement = (Rate) view.getElement();
    Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
        .find(view.eResource().getResourceSet().getResources());
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getIncomingTypeModelFacetLinks_InfluenceLink_4004(modelElement, crossReferences));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getInfluenceLink_4004IncomingLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2001OutgoingLinks(View view) {
    Variable modelElement = (Variable) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getCloud_2002OutgoingLinks(View view) {
    Cloud modelElement = (Cloud) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Rate_4003(modelElement));
    result.addAll(getOutgoingTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getStock_2003OutgoingLinks(View view) {
    Stock modelElement = (Stock) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_Rate_4003(modelElement));
    result.addAll(getOutgoingTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getVariable_2004OutgoingLinks(View view) {
    Variable modelElement = (Variable) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getRate_4003OutgoingLinks(View view) {
    Rate modelElement = (Rate) view.getElement();
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    result.addAll(getOutgoingTypeModelFacetLinks_InfluenceLink_4004(modelElement));
    return result;
  }

  /**
   * @generated
   */
  public static List<SystemdynamicsLinkDescriptor> getInfluenceLink_4004OutgoingLinks(View view) {
    return Collections.emptyList();
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getContainedTypeModelFacetLinks_Rate_4003(
      SystemModel container) {
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    for (Iterator<?> links = container.getVariables().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof Rate) {
        continue;
      }
      Rate link = (Rate) linkObject;
      if (RateEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      Stock dst = link.getTo();
      Stock src = link.getFrom();
      result.add(new SystemdynamicsLinkDescriptor(src, dst, link,
          SystemdynamicsElementTypes.Rate_4003, RateEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getContainedTypeModelFacetLinks_InfluenceLink_4004(
      SystemModel container) {
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    for (Iterator<?> links = container.getLinks().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof InfluenceLink) {
        continue;
      }
      InfluenceLink link = (InfluenceLink) linkObject;
      if (InfluenceLinkEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry
          .getLinkWithClassVisualID(link)) {
        continue;
      }
      Variable dst = link.getTo();
      Variable src = link.getFrom();
      result.add(new SystemdynamicsLinkDescriptor(src, dst, link,
          SystemdynamicsElementTypes.InfluenceLink_4004, InfluenceLinkEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getIncomingTypeModelFacetLinks_Rate_4003(
      Stock target, Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
    for (EStructuralFeature.Setting setting : settings) {
      if (setting.getEStructuralFeature() != SDModelPackage.eINSTANCE.getRate_To()
          || false == setting.getEObject() instanceof Rate) {
        continue;
      }
      Rate link = (Rate) setting.getEObject();
      if (RateEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      Stock src = link.getFrom();
      result.add(new SystemdynamicsLinkDescriptor(src, target, link,
          SystemdynamicsElementTypes.Rate_4003, RateEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getIncomingTypeModelFacetLinks_InfluenceLink_4004(
      Variable target, Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
    for (EStructuralFeature.Setting setting : settings) {
      if (setting.getEStructuralFeature() != SDModelPackage.eINSTANCE.getInfluenceLink_To()
          || false == setting.getEObject() instanceof InfluenceLink) {
        continue;
      }
      InfluenceLink link = (InfluenceLink) setting.getEObject();
      if (InfluenceLinkEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry
          .getLinkWithClassVisualID(link)) {
        continue;
      }
      Variable src = link.getFrom();
      result.add(new SystemdynamicsLinkDescriptor(src, target, link,
          SystemdynamicsElementTypes.InfluenceLink_4004, InfluenceLinkEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getOutgoingTypeModelFacetLinks_Rate_4003(
      Stock source) {
    SystemModel container = null;
    // Find container element for the link.
    // Climb up by containment hierarchy starting from the source
    // and return the first element that is instance of the container class.
    for (EObject element = source; element != null && container == null; element = element
        .eContainer()) {
      if (element instanceof SystemModel) {
        container = (SystemModel) element;
      }
    }
    if (container == null) {
      return Collections.emptyList();
    }
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    for (Iterator<?> links = container.getVariables().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof Rate) {
        continue;
      }
      Rate link = (Rate) linkObject;
      if (RateEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry.getLinkWithClassVisualID(link)) {
        continue;
      }
      Stock dst = link.getTo();
      Stock src = link.getFrom();
      if (src != source) {
        continue;
      }
      result.add(new SystemdynamicsLinkDescriptor(src, dst, link,
          SystemdynamicsElementTypes.Rate_4003, RateEditPart.VISUAL_ID));
    }
    return result;
  }

  /**
   * @generated
   */
  private static Collection<SystemdynamicsLinkDescriptor> getOutgoingTypeModelFacetLinks_InfluenceLink_4004(
      Variable source) {
    SystemModel container = null;
    // Find container element for the link.
    // Climb up by containment hierarchy starting from the source
    // and return the first element that is instance of the container class.
    for (EObject element = source; element != null && container == null; element = element
        .eContainer()) {
      if (element instanceof SystemModel) {
        container = (SystemModel) element;
      }
    }
    if (container == null) {
      return Collections.emptyList();
    }
    LinkedList<SystemdynamicsLinkDescriptor> result = new LinkedList<SystemdynamicsLinkDescriptor>();
    for (Iterator<?> links = container.getLinks().iterator(); links.hasNext();) {
      EObject linkObject = (EObject) links.next();
      if (false == linkObject instanceof InfluenceLink) {
        continue;
      }
      InfluenceLink link = (InfluenceLink) linkObject;
      if (InfluenceLinkEditPart.VISUAL_ID != SystemdynamicsVisualIDRegistry
          .getLinkWithClassVisualID(link)) {
        continue;
      }
      Variable dst = link.getTo();
      Variable src = link.getFrom();
      if (src != source) {
        continue;
      }
      result.add(new SystemdynamicsLinkDescriptor(src, dst, link,
          SystemdynamicsElementTypes.InfluenceLink_4004, InfluenceLinkEditPart.VISUAL_ID));
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
    public List<SystemdynamicsNodeDescriptor> getSemanticChildren(View view) {
      return SystemdynamicsDiagramUpdater.getSemanticChildren(view);
    }

    /**
     * @generated
     */
    @Override
    public List<SystemdynamicsLinkDescriptor> getContainedLinks(View view) {
      return SystemdynamicsDiagramUpdater.getContainedLinks(view);
    }

    /**
     * @generated
     */
    @Override
    public List<SystemdynamicsLinkDescriptor> getIncomingLinks(View view) {
      return SystemdynamicsDiagramUpdater.getIncomingLinks(view);
    }

    /**
     * @generated
     */
    @Override
    public List<SystemdynamicsLinkDescriptor> getOutgoingLinks(View view) {
      return SystemdynamicsDiagramUpdater.getOutgoingLinks(view);
    }
  };

}
