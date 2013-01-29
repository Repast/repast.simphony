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

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import repast.simphony.agents.designer.editparts.policies.AgentDiagramElementEditPolicy;
import repast.simphony.agents.designer.model.AgentDiagramElementModelPart;

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
public abstract class AgentDiagramElementEditPart extends AgentBuilderEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new AgentDiagramElementEditPolicy());
		// removeEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE);

	}

	/**
	 * Returns <code>true</code> if the editPart has properties, and thus can
	 * show the properties view.
	 * 
	 * @return <code>true</code> if the editPart has properties
	 */
	public abstract boolean hasProperties();

	/**
	 * Returns the diagram element's tool tip of type
	 * <code>IAgentBuilderToolTip</code>.
	 * 
	 * @return the diagram element's tool tip of type
	 *         <code>IAgentBuilderToolTip</code>.
	 */
	public IAgentBuilderToolTip getFlowDiagramElementToolTip() {
		return (IAgentBuilderToolTip) getFigure().getToolTip();
	}

	/**
	 * Returns the model instance.
	 * 
	 * @return the model instance.
	 */
	private AgentDiagramElementModelPart getFlowDiagramElement() {
		return (AgentDiagramElementModelPart) getModel();
	}

	/**
	 * Register the editPart as a property change listener of the modelPart
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive())
			return;
		super.activate();
		getFlowDiagramElement().addPropertyChangeListener(this);
	}

	/**
	 * Makes the EditPart insensible to changes in the model by removing itself
	 * from the model's list of listeners.
	 */
	@Override
	public void deactivate() {
		if (!isActive())
			return;
		super.deactivate();
		getFlowDiagramElement().removePropertyChangeListener(this);
	}

	/**
	 * Handles changes in properties of this. It is activated through the
	 * PropertyChangeListener. It updates children, source and target
	 * connections, and the visuals of this based on the property changed.
	 * 
	 * @param evt
	 *            Event which details the property change.
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * 
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if (AgentDiagramElementModelPart.PROP_LOCATION.equals(propName))
			refreshVisuals();
	}

	/**
	 * Updates the visual aspect of this.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		try {
			Point point = getFlowDiagramElement().getLocation();
			if (point != null) {
				// Dimension size = getAgentBuilderModelPart().getSize();
				Rectangle r = new Rectangle(point.x, point.y, -1, -1); // preferred
																		// size
				((GraphicalEditPart) getParent()).setLayoutConstraint(this,
						getFigure(), r);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
