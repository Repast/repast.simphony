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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import repast.simphony.agents.designer.editparts.policies.AgentPropertyorStepEditPolicy;
import repast.simphony.agents.designer.editparts.policies.AgentPropertyorStepNodeEditPolicy;
import repast.simphony.agents.designer.figures.AnchorRegistry;
import repast.simphony.agents.designer.figures.PropertyOrStepFigure;
import repast.simphony.agents.designer.model.AgentPropertyorStepModelPart;
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
@SuppressWarnings("unchecked")
public abstract class AgentPropertyorStepEditPart extends
		AgentDiagramElementEditPart implements NodeEditPart {

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				createFlowletNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new AgentPropertyorStepEditPolicy());
	}

	/**
	 * Creates a new AgentPropertyorStepNodeEditPolicy. This method can be
	 * overridden by subclasses to support a more specific policy
	 * 
	 * @return a new AgentPropertyorStepNodeEditPolicy
	 */
	protected AgentPropertyorStepNodeEditPolicy createFlowletNodeEditPolicy() {
		return new AgentPropertyorStepNodeEditPolicy(this);
	}

	/**
	 * Returns the Figure of this, as a flowlet type figure.
	 * 
	 * @return Figure as a PropertyOrStepFigure.
	 */
	public PropertyOrStepFigure getFlowletFigure() {
		return (PropertyOrStepFigure) getFigure();
	}

	/**
	 * Returns the model associated with this as a AgentPropertyorStepModelPart.
	 * 
	 * @return The model of this as a AgentPropertyorStepModelPart.
	 */
	protected AgentPropertyorStepModelPart getFlowlet() {
		return (AgentPropertyorStepModelPart) getModel();
	}

	/**
	 * Handles changes in properties of this. It is activated through the
	 * PropertyChangeListener. It updates children, source and target
	 * connections, and the visuals of this based on the property changed.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * 
	 * @param evt
	 *            Event which details the property change.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if (AgentPropertyorStepModelPart.PROP_TARGET_CONNECTION
				.equals(propName))
			refreshTargetConnections();
		else if (AgentPropertyorStepModelPart.PROP_SOURCE_CONNECTION
				.equals(propName))
			refreshSourceConnections();
		else
			super.propertyChange(evt);
	}

	/**
	 * Returns a list of connections for which this is the source.
	 * 
	 * @return List of connections.
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	@Override
	protected List getModelSourceConnections() {
		return getFlowlet().getSourceTransitions();
	}

	/**
	 * Returns a list of connections for which this is the target.
	 * 
	 * @return List of connections.
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	@Override
	protected List getModelTargetConnections() {
		return getFlowlet().getTargetTransitions();
	}

	/**
	 * Returns a list of unconnected anchors of the type given in the isSource
	 * flag
	 * 
	 * @param isSource
	 *            flag whether source anchors or target anchors (false)
	 * @return a list of unconnected anchors
	 */
	public List getUnconnectedAnchors(boolean isSource) {
		return getUnconnectedAnchors(isSource, null);
	}

	/**
	 * Returns a list of unconnected anchors of the type given in the isSource
	 * flag. If the ignored argument is not null then a list is returned where
	 * the argument is not taken into account
	 * 
	 * @param isSource
	 *            flag whether source anchors or target anchors (false)
	 * @return a list of unconnected anchors
	 */
	@SuppressWarnings("unchecked")
	public List getUnconnectedAnchors(boolean isSource,
			TransitionModelPart ignoredTransition) {
		List anchors = new ArrayList();
		List transitions = isSource ? getFlowlet().getSourceTransitions()
				: getFlowlet().getTargetTransitions();
		List otherTransitions = (!isSource) ? getFlowlet()
				.getSourceTransitions() : getFlowlet().getTargetTransitions();
		String flowletType = getFlowletFigure().getType();
		int maxTransitions = isSource ? AnchorRegistry
				.getMaxSourceTransitions(flowletType) : AnchorRegistry
				.getMaxTargetTransitions(flowletType);

		int transitionsCount = transitions.size();
		if (ignoredTransition != null
				&& (getModelSourceConnections().contains(ignoredTransition) || getModelTargetConnections()
						.contains(ignoredTransition)))
			transitionsCount--;

		if (transitionsCount == maxTransitions)
			return anchors; // already all anchors connected

		List allAnchors = isSource ? getFlowletFigure()
				.getSourceConnectionAnchors() : getFlowletFigure()
				.getTargetConnectionAnchors();
		if (allAnchors.isEmpty())
			return anchors; // some flowlets may have no anchors of the desired
		// type

		// make a copy of the list
		for (int i = 0; i < allAnchors.size(); i++)
			anchors.add(allAnchors.get(i));

		for (Iterator iter = transitions.iterator(); iter.hasNext();) {
			TransitionModelPart trans = (TransitionModelPart) iter.next();
			if (ignoredTransition == trans)
				continue;
			// remove already occupied anchors
			anchors.remove(allAnchors.get(isSource ? trans
					.getSourceAnchorIndex() : trans.getTargetAnchorIndex()));
		}
		for (Iterator iter = otherTransitions.iterator(); iter.hasNext();) {
			TransitionModelPart trans = (TransitionModelPart) iter.next();
			// remove already occupied anchors
			anchors.remove(allAnchors.get((!isSource) ? trans
					.getSourceAnchorIndex() : trans.getTargetAnchorIndex()));
		}
		return anchors;
	}

	/**
	 * Returns the connection anchor for the given ConnectionEditPart's source.
	 * 
	 * @return ConnectionAnchor.
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		TransitionModelPart t = (TransitionModelPart) connEditPart.getModel();
		return getFlowletFigure().getSourceConnectionAnchor(
				t.getSourceAnchorIndex());
	}

	/**
	 * Returns the connection anchor for the given ConnectionEditPart's target.
	 * 
	 * @return ConnectionAnchor.
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		TransitionModelPart t = (TransitionModelPart) connEditPart.getModel();
		return getFlowletFigure().getTargetConnectionAnchor(
				t.getTargetAnchorIndex());
	}

	/**
	 * Returns the connection anchor of a source connection which is at the
	 * given point.
	 * 
	 * @return ConnectionAnchor.
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		List unconnectedAnchors = request instanceof ReconnectRequest ? getUnconnectedAnchors(
				true, (TransitionModelPart) ((ReconnectRequest) request)
						.getConnectionEditPart().getModel())
				: getUnconnectedAnchors(true);
		return PropertyOrStepFigure.getClosestConnectionAnchorAt(pt,
				unconnectedAnchors);
	}

	/**
	 * Returns the connection anchor of a terget connection which is at the
	 * given point.
	 * 
	 * @return ConnectionAnchor.
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		List unconnectedAnchors = request instanceof ReconnectRequest ? getUnconnectedAnchors(
				false, (TransitionModelPart) ((ReconnectRequest) request)
						.getConnectionEditPart().getModel())
				: getUnconnectedAnchors(false);
		return PropertyOrStepFigure.getClosestConnectionAnchorAt(pt,
				unconnectedAnchors);
	}

}
