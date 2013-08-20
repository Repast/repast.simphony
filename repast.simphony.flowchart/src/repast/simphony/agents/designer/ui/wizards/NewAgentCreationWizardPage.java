/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.ui.wizards;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.BindingHandler;

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
public class NewAgentCreationWizardPage extends WizardNewFileCreationPage {

	private IPath newFilePath;
	private String templateSourceFile = null;
	private String parentClassName;
	private String interfaces;
	private String imports;

	/**
	 * TODO
	 * 
	 * @param pageName
	 * @param selection
	 */
	public NewAgentCreationWizardPage(String title,
			IStructuredSelection selection) {
		super(title, selection);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setPageComplete(this.validatePage());
	}

	/**
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createFileHandle(org.eclipse.core.runtime.IPath)
	 */
	@Override
	protected IFile createFileHandle(IPath filePath) {
		newFilePath = filePath;
		return super.createFileHandle(filePath);
	}

	/**
	 * Returns the Input stream to the serialized content of a new
	 * AgentModelBind object. Sets the default flow name to the flowFile name
	 * without the extension
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	@Override
	protected InputStream getInitialContents() {
		try {
			String fileName = newFilePath.lastSegment();
			String flowName = fileName.substring(0, fileName.length()
					- newFilePath.getFileExtension().length() - 1);

			AgentModelBind agentModelBind;
			if ((templateSourceFile == null) || (templateSourceFile.equals(""))) {
				agentModelBind = new AgentModelBind(flowName);
				agentModelBind.setParentClassName(parentClassName);
				agentModelBind.setInterfaces(interfaces);
				agentModelBind.setImports(imports);
			} else {
				try {
					agentModelBind = BindingHandler.getInstance()
							.loadFlowModel(
									new File(AgentBuilderPlugin
											.getPluginInstallationDirectory()
											+ this.templateSourceFile));
					agentModelBind.setFlowName(flowName);
					agentModelBind.setParentClassName(this.parentClassName);
					agentModelBind.setInterfaces(this.interfaces);
					agentModelBind.setImports(this.imports);
				} catch (Exception e) {
					agentModelBind = new AgentModelBind(flowName);
					agentModelBind.setParentClassName(parentClassName);
					agentModelBind.setInterfaces(interfaces);
					agentModelBind.setImports(imports);
				}
			}
			return BindingHandler.getInstance().saveFlowModel(agentModelBind);
		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
			return null;
		}
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	boolean finish() {
		IFile newFile = this.createNewFile();
		if (newFile == null)
			return false; // ie.- creation was unsuccessful

		// Since the flowFile resource was created fine, open it for editing
		// if requested by the user
		try {
			IWorkbenchWindow dwindow = AgentBuilderPlugin.getDefault()
					.getWorkbench().getActiveWorkbenchWindow();
			if (dwindow != null) {
				IWorkbenchPage page = dwindow.getActivePage();
				if (page != null) {
					IDE.openEditor(page, newFile, true);
				}
			}
		} catch (org.eclipse.ui.PartInitException e) {
			AgentBuilderPlugin.log(e);
			return false;
		}

		return true;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public String getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

	public String getTemplateSourceFile() {
		return templateSourceFile;
	}

	public void setTemplateSourceFile(String templateSourceFile) {
		this.templateSourceFile = templateSourceFile;
	}
}
