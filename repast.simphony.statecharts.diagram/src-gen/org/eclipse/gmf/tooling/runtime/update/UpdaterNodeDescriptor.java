/*
 * Copyright (c) 2006, 2007, 2012 Borland Software Corporation and others
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Alexander Shatalin (Borland) - original templates
 *    Michael Golubev (Montages) - API extracted to gmf.tooling.runtime (#372479)
 */
package org.eclipse.gmf.tooling.runtime.update;

import org.eclipse.emf.ecore.EObject;

/**
 * @since 3.0
 */
public class UpdaterNodeDescriptor {

	private final EObject myModelElement;

	private final int myVisualID;

	public UpdaterNodeDescriptor(EObject modelElement, int visualID) {
		myModelElement = modelElement;
		myVisualID = visualID;
	}

	public EObject getModelElement() {
		return myModelElement;
	}

	public int getVisualID() {
		return myVisualID;
	}

}
