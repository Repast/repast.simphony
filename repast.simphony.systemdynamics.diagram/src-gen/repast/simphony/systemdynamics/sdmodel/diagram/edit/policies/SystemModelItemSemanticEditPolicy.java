package repast.simphony.systemdynamics.sdmodel.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.CloudCreateCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.StockCreateCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.commands.VariableCreateCommand;
import repast.simphony.systemdynamics.sdmodel.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class SystemModelItemSemanticEditPolicy extends SystemdynamicsBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public SystemModelItemSemanticEditPolicy() {
    super(SystemdynamicsElementTypes.SystemModel_1000);
  }

  /**
   * @generated
   */
  protected Command getCreateCommand(CreateElementRequest req) {
    if (SystemdynamicsElementTypes.Variable_2001 == req.getElementType()) {
      return getGEFWrapper(new VariableCreateCommand(req));
    }
    if (SystemdynamicsElementTypes.Cloud_2002 == req.getElementType()) {
      return getGEFWrapper(new CloudCreateCommand(req));
    }
    if (SystemdynamicsElementTypes.Stock_2003 == req.getElementType()) {
      return getGEFWrapper(new StockCreateCommand(req));
    }
    return super.getCreateCommand(req);
  }

  /**
   * @generated
   */
  protected Command getDuplicateCommand(DuplicateElementsRequest req) {
    TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
    return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
  }

  /**
   * @generated
   */
  private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

    /**
     * @generated
     */
    public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain,
        DuplicateElementsRequest req) {
      super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req
          .getAllDuplicatedElementsMap());
    }

  }

}
