package repast.simphony.systemdynamics.part;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;

public class SystemDynamicsCreationWizardPage0 extends WizardNewFileCreationPage implements Listener {

  private Text classNameTxt;
  private Text packageTxt;

  private IProject project;

  private String file;
  private Text fileTxt;
  @SuppressWarnings("restriction")
  private ResourceAndContainerGroup resourceGroup;
  private IStructuredSelection currentSelection;
  private IPath initialContainerFullPath;
  
  /**
   * 
   * @param pageName
   * @param project
   *          may be null
   */
  protected SystemDynamicsCreationWizardPage0(String pageName, IStructuredSelection selection,
      IProject project, String file) {
    super(pageName, selection);
    this.project = project;
    this.file = file;
    this.currentSelection = selection;
  }

  @SuppressWarnings("restriction")
  protected String getNewFileLabel() {
    return IDEWorkbenchMessages.WizardNewFileCreationPage_fileLabel;
  }

  /**
   * The <code>WizardNewFileCreationPage</code> implementation of this
   * <code>Listener</code> method handles all events and enablements for
   * controls on this page. Subclasses may extend.
   */
  public void handleEvent(Event event) {
    setPageComplete(validatePage());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
   * .Composite)
   */
  @Override
  public void createControl(Composite parent) {
    Composite top = new Composite(parent, SWT.NONE);
    top.setLayout(new GridLayout());
    setControl(top);
    
    
    Composite composite = new Composite(top, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

    Label lblParentFolder = new Label(composite, SWT.NONE);
    lblParentFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblParentFolder.setText("File Name:");

    Validator validator = new Validator();
    fileTxt = new Text(composite, SWT.BORDER);
    fileTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    fileTxt.setText(file);
    fileTxt.addModifyListener(validator);

    Group grpStatechartProperties = new Group(top, SWT.NONE);
    grpStatechartProperties.setText("Code Properties");
    grpStatechartProperties.setLayout(new GridLayout(2, false));
    grpStatechartProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

    Label lblClassName = new Label(grpStatechartProperties, SWT.NONE);
    lblClassName.setToolTipText("The class name of the state chart");
    lblClassName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblClassName.setText("Class Name:");

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
    
    super.createControl(top);
   

    setPageComplete(validatePage());
  }

  /**
   * Sets the initial contents of the container name entry field, based upon
   * either a previously-specified initial value or the ability to determine
   * such a value.
   */
  protected void initialPopulateContainerNameField() {
    if (initialContainerFullPath != null) {
      resourceGroup.setContainerFullPath(initialContainerFullPath);
    } else {
      Iterator it = currentSelection.iterator();
      if (it.hasNext()) {
        Object object = it.next();
        IResource selectedResource = null;
        if (object instanceof IResource) {
          selectedResource = (IResource) object;
        } else if (object instanceof IAdaptable) {
          selectedResource = (IResource) ((IAdaptable) object).getAdapter(IResource.class);
        }
        if (selectedResource != null) {
          if (selectedResource.getType() == IResource.FILE) {
            selectedResource = selectedResource.getParent();
          }
          if (selectedResource.isAccessible()) {
            resourceGroup.setContainerFullPath(selectedResource.getFullPath());
          }
        }
      }
    }
  }

  protected String getClassName() {
    return classNameTxt.getText().trim();
  }

  protected String getPackage() {
    return packageTxt.getText().trim();
  }

  protected String getFile() {
    return fileTxt.getText().trim();
  }

  public URI getURI() {
    String file = getFile();
    if (!file.startsWith("/"))
      file = "/" + file;
    return URI.createPlatformResourceURI(file, false);
  }

  @SuppressWarnings("restriction")
  private String[] getSourceComplianceLevels(IJavaProject javaProject) {

    if (javaProject != null) {
      return new String[] { javaProject.getOption(JavaCore.COMPILER_SOURCE, true),
          javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true) };
    }

    return new String[] { JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE) };
  }

  @SuppressWarnings("restriction")
  private IStatus validateJavaTypeName(String text, IJavaProject project) {
    String[] sourceComplianceLevels = getSourceComplianceLevels(project);
    return JavaConventions.validateJavaTypeName(text, sourceComplianceLevels[0],
        sourceComplianceLevels[1]);
  }

  @SuppressWarnings("restriction")
  private IStatus validatePackageName(String text, IJavaProject project) {
    String[] sourceComplianceLevels = getSourceComplianceLevels(project);
    return JavaConventions.validatePackageName(text, sourceComplianceLevels[0],
        sourceComplianceLevels[1]);
  }

  @SuppressWarnings("restriction")
  protected boolean validatePage() {
    IPath path = new Path(getFile());
    IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path.uptoSegment(1));
    if (res == null) {
      setErrorMessage("File name is not valid. File must be within a project");
      return false;
    } else {
      if (!getFile().endsWith(".rsc")) {
        setErrorMessage("File name is not valid. File must end with .rsc extension");
        return false;
      }
    }

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
    IFile file = javaProject.getProject().getFile("src-gen/" + fqn.replace(".", "/") + ".java");
    if (file.exists()) {
      setErrorMessage("System Dynamics class already exists in the specified package. Change the chart name or the package.");
      return false;
    }

    setErrorMessage(null);
    return true;
  }

  private class Validator implements ModifyListener {

    @Override
    public void modifyText(ModifyEvent e) {
      setPageComplete(validatePage());
    }
  }
}
