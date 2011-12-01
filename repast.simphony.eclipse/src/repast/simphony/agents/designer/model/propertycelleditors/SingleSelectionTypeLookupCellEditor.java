/*
 * Copyright (c) 2004, Peter Friese All rights reserved. (Adapted by Michael J.
 * North for Use in Repast Simphony, with Thanks to the Original Authors)
 * (Michael J. North’s Modifications are Copyright 2007 Under the Repast
 * Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.model.propertycelleditors;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.SelectionDialog;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;

/**
 * A cell editor that opens a type lookup editor for selecting a java type.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 09.07.2004
 */
@SuppressWarnings( { "unchecked", "deprecation" })
public class SingleSelectionTypeLookupCellEditor extends DialogCellEditor {

	public String value = "";

	/**
	 * A cell editor that features a button with elipses. When the user clicks
	 * the button, a Java type chooser dialog opens, displaying the type
	 * hierarchy of the type specified in <code>baseType</code>.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param baseType
	 *            The base type of the class hierarchy to display.
	 */
	public SingleSelectionTypeLookupCellEditor(Composite parent, String newValue) {
		super(parent);
		this.value = newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		SelectionDialog dialog;
		try {
			// IJavaSearchScope scope = SearchEngine
			// .createHierarchyScope(this.baseType);
			IJavaElement[] javaElements = new IJavaElement[1];
			javaElements[0] = AgentBuilderPlugin.getActiveJavaProject();
			IJavaSearchScope scope = SearchEngine
					.createJavaSearchScope(javaElements);
			dialog = JavaUI.createTypeDialog(cellEditorWindow.getShell(),
					new ProgressMonitorDialog(cellEditorWindow.getShell()),
					scope, IJavaElementSearchConstants.CONSIDER_ALL_TYPES,
					false, this.value);
			dialog
					.setTitle("Please Select a Type (Press the \"Cancel\" Button to Select Nothing)");
			dialog
					.setMessage("Please Select a Type (Press the \"Cancel\" Button to Select Nothing)");
		} catch (JavaModelException e) {
			e.printStackTrace();
			return "";
		}
		if (dialog.open() == IDialogConstants.CANCEL_ID)
			return "";

		Object[] types = dialog.getResult();
		if (types == null || types.length == 0) {
			return "";
		}

		IType selection = (IType) types[0];
		return selection.getFullyQualifiedName();
	}

}
