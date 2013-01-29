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

package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.ui.AgentBuilderPluginImages;

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
public class NewAgentCreationWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;
	private NewAgentCreationWizardPage wizardPage;
	private String title;
	private String description;
	private String fileName;
	private ImageDescriptor imageDescriptor;
	private String templateSourceFile = null;
	private String parentClassName = "";
	private String interfaces = "";
	private String imports = "";

	/**
	 * Constructor
	 * 
	 */
	public NewAgentCreationWizard() {
		super();
		this.setTitle(AgentBuilderPlugin
				.getResourceString("WizardPage_NewAgent.pageName"));
		this.setFileName(AgentBuilderPlugin
				.getResourceString("WizardPage_NewAgent.fileName"));
		this.setDescription(AgentBuilderPlugin
				.getResourceString("WizardPage_NewAgent.pageDescription"));
		this.setImageDescriptor(AgentBuilderPluginImages.DESC_WIZBAN_NewAgent);
	}

	/**
	 * Initializes the Wizard.
	 * 
	 * @param workbench
	 * @param selection
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		wizardPage = new NewAgentCreationWizardPage(this.getTitle(), selection);
		wizardPage.setTitle(this.getTitle());
		wizardPage.setDescription(this.getDescription());
		wizardPage.setFileName(this.getFileName());
		wizardPage.setImageDescriptor(this.getImageDescriptor());
		wizardPage.setTemplateSourceFile(templateSourceFile);
		wizardPage.setParentClassName(parentClassName);
		wizardPage.setInterfaces(interfaces);
		wizardPage.setImports(imports);
		this.addPage(wizardPage);
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return wizardPage.finish();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	public void setImageDescriptor(ImageDescriptor imageDescriptor) {
		this.imageDescriptor = imageDescriptor;
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
