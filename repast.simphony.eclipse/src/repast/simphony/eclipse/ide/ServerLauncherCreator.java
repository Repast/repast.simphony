package repast.simphony.eclipse.ide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import repast.simphony.eclipse.RSProjectConfigurator;
import repast.simphony.eclipse.RepastServerLauncherClasspathContainer;

public class ServerLauncherCreator {

    private ILaunchConfigurationType launchType;

    public ServerLauncherCreator() {
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchType = launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    }

    public void run(IJavaProject project, IFolder folder) throws CoreException {

        ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchType.newInstance(folder,
                project.getElementName() + " Server");
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                project.getElementName());
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                "repast.simphony.ws.RepastWS");
        
        String scenarioDirectory = project.getProject().getName() + ".rs";
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                "5000 \"${workspace_loc:" + project.getElementName() + "}/" + scenarioDirectory + "\" true");
        
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, RSProjectConfigurator.VMARGS);
        IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
        IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
                IRuntimeClasspathEntry.STANDARD_CLASSES);
        r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
        List classpath = new ArrayList();
        classpath.add(r.getMemento());
        IPath jarPath = new Path(RepastServerLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
        r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
        r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
        classpath.add(r.getMemento());
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
        launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
        launchConfigurationWorkingCopy.doSave();
    }
}
