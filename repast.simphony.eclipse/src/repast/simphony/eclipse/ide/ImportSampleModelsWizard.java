package repast.simphony.eclipse.ide;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;

public class ImportSampleModelsWizard extends Wizard implements IImportWizard, INewWizard {

  ExternalProjectImportWizard wizard;

  public ImportSampleModelsWizard() {
    IPath eclipseHome = JavaCore.getClasspathVariable("ECLIPSE_HOME");
    String eclipseHomeString = eclipseHome.toOSString();
    String modelsPath = Platform.getOS().equals(Platform.OS_MACOSX) ?  eclipseHomeString + File.separator + "../../.."
        + File.separator + "models" : eclipseHomeString + File.separator + ".." + File.separator + "models";
    this.wizard = new ExternalProjectImportWizard(modelsPath);
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    wizard.init(workbench, selection);

    IDialogSettings settings = wizard.getDialogSettings();
    boolean copyToWorkspace = settings.getBoolean("WizardProjectsImportPage.STORE_COPY_PROJECT_ID");
    if (!copyToWorkspace) {
      settings.put("WizardProjectsImportPage.STORE_COPY_PROJECT_ID", true);
    }
    ImportSampleModelsWizardDialog dialog = new ImportSampleModelsWizardDialog(this.getShell(),
        wizard);

    // Workaround needed to add launch configurations to favorites for importing
    // into new workspace (see repast.simphony.agents.base.Util)
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType launchType = launchManager
        .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    try {
      ILaunchConfiguration[] launchConfigurations = launchManager
          .getLaunchConfigurations(launchType);
    } catch (CoreException e) {
      e.printStackTrace();
    }
    DebugUIPlugin.getDefault().getLaunchConfigurationManager()
        .getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
    DebugUIPlugin.getDefault().getLaunchConfigurationManager()
        .getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

    dialog.open();

  }

  @Override
  public boolean performFinish() {
    return wizard.performFinish();
  }

}
