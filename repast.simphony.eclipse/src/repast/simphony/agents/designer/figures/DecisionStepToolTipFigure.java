/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
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

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;

import repast.simphony.agents.designer.editparts.DecisionStepEditPart;
import repast.simphony.agents.designer.editparts.IAgentBuilderToolTip;
import repast.simphony.agents.designer.model.DecisionStepModelPart;

/**
 * The Decision Flowlet's tooltip figure which presents the flowlet's if
 * statement
 * 
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
public class DecisionStepToolTipFigure extends Panel implements
		IAgentBuilderToolTip {

	static final public String TOOLTIP_PROP_STATEMENT = "tooltip_DecisionStep_statement";

	private DecisionStepEditPart decisionStepEditPart;
	private Label statementLabel = new Label();

	/**
	 * Constructor. Creates all the panels and sets the layout
	 */
	public DecisionStepToolTipFigure(DecisionStepEditPart decisionStepEditPart) {
		super();
		this.decisionStepEditPart = decisionStepEditPart;

		Panel statementPanel = new Panel();
		statementPanel.add(statementLabel);
		FlowLayout lay1 = new FlowLayout(false);
		lay1.setMinorSpacing(0);
		statementPanel.setLayoutManager(lay1);
		add(statementPanel);

		FlowLayout lay3 = new FlowLayout(false);
		lay3.setMinorSpacing(0);
		setLayoutManager(lay3);
	}

	/*
	 * Updates the tooltips specified part.
	 * 
	 * @see repast.simphony.agents.designer.editparts.IAgentBuilderToolTip#update(java.lang.String,
	 *      java.lang.Object)
	 */
	public void update(String key, Object value) {
		if (TOOLTIP_PROP_STATEMENT.equals(key)) {
			String statement = (String) value;
			statementLabel.setText(statement);
		}
	}

	/**
	 * Updates the tooltip's contents and repaints the figure.
	 * 
	 * @see org.eclipse.draw2d.IFigure#repaint()
	 */
	@Override
	public void repaint() {
		updateContents();
		super.repaint();
	}

	/**
	 * Updates the tollbox's contents.
	 * 
	 */
	private void updateContents() {
		DecisionStepModelPart decisionStepModelPart = (DecisionStepModelPart) decisionStepEditPart
				.getModel();

		// update statement
		updateStatement(decisionStepModelPart);
	}

	/**
	 * Updates the statement
	 * 
	 * @param decisionStepModelPart
	 */
	private void updateStatement(DecisionStepModelPart decisionStepModelPart) {
		statementLabel.setText(decisionStepModelPart.getFlowletLabel()
				.getText());
	}

}
