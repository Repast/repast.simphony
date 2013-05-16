/**
 * 
 */
package repast.simphony.eclipse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SystemUtils;
import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainer;
import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import repast.simphony.eclipse.util.Utilities;

/**
 * Configures a Repast Simphony project by setting the classpath, adding the
 * natures, etc.
 * 
 * @author Nick Collier, Michael North
 */
public class RSProjectConfigurator {
  
//  public final static String PREFERRED_LAUNCHERS = "org.eclipse.debug.core.preferred_launchers";
//  public final static String RELOGO_LAUNCH_DELEGATE = "repast.simphony.relogo.ide.relogoLaunchDelegate";
//  public final static String LOCAL_JAVA_LAUNCH_DELEGATE = "org.eclipse.jdt.launching.localJavaApplication";
//  public final static String LAUNCH_DELEGATE_RUN = "[run]";
//  public final static String LAUNCH_DELEGATE_DEBUG = "[debug]";
  
  /**
   * Configures a new project for Repast Simphony. This adds the GroovyRuntime to the project,
   * updates the classpath with the Repast Simphony classpath
   * 
   * @param project
   * @param monitor
   */
  public void configureNewProject(IJavaProject project, IProgressMonitor monitor) throws CoreException {
    
    GroovyRuntime.addGroovyRuntime(project.getProject());
    updateClasspath(project);
    
    // add the RepastSimphony nature which will use the repast config extension point
    // to give other plugins (e.g. flowchart) the chance to configure their project
    Utilities.addNature(project.getProject(), RepastSimphonyPlugin.REPAST_SIMPHONY_NATURE_ID);
    project.save(monitor, true);
  }
  
  /**
   * Removes the repast simphony from the specified project. This removes the nature
   * and the classpath container.
   * 
   * @param project
   * @param monitor
   * @throws CoreException
   */
  public void deconfigureProject(IJavaProject project, IProgressMonitor monitor) throws CoreException {
    GroovyRuntime.removeGroovyNature(project.getProject());
    removeClasspath(project);
    
    Utilities.removeNature(project.getProject(), RepastSimphonyPlugin.REPAST_SIMPHONY_NATURE_ID);
    project.save(monitor, true);
  }
  
  private void removeClasspath(IJavaProject project) throws JavaModelException {
    IClasspathEntry rsEntry = JavaCore.newContainerEntry(new Path(RepastProjectClasspathContainer.JAR_CLASSPATH_DEFAULT));
    IClasspathEntry[] entries = project.getRawClasspath();
    List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
    for (IClasspathEntry entry : entries) {
      if (!entry.equals(rsEntry)) newEntries.add(entry);
    }
    project.setRawClasspath(newEntries.toArray(new IClasspathEntry[0]), null);
    
  }
  
  private void updateClasspath(IJavaProject project) throws JavaModelException {
    IPath jarPath = new Path(RepastProjectClasspathContainer.JAR_CLASSPATH_DEFAULT);
    IClasspathEntry[] oldClassPathEntries = project.getRawClasspath();
    boolean found = false;
    for (int i = 0; i < oldClassPathEntries.length; i++) {
      final IClasspathEntry classPathEntry = oldClassPathEntries[i];
      if (classPathEntry.getPath().lastSegment().equals(jarPath.lastSegment())) {
        found = true;
        break;
      }
    }

    if (!found) {
      final IClasspathEntry[] newClassPathEntries = (IClasspathEntry[]) ArrayUtils.add(
          oldClassPathEntries, JavaCore.newContainerEntry(jarPath));
      project.setRawClasspath(newClassPathEntries, null);
    }
  }
  
