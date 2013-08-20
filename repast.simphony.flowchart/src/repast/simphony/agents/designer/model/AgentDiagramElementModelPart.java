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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import repast.simphony.agents.designer.model.propertysources.LocationPropertySource;

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
abstract public class AgentDiagramElementModelPart extends AgentModelPart
		implements IPropertySource {

	public static final String PROP_LOCATION = "location"; //$NON-NLS-1$

	public static final String DEFAULT_PARENT_CLASS_NAME = "java.lang.Object";

	private Point location = new Point(0, 0);

	protected String parentClassName = DEFAULT_PARENT_CLASS_NAME;

	private static Set agentDiagramElementPropertyDescriptors = null;

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * descriptor for the location group
	 */
	private void createPropertyDescriptors() {
		if (agentDiagramElementPropertyDescriptors != null)
			return;

		agentDiagramElementPropertyDescriptors = new HashSet();
		// flowDiagramElementPropertyDescriptors.add(new
		// PropertyDescriptor(PROP_LOCATION,
		// AgentBuilderPlugin.getResourceString("PropertyDescriptor_AgentDiagramElement.location")));
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
	 * Returns useful property flowDiagramElementDescriptors for the use in
	 * property sheets. This supports location.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 * @return Array of property flowDiagramElementescriptors.
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
		if (PROP_LOCATION.equals(propName))
			return new LocationPropertySource(getLocation());
		else
			System.out
					.println("FlowDiagramElementModelPart.getPropertyValue() unknown propName: "
							+ propName);

		return null;
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
		if (PROP_LOCATION.equals(id)) {
			LocationPropertySource propSource = (LocationPropertySource) value;
			setLocation(new Point(propSource.getValue()));
		} else
			System.out
					.println("AgentDiagramElementModelPart.setPropertyValue() unknown id: "
							+ id);
	}

	/**
	 * Sets the location property and fires a change event
	 * 
	 * @param point
	 *            the new location
	 */
	public void setLocation(Point point) {
		if (location == null || point == null || location.equals(point))
			return;
		Point old = location.getCopy();
		location = point;
		firePropertyChange(PROP_LOCATION, old, point);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object prop) {
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

	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

}
