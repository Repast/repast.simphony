/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif�s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North�s Modifications are Copyright 2007 Under
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.model.codegen.Consts;
import repast.simphony.agents.model.codegen.DepthCounter;
import repast.simphony.agents.model.codegen.JavaSrcModelDigester;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class AgentBuilderBuilder extends IncrementalProjectBuilder {

	public static final String newFont = "<FONT FACE=Verdana, Arial, Sans>";
	
	/**
	 * Creates a new AgentBuilderBuilder instance.
	 * 
	 */
	public AgentBuilderBuilder() {
		super();
	}

	/**
	 * Returns the JavaProject instance of the project.
	 * 
	 * @return the JavaProject instance of the project.
	 */
	IJavaProject getJavaProject() {
		return AgentBuilderPlugin.getJavaProject(getProject());
	}

	/**
	 * TODO
	 * 
	 * @return
	 * @throws JavaModelException
	 */
	@SuppressWarnings("unchecked")
	private IContainer[] getClasspathSrcContainers() throws JavaModelException {
		IClasspathEntry[] classpathEntries = getJavaProject().getRawClasspath();
		List entries = new ArrayList();
		IProject project = getProject();
		for (int i = 0; i < classpathEntries.length; i++) {
			IClasspathEntry entry = classpathEntries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				if (entry.getPath().segmentCount() < 2)
					entries.add(project);
				else
					entries.add(project.getFolder(entry.getPath()
							.removeFirstSegments(1)));
			}
		}
		return (IContainer[]) entries.toArray(new IContainer[entries.size()]);
	}

	/**
	 * Performs a full build or an incremental build and returns a list of
	 * projects which have to be built too or null if no other projects need a
	 * rebuild.
	 */
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		IResourceDelta delta = null;
		if (kind != FULL_BUILD)
			delta = getDelta(getProject());

		if (delta == null || kind == FULL_BUILD) {
			// Full build
			IProject project = getProject();
			if (!isBuildableProject(project))
				return null;
			// build project source folders
			IContainer[] classpathContainers = getClasspathSrcContainers();
			for (int i = 0; i < classpathContainers.length; i++) {
				IContainer container = classpathContainers[i];
				buildFlows(container, container, monitor);
			}

			/*
			 * IPath flowsFolderPath =
			 * project.getFullPath().append(AgentBuilderPlugin
			 * .FOLDER_NAME_FLOWS); IWorkspace workspace =
			 * project.getWorkspace(); if
			 * (workspace.getRoot().exists(flowsFolderPath)) { IResource
			 * flowsFolderResource =
			 * workspace.getRoot().findMember(flowsFolderPath); if
			 * (flowsFolderResource == null) return null; else if
			 * (flowsFolderResource instanceof IFolder)
			 * buildFlows((IFolder)flowsFolderResource, monitor); else if
			 * (flowsFolderResource instanceof IFile)
			 * buildFlow((IFile)flowsFolderResource, monitor); }
			 */
		} else {
			// incremental build
			delta.accept(new BuildDeltaVisitor(monitor, this));
		}

		return null;
	}

	/**
	 * TODO
	 * 
	 * @param resource
	 * @return
	 * @throws CoreException
	 */
	IContainer getClasspathSrcFolder(IResource resource) throws CoreException {
		/*
		 * IProject project = getProject(); IClasspathEntry[] classpathEntries =
		 * getJavaProject().getRawClasspath(); for (int i = 0; i <
		 * classpathEntries.length; i++) { IClasspathEntry entry =
		 * classpathEntries[i]; if (entry.getEntryKind() ==
		 * IClasspathEntry.CPE_SOURCE) { IFolder srcFolder =
		 * project.getFolder(entry.getPath().removeFirstSegments(1)); if
		 * (resource
		 * .getFullPath().matchingFirstSegments(srcFolder.getFullPath()) ==
		 * srcFolder.getFullPath().segments().length) { return srcFolder; } } }
		 * return project;
		 */
		IContainer[] classpathContainers = getClasspathSrcContainers();
		for (int i = 0; i < classpathContainers.length; i++) {
			IContainer container = classpathContainers[i];
			if (resource.getFullPath().matchingFirstSegments(
					container.getFullPath()) == container.getFullPath()
					.segmentCount())
				return container;
		}
		return getProject();
	}

	/**
	 * Builds the flow. Creates the java source in the same folder where the
	 * flow file resides. Creates all intermediate folder resources if
	 * necessary.
	 * 
	 * @param srcFolder
	 *            the parent src folder that is sepcified in the classpath
	 * @param file
	 *            the flow file to build
	 * @param monitor
	 *            the progress monitor
	 */
	protected void buildFlow(IContainer srcFolder, IFile file,
			IProgressMonitor monitor) {
		InputStream in = null;
		try {
			IPath packagePath = file.getFullPath().removeFirstSegments(
					file.getFullPath().matchingFirstSegments(
							srcFolder.getFullPath())).removeLastSegments(1);
			String packageName = packagePath.toString().replace(
					IPath.SEPARATOR, '.');

			in = JavaSrcModelDigester.generateCode(file.getLocation().toFile(),
					packageName, AgentBuilderPlugin.getWorkspaceRoot()
							.getLocation().toFile());

			IFile outputFile = getProject().getFile(
					file.getFullPath().removeFirstSegments(1)
							.removeFileExtension().addFileExtension(
									Consts.FILE_EXTENSION_GROOVY));
			monitor.subTask("Build Groovy source: " + outputFile);
			if (!outputFile.exists())
				outputFile.create(in, true, monitor);
			else
				outputFile.setContents(in, true, true, monitor);

			DepthCounter.setGeneratingGroovy(false);

			in = JavaSrcModelDigester.generateCode(file.getLocation().toFile(),
					packageName, AgentBuilderPlugin.getWorkspaceRoot()
							.getLocation().toFile());

			StringBuffer groovyFileStringBuffer = new StringBuffer(in
					.available());
			while (in.available() > 0) {
				groovyFileStringBuffer.append((char) in.read());
			}

			DepthCounter.setGeneratingGroovy(true);

			outputFile = getProject().getFile(
					file.getFullPath().removeFirstSegments(1)
							.removeFileExtension().addFileExtension(
									Consts.FILE_EXTENSION_HTML));

			IPath outputFilePath = outputFile.getFullPath();

			while ((outputFilePath.segmentCount() > 0)
					&& (!outputFilePath.segment(0).equals("src"))) {
				outputFilePath = outputFilePath.removeFirstSegments(1);
			}

			String baseFileName = file.getName();
			if (baseFileName.endsWith(".agent"))
				baseFileName = baseFileName.substring(0,
						baseFileName.length() - 6);
			String fullFileName = outputFilePath.removeFirstSegments(1)
					.removeLastSegments(1).removeFileExtension()
					.toPortableString().replace("/", ".")
					+ "." + baseFileName + ".html";

			String imageReference = "";
			String imageFileName = outputFilePath.removeFirstSegments(1)
					.removeLastSegments(1).removeFileExtension()
					.toPortableString().replace("/", ".")
					+ "." + baseFileName + ".png";
			IFolder docsFolder = getProject().getFolder("docs");
			IFolder imagesFolder = docsFolder.getFolder("images");
			IFile imageFile = imagesFolder.getFile(imageFileName);
			if (imageFile.exists()) {
				imageReference = "<IMG SRC=images/" + imageFileName
						+ " border=1 alt=\"Flowchart for " + file.getName()
						+ "\" title=\"Flowchart for " + file.getName()
						+ "\"></P>";
			}

			String groovyFileString = "<HTML>" + newFont + "<TITLE>"
					+ file.getName()
					+ "</TITLE><BODY>"
					+ "<STYLE TYPE=\"text/css\"><!--.centeralign {text-align:center}--></STYLE>"
					+ "<HEAD><P CLASS=\"centeralign\">"
					+ file.getName() + "</P></HEAD>" + imageReference
					+ groovyFileStringBuffer.toString() + "</BODY></HTML>";

			groovyFileString = groovyFileString.replace(Util
					.getPlatformLineDelimiter(), "</p>"
					+ Util.getPlatformLineDelimiter());
			groovyFileString = groovyFileString.replace("/**", "");
			groovyFileString = groovyFileString.replace(" */", "");
			groovyFileString = groovyFileString.replace(" *", "");
			groovyFileString = groovyFileString.replace("{", "<BLOCKQUOTE>");
			groovyFileString = groovyFileString.replace("}", "</BLOCKQUOTE>");
			groovyFileString = groovyFileString.replace("//", "");

			ByteArrayInputStream bin = new ByteArrayInputStream(
					groovyFileString.getBytes());

			IFile htmlFile = docsFolder.getFile(fullFileName);
			monitor.subTask("Build HTML: " + fullFileName);
			if (!htmlFile.exists())
				htmlFile.create(bin, true, monitor);
			else
				htmlFile.setContents(bin, true, true, monitor);

			IFile indexFile = docsFolder.getFile("index.html");
			InputStream indexStream = indexFile.getContents();
			StringBuffer indexFileStringBuffer = new StringBuffer(indexStream
					.available());
			while (indexStream.available() > 0) {
				indexFileStringBuffer.append((char) indexStream.read());
			}
			String indexFileString = indexFileStringBuffer.toString();

			if ((outputFilePath != null) && (outputFilePath.segment(0) != null)
					&& (outputFilePath.segment(0).equals("src"))) {

				if (!indexFileString.contains(newFont)) {
					indexFileString = indexFileString.replace("<html>","<html><FONT FACE=Verdana, Arial, Sans>");
				}
				if (!indexFileString.contains("model...</p>")) {
					indexFileString = indexFileString.replace("model...","model...</p>");
				}
				
				String sFileName = fullFileName.substring(0, fullFileName
						.length() - 5);
				if (!indexFileString.contains(sFileName)) {
					indexFileString = indexFileString.replace("</body>",
							"<li>The documentation for \"" + sFileName
									+ "\" can be found <a href=\""
									+ fullFileName + "\">here</a>.</li></p>"
									+ Util.getPlatformLineDelimiter()
									+ "</body>");
				}

				bin = new ByteArrayInputStream(indexFileString.getBytes());

				monitor.subTask("Build HTML Index: " + indexFile);
				if (!indexFile.exists())
					indexFile.create(bin, true, monitor);
				else
					indexFile.setContents(bin, true, true, monitor);

			}

		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e1) {
				AgentBuilderPlugin.log(e1);
			}
			monitor.done();
		}
	}

	/**
	 * Builds all flows in the given folder. Also builds recursively subfolders
	 * 
	 * @param srcFolder
	 *            the parent src folder that is sepcified in the classpath
	 * @param buildFolder
	 *            the folder which is checked for flow resources
	 * @param monitor
	 * @throws CoreException
	 */
	private void buildFlows(IContainer srcFolder, IContainer buildFolder,
			IProgressMonitor monitor) throws CoreException {
		monitor.subTask(AgentBuilderPlugin
				.getResourceString("Monitor_Builder.buildingAgents"));
		IResource[] members = buildFolder.members();

		for (int i = 0; i < members.length; i++) {
			IResource member = members[i];
			if (member instanceof IFolder)
				buildFlows(srcFolder, (IFolder) member, monitor);
			else if (member instanceof IFile && isFlowFile((IFile) member)) {
				buildFlow(srcFolder, (IFile) member, monitor);
			}
		}
		monitor.done();
	}

	/**
	 * Returns <code>true</code> if the file is a java file.
	 * 
	 * @param file
	 *            the file to check
	 * @return <code>true</code> if the file is a java file.
	 */
	public boolean isJavaFile(IFile file) {
		String extension = file.getProjectRelativePath().getFileExtension();
		return extension == null ? false : Consts.FILE_EXTENSION_GROOVY
				.equalsIgnoreCase(extension);
	}

	/**
	 * Returns true if the file is a flow file.
	 * 
	 * @param file
	 *            the file to check
	 * @return true if the file is a flow file.
	 */
	public static boolean isFlowFile(IFile file) {
		String extension = file.getProjectRelativePath().getFileExtension();
		return extension == null ? false : isFlowFileExtension(extension);
	}

	/**
	 * Returns true if the extension is a flow extension.
	 * 
	 * @param file
	 *            the file to check
	 * @return true if the file is a flow file.
	 */
	public static boolean isFlowFileExtension(String extension) {

		if (extension == null) {
			return false;
		} else {
			for (String newExtension : AgentBuilderPlugin.AGENT_BUILDER_FILE_EXTENSIONS) {
				if (newExtension.equalsIgnoreCase(extension))
					return true;
			}
			return false;
		}

	}

	/**
	 * Returns whether the project is buildable by the AgentBuilderBuilder.
	 * 
	 * @param project
	 * @return true if the AgentBuilderBuilder can build the given project
	 */
	static boolean isBuildableProject(IProject project) {
		return AgentBuilderPlugin.hasFlowchartNature(project);
	}

}
