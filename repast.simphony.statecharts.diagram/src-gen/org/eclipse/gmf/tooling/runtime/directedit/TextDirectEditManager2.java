/******************************************************************************
 * Copyright (c) 2002, 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Dmitry Stadnik (Borland) - contribution for bugzilla 135694
 ****************************************************************************/

package org.eclipse.gmf.tooling.runtime.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;

/**
 * @since 3.2
 * @deprecated This interface is left for backward compatibility only and will be removed soon.
 * @see org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager
 */
@Deprecated
public class TextDirectEditManager2 extends TextDirectEditManager {

	/**
	 * constructor
	 * 
	 * @param source
	 *            <code>GraphicalEditPart</code> to support direct edit of. The
	 *            figure of the <code>source</code> edit part must be of type
	 *            <code>WrapLabel</code>.
	 */
	public TextDirectEditManager2(ITextAwareEditPart source) {
		super(source);
	}

	/**
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	public TextDirectEditManager2(GraphicalEditPart source, Class<?> editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
	}

}