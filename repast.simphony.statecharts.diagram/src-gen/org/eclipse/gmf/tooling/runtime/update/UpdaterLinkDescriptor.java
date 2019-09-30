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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * @since 3.0
 */
public class UpdaterLinkDescriptor extends UpdaterNodeDescriptor {

	private EObject mySource;

	private EObject myDestination;

	private IAdaptable mySemanticAdapter;

	private UpdaterLinkDescriptor(EObject source, EObject destination, EObject linkElement, int linkVID) {
		super(linkElement, linkVID);
		mySource = source;
		myDestination = destination;
	}

	public UpdaterLinkDescriptor(EObject source, EObject destination, final IElementType elementType, int linkVID) {
		this(source, destination, (EObject) null, linkVID);
		mySemanticAdapter = new IAdaptable() {

			public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
				if (IElementType.class.equals(adapter)) {
					return elementType;
				}
				return null;
			}
		};
	}

	public UpdaterLinkDescriptor(EObject source, EObject destination, EObject linkElement, final IElementType elementType, int linkVID) {
		this(source, destination, linkElement, linkVID);
		mySemanticAdapter = new EObjectAdapter(linkElement) {

			public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
				if (IElementType.class.equals(adapter)) {
					return elementType;
				}
				return super.getAdapter(adapter);
			}
		};
	}

	public EObject getSource() {
		return mySource;
	}

	public EObject getDestination() {
		return myDestination;
	}

	public IAdaptable getSemanticAdapter() {
		return mySemanticAdapter;
	}

}
