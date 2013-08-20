/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package repast.simphony.systemdynamics.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class MDLImportWizardPage extends WizardNewFileCreationPage {

  protected FileFieldEditor editor;
  private Text classNameTxt;
  private Text packageTxt;
  private IProject project;

  public MDLImportWizardPage(String pageName, IStructuredSelection selection, IProject project) {
    super(pageName, selection);
    setTitle(pageName); // NON-NLS-1
    this.project = project;
    setDescription("Translate an MDL file into a Repast Simphony System Dynamics Diagram"); // NON-NLS-1
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls
   * (org.eclipse.swt.widgets.Composite)
   */
  protected void createAdvancedControls(Composite parent) {
    Composite fileSelectionArea = new Composite(parent, SWT.NONE);
    GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
    fileSelectionArea.setLayoutData(fileSelectionData);

    GridLayout fileSelectionLayout = new GridLayout();
    fileSelectionLayout.numColumns = 3;
    fileSelectionLayout.makeColumnsEqualWidth = false;
    fileSelectionLayout.marginWidth = 0;
    fileSelectionLayout.marginHeight = 0;
    fileSelectionArea.setLayout(fileSelectionLayout);

    editor = new FileFieldEditor("fileSelect", "Select MDL File: ", fileSelectionArea); // NON-NLS-1
                                                                                        // //NON-NLS-2
    editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        IPath path = new Path(MDLImportWizardPage.this.editor.getStringValue());
        setFileName(path.removeFileExtension().lastSegment() + ".rsd");
      }
    });
    String[] extensions = new String[] { "*.mdl", "*.MDL" }; // NON-NLS-1
    editor.setFileExtensions(extensions);
    fileSelectionArea.moveAbove(null);

    Group grpStatechartProperties = new Group(parent, SWT.NONE);
    grpStatechartProperties.setText("Code Properties");
    grpStatechartProperties.setLayout(new GridLayout(2, false));
    grpStatechartProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

    Label lblClassName = new Label(grpStatechartProperties, SWT.NONE);
    lblClassName.setToolTipText("The class name of the state chart");
    lblClassName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblClassName.setText("Class Name:");

    Validator validator = new Validator();
    classNameTxt = new Text(grpStatechartProperties, SWT.BORDER);
    classNameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    classNameTxt.addModifyListener(validator);

    Label lblPackage = new Label(grpStatechartProperties, SWT.NONE);
    lblPackage.setToolTipText("The package of the statechart");
    lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblPackage.setText("Package:");

    packageTxt = new Text(grpStatechartProperties, SWT.BORDER);
    packageTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    packageTxt.addModifyListener(validator);
  }

  protected String getClassName() {
    return classNameTxt == null ? "" : classNameTxt.getText().trim();
  }

  protected String getPackage() {
    return packageTxt == null ? "" : packageTxt.getText().trim();
  }

  private String[] getSourceComplianceLevels(IJavaProject javaProject) {

    if (javaProject != null) {
      return new String[] { javaProject.getOption(JavaCore.COMPILER_SOURCE, true),
          javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true) };
    }

    return new String[] { JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE) };
  }

  private IStatus validateJavaTypeName(String text, IJavaProject project) {
    String[] sourceComplianceLevels = getSourceComplianceLevels(project);
    return JavaConventions.validateJavaTypeName(text, sourceComplianceLevels[0],
        sourceComplianceLevels[1]);
  }
  
  private IStatus validatePackageName(String text, IJavaProject project) {
    String[] sourceComplianceLevels = getSourceComplianceLevels(project);
    return JavaConventions.validatePackageName(text, sourceComplianceLevels[0],
        sourceComplianceLevels[1]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
   */
  @Override
  protected boolean validatePage() {
    if (super.validatePage()) {
      IJavaProject javaProject = null;
      if (project != null)
        javaProject = JavaCore.create(project);

      String val = getClassName();
      if (val.length() == 0) {
        setErrorMessage("Class name is blank.");
        return false;
      } else {
        IStatus status = validateJavaTypeName(val, javaProject);
        if (!status.isOK()) {
          setErrorMessage("Class name is not valid. '" + val + "' is not a class identifier.");
          return false;
        }
      }

      val = getPackage();
      if (val.length() == 0) {
        setErrorMessage("Package is blank.");
        return false;
      } else {
        IStatus status = validatePackageName(val, javaProject);
        if (!status.isOK()) {
          setErrorMessage("Package is not valid. '" + val + "' is not a valid package identifier.");
          return false;
        }
      }

      String fqn = getPackage() + "." + getClassName();
      if (javaProject != null) {
        IFile file = javaProject.getProject().getFile("src-gen/" + fqn.replace(".", "/") + ".java");
        if (file.exists()) {
          setErrorMessage("System Dynamics class already exists in the specified package. Change the chart name or the package.");
          return false;
        }
      }
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createLinkTarget()
   */
  protected void createLinkTarget() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
   */
  protected InputStream getInitialContents() {
    try {
      return new FileInputStream(new File(editor.getStringValue()));
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getNewFileLabel()
   */
  protected String getNewFileLabel() {
    return "New RSD File Name:"; // NON-NLS-1
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
   */
  protected IStatus validateLinkedResource() {
    return new Status(IStatus.OK, "repast.simphony.systemdynamics.diagram", IStatus.OK, "", null); // NON-NLS-1
                                                                                                   // //NON-NLS-2
  }

  private class Validator implements ModifyListener {

    @Override
    public void modifyText(ModifyEvent e) {
      setPageComplete(validatePage());
    }
  }
}
