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

import org.eclipse.draw2d.IFigure;

import repast.simphony.agents.designer.figures.AgentBuilderFigureFactory;
import repast.simphony.agents.designer.figures.BehaviorStepFigure;
import repast.simphony.agents.designer.figures.BehaviorStepToolTipFigure;
import repast.simphony.agents.designer.model.BehaviorStepModelPart;

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
public class BehaviorStepEditPart extends AgentPropertyorStepWithLabelEditPart {

	/**
	 * The toolTip figure that will be reused for flowlet figure creation.
	 */
	private BehaviorStepToolTipFigure toolTip;

	/**
	 * Constructor. Sets the tooltip.
	 */
	public BehaviorStepEditPart() {
		super();
		toolTip = new BehaviorStepToolTipFigure(this);
	}

	/**
	 * @see repast.simphony.agents.designer.editparts.Flow4JEditPart#createAccessible()
	 */
	/*
	 * protected AccessibleEditPart createAccessible() { return new
	 * AccessibleGraphicalEditPart(){ public void getName(AccessibleEvent e) {
	 * e.result = getStartFlowlet().toString(); } }; }
	 */

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		if (getModel() == null)
			return null;

		IFigure figure = AgentBuilderFigureFactory.createStartFlowletFigure();
		figure.setToolTip(toolTip);

		return figure;
	}

	/**
	 * Returns the Figure for this as an OutputFigure.
	 * 
	 * @return Figure of this as a OutputFigure.
	 */
	protected BehaviorStepFigure getStartFlowletFigure() {
		return (BehaviorStepFigure) getFigure();
	}

	/**
	 * Returns the model of this as a BehaviorStepModelPart.
	 * 
	 * @return Model of this as a BehaviorStepModelPart.
	 */
	protected BehaviorStepModelPart getStartFlowlet() {
		return (BehaviorStepModelPart) getModel();
	}

}
