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

package repast.simphony.agents.designer.editparts.policies;

import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import repast.simphony.agents.designer.editparts.AgentPropertyorStepEditPart;
import repast.simphony.agents.designer.figures.AgentBuilderFigureFactory;
import repast.simphony.agents.designer.model.AgentPropertyorStepModelPart;
import repast.simphony.agents.designer.model.TransitionModelPart;
import repast.simphony.agents.designer.model.commands.ConnectionCommand;

/**
 * 
 * A AgentPropertyorStepNodeEditPolicy is responsible for creating and
 * reconnecting flowlet connections graphically.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
@SuppressWarnings("unchecked")
public class AgentPropertyorStepNodeEditPolicy extends GraphicalNodeEditPolicy {

	private AgentPropertyorStepEditPart agentPropertyorStepEditPart;

	/**
	 * Constructor
	 * 
	 * @param agentPropertyorStepEditPart
	 */
	public AgentPropertyorStepNodeEditPolicy(
			AgentPropertyorStepEditPart agentPropertyorStepEditPart) {
		this.agentPropertyorStepEditPart = agentPropertyorStepEditPart;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#createDummyConnection(org.eclipse.gef.Request)
	 */
	@Override
	protected Connection createDummyConnection(Request req) {
		PolylineConnection conn = AgentBuilderFigureFactory
				.createTransition(null);
		return conn;
	}

	/**
	 * Returns a new ConnectionCommand. Subclasses can override this method to
	 * return a more specific ConnectionCommand type
	 * 
	 * @return a new ConnectionCommand
	 */
	protected ConnectionCommand createConnectionCommand() {
		return new ConnectionCommand();
	}

	/**
	 * Returns a new TransitionModelPart. Subclasses can override this method to
	 * return a more specific TransitionModelPart type
	 * 
	 * @return a new TransitionModelPart
	 */
	protected TransitionModelPart createTransitionModelPart() {
		return new TransitionModelPart();
	}

	/**
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		List unconnectedAnchors = agentPropertyorStepEditPart
				.getUnconnectedAnchors(true);
		if (unconnectedAnchors.isEmpty())
			return null;

		ConnectionCommand command = createConnectionCommand();
		command.setTransition(createTransitionModelPart());
		command.setSource(getFlowlet());
		ConnectionAnchor anchor = getFlowletEditPart()
				.getSourceConnectionAnchor(request);
		command.setSourceAnchorIndex(getFlowletEditPart().getFlowletFigure()
				.getSourceConnectionAnchorIndex(anchor));
		request.setStartCommand(command);

		return command;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		List unconnectedAnchors = ((AgentPropertyorStepEditPart) request
				.getTargetEditPart()).getUnconnectedAnchors(false);
		if (unconnectedAnchors.isEmpty())
			return null;

		ConnectionCommand command = (ConnectionCommand) request
				.getStartCommand();
		command.setTarget(getFlowlet());
		ConnectionAnchor anchor = getFlowletEditPart()
				.getTargetConnectionAnchor(request);
		if (anchor == null)
			return null;
		command.setTargetAnchorIndex(getFlowletEditPart().getFlowletFigure()
				.getTargetConnectionAnchorIndex(anchor));

		return command;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected AgentPropertyorStepModelPart getFlowlet() {
		return (AgentPropertyorStepModelPart) getHost().getModel();
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected AgentPropertyorStepEditPart getFlowletEditPart() {
		return (AgentPropertyorStepEditPart) getHost();
	}

	/**
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		TransitionModelPart transition = (TransitionModelPart) request
				.getConnectionEditPart().getModel();
		// check unconnected anchors
		if (getFlowletEditPart().getUnconnectedAnchors(true, transition)
				.isEmpty())
			return null;

		ConnectionCommand command = createConnectionCommand();
		command.setTransition(transition);

		ConnectionAnchor ctor = getFlowletEditPart().getSourceConnectionAnchor(
				request);
		command.setSource(getFlowlet());
		command.setSourceAnchorIndex(getFlowletEditPart().getFlowletFigure()
				.getSourceConnectionAnchorIndex(ctor));
		return command;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		TransitionModelPart transition = (TransitionModelPart) request
				.getConnectionEditPart().getModel();
		// check unconnected anchors
		if (getFlowletEditPart().getUnconnectedAnchors(false, transition)
				.isEmpty())
			return null;

		ConnectionCommand command = createConnectionCommand();
		command.setTransition((TransitionModelPart) request
				.getConnectionEditPart().getModel());

		ConnectionAnchor anchor = getFlowletEditPart()
				.getTargetConnectionAnchor(request);
		command.setTarget(getFlowlet());
		command.setTargetAnchorIndex(getFlowletEditPart().getFlowletFigure()
				.getTargetConnectionAnchorIndex(anchor));
		return command;
	}

}
