/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif?s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North?s Modifications are Copyright 2007 Under
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

package repast.simphony.eclipse.ide;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import repast.simphony.eclipse.RSProjectConfigurator;
import repast.simphony.eclipse.RepastSimphonyPlugin;
import repast.simphony.eclipse.util.Utilities;
import repast.simphony.eclipse.util.WorkspaceRunnableAdapter;

/**
 * 
 * Class NewProjectCreationWizard
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif?s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
public class NewProjectCreationWizard extends BasicNewResourceWizard implements INewWizard {

  public WizardNewProjectCreationPage mainPage;
  public NewProjectCreationWizardPageJ javaPage;
  private IConfigurationElement configElement;
  public IProject newProject;
  public boolean configureGroovyAndVisualEditing = true;

  /**
   * Creates the project creation wizard.
   * 
   */
  public NewProjectCreationWizard() {
    super();
    // this.showScoreFileContainerSelector = false;
    setWindowTitle(RepastSimphonyPlugin.getInstance().getResourceString(
        "Wizard_NewProject.windowTitle"));
  }

  public void init(IWorkbench workbench, IStructuredSelection selection) {
    super.init(workbench, selection);
    setWindowTitle(RepastSimphonyPlugin.getInstance().getResourceString(
        "Wizard_NewProject.windowTitle"));
  }

  /**
   * Adds the wizard pages
   * 
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages() {

    mainPage = new WizardNewProjectCreationPage("WizardPage_NewProject");
    mainPage.setTitle(RepastSimphonyPlugin.getInstance().getResourceString("WizardPage_NewProject.pageTitle"));
    mainPage.setDescription(RepastSimphonyPlugin.getInstance().getResourceString("WizardPage_NewProject.pageDescription"));
    this.addPage(mainPage);

    // the java wizard page
    javaPage = new NewProjectCreationWizardPageJ(mainPage);
    this.addPage(javaPage);

    super.addPages();

  }

  public String getDefaultBaseDir() {
    return "../" + mainPage.getProjectName();
  }

  /*
   * Finishes the Wizard and creates the project.
   * 
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish() {
    IWorkspaceRunnable op = new IWorkspaceRunnable() {
      // IRunnableWithProgress op= new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor) throws /*
                                                        * InvocationTargetException,
                                                        * InterruptedException,
                                                        */CoreException, OperationCanceledException {
        try {
          // TODO replace contextPage with new or delete this equivalent
          // if ((contextPage) != null
          // && (contextPage.getFullyConfigureField() != null)) {
          // configureGroovyAndVisualEditing = contextPage
          // .getFullyConfigureField().getSelection();
          // }
          finishPage(monitor);
          // NewProjectCreationWizard.super.performFinish();
          monitor.done();
        } catch (InterruptedException e) {
          throw new OperationCanceledException(e.getMessage());
        } catch (CoreException e) {
          RepastSimphonyPlugin.getInstance().log(e);
        }
      }
    };
    try {
      getContainer().run(false, true, new WorkspaceRunnableAdapter(op));
    } catch (InvocationTargetException e) {
      RepastSimphonyPlugin.getInstance().log(e);
      RepastSimphonyPlugin.getInstance().message("Creation of element failed.");
      return false;
    } catch (InterruptedException e) {
      RepastSimphonyPlugin.getInstance().log(e);
      RepastSimphonyPlugin.getInstance().message("Creation of element failed.");
      return false;
    }
    return true;
  }

  /**
   * Creates the java projects and add the Repast Simphony Nature
   * 
   * @param monitor
   * @throws InterruptedException
   * @throws CoreException
   */
  protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
    monitor.beginTask("", 10); //$NON-NLS-1$
    javaPage.performFinish(monitor);

    IJavaProject javaProject = javaPage.getJavaProject();
    BasicNewProjectResourceWizard.updatePerspective(configElement);

    IClasspathEntry list[] = javaProject.getRawClasspath();
    IPath srcPath = null;
    for (IClasspathEntry entry : list) {
      if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
        srcPath = entry.getPath();
      }
    }
    
    if (srcPath != null) {
      String scenarioDirectory = mainPage.getProjectName() + ".rs";

      String mainDataSourcePluginDirectory = RepastSimphonyPlugin.getInstance()
          .getPluginInstallationDirectory();
      
      IFolder srcFolder = javaProject.getProject().getFolder(srcPath.removeFirstSegments(1));

      // make sure packagename string is formatted properly (no spaces).
      String packageName = mainPage.getProjectName().replace(" ", "_");
      packageName = packageName.substring(0, 1).toLowerCase() + packageName.substring(1, packageName.length());
      IFolder packageFolder = srcFolder.getFolder(packageName);
      packageFolder.create(true, true, monitor);
      
      // Copy all of the contents from the r.s.eclipse/setupfiles into the user new project.
      // Many of the files have %variable% that will be replaced with e.g. the project name
      String[][] variableMap = { { "%MODEL_NAME%", mainPage.getProjectName() },
          { "%PROJECT_NAME%", javaProject.getElementName() },
          { "%SCENARIO_DIRECTORY%", scenarioDirectory },
          { "%PACKAGE%", packageName },
          { "%REPAST_SIMPHONY_INSTALL_BUILDER_PLUGIN_DIRECTORY%", mainDataSourcePluginDirectory } };

      IFolder newFolder = srcFolder.getFolder("../docs");
      Utilities.copyFolderFromPluginInstallation("docs", newFolder,  variableMap, monitor);

      // for distributed batch (see SIM-459)
      newFolder = srcFolder.getFolder("../output");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);

      newFolder = srcFolder.getFolder("../freezedried_data");
      Utilities.copyFolderFromPluginInstallation("freezedried_data", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../icons");
      Utilities.copyFolderFromPluginInstallation("icons", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../installer");
      Utilities.copyFolderFromPluginInstallation("installer", newFolder,  variableMap, monitor);
      
      newFolder = srcFolder.getFolder("../repast-licenses");
      Utilities.copyFolderFromPluginInstallation("repast-licenses", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../launchers");
      Utilities.copyFolderFromPluginInstallation("launchers", newFolder,  variableMap, monitor);

      RSProjectConfigurator configurator = new RSProjectConfigurator();
      configurator.createLaunchConfigurations(javaProject, newFolder, scenarioDirectory);

      newFolder = srcFolder.getFolder("../batch");
      Utilities.copyFolderFromPluginInstallation("batch", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../integration");
      Utilities.copyFolderFromPluginInstallation("integration", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../lib");
      Utilities.copyFolderFromPluginInstallation("lib", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../data");
      Utilities.copyFolderFromPluginInstallation("data", newFolder,  variableMap, monitor);

      // Copy the RS scenario elements
      newFolder = srcFolder.getFolder("../" + scenarioDirectory);
      Utilities.copyFolderFromPluginInstallation("package.rs", newFolder,  variableMap, monitor);

      newFolder = srcFolder.getFolder("../license.txt");
      Utilities.copyFileFromPluginInstallation("license.txt", newFolder, "", variableMap, monitor);

      newFolder = srcFolder.getFolder("../MessageCenter.log4j.properties");
      Utilities.copyFileFromPluginInstallation("MessageCenter.log4j.properties", newFolder, "",  variableMap, monitor);

      newFolder = srcFolder.getFolder("../model_description.txt");
      Utilities.copyFileFromPluginInstallation("model_description.txt", newFolder, "", variableMap,  monitor);

      configurator.configureNewProject(javaProject, new SubProgressMonitor(monitor, 1));
      
      try {
        selectAndReveal(javaProject.findPackageFragment(packageFolder.getFullPath())
            .getCorrespondingResource(), this.getWorkbench().getActiveWorkbenchWindow());
      } catch (Exception e) {
      }
    }
    
    monitor.done();
  }
  
  public void resetProjectName() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see IWizard#performCancel()
   */
  public boolean performCancel() {
    javaPage.performCancel();
    return super.performCancel();
  }

  public void setWindowTitle(String title) {
    super.setWindowTitle(title);
  }

}
