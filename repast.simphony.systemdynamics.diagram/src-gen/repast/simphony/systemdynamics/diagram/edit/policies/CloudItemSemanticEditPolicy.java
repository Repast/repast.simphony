package repast.simphony.systemdynamics.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.systemdynamics.diagram.edit.commands.InfluenceLinkCreateCommand;
import repast.simphony.systemdynamics.diagram.edit.commands.InfluenceLinkReorientCommand;
import repast.simphony.systemdynamics.diagram.edit.commands.RateCreateCommand;
import repast.simphony.systemdynamics.diagram.edit.commands.RateReorientCommand;
import repast.simphony.systemdynamics.diagram.edit.parts.InfluenceLinkEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry;
import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class CloudItemSemanticEditPolicy extends SystemdynamicsBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public CloudItemSemanticEditPolicy() {
    super(SystemdynamicsElementTypes.Cloud_2002);
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
      if (SystemdynamicsVisualIDRegistry.getVisualID(incomingLink) == RateEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
        continue;
      }
      if (SystemdynamicsVisualIDRegistry.getVisualID(incomingLink) == InfluenceLinkEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
        continue;
      }
    }
    for (Iterator<?> it = view.getSourceEdges().iterator(); it.hasNext();) {
      Edge outgoingLink = (Edge) it.next();
      if (SystemdynamicsVisualIDRegistry.getVisualID(outgoingLink) == RateEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
        continue;
      }
      if (SystemdynamicsVisualIDRegistry.getVisualID(outgoingLink) == InfluenceLinkEditPart.VISUAL_ID) {
        DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
        cmd.add(new DestroyElementCommand(r));
        cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
        continue;
      }
    }
    EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
    if (annotation == null) {
      // there are indirectly referenced children, need extra commands: false
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
  protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
    Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
        : getCompleteCreateRelationshipCommand(req);
    return command != null ? command : super.getCreateRelationshipCommand(req);
  }

  /**
   * @generated
   */
  protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
    if (SystemdynamicsElementTypes.Rate_4003 == req.getElementType()) {
      return getGEFWrapper(new RateCreateCommand(req, req.getSource(), req.getTarget()));
    }
    if (SystemdynamicsElementTypes.InfluenceLink_4004 == req.getElementType()) {
      return getGEFWrapper(new InfluenceLinkCreateCommand(req, req.getSource(), req.getTarget()));
    }
    return null;
  }

  /**
   * @generated
   */
  protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
    if (SystemdynamicsElementTypes.Rate_4003 == req.getElementType()) {
      return getGEFWrapper(new RateCreateCommand(req, req.getSource(), req.getTarget()));
    }
    if (SystemdynamicsElementTypes.InfluenceLink_4004 == req.getElementType()) {
      return getGEFWrapper(new InfluenceLinkCreateCommand(req, req.getSource(), req.getTarget()));
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
    case RateEditPart.VISUAL_ID:
      return getGEFWrapper(new RateReorientCommand(req));
    case InfluenceLinkEditPart.VISUAL_ID:
      return getGEFWrapper(new InfluenceLinkReorientCommand(req));
    }
    return super.getReorientRelationshipCommand(req);
  }

}
