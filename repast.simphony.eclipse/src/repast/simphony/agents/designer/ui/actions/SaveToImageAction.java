/*******************************************************************************
 * Modified from org.eclipse.gef.examples.logicdesigner.actions.PasteTemplateAction
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package repast.simphony.agents.designer.ui.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.ui.editors.AgentEditor;

/**
 * If the current object on the clipboard is a valid template, this action will
 * paste the template to the viewer.
 * 
 * @author Eric Bordeau, Pratik Shah Modified by Michael North
 * @see org.eclipse.gef.ui.actions.CopyTemplateAction
 */
public class SaveToImageAction extends SelectionAction {
	
	public static final String SAVE_TO_IMAGE = "SaveToImage";

	/**
	 * Constructor for PasteTemplateAction.
	 * 
	 * @param editor
	 */
	public SaveToImageAction(IWorkbenchPart editor) {
		super(editor);
	}

	/**
	 * @return <code>true</code> if {@link #createPasteCommand()} returns an
	 *         executable command
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		// TODO Workaround for Bug 82622/39369. Should be removed when 39369 is
		// fixed.
		return true;
	}

	/**
	 * Creates and returns a command (which may be <code>null</code>) to create
	 * a new EditPart based on the template on the clipboard.
	 * 
	 * @return the paste command
	 */
	protected Command doExportCommand() {
		Util.exportAsImage(AgentBuilderPlugin.getActiveFlowEditor());
		return new CompoundCommand();
	}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
	 */
	protected void init() {
		setId(SAVE_TO_IMAGE);
		setText("Save to &Image File");
	}

	/**
	 * Executes the command returned by {@link #createPasteCommand()}.
	 */
	public void run() {
		execute(doExportCommand());
	}

}
