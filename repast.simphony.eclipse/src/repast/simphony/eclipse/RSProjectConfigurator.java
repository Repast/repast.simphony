/**
 * 
 */
package repast.simphony.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SystemUtils;
import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainer;
import org.codehaus.groovy.eclipse.core.compiler.CompilerUtils;
import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.codehaus.groovy.frameworkadapter.util.SpecifiedVersion;
import org.eclipse.core.resources.IFolder;
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

import repast.simphony.eclipse.ide.InstallerLauncherCreator;
import repast.simphony.eclipse.ide.ModelRunLauncherCreator;
import repast.simphony.eclipse.ide.ServerLauncherCreator;
import repast.simphony.eclipse.util.Utilities;

/**
 * Configures a Repast Simphony project by setting the classpath, adding the
 * natures, etc.
 * 
 * @author Nick Collier, Michael North
 */
public class RSProjectConfigurator {
  

	// VM args that are required for Java 9+.
	//   -XX:+IgnoreUnrecognizedVMOptions is used for cases when Java 8 is the JRE
	//      so that the module exports don't cause an unrecognized arg error
	
  public final static String VMARGS       = "-XX:+IgnoreUnrecognizedVMOptions --add-modules=ALL-SYSTEM --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED --add-exports=java.base/java.lang=ALL-UNNAMED --add-exports=java.desktop/sun.awt=ALL-UNNAMED --add-exports=java.desktop/sun.java2d=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED";
 
  // VM args specific to batch runs
  public final static String BATCH_VMARGS = "-XX:+IgnoreUnrecognizedVMOptions --add-modules=ALL-SYSTEM --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED --add-exports=java.base/java.lang=ALL-UNNAMED --add-exports=java.xml/com.sun.org.apache.xpath.internal.objects=ALL-UNNAMED --add-exports=java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED";
  
  //The Eclipse launch favorites list 
  public final static List<String> favoritesList =  new ArrayList<String>() {{
  	add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
  	add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
  }};
  
  
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
    
    CompilerUtils.setCompilerLevel(project.getProject(), SpecifiedVersion.DONT_CARE);
    
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
      ILaunchConfigurationType launchType = 
      		launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

      // Workaround needed to add launch configurations to favorites before
      // the launch history is checked by user
      DebugUIPlugin.getDefault().getLaunchConfigurationManager().getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
      DebugUIPlugin.getDefault().getLaunchConfigurationManager().getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

      // Create the launches for the model run and batch run
      ModelRunLauncherCreator modelRunCreator = new ModelRunLauncherCreator();
      modelRunCreator.run(javaProject, newFolder);
      
      // Create the launch for the model server
      ServerLauncherCreator creator = new ServerLauncherCreator();
      creator.run(javaProject, newFolder);

      // Create the launches for the model installer builders
      InstallerLauncherCreator installerCreator = new InstallerLauncherCreator();
      installerCreator.run(javaProject, newFolder);
     
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

}
