/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Flow4J-Eclipse project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.TypeLookupPropertyDescriptor;
import repast.simphony.agents.designer.model.propertysources.BehaviorSchedulePropertySource;
import repast.simphony.agents.designer.model.propertysources.BehaviorWatchPropertySource;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * Model part of the start flowlet.
 */
@SuppressWarnings("unchecked")
public class BehaviorStepModelPart extends
		AgentPropertyorStepWithLabelModelPart {

	public static final String PROP_BEHAVIOR_SCHEDULE = "behavior_schedule";
	public static final String PROP_BEHAVIOR_WATCH = "behavior_watch";

	public static final String PROP_BEHAVIOR_COMMENT = "behavior_comment";
	public static final String PROP_BEHAVIOR_VISIBILITY = "behavior_visibility";
	public static final String PROP_BEHAVIOR_RETURNTYPE = "behavior_returnType";
	public static final String PROP_BEHAVIOR_COMPILEDNAME = "behavior_compiledName";
	public static final String PROP_BEHAVIOR_PARAMETERS = "behavior_parameters";
	public static final String PROP_BEHAVIOR_EXCEPTIONS = "behavior_exceptions";

	private BehaviorSchedulePropertySource behaviorSchedulePropertySource = new BehaviorSchedulePropertySource();
	private BehaviorWatchPropertySource behaviorWatchPropertySource = new BehaviorWatchPropertySource(
			this);
	private String comment = "This is the step behavior.";
	private Integer visibility = new Integer(0);
	private String[] visibilityList = { "Everyone", "Only Me",
			"Only My Children", "Only My Friends",
			"Everyone (Without Creating an Agent)",
			"Only Me (Without Creating an Agent)",
			"Only My Children (Without Creating an Agent)",
			"Only My Friends (Without Creating an Agent)" };
	private String returnType = "def";
	private String compiledName = "";
	private String parameters = "";
	private String exceptions = "";
	private static String[] typeList = { "def", "<Constructor>", "boolean", "int", "long", "float",
			"double", "String", "Amount", "Matrix", "Object",
			EditableComboBoxPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR };

	private Set behaviorStepPropertyDescriptors = null;

	/**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
	@Override
	public String getType() {
		return AgentBuilderConsts.BEHAVIOR;
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
		// allways create new ones
		createPropertyDescriptors();

		return behaviorStepPropertyDescriptors;
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * TextPropertyDescriptor for the flowlet's label
	 */
	private void createPropertyDescriptors() {

		behaviorStepPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());

		behaviorStepPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_BEHAVIOR_COMMENT,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.comment")));
		behaviorStepPropertyDescriptors
				.add(new PropertyDescriptor(
						PROP_BEHAVIOR_WATCH,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watch")));
		behaviorStepPropertyDescriptors
				.add(new PropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.schedule")));
		behaviorStepPropertyDescriptors
				.add(new ExtendedComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_VISIBILITY,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.visibility"),
						visibilityList));
		behaviorStepPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_RETURNTYPE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.returnType"),
						typeList, null));
		behaviorStepPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_BEHAVIOR_COMPILEDNAME,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.compiledName")));
		behaviorStepPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_BEHAVIOR_PARAMETERS,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.parameters")));
		behaviorStepPropertyDescriptors
				.add(new TypeLookupPropertyDescriptor(
						PROP_BEHAVIOR_EXCEPTIONS,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.exceptions"),
						true, IJavaElementSearchConstants.CONSIDER_ALL_TYPES,
						this.exceptions));
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

		if (PROP_BEHAVIOR_COMMENT.equals(id)) {
			return getComment();
		} else if (PROP_BEHAVIOR_SCHEDULE.equals(id)) {
			return getBehaviorSchedulePropertySource();
		} else if (PROP_BEHAVIOR_WATCH.equals(id)) {
			return getBehaviorWatchPropertySource();
		} else if (PROP_BEHAVIOR_VISIBILITY.equals(id)) {
			return getVisibility();
		} else if (PROP_BEHAVIOR_RETURNTYPE.equals(id)) {
			return getReturnType();
		} else if (PROP_BEHAVIOR_COMPILEDNAME.equals(id)) {
			return getCompiledName();
		} else if (PROP_BEHAVIOR_PARAMETERS.equals(id)) {
			return getParameters();
		} else if (PROP_BEHAVIOR_EXCEPTIONS.equals(id)) {
			return getExceptions();
		} else {
			return super.getPropertyValue(id);
		}

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
		if (PROP_BEHAVIOR_COMMENT.equals(id)) {
			setComment((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULE.equals(id)) {
			setBehaviorSchedulePropertySource((BehaviorSchedulePropertySource) value);
		} else if (PROP_BEHAVIOR_WATCH.equals(id)) {
			setBehaviorWatchPropertySource((BehaviorWatchPropertySource) value);
		} else if (PROP_BEHAVIOR_VISIBILITY.equals(id)) {
			setVisibility((Integer) value);
		} else if (PROP_BEHAVIOR_RETURNTYPE.equals(id)) {
			setReturnType((String) value);
		} else if (PROP_BEHAVIOR_COMPILEDNAME.equals(id)) {
			setCompiledName((String) value);
		} else if (PROP_BEHAVIOR_PARAMETERS.equals(id)) {
			setParameters((String) value);
		} else if (PROP_BEHAVIOR_EXCEPTIONS.equals(id)) {
			setExceptions((String) value);
		} else {
			super.setPropertyValue(id, value);
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getStartFlowletPropertyDescriptors() {
		return behaviorStepPropertyDescriptors;
	}

	public void setStartFlowletPropertyDescriptors(
			Set startFlowletPropertyDescriptors) {
		this.behaviorStepPropertyDescriptors = startFlowletPropertyDescriptors;
	}

	public Integer getVisibility() {
		return visibility;
	}

	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getCompiledName() {
		return compiledName;
	}

	public void setCompiledName(String compiledName) {
		this.compiledName = compiledName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getExceptions() {
		return exceptions;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public BehaviorSchedulePropertySource getBehaviorSchedulePropertySource() {
		return behaviorSchedulePropertySource;
	}

	public void setBehaviorSchedulePropertySource(
			BehaviorSchedulePropertySource behaviorSchedulePropertySource) {
		this.behaviorSchedulePropertySource = behaviorSchedulePropertySource;
	}

	public BehaviorWatchPropertySource getBehaviorWatchPropertySource() {
		return behaviorWatchPropertySource;
	}

	public void setBehaviorWatchPropertySource(
			BehaviorWatchPropertySource behaviorWatchPropertySource) {
		this.behaviorWatchPropertySource = behaviorWatchPropertySource;
	}

	/**
	 * Sets the flowlet's label.
	 * 
	 * @param propertyOrStepLabel
	 */
	public void setLabel(String newLabel) {
		super.setLabel(newLabel);
		if ((this.compiledName == null) || (this.compiledName.equals(""))) {
			this.compiledName = newLabel.trim().toLowerCase();
		}
	}

	/**
	 * TODO
	 * 
	 * @param prop
	 * @param old
	 * @param finalValue
	 */
	public void firePropertyChange(String prop, Object old, Object newValue) {
		super.firePropertyChange(prop, old, newValue);
	}

}
