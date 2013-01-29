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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.TypeLookupPropertyDescriptor;

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
public class AgentDiagramModelPart extends AgentModelPart implements
		IPropertySource {

	public static final String CHILDREN = "children"; //$NON-NLS-1$

	public static final String PROP_AGENT_COMMENT = "agent_comment"; //$NON-NLS-1$
	public static final String PROP_AGENT_CLASS_NAME = "agent_class_name"; //$NON-NLS-1$
	public static final String PROP_AGENT_PARENT_CLASS_NAME = "agent_parent_class_name"; //$NON-NLS-1$
	public static final String PROP_AGENT_INTERFACES = "agent_interfaces"; //$NON-NLS-1$
	public static final String PROP_AGENT_IMPORTS = "agent_imports"; //$NON-NLS-1$
	public static final String PROP_AGENT_NEXT_STEP = "agent_next_step"; //$NON-NLS-1$

	private static Set agentDiagramElementPropertyDescriptors = null;
	private List children = new ArrayList();

	private String agentComment = "This is an agent.";
	private String agentName = "";
	private String agentParentClassName = "";
	private String agentInterfaces = "";
	private String agentImports = "";

	/**
	 * Adds a child to the agent.
	 * 
	 * @param child
	 */
	public void addChild(AgentDiagramElementModelPart child) {
		setChildParentClassName(child);
		addChild(child, -1);
	}

	/**
	 * Adds a child's modelpart to the diagram
	 * 
	 * @param child
	 *            child model part
	 * @param index
	 */
	public void addChild(AgentDiagramElementModelPart child, int index) {
		setChildParentClassName(child);
		if (index >= 0)
			children.add(index, child);
		else
			children.add(child);

		fireStructureChange(CHILDREN, child);
	}

	public void setChildParentClassName(AgentDiagramElementModelPart child) {
		if ((agentParentClassName == null) || (agentParentClassName.equals(""))) {
			child
					.setParentClassName(AgentDiagramElementModelPart.DEFAULT_PARENT_CLASS_NAME);
		} else {
			child.setParentClassName(agentParentClassName);
		}
	}

	/**
	 * Returns the list of all children's model parts
	 * 
	 * @return
	 */
	public List getChildren() {
		return children;
	}

	/**
	 * Removes the childs's model part from the diagram
	 * 
	 * @param child
	 */
	public void removeChild(AgentDiagramElementModelPart child) {
		if (children.remove(child))
			fireStructureChange(CHILDREN, child);
	}

	/**
	 * TODO
	 * 
	 * @param l
	 */
	public void addStructureChangeListener(PropertyChangeListener l) {
		addPropertyChangeListener(l);
	}

	/**
	 * TODO
	 * 
	 * @param l
	 */
	public void removeStructureChangeListener(PropertyChangeListener l) {
		removePropertyChangeListener(l);
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * descriptor for the location group
	 */
	private void createPropertyDescriptors() {
		if (agentDiagramElementPropertyDescriptors != null)
			return;

		agentDiagramElementPropertyDescriptors = new HashSet();

		agentDiagramElementPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_AGENT_COMMENT,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentDiagram.agentComment"))); //$NON-NLS-1$
		PropertyDescriptor propDesc = new PropertyDescriptor(
				PROP_AGENT_CLASS_NAME,
				AgentBuilderPlugin
						.getResourceString("PropertyDescriptor_AgentDiagram.agentName"));
		agentDiagramElementPropertyDescriptors.add(propDesc);
		agentDiagramElementPropertyDescriptors
				.add(new TypeLookupPropertyDescriptor(
						PROP_AGENT_PARENT_CLASS_NAME,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentDiagram.agentParentClassName"),
						false, IJavaElementSearchConstants.CONSIDER_CLASSES,
						this.agentParentClassName)); //$NON-NLS-1$
		agentDiagramElementPropertyDescriptors
				.add(new TypeLookupPropertyDescriptor(
						PROP_AGENT_INTERFACES,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentDiagram.agentSpecialInterfacesImplemented"),
						true, IJavaElementSearchConstants.CONSIDER_INTERFACES,
						this.agentInterfaces)); //$NON-NLS-1$
		agentDiagramElementPropertyDescriptors
				.add(new TypeLookupPropertyDescriptor(
						PROP_AGENT_IMPORTS,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_AgentDiagram.agentImports"),
						true, IJavaElementSearchConstants.CONSIDER_ALL_TYPES,
						this.agentImports)); //$NON-NLS-1$
		// agentDiagramElementPropertyDescriptors
		// .add(new PropertyDescriptor(
		// PROP_AGENT_NEXT_STEP,
		// AgentBuilderPlugin
		// .getResourceString("PropertyDescriptor_AgentDiagram.agentNextStep")));
		// //$NON-NLS-1$
	}

	/**
	 * Returns the collection of the property descriptors
	 * 
	 * @return the collection of the property descriptors
	 */
	protected Collection getPropertyDescriptorsCollection() {
		if (agentDiagramElementPropertyDescriptors == null)
			createPropertyDescriptors();

		return agentDiagramElementPropertyDescriptors;
	}

	/**
	 * Returns useful property agentDiagramDescriptors for the use in property
	 * sheets. This supports location.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 * @return Array of property agentDiagramElementescriptors.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return (IPropertyDescriptor[]) getPropertyDescriptorsCollection()
				.toArray(new IPropertyDescriptor[] {});
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
	public Object getPropertyValue(Object propName) {
		if (PROP_AGENT_COMMENT.equals(propName))
			return getAgentComment();
		if (PROP_AGENT_CLASS_NAME.equals(propName))
			return getAgentName();
		if (PROP_AGENT_PARENT_CLASS_NAME.equals(propName))
			return getAgentParentClassName();
		if (PROP_AGENT_INTERFACES.equals(propName))
			return getAgentInterfaces();
		if (PROP_AGENT_IMPORTS.equals(propName))
			return getAgentImports();
		if (PROP_AGENT_NEXT_STEP.equals(propName))
			return "";
		else
			System.out
					.println("AgentDiagramModelPart.getPropertyValue() unknown propName: "
							+ propName);

		return null;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object propName) {
		return isPropertySet((String) propName);
	}

	/**
	 * TODO
	 * 
	 * @param propName
	 * @return
	 */
	protected boolean isPropertySet(String propName) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object arg0) {
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
	public void setPropertyValue(Object id, Object value) {
		if (PROP_AGENT_COMMENT.equals(id)) {
			setAgentComment((String) value);
		} else if (PROP_AGENT_CLASS_NAME.equals(id)) {
			setAgentName((String) value);
		} else if (PROP_AGENT_PARENT_CLASS_NAME.equals(id)) {
			setAgentParentClassName((String) value);
		} else if (PROP_AGENT_INTERFACES.equals(id)) {
			setAgentInterfaces((String) value);
		} else if (PROP_AGENT_IMPORTS.equals(id)) {
			setAgentImports((String) value);
		} else if (PROP_AGENT_NEXT_STEP.equals(id)) {
		} else {
			System.out
					.println("AgentDiagramModelPart.setPropertyValue() unknown id: "
							+ id);
		}
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentClassName) {
		this.agentName = agentClassName;
	}

	public String getAgentParentClassName() {
		return agentParentClassName;
	}

	public void setAgentParentClassName(String agentParentClassName) {
		this.agentParentClassName = agentParentClassName;
	}

	public String getAgentInterfaces() {
		return agentInterfaces;
	}

	public void setAgentInterfaces(String agentInterfaces) {
		this.agentInterfaces = agentInterfaces;
	}

	public String getAgentComment() {
		return agentComment;
	}

	public void setAgentComment(String agentComment) {
		this.agentComment = agentComment;
	}

	public String getAgentImports() {
		return agentImports;
	}

	public void setAgentImports(String agentImports) {
		this.agentImports = agentImports;
	}

}
