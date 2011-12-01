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

package repast.simphony.agents.designer.ui.editors;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PlatformUI;

/**
 * @author alex
 * 
 * TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class JarEntryEditorInput implements IStorageEditorInput {

	private IStorage jarEntry;

	public JarEntryEditorInput(String archiveName, String entryName) {
		jarEntry = new JarEntry(archiveName, entryName);
	}

	public JarEntryEditorInput(String pathStr) {
		int jarSepPos = pathStr
				.indexOf(IJavaSearchScope.JAR_FILE_ENTRY_SEPARATOR);
		String archiveName = pathStr.substring(0, jarSepPos);
		String entryName = pathStr.substring(jarSepPos + 1);

		jarEntry = new JarEntry(archiveName, entryName);
	}

	/**
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	public IStorage getStorage() throws CoreException {
		return jarEntry;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return true;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry()
				.getImageDescriptor(jarEntry.getFullPath().getFileExtension());
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return jarEntry.getName();
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return jarEntry.getFullPath().toString();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * 
	 * @author alex
	 * 
	 * TODO
	 * 
	 * 
	 * 
	 */
	public class JarEntry implements IStorage {

		String archiveName;
		String entryName;
		IPath path;

		public JarEntry(String archiveName, String entryName) {
			this.archiveName = archiveName;
			this.entryName = entryName;
			path = new Path(entryName);
		}

		/**
		 * @see org.eclipse.core.resources.IStorage#getContents()
		 */
		public InputStream getContents() throws CoreException {

			try {
				ZipFile zipFile = new ZipFile(archiveName);
				ZipEntry zipEntry = zipFile.getEntry(entryName);
				if (zipEntry == null) {
					throw new JavaModelException(new CoreException(new Status(
							IStatus.ERROR, "Flow4J", "Error: Invalid Path for "
									+ this.entryName)));
				}
				return zipFile.getInputStream(zipEntry);
			} catch (IOException e) {
				throw new JavaModelException(e,
						IJavaModelStatusConstants.IO_EXCEPTION);
			}
		}

		/**
		 * @see org.eclipse.core.resources.IStorage#getFullPath()
		 */
		public IPath getFullPath() {
			return path;
		}

		/**
		 * @see org.eclipse.core.resources.IStorage#getName()
		 */
		public String getName() {
			return path.lastSegment();
		}

		/**
		 * @see org.eclipse.core.resources.IStorage#isReadOnly()
		 */
		public boolean isReadOnly() {
			return true;
		}

		/**
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		public Object getAdapter(Class adapter) {
			return Platform.getAdapterManager().getAdapter(this, adapter);
		}

	}

}
