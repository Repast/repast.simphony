/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.designer.ui.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;

import repast.simphony.agents.designer.ui.actions.SaveToImageAction;
import repast.simphony.agents.designer.ui.actions.ShowPropertiesAction;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
@SuppressWarnings( { "unchecked", "deprecation" })
public class AgentBuilderContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	/**
	 * TODO
	 * 
	 * @param viewer
	 * @param registry
	 */
	public AgentBuilderContextMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

	/**
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager manager) {
		GEFActionConstants.addStandardActionGroups(manager);

		IAction action;

		action = actionRegistry.getAction(IWorkbenchActionConstants.UNDO);
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = actionRegistry.getAction(IWorkbenchActionConstants.REDO);
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = actionRegistry
				.getAction(ShowPropertiesAction.ACTION_SHOW_PROPERTIES);
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_REST, action);

		action = actionRegistry.getAction(ActionFactory.PASTE.getId());
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry.getAction(ActionFactory.COPY.getId());
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry.getAction(ActionFactory.DELETE.getId());
		if (action.isEnabled())
			action = actionRegistry.getAction(IWorkbenchActionConstants.DELETE);

		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry.getAction(IWorkbenchActionConstants.SAVE);
		manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
		
		action = actionRegistry.getAction(SaveToImageAction.SAVE_TO_IMAGE);
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);

	}

}