  public void createLaunchConfigurations(IJavaProject javaProject, IFolder newFolder,
      String scenarioDirectory) {

    try {
      ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
      ILaunchConfigurationType launchType = launchManager
          .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
      ILaunchConfiguration[] launchConfigurations = launchManager
          .getLaunchConfigurations(launchType);

      // Workaround needed to add launch configurations to favorites before
      // the launch history is checked by user
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

      List classpath = new ArrayList();

      // ******************************************************************
      // ******
      // * *
      // * Run Model *
      // * *
      // ******************************************************************
      // ******
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchType.newInstance(
          newFolder, javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastMain");
//      Map<String, String> preferredLaunchers = new HashMap<String, String>();
//      preferredLaunchers.put(LAUNCH_DELEGATE_RUN, LOCAL_JAVA_LAUNCH_DELEGATE);
//      preferredLaunchers.put(LAUNCH_DELEGATE_DEBUG, LOCAL_JAVA_LAUNCH_DELEGATE);
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "\"${workspace_loc:"
              + javaProject.getElementName() + "}/" + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      List favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      IPath jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Build Installer *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(
            "Build Installer for " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder, "Build Installer for "
          + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "org.apache.tools.ant.launch.Launcher");
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      if (SystemUtils.IS_OS_MAC)
        launchConfigurationWorkingCopy
            .setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                "-buildfile \"${workspace_loc:"
                    + javaProject.getElementName()
                    + "}"
                    + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                    + "${folder_prompt:the Folder to output the installer (setup.jar) file}/setup.jar\" "
                    + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                    + " -DGroovyHomeDirectory=\"${groovy_home}\""
                    + " -DREPAST_VERSION=${REPAST_VERSION}");
      else if (SystemUtils.IS_OS_WINDOWS)
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");
      // for non-Windows or Mac
      else
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");

      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Batch Run *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName()
            .equals("Batch " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder,
          "Batch " + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,

          "repast.simphony.batch.standalone.StandAloneMain");
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,"-model_dir \"${workspace_loc:" + javaProject.getElementName() + "}\"");
      
      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      jarPath = new Path(StandAloneBatchCPInit.CP_VARIABLE_NAME);
      r = JavaRuntime.newVariableRuntimeClasspathEntry(jarPath);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      
      jarPath = GroovyClasspathContainer.CONTAINER_ID;
      r = JavaRuntime.newVariableRuntimeClasspathEntry(jarPath);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();
      favoritesList = null;

    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Should be identical to above, except for the specification of the launch
   * configuration specific to ReLogo projects.
   * 
   * @param javaProject
   * @param newFolder
   * @param scenarioDirectory
   */
  public void createReLogoLaunchConfigurations(IJavaProject javaProject, IFolder newFolder,
      String scenarioDirectory) {

    try {
      ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
      ILaunchConfigurationType launchType = launchManager
          .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
      ILaunchConfiguration[] launchConfigurations = launchManager
          .getLaunchConfigurations(launchType);

      // Workaround needed to add launch configurations to favorites before
      // the launch history is checked by user
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

      List classpath = new ArrayList();

      // ******************************************************************
      // ******
      // * *
      // * Run Model *
      // * *
      // ******************************************************************
      // ******
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchType.newInstance(
          newFolder, javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastMain");
//      Map<String, String> preferredLaunchers = new HashMap<String, String>();
//      preferredLaunchers.put(LAUNCH_DELEGATE_RUN, RELOGO_LAUNCH_DELEGATE);
//      preferredLaunchers.put(LAUNCH_DELEGATE_DEBUG, RELOGO_LAUNCH_DELEGATE);
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "\"${workspace_loc:"
              + javaProject.getElementName() + "}/" + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      List favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      IPath jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Build Installer *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(
            "Build Installer for " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder, "Build Installer for "
          + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "org.apache.tools.ant.launch.Launcher");
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      if (SystemUtils.IS_OS_MAC)
        launchConfigurationWorkingCopy
            .setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                "-buildfile \"${workspace_loc:"
                    + javaProject.getElementName()
                    + "}"
                    + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                    + "${folder_prompt:the Folder to output the installer (setup.jar) file}/setup.jar\" "
                    + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                    + " -DGroovyHomeDirectory=\"${groovy_home}\""
                    + " -DREPAST_VERSION=${REPAST_VERSION}");
      else if (SystemUtils.IS_OS_WINDOWS)
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");
      // for non-Windows or Mac
      else
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");

      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Batch Run *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName()
            .equals("Batch " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder,
          "Batch " + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,

          "repast.simphony.batch.standalone.StandAloneMain");
//      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,"-model_dir \"${workspace_loc:" + javaProject.getElementName() + "}\"");
      
      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      jarPath = new Path(StandAloneBatchCPInit.CP_VARIABLE_NAME);
      r = JavaRuntime.newVariableRuntimeClasspathEntry(jarPath);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      
      jarPath = GroovyClasspathContainer.CONTAINER_ID;
      r = JavaRuntime.newVariableRuntimeClasspathEntry(jarPath);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();
      favoritesList = null;

    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

}
