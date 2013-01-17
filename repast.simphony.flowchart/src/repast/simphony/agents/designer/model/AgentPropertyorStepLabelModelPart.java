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

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;

/**
 * Model Part of a Flowlet label.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
@SuppressWarnings("unchecked")
public class AgentPropertyorStepLabelModelPart extends
		AgentDiagramElementModelPart {

	public static final String PROP_LABEL = "label"; //$NON-NLS-1$

	private String text;
	private AgentPropertyorStepWithLabelModelPart flowletWithlabel;
	private HashSet flowletLabelPropertyDescriptors = null;

	/**
	 * Default Constructor
	 */
	public AgentPropertyorStepLabelModelPart() {
		super();
	}

	/**
	 * Constructor that also sets the flowlet's initial text.
	 * 
	 * @text the labels initial text
	 */
	public AgentPropertyorStepLabelModelPart(String text) {
		super();
		this.text = text;
	}

	/**
	 * Returns the label's text
	 * 
	 * @return the label's text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the labels text.
	 * 
	 * @param text
	 *            the label's new text
	 */
	public void setText(String text) {
		this.text = text;
		firePropertyChange(PROP_LABEL, null, text);
	}

	/**
	 * Sets the corresponding flowletWithLabel ModelPart
	 * 
	 * @param flowletWithLabel
	 */
	public void setFlowletWithlabel(
			AgentPropertyorStepWithLabelModelPart flowletWithLabel) {
		flowletWithlabel = flowletWithLabel;
	}

	/**
	 * Returns the name of the resource entry that contains the property name.
	 * This metzhod returns the default key, subclasses can specify a more
	 * specific key.
	 * 
	 * @return key in the resources file
	 */
	protected String getLabelPropertyNameResource() {
		return flowletWithlabel.getLabelPropertyNameResource();
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * TextPropertyDescriptor
	 */
	private void createPropertyDescriptors() {
		if (flowletLabelPropertyDescriptors != null)
			return;

		flowletLabelPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());
		flowletLabelPropertyDescriptors.add(new TextPropertyDescriptor(
				PROP_LABEL, AgentBuilderPlugin
						.getResourceString(getLabelPropertyNameResource())));
	}

	/**
	 * Returns the collection of the property descriptors
	 * 
	 * @return the collection of the property descriptors
	 */
	@Override
	protected Collection getPropertyDescriptorsCollection() {
		if (flowletLabelPropertyDescriptors == null)
			createPropertyDescriptors();

		return flowletLabelPropertyDescriptors;
	}

	/**
	 * Returns an Object which represents the appropriate value for the property
	 * name supplied.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 * @param propName
	 *            Name of the property for which the the values are needed.
	 * @return Object which is the value of the property.
	 */
	@Override
	public Object getPropertyValue(Object propName) {
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(propName))
			return getText();
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
			setText((String) value);
		else
			super.setPropertyValue(id, value);
	}

	/**
	 * Returns the model part
	 * 
	 * @return the model part
	 */
	public AgentPropertyorStepWithLabelModelPart getFlowletWithlabel() {
		return flowletWithlabel;
	}

}
