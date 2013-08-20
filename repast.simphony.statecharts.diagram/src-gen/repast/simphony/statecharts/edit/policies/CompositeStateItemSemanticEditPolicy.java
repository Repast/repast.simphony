package repast.simphony.statecharts.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.commands.TransitionCreateCommand;
import repast.simphony.statecharts.edit.commands.TransitionReorientCommand;
import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class CompositeStateItemSemanticEditPolicy extends StatechartBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public CompositeStateItemSemanticEditPolicy() {
    super(StatechartElementTypes.CompositeState_2004);
  }

  /**
   * @generated
   */
  protected Command getDestroyElementCommand(DestroyElementRequest req) {
    View view = (View) getHost().getModel();
    CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
    cmd.setTransactionNestingEnabled(false);
    for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
      Edge incomingLink = (Edge) it.next();
      if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
        continue;
      }
    }
    for (Iterator<?> it = view.getSourceEdges().iterator(); it.hasNext();) {
      Edge outgoingLink = (Edge) it.next();
      if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
        continue;
      }
    }
    EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
    if (annotation == null) {
      // there are indirectly referenced children, need extra commands: false
      addDestroyChildNodesCommand(cmd);
      addDestroyShortcutsCommand(cmd, view);
      // delete host element
      cmd.add(new DestroyElementCommand(req));
    } else {
      cmd.add(new DeleteCommand(getEditingDomain(), view));
    }
    return getGEFWrapper(cmd.reduce());
  }

  /**
   * @generated
   */
  private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
    View view = (View) getHost().getModel();
    for (Iterator<?> nit = view.getChildren().iterator(); nit.hasNext();) {
      Node node = (Node) nit.next();
      switch (StatechartVisualIDRegistry.getVisualID(node)) {
      case CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID:
        for (Iterator<?> cit = node.getChildren().iterator(); cit.hasNext();) {
          Node cnode = (Node) cit.next();
          switch (StatechartVisualIDRegistry.getVisualID(cnode)) {
          case State2EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case CompositeState2EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case PseudoState3EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case PseudoState4EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case FinalState2EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case HistoryEditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          case History2EditPart.VISUAL_ID:
            for (Iterator<?> it = cnode.getTargetEdges().iterator(); it.hasNext();) {
              Edge incomingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(incomingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
              }
            }
            for (Iterator<?> it = cnode.getSourceEdges().iterator(); it.hasNext();) {
              Edge outgoingLink = (Edge) it.next();
              if (StatechartVisualIDRegistry.getVisualID(outgoingLink) == TransitionEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(),
                    false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
              }
            }
            cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode
                .getElement(), false))); // directlyOwned: true
            // don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
            // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
            break;
          }
        }
        break;
      }
    }
  }

  /**
   * @generated
   */
  protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
    Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
        : getCompleteCreateRelationshipCommand(req);
    return command != null ? command : super.getCreateRelationshipCommand(req);
  }

  /**
   * @generated
   */
  protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
    if (StatechartElementTypes.Transition_4001 == req.getElementType()) {
      return getGEFWrapper(new TransitionCreateCommand(req, req.getSource(), req.getTarget()));
    }
    return null;
  }

  /**
   * @generated
   */
  protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
    if (StatechartElementTypes.Transition_4001 == req.getElementType()) {
      return getGEFWrapper(new TransitionCreateCommand(req, req.getSource(), req.getTarget()));
    }
    return null;
  }

  /**
   * Returns command to reorient EClass based link. New link target or source
   * should be the domain model element associated with this node.
   * 
   * @generated
   */
  protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
    switch (getVisualID(req)) {
    case TransitionEditPart.VISUAL_ID:
      return getGEFWrapper(new TransitionReorientCommand(req));
    }
    return super.getReorientRelationshipCommand(req);
  }

}
