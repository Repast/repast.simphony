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
 * Class NewProjectCreationWizard TODO
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
    mainPage.setTitle(RepastSimphonyPlugin.getInstance().getResourceString(
        "WizardPage_NewProject.pageTitle"));
    mainPage.setDescription(RepastSimphonyPlugin.getInstance().getResourceString(
        "WizardPage_NewProject.pageDescription"));
    this.addPage(mainPage);

    // the java wizard page
    javaPage = new NewProjectCreationWizardPageJ(mainPage);
    this.addPage(javaPage);

    super.addPages();

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   */
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

      // String scenarioDirectory = this.contextPage.getModelPackage()
      // + ".rs";

      // TODO the inputs on the legacy score context page are just replaced
      // with the project name here. If we want additional customization
      // for model and package names, new gui elements need to be created.

      // TODO make sure packagename string is formatted properly (no spaces).

      String scenarioDirectory = mainPage.getProjectName() + ".rs";

      String mainDataSourcePluginDirectory = RepastSimphonyPlugin.getInstance()
          .getPluginInstallationDirectory();

      
      IFolder srcFolder = javaProject.getProject().getFolder(srcPath.removeFirstSegments(1));
      // IFolder packageFolder = srcFolder.getFolder(this.contextPage
      // .getPackage());
      String packageName = mainPage.getProjectName().replace(" ", "_");
      packageName = packageName.substring(0, 1).toLowerCase() + packageName.substring(1, packageName.length());
      IFolder packageFolder = srcFolder.getFolder(packageName);
      packageFolder.create(true, true, monitor);
      
      String[][] variableMap = { { "%MODEL_NAME%", mainPage.getProjectName() },
          { "%PROJECT_NAME%", javaProject.getElementName() },
          { "%SCENARIO_DIRECTORY%", scenarioDirectory },
          { "%PACKAGE%", packageName },
          { "%REPAST_SIMPHONY_INSTALL_BUILDER_PLUGIN_DIRECTORY%", mainDataSourcePluginDirectory } };

      
      IFolder newFolder = srcFolder.getFolder("../docs");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("docs/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("docs/index.html", newFolder, "index.html",
          variableMap, monitor);

      // for distributed batch (see SIM-459)
      newFolder = srcFolder.getFolder("../output");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);

      newFolder = srcFolder.getFolder("../freezedried_data");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("freezedried_data/ReadMe.txt", newFolder,
          "ReadMe.txt", variableMap, monitor);

      newFolder = srcFolder.getFolder("../icons");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("icons/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("icons/model.png", newFolder, "model.png",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("icons/model.bmp", newFolder, "model.bmp",
          variableMap, monitor);
      
      Utilities.createModelInstallerFiles(srcFolder, monitor, variableMap);
      
      newFolder = srcFolder.getFolder("../repast-licenses");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/apache-license.txt", newFolder,
          "apache-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/asm-license.txt", newFolder,
          "asm-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/axion-license.txt", newFolder,
          "axion-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/binding-license.txt", newFolder,
          "binding-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/common-public-license.txt",
          newFolder, "common-public-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/concurrent-license.pdf", newFolder,
          "concurrent-license.pdf", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/CPL.txt", newFolder, "CPL.txt",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/forms-license.txt", newFolder,
          "forms-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/geotools-license.txt", newFolder,
          "geotools-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/groovy-license.txt", newFolder,
          "groovy-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/hsqldb-license.txt", newFolder,
          "hsqldb-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation(
          "repast-licenses/jakarta-commons-collections-license.txt", newFolder,
          "jakarta-commons-collections-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jaxen-license.txt", newFolder,
          "jaxen-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jh-license.txt", newFolder,
          "jh-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jide-oss-license.txt", newFolder,
          "jide-oss-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jmatlink-license.txt", newFolder,
          "jmatlink-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jmf-license.txt", newFolder,
          "jmf-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jmock-license.txt", newFolder,
          "jmock-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jscience-license.txt", newFolder,
          "jscience-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jsp-servlet-api-license.txt",
          newFolder, "jsp-servlet-api-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jts-license.txt", newFolder,
          "jts-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/jung-license.txt", newFolder,
          "jung-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/lgpl-2.1.txt", newFolder,
          "lgpl-2.1.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/LICENSE-jgoodies.txt", newFolder,
          "LICENSE-jgoodies.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/log4j-license.txt", newFolder,
          "log4j-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation(
          "repast-licenses/mitre-relogo-import-wizard-license.txt", newFolder,
          "mitre-relogo-import-wizard-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/MPL-license.txt", newFolder,
          "MPL-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/msql-connector-license.txt",
          newFolder, "msql-connector-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/mx4j-license.txt", newFolder,
          "mx4j-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/openforecast-license.txt",
          newFolder, "openforecast-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/piccolo-license.txt", newFolder,
          "piccolo-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/proactive-license.txt", newFolder,
          "proactive-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/repast-license.txt", newFolder,
          "repast-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/saxpath-license.txt", newFolder,
          "saxpath-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/swingx-license.txt", newFolder,
          "swingx-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/table-layout-license.txt",
          newFolder, "table-layout-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/violinstrings-license.txt",
          newFolder, "violinstrings-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/wizard-license.txt", newFolder,
          "wizard-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/xpp3-license.txt", newFolder,
          "xpp3-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/xstream-license.txt", newFolder,
          "xstream-license.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/license_apache.txt", newFolder,
          "license_apache.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/license_apache11.txt", newFolder,
          "license_apache11.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/license_flow4j.txt", newFolder,
          "license_flow4j.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/license_flow4J-eclipse.txt",
          newFolder, "license_flow4J-eclipse.txt", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("repast-licenses/license_xstream.txt", newFolder,
          "license_xstream.txt", variableMap, monitor);

      newFolder = srcFolder.getFolder("../launchers");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("launchers/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);

      RSProjectConfigurator configurator = new RSProjectConfigurator();
      configurator.createLaunchConfigurations(javaProject, newFolder, scenarioDirectory);

      newFolder = srcFolder.getFolder("../batch");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("batch/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("batch/batch_params.xml", newFolder,
          "batch_params.xml", variableMap, monitor);

      newFolder = srcFolder.getFolder("../integration");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("integration/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);

      newFolder = srcFolder.getFolder("../lib");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("lib/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);

      newFolder = srcFolder.getFolder("../data");
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("data/ReadMe.txt", newFolder, "ReadMe.txt",
          variableMap, monitor);

      newFolder = srcFolder.getFolder("../" + scenarioDirectory);
      if (!newFolder.exists())
        newFolder.create(true, true, monitor);
      Utilities.copyFileFromPluginInstallation("package.rs/scenario.xml", newFolder,
          "scenario.xml", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("package.rs/user_path.xml", newFolder,
          "user_path.xml", variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("package.rs/context.xml", newFolder, "context.xml",
          variableMap, monitor);
      Utilities.copyFileFromPluginInstallation("package.rs/parameters.xml", newFolder,
          "parameters.xml", variableMap, monitor);

      newFolder = srcFolder.getFolder("../license.txt");
      Utilities.copyFileFromPluginInstallation("license.txt", newFolder, "", variableMap, monitor);

      newFolder = srcFolder.getFolder("../MessageCenter.log4j.properties");
      Utilities.copyFileFromPluginInstallation("MessageCenter.log4j.properties", newFolder, "",
          variableMap, monitor);

      newFolder = srcFolder.getFolder("../model_description.txt");
      Utilities.copyFileFromPluginInstallation("model_description.txt", newFolder, "", variableMap,
          monitor);

      
      configurator.configureNewProject(javaProject, new SubProgressMonitor(monitor, 1));
      
      try {
        selectAndReveal(javaProject.findPackageFragment(packageFolder.getFullPath())
            .getCorrespondingResource(), this.getWorkbench().getActiveWorkbenchWindow());
      } catch (Exception e) {
      }
     

    }
    

    monitor.done();
  }
  
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   */
  public void resetProjectName() {

    // TODO wtf?
    // if ((!this.showScoreFileContainerSelector)
    // && (this.contextPage != null)) {
    // this.contextPage.resetProjectName(this.getDefaultBaseDir());
    // }

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
