/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif�s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North�s Modifications are Copyright 2007 Under
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

package repast.simphony.agents.designer.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import repast.simphony.agents.designer.model.TransitionModelPart;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public abstract class BendpointCommand extends Command {

	protected int index;
	protected Point location;
	protected TransitionModelPart transition;
	private Dimension d1, d2;

	/**
	 * TODO
	 * 
	 */
	public BendpointCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected Dimension getFirstRelativeDimension() {
		return d1;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected Dimension getSecondRelativeDimension() {
		return d2;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected int getIndex() {
		return index;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected Point getLocation() {
		return location;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	protected TransitionModelPart getTransition() {
		return transition;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		execute();
	}

	/**
	 * TODO
	 * 
	 * @param dim1
	 * @param dim2
	 */
	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	/**
	 * TODO
	 * 
	 * @param i
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * TODO
	 * 
	 * @param p
	 */
	public void setLocation(Point p) {
		location = p;
	}

	/**
	 * TODO
	 * 
	 * @param w
	 */
	public void setTransition(TransitionModelPart t) {
		transition = t;
	}

}