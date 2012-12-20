package repast.simphony.systemdynamics.sdmodel.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.CausalLinkCreateCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.CausalLinkReorientCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.RateCreateCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.RateReorientCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.CausalLinkEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.providers.SystemdynamicsElementTypes;

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
    if (SystemdynamicsElementTypes.Rate_4003 == req.getElementType()) {
      return getGEFWrapper(new RateCreateCommand(req, req.getSource(), req.getTarget()));
    }
    if (SystemdynamicsElementTypes.CausalLink_4002 == req.getElementType()) {
      return getGEFWrapper(new CausalLinkCreateCommand(req, req.getSource(), req.getTarget()));
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
    if (SystemdynamicsElementTypes.CausalLink_4002 == req.getElementType()) {
      return getGEFWrapper(new CausalLinkCreateCommand(req, req.getSource(), req.getTarget()));
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
    case CausalLinkEditPart.VISUAL_ID:
      return getGEFWrapper(new CausalLinkReorientCommand(req));
    }
    return super.getReorientRelationshipCommand(req);
  }

}
