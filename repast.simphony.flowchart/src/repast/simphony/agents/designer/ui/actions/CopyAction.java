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

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.editparts.AgentPropertyorStepWithLabelEditPart;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart;
import repast.simphony.agents.designer.ui.editors.AgentEditor;

/**
 * If the current object on the clipboard is a valid template, this action will
 * paste the template to the viewer.
 * 
 * @author Eric Bordeau, Pratik Shah Modified by Michael North
 * @see org.eclipse.gef.ui.actions.CopyTemplateAction
 */
public class CopyAction extends SelectionAction {

	static List selection = null;

	/**
	 * Constructor for PasteTemplateAction.
	 * 
	 * @param editor
	 */
	public CopyAction(IWorkbenchPart editor) {
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
	 * Returns the template on the clipboard, if there is one. Note that the
	 * template on the clipboard might be from a palette from another type of
	 * editor.
	 * 
	 * @return the clipboard's contents
	 */
	protected Object getClipboardContents() {
		return Clipboard.getDefault().getContents();
	}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
	 */
	protected void init() {
		setId(ActionFactory.COPY.getId());
		setText("&Copy");
	}

	/**
	 * Executes the command returned by {@link #createPasteCommand()}.
	 */
	public void run() {

		AgentEditor agentEditor = AgentBuilderPlugin.getActiveFlowEditor();
		if (agentEditor != null) {
			AgentDiagramModelPart flowDiagram = agentEditor.getFlowDiagram();
			selection = agentEditor.getSelectedObjects();

			String text = "";
			String lineSeparator = System.getProperty("line.separator");
			for (Object obj : selection) {
				if (obj instanceof AgentPropertyorStepWithLabelEditPart) {
					AgentPropertyorStepWithLabelEditPart gep = (AgentPropertyorStepWithLabelEditPart) obj;
					Object model = gep.getModel();
					if (model instanceof AgentPropertyorStepWithLabelModelPart) {
						AgentPropertyorStepWithLabelModelPart gem = (AgentPropertyorStepWithLabelModelPart) model;
						text = text + gem.getLabel() + lineSeparator;
					}
				}
			}

			Clipboard.getDefault().setContents(text.toCharArray());
			StringSelection stringSelection = new StringSelection(text);
			java.awt.datatransfer.Clipboard clipboard = Toolkit
					.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, new ClipboardOwner() {
				public void lostOwnership(java.awt.datatransfer.Clipboard arg0,
						Transferable arg1) {
				}
			});

		}

	}

	public List getSelectedObjects() {
		return selection;
	}

}
