package repast.simphony.systemdynamics.diagram.part;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
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

/**
 * @generated
 */
public class SystemdynamicsCreationWizardPage extends WizardNewFileCreationPage {

  /**
   * @generated
   */
  private final String fileExtension;
  private Text txtClassName;
  private Text txtPackage;

  private IProject project;

  /**
   * @generated NOT
   */
  public SystemdynamicsCreationWizardPage(String pageName, IProject project,
      IStructuredSelection selection, String fileExtension) {
    super(pageName, selection);
    this.project = project;
    this.fileExtension = fileExtension;
  }

  /**
   * Override to create files with this extension.
   * 
   * @generated
   */
  protected String getExtension() {
    return fileExtension;
  }

  /**
   * @generated
   */
  public URI getURI() {
    return URI.createPlatformResourceURI(getFilePath().toString(), false);
  }

  /**
   * @generated
   */
  protected IPath getFilePath() {
    IPath path = getContainerFullPath();
    if (path == null) {
      path = new Path(""); //$NON-NLS-1$
    }
    String fileName = getFileName();
    if (fileName != null) {
      path = path.append(fileName);
    }
    return path;
  }

  /**
   * @generated NOT
   */
  public void createControl(Composite parent) {
    super.createControl(parent);
    setFileName(SystemdynamicsDiagramEditorUtil.getUniqueFileName(getContainerFullPath(),
        getFileName(), getExtension()));

    Composite comp = (Composite) getControl();
    Group grpCodeGenerationProperties = new Group(comp, SWT.NONE);
    grpCodeGenerationProperties.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
        | GridData.HORIZONTAL_ALIGN_FILL));
    grpCodeGenerationProperties.setText("Code Generation Properties");
    GridLayout gl_grpCodeGenerationProperties = new GridLayout();
    gl_grpCodeGenerationProperties.numColumns = 2;
    grpCodeGenerationProperties.setLayout(gl_grpCodeGenerationProperties);

    Label lblClas = new Label(grpCodeGenerationProperties, SWT.NONE);
    lblClas.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblClas.setText("Class Name:");

    txtClassName = new Text(grpCodeGenerationProperties, SWT.BORDER);
    txtClassName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtClassName.addModifyListener(new Validator());

    Label lblPackage = new Label(grpCodeGenerationProperties, SWT.NONE);
    lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblPackage.setText("Package:");

    txtPackage = new Text(grpCodeGenerationProperties, SWT.BORDER);
    txtPackage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    txtPackage.addModifyListener(new Validator());

    setPageComplete(validatePage());
  }

  protected String getClassName() {
    if (txtClassName == null) return "";
    return txtClassName.getText().trim();
  }

  protected String getPackage() {
    if (txtPackage == null) return "";
    return txtPackage.getText().trim();
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

  private String[] getSourceComplianceLevels(IJavaProject javaProject) {

    if (javaProject != null) {
      return new String[] { javaProject.getOption(JavaCore.COMPILER_SOURCE, true),
          javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true) };
    }

    return new String[] { JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE) };
  }

  /**
   * @generated NOT
   */
  protected boolean validatePage() {
    if (!super.validatePage()) {
      return false;
    }

    String extension = getExtension();
    if (extension != null && !getFilePath().toString().endsWith("." + extension)) {
      setErrorMessage(NLS.bind(Messages.SystemdynamicsCreationWizardPageExtensionError, extension));
      return false;
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

    if (javaProject != null) {
      IPath path = new Path(getPackage().replace(".", "/") + "/" +  getClassName() + ".java");
      IJavaElement element;
      try {
        element = javaProject.findElement(path);
        if (element != null) {
          setErrorMessage("Class " + getPackage() + "." + getClassName() + " already exists.");
          return false;
        }
      } catch (JavaModelException e) {
      }
    }
    return true;
  }
  
  private class Validator implements ModifyListener {
    @Override
    public void modifyText(ModifyEvent e) {
      setPageComplete(validatePage());
    }
  }
}
