package repast.simphony.statecharts.edit.policies;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardSupportUtil;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

import repast.simphony.statecharts.edit.commands.CompositeStateCreateCommand;
import repast.simphony.statecharts.edit.commands.FinalStateCreateCommand;
import repast.simphony.statecharts.edit.commands.PseudoState2CreateCommand;
import repast.simphony.statecharts.edit.commands.PseudoState5CreateCommand;
import repast.simphony.statecharts.edit.commands.PseudoStateCreateCommand;
import repast.simphony.statecharts.edit.commands.StateCreateCommand;
import repast.simphony.statecharts.providers.StatechartElementTypes;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * @generated
 */
public class StateMachineItemSemanticEditPolicy extends StatechartBaseItemSemanticEditPolicy {

  /**
   * @generated
   */
  public StateMachineItemSemanticEditPolicy() {
    super(StatechartElementTypes.StateMachine_1000);
  }

  /**
   * @generated
   */
  protected Command getCreateCommand(CreateElementRequest req) {
    if (StatechartElementTypes.State_2003 == req.getElementType()) {
      return getGEFWrapper(new StateCreateCommand(req));
    }
    if (StatechartElementTypes.CompositeState_2004 == req.getElementType()) {
      return getGEFWrapper(new CompositeStateCreateCommand(req));
    }
    if (StatechartElementTypes.PseudoState_2005 == req.getElementType()) {
      return getGEFWrapper(new PseudoStateCreateCommand(req));
    }
    if (StatechartElementTypes.PseudoState_2006 == req.getElementType()) {
      return getGEFWrapper(new PseudoState2CreateCommand(req));
    }
    if (StatechartElementTypes.PseudoState_2007 == req.getElementType()) {
      return getGEFWrapper(new PseudoState5CreateCommand(req));
    }
    if (StatechartElementTypes.FinalState_2008 == req.getElementType()) {
      return getGEFWrapper(new FinalStateCreateCommand(req));
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

    /**
     * @generated NOT
     */
    protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

      // Remove elements whose container is getting copied.
      ClipboardSupportUtil.getCopyElements(getObjectsToBeDuplicated());

      // Perform the copy and update the references.
      EcoreUtil.Copier copier = new EcoreUtil.Copier();
      copier.copyAll(getObjectsToBeDuplicated());
      copier.copyReferences();

      // Update the map with all elements duplicated.
      getAllDuplicatedObjectsMap().putAll(copier);

      // Add the duplicates to the original's container.
      for (Iterator i = getObjectsToBeDuplicated().iterator(); i.hasNext();) {
        EObject original = (EObject) i.next();
        EObject duplicate = (EObject) copier.get(original);
        
        if (duplicate instanceof AbstractState) {
          ((AbstractState)duplicate).setUuid(EcoreUtil.generateUUID());
        } else if (duplicate instanceof Transition) {
          ((Transition)duplicate).setUuid(EcoreUtil.generateUUID());
        }

        EReference reference = original.eContainmentFeature();
        if (reference != null
            && FeatureMapUtil.isMany(original.eContainer(), reference)
            && ClipboardSupportUtil.isOkToAppendEObjectAt(original.eContainer(), reference,
                duplicate)) {

          ClipboardSupportUtil.appendEObjectAt(original.eContainer(), reference, duplicate);
        }
      }
      return CommandResult.newOKCommandResult(getAllDuplicatedObjectsMap());
    }

  }

}
