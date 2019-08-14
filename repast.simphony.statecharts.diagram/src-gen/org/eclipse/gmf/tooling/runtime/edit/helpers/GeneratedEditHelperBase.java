package org.eclipse.gmf.tooling.runtime.edit.helpers;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

public class GeneratedEditHelperBase extends AbstractEditHelper {

	public static final String EDIT_POLICY_COMMAND = "edit policy command"; //$NON-NLS-1$

	public static final String CONTEXT_ELEMENT_TYPE = "context element type"; //$NON-NLS-1$

	@Override
	protected IEditHelperAdvice[] getEditHelperAdvice(IEditCommandRequest req) {
		if (req.getParameter(CONTEXT_ELEMENT_TYPE) instanceof IElementType) {
			return ElementTypeRegistry.getInstance().getEditHelperAdvice((IElementType) req.getParameter(CONTEXT_ELEMENT_TYPE));
		}
		return super.getEditHelperAdvice(req);
	}

	@Override
	protected ICommand getInsteadCommand(IEditCommandRequest req) {
		ICommand epCommand = (ICommand) req.getParameter(EDIT_POLICY_COMMAND);
		req.setParameter(EDIT_POLICY_COMMAND, null);
		ICommand ehCommand = super.getInsteadCommand(req);
		if (epCommand == null) {
			return ehCommand;
		}
		if (ehCommand == null) {
			return epCommand;
		}
		CompositeCommand command = new CompositeCommand(null);
		command.add(epCommand);
		command.add(ehCommand);
		return command;
	}

	@Override
	protected ICommand getCreateCommand(CreateElementRequest req) {
		return null;
	}

	@Override
	protected ICommand getCreateRelationshipCommand(CreateRelationshipRequest req) {
		return null;
	}

	@Override
	protected ICommand getDestroyElementCommand(DestroyElementRequest req) {
		return null;
	}

	@Override
	protected ICommand getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return null;
	}
}
