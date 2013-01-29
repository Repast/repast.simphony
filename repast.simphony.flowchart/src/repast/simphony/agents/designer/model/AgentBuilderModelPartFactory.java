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

package repast.simphony.agents.designer.model;

import org.eclipse.gef.requests.CreationFactory;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class AgentBuilderModelPartFactory implements CreationFactory {

	private Object templateType;

	/**
	 * TODO
	 * 
	 * @param template
	 */
	public AgentBuilderModelPartFactory() {
		super();
	}

	/**
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject() {
		if (BehaviorStepModelPart.class.equals(templateType))
			return new BehaviorStepModelPart();

		if (TaskStepModelPart.class.equals(templateType))
			return new TaskStepModelPart();

		if (DecisionStepModelPart.class.equals(templateType))
			return new DecisionStepModelPart();

		if (JoinStepModelPart.class.equals(templateType))
			return new JoinStepModelPart();

		if (AgentPropertyModelPart.class.equals(templateType))
			return new AgentPropertyModelPart();

		if (EndStepModelPart.class.equals(templateType))
			return new EndStepModelPart();

		System.out
				.println("AgentBuilderModelPartFactory.getNewObject() unknown template: "
						+ templateType);
		return null;
	}

	/**
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType() {
		return templateType;
	}

	/**
	 * @param string
	 */
	public void setTemplateType(Object obj) {
		templateType = obj;
	}

	/**
	 * Creates flowlet label.
	 * 
	 * @param flowletWithLabel
	 *            the labels parent
	 * @return a newly created label model
	 */
	static public AgentPropertyorStepLabelModelPart createFlowletLabelModelPart(
			AgentPropertyorStepWithLabelModelPart flowletWithLabel) {
		if (flowletWithLabel instanceof TaskStepModelPart)
			return new TaskStepLabelModelPart();
		if (flowletWithLabel instanceof DecisionStepModelPart)
			return new DecisionStepLabelModelPart();
		if (flowletWithLabel instanceof AgentPropertyModelPart)
			return new AgentPropertyLabelModelPart();

		return new AgentPropertyorStepLabelModelPart();
	}

}
