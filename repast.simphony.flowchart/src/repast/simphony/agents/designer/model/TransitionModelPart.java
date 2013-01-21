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

package repast.simphony.agents.designer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.Bendpoint;

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
public class TransitionModelPart extends AgentModelPart {

	public static String PROP_SOURCE_FLOWLET = "source_flowlet"; //$NON-NLS-1$
	public static String PROP_TARGET_FLOWLET = "target_flowlet"; //$NON-NLS-1$

	public static String PROP_SOURCE_ANCHORINDEX = "source_anchorindex"; //$NON-NLS-1$
	public static String PROP_TARGET_ANCHORINDEX = "target_anchorindex"; //$NON-NLS-1$

	public static String PROP_BENDPOINT = "bendpoint"; //$NON-NLS-1$

	private AgentPropertyorStepModelPart source;
	private AgentPropertyorStepModelPart target;
	private int sourceAnchorIndex;
	private int targetAnchorIndex;
	private List bendpoints = new ArrayList();

	/**
	 * TODO
	 * 
	 */
	public TransitionModelPart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public AgentPropertyorStepModelPart getSource() {
		return source;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public AgentPropertyorStepModelPart getTarget() {
		return target;
	}

	/**
	 * TODO
	 * 
	 * @param source
	 */
	public void setSource(AgentPropertyorStepModelPart f) {
		Object old = source;
		source = f;
		firePropertyChange(PROP_SOURCE_FLOWLET, old, source);
	}

	/**
	 * TODO
	 * 
	 * @param target
	 */
	public void setTarget(AgentPropertyorStepModelPart f) {
		// Object old = target;
		target = f;
		// firePropertyChange(PROP_TARGET_FLOWLET, old, target);//$NON-NLS-1$
	}

	/**
	 * TODO
	 * 
	 */
	public void attachSource() {
		if (getSource() == null
				|| getSource().getSourceTransitions().contains(this))
			return;
		getSource().connectSource(this);
	}

	/**
	 * TODO
	 * 
	 */
	public void attachTarget() {
		if (getTarget() == null
				|| getTarget().getTargetTransitions().contains(this))
			return;
		getTarget().connectTarget(this);
	}

	/**
	 * TODO
	 * 
	 */
	public void detachSource() {
		if (getSource() == null)
			return;
		getSource().disconnectSource(this);
	}

	/**
	 * TODO
	 * 
	 */
	public void detachTarget() {
		if (getTarget() == null)
			return;
		getTarget().disconnectTarget(this);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public int getSourceAnchorIndex() {
		return sourceAnchorIndex;
	}

	/**
	 * TODO
	 * 
	 * @param sourceAnchorIndex
	 */
	public void setSourceAnchorIndex(int index) {
		int old = sourceAnchorIndex;
		sourceAnchorIndex = index;
		firePropertyChange(PROP_SOURCE_ANCHORINDEX, new Integer(old),
				new Integer(sourceAnchorIndex));
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public int getTargetAnchorIndex() {
		return targetAnchorIndex;
	}

	/**
	 * TODO
	 * 
	 * @param targetAnchorIndex
	 */
	public void setTargetAnchorIndex(int index) {
		// int old = targetAnchorIndex;
		targetAnchorIndex = index;
		// firePropertyChange(PROP_TARGET_ANCHORINDEX, new Integer(old), new
		// Integer(targetAnchorIndex));//$NON-NLS-1$
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List getBendpoints() {
		return bendpoints;
	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @param point
	 */
	public void insertBendpoint(int index, Bendpoint point) {
		getBendpoints().add(index, point);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	/**
	 * TODO
	 * 
	 * @param index
	 */
	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @param point
	 */
	public void setBendpoint(int index, Bendpoint point) {
		getBendpoints().set(index, point);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	/**
	 * TODO
	 * 
	 * @param points
	 */
	public void setBendpoints(Vector points) {
		bendpoints = points;
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

}
