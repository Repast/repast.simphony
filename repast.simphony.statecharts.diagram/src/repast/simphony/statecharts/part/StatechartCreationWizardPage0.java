package repast.simphony.statecharts.part;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import repast.simphony.statecharts.scmodel.LanguageTypes;

public class StatechartCreationWizardPage0 extends WizardPage {

  private Text nameTxt;
  private Text classNameTxt;
  private Text packageTxt;
  private Text agentClassText;

  private IProject project;
  private Button btnJava;
  private Button btnGroovy;
  private Button btnRelogo;

  private String agentType, file;
  private Text fileTxt;

  /**
   * 
   * @param pageName
   * @param project
   *          may be null
   */
  protected StatechartCreationWizardPage0(String pageName, IProject project, String agentType,
      String file) {
    super(pageName);
    this.project = project;
    this.agentType = agentType;
    this.file = file;
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
    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(2, false));

    Composite composite = new Composite(container, SWT.NONE);
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

    Group grpStatechartProperties = new Group(container, SWT.NONE);
    grpStatechartProperties.setText("Statechart Properties");
    grpStatechartProperties.setLayout(new GridLayout(2, false));
    grpStatechartProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
    // grpStatechartProperties.setText("Statechart Properties");

    Label lblStatechartName = new Label(grpStatechartProperties, SWT.NONE);
    lblStatechartName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblStatechartName.setToolTipText("The name of the statechart.");
    lblStatechartName.setText("Name:");

    nameTxt = new Text(grpStatechartProperties, SWT.BORDER);
    nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    nameTxt.addModifyListener(validator);

    Label lblClassName = new Label(grpStatechartProperties, SWT.NONE);
    lblClassName.setToolTipText("The class name of the state chart");
    lblClassName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblClassName.setText("Class Name:");

    classNameTxt = new Text(grpStatechartProperties, SWT.BORDER);
    classNameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    classNameTxt.addModifyListener(validator);

    Label lblPackage = new Label(grpStatechartProperties, SWT.NONE);
    lblPackage.setToolTipText("The package of the statechart");
    lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblPackage.setText("Package:");

    packageTxt = new Text(grpStatechartProperties, SWT.BORDER);
    packageTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    packageTxt.addModifyListener(validator);

    Label lblAgentClass = new Label(grpStatechartProperties, SWT.NONE);
    lblAgentClass.setToolTipText("The agent type associated with this statechart.");
    lblAgentClass.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
    lblAgentClass.setText("Agent Class:");

    agentClassText = new Text(grpStatechartProperties, SWT.BORDER);
    agentClassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    agentClassText.setText(agentType);

    Group grpDefaultLanguage = new Group(container, SWT.NONE);
    grpDefaultLanguage
        .setToolTipText("Select the default language for any actions in the statechart");
    grpDefaultLanguage.setLayout(new GridLayout(3, false));
    grpDefaultLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    grpDefaultLanguage.setText("Default Language");

    btnJava = new Button(grpDefaultLanguage, SWT.RADIO);
    btnJava.setSelection(true);
    btnJava.setText("Java");

    btnGroovy = new Button(grpDefaultLanguage, SWT.RADIO);
    btnGroovy.setText("Groovy");

    btnRelogo = new Button(grpDefaultLanguage, SWT.RADIO);
    btnRelogo.setText("ReLogo");
    agentClassText.addModifyListener(validator);
    
    generateDefaultNames();
    setPageComplete(validatePage());
  }
  
  private void generateDefaultNames() {
    nameTxt.setText("Statechart");
    classNameTxt.setText("Statechart");
    if (agentType != null && agentType.length() > 0) {
      int index = agentType.lastIndexOf(".");
      if (index != -1) {
        packageTxt.setText(agentType.substring(0, index) + ".chart");
      } else {
        packageTxt.setText("chart");
      }
    } else {
      packageTxt.setText("chart");
    }
  }

  protected String getStatechartName() {
    return nameTxt.getText().trim();
  }

  protected String getClassName() {
    return classNameTxt.getText().trim();
  }

  protected String getPackage() {
    return packageTxt.getText().trim();
  }

  protected String getAgentClassName() {
    return agentClassText.getText().trim();
  }

  protected LanguageTypes getLanguage() {
    if (btnJava.getSelection())
      return LanguageTypes.JAVA;
    else if (btnGroovy.getSelection())
      return LanguageTypes.GROOVY;
    else
      return LanguageTypes.RELOGO;
  }

  protected String getFile() {
    return fileTxt.getText().trim();
  }
  
  public URI getURI() {
    String file = getFile();
    if (!file.startsWith("/")) file = "/" + file;
    return URI.createPlatformResourceURI(file, false);
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
    
    
    String val = getStatechartName();
    if (val.length() == 0) {
      setErrorMessage("Statechart name is blank.");
      return false;
    }

    IJavaProject javaProject = null;
    if (project != null)
      javaProject = JavaCore.create(project);

    val = getClassName();
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

    val = getAgentClassName();
    if (val.length() == 0) {
      setErrorMessage("Agent class is blank.");
      return false;
    } else {
      IStatus status = validatePackageName(val, javaProject);
      if (!status.isOK()) {
        setErrorMessage("Agent class is not valid. '" + val + "' is not a valid identifier.");
        return false;
      }
    }
    
    String fqn = getPackage() + "." + getClassName();
    IFile file = javaProject.getProject().getFile("src-gen/" + fqn.replace(".", "/") + ".java");
    if (file.exists()) {
      setErrorMessage("Chart class already exists in the specified package. Change the chart name or the package.");
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
