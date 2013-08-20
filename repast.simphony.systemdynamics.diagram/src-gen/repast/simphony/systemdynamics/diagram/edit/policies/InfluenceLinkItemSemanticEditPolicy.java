package repast.simphony.systemdynamics.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class InfluenceLinkItemSemanticEditPolicy extends SystemdynamicsBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public InfluenceLinkItemSemanticEditPolicy() {
    super(SystemdynamicsElementTypes.InfluenceLink_4004);
  }

  /**
   * @generated
   */
  protected Command getDestroyElementCommand(DestroyElementRequest req) {
    return getGEFWrapper(new DestroyElementCommand(req));
  }

}
