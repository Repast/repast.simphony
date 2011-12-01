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

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.designer.model.DecisionStepModelPart;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
public class DecisionStepBind extends PropertyOrStepWithLabelBind {

	public static String ALIAS = "decisionstep";

	private String description;
	private String booleanStatement = "";
	private String comment = "";
	private String branchType = DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_IF;

	/**
	 * TODO
	 * 
	 * @see repast.simphony.agents.model.bind.IPropertyOrStepBind#getId()
	 */
	public String getId(AgentModelBind agentModelBind) {
		return BindConsts.REF_PREFIX_DECISION_FLOWLET
				+ agentModelBind.getDecisionFlowlets().indexOf(this);
	}

	/**
	 * @see repast.simphony.agents.model.bind.IPropertyOrStepBind#getType()
	 */
	public String getType() {
		return AgentBuilderConsts.DECISION;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBooleanStatement() {
		return booleanStatement;
	}

	public void setBooleanStatement(String booleanStatement) {
		this.booleanStatement = booleanStatement;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

}
