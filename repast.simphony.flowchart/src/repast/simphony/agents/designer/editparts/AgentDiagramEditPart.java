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

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;

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
public class AgentDiagramEditPart extends AgentBuilderContainerEditPart
		implements LayerConstants {

	private IFile flowFile;

	/**
	 * Creates the editPart instance
	 * 
	 * @param flowFile
	 *            the flow file
	 */
	public AgentDiagramEditPart(IFile flowFile) {
		super();
		this.flowFile = flowFile;
	}

	/**
	 * @see repast.simphony.agents.designer.editparts.Flow4JEditPart#createAccessible()
	 */
	/*
	 * protected AccessibleEditPart createAccessible() { return new
	 * AccessibleGraphicalEditPart(){ public void getName(AccessibleEvent e) {
	 * e.result = getFlowDiagram().toString(); } }; }
	 */

	/**
	 * Installs EditPolicies specific to this.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// installEditPolicy(EditPolicy.NODE_ROLE, null);
		// installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		// installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new
		// RootComponentEditPolicy());
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive())
			return;
		super.activate();
		getFlowDiagram().addStructureChangeListener(this);
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
		getFlowDiagram().removeStructureChangeListener(this);
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
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if (AgentDiagramModelPart.CHILDREN.equals(propName))
			refreshChildren();
	}

	/**
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request req) {
		if (req instanceof SelectionRequest
				&& ((SelectionRequest) req).getLastButtonPressed() == 3)
			return new DeselectAllTracker(this);
		return new MarqueeDragTracker();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setLayoutManager(new FreeformLayout());
		f.setBorder(new MarginBorder(5));
		f.setBackgroundColor(ColorConstants.listBackground);
		f.setOpaque(true);
		return f;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);
		AutomaticRouter router = new FanRouter();
		router.setNextRouter(new BendpointConnectionRouter());
		cLayer.setConnectionRouter(router);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#addChild(org.eclipse.gef.EditPart,
	 *      int)
	 */
	@Override
	protected void addChild(EditPart child, int index) {
		// TODO Auto-generated method stub
		super.addChild(child, index);
	}

	/**
	 * Returns the file of this flow diagram
	 * 
	 * @return the file of this flow diagram
	 */
	public IFile getFlowFile() {
		return flowFile;
	}

}
