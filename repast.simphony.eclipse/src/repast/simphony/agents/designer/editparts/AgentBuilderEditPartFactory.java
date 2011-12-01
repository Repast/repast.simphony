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

package repast.simphony.agents.designer.editparts;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.model.AgentPropertyLabelModelPart;
import repast.simphony.agents.designer.model.AgentPropertyModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepLabelModelPart;
import repast.simphony.agents.designer.model.BehaviorStepModelPart;
import repast.simphony.agents.designer.model.BooleanTransitionModelPart;
import repast.simphony.agents.designer.model.DecisionStepLabelModelPart;
import repast.simphony.agents.designer.model.DecisionStepModelPart;
import repast.simphony.agents.designer.model.EndStepModelPart;
import repast.simphony.agents.designer.model.JoinStepModelPart;
import repast.simphony.agents.designer.model.TaskStepLabelModelPart;
import repast.simphony.agents.designer.model.TaskStepModelPart;
import repast.simphony.agents.designer.model.TransitionModelPart;

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
public class AgentBuilderEditPartFactory implements EditPartFactory {

	private IFile flowFile;

	/**
	 * Creates the factory instance
	 * 
	 * @param flowFile
	 *            the flow file
	 */
	public AgentBuilderEditPartFactory(IFile flowFile) {
		this.flowFile = flowFile;
	}

	/**
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart child = null;

		if (model instanceof BehaviorStepModelPart)
			child = new BehaviorStepEditPart();
		else if (model instanceof TaskStepModelPart)
			child = new TaskStepEditPart();
		else if (model instanceof DecisionStepModelPart)
			child = new DecisionStepEditPart((DecisionStepModelPart) model);
		else if (model instanceof JoinStepModelPart)
			child = new JoinStepEditPart();
		else if (model instanceof AgentPropertyModelPart)
			child = new AgentPropertyEditPart();
		else if (model instanceof EndStepModelPart)
			child = new EndStepEditPart();

		// order is important
		else if (model instanceof TaskStepLabelModelPart)
			child = new TaskFlowletLabelEditPart();
		else if (model instanceof DecisionStepLabelModelPart)
			child = new DecisionStepLabelEditPart();
		else if (model instanceof AgentPropertyLabelModelPart)
			child = new AgentPropertyLabelEditPart();
		else if (model instanceof AgentPropertyorStepLabelModelPart)
			child = new AgentPropertyorStepLabelEditPart();

		// order is important because BooleanTransitionModelPart extends
		// TransitionModelPart
		else if (model instanceof BooleanTransitionModelPart)
			child = new BooleanTransitionEditPart();
		else if (model instanceof TransitionModelPart)
			child = new TransitionEditPart();

		// Note that subclasses of FlowDiagramModelPart have already been
		// matched above
		else if (model instanceof AgentDiagramModelPart)
			child = new AgentDiagramEditPart(flowFile);
		else
			System.out
					.println("AgentBuilderEditPartFactory.createEditPart() unknown model type: "
							+ model.getClass().getName());
		child.setModel(model);

		return child;
	}

}
