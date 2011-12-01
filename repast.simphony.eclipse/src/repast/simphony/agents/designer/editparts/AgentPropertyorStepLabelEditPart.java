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
import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import repast.simphony.agents.designer.editparts.policies.AgentPropertyorStepLabelDirectEditPolicy;
import repast.simphony.agents.designer.figures.AgentBuilderFigureFactory;
import repast.simphony.agents.designer.figures.PropertyOrStepLabelFigure;
import repast.simphony.agents.designer.model.AgentPropertyorStepLabelModelPart;
import repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart;

/**
 * The flowlet label edit part.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
@SuppressWarnings("unchecked")
public class AgentPropertyorStepLabelEditPart extends
		AgentDiagramElementEditPart {

	private DirectEditManager manager;

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// should not be deletable on its own
		removeEditPolicy(EditPolicy.COMPONENT_ROLE);
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new AgentPropertyorStepLabelDirectEditPolicy());
	}

	/**
	 * Creates the flowlet figure with the model's text.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		if (getModel() == null)
			return null;

		// return AgentBuilderFigureFactory.createFlowletLabelFigure(
		// ((AgentPropertyorStepLabelModelPart) getModel()).getText());

		PropertyOrStepLabelFigure propertyOrStepLabelFigure = (PropertyOrStepLabelFigure) AgentBuilderFigureFactory
				.createFlowletLabelFigure(((AgentPropertyorStepLabelModelPart) getModel())
						.getText());
		adjustFlowletLabelFigure(propertyOrStepLabelFigure);
		return propertyOrStepLabelFigure;
	}

	protected void adjustFlowletLabelFigure(
			PropertyOrStepLabelFigure propertyOrStepLabelFigure) {
	}

	/**
	 * @see repast.simphony.agents.designer.editparts.AgentDiagramElementEditPart#hasProperties()
	 */
	@Override
	public boolean hasProperties() {
		return true;
	}

	/**
	 * Returns the corresponding
	 * <code>AgentPropertyorStepWithLabelEditPart</code>. Scans through all
	 * editParts and returns one that's model is the same as the flowlet of this
	 * instance. Returns <code>null</code> if not found.
	 * 
	 * @return the AgentPropertyorStepWithLabelEditPart of this instance
	 */
	protected AgentPropertyorStepWithLabelEditPart getFlowletWithLabelEditPart() {
		for (Iterator editPartsIter = getParent().getChildren().iterator(); editPartsIter
				.hasNext();) {
			AgentDiagramElementEditPart editPart = (AgentDiagramElementEditPart) editPartsIter
					.next();
			if (editPart instanceof AgentPropertyorStepWithLabelEditPart) {
				AgentPropertyorStepWithLabelEditPart fwlep = (AgentPropertyorStepWithLabelEditPart) editPart;
				if (((AgentPropertyorStepWithLabelModelPart) fwlep.getModel())
						.getFlowletLabel() == getModel()) {
					return fwlep;
				}
			}
		}

		return null;
	}

	/**
	 * Performs a direct edit on the Flowlet Label.
	 */
	private void performDirectEdit() {
		if (manager == null)
			manager = getFlowletLabelEditManager();

		manager.show();
	}

	/**
	 * Returns the FlowletLabelEditManager for this type of EditPart. By
	 * defaualt the <code>forceDirty</code> flag is set to false.
	 * 
	 * @return the FlowletLabelEditManager for tihis type of EditPart.
	 */
	protected FlowletLabelEditManager getFlowletLabelEditManager() {
		return new FlowletLabelEditManager(this, new LabelCellEditorLocator(
				(Label) getFigure()), false);
	}

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	/**
	 * Reacts on property change of the modelPart. Sets the figures text if the
	 * label's property was changed.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String propName = event.getPropertyName();
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(propName))
			((PropertyOrStepLabelFigure) getFigure()).setText((String) event
					.getNewValue());
		else
			super.propertyChange(event);
	}

	/**
	 * Returns the model of this as a FlowDiagramModelPart.
	 * 
	 * @return FlowDiagramModelPart of this.
	 */
	protected AgentPropertyorStepLabelModelPart getFlowletLabel() {
		return (AgentPropertyorStepLabelModelPart) getModel();
	}

	/**
	 * Relocates the flowlet label's editor
	 * 
	 * @author alex
	 */
	protected class LabelCellEditorLocator implements CellEditorLocator {

		private Label label;

		public LabelCellEditorLocator(Label label) {
			setLabel(label);
		}

		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Point pref = text.computeSize(-1, -1);
			Rectangle rect = label.getTextBounds().getCopy();
			label.translateToAbsolute(rect);
			text.setBounds(rect.x - 4, rect.y - 1, pref.x + 1, pref.y + 1);
		}

		protected Label getLabel() {
			return label;
		}

		protected void setLabel(Label label) {
			this.label = label;
		}

	}

	/**
	 * The direct edit manager for the flowlet's label
	 * 
	 * @author alex
	 */
	protected class FlowletLabelEditManager extends DirectEditManager {

		/**
		 * If set to <code>true</code>, then the sellEditor's value will
		 * always be reset even when the vlaue was not changed by the user. This
		 * is necessary in the case when display text and edit text differ.
		 */
		private boolean forceDirty;
		private LabelCellEditorLocator locator;

		/**
		 * Contructor
		 * 
		 * @param source
		 * @param locator
		 * @param forceDirty
		 */
		public FlowletLabelEditManager(GraphicalEditPart source,
				LabelCellEditorLocator locator, boolean forceDirty) {
			super(source, TextCellEditor.class, locator);
			this.forceDirty = forceDirty;
			this.locator = locator;
		}

		/**
		 * Initializes the Cell Editor. Sets the editable Text which is
		 * accesible from the label Model Part
		 */
		@Override
		protected void initCellEditor() {
			Text text = (Text) getCellEditor().getControl();
			AgentPropertyorStepLabelEditPart labelEditPart = (AgentPropertyorStepLabelEditPart) getEditPart();

			// set the text which is accesible from the label
			AgentPropertyorStepLabelModelPart model = (AgentPropertyorStepLabelModelPart) labelEditPart
					.getModel();
			getCellEditor().setValue(model.getText());
			text.selectAll();
		}

		/**
		 * Sets the flowlets name if the classname did not change.
		 * 
		 * @see org.eclipse.gef.tools.DirectEditManager#commit()
		 */
		@Override
		protected void commit() {
			if (forceDirty && !isDirty()) {
				getCellEditor().getControl();
				// set to the original Text
				getCellEditor().setValue(locator.getLabel().getText());
				setDirty(true);
			}
			super.commit();
		}

	}

}
