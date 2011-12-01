/*
 * Copyright (c) 2004, Peter Friese All rights reserved. (Adapted by Michael J.
 * North for Use in Repast Simphony, with Thanks to the Original Author)
 * (Michael J. North’s Modifications are Copyright 2007 Under the Repast
 * Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.model.propertydescriptors;

import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxCellEditor;
import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxLabelProvider;

/**
 * A property descriptor for looking up java types.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 09.07.2004
 */

public class TypeLookupPropertyDescriptor extends PropertyDescriptor {

	public final static String CHOOSE_USING_TYPE_SELECTOR = "Choose Using Type Selector...";

	public final static String[] DEFAULT_SELECTION_LIST = { TypeLookupPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR };

	private boolean multipleSelect = false;
	public String value = "";
	public int typesToConsider = IJavaElementSearchConstants.CONSIDER_ALL_TYPES;

	/**
	 * @param id
	 * @param displayName
	 */
	public TypeLookupPropertyDescriptor(Object id, String displayName,
			boolean multipleSelect, int newTypesToConsider, String newValue) {
		super(id, displayName);
		this.multipleSelect = multipleSelect;
		this.typesToConsider = newTypesToConsider;
		this.value = newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.PropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		EditableComboBoxCellEditor editor = new EditableComboBoxCellEditor(
				parent,
				TypeLookupPropertyDescriptor.DEFAULT_SELECTION_LIST,
				SWT.DROP_DOWN, this.multipleSelect, null);
		editor.doSetValue(value);
		return editor;
	}

	public ILabelProvider getLabelProvider() {
		if (this.isLabelProviderSet()) {
			return super.getLabelProvider();
		} else {
			return new EditableComboBoxLabelProvider();
		}
	}

	public void doSetValue(Object newText) {
		if (newText instanceof String) {
			this.value = (String) newText;
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
