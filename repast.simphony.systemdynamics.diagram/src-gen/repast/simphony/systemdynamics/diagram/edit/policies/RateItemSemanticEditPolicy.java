package repast.simphony.systemdynamics.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import repast.simphony.systemdynamics.diagram.edit.commands.InfluenceLinkCreateCommand;
import repast.simphony.systemdynamics.diagram.edit.commands.InfluenceLinkReorientCommand;
import repast.simphony.systemdynamics.diagram.edit.parts.InfluenceLinkEditPart;
import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class RateItemSemanticEditPolicy extends SystemdynamicsBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public RateItemSemanticEditPolicy() {
    super(SystemdynamicsElementTypes.Rate_4003);
  }

  /**
   * @generated
   */
  protected Command getDestroyElementCommand(DestroyElementRequest req) {
    return getGEFWrapper(new DestroyElementCommand(req));
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
    if (SystemdynamicsElementTypes.InfluenceLink_4004 == req.getElementType()) {
      return getGEFWrapper(new InfluenceLinkCreateCommand(req, req.getSource(), req.getTarget()));
    }
    return null;
  }

  /**
   * @generated
   */
  protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
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
    case InfluenceLinkEditPart.VISUAL_ID:
      return getGEFWrapper(new InfluenceLinkReorientCommand(req));
    }
    return super.getReorientRelationshipCommand(req);
  }

}
