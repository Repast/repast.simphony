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

package repast.simphony.agents.designer.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.base.FigureConsts;

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
public class TaskStepFigure extends PropertyOrStepFigure {

	private Rectangle BOUNDS = new Rectangle(0, 0,
			FigureConsts.DIMENSION_FIGURE_JAVATASK.width,
			FigureConsts.DIMENSION_FIGURE_JAVATASK.height);

	/**
	 * Constructs a task flowlet figure
	 * 
	 */
	public TaskStepFigure() {
		super();
		getBounds().width = BOUNDS.width;
		getBounds().height = BOUNDS.height;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	@Override
	protected void paintFigure(Graphics g) {
		Rectangle drawRect = getBounds().getCropped(new Insets(1));
		PointList pointList = new PointList();
		pointList.addPoint(drawRect.getTopLeft());
		pointList.addPoint(drawRect.getBottomLeft());
		pointList.addPoint(drawRect.getBottomRight());
		pointList.addPoint(drawRect.getTopRight());

		paintFlowletFigure(g, pointList);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(FigureConsts.DIMENSION_FIGURE_JAVATASK.width,
				FigureConsts.DIMENSION_FIGURE_JAVATASK.height);
	}

	/**
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		return getBounds();
	}

	/**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
	@Override
	public String getType() {
		return AgentBuilderConsts.TASK;
	}

}
