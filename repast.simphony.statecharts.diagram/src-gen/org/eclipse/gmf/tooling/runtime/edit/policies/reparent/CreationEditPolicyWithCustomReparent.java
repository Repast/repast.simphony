package org.eclipse.gmf.tooling.runtime.edit.policies.reparent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.structure.DiagramStructure;

/**
 * @see #237059, backported to GMF from UML2Tools 
 */
public class CreationEditPolicyWithCustomReparent extends CreationEditPolicy {

	private final DiagramStructure myDiagramStructure;

	public CreationEditPolicyWithCustomReparent(DiagramStructure diagramStructure) {
		myDiagramStructure = diagramStructure;
	}

	@Override
	protected Command getReparentCommand(ChangeBoundsRequest request) {
		return super.getReparentCommand(request);
	}

	/**
	 * The only difference in this method is that we use
	 * MoveRequestWithParentInfo instead of MoveRequest and configuring it with
	 * info about actual semantic element of moved element's container
	 */
	@Override
	protected ICommand getReparentCommand(IGraphicalEditPart gep) {
		CompositeCommand cc = new CompositeCommand(DiagramUIMessages.AddCommand_Label);
		View container = (View) getHost().getModel();
		EObject context = ViewUtil.resolveSemanticElement(container);
		View view = (View) gep.getModel();
		EObject element = ViewUtil.resolveSemanticElement(view);

		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();

		//
		// semantic
		if (element != null) {
			EObject actualContainer = getSemanticContainer(gep);
			MoveRequestWithParentInfo request = new MoveRequestWithParentInfo(editingDomain, context, element);
			request.registerActualContainer(element, actualContainer);
			Command moveSemanticCmd = getHost().getCommand(new EditCommandRequestWrapper(request));

			if (moveSemanticCmd == null) {
				return org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand.INSTANCE;
			}

			cc.compose(new CommandProxy(moveSemanticCmd));
		}
		//
		// notation
		cc.compose(getReparentViewCommand(gep));
		return cc;
	}

	/**
	 * The only difference in this method is that we use
	 * MoveRequestWithParentInfo instead of MoveRequest and configuring it with
	 * info about actual semantic element of moved element's container
	 */
	@Override
	protected ICommand getReparentGroupCommand(GroupEditPart groupEP) {
		CompositeCommand cc = new CompositeCommand(DiagramUIMessages.AddCommand_Label);
		View container = (View) getHost().getModel();
		EObject context = ViewUtil.resolveSemanticElement(container);
		EObject groupSemanticParent = getSemanticContainer(groupEP);

		// semantic
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		for (Iterator<?> iter = groupEP.getShapeChildren().iterator(); iter.hasNext();) {
			IGraphicalEditPart childEP = (IGraphicalEditPart) iter.next();
			EObject element = ViewUtil.resolveSemanticElement((View) childEP.getModel());
			if (element != null) {
				MoveRequestWithParentInfo request = new MoveRequestWithParentInfo(editingDomain, context, element);
				request.registerActualContainer(element, groupSemanticParent);
				Command moveSemanticCmd = getHost().getCommand(new EditCommandRequestWrapper(request));

				if (moveSemanticCmd == null) {
					return org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand.INSTANCE;
				}

				cc.compose(new CommandProxy(moveSemanticCmd));
			}
		}

		// notation
		cc.compose(getReparentViewCommand(groupEP));
		return cc;
	}

	protected final EObject getSemanticContainer(IGraphicalEditPart gep) {
		IGraphicalEditPart parentEP = (IGraphicalEditPart) gep.getParent();
		if (parentEP == null) {
			return null;
		}
		View view = parentEP.getPrimaryView();
		if (view == null) {
			return null;
		}
		return ViewUtil.resolveSemanticElement(view);
	}

	@Override
	protected ICommand getReparentViewCommand(IGraphicalEditPart gep) {
		View container = (View) getHost().getModel();
		View view = (View) gep.getModel();
		MoveViewCommand result = new MoveViewCommand(gep.getEditingDomain(), new EObjectAdapter(container), new EObjectAdapter(view), getHostImpl().getDiagramPreferencesHint());
		result.setVisualIDRegistry(myDiagramStructure);
		return result;
	}

	protected final IGraphicalEditPart getHostImpl() {
		return (IGraphicalEditPart) getHost();
	}

	public static class MoveRequestWithParentInfo extends MoveRequest {

		private final Map<EObject, EObject> myElement2ActualParent = new HashMap<EObject, EObject>();

		public MoveRequestWithParentInfo(TransactionalEditingDomain editingDomain, EObject targetContainer, EObject elementToMove) {
			super(editingDomain, targetContainer, elementToMove);
		}

		public void registerActualContainer(EObject elementToMove, EObject actualContainer) {
			if (actualContainer != null) {
				myElement2ActualParent.put(elementToMove, actualContainer);
			} else {
				myElement2ActualParent.remove(elementToMove);
			}
		}

		public EObject getActualContainer(EObject movedElement) {
			return myElement2ActualParent.get(movedElement);
		}

	}

}
