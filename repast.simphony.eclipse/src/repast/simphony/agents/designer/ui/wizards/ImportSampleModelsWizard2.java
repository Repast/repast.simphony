/**
 * 
 */
package repast.simphony.agents.designer.ui.wizards;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;

/**
 * Integrates with the Import dialog and imports the sample models.
 * 
 * @author Nick Collier
 */
public class ImportSampleModelsWizard2 extends ExternalProjectImportWizard {

  /**
   * 
   */
  public ImportSampleModelsWizard2() {
    super(JavaCore.getClasspathVariable("ECLIPSE_HOME").toOSString() + File.separator + ".."
        + File.separator + "models");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard#init(org
   * .eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
   */
  @Override
  public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
    super.init(workbench, currentSelection);
    
    boolean copyToWorkspace = getDialogSettings().getBoolean("WizardProjectsImportPage.STORE_COPY_PROJECT_ID");
    if (!copyToWorkspace){
            getDialogSettings().put("WizardProjectsImportPage.STORE_COPY_PROJECT_ID", true);
    }
    
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
  }
}
