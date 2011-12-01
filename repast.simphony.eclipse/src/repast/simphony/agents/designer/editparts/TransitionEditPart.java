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
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import repast.simphony.agents.designer.editparts.policies.TransitionBendpointEditPolicy;
import repast.simphony.agents.designer.editparts.policies.TransitionEditPolicy;
import repast.simphony.agents.designer.editparts.policies.TransitionEndpointEditPolicy;
import repast.simphony.agents.designer.figures.AgentBuilderFigureFactory;
import repast.simphony.agents.designer.model.TransitionBendpointModelPart;
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
public class TransitionEditPart extends AbstractConnectionEditPart implements
		PropertyChangeListener {

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new TransitionEndpointEditPolicy());
		// Note that the Connection is already added to the diagram and knows
		// its Router.
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new TransitionBendpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new TransitionEditPolicy());
	}

	/**
	 * Returns a newly created Figure to represent the connection.
	 * 
	 * @return The created transition Figure.
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		if (getTransition() == null)
			return null;
		Connection transition = AgentBuilderFigureFactory
				.createBendableTransition(getTransition());

		return transition;
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		getTransition().addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		getTransition().removePropertyChangeListener(this);
		super.deactivate();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#activateFigure()
	 */
	@Override
	public void activateFigure() {
		super.activateFigure();
		/*
		 * Once the figure has been added to the ConnectionLayer, start
		 * listening for its router to change.
		 */
		getFigure().addPropertyChangeListener(
				Connection.PROPERTY_CONNECTION_ROUTER, this);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#deactivateFigure()
	 */
	@Override
	public void deactivateFigure() {
		getFigure().removePropertyChangeListener(
				Connection.PROPERTY_CONNECTION_ROUTER, this);
		super.deactivateFigure();
	}

	/**
	 * Returns the model of this represented as a TransitionModelPart.
	 * 
	 * @return Model of this as <code>TransitionModelPart</code>
	 */
	protected TransitionModelPart getTransition() {
		return (TransitionModelPart) getModel();
	}

	/**
	 * Returns the Figure associated with this, which draws the
	 * TransitionModelPart.
	 * 
	 * @return Figure of this.
	 */
	protected IFigure getTransitionFigure() {
		return getFigure();
	}

	/**
	 * Listens to changes in properties of the TransitionModelPart (like the
	 * contents being carried), and reflects is in the visuals.
	 * 
	 * @param event
	 *            Event notifying the change.
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (Connection.PROPERTY_CONNECTION_ROUTER.equals(property))
			refreshBendpoints();

		// if ("value".equals(property)) //$NON-NLS-1$
		// refreshVisuals();
		else if (TransitionModelPart.PROP_BENDPOINT.equals(property))
			refreshBendpoints();
		else
			System.out
					.println("TransitionEditPart.propertyChange() unknown event: " + property); //$NON-NLS-1$
	}

	/**
	 * Updates the bendpoints, based on the model.
	 */
	@SuppressWarnings("unchecked")
	protected void refreshBendpoints() {
		List modelConstraint = getTransition().getBendpoints();
		List figureConstraint = new ArrayList();
		for (int i = 0; i < modelConstraint.size(); i++) {
			TransitionBendpointModelPart tbp = (TransitionBendpointModelPart) modelConstraint
					.get(i);
			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
			rbp.setRelativeDimensions(tbp.getFirstRelativeDimension(), tbp
					.getSecondRelativeDimension());
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}

	/**
	 * Refreshes the visual aspects of this, based upon the model (Wire). It
	 * changes the wire color depending on the state of Wire.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshBendpoints();
	}

}
