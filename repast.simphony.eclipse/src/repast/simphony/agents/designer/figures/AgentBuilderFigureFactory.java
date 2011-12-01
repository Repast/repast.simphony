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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Color;

import repast.simphony.agents.designer.model.DecisionStepModelPart;
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
public class AgentBuilderFigureFactory {

	/**
	 * Returns a new instance of BehaviorStepFigure.
	 * 
	 * @return a new instance of BehaviorStepFigure.
	 */
	public static IFigure createStartFlowletFigure() {
		return new BehaviorStepFigure();
	}

	/**
	 * Returns a new instance of TaskStepFigure.
	 * 
	 * @return a new instance of TaskStepFigure.
	 */
	public static IFigure createTaskFlowletFigure() {
		return new TaskStepFigure();
	}

	/**
	 * Returns a new instance of DecisionStepFigure.
	 * 
	 * @return a new instance of DecisionStepFigure.
	 */
	public static IFigure createDecisionFlowletFigure(
			DecisionStepModelPart parentModelPart) {
		return new DecisionStepFigure(parentModelPart);
	}

	/**
	 * Returns a new instance of JoinStepFigure.
	 * 
	 * @return a new instance of JoinStepFigure.
	 */
	public static IFigure createJoinFlowletFigure() {
		return new JoinStepFigure();
	}

	/**
	 * Returns a new instance of AgentPropertyFigure.
	 * 
	 * @return a new instance of AgentPropertyFigure.
	 */
	public static IFigure createPropertyFlowletFigure() {
		return new AgentPropertyFigure();
	}

	/**
	 * Returns a new instance of EndStepFigure.
	 * 
	 * @return a new instance of EndStepFigure.
	 */
	public static IFigure createEndFlowletFigure() {
		return new EndStepFigure();
	}

	/**
	 * TODO
	 * 
	 * @param text
	 * @return
	 */
	public static IFigure createFlowletLabelFigure(String text) {
		return new PropertyOrStepLabelFigure(text);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public static IFigure createFlowletLabelFigure() {
		return new PropertyOrStepLabelFigure();
	}

	/**
	 * TODO
	 * 
	 * @param transition
	 * @return
	 */
	public static PolylineConnection createTransition(
			TransitionModelPart transition) {
		return new PolylineConnection();
	}

	/**
	 * TODO
	 * 
	 * @param transition
	 * @return
	 */
	public static PolylineConnection createBendableTransition(
			TransitionModelPart transition) {
		PolylineConnection conn = new PolylineConnection();
		PolygonDecoration arrow;

		if (transition == null)// || transition.getTarget() instanceof
			// SimpleOutput)
			arrow = null;
		else {
			arrow = new PolygonDecoration();
			arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
			arrow.setScale(5, 2.5);
			arrow.setForegroundColor(new Color(null, 0, 0, 0));
			conn.setForegroundColor(new Color(null, 0, 0, 0));
		}
		conn.setTargetDecoration(arrow);
		return conn;
	}

}
