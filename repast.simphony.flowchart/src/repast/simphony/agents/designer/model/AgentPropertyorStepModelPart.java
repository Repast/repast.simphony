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
abstract public class AgentPropertyorStepModelPart extends
		AgentDiagramElementModelPart {

	public static String PROP_SOURCE_CONNECTION = "source_connection"; //$NON-NLS-1$
	public static String PROP_TARGET_CONNECTION = "target_connection"; //$NON-NLS-1$

	private List targetTransitions = new ArrayList();
	private List sourceTransitions = new ArrayList();

	/**
	 * TODO
	 * 
	 */
	public AgentPropertyorStepModelPart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List getSourceTransitions() {
		return sourceTransitions;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List getTargetTransitions() {
		return targetTransitions;
	}

	/**
	 * Connets the source transition.
	 * 
	 * @param t
	 *            the transition
	 */
	public void connectSource(TransitionModelPart t) {
		sourceTransitions.add(t);
		// update();
		fireStructureChange(PROP_SOURCE_CONNECTION, t);
	}

	/**
	 * Connets the target transition.
	 * 
	 * @param t
	 *            the transition
	 */
	public void connectTarget(TransitionModelPart t) {
		targetTransitions.add(t);
		// update();
		fireStructureChange(PROP_TARGET_CONNECTION, t);
	}

	/**
	 * Disconnets the source transition.
	 * 
	 * @param t
	 *            the transition
	 */
	public void disconnectSource(TransitionModelPart t) {
		sourceTransitions.remove(t);
		// update();
		fireStructureChange(PROP_SOURCE_CONNECTION, t);
	}

	/**
	 * Disconnets the target transition.
	 * 
	 * @param t
	 *            the transition
	 */
	public void disconnectTarget(TransitionModelPart t) {
		targetTransitions.remove(t);
		// update();
		fireStructureChange(PROP_TARGET_CONNECTION, t);
	}

	/**
	 * Returns the flowlet type.
	 * 
	 * @return the flowlet type
	 */
	abstract public String getType();
}
