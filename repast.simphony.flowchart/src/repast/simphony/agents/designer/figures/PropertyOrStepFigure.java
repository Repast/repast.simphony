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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.Color;

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
abstract public class PropertyOrStepFigure extends AgentBuilderFigure implements
		HandleBounds {

	static private final Color FLOWLET_FILL_COLOR = new Color(null, 193, 213,
			255);
	static private final Color FLOWLET_BORDER_COLOR = new Color(null, 105, 140,
			255);

	private List sourceConnectionAnchors = new ArrayList();
	private List targetConnectionAnchors = new ArrayList();

	/**
	 * TODO
	 * 
	 */
	public PropertyOrStepFigure() {
		super();
		createConnectionAnchors(AnchorRegistry
				.getSourceAnchorOffsets(getType()),
				getSourceConnectionAnchors());
		createConnectionAnchors(AnchorRegistry
				.getTargetAnchorOffsets(getType()),
				getTargetConnectionAnchors());
	}

	/**
	 * Creates ConnectionAnchor objects and adds them to their List.
	 * 
	 * @param anchorOffsets
	 * @param connectionAnchors
	 */
	private void createConnectionAnchors(List anchorOffsets,
			List connectionAnchors) {
		for (Iterator iter = anchorOffsets.iterator(); iter.hasNext();) {
			repast.simphony.agents.base.Point anchorOffset = (repast.simphony.agents.base.Point) iter
					.next();
			PropertyOrStepConnectionAnchor anchor = new PropertyOrStepConnectionAnchor(
					this);
			anchor.setOffset(anchorOffset.x, anchorOffset.y);
			connectionAnchors.add(anchor);
		}
	}

	/**
	 * Paints the flowlets shape. fills the polygon and draws the outline.
	 * 
	 * @param g
	 * @param pointList
	 */
	protected void paintFlowletFigure(Graphics g, PointList pointList) {

		// Color foregroundColor = g.getForegroundColor();
		// Color backgroundColor = g.getBackgroundColor();
		// int antiAlias = g.getAntialias();
		// g.setAntialias(SWT.ON);

		g.setBackgroundColor(FLOWLET_FILL_COLOR);
		g.fillPolygon(pointList);

		g.setLineWidth(2);
		g.setForegroundColor(FLOWLET_BORDER_COLOR);
		g.drawPolygon(pointList);
		g.setForegroundColor(FLOWLET_BORDER_COLOR);

		// g.setAntialias(antiAlias);
		// g.setAntialias(SWT.OFF);
		// g.setBackgroundColor(backgroundColor);
		// g.setForegroundColor(foregroundColor);

	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List getSourceConnectionAnchors() {
		return sourceConnectionAnchors;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List getTargetConnectionAnchors() {
		return targetConnectionAnchors;
	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @return
	 */
	public ConnectionAnchor getSourceConnectionAnchor(int index) {
		return (ConnectionAnchor) sourceConnectionAnchors.get(index);
	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @return
	 */
	public ConnectionAnchor getTargetConnectionAnchor(int index) {
		return (ConnectionAnchor) targetConnectionAnchors.get(index);
	}

	/**
	 * TODO
	 * 
	 * @param anchor
	 * @return
	 */
	public int getSourceConnectionAnchorIndex(ConnectionAnchor anchor) {
		return sourceConnectionAnchors.indexOf(anchor);
	}

	/**
	 * TODO
	 * 
	 * @param anchor
	 * @return
	 */
	public int getTargetConnectionAnchorIndex(ConnectionAnchor anchor) {
		return targetConnectionAnchors.indexOf(anchor);
	}

	/**
	 * TODO
	 * 
	 * @param p
	 * @return
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		return getClosestConnectionAnchorAt(p, getSourceConnectionAnchors());
	}

	/**
	 * TODO
	 * 
	 * @param p
	 * @return
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		return getClosestConnectionAnchorAt(p, getTargetConnectionAnchors());
	}

	/**
	 * Returns the Anchor from the given List of anchors that is closest to the
	 * given Point.
	 * 
	 * @param p
	 *            the point
	 * @param connectionAnchors
	 *            list of possible anchors
	 * @return the anchor thats closest to the given Point.
	 */
	static public ConnectionAnchor getClosestConnectionAnchorAt(Point p,
			List connectionAnchors) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		for (Iterator iter = connectionAnchors.iterator(); iter.hasNext();) {
			ConnectionAnchor c = (ConnectionAnchor) iter.next();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}

		return closest;
	}

	/**
	 * Returns the flowlet type.
	 * 
	 * @return the flowlet type
	 */
	abstract public String getType();

}
