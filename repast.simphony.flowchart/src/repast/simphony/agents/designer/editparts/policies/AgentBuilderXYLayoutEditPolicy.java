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

package repast.simphony.agents.designer.editparts.policies;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import repast.simphony.agents.base.FigureConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.editparts.AgentPropertyEditPart;
import repast.simphony.agents.designer.editparts.AgentPropertyorStepLabelEditPart;
import repast.simphony.agents.designer.editparts.BehaviorStepEditPart;
import repast.simphony.agents.designer.editparts.DecisionStepEditPart;
import repast.simphony.agents.designer.editparts.EndStepEditPart;
import repast.simphony.agents.designer.editparts.JoinStepEditPart;
import repast.simphony.agents.designer.editparts.TaskStepEditPart;
import repast.simphony.agents.designer.model.AgentBuilderModelPartFactory;
import repast.simphony.agents.designer.model.AgentDiagramElementModelPart;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.model.AgentPropertyModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepLabelModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart;
import repast.simphony.agents.designer.model.BehaviorStepModelPart;
import repast.simphony.agents.designer.model.DecisionStepModelPart;
import repast.simphony.agents.designer.model.EndStepModelPart;
import repast.simphony.agents.designer.model.JoinStepModelPart;
import repast.simphony.agents.designer.model.TaskStepModelPart;
import repast.simphony.agents.designer.model.commands.CreateCommand;
import repast.simphony.agents.designer.model.commands.SetConstraintCommand;
import repast.simphony.agents.designer.ui.editors.AgentEditor;

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
@SuppressWarnings("unchecked")
public class AgentBuilderXYLayoutEditPolicy extends XYLayoutEditPolicy {

	/**
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	@Override
	protected Command createAddCommand(EditPart childEditPart, Object constraint) {
		/*
		 * FlowDiagramElement part =
		 * (FlowDiagramElement)childEditPart.getModel(); Rectangle rect =
		 * (Rectangle)constraint;
		 * 
		 * AddCommand add = new AddCommand();
		 * add.setParent((FlowDiagramModelPart)getHost().getModel());
		 * add.setChild(part);
		 * add.setLabel(AgentBuilderPlugin.getResourceString(
		 * "EditPolicy_XYLayoutEdit_AddCommand.label")); //$NON-NLS-1$
		 * 
		 * SetConstraintCommand setConstraint = new SetConstraintCommand();
		 * 
		 * setConstraint.setLocation(rect); setConstraint.setPart(part);
		 * setConstraint.setLabel(AgentBuilderPlugin.getResourceString(
		 * "EditPolicy_XYLayoutEdit_AddCommand.label"));
		 * 
		 * return add.chain(setConstraint);
		 */
		return null;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		SetConstraintCommand locationCommand = new SetConstraintCommand();
		locationCommand
				.setPart((AgentDiagramElementModelPart) child.getModel());

		AgentEditor agentEditor = AgentBuilderPlugin.getActiveFlowEditor();
		if (agentEditor != null) {
			locationCommand
					.setEnableCommand(!(child instanceof AgentPropertyorStepLabelEditPart)
							|| (agentEditor.labelsCanMove()));
		}

		locationCommand.setLocation((Rectangle) constraint);

