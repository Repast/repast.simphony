package repast.simphony.statecharts.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

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
import repast.simphony.statecharts.part.Messages;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

/**
 * @generated
 */
public class StatechartModelingAssistantProvider extends ModelingAssistantProvider {

  /**
   * @generated
   */
  public List getTypesForPopupBar(IAdaptable host) {
    IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
    if (editPart instanceof StateMachineEditPart) {
      ArrayList<IElementType> types = new ArrayList<IElementType>(6);
      types.add(StatechartElementTypes.State_2003);
      types.add(StatechartElementTypes.CompositeState_2004);
      types.add(StatechartElementTypes.PseudoState_2005);
      types.add(StatechartElementTypes.PseudoState_2006);
      types.add(StatechartElementTypes.PseudoState_2007);
      types.add(StatechartElementTypes.FinalState_2008);
      return types;
    }
    if (editPart instanceof CompositeStateCompositeStateCompartmentEditPart) {
      ArrayList<IElementType> types = new ArrayList<IElementType>(7);
      types.add(StatechartElementTypes.State_3001);
      types.add(StatechartElementTypes.CompositeState_3002);
      types.add(StatechartElementTypes.PseudoState_3003);
      types.add(StatechartElementTypes.PseudoState_3006);
      types.add(StatechartElementTypes.FinalState_3007);
      types.add(StatechartElementTypes.History_3008);
      types.add(StatechartElementTypes.History_3009);
      return types;
    }
    if (editPart instanceof CompositeStateCompositeStateCompartment2EditPart) {
      ArrayList<IElementType> types = new ArrayList<IElementType>(7);
      types.add(StatechartElementTypes.State_3001);
      types.add(StatechartElementTypes.CompositeState_3002);
      types.add(StatechartElementTypes.PseudoState_3003);
      types.add(StatechartElementTypes.PseudoState_3006);
      types.add(StatechartElementTypes.FinalState_3007);
      types.add(StatechartElementTypes.History_3008);
      types.add(StatechartElementTypes.History_3009);
      return types;
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public List getRelTypesOnSource(IAdaptable source) {
    IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
        .getAdapter(IGraphicalEditPart.class);
    if (sourceEditPart instanceof StateEditPart) {
      return ((StateEditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof CompositeStateEditPart) {
      return ((CompositeStateEditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof PseudoStateEditPart) {
      return ((PseudoStateEditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof PseudoState2EditPart) {
      return ((PseudoState2EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof PseudoState5EditPart) {
      return ((PseudoState5EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof FinalStateEditPart) {
      return ((FinalStateEditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof State2EditPart) {
      return ((State2EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof CompositeState2EditPart) {
      return ((CompositeState2EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof PseudoState3EditPart) {
      return ((PseudoState3EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof PseudoState4EditPart) {
      return ((PseudoState4EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof FinalState2EditPart) {
      return ((FinalState2EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof HistoryEditPart) {
      return ((HistoryEditPart) sourceEditPart).getMARelTypesOnSource();
    }
    if (sourceEditPart instanceof History2EditPart) {
      return ((History2EditPart) sourceEditPart).getMARelTypesOnSource();
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public List getRelTypesOnTarget(IAdaptable target) {
    IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
        .getAdapter(IGraphicalEditPart.class);
    if (targetEditPart instanceof StateEditPart) {
      return ((StateEditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof CompositeStateEditPart) {
      return ((CompositeStateEditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof PseudoStateEditPart) {
      return ((PseudoStateEditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof PseudoState2EditPart) {
      return ((PseudoState2EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof PseudoState5EditPart) {
      return ((PseudoState5EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof FinalStateEditPart) {
      return ((FinalStateEditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof State2EditPart) {
      return ((State2EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof CompositeState2EditPart) {
      return ((CompositeState2EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof PseudoState3EditPart) {
      return ((PseudoState3EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof PseudoState4EditPart) {
      return ((PseudoState4EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof FinalState2EditPart) {
      return ((FinalState2EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof HistoryEditPart) {
      return ((HistoryEditPart) targetEditPart).getMARelTypesOnTarget();
    }
    if (targetEditPart instanceof History2EditPart) {
      return ((History2EditPart) targetEditPart).getMARelTypesOnTarget();
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
    IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
        .getAdapter(IGraphicalEditPart.class);
    IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
        .getAdapter(IGraphicalEditPart.class);
    if (sourceEditPart instanceof StateEditPart) {
      return ((StateEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof CompositeStateEditPart) {
      return ((CompositeStateEditPart) sourceEditPart)
          .getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof PseudoStateEditPart) {
      return ((PseudoStateEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof PseudoState2EditPart) {
      return ((PseudoState2EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof PseudoState5EditPart) {
      return ((PseudoState5EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof FinalStateEditPart) {
      return ((FinalStateEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof State2EditPart) {
      return ((State2EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof CompositeState2EditPart) {
      return ((CompositeState2EditPart) sourceEditPart)
          .getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof PseudoState3EditPart) {
      return ((PseudoState3EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof PseudoState4EditPart) {
      return ((PseudoState4EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof FinalState2EditPart) {
      return ((FinalState2EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof HistoryEditPart) {
      return ((HistoryEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    if (sourceEditPart instanceof History2EditPart) {
      return ((History2EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
    IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
        .getAdapter(IGraphicalEditPart.class);
    if (targetEditPart instanceof StateEditPart) {
      return ((StateEditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof CompositeStateEditPart) {
      return ((CompositeStateEditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof PseudoStateEditPart) {
      return ((PseudoStateEditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof PseudoState2EditPart) {
      return ((PseudoState2EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof PseudoState5EditPart) {
      return ((PseudoState5EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof FinalStateEditPart) {
      return ((FinalStateEditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof State2EditPart) {
      return ((State2EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof CompositeState2EditPart) {
      return ((CompositeState2EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof PseudoState3EditPart) {
      return ((PseudoState3EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof PseudoState4EditPart) {
      return ((PseudoState4EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof FinalState2EditPart) {
      return ((FinalState2EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof HistoryEditPart) {
      return ((HistoryEditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    if (targetEditPart instanceof History2EditPart) {
      return ((History2EditPart) targetEditPart).getMATypesForSource(relationshipType);
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
    IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
        .getAdapter(IGraphicalEditPart.class);
    if (sourceEditPart instanceof StateEditPart) {
      return ((StateEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof CompositeStateEditPart) {
      return ((CompositeStateEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof PseudoStateEditPart) {
      return ((PseudoStateEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof PseudoState2EditPart) {
      return ((PseudoState2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof PseudoState5EditPart) {
      return ((PseudoState5EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof FinalStateEditPart) {
      return ((FinalStateEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof State2EditPart) {
      return ((State2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof CompositeState2EditPart) {
      return ((CompositeState2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof PseudoState3EditPart) {
      return ((PseudoState3EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof PseudoState4EditPart) {
      return ((PseudoState4EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof FinalState2EditPart) {
      return ((FinalState2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof HistoryEditPart) {
      return ((HistoryEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    if (sourceEditPart instanceof History2EditPart) {
      return ((History2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * @generated
   */
  public EObject selectExistingElementForSource(IAdaptable target, IElementType relationshipType) {
    return selectExistingElement(target, getTypesForSource(target, relationshipType));
  }

  /**
   * @generated
   */
  public EObject selectExistingElementForTarget(IAdaptable source, IElementType relationshipType) {
    return selectExistingElement(source, getTypesForTarget(source, relationshipType));
  }

  /**
   * @generated
   */
  protected EObject selectExistingElement(IAdaptable host, Collection types) {
    if (types.isEmpty()) {
      return null;
    }
    IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
    if (editPart == null) {
      return null;
    }
    Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
    HashSet<EObject> elements = new HashSet<EObject>();
    for (Iterator<EObject> it = diagram.getElement().eAllContents(); it.hasNext();) {
      EObject element = it.next();
      if (isApplicableElement(element, types)) {
        elements.add(element);
      }
    }
    if (elements.isEmpty()) {
      return null;
    }
    return selectElement((EObject[]) elements.toArray(new EObject[elements.size()]));
  }

  /**
   * @generated
   */
  protected boolean isApplicableElement(EObject element, Collection types) {
    IElementType type = ElementTypeRegistry.getInstance().getElementType(element);
    return types.contains(type);
  }

  /**
   * @generated
   */
  protected EObject selectElement(EObject[] elements) {
    Shell shell = Display.getCurrent().getActiveShell();
    ILabelProvider labelProvider = new AdapterFactoryLabelProvider(StatechartDiagramEditorPlugin
        .getInstance().getItemProvidersAdapterFactory());
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
    dialog.setMessage(Messages.StatechartModelingAssistantProviderMessage);
    dialog.setTitle(Messages.StatechartModelingAssistantProviderTitle);
    dialog.setMultipleSelection(false);
    dialog.setElements(elements);
    EObject selected = null;
    if (dialog.open() == Window.OK) {
      selected = (EObject) dialog.getFirstResult();
    }
    return selected;
  }
}
