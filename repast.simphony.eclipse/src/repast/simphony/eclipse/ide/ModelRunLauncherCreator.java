package repast.simphony.eclipse.ide;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import repast.simphony.eclipse.RSProjectConfigurator;
import repast.simphony.eclipse.RepastLauncherClasspathContainer;
import repast.simphony.eclipse.StandAloneBatchClasspathContainer;

/**
 * Creates the Eclipse launch configurations for running the model.
 * 
 *
 */
public class ModelRunLauncherCreator {

    private ILaunchConfigurationType launchType;

    public ModelRunLauncherCreator() {
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchType = launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    }

    public void run(IJavaProject project, IFolder folder) throws CoreException {
      
    	// Create the launch for running the model
    	ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = 
    			launchType.newInstance(folder, project.getElementName() + " Model");

    	String scenarioDirectory = project.getProject().getName() + ".rs";
    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());
    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "repast.simphony.runtime.RepastMain");
    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, 
    			"\"${workspace_loc:" + project.getElementName() + "}/" + scenarioDirectory + "\"");
    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, RSProjectConfigurator.VMARGS);

    	List<String> classpath = new ArrayList<String>();
    	IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
    	IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath, IRuntimeClasspathEntry.STANDARD_CLASSES);
    	r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
    	classpath.add(r.getMemento());

    	IPath jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
    	r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
    	r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
    	classpath.add(r.getMemento());

    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
    	launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
    	launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS, RSProjectConfigurator.favoritesList); 
    	launchConfigurationWorkingCopy.doSave();
    	
          
      // Create the launch for the model batch run
      classpath.clear();
     
      launchConfigurationWorkingCopy = 
      		launchType.newInstance(folder, "Batch " + project.getElementName() + " Model");
      
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "repast.simphony.batch.standalone.StandAloneMain");
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, RSProjectConfigurator.BATCH_VMARGS);

      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,"-model_dir \"${workspace_loc:" + project.getElementName() + "}\"");
            
      systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath, IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
            
      jarPath = new Path(StandAloneBatchClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      
      jarPath = new Path(GroovyClasspathContainer.ID);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.USER_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
    
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS, RSProjectConfigurator.favoritesList);
      launchConfigurationWorkingCopy.doSave();
    }
}
