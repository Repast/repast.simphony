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

package repast.simphony.agents.designer.core;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.JavaCore;

import repast.simphony.agents.designer.core.elements.IAgentBuilderElement;

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
public class AgentBuilderProject implements IProjectNature,
		IAgentBuilderElement {

	public static String PROJ_PROP_CREATE_AGENT_REPOSITORY_CLASS = "createFlowRepositoryClass"; //$NON-NLS-1$
	public static String PROJ_PROP_FLOW_REPOSITORY_CLASS_PATH = "flowRepositoryClassPath"; //$NON-NLS-1$

	private IProject project;

	/**
	 * Creates a new project.
	 */
	public AgentBuilderProject() {
		super();
	}

	/**
	 * @see repast.simphony.agents.designer.core.elements.IAgentBuilderElement#getUnderlyingResource()
	 */
	public IResource getUnderlyingResource() {
		return project;
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException {
		// adding the java nature also adds the java builder.
		// but because the natureManager works with HashSets sometimes
		// javabuilder stands in front of the flow builder
		// so we must ensure that the flow builder is in front of the
		// java builder
		removeFromBuildSpec(JavaCore.BUILDER_ID);
		addToBuildSpec(JavaCore.BUILDER_ID);
		// addToBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_REPOSITORY_BUILDER_ID);
		addToBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_BUILDER_ID);
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {
		removeFromBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_BUILDER_ID);
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * Adds a builder to the build spec for the given project.
	 * 
	 * @param builderID
	 * @throws CoreException
	 */
	public void addToBuildSpec(String builderID) throws CoreException {

		IProjectDescription description = getProject().getDescription();
		ICommand javaCommand = getFlowBuilderCommand(description);

		if (javaCommand == null) {

			// Add a command to the build spec
			ICommand command = description.newCommand();
			command.setBuilderName(builderID);
			setFlowBuilderCommand(description, command);
		}
	}

	/**
	 * Find the specific Flow command amongst the build spec of a given
	 * description
	 */
	private ICommand getFlowBuilderCommand(IProjectDescription description)
			throws CoreException {

		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(
					AgentBuilderPlugin.AGENT_BUILDER_BUILDER_ID)) {
				return commands[i];
			}
		}
		return null;
	}

	/**
	 * Update the Flow command in the build spec (replace existing one if
	 * present, add one first if none).
	 */
	private void setFlowBuilderCommand(IProjectDescription description,
			ICommand newCommand) throws CoreException {

		ICommand[] oldCommands = description.getBuildSpec();
		ICommand oldCommand = getFlowBuilderCommand(description);
		ICommand[] newCommands;

		if (oldCommand == null) {
			// Add a Flow build spec before other builders (1FWJK7I)
			newCommands = new ICommand[oldCommands.length + 1];
			System
					.arraycopy(oldCommands, 0, newCommands, 1,
							oldCommands.length);
			newCommands[0] = newCommand;
		} else {
			for (int i = 0, max = oldCommands.length; i < max; i++) {
				if (oldCommands[i] == oldCommand) {
					oldCommands[i] = newCommand;
					break;
				}
			}
			newCommands = oldCommands;
		}

		// Commit the spec change into the project
		description.setBuildSpec(newCommands);
		getProject().setDescription(description, null);
	}

	/**
	 * Removes the given builder from the build spec for the given project.
	 */
	public void removeFromBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(builderID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				getProject().setDescription(description, null);
				return;
			}
		}
	}

	/**
	 * TODO
	 * 
	 * @param builderID
	 * @return
	 * @throws CoreException
	 */
	public boolean containsBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
			if (commands[i].getBuilderName().equals(builderID))
				return true;

		return false;
	}

	/**
	 * TODO
	 * 
	 * @param key
	 * @return
	 */
	public String readProperty(String key) {
		String result = "";
		try {
			result = AgentBuilderPlugin.getJavaProject(getProject())
					.getCorrespondingResource().getPersistentProperty(
							new QualifiedName("AgentBuilderProject", key));
		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
		}

		return result;
	}

	public void writeProperty(String key, String value) {
		try {
			AgentBuilderPlugin.getJavaProject(getProject())
					.getCorrespondingResource().setPersistentProperty(
							new QualifiedName("AgentBuilderProject", key),
							value);
		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
		}
	}

	public String getFlowReposClassPath() {
		return readProperty(AgentBuilderProject.PROJ_PROP_FLOW_REPOSITORY_CLASS_PATH);
	}

	public void updateFlowReposClassPath(String value) {
		writeProperty(AgentBuilderProject.PROJ_PROP_FLOW_REPOSITORY_CLASS_PATH,
				value);
	}

	public boolean isCreateFlowRepositoryClass() {
		return "true"
				.equals(readProperty(AgentBuilderProject.PROJ_PROP_CREATE_AGENT_REPOSITORY_CLASS));
	}

	public void updateCreateFlowRepositoryClass(boolean value) {
		writeProperty(
				AgentBuilderProject.PROJ_PROP_CREATE_AGENT_REPOSITORY_CLASS,
				new Boolean(value).toString());
		try {
			if (value) {
				if (!containsBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_REPOSITORY_BUILDER_ID)) {
					removeFromBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_BUILDER_ID);
					addToBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_REPOSITORY_BUILDER_ID);
					addToBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_BUILDER_ID);
				}
			} else
				removeFromBuildSpec(AgentBuilderPlugin.AGENT_BUILDER_REPOSITORY_BUILDER_ID);
		} catch (CoreException e) {
			AgentBuilderPlugin.log(e);
		}
	}

}
