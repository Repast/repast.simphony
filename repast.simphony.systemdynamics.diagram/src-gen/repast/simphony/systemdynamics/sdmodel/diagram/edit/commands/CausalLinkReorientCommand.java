package repast.simphony.systemdynamics.sdmodel.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import repast.simphony.systemdynamics.sdmodel.AbstractVariable;
import repast.simphony.systemdynamics.sdmodel.CausalLink;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.policies.SystemdynamicsBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class CausalLinkReorientCommand extends EditElementCommand {

  /**
   * @generated
   */
  private final int reorientDirection;

  /**
   * @generated
   */
  private final EObject oldEnd;

  /**
   * @generated
   */
  private final EObject newEnd;

  /**
   * @generated
   */
  public CausalLinkReorientCommand(ReorientRelationshipRequest request) {
    super(request.getLabel(), request.getRelationship(), request);
    reorientDirection = request.getDirection();
    oldEnd = request.getOldRelationshipEnd();
    newEnd = request.getNewRelationshipEnd();
  }

  /**
   * @generated
   */
  public boolean canExecute() {
    if (false == getElementToEdit() instanceof CausalLink) {
      return false;
    }
    if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
      return canReorientSource();
    }
    if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
      return canReorientTarget();
    }
    return false;
  }

  /**
   * @generated
   */
  protected boolean canReorientSource() {
    if (!(oldEnd instanceof AbstractVariable && newEnd instanceof AbstractVariable)) {
      return false;
    }
    AbstractVariable target = getLink().getTo();
    if (!(getLink().eContainer() instanceof SystemModel)) {
      return false;
    }
    SystemModel container = (SystemModel) getLink().eContainer();
    return SystemdynamicsBaseItemSemanticEditPolicy.getLinkConstraints().canExistCausalLink_4002(
        container, getLink(), getNewSource(), target);
  }

  /**
   * @generated
   */
  protected boolean canReorientTarget() {
    if (!(oldEnd instanceof AbstractVariable && newEnd instanceof AbstractVariable)) {
      return false;
    }
    AbstractVariable source = getLink().getFrom();
    if (!(getLink().eContainer() instanceof SystemModel)) {
      return false;
    }
    SystemModel container = (SystemModel) getLink().eContainer();
    return SystemdynamicsBaseItemSemanticEditPolicy.getLinkConstraints().canExistCausalLink_4002(
        container, getLink(), source, getNewTarget());
  }

  /**
   * @generated
   */
  protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
      throws ExecutionException {
    if (!canExecute()) {
      throw new ExecutionException("Invalid arguments in reorient link command"); //$NON-NLS-1$
    }
    if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
      return reorientSource();
    }
    if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
      return reorientTarget();
    }
    throw new IllegalStateException();
  }

  /**
   * @generated
   */
  protected CommandResult reorientSource() throws ExecutionException {
    getLink().setFrom(getNewSource());
    return CommandResult.newOKCommandResult(getLink());
  }

  /**
   * @generated
   */
  protected CommandResult reorientTarget() throws ExecutionException {
    getLink().setTo(getNewTarget());
    return CommandResult.newOKCommandResult(getLink());
  }

  /**
   * @generated
   */
  protected CausalLink getLink() {
    return (CausalLink) getElementToEdit();
  }

  /**
   * @generated
   */
  protected AbstractVariable getOldSource() {
    return (AbstractVariable) oldEnd;
  }

  /**
   * @generated
   */
  protected AbstractVariable getNewSource() {
    return (AbstractVariable) newEnd;
  }

  /**
   * @generated
   */
  protected AbstractVariable getOldTarget() {
    return (AbstractVariable) oldEnd;
  }

  /**
   * @generated
   */
  protected AbstractVariable getNewTarget() {
    return (AbstractVariable) newEnd;
  }
}
