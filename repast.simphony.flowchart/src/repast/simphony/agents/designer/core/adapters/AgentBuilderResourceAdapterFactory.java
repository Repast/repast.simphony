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

package repast.simphony.agents.designer.core.adapters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

import repast.simphony.agents.designer.builder.AgentBuilderBuilder;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.core.AgentBuilderProject;
import repast.simphony.agents.designer.core.elements.AgentBuilderFile;
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
@SuppressWarnings("unchecked")
public class AgentBuilderResourceAdapterFactory implements IAdapterFactory {

	/**
	 * TODO
	 * 
	 */
	public AgentBuilderResourceAdapterFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (AgentBuilderFile.class.equals(adapterType))
			return createFlow4JFileResource((IFile) adaptableObject);

		if (AgentBuilderProject.class.equals(adapterType))
			return createFlow4JProjectResource((IProject) adaptableObject);

		if (IAgentBuilderElement.class.equals(adapterType)) {
			if (adaptableObject instanceof IFile)
				return createFlow4JFileResource((IFile) adaptableObject);

			if (adaptableObject instanceof IProject)
				return createFlow4JProjectResource((IProject) adaptableObject);
		}

		return null;
	}

	/**
	 * TODO
	 * 
	 * @param file
	 * @return
	 */
	private AgentBuilderFile createFlow4JFileResource(IFile file) {
		if (AgentBuilderBuilder.isFlowFile(file)) {
			return new AgentBuilderFile(file);
		} else {
			return null;
		}
	}

	/**
	 * TODO
	 * 
	 * @param aProject
	 * @return
	 */
	private AgentBuilderProject createFlow4JProjectResource(IProject aProject) {
		try {
			if (aProject.hasNature(AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID)) {
				AgentBuilderProject project = new AgentBuilderProject();
				project.setProject(aProject);
				return project;
			}
		} catch (CoreException e) {
			System.err
					.println("Exception occurred in createFlow4JProjectResource: "
							+ e.toString());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		Class[] classes = { IAgentBuilderElement.class, AgentBuilderFile.class,
				AgentBuilderProject.class };
		return classes;
	}

}
