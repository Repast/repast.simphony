package repast.simphony.statecharts.providers;

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

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
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
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * @generated
 */
public class StatechartElementTypes {

  /**
   * @generated
   */
  private StatechartElementTypes() {
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
  public static final IElementType StateMachine_1000 = getElementType("repast.simphony.statecharts.diagram.StateMachine_1000"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType State_2003 = getElementType("repast.simphony.statecharts.diagram.State_2003"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType CompositeState_2004 = getElementType("repast.simphony.statecharts.diagram.CompositeState_2004"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType PseudoState_2005 = getElementType("repast.simphony.statecharts.diagram.PseudoState_2005"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType PseudoState_2006 = getElementType("repast.simphony.statecharts.diagram.PseudoState_2006"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType PseudoState_2007 = getElementType("repast.simphony.statecharts.diagram.PseudoState_2007"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType FinalState_2008 = getElementType("repast.simphony.statecharts.diagram.FinalState_2008"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType State_3001 = getElementType("repast.simphony.statecharts.diagram.State_3001"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType CompositeState_3002 = getElementType("repast.simphony.statecharts.diagram.CompositeState_3002"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType PseudoState_3003 = getElementType("repast.simphony.statecharts.diagram.PseudoState_3003"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType PseudoState_3006 = getElementType("repast.simphony.statecharts.diagram.PseudoState_3006"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType FinalState_3007 = getElementType("repast.simphony.statecharts.diagram.FinalState_3007"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType History_3008 = getElementType("repast.simphony.statecharts.diagram.History_3008"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType History_3009 = getElementType("repast.simphony.statecharts.diagram.History_3009"); //$NON-NLS-1$
  /**
   * @generated
   */
  public static final IElementType Transition_4001 = getElementType("repast.simphony.statecharts.diagram.Transition_4001"); //$NON-NLS-1$

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
        return StatechartDiagramEditorPlugin.getInstance().getItemImageDescriptor(
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
   * @generated NOT
   */
  public static Image getImage(IAdaptable hint) {
    // hint should be a IElementType
    IElementType etype = (IElementType)hint.getAdapter(IElementType.class);
  
    if (etype == null) return null;
    
    ImageRegistry registry = getImageRegistry();
    Image image = registry.get(etype.getIconURL().toString());
    if (image == null) {
      ImageDescriptor desc = ImageDescriptor.createFromURL(etype.getIconURL());
      image = desc.createImage();
      registry.put(etype.getIconURL().toString(), image);
    }
    return image;
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

      elements.put(StateMachine_1000, StatechartPackage.eINSTANCE.getStateMachine());

      elements.put(State_2003, StatechartPackage.eINSTANCE.getState());

      elements.put(CompositeState_2004, StatechartPackage.eINSTANCE.getCompositeState());

      elements.put(PseudoState_2005, StatechartPackage.eINSTANCE.getPseudoState());

      elements.put(PseudoState_2006, StatechartPackage.eINSTANCE.getPseudoState());

      elements.put(PseudoState_2007, StatechartPackage.eINSTANCE.getPseudoState());

      elements.put(FinalState_2008, StatechartPackage.eINSTANCE.getFinalState());

      elements.put(State_3001, StatechartPackage.eINSTANCE.getState());

      elements.put(CompositeState_3002, StatechartPackage.eINSTANCE.getCompositeState());

      elements.put(PseudoState_3003, StatechartPackage.eINSTANCE.getPseudoState());

      elements.put(PseudoState_3006, StatechartPackage.eINSTANCE.getPseudoState());

      elements.put(FinalState_3007, StatechartPackage.eINSTANCE.getFinalState());

      elements.put(History_3008, StatechartPackage.eINSTANCE.getHistory());

      elements.put(History_3009, StatechartPackage.eINSTANCE.getHistory());

      elements.put(Transition_4001, StatechartPackage.eINSTANCE.getTransition());
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
      KNOWN_ELEMENT_TYPES.add(StateMachine_1000);
      KNOWN_ELEMENT_TYPES.add(State_2003);
      KNOWN_ELEMENT_TYPES.add(CompositeState_2004);
      KNOWN_ELEMENT_TYPES.add(PseudoState_2005);
      KNOWN_ELEMENT_TYPES.add(PseudoState_2006);
      KNOWN_ELEMENT_TYPES.add(PseudoState_2007);
      KNOWN_ELEMENT_TYPES.add(FinalState_2008);
      KNOWN_ELEMENT_TYPES.add(State_3001);
      KNOWN_ELEMENT_TYPES.add(CompositeState_3002);
      KNOWN_ELEMENT_TYPES.add(PseudoState_3003);
      KNOWN_ELEMENT_TYPES.add(PseudoState_3006);
      KNOWN_ELEMENT_TYPES.add(FinalState_3007);
      KNOWN_ELEMENT_TYPES.add(History_3008);
      KNOWN_ELEMENT_TYPES.add(History_3009);
      KNOWN_ELEMENT_TYPES.add(Transition_4001);
    }
    return KNOWN_ELEMENT_TYPES.contains(elementType);
  }

  /**
   * @generated
   */
  public static IElementType getElementType(int visualID) {
    switch (visualID) {
    case StateMachineEditPart.VISUAL_ID:
      return StateMachine_1000;
    case StateEditPart.VISUAL_ID:
      return State_2003;
    case CompositeStateEditPart.VISUAL_ID:
      return CompositeState_2004;
    case PseudoStateEditPart.VISUAL_ID:
      return PseudoState_2005;
    case PseudoState2EditPart.VISUAL_ID:
      return PseudoState_2006;
    case PseudoState5EditPart.VISUAL_ID:
      return PseudoState_2007;
    case FinalStateEditPart.VISUAL_ID:
      return FinalState_2008;
    case State2EditPart.VISUAL_ID:
      return State_3001;
    case CompositeState2EditPart.VISUAL_ID:
      return CompositeState_3002;
    case PseudoState3EditPart.VISUAL_ID:
      return PseudoState_3003;
    case PseudoState4EditPart.VISUAL_ID:
      return PseudoState_3006;
    case FinalState2EditPart.VISUAL_ID:
      return FinalState_3007;
    case HistoryEditPart.VISUAL_ID:
      return History_3008;
    case History2EditPart.VISUAL_ID:
      return History_3009;
    case TransitionEditPart.VISUAL_ID:
      return Transition_4001;
    }
    return null;
  }

}
