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

package repast.simphony.agents.designer.editparts;

import java.util.List;

import org.eclipse.gef.EditPolicy;

import repast.simphony.agents.designer.editparts.policies.AgentBuilderXYLayoutEditPolicy;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;

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
@SuppressWarnings("unchecked")
public abstract class AgentBuilderContainerEditPart extends
		AgentBuilderEditPart {

	/**
	 * @see repast.simphony.agents.designer.editparts.Flow4JEditPart#createAccessible()
	 */
	/*
	 * protected AccessibleEditPart createAccessible() { return new
	 * AccessibleGraphicalEditPart(){ public void getName(AccessibleEvent e) {
	 * e.result = getFlowDiagram().toString(); } }; }
	 */

	/**
	 * Returns the model of this as a FlowDiagramModelPart.
	 * 
	 * @return LogicDiagram of this.
	 */
	protected AgentDiagramModelPart getFlowDiagram() {
		return (AgentDiagramModelPart) getModel();
	}

	/**
	 * Installs the desired EditPolicies for this.
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// installEditPolicy(EditPolicy.CONTAINER_ROLE, new
		// Flow4JContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new AgentBuilderXYLayoutEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	/*
	 * protected IFigure createFigure() { // TODO Auto-generated method stub
	 * return null; }
	 */

	/**
	 * Returns the children of this through the model.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 * @return Children of this as a List.
	 */
	@Override
	protected List getModelChildren() {
		return getFlowDiagram().getChildren();
	}

}
