/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif?s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North?s Modifications are Copyright 2007 Under
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

package repast.simphony.eclipse.ide;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import repast.simphony.eclipse.RepastSimphonyPlugin;

/**
 * Class NewProjectCreationWizardPageJ 
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif?s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class NewProjectCreationWizardPageJ extends JavaCapabilityConfigurationPage {

  private WizardNewProjectCreationPage fMainPage;

  private IPath fCurrProjectLocation;
  public IProject fCurrProject;
  protected boolean fCanRemoveContent;

  /**
   * Constructor for NewProjectCreationWizardPage.
   */
  public NewProjectCreationWizardPageJ(WizardNewProjectCreationPage mainPage) {
    super();
    fMainPage = mainPage;
    fCurrProjectLocation = null;
    fCurrProject = null;
    fCanRemoveContent = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      removeProject();
      changeToNewProject();
      if ((this.getWizard() != null) && (this.getWizard() instanceof NewProjectCreationWizard)) {
        NewProjectCreationWizard parentWizard = ((NewProjectCreationWizard) this.getWizard());
        parentWizard.resetProjectName();
      }
    } else {
      // removeProject();
    }
    super.setVisible(visible);
  }

  /**
   * TODO
   * 
   */
  private void changeToNewProject() {
    IProject newProjectHandle = fMainPage.getProjectHandle();
    IPath newProjectLocation = fMainPage.getLocationPath();

    if (fMainPage.useDefaults()) {
      fCanRemoveContent = !newProjectLocation.append(fMainPage.getProjectName()).toFile().exists();
    } else {
      fCanRemoveContent = !newProjectLocation.toFile().exists();
    }

    final boolean initialize = !(newProjectHandle.equals(fCurrProject) && newProjectLocation
        .equals(fCurrProjectLocation));

    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor) throws InvocationTargetException,
          InterruptedException {
        try {
          updateProject(initialize, monitor);
        } catch (CoreException e) {
          throw new InvocationTargetException(e);
        }
      }
    };

    try {
      getContainer().run(false, true, op);
    } catch (InvocationTargetException e) {
      RepastSimphonyPlugin.getInstance().message(
          "An error occurred while creating project. Check log for details.");
      RepastSimphonyPlugin.getInstance().log(e);
    } catch (InterruptedException e) {
      // cancel pressed
    }
  }

  /**
   * TODO
   * 
   * @param initialize
   * @param monitor
   * @throws CoreException
   * @throws InterruptedException
   */
  protected void updateProject(boolean initialize, IProgressMonitor monitor) throws CoreException,
      InterruptedException {
    fCurrProject = fMainPage.getProjectHandle();
    fCurrProjectLocation = fMainPage.getLocationPath();
    boolean noProgressMonitor = !initialize && fCanRemoveContent;

    if (monitor == null || noProgressMonitor) {
      monitor = new NullProgressMonitor();
    }
    try {
      monitor.beginTask("Creating project and examining existing resources...", 2); //$NON-NLS-1$

      createProject(fCurrProject, fCurrProjectLocation, new SubProgressMonitor(monitor, 1));
      if (initialize) {

        IJavaProject javaProject = JavaCore.create(fCurrProject);
        init(javaProject, null, null, false);

      }
      monitor.worked(1);
    } finally {
      monitor.done();
    }
  }

  /**
   * Called from the wizard on finish.
   */
  public void performFinish(IProgressMonitor monitor) throws CoreException, InterruptedException {
    try {
      if (fCurrProject == null) {
        updateProject(true, new SubProgressMonitor(monitor, 1));
      }
      configureJavaProject(new SubProgressMonitor(monitor, 2));

    } finally {
      fCurrProject = null;
    }
  }

  private void removeProject() {

    if (fCurrProject == null || !fCurrProject.exists()) {
      return;
    }

    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor) throws InvocationTargetException,
          InterruptedException {
        boolean noProgressMonitor = Platform.getLocation().equals(fCurrProjectLocation);
        if (monitor == null || noProgressMonitor) {
          monitor = new NullProgressMonitor();
        }
        monitor.beginTask("Removing project...", 3); //$NON-NLS-1$

        try {
          fCurrProject.delete(fCanRemoveContent, false, monitor);
        } catch (CoreException e) {
          throw new InvocationTargetException(e);
        } finally {
          monitor.done();
          fCurrProject = null;
          fCanRemoveContent = false;
        }
      }
    };

    try {
      getContainer().run(false, true, op);
    } catch (InvocationTargetException e) {
      RepastSimphonyPlugin.getInstance().message("An error occurred while removing a temporary project"); //$NON-NLS-1$
      RepastSimphonyPlugin.getInstance().log(e);
    } catch (InterruptedException e) {
      // cancel pressed
    }
  }

  /**
   * Called from the wizard on cancel.
   */
  public void performCancel() {
    removeProject();
  }

}
