package repast.simphony.systemdynamics.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import repast.simphony.systemdynamics.diagram.edit.policies.SystemdynamicsBaseItemSemanticEditPolicy;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

/**
 * @generated
 */
public class RateReorientCommand extends EditElementCommand {

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
  public RateReorientCommand(ReorientRelationshipRequest request) {
    super(request.getLabel(), request.getRelationship(), request);
    reorientDirection = request.getDirection();
    oldEnd = request.getOldRelationshipEnd();
    newEnd = request.getNewRelationshipEnd();
  }

  /**
   * @generated
   */
  public boolean canExecute() {
    if (false == getElementToEdit() instanceof Rate) {
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
    if (!(oldEnd instanceof Stock && newEnd instanceof Stock)) {
      return false;
    }
    Stock target = getLink().getTo();
    if (!(getLink().eContainer() instanceof SystemModel)) {
      return false;
    }
    SystemModel container = (SystemModel) getLink().eContainer();
    return SystemdynamicsBaseItemSemanticEditPolicy.getLinkConstraints().canExistRate_4003(
        container, getLink(), getNewSource(), target);
  }

  /**
   * @generated
   */
  protected boolean canReorientTarget() {
    if (!(oldEnd instanceof Stock && newEnd instanceof Stock)) {
      return false;
    }
    Stock source = getLink().getFrom();
    if (!(getLink().eContainer() instanceof SystemModel)) {
      return false;
    }
    SystemModel container = (SystemModel) getLink().eContainer();
    return SystemdynamicsBaseItemSemanticEditPolicy.getLinkConstraints().canExistRate_4003(
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
  protected Rate getLink() {
    return (Rate) getElementToEdit();
  }

  /**
   * @generated
   */
  protected Stock getOldSource() {
    return (Stock) oldEnd;
  }

  /**
   * @generated
   */
  protected Stock getNewSource() {
    return (Stock) newEnd;
  }

  /**
   * @generated
   */
  protected Stock getOldTarget() {
    return (Stock) oldEnd;
  }

  /**
   * @generated
   */
  protected Stock getNewTarget() {
    return (Stock) newEnd;
  }
}
