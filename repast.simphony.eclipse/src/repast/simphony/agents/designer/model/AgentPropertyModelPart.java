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

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;

/**
 * The Model Part for the Template Flowlet.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class AgentPropertyModelPart extends
		AgentPropertyorStepWithLabelModelPart {

	public static final String PROP_AGENT_PROPERTY_COMMENT = "agent_property_description"; //$NON-NLS-1$
	public static final String PROP_AGENT_PROPERTY_COMPILED_NAME = "agent_property_compiled_name"; //$NON-NLS-1$
	public static final String PROP_AGENT_PROPERTY_TYPE = "agent_property_type"; //$NON-NLS-1$
	public static final String PROP_AGENT_PROPERTY_DEFAULT_VALUE = "agent_property_default_value"; //$NON-NLS-1$
	private static final String PROP_AGENT_PROPERTY_VISIBILITY = "agent_property_visibility";

	private HashSet agentPropertyPropertyDescriptors = null;
	private String propertyComment = "This is an agent property.";
	private String propertyCompiledName = "";
	private String propertyType = "def";
	private String propertyDefaultValue = "0";
	private static String[] typeList = { "def", "boolean", "int", "long",
			"float", "double", "String", "boolean[]", "int[]", "long[]",
			"float[]", "double[]", "String[]", "Amount", "Matrix", "Object",
			"Multilinear Regression Model", "Best Fit Regression Model",
			"Linear Neural Net", "Logistic Neural Net", "Softmax Neural Net",
			"Genetic Algorithm", "Systems Dynamics Equation",
			EditableComboBoxPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR };

	private Integer visibility = new Integer(0);
	private static String[] visibilityList = { "Everyone", "Only Me",
			"Only My Children", "Only My Friends",
			"Everyone (Without Creating an Agent)",
			"Only Me (Without Creating an Agent)",
			"Only My Children (Without Creating an Agent)",
			"Only My Friends (Without Creating an Agent)" };

	/**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
	@Override
	public String getType() {
		return AgentBuilderConsts.PROPERTY;
	}

	/**
	 * Returns the name of the resource entry that contains the property name
	 * 
	 * @return key in the resources file
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart#getLabelPropertyNameResource()
	 */
	@Override
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
		if (agentPropertyPropertyDescriptors == null)
			createPropertyDescriptors();

		return agentPropertyPropertyDescriptors;
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * ReadOnlyTextPropertyDescriptor for the task's propertyComment
	 */
	private void createPropertyDescriptors() {
		if (agentPropertyPropertyDescriptors != null)
			return;

		agentPropertyPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());

		agentPropertyPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_AGENT_PROPERTY_COMMENT,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentProperty.comment")));
		agentPropertyPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_AGENT_PROPERTY_COMPILED_NAME,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentProperty.compiledName")));
		agentPropertyPropertyDescriptors
				.add(new ExtendedComboBoxPropertyDescriptor(
						PROP_AGENT_PROPERTY_VISIBILITY,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentProperty.visibility"),
						visibilityList));
		agentPropertyPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_AGENT_PROPERTY_TYPE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentProperty.type"),
						typeList, null));
		agentPropertyPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_AGENT_PROPERTY_DEFAULT_VALUE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentProperty.defaultValue")));

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
	public Object getPropertyValue(Object id) {
		if (PROP_AGENT_PROPERTY_COMMENT.equals(id))
			return getPropertyDescription();
		if (PROP_AGENT_PROPERTY_COMPILED_NAME.equals(id))
			return getPropertyCompiledName();
		if (PROP_AGENT_PROPERTY_TYPE.equals(id))
			return getPropertyType();
		if (PROP_AGENT_PROPERTY_DEFAULT_VALUE.equals(id))
			return getPropertyDefaultValue();
		if (PROP_AGENT_PROPERTY_VISIBILITY.equals(id))
			return getVisibility();
		else
			return super.getPropertyValue(id);
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
		if (PROP_AGENT_PROPERTY_COMMENT.equals(id)) {
			setPropertyDescription((String) value);
		} else if (PROP_AGENT_PROPERTY_COMPILED_NAME.equals(id)) {
			setPropertyCompiledName((String) value);
		} else if (PROP_AGENT_PROPERTY_TYPE.equals(id)) {
			String stringValue = (String) value;
			if (stringValue.endsWith("[]")) {
				setPropertyType(stringValue);
				setPropertyDefaultValue("new " + stringValue.substring(0, stringValue.length() - 1) + "size]");
			} else if (stringValue.equals("String")) {
				setPropertyType("String");
				setPropertyDefaultValue("\"\"");
			} else if (stringValue.equals("Multilinear Regression Model")) {
				setPropertyType("RepastRegressionModel");
				setPropertyDefaultValue("new RepastRegressionModel(false)");
			} else if (stringValue.equals("Best Fit Regression Model")) {
				setPropertyType("RepastRegressionModel");
				setPropertyDefaultValue("new RepastRegressionModel(true)");
			} else if (stringValue.equals("Linear Neural Net")) {
				setPropertyType("NeuralNet");
				setPropertyDefaultValue("JooneTools.create_standard(new int[]{inputNeurons intermediateLayer1 intermediateLayer2 intermediateLayerN outputNeurons}, JooneTools.LINEAR)");
			} else if (stringValue.equals("Logistic Neural Net")) {
				setPropertyType("NeuralNet");
				setPropertyDefaultValue("JooneTools.create_standard(new int[]{inputNeurons intermediateLayer1 intermediateLayer2 intermediateLayerN outputNeurons}, JooneTools.LOGISTIC)");
			} else if (stringValue.equals("Softmax Neural Net")) {
				setPropertyType("NeuralNet");
				setPropertyDefaultValue("JooneTools.create_standard(new int[]{inputNeurons intermediateLayer1 intermediateLayer2 intermediateLayerN outputNeurons}, JooneTools.SOFTMAX)");
			} else if (stringValue.equals("Genetic Algorithm")) {
				setPropertyType("RepastGA");
				setPropertyDefaultValue("new RepastGA(this, \"evaluate\", populationSize, new Gene[]{ new IntegerGene(min1, max1) new DoubleGene(min2, max2)}) // Remember to define \"int evaluate(double[numberOfGenes])\"");
			} else if (stringValue.equals("Systems Dynamics Equation")) {
				setPropertyType("Formula");
				setPropertyDefaultValue("new Formula(this, \"y\", \"x * dx\", Type.EQUATION) // y = x * dx");
			} else if (stringValue.contains(".")) {
				setPropertyType(stringValue);
				setPropertyDefaultValue("new " + stringValue + "()");
			} else {
				setPropertyType(stringValue);
			}
		} else if (PROP_AGENT_PROPERTY_DEFAULT_VALUE.equals(id)) {
			setPropertyDefaultValue((String) value);
		} else if (PROP_AGENT_PROPERTY_VISIBILITY.equals(id)) {
			setVisibility((Integer) value);
		} else {
			super.setPropertyValue(id, value);
		}
	}

	/**
	 * @return
	 */
	public String getPropertyDescription() {
		return propertyComment;
	}

	/**
	 * Sets the propertyComment and fires a property change event.
	 * 
	 * @param string
	 *            the propertyComment
	 */
	public void setPropertyDescription(String string) {
		propertyComment = string;
	}

	public String getPropertyCompiledName() {
		return propertyCompiledName;
	}

	public void setPropertyCompiledName(String propertyCompiledName) {
		this.propertyCompiledName = propertyCompiledName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public void setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
	}

	public Integer getVisibility() {
		return visibility;
	}

	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

}
