package repast.simphony.eclipse.ide;

import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import repast.simphony.eclipse.RSProjectConfigurator;

/**
 * Creates the Eclipse launch configurations for the build model installer ant scripts.
 * 
 *
 */
public class InstallerLauncherCreator {

    private ILaunchConfigurationType launchType;

    public InstallerLauncherCreator() {
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchType = launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    }

    public void run(IJavaProject project, IFolder folder) throws CoreException {
      
    	// Create the launch for the build model installer IzPack jar
      ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = 
      		launchType.newInstance(folder, "Build Installer for " + project.getElementName() + " Model");
      
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "org.apache.tools.ant.launch.Launcher");
      
      launchConfigurationWorkingCopy.setAttribute(
      		IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
      		"-buildfile \"${workspace_loc:" + project.getElementName() + "}"
      				+ "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
      				+ "${folder_prompt:the Folder to output the installer (setup.jar) file}/setup.jar\" "
      				+ "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
      				+ " -DREPAST_VERSION=${REPAST_VERSION}");
      
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, RSProjectConfigurator.VMARGS);

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS, RSProjectConfigurator.favoritesList);
      launchConfigurationWorkingCopy.doSave();
      
      
      
      // Create the launch for the build portable model archive
      launchConfigurationWorkingCopy = 
      		launchType.newInstance(folder, "Build Portable Archive for " + project.getElementName() + " Model");
      
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "org.apache.tools.ant.launch.Launcher");
      
      launchConfigurationWorkingCopy.setAttribute(
      		IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
      		"-buildfile \"${workspace_loc:" + project.getElementName() + "}"
      				+ "/installer/create_model_archive.xml\" -DoutputInstallationFile=\""
      				+ "${folder_prompt:the Folder to output the archive (model.zip) file}\" "
      				+ "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
      				+ " -DREPAST_VERSION=${REPAST_VERSION}");
      
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, RSProjectConfigurator.VMARGS);

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS, RSProjectConfigurator.favoritesList);
      launchConfigurationWorkingCopy.doSave();
 
    }
}
