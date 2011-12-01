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

package repast.simphony.agents.designer.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.model.codegen.Consts;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
public class BuildDeltaVisitor implements IResourceDeltaVisitor {

	private IProgressMonitor monitor;
	private AgentBuilderBuilder builder;

	/**
	 * TODO
	 * 
	 */
	public BuildDeltaVisitor(IProgressMonitor monitor,
			AgentBuilderBuilder builder) {
		super();
		this.monitor = monitor;
		this.builder = builder;
	}

	/**
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 * @return true if the resource delta's children should be visited; false if
	 *         they should be skipped.
	 */
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();

		if (resource instanceof IProject) {
			// Only check projects with plugin nature
			IProject project = (IProject) resource;
			return AgentBuilderBuilder.isBuildableProject(project);
		}

		if (resource instanceof IFolder)
			return true;

		if (resource instanceof IFile) {
			// see if this is it
			IFile file = (IFile) resource;

			if (AgentBuilderBuilder.isFlowFile(file)) {
				// That's it, but only check it if it has been added or changed
				if (delta.getKind() != IResourceDelta.REMOVED) {
					builder.buildFlow(builder.getClasspathSrcFolder(file),
							file, monitor);
					return true;
				} else {
					removeOutputFile(file, monitor);
				}
			}
		}
		return false;
	}

	/**
	 * Removes the flow's java source file
	 * 
	 * @param file
	 *            the flow file
	 * @param monitor
	 *            the progress monitor
	 */
	private void removeOutputFile(IFile file, IProgressMonitor monitor) {
		// String className =
		// file.getFullPath().removeFileExtension().lastSegment();
		// IPath javaFilePath =
		// builder.getProject().getFolder(AgentBuilderPlugin.FOLDER_NAME_JAVA).
		// getFullPath().removeFirstSegments(1)
		// .append(file.getFullPath().removeFirstSegments(1));
		// javaFilePath =
		//javaFilePath.removeLastSegments(1).append(className).addFileExtension(
		// "groovy");
		// IFile outputFile = builder.getProject().getFile(javaFilePath);
		IFile outputFile = builder.getProject().getFile(
				file.getFullPath().removeFirstSegments(1).removeFileExtension()
						.addFileExtension(Consts.FILE_EXTENSION_GROOVY));

		monitor.subTask("removing file " + outputFile.getName());
		try {
			outputFile.delete(true, true, monitor);
		} catch (CoreException e) {
		}

		outputFile = builder.getProject().getFile(
				file.getFullPath().removeFirstSegments(1).removeFileExtension()
						.addFileExtension(Consts.FILE_EXTENSION_HTML));

		IPath outputFilePath = outputFile.getFullPath();

		while ((outputFilePath.segmentCount() > 0)
				&& (!outputFilePath.segment(0).equals("src"))) {
			outputFilePath = outputFilePath.removeFirstSegments(1);
		}

		String baseFileName = file.getName();
		if (baseFileName.endsWith(".agent"))
			baseFileName = baseFileName.substring(0, baseFileName.length() - 6);
		String fullFileName = outputFilePath.removeFirstSegments(1)
				.removeLastSegments(1).removeFileExtension().toPortableString()
				.replace("/", ".")
				+ "." + baseFileName + ".html";

		IFolder docsFolder = builder.getProject().getFolder("docs");
		IFile htmlFile = docsFolder.getFile(fullFileName);

		monitor.subTask("removing file " + fullFileName);
		try {
			htmlFile.delete(true, true, monitor);
		} catch (CoreException e) {
			AgentBuilderPlugin.log(e);
		}

		try {
			IFile indexFile = docsFolder.getFile("index.html");
			InputStream indexStream = indexFile.getContents();
			StringBuffer indexFileStringBuffer = new StringBuffer(indexStream
					.available());
			while (indexStream.available() > 0) {
				indexFileStringBuffer.append((char) indexStream.read());
			}
			String indexFileString = indexFileStringBuffer.toString();
			outputFilePath = outputFile.getFullPath();
			while ((outputFilePath.segmentCount() > 0)
					&& (!outputFilePath.segment(0).equals("src"))) {
				outputFilePath = outputFilePath.removeFirstSegments(1);
			}
			if (outputFilePath.segment(0).equals("src")) {

				String sFileName = fullFileName.substring(0, fullFileName
						.length() - 5);
				indexFileString = indexFileString.replace(
						"<li>The documentation for \"" + sFileName
								+ "\" can be found <a href=\"" + fullFileName
								+ "\">here</a>.</li></p>"
								+ Util.getPlatformLineDelimiter(), "");

				ByteArrayInputStream bin = new ByteArrayInputStream(
						indexFileString.getBytes());

				monitor.subTask("Build HTML Index: " + indexFile);
				if (!indexFile.exists())
					indexFile.create(bin, true, monitor);
				else
					indexFile.setContents(bin, true, true, monitor);

			}

			baseFileName = file.getName();
			if (baseFileName.endsWith(".agent"))
				baseFileName = baseFileName.substring(0, baseFileName.length() - 6);
			fullFileName = outputFilePath.removeFirstSegments(1)
					.removeLastSegments(1).removeFileExtension().toPortableString()
					.replace("/", ".")
					+ "." + baseFileName + ".png";

			docsFolder = builder.getProject().getFolder("docs");
			IFolder imagesFolder = docsFolder.getFolder("images");
			IFile imageFile = imagesFolder.getFile(fullFileName);

			monitor.subTask("removing file " + fullFileName);
			try {
				imageFile.delete(true, true, monitor);
			} catch (CoreException e) {
			}
			
		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
		} finally {
			monitor.done();
		}
	}

}
