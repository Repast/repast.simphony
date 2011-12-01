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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.model.codegen.DepthCounter;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * Model part of a flowlet that has a label property.
 */
@SuppressWarnings("unchecked")
abstract public class AgentPropertyorStepWithLabelModelPart extends
		AgentPropertyorStepModelPart {

	protected AgentPropertyorStepLabelModelPart propertyOrStepLabel;
	private Set propertyOrStepWithLabelPropertyDescriptors = null;

	/**
	 * Returns the flowlet's label model.
	 * 
	 * @return the flowlet's label model.
	 */
	public AgentPropertyorStepLabelModelPart getFlowletLabel() {
		return propertyOrStepLabel;
	}

	public String getLabel() {
		if (this.hasValidLabel())
			return this.propertyOrStepLabel.getText();
		else
			return "";
	}

	public void setLabel(String newLabel) {
		if (this.propertyOrStepLabel != null)
			this.propertyOrStepLabel.setText(newLabel);
	}

	/**
	 * Sets the flowlet's label.
	 * 
	 * @param propertyOrStepLabel
	 */
	public void setFlowletLabel(AgentPropertyorStepLabelModelPart flowletLabel) {
		this.propertyOrStepLabel = flowletLabel;
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * TextPropertyDescriptor for the flowlet's label
	 */
	private void createPropertyDescriptors() {
		if (propertyOrStepWithLabelPropertyDescriptors != null)
			return;

		propertyOrStepWithLabelPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());
		propertyOrStepWithLabelPropertyDescriptors
				.add(new TextPropertyDescriptor(
						AgentPropertyorStepLabelModelPart.PROP_LABEL,
						AgentBuilderPlugin
								.getResourceString(getLabelPropertyNameResource())));
	}

	/**
	 * Returns whether the property has a valid label. This means that it is not
	 * <code>null</code> and length is not zero.
	 * 
	 * @return <code>true</code> if the property has a valid label.
	 */
	public boolean hasValidLabel() {
		return ((this.propertyOrStepLabel != null) && DepthCounter
				.isSet(this.propertyOrStepLabel.getText()));
	}

	/**
	 * Returns the name of the resource entry that contains the property name.
	 * This metzhod returns the default key, subclasses can specify a more
	 * specific key.
	 * 
	 * @return key in the resources file
	 */
	protected String getLabelPropertyNameResource() {
		return "PropertyDescriptor_PropertyOrStep.label";
	}

	/**
	 * Collects all property descriptors. Those of the superclass and those of
	 * this class
	 * 
	 * @return Collection with all Property descriptors
	 */
	@Override
	protected Collection getPropertyDescriptorsCollection() {
		if (propertyOrStepWithLabelPropertyDescriptors == null)
			createPropertyDescriptors();

		return propertyOrStepWithLabelPropertyDescriptors;
	}

	/**
	 * Returns an Object which represents the appropriate value for the property
	 * name supplied. In case of label property the request will be routed to
	 * the label modelPart.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 * @param propName
	 *            Name of the property for which the the values are needed.
	 * @return Object which is the value of the property.
	 */
	@Override
	public Object getPropertyValue(Object propName) {
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(propName))
			// return propertyOrStepLabel.getText();
			return propertyOrStepLabel.getPropertyValue(propName);
		else
			return super.getPropertyValue(propName);
	}

	/**
	 * Sets the value of a given property with the value supplied. Also fires a
	 * property change if necessary.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 * @param id
	 *            Name of the parameter to be changed.
	 * @param value
	 *            Value to be set to the given parameter.
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(id))
			propertyOrStepLabel.setText((String) value);
		else
			super.setPropertyValue(id, value);
	}

}