		return locationCommand;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof BehaviorStepEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof TaskStepEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof DecisionStepEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof JoinStepEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof AgentPropertyEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof EndStepEditPart)
			return new NonResizableEditPolicy();

		if (child instanceof AgentPropertyorStepLabelEditPart)
			return new NonResizableEditPolicy();

		return super.createChildEditPolicy(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand
	 * (org.eclipse.gef.Request)
	 */
	@Override
	protected Command getDeleteDependantCommand(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Factory method that returns a CreateCommand for flowlet creations.
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		AgentDiagramModelPart agentDiagram = (AgentDiagramModelPart) getHost()
				.getModel();
		if (BehaviorStepModelPart.class.equals(request.getNewObjectType())) {
			Rectangle rect = (Rectangle) getConstraintFor(request);
			Rectangle figRect = new Rectangle(rect.x, rect.y
					- FigureConsts.FIGURE_Y_OFFSET, rect.width,
					rect.height);
			return getCreateSteporPropertyWithLabelCommand(request,
					agentDiagram, (Rectangle) getConstraintFor(request),
					figRect, getNextFreeStartFlowletName(agentDiagram
							.getChildren()));

		} else if (TaskStepModelPart.class.equals(request.getNewObjectType())) {
			Rectangle rect = (Rectangle) getConstraintFor(request);
			Rectangle figRect = new Rectangle(rect.x, rect.y
					- FigureConsts.FIGURE_Y_OFFSET, rect.width,
					rect.height);
			return getCreateSteporPropertyWithLabelCommand(request,
					agentDiagram, (Rectangle) getConstraintFor(request),
					figRect, "Do Task");

		} else if (DecisionStepModelPart.class.equals(request
				.getNewObjectType())) {
			Rectangle rect = (Rectangle) getConstraintFor(request);
			Rectangle figRect = new Rectangle(rect.x, rect.y
					- FigureConsts.FIGURE_Y_OFFSET, rect.width,
					rect.height);
			return getCreateSteporPropertyWithLabelCommand(request,
					agentDiagram, (Rectangle) getConstraintFor(request),
					figRect, "Evaluate Criteria");

		} else if (JoinStepModelPart.class.equals(request.getNewObjectType())) {
			return getCreateSteporPropertyCommand(request, agentDiagram,
					(Rectangle) getConstraintFor(request),
					(AgentPropertyorStepModelPart) request.getNewObject());

		} else if (AgentPropertyModelPart.class.equals(request
				.getNewObjectType())) {
			Rectangle rect = (Rectangle) getConstraintFor(request);
			Rectangle figRect = new Rectangle(rect.x
					+ FigureConsts.PROPERTY_FIGURE_X_OFFSET, rect.y
					- FigureConsts.FIGURE_Y_OFFSET, rect.width,
					rect.height);
			return getCreateSteporPropertyWithLabelCommand(request,
					agentDiagram, (Rectangle) getConstraintFor(request),
					figRect, getNextFreePropertyFlowletName(agentDiagram
							.getChildren()));

		} else if (EndStepModelPart.class.equals(request.getNewObjectType())) {
			return getCreateSteporPropertyCommand(request, agentDiagram,
					(Rectangle) getConstraintFor(request),
					(AgentPropertyorStepModelPart) request.getNewObject());
		}

		return null;
	}

	/**
	 * Returns a CompoundCommand object that creates a flowlet and its label.
	 * 
	 * @param request
	 * @param flowDiagram
	 * @param flowletRect
	 * @param labelRect
	 * @param labelText
	 * @return a CompoundCommand that creates a flowlet and its label
	 */
	static public CompoundCommand getCreateSteporPropertyWithLabelCommand(
			CreateRequest request, AgentDiagramModelPart flowDiagram,
			Rectangle flowletRect, Rectangle labelRect, String labelText) {

		AgentPropertyorStepWithLabelModelPart flowletWithLabel = (AgentPropertyorStepWithLabelModelPart) request
				.getNewObject();
		// ask factory to create label
		AgentPropertyorStepLabelModelPart flowletLabel = AgentBuilderModelPartFactory
				.createFlowletLabelModelPart(flowletWithLabel);
		flowletLabel.setText(labelText);

		// create flowlet command
		CreateCommand createFlowletCommand = getCreateSteporPropertyCommand(
				request, flowDiagram, flowletRect, flowletWithLabel);
		flowletWithLabel.setFlowletLabel(flowletLabel);
		flowletLabel.setFlowletWithlabel(flowletWithLabel);
		createFlowletCommand
				.setLabel(AgentBuilderPlugin
						.getResourceString("EditPolicy_XYLayoutEdit_CreateCommand.label"));

		// create flowlet label command
		CreateCommand createFlowletLabelCommand = new CreateCommand();
		createFlowletLabelCommand.setParent(flowDiagram);
		createFlowletLabelCommand.setChild(flowletLabel);
		Rectangle constraint = labelRect;
		// (Rectangle)getConstraintFor(request);
		createFlowletLabelCommand.setLocation(constraint);
		createFlowletLabelCommand
				.setLabel(AgentBuilderPlugin
						.getResourceString("EditPolicy_XYLayoutEdit_CreateCommand.label"));

		// create compound command
		CompoundCommand command = new CompoundCommand();
		command.add(createFlowletCommand);
		command.add(createFlowletLabelCommand);

		if (AgentPropertyModelPart.class.equals(request.getNewObjectType())) {
			AgentPropertyModelPart part = (AgentPropertyModelPart) request
					.getNewObject();
			part.setPropertyCompiledName(labelText.trim().toLowerCase());
		}

		if (BehaviorStepModelPart.class.equals(request.getNewObjectType())) {
			BehaviorStepModelPart part = (BehaviorStepModelPart) request
					.getNewObject();
			part.setCompiledName(labelText.trim().toLowerCase());
		}

		return command;
	}

	/**
	 * TODO
	 * 
	 * @param request
	 * @param flowDiagram
	 * @param flowletRect
	 * @param flowlet
	 * @return
	 */
	static public CreateCommand getCreateSteporPropertyCommand(
			CreateRequest request, AgentDiagramModelPart flowDiagram,
			Rectangle flowletRect, AgentPropertyorStepModelPart flowlet) {

		// create flowlet command
		CreateCommand createFlowletCommand = new CreateCommand();
		createFlowletCommand.setParent(flowDiagram);
		createFlowletCommand.setChild(flowlet);
		Rectangle constraint = flowletRect;
		// (Rectangle)getConstraintFor(request);
		createFlowletCommand.setLocation(constraint);

		return createFlowletCommand;
	}

	/**
	 * Returns sort of unique name for a start flowlet. The name prefix is
	 * "Start" and a numer 1..1000 is appended.
	 * 
	 * @param flowlets
	 *            the flowlets in the diagram
	 * @return unused name for a start flowlet.
	 */
	public String getNextFreeStartFlowletName(List flowlets) {
		String prefix = "Step";
		if (!existsStartFlowlet(prefix, flowlets))
			return prefix;
		for (int i = 1; i < 1000; i++) {
			if (!existsStartFlowlet(prefix + i, flowlets)) {
				return prefix + i;
			}
		}

		return prefix;
	}

	/**
	 * Returns sort of unique name for a property flowlet. The name prefix is
	 * "Property" and a numer 1..1000 is appended.
	 * 
	 * @param flowlets
	 *            the flowlets in the diagram
	 * @return unused name for a start flowlet.
	 */
	public String getNextFreePropertyFlowletName(List flowlets) {
		String prefix = "Property";
		if (!existsPropertyFlowlet(prefix, flowlets))
			return prefix;
		for (int i = 1; i < 1000; i++) {
			if (!existsPropertyFlowlet(prefix + i, flowlets)) {
				return prefix + i;
			}
		}

		return prefix;
	}

	/**
	 * Returns <code>true</code> if the start flowlet with the given name
	 * already exists.
	 * 
	 * @param startName
	 *            the name to ceck for existence
	 * @param flowlets
	 *            the flowlets in the diagram
	 * @return <code>true</code> if the start flowlet with the given name
	 *         already exists
	 */
	private boolean existsStartFlowlet(String startName, List flowlets) {
		for (Iterator iter = flowlets.iterator(); iter.hasNext();) {
			AgentDiagramElementModelPart flowlet = (AgentDiagramElementModelPart) iter
					.next();
			if (flowlet instanceof BehaviorStepModelPart)
				if (((BehaviorStepModelPart) flowlet).getFlowletLabel()
						.getText().equals(startName))
					return true;
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if the start flowlet with the given name
	 * already exists.
	 * 
	 * @param startName
	 *            the name to ceck for existence
	 * @param flowlets
	 *            the flowlets in the diagram
	 * @return <code>true</code> if the start flowlet with the given name
	 *         already exists
	 */
	private boolean existsPropertyFlowlet(String startName, List flowlets) {
		for (Iterator iter = flowlets.iterator(); iter.hasNext();) {
			AgentDiagramElementModelPart flowlet = (AgentDiagramElementModelPart) iter
					.next();
			if (flowlet instanceof AgentPropertyModelPart)
				if (((AgentPropertyModelPart) flowlet).getFlowletLabel()
						.getText().equals(startName))
					return true;
		}
		return false;
	}

}
