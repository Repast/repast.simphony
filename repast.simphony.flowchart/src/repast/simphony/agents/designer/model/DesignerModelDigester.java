/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.SimpleFactory;

import repast.simphony.agents.designer.editparts.policies.AgentBuilderXYLayoutEditPolicy;
import repast.simphony.agents.designer.figures.AnchorRegistry;
import repast.simphony.agents.designer.model.commands.ConnectionCommand;
import repast.simphony.agents.designer.model.commands.CreateBendpointCommand;
import repast.simphony.agents.designer.model.commands.CreateCommand;
import repast.simphony.agents.designer.ui.actions.PasteAction;
import repast.simphony.agents.model.AbstractModelDigester;
import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.AgentPropertyBind;
import repast.simphony.agents.model.bind.BehaviorStepBind;
import repast.simphony.agents.model.bind.BendpointBind;
import repast.simphony.agents.model.bind.BooleanTransitionBind;
import repast.simphony.agents.model.bind.DecisionStepBind;
import repast.simphony.agents.model.bind.EndStepBind;
import repast.simphony.agents.model.bind.ILocationHolder;
import repast.simphony.agents.model.bind.IPropertyOrStepBind;
import repast.simphony.agents.model.bind.JoinStepBind;
import repast.simphony.agents.model.bind.PropertyOrStepLabelBind;
import repast.simphony.agents.model.bind.TaskStepBind;
import repast.simphony.agents.model.bind.TransitionBind;
import repast.simphony.agents.model.bind.TransitionSourceBind;
import repast.simphony.agents.model.bind.TransitionTargetBind;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class DesignerModelDigester extends AbstractModelDigester {

	public AgentDiagramModelPart flowDiagramModelPart;

	public CompoundCommand createCommand = new CompoundCommand();
	public Map transitionBindToTransitionMapping = new HashMap();
	public Map flowletToFlowletBindMapping = new HashMap();

	public Map getFlowletToFlowletBindMapping() {
		return flowletToFlowletBindMapping;
	}

	public void setFlowletToFlowletBindMapping(Map flowletToFlowletBindMapping) {
		this.flowletToFlowletBindMapping = flowletToFlowletBindMapping;
	}

	public Map flowletIdToFlowletMapping = new HashMap();

	/**
	 * Creates a new digester with the given FlowDiagramModelPart. It can be an
	 * empty FlowDiagramModelPart or a filled one that was just unmarshalled.
	 * 
	 * @param flowDiagramModelPart
	 *            the flowDiagramModelPart
	 */
	public DesignerModelDigester(AgentDiagramModelPart flowDiagramModelPart) {
		super();
		this.flowDiagramModelPart = flowDiagramModelPart;
	}

	/**
	 * Returns the CompoundComand that is responsible for the creation of all
	 * EditParts and Figures and so on.
	 * 
	 * @return the CompoundCommand Which creates all necessary parts for the
	 *         flowDiagram
	 */
	public CompoundCommand getCreateCommand() {
		return createCommand;
	}

	/**
	 * Digests the flowname attribute.
	 * 
	 * @see repast.simphony.agents.model.IModelDigester#digest(repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digest(AgentModelBind agentModelBind) {
		flowDiagramModelPart.setAgentName(agentModelBind.getFlowName());
		flowDiagramModelPart.setAgentComment(agentModelBind.getComment());
		flowDiagramModelPart.setAgentImports(agentModelBind.getImports());
		flowDiagramModelPart.setAgentInterfaces(agentModelBind.getInterfaces());
		flowDiagramModelPart.setAgentParentClassName(agentModelBind
				.getParentClassName());
		super.digest(agentModelBind);
	}

	public Command digestFlowlet(Object flowletBind) {

		AgentModelBind agentModelBind = null;

		if (flowletBind instanceof BehaviorStepBind) {
			digestBehaviorStepFlowlet((BehaviorStepBind) flowletBind,
					agentModelBind);
		}
		// task flowlets
		else if (flowletBind instanceof TaskStepBind) {
			digestTaskFlowlet((TaskStepBind) flowletBind, agentModelBind);
		}
		// decision flowlets
		else if (flowletBind instanceof DecisionStepBind) {
			digestDecisionFlowlet((DecisionStepBind) flowletBind,
					agentModelBind);
		}
		// join flowlets
		else if (flowletBind instanceof JoinStepBind) {
			digestJoinFlowlet((JoinStepBind) flowletBind, agentModelBind);
		}
		// property flowlets
		else if (flowletBind instanceof AgentPropertyBind) {
			digestPropertyFlowlet((AgentPropertyBind) flowletBind,
					agentModelBind);
		}
		// end flowlets
		else if (flowletBind instanceof EndStepBind) {
			digestEndFlowlet((EndStepBind) flowletBind, agentModelBind);
		}

		return createCommand;

	}

	/**
	 * @param flowlet
	 * @param flowModel
	 */
	@Override
	public void digestBehaviorStepFlowlet(BehaviorStepBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(BehaviorStepModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		Rectangle labelRect = new Rectangle(flowletBind.getLabel().getX(),
				flowletBind.getLabel().getY(), 0, 0);

		CompoundCommand command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyWithLabelCommand(request,
						flowDiagramModelPart, flowletRect, labelRect,
						flowletBind.getLabel().getText());
		BehaviorStepModelPart startFlowletModelpart = (BehaviorStepModelPart) ((CreateCommand) command
				.getChildren()[0]).getChild();
		startFlowletModelpart.setCompiledName(startFlowletModelpart.getLabel()
				.toLowerCase());

		startFlowletModelpart.setComment(flowletBind.getComment());

		String tempString = flowletBind.getVisibility();
		if (tempString == null) {
			startFlowletModelpart.setVisibility(0);
		} else {
			startFlowletModelpart.setVisibility(Integer.parseInt(tempString));
		}

		startFlowletModelpart.setReturnType(flowletBind.getReturnType());
		startFlowletModelpart.setCompiledName(flowletBind.getCompiledName());
		startFlowletModelpart.setParameters(flowletBind.getParameters());
		startFlowletModelpart.setExceptions(flowletBind.getExceptions());

		startFlowletModelpart.getBehaviorSchedulePropertySource()
				.setScheduleAnnotationStart(
						flowletBind.getScheduleAnnotationStart());
		startFlowletModelpart.getBehaviorSchedulePropertySource()
				.setScheduleAnnotationPick(
						flowletBind.getScheduleAnnotationPick());
		startFlowletModelpart.getBehaviorSchedulePropertySource()
				.setScheduleAnnotationInterval(
						flowletBind.getScheduleAnnotationInterval());
		startFlowletModelpart.getBehaviorSchedulePropertySource()
				.setScheduleAnnotationPriority(
						flowletBind.getScheduleAnnotationPriority());
		startFlowletModelpart.getBehaviorSchedulePropertySource()
				.setScheduleAnnotationDuration(
						flowletBind.getScheduleAnnotationDuration());
		tempString = flowletBind.getScheduleAnnotationShuffle();
		if (tempString == null) {
			startFlowletModelpart.getBehaviorSchedulePropertySource()
					.setScheduleAnnotationShuffle(0);
		} else {
			startFlowletModelpart.getBehaviorSchedulePropertySource()
					.setScheduleAnnotationShuffle(Integer.parseInt(tempString));
		}

		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationId(flowletBind.getWatchAnnotationId());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationQuery(flowletBind.getWatchAnnotationQuery());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationPick(flowletBind.getWatchAnnotationPick());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationTargetClassName(
						flowletBind.getWatchAnnotationTargetClassName());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationTargetFieldNames(
						flowletBind.getWatchAnnotationTargetFieldNames());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationTriggerCondition(
						flowletBind.getWatchAnnotationTriggerCondition());
		tempString = flowletBind.getWatchAnnotationTriggerSchedule();
		if (tempString == null) {
			startFlowletModelpart.getBehaviorWatchPropertySource()
					.setWatchAnnotationTriggerSchedule(0);
		} else {
			startFlowletModelpart.getBehaviorWatchPropertySource()
					.setWatchAnnotationTriggerSchedule(
							Integer.parseInt(tempString));
		}
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationTriggerDelta(
						flowletBind.getWatchAnnotationTriggerDelta());
		startFlowletModelpart.getBehaviorWatchPropertySource()
				.setWatchAnnotationTriggerPriority(
						flowletBind.getWatchAnnotationTriggerPriority());

		flowletToFlowletBindMapping.put(startFlowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					startFlowletModelpart);
		}

		createCommand.add(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestDecisionFlowlet
	 * (repast.simphony.agents.model.bind.DecisionStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestDecisionFlowlet(DecisionStepBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(DecisionStepModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		Rectangle labelRect = new Rectangle(flowletBind.getLabel().getX(),
				flowletBind.getLabel().getY(), 0, 0);

		CompoundCommand command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyWithLabelCommand(request,
						flowDiagramModelPart, flowletRect, labelRect,
						flowletBind.getLabel().getText());
		DecisionStepModelPart decisionFlowletModelpart = (DecisionStepModelPart) ((CreateCommand) command
				.getChildren()[0]).getChild();
		decisionFlowletModelpart.setComment(flowletBind.getDescription());
		decisionFlowletModelpart.setCriteria(flowletBind.getBooleanStatement());
		decisionFlowletModelpart.setBranchType(flowletBind.getBranchType());

		flowletToFlowletBindMapping.put(decisionFlowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					decisionFlowletModelpart);
		}

		createCommand.add(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestEndFlowlet(repast
	 * .simphony.agents.model.bind.EndStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestEndFlowlet(EndStepBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(EndStepModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		AgentPropertyorStepModelPart flowletModelpart = (AgentPropertyorStepModelPart) request
				.getNewObject();

		Command command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyCommand(request, flowDiagramModelPart,
						flowletRect, flowletModelpart);
		flowletToFlowletBindMapping.put(flowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					flowletModelpart);
		}

		createCommand.add(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestTaskFlowlet(
	 * repast.simphony.agents.model.bind.TaskStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestTaskFlowlet(TaskStepBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(TaskStepModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		Rectangle labelRect = new Rectangle(flowletBind.getLabel().getX(),
				flowletBind.getLabel().getY(), 0, 0);

		CompoundCommand command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyWithLabelCommand(request,
						flowDiagramModelPart, flowletRect, labelRect,
						flowletBind.getLabel().getText());
		TaskStepModelPart taskFlowletModelpart = (TaskStepModelPart) ((CreateCommand) command
				.getChildren()[0]).getChild();
		taskFlowletModelpart.setComment(flowletBind.getComment());
		taskFlowletModelpart.setTaskCommand1(flowletBind.getCommand1());
		taskFlowletModelpart.setTaskCommand2(flowletBind.getCommand2());
		taskFlowletModelpart.setTaskCommand3(flowletBind.getCommand3());
		taskFlowletModelpart.setTaskCommand4(flowletBind.getCommand4());
		taskFlowletModelpart.setTaskCommand5(flowletBind.getCommand5());

		flowletToFlowletBindMapping.put(taskFlowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					taskFlowletModelpart);
		}

		createCommand.add(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestJoinFlowlet(
	 * repast.simphony.agents.model.bind.JoinStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestJoinFlowlet(JoinStepBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(JoinStepModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		AgentPropertyorStepModelPart flowletModelpart = (AgentPropertyorStepModelPart) request
				.getNewObject();

		Command command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyCommand(request, flowDiagramModelPart,
						flowletRect, flowletModelpart);
		flowletToFlowletBindMapping.put(flowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					flowletModelpart);
		}

		createCommand.add(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestPropertyFlowlet
	 * (repast.simphony.agents.model.bind.AgentPropertyBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestPropertyFlowlet(AgentPropertyBind flowletBind,
			AgentModelBind agentModelBind) {
		CreateRequest request = new CreateRequest();
		request.setFactory(new SimpleFactory(AgentPropertyModelPart.class));

		Rectangle flowletRect = new Rectangle(flowletBind.getX(), flowletBind
				.getY(), 0, 0);
		Rectangle labelRect = new Rectangle(flowletBind.getLabel().getX(),
				flowletBind.getLabel().getY(), 0, 0);

		CompoundCommand command = AgentBuilderXYLayoutEditPolicy
				.getCreateSteporPropertyWithLabelCommand(request,
						flowDiagramModelPart, flowletRect, labelRect,
						flowletBind.getLabel().getText());
		AgentPropertyModelPart propertyFlowletModelpart = (AgentPropertyModelPart) ((CreateCommand) command
				.getChildren()[0]).getChild();
		propertyFlowletModelpart.setPropertyDescription(flowletBind
				.getDescription());
		propertyFlowletModelpart.setVisibility(flowletBind.getVisibility());
		propertyFlowletModelpart.setPropertyCompiledName(flowletBind
				.getPropertyCompiledName());
		propertyFlowletModelpart.setPropertyType(flowletBind.getPropertyType());
		propertyFlowletModelpart.setPropertyDefaultValue(flowletBind
				.getPropertyDefaultValue());
		flowletToFlowletBindMapping.put(propertyFlowletModelpart, flowletBind);
		if (agentModelBind != null) {
			flowletIdToFlowletMapping.put(flowletBind.getId(agentModelBind),
					propertyFlowletModelpart);
		}

		createCommand.add(command);
	}

	/**
	 * Performs the basic ConnectionCommand creation
	 * 
	 * @param transitionBind
	 * @param transitionModelPart
	 */
	public ConnectionCommand digestTransition(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind,
			AgentModelBind agentModelBind,
			TransitionModelPart transitionModelPart) {
		ConnectionCommand command = new ConnectionCommand();
		command.setTransition(transitionModelPart);

		if (agentModelBind != null) {
			command
					.setSource((AgentPropertyorStepModelPart) flowletIdToFlowletMapping
							.get(sourceFlowletBind.getId(agentModelBind)));
		}
		command.setSourceAnchorIndex(transitionBind.getSourceFlowlet()
				.getAnchor());

		if (agentModelBind != null) {
			command
					.setTarget((AgentPropertyorStepModelPart) flowletIdToFlowletMapping
							.get(targetFlowletBind.getId(agentModelBind)));
		}
		command.setTargetAnchorIndex(transitionBind.getTargetFlowlet()
				.getAnchor());

		// IPropertyOrStepBind source = transitionBind.getSourceFlowlet();
		// TransitionTargetFlowletGen target =
		// transitionBind.getTransitionTargetFlowletGen();
		//
		// String flowletRef = source.getType() + "_" + source.getIndex();
		// AgentPropertyorStepModelPart flowletModelPart =
		//(AgentPropertyorStepModelPart)flowletIdToFlowletMapping.get(flowletRef
		// );
		// command.setSource((AgentPropertyorStepModelPart)
		// flowletIdToFlowletMapping.get(flowletRef));
		// command.setSourceAnchorIndex(transitionBind.
		// getTransitionSourceFlowletGen().getAnchor());
		// transitionBind.setSourceFlowletBind((IPropertyOrStepBind)
		// flowletToFlowletBindMapping.get(flowletModelPart));
		//
		// flowletRef = target.getType() + "_" + target.getIndex();
		// flowletModelPart =
		//(AgentPropertyorStepModelPart)flowletIdToFlowletMapping.get(flowletRef
		// );
		// command.setTarget(flowletModelPart);
		// command.setTargetAnchorIndex(transitionBind.
		// getTransitionTargetFlowletGen().getAnchor());
		// transitionBind.setTargetFlowletBind((IPropertyOrStepBind)
		// flowletToFlowletBindMapping.get(flowletModelPart));

		transitionBindToTransitionMapping.put(transitionBind,
				transitionModelPart);

		createCommand.add(command);

		return command;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestTransition(repast
	 * .simphony.agents.model.bind.TransitionBind,
	 * repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 * repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestTransition(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {

		digestTransition(transitionBind, sourceFlowletBind, targetFlowletBind,
				agentModelBind, new TransitionModelPart());
		// digest the bendpoints
		super.digestTransition(transitionBind, sourceFlowletBind,
				targetFlowletBind, agentModelBind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.agents.model.AbstractModelDigester#digestBooleanTransition
	 * (repast.simphony.agents.model.bind.BooleanTransitionBind,
	 * repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 * repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 * repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digestBooleanTransition(BooleanTransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {

		digestTransition(transitionBind, sourceFlowletBind, targetFlowletBind,
				agentModelBind, new BooleanTransitionModelPart());
		// digest the bendpoints
		super.digestBooleanTransition(transitionBind, sourceFlowletBind,
				targetFlowletBind, agentModelBind);
	}

	/**
	 * Adds a CreateBendpointCommand to the global CompoundCommand. Calculates
	 * the relative dimensions
	 */
	@Override
	public void digestBendpoint(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, BendpointBind bendpointBind,
			AgentModelBind agentModelBind) {
		TransitionModelPart transition = (TransitionModelPart) transitionBindToTransitionMapping
				.get(transitionBind);

		// create the command
		CreateBendpointCommand command = new CreateBendpointCommand();
		command.setTransition(transition);

		Point bendpointLocation = new Point(bendpointBind.getX(), bendpointBind
				.getY());
		Point sourceFlowletBindLoc = new Point(
				((ILocationHolder) sourceFlowletBind).getX(),
				((ILocationHolder) sourceFlowletBind).getY());
		Point targetFlowletBindLoc = new Point(
				((ILocationHolder) targetFlowletBind).getX(),
				((ILocationHolder) targetFlowletBind).getY());
		repast.simphony.agents.base.Point sourceOffsetFP = AnchorRegistry
				.getSourceAnchorOffset(sourceFlowletBind.getType(),
						transitionBind.getSourceFlowlet().getAnchor());
		Point sourceOffset = new Point(sourceOffsetFP.x, sourceOffsetFP.y);
		Dimension dim1 = bendpointLocation.getDifference(sourceFlowletBindLoc
				.getTranslated(sourceOffset));

		repast.simphony.agents.base.Point targetOffsetFP = AnchorRegistry
				.getTargetAnchorOffset(targetFlowletBind.getType(),
						transitionBind.getTargetFlowlet().getAnchor());
		Point targetOffset = new Point(targetOffsetFP.x, targetOffsetFP.y);
		Dimension dim2 = bendpointLocation.getDifference(targetFlowletBindLoc
				.getTranslated(targetOffset));

		command.setRelativeDimensions(dim1, dim2);
		command.setIndex(transitionBind.getBendpoints().indexOf(bendpointBind));

		createCommand.add(command);
	}

	// ############################################################
	// ######## AgentModelBind Creation ###################
	// ############################################################

	/**
	 * Fills a new AgentModelBind object with the model data, ready for
	 * marshalling
	 * 
	 * @see repast.simphony.agents.model.IModelDigester#createFlowDiagramBind()
	 * @return retruns the filled FlowDiargamBind object
	 */
	public AgentModelBind createFlowModelBind() {
		AgentModelBind agentModelBind = new AgentModelBind(flowDiagramModelPart
				.getAgentName());

		Map flowletToIndexMapping = new HashMap();

		agentModelBind.setComment(flowDiagramModelPart.getAgentComment());

		agentModelBind.setImports(flowDiagramModelPart.getAgentImports());
		agentModelBind.setInterfaces(flowDiagramModelPart.getAgentInterfaces());
		agentModelBind.setParentClassName(flowDiagramModelPart
				.getAgentParentClassName());

		// iterate over all flowlets
		for (Iterator iter = flowDiagramModelPart.getChildren().iterator(); iter
				.hasNext();) {
			AgentDiagramElementModelPart modelPart = (AgentDiagramElementModelPart) iter
					.next();
			IPropertyOrStepBind propertyOrStepBind = null;
			Integer flowletIndex = null;
			// flowlets
			if (modelPart instanceof BehaviorStepModelPart) {
				propertyOrStepBind = createBehaviorStepFlowletBind((BehaviorStepModelPart) modelPart);
				agentModelBind
						.addBehaviorStepFlowlet((BehaviorStepBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind
						.getBehaviorStepFlowlets().indexOf(propertyOrStepBind));
			}
			// task flowlets
			else if (modelPart instanceof TaskStepModelPart) {
				propertyOrStepBind = createTaskFlowletBind((TaskStepModelPart) modelPart);
				agentModelBind
						.addTaskFlowlet((TaskStepBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind.getTaskFlowlets()
						.indexOf(propertyOrStepBind));
			}
			// decision flowlets
			else if (modelPart instanceof DecisionStepModelPart) {
				propertyOrStepBind = createDecisionFlowletBind((DecisionStepModelPart) modelPart);
				agentModelBind
						.addDecisionFlowlet((DecisionStepBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind.getDecisionFlowlets()
						.indexOf(propertyOrStepBind));
			}
			// join flowlets
			else if (modelPart instanceof JoinStepModelPart) {
				propertyOrStepBind = createJoinFlowletBind((JoinStepModelPart) modelPart);
				agentModelBind
						.addJoinFlowlet((JoinStepBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind.getJoinFlowlets()
						.indexOf(propertyOrStepBind));
			}
			// property flowlets
			else if (modelPart instanceof AgentPropertyModelPart) {
				propertyOrStepBind = createPropertyFlowletBind((AgentPropertyModelPart) modelPart);
				agentModelBind
						.addPropertyFlowlet((AgentPropertyBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind.getPropertyFlowlets()
						.indexOf(propertyOrStepBind));
			}
			// end flowlets
			else if (modelPart instanceof EndStepModelPart) {
				propertyOrStepBind = createEndFlowletBind((EndStepModelPart) modelPart);
				agentModelBind.addEndFlowlet((EndStepBind) propertyOrStepBind);
				flowletIndex = new Integer(agentModelBind.getEndFlowlets()
						.indexOf(propertyOrStepBind));
			}
			if (flowletIndex != null)
				flowletToIndexMapping.put(modelPart, flowletIndex);
		}

		// Transitions
		for (Iterator iter = flowDiagramModelPart.getChildren().iterator(); iter
				.hasNext();) {
			AgentDiagramElementModelPart elem = (AgentDiagramElementModelPart) iter
					.next();

			if (elem instanceof DecisionStepModelPart)
				addBooleanTransitionBind(agentModelBind,
						(DecisionStepModelPart) elem, flowletToIndexMapping);
			else if (elem instanceof AgentPropertyorStepModelPart)
				addTransitionBind(agentModelBind,
						(AgentPropertyorStepModelPart) elem,
						flowletToIndexMapping);
		}

		return agentModelBind;
	}

	public IPropertyOrStepBind createFlowletBind(Object modelPart) {

		IPropertyOrStepBind propertyOrStepBind = null;

		if (modelPart instanceof BehaviorStepModelPart) {
			propertyOrStepBind = createBehaviorStepFlowletBind((BehaviorStepModelPart) modelPart);
		}
		// task flowlets
		else if (modelPart instanceof TaskStepModelPart) {
			propertyOrStepBind = createTaskFlowletBind((TaskStepModelPart) modelPart);
		}
		// decision flowlets
		else if (modelPart instanceof DecisionStepModelPart) {
			propertyOrStepBind = createDecisionFlowletBind((DecisionStepModelPart) modelPart);
		}
		// join flowlets
		else if (modelPart instanceof JoinStepModelPart) {
			propertyOrStepBind = createJoinFlowletBind((JoinStepModelPart) modelPart);
		}
		// property flowlets
		else if (modelPart instanceof AgentPropertyModelPart) {
			propertyOrStepBind = createPropertyFlowletBind((AgentPropertyModelPart) modelPart);
		}
		// end flowlets
		else if (modelPart instanceof EndStepModelPart) {
			propertyOrStepBind = createEndFlowletBind((EndStepModelPart) modelPart);
		}

		return propertyOrStepBind;

	}

	public TransitionBind createTransitionBind(
			AgentPropertyorStepModelPart source, int sourceIndex,
			AgentPropertyorStepModelPart target, int targetIndex,
			TransitionModelPart transition) {

		TransitionBind transitionBind = new TransitionBind();

		// source
		TransitionSourceBind transitionSource = new TransitionSourceBind();
		transitionBind.setSourceFlowlet(transitionSource);
		transitionSource.setType(source.getType());
		transitionSource.setIndex(sourceIndex);
		transitionSource.setAnchor(sourceIndex);

		// target
		TransitionTargetBind transitionTarget = new TransitionTargetBind();
		transitionBind.setTargetFlowlet(transitionTarget);
		transitionTarget.setType(target.getType());
		transitionTarget.setIndex(targetIndex);
		transitionTarget.setAnchor(targetIndex);

		// bendpoints
		for (Iterator bendpointsIter = transition.getBendpoints().iterator(); bendpointsIter
				.hasNext();) {
			TransitionBendpointModelPart bendpoint = (TransitionBendpointModelPart) bendpointsIter
					.next();
			BendpointBind bendpointBind = new BendpointBind();
			repast.simphony.agents.base.Point sourceAnchorOffsetFP = AnchorRegistry
					.getSourceAnchorOffset(source.getType(), transition
							.getSourceAnchorIndex());
			Point sourceAnchorOffset = new Point(sourceAnchorOffsetFP.x
					+ PasteAction.OFFSET, sourceAnchorOffsetFP.y
					+ PasteAction.OFFSET);
			bendpointBind.setX(transition.getSource().getLocation().x
					+ sourceAnchorOffset.x
					+ bendpoint.getFirstRelativeDimension().width
					+ PasteAction.OFFSET);
			bendpointBind.setY(transition.getSource().getLocation().y
					+ sourceAnchorOffset.y
					+ bendpoint.getFirstRelativeDimension().height
					+ PasteAction.OFFSET);
			transitionBind.addBendpoint(bendpointBind);
		}

		return transitionBind;

	}

	/**
	 * Creates a new binding object using the data from the model
	 * 
	 * @param label
	 */
	public PropertyOrStepLabelBind createFlowletLabelBind(
			AgentPropertyorStepLabelModelPart label) {
		try {
			PropertyOrStepLabelBind labelBind = new PropertyOrStepLabelBind();
			labelBind.setX(label.getLocation().x);
			labelBind.setY(label.getLocation().y);
			labelBind.setText(label.getText());
			return labelBind;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Creates a new BehaviorStepBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public BehaviorStepBind createBehaviorStepFlowletBind(
			BehaviorStepModelPart flowlet) {

		BehaviorStepBind behaviorStepBind = new BehaviorStepBind();
		behaviorStepBind.setX(flowlet.getLocation().x);
		behaviorStepBind.setY(flowlet.getLocation().y);
		PropertyOrStepLabelBind label = createFlowletLabelBind(flowlet
				.getFlowletLabel());
		if (label != null) behaviorStepBind.setLabel(label);
		behaviorStepBind.setComment(flowlet.getComment());
		behaviorStepBind.setVisibility(flowlet.getVisibility().toString());
		behaviorStepBind.setReturnType(flowlet.getReturnType());
		behaviorStepBind.setCompiledName(flowlet.getCompiledName());
		behaviorStepBind.setParameters(flowlet.getParameters());
		behaviorStepBind.setExceptions(flowlet.getExceptions());

		behaviorStepBind.setScheduleAnnotationStart(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationStart());
		behaviorStepBind.setScheduleAnnotationPick(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationPick());
		behaviorStepBind.setScheduleAnnotationInterval(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationInterval());
		behaviorStepBind.setScheduleAnnotationPriority(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationPriority());
		behaviorStepBind.setScheduleAnnotationDuration(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationDuration());
		behaviorStepBind.setScheduleAnnotationShuffle(flowlet
				.getBehaviorSchedulePropertySource()
				.getScheduleAnnotationShuffle().toString());

		behaviorStepBind.setWatchAnnotationId(flowlet
				.getBehaviorWatchPropertySource().getWatchAnnotationId());
		behaviorStepBind.setWatchAnnotationPick(flowlet
				.getBehaviorWatchPropertySource().getWatchAnnotationPick());
		behaviorStepBind.setWatchAnnotationQuery(flowlet
				.getBehaviorWatchPropertySource().getWatchAnnotationQuery());
		behaviorStepBind.setWatchAnnotationTargetClassName(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTargetClassName());
		behaviorStepBind.setWatchAnnotationTargetFieldNames(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTargetFieldNames());
		behaviorStepBind.setWatchAnnotationTriggerCondition(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTriggerCondition());
		behaviorStepBind.setWatchAnnotationTriggerDelta(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTriggerDelta());
		behaviorStepBind.setWatchAnnotationTriggerSchedule(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTriggerSchedule().toString());
		behaviorStepBind.setWatchAnnotationTriggerPriority(flowlet
				.getBehaviorWatchPropertySource()
				.getWatchAnnotationTriggerPriority());

		return behaviorStepBind;
	}

	/**
	 * Creates a new TaskStepBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public TaskStepBind createTaskFlowletBind(TaskStepModelPart flowlet) {
		TaskStepBind taskFlowletBind = new TaskStepBind();
		taskFlowletBind.setX(flowlet.getLocation().x);
		taskFlowletBind.setY(flowlet.getLocation().y);
		PropertyOrStepLabelBind label = createFlowletLabelBind(flowlet
				.getFlowletLabel());
		if (label != null) taskFlowletBind.setLabel(label);
		taskFlowletBind.setComment(flowlet.getComment());
		taskFlowletBind.setCommand1(flowlet.getTaskCommand1());
		taskFlowletBind.setCommand2(flowlet.getTaskCommand2());
		taskFlowletBind.setCommand3(flowlet.getTaskCommand3());
		taskFlowletBind.setCommand4(flowlet.getTaskCommand4());
		taskFlowletBind.setCommand5(flowlet.getTaskCommand5());

		return taskFlowletBind;
	}

	/**
	 * Creates a new DecisionStepBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public DecisionStepBind createDecisionFlowletBind(
			DecisionStepModelPart flowlet) {
		DecisionStepBind decisionStepBind = new DecisionStepBind();
		decisionStepBind.setX(flowlet.getLocation().x);
		decisionStepBind.setY(flowlet.getLocation().y);
		decisionStepBind.setDescription(flowlet.getComment());
		decisionStepBind.setComment(flowlet.getComment());
		decisionStepBind.setBooleanStatement(flowlet.getCriteria());
		decisionStepBind.setBranchType(flowlet.getBranchType());
		PropertyOrStepLabelBind label = createFlowletLabelBind(flowlet
				.getFlowletLabel());
		if (label != null) decisionStepBind.setLabel(label);
		return decisionStepBind;
	}

	/**
	 * Creates a new JoinStepBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public JoinStepBind createJoinFlowletBind(JoinStepModelPart flowlet) {
		JoinStepBind joinStepBind = new JoinStepBind();
		joinStepBind.setX(flowlet.getLocation().x);
		joinStepBind.setY(flowlet.getLocation().y);

		return joinStepBind;
	}

	/**
	 * Creates a new AgentPropertyBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public AgentPropertyBind createPropertyFlowletBind(
			AgentPropertyModelPart flowlet) {
		AgentPropertyBind agentPropertyBind = new AgentPropertyBind();
		agentPropertyBind.setX(flowlet.getLocation().x);
		agentPropertyBind.setY(flowlet.getLocation().y);
		agentPropertyBind.setDescription(flowlet.getPropertyDescription());
		agentPropertyBind.setPropertyComment(flowlet.getPropertyDescription());
		agentPropertyBind.setPropertyCompiledName(flowlet
				.getPropertyCompiledName());
		agentPropertyBind.setPropertyType(flowlet.getPropertyType());
		agentPropertyBind.setPropertyDefaultValue(flowlet
				.getPropertyDefaultValue());
		agentPropertyBind.setVisibility(flowlet.getVisibility());
		PropertyOrStepLabelBind label = createFlowletLabelBind(flowlet
				.getFlowletLabel());
		if (label != null) agentPropertyBind.setLabel(label);
		return agentPropertyBind;
	}

	/**
	 * Creates a new EndStepBind object using the data from the model
	 * 
	 * @param flowlet
	 */
	public EndStepBind createEndFlowletBind(EndStepModelPart flowlet) {
		EndStepBind endStepBind = new EndStepBind();
		endStepBind.setX(flowlet.getLocation().x);
		endStepBind.setY(flowlet.getLocation().y);

		return endStepBind;
	}

	/**
	 * Iterates over all flowlets and stores the source connections together
	 * with the bendpoints
	 * 
	 * @param transitionsGen
	 *            the container where all source transitions are added
	 * @param flowlet
	 *            source connections and bendpoints of this flowlet are added
	 * @param flowletToIndexMapping
	 *            the map where the flowlets inices are stored inside their
	 *            container
	 */
	public void addTransitionBind(AgentModelBind AgentModelBind,
			AgentPropertyorStepModelPart flowlet, Map flowletToIndexMapping) {
		// iterate over all flowlet's source connections
		for (Iterator transitionsIter = flowlet.getSourceTransitions()
				.iterator(); transitionsIter.hasNext();) {
			TransitionModelPart transition = (TransitionModelPart) transitionsIter
					.next();
			TransitionBind transitionBind = new TransitionBind();
			setTransitionBasicContents(transitionBind, transition,
					flowletToIndexMapping);
			AgentModelBind.addTransition(transitionBind);
		}
	}

	/**
	 * Iterates over all flowlets and stores the source connections together
	 * with the bendpoints of BooleanTransitions
	 * 
	 * @param transitionsGen
	 *            the container where all source transitions are added
	 * @param flowlet
	 *            source connections and bendpoints of this flowlet are added
	 * @param flowletToIndexMapping
	 *            the map where the flowlets inices are stored inside their
	 *            container
	 */
	public void addBooleanTransitionBind(AgentModelBind AgentModelBind,
			DecisionStepModelPart flowlet, Map flowletToIndexMapping) {
		// iterate over all flowlet's source connections
		for (Iterator transitionsIter = flowlet.getSourceTransitions()
				.iterator(); transitionsIter.hasNext();) {
			BooleanTransitionModelPart booleanTransition = (BooleanTransitionModelPart) transitionsIter
					.next();
			BooleanTransitionBind transitionBind = new BooleanTransitionBind();
			setTransitionBasicContents(transitionBind, booleanTransition,
					flowletToIndexMapping);
			// set value
			transitionBind.setValue(booleanTransition.getBoolValue());
			AgentModelBind.addBooleanTransition(transitionBind);
		}
	}

	/**
	 * Sets the basic contents of a transition.
	 * 
	 * @param transitionBind
	 * @param transition
	 * @param flowletToIndexMapping
	 */
	public void setTransitionBasicContents(TransitionBind transitionBind,
			TransitionModelPart transition, Map flowletToIndexMapping) {
		// source
		TransitionSourceBind transitionSource = new TransitionSourceBind();
		AgentPropertyorStepModelPart source = transition.getSource();
		transitionBind.setSourceFlowlet(transitionSource);
		transitionSource.setType(source.getType());
		transitionSource.setIndex(((Integer) flowletToIndexMapping.get(source))
				.intValue());
		transitionSource.setAnchor(transition.getSourceAnchorIndex());
		// target
		TransitionTargetBind transitionTarget = new TransitionTargetBind();
		AgentPropertyorStepModelPart target = transition.getTarget();
		transitionBind.setTargetFlowlet(transitionTarget);
		transitionTarget.setType(target.getType());
		transitionTarget.setIndex(((Integer) flowletToIndexMapping.get(target))
				.intValue());
		transitionTarget.setAnchor(transition.getTargetAnchorIndex());
		// bendpoints
		for (Iterator bendpointsIter = transition.getBendpoints().iterator(); bendpointsIter
				.hasNext();) {
			TransitionBendpointModelPart bendpoint = (TransitionBendpointModelPart) bendpointsIter
					.next();
			BendpointBind bendpointBind = new BendpointBind();
			repast.simphony.agents.base.Point sourceAnchorOffsetFP = AnchorRegistry
					.getSourceAnchorOffset(source.getType(), transition
							.getSourceAnchorIndex());
			Point sourceAnchorOffset = new Point(sourceAnchorOffsetFP.x,
					sourceAnchorOffsetFP.y);
			bendpointBind.setX(transition.getSource().getLocation().x
					+ sourceAnchorOffset.x
					+ bendpoint.getFirstRelativeDimension().width);
			bendpointBind.setY(transition.getSource().getLocation().y
					+ sourceAnchorOffset.y
					+ bendpoint.getFirstRelativeDimension().height);
			transitionBind.addBendpoint(bendpointBind);
		}
	}

}
