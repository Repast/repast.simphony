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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import repast.simphony.agents.base.FigureConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.editparts.AgentPropertyorStepEditPart;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepModelPart;
import repast.simphony.agents.designer.model.BooleanTransitionModelPart;
import repast.simphony.agents.designer.model.DesignerModelDigester;
import repast.simphony.agents.designer.model.TransitionModelPart;
import repast.simphony.agents.designer.model.commands.ConnectionCommand;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.model.bind.IPropertyOrStepBind;
import repast.simphony.agents.model.bind.PropertyOrStepBind;
import repast.simphony.agents.model.bind.PropertyOrStepLabelBind;
import repast.simphony.agents.model.bind.PropertyOrStepWithLabelBind;
import repast.simphony.agents.model.bind.TransitionBind;

/**
 * If the current object on the clipboard is a valid template, this action will
 * paste the template to the viewer.
 * 
 * @author Eric Bordeau, Pratik Shah Modified by Michael North
 * @see org.eclipse.gef.ui.actions.CopyTemplateAction
 */
public class PasteAction extends SelectionAction {

	public static final int OFFSET = 15;

	/**
	 * Constructor for PasteTemplateAction.
	 * 
	 * @param editor
	 */
	public PasteAction(IWorkbenchPart editor) {
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
	protected Command createPasteCommand() {

		CompoundCommand result = new CompoundCommand();

		AgentEditor agentEditor = AgentBuilderPlugin.getActiveFlowEditor();
		if (agentEditor != null) {

			AgentDiagramModelPart flowDiagram = agentEditor.getFlowDiagram();

			List selection = agentEditor.getCopiedObjects();
			if (selection != null) {

				Map<AgentPropertyorStepEditPart, IPropertyOrStepBind> oldFlowletBindMap = new HashMap<AgentPropertyorStepEditPart, IPropertyOrStepBind>();
				Map<AgentPropertyorStepModelPart, AgentPropertyorStepModelPart> newFlowletBindMap = new HashMap<AgentPropertyorStepModelPart, AgentPropertyorStepModelPart>();
				Map<AgentPropertyorStepModelPart, AgentPropertyorStepEditPart> flowletEditMap = new HashMap<AgentPropertyorStepModelPart, AgentPropertyorStepEditPart>();

				for (Object obj : selection) {

					if (obj instanceof AgentPropertyorStepEditPart) {

						AgentPropertyorStepEditPart gep = (AgentPropertyorStepEditPart) obj;

						DesignerModelDigester digester = new DesignerModelDigester(
								flowDiagram);
						IPropertyOrStepBind iBind = digester
								.createFlowletBind(gep.getModel());
						oldFlowletBindMap.put(gep, iBind);
						flowletEditMap.put((AgentPropertyorStepModelPart) gep
								.getModel(), gep);
						if (iBind instanceof PropertyOrStepWithLabelBind) {
							PropertyOrStepWithLabelBind bind = (PropertyOrStepWithLabelBind) iBind;
							PropertyOrStepLabelBind bindLabel = bind.getLabel();
							bindLabel.setX(bindLabel.getX() + PasteAction.OFFSET);
							bindLabel.setY(bindLabel.getY() + PasteAction.OFFSET);
							bind.setX(bind.getX() + PasteAction.OFFSET);
							bind.setY(bind.getY() + PasteAction.OFFSET);
						} else if (iBind instanceof PropertyOrStepBind) {
							PropertyOrStepBind bind = (PropertyOrStepBind) iBind;
							bind.setX(bind.getX() + PasteAction.OFFSET);
							bind.setY(bind.getY() + PasteAction.OFFSET);
						}
						result.add(digester.digestFlowlet(iBind));

						Object value = digester
								.getFlowletToFlowletBindMapping().keySet()
								.iterator().next();
						newFlowletBindMap.put(
								(AgentPropertyorStepModelPart) gep.getModel(),
								(AgentPropertyorStepModelPart) value);

					}

				}

				for (Object obj : selection) {

					if (obj instanceof AgentPropertyorStepEditPart) {

						AgentPropertyorStepEditPart gep = (AgentPropertyorStepEditPart) obj;

						List connections = gep.getSourceConnections();
						for (Object connectionObject : connections) {

							if (connectionObject instanceof ConnectionEditPart) {

								ConnectionEditPart connection = (ConnectionEditPart) connectionObject;
								EditPart ep = connection.getTarget();
								if (ep instanceof AgentPropertyorStepEditPart) {

									AgentPropertyorStepEditPart aep = (AgentPropertyorStepEditPart) ep;
									if (oldFlowletBindMap.get(aep) != null) {

										Object coModel = connection.getModel();
										if (coModel instanceof TransitionModelPart) {

											TransitionModelPart tep = (TransitionModelPart) coModel;

											DesignerModelDigester digester = new DesignerModelDigester(
													flowDiagram);
											AgentPropertyorStepModelPart source = newFlowletBindMap
													.get(tep.getSource());
											int sourceIndex = tep
													.getSourceAnchorIndex();
											AgentPropertyorStepModelPart target = newFlowletBindMap
													.get(tep.getTarget());
											int targetIndex = tep
													.getTargetAnchorIndex();
											IPropertyOrStepBind sourceBind = oldFlowletBindMap
													.get(flowletEditMap.get(tep
															.getSource()));
											IPropertyOrStepBind targetBind = oldFlowletBindMap
													.get(flowletEditMap.get(tep
															.getTarget()));
											TransitionBind tBind = digester
													.createTransitionBind(
															source,
															sourceIndex,
															target,
															targetIndex, tep);
											if (tep instanceof BooleanTransitionModelPart) {
												BooleanTransitionModelPart btep = new BooleanTransitionModelPart();
												btep
														.setValue(((BooleanTransitionModelPart) tep)
																.getBoolValue());
												ConnectionCommand cc = digester
														.digestTransition(
																tBind,
																sourceBind,
																targetBind,
																null, btep);
												cc.setSource(source);
												cc
														.setSourceAnchorIndex(sourceIndex);
												cc.setTarget(target);
												cc
														.setTargetAnchorIndex(targetIndex);
												result.add(cc);
											} else {
												ConnectionCommand cc = digester
														.digestTransition(
																tBind,
																sourceBind,
																targetBind,
																null,
																new TransitionModelPart());
												cc.setSource(source);
												cc
														.setSourceAnchorIndex(sourceIndex);
												cc.setTarget(target);
												cc
														.setTargetAnchorIndex(targetIndex);
												result.add(cc);
											}

										}

									}

								}

							}

						}

					}

				}

			}

		}

		return result;

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
	 * @see PasteAction#getPasteLocation(GraphicalEditPart)
	 */
	protected Point getPasteLocation(GraphicalEditPart container) {
		Point result = new Point(10, 10);
		IFigure fig = container.getContentPane();
		result.translate(fig.getClientArea(Rectangle.SINGLETON).getLocation());
		fig.translateToAbsolute(result);
		return result;
	}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
	 */
	protected void init() {
		setId(ActionFactory.PASTE.getId());
		setText("&Paste");
	}

	/**
	 * Executes the command returned by {@link #createPasteCommand()}.
	 */
	public void run() {
		execute(createPasteCommand());
	}

}
