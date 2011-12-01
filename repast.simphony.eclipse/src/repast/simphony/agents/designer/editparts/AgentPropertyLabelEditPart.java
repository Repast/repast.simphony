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

package repast.simphony.agents.designer.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Label;

import repast.simphony.agents.designer.figures.AgentPropertyToolTipFigure;
import repast.simphony.agents.designer.figures.PropertyOrStepLabelFigure;
import repast.simphony.agents.designer.model.AgentPropertyModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepLabelModelPart;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
public class AgentPropertyLabelEditPart extends
		AgentPropertyorStepLabelEditPart {

	/**
	 * Sets the figure's display text to the decision's description if it is
	 * set. otherwise to the decision statement itself.
	 * 
	 * @param propertyOrStepLabelFigure
	 *            the figure.
	 * @see repast.simphony.agents.designer.editparts.AgentPropertyorStepLabelEditPart#adjustFlowletLabelFigure(repast.simphony.agents.designer.figures.PropertyOrStepLabelFigure)
	 */
	@Override
	protected void adjustFlowletLabelFigure(
			PropertyOrStepLabelFigure propertyOrStepLabelFigure) {
		AgentPropertyorStepLabelModelPart labelModelPart = (AgentPropertyorStepLabelModelPart) getModel();
		String labelText = labelModelPart.getText();

		AgentPropertyModelPart agentProperty = (AgentPropertyModelPart) labelModelPart
				.getFlowletWithlabel();
		if (agentProperty.hasValidLabel())
			labelText = agentProperty.getLabel();

		// set the figure's text
		propertyOrStepLabelFigure.setText(labelText);
	}

	/**
	 * Reacts on property change of the modelPart. Sets the label's text only if
	 * it has no valid description.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String propName = event.getPropertyName();
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(propName)) {
			String templateStr = (String) event.getNewValue();
			adjustFlowletLabelFigure((PropertyOrStepLabelFigure) getFigure());
			AgentPropertyorStepLabelModelPart labelModelPart = (AgentPropertyorStepLabelModelPart) getModel();
			AgentPropertyModelPart template = (AgentPropertyModelPart) labelModelPart
					.getFlowletWithlabel();
			if (!template.hasValidLabel())
				((PropertyOrStepLabelFigure) getFigure()).setText(templateStr);
			// update DecisionStepEditPart's tooltip
			AgentPropertyEditPart agentPropertyEditPart = (AgentPropertyEditPart) getFlowletWithLabelEditPart();
			IAgentBuilderToolTip toolTip = agentPropertyEditPart
					.getFlowDiagramElementToolTip();
			toolTip.update(AgentPropertyToolTipFigure.TOOLTIP_PROP_TEMPLATE,
					templateStr);
		} else
			super.propertyChange(event);
	}

	/**
	 * Returns the FlowletLabelEditManager for this type of EditPart. Sets the
	 * <code>forceDirty</code> flag to <code>true</code> because the edit
	 * text differs from the display text.
	 * 
	 * @return the FlowletLabelEditManager for this type of EditPart.
	 * @see repast.simphony.agents.designer.editparts.AgentPropertyorStepLabelEditPart#getFlowletLabelEditManager()
	 */
	@Override
	protected FlowletLabelEditManager getFlowletLabelEditManager() {
		return new AgentPropertyorStepLabelEditPart.FlowletLabelEditManager(
				this, new LabelCellEditorLocator((Label) getFigure()), true);
	}

}
