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

package repast.simphony.agents.model.bind;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.agents.AgentBuilderConsts;

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
public class AgentModelBind {

	public static String ALIAS = "agent";

	private int version = AgentBuilderConsts.AGENT_FILE_VERSION;

	private String comment = "This is an agent.";
	private String flowName;
	private String parentClassName = "";
	private String interfaces = "";
	private String imports = "";

	private List startFlowlets;
	private List taskFlowlets;
	private List decisionFlowlets;
	private List joinFlowlets;
	private List propertyFlowlets;
	private List endFlowlets;

	private List transitions;
	private List booleanTransitions;

	public AgentModelBind() {
		super();
	}

	public AgentModelBind(String flowName) {
		super();
		this.flowName = flowName;
		setVersion(AgentBuilderConsts.AGENT_FILE_VERSION);
	}

	public void addBehaviorStepFlowlet(BehaviorStepBind flowletBind) {
		if (startFlowlets == null)
			startFlowlets = new ArrayList();
		startFlowlets.add(flowletBind);
	}

	public void addTaskFlowlet(TaskStepBind flowletBind) {
		if (taskFlowlets == null)
			taskFlowlets = new ArrayList();
		taskFlowlets.add(flowletBind);
	}

	public void addDecisionFlowlet(DecisionStepBind flowletBind) {
		if (decisionFlowlets == null)
			decisionFlowlets = new ArrayList();
		decisionFlowlets.add(flowletBind);
	}

	public void addJoinFlowlet(JoinStepBind flowletBind) {
		if (joinFlowlets == null)
			joinFlowlets = new ArrayList();
		joinFlowlets.add(flowletBind);
	}

	public void addPropertyFlowlet(AgentPropertyBind flowletBind) {
		if (propertyFlowlets == null)
			propertyFlowlets = new ArrayList();
		propertyFlowlets.add(flowletBind);
	}

	public void addEndFlowlet(EndStepBind flowletBind) {
		if (endFlowlets == null)
			endFlowlets = new ArrayList();
		endFlowlets.add(flowletBind);
	}

	public void addTransition(TransitionBind transitionBind) {
		if (transitions == null)
			transitions = new ArrayList();
		transitions.add(transitionBind);
	}

	public void addBooleanTransition(BooleanTransitionBind transitionBind) {
		if (booleanTransitions == null)
			booleanTransitions = new ArrayList();
		booleanTransitions.add(transitionBind);
	}

	/**
	 * @return
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * @return
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param string
	 */
	public void setFlowName(String string) {
		flowName = string;
	}

	/**
	 * @param i
	 */
	public void setVersion(int i) {
		version = i;
	}

	/**
	 * @return
	 */
	public List getBehaviorStepFlowlets() {
		return startFlowlets;
	}

	/**
	 * @param list
	 */
	public void setStartFlowlets(List list) {
		startFlowlets = list;
	}

	/**
	 * @return
	 */
	public List getDecisionFlowlets() {
		return decisionFlowlets;
	}

	/**
	 * @return
	 */
	public List getEndFlowlets() {
		return endFlowlets;
	}

	/**
	 * @return
	 */
	public List getTaskFlowlets() {
		return taskFlowlets;
	}

	/**
	 * @return
	 */
	public List getJoinFlowlets() {
		return joinFlowlets;
	}

	/**
	 * @return
	 */
	public List getPropertyFlowlets() {
		return propertyFlowlets;
	}

	/**
	 * @param list
	 */
	public void setDecisionFlowlets(List list) {
		decisionFlowlets = list;
	}

	/**
	 * @param list
	 */
	public void setEndFlowlets(List list) {
		endFlowlets = list;
	}

	/**
	 * @param list
	 */
	public void setTaskFlowlets(List list) {
		taskFlowlets = list;
	}

	/**
	 * @param list
	 */
	public void setJoinFlowlets(List list) {
		joinFlowlets = list;
	}

	/**
	 * @param list
	 */
	public void setPropertyFlowlets(List list) {
		propertyFlowlets = list;
	}

	/**
	 * @return
	 */
	public List getBooleanTransitions() {
		return booleanTransitions;
	}

	/**
	 * @return
	 */
	public List getTransitions() {
		return transitions;
	}

	/**
	 * @param list
	 */
	public void setBooleanTransitions(List list) {
		booleanTransitions = list;
	}

	/**
	 * @param list
	 */
	public void setTransitions(List list) {
		transitions = list;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public String getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comments) {
		this.comment = comments;
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

}
