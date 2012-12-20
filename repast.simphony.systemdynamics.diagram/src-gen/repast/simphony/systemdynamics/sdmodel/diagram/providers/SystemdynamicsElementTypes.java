package repast.simphony.systemdynamics.sdmodel.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.CausalLinkEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.CloudEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.StockEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.VariableEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @generated
 */
public class SystemdynamicsElementTypes {

  /**
   * @generated
   */
  private SystemdynamicsElementTypes() {
  }

  /**
   * @generated
   */
  private static Map<IElementType, ENamedElement> elements;

  /**
   * @generated
   */
  private static ImageRegistry imageRegistry;

  /**
   * @generated
   */
  private static Set<IElementType> KNOWN_ELEMENT_TYPES;

  /**
   * @generated
   */
  public static final IElementType SystemModel_1000 = getElementType("repast.simphony.systemdynamics.diagram.SystemModel_1000"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType Variable_2001 = getElementType("repast.simphony.systemdynamics.diagram.Variable_2001"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType Cloud_2002 = getElementType("repast.simphony.systemdynamics.diagram.Cloud_2002"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType Stock_2003 = getElementType("repast.simphony.systemdynamics.diagram.Stock_2003"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType Rate_4003 = getElementType("repast.simphony.systemdynamics.diagram.Rate_4003"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType CausalLink_4002 = getElementType("repast.simphony.systemdynamics.diagram.CausalLink_4002"); //$NON-NLS-1$

  /**
   * @generated
   */
  private static ImageRegistry getImageRegistry() {
    if (imageRegistry == null) {
      imageRegistry = new ImageRegistry();
    }
    return imageRegistry;
  }

  /**
   * @generated
   */
  private static String getImageRegistryKey(ENamedElement element) {
    return element.getName();
  }

  /**
   * @generated
   */
  private static ImageDescriptor getProvidedImageDescriptor(ENamedElement element) {
    if (element instanceof EStructuralFeature) {
      EStructuralFeature feature = ((EStructuralFeature) element);
      EClass eContainingClass = feature.getEContainingClass();
      EClassifier eType = feature.getEType();
      if (eContainingClass != null && !eContainingClass.isAbstract()) {
        element = eContainingClass;
      } else if (eType instanceof EClass && !((EClass) eType).isAbstract()) {
        element = eType;
      }
    }
    if (element instanceof EClass) {
      EClass eClass = (EClass) element;
      if (!eClass.isAbstract()) {
        return SystemdynamicsDiagramEditorPlugin.getInstance().getItemImageDescriptor(
            eClass.getEPackage().getEFactoryInstance().create(eClass));
      }
    }
    // TODO : support structural features
    return null;
  }

  /**
   * @generated
   */
  public static ImageDescriptor getImageDescriptor(ENamedElement element) {
    String key = getImageRegistryKey(element);
    ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
    if (imageDescriptor == null) {
      imageDescriptor = getProvidedImageDescriptor(element);
      if (imageDescriptor == null) {
        imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
      }
      getImageRegistry().put(key, imageDescriptor);
    }
    return imageDescriptor;
  }

  /**
   * @generated
   */
  public static Image getImage(ENamedElement element) {
    String key = getImageRegistryKey(element);
    Image image = getImageRegistry().get(key);
    if (image == null) {
      ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
      if (imageDescriptor == null) {
        imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
      }
      getImageRegistry().put(key, imageDescriptor);
      image = getImageRegistry().get(key);
    }
    return image;
  }

  /**
   * @generated
   */
  public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
    ENamedElement element = getElement(hint);
    if (element == null) {
      return null;
    }
    return getImageDescriptor(element);
  }

  /**
   * @generated
   */
  public static Image getImage(IAdaptable hint) {
    ENamedElement element = getElement(hint);
    if (element == null) {
      return null;
    }
    return getImage(element);
  }

  /**
   * Returns 'type' of the ecore object associated with the hint.
   * 
   * @generated
   */
  public static ENamedElement getElement(IAdaptable hint) {
    Object type = hint.getAdapter(IElementType.class);
    if (elements == null) {
      elements = new IdentityHashMap<IElementType, ENamedElement>();

      elements.put(SystemModel_1000, SDModelPackage.eINSTANCE.getSystemModel());

      elements.put(Variable_2001, SDModelPackage.eINSTANCE.getVariable());

      elements.put(Cloud_2002, SDModelPackage.eINSTANCE.getCloud());

      elements.put(Stock_2003, SDModelPackage.eINSTANCE.getStock());

      elements.put(Rate_4003, SDModelPackage.eINSTANCE.getRate());

      elements.put(CausalLink_4002, SDModelPackage.eINSTANCE.getCausalLink());
    }
    return (ENamedElement) elements.get(type);
  }

  /**
   * @generated
   */
  private static IElementType getElementType(String id) {
    return ElementTypeRegistry.getInstance().getType(id);
  }

  /**
   * @generated
   */
  public static boolean isKnownElementType(IElementType elementType) {
    if (KNOWN_ELEMENT_TYPES == null) {
      KNOWN_ELEMENT_TYPES = new HashSet<IElementType>();
      KNOWN_ELEMENT_TYPES.add(SystemModel_1000);
      KNOWN_ELEMENT_TYPES.add(Variable_2001);
      KNOWN_ELEMENT_TYPES.add(Cloud_2002);
      KNOWN_ELEMENT_TYPES.add(Stock_2003);
      KNOWN_ELEMENT_TYPES.add(Rate_4003);
      KNOWN_ELEMENT_TYPES.add(CausalLink_4002);
    }
    return KNOWN_ELEMENT_TYPES.contains(elementType);
  }

  /**
   * @generated
   */
  public static IElementType getElementType(int visualID) {
    switch (visualID) {
    case SystemModelEditPart.VISUAL_ID:
      return SystemModel_1000;
    case VariableEditPart.VISUAL_ID:
      return Variable_2001;
    case CloudEditPart.VISUAL_ID:
      return Cloud_2002;
    case StockEditPart.VISUAL_ID:
      return Stock_2003;
    case RateEditPart.VISUAL_ID:
      return Rate_4003;
    case CausalLinkEditPart.VISUAL_ID:
      return CausalLink_4002;
    }
    return null;
  }

}
