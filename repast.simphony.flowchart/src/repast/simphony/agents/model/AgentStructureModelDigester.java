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

package repast.simphony.agents.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.BooleanTransitionBind;
import repast.simphony.agents.model.bind.IPropertyOrStepBind;
import repast.simphony.agents.model.bind.TransitionBind;

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
public class AgentStructureModelDigester extends AbstractModelDigester {

	private List nodes = new ArrayList();
	private Map flowletId2NodeMap = new HashMap();

	protected List getStartFlowletNodes() {
		List startFowletNodes = new ArrayList();
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			IFlowNode flowNode = (IFlowNode) iter.next();
			if (flowNode instanceof StartFlowletNode)
				startFowletNodes.add(flowNode);
		}

		return startFowletNodes;
	}

	protected List getPropertyFlowletNodes() {
		List templateFowletNodes = new ArrayList();
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			IFlowNode flowNode = (IFlowNode) iter.next();
			if (flowNode instanceof PropertyFlowletNode)
				templateFowletNodes.add(flowNode);
		}

		return templateFowletNodes;
	}

	/**
	 * 
	 * TODO
	 * 
	 * @param propertyOrStepBind
	 */
	protected void addFlowletNode(AbstractFlowletNode flowNode) {
		if (nodes.contains(flowNode))
			return;
		nodes.add(flowNode);
		flowletId2NodeMap.put(flowNode.getFlowletBind().getId(
				flowNode.getFlowModelBind()), flowNode);
	}

	/**
	 * 
	 * Class IFlowNode TODO
	 * 
	 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony
	 *         from Alexander Greif’s Flow4J-Eclipse
	 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with
	 *         Thanks to the Original Author)
	 * 
	 */
	protected interface IFlowNode {
		void addPreviousNode(IFlowNode flowNode);

		void addFollowingNode(IFlowNode flowNode);

		void visit(IAgentVisitor visitor);

		boolean isVisited();
	}

	protected abstract class AbstractFlowletNode implements IFlowNode {
		protected AgentModelBind agentModelBind;
		protected IPropertyOrStepBind propertyOrStepBind;
		protected boolean isVisited;

		AbstractFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			this.propertyOrStepBind = propertyOrStepBind;
			this.agentModelBind = agentModelBind;
		}

		public IPropertyOrStepBind getFlowletBind() {
			return propertyOrStepBind;
		}

		@Override
		public boolean equals(Object arg0) {
			return this.getFlowletBind().equals(
					((AbstractFlowletNode) arg0).getFlowletBind());
		}

		public void addFollowingNode(IFlowNode flowNode) {
		}

		public void addPreviousNode(IFlowNode flowNode) {
		}

		public abstract void visit(IAgentVisitor visitor);

		public void setVisited(boolean isVisited) {
			this.isVisited = isVisited;
		}

		public boolean isVisited() {
			return isVisited;
		}

		/**
		 * @return
		 */
		public AgentModelBind getFlowModelBind() {
			return agentModelBind;
		}

	}

	/**
	 * 
	 * Class SimpleFlowNode TODO
	 * 
	 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony
	 *         from Alexander Greif’s Flow4J-Eclipse
	 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with
	 *         Thanks to the Original Author)
	 * 
	 */
	private abstract class SimpleFlowletNode extends AbstractFlowletNode {
		IFlowNode previousFlowletNode;
		IFlowNode followingFlowletNode;

		SimpleFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addFollowingNode(IFlowNode flowletNode) {
			followingFlowletNode = flowletNode;
		}

		@Override
		public void addPreviousNode(IFlowNode flowletNode) {
			previousFlowletNode = flowletNode;
		}

		protected IFlowNode getPreviousFlowletNode() {
			return previousFlowletNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (followingFlowletNode != null)
				followingFlowletNode.visit(visitor);
		}
	}

	public class StartFlowletNode extends AbstractFlowletNode {
		IFlowNode followingFlowletNode;

		public StartFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addFollowingNode(IFlowNode flowNode) {
			followingFlowletNode = flowNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (followingFlowletNode != null)
				followingFlowletNode.visit(visitor);
		}
	}

	public class TaskFlowletNode extends SimpleFlowletNode {
		public TaskFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}
	}

	public class DecisionFlowletNode extends AbstractFlowletNode {
		IFlowNode previousFlowletNode;
		IFlowNode followingTrueFlowletNode;
		IFlowNode followingFalseFlowletNode;

		public DecisionFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addPreviousNode(IFlowNode flowNode) {
			previousFlowletNode = flowNode;
		}

		protected IFlowNode getPreviousFlowletNode() {
			return previousFlowletNode;
		}

		public void addFollowingNode(IFlowNode flowNode, boolean isTrue) {
			if (isTrue)
				followingTrueFlowletNode = flowNode;
			else
				followingFalseFlowletNode = flowNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (followingTrueFlowletNode != null)
				followingTrueFlowletNode.visit(visitor);
			if (followingFalseFlowletNode != null)
				followingFalseFlowletNode.visit(visitor);
		}

		public IFlowNode getFollowingFalseFlowletNode() {
			return followingFalseFlowletNode;
		}

		public IFlowNode getFollowingTrueFlowletNode() {
			return followingTrueFlowletNode;
		}

	}

	public class LoopFlowletNode extends SimpleFlowletNode {
		IFlowNode previousFlowletNode;
		IFlowNode followingTrueFlowletNode;
		IFlowNode followingFalseFlowletNode;

		public LoopFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addPreviousNode(IFlowNode flowNode) {
			previousFlowletNode = flowNode;
		}

		@Override
		protected IFlowNode getPreviousFlowletNode() {
			return previousFlowletNode;
		}

		public void addFollowingNode(IFlowNode flowNode, boolean isTrue) {
			if (isTrue)
				followingTrueFlowletNode = flowNode;
			else
				followingFalseFlowletNode = flowNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (followingTrueFlowletNode != null)
				followingTrueFlowletNode.visit(visitor);
			if (followingFalseFlowletNode != null)
				followingFalseFlowletNode.visit(visitor);
		}

		public IFlowNode getFollowingFalseFlowletNode() {
			return followingFalseFlowletNode;
		}

		public IFlowNode getFollowingTrueFlowletNode() {
			return followingTrueFlowletNode;
		}

	}

	public class JoinFlowletNode extends AbstractFlowletNode {
		List previousFlowletNodes = new ArrayList();
		public IFlowNode followingFlowletNode;

		public JoinFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addFollowingNode(IFlowNode flowNode) {
			followingFlowletNode = flowNode;
		}

		@Override
		public void addPreviousNode(IFlowNode flowNode) {
			previousFlowletNodes.add(flowNode);
		}

		protected List getPreviousFlowletNodes() {
			return previousFlowletNodes;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (followingFlowletNode != null)
				followingFlowletNode.visit(visitor);
		}
	}

	public class PropertyFlowletNode extends AbstractFlowletNode {
		IFlowNode previousFlowletNode;

		public PropertyFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addPreviousNode(IFlowNode flowletNode) {
			previousFlowletNode = flowletNode;
		}

		protected IFlowNode getPreviousFlowletNode() {
			return previousFlowletNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
		}
	}

	public class EndFlowletNode extends AbstractFlowletNode {
		IFlowNode previousFlowletNode;

		public EndFlowletNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void addPreviousNode(IFlowNode flowletNode) {
			previousFlowletNode = flowletNode;
		}

		protected IFlowNode getPreviousFlowletNode() {
			return previousFlowletNode;
		}

		@Override
		public void visit(IAgentVisitor visitor) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.AbstractModelDigester#digestBooleanTransition(repast.simphony.agents.model.bind.BooleanTransitionBind,
	 *      repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 *      repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 *      repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	protected void digestBooleanTransition(
			BooleanTransitionBind booleanTransitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {
		DecisionFlowletNode sourceNode = (DecisionFlowletNode) flowletId2NodeMap
				.get(sourceFlowletBind.getId(agentModelBind));
		IFlowNode targetNode = (IFlowNode) flowletId2NodeMap
				.get(targetFlowletBind.getId(agentModelBind));
		sourceNode.addFollowingNode(targetNode, booleanTransitionBind
				.getValue());
		targetNode.addPreviousNode(sourceNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.AbstractModelDigester#digestTransition(repast.simphony.agents.model.bind.TransitionBind,
	 *      repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 *      repast.simphony.agents.model.bind.IPropertyOrStepBind,
	 *      repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	protected void digestTransition(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {
		IFlowNode sourceNode = (IFlowNode) flowletId2NodeMap
				.get(sourceFlowletBind.getId(agentModelBind));
		IFlowNode targetNode = (IFlowNode) flowletId2NodeMap
				.get(targetFlowletBind.getId(agentModelBind));
		sourceNode.addFollowingNode(targetNode);
		targetNode.addPreviousNode(sourceNode);
	}

}
