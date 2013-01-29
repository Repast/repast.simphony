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

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import repast.simphony.agents.designer.AgentBuilderDesignerException;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
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
public class BooleanTransitionModelPart extends TransitionModelPart implements
		IPropertySource {

	public static String PROP_BOOLEAN_VALUE = "boolean_value"; //$NON-NLS-1$
	public static Value TRUE_VALUE = new BooleanTransitionModelPart().new Value(
			0, true);
	private static Value FALSE_VALUE = new BooleanTransitionModelPart().new Value(
			1, false);
	private static String[] BOOLEAN_VALUES = { TRUE_VALUE.toString(),
			FALSE_VALUE.toString() };

	/**
	 * The initial value ist TRUE. Must correspont with the default label value
	 * in the BooleanTransitionEditPart.
	 */
	private Value value = TRUE_VALUE;
	private static Set booleanTransitionPropertyDescriptors = null;

	/**
	 * 
	 * Holds the Boolean value information
	 */
	public class Value {
		private int intValue;
		private boolean boolValue;

		Value(int intValue, boolean boolValue) {
			this.intValue = intValue;
			this.boolValue = boolValue;
		}

		@Override
		public String toString() {
			return new Boolean(boolValue).toString();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object otherValue) {
			return intValue == ((Value) otherValue).intValue;
		}
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	private Value getValue() {
		return value;
	}

	/**
	 * Returns the Value object for the requested integer value.
	 * 
	 * @param intValue
	 *            the Value object for the requested integer value.
	 * @return
	 * @throws AgentBuilderDesignerException
	 *             if the intValue is unknown
	 */
	private Value getValue(int intValue) {
		if (intValue == TRUE_VALUE.intValue)
			return TRUE_VALUE;
		else if (intValue == FALSE_VALUE.intValue)
			return FALSE_VALUE;
		else
			throw new AgentBuilderDesignerException("Invalid boolean value: "
					+ intValue);
	}

	/**
	 * Returns the boolean representation of the value.
	 * 
	 * @return the boolean representation of the value.
	 */
	public boolean getBoolValue() {
		return getValue().boolValue;
	}

	/**
	 * Returns the String representation of the value.
	 * 
	 * @return the String representation of the value.
	 */
	public String getStringValue() {
		return getValue().toString();
	}

	/**
	 * Sets the value and notifies the other BooleanTransition.
	 * 
	 * @param value
	 *            the new value
	 */
	private void setValue(Value value) {
		Value oldValue = this.value;
		this.value = value;
		firePropertyChange(PROP_BOOLEAN_VALUE, oldValue, value);
	}

	/**
	 * Sets the of this BooleanTransition
	 * 
	 * @param boolValue
	 *            the new value
	 */
	public void setValue(boolean boolValue) {
		setValue(boolValue ? TRUE_VALUE : FALSE_VALUE);
	}

	/**
	 * Provides the textual contents of the property label
	 */
	private class BooleanValueLabelProvider extends LabelProvider {

		/**
		 * Returns the text for the label of the given value, but not for the
		 * surrounding class instance.
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			return getValue(((Integer) element).intValue()).toString();
		}

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
	 * ComboBoxPropertyDescriptor
	 */
	private void createPropertyDescriptors() {
		if (booleanTransitionPropertyDescriptors != null)
			return;

		booleanTransitionPropertyDescriptors = new HashSet();
		ExtendedComboBoxPropertyDescriptor cbpd = new ExtendedComboBoxPropertyDescriptor(
				PROP_BOOLEAN_VALUE,
				AgentBuilderPlugin
						.getResourceString("PropertyDescriptor_BooleanTransition.boolean_value"),
				BOOLEAN_VALUES);
		cbpd.setLabelProvider(new BooleanValueLabelProvider());
		booleanTransitionPropertyDescriptors.add(cbpd);
	}

	/**
	 * Returns the collection of the property descriptors
	 * 
	 * @return the collection of the property descriptors
	 */
	protected Collection getPropertyDescriptorsCollection() {
		if (booleanTransitionPropertyDescriptors == null)
			createPropertyDescriptors();

		return booleanTransitionPropertyDescriptors;
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
	 * Returns an Object which represents the appropriate boolValue for the
	 * property name supplied.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 * @param propName
	 *            Name of the property for which the the values are needed.
	 * @return Object which is the boolValue of the property.
	 */
	public Object getPropertyValue(Object propName) {
		if (PROP_BOOLEAN_VALUE.equals(propName))
			return new Integer(value.intValue);
		else
			throw new AgentBuilderDesignerException(
					"BoleanTransitionModelPart.getPropertyValue() unknown propName: "
							+ propName);
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
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the boolValue of a given property with the boolValue supplied. Also
	 * fires a property change if necessary.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 * @param intValue
	 *            Name of the parameter to be changed.
	 * @param boolValue
	 *            Value to be set to the given parameter.
	 */
	public void setPropertyValue(Object id, Object value) {
		if (PROP_BOOLEAN_VALUE.equals(id)) {
			setValue(getValue(((Integer) value).intValue()));
		} else
			System.out
					.println("BoleanTransitionModelPart.setPropertyValue() unknown intValue: "
							+ id);
	}

	/**
	 * handles the notofication of the other BooleanTransition after a value
	 * change.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String propName = event.getPropertyName();
		if (PROP_BOOLEAN_VALUE.equals(propName)) {
			BooleanTransitionModelPart source = (BooleanTransitionModelPart) event
					.getSource();
			if (source.getBoolValue() == this.getBoolValue()) {
				// the other transition was set to my value, so I have to react
				// and change my value
				setValue(!source.getBoolValue());
			}
		} else
			super.propertyChange(event);

	}

}
