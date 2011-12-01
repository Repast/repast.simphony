package repast.simphony.agents.designer.core;

import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class GroovyRepastLauncherClasspathContainer extends
		GroovyClasspathContainer {

	public static final String JAR_CLASSPATH_DEFAULT = "REPAST_SIMPHONY_LAUNCHER";

	private static final IPath PATH = new Path(JAR_CLASSPATH_DEFAULT);

	private static final String DESCRIPTION = AgentBuilderPlugin
			.getResourceString("Classpath_Container_Launcher.description");

	private IClasspathEntry[] classpathList = null;
	

	public GroovyRepastLauncherClasspathContainer(IProject project) {
		super(project);
	}

	public IClasspathEntry[] getClasspathEntries() {

		if (classpathList == null) {
			
			IClasspathEntry[] groovyClasspathEntries = super.getClasspathEntries();
			int groovyEntriesLength = groovyClasspathEntries.length;
			
			String[] jarPathList = AgentBuilderPlugin
					.getJarPathListForLauncher();
			int jarPathListLength = jarPathList.length;
			
			int totalListLength = groovyEntriesLength + jarPathListLength;
			
			classpathList = new IClasspathEntry[totalListLength];
			
			for (int i = 0; i < groovyEntriesLength; i++){
				classpathList[i] = groovyClasspathEntries[i];
			}
			
			for (int i = groovyEntriesLength; i < totalListLength; i++) {
				IPath jarPath = new Path(jarPathList[i - groovyEntriesLength]);
				if ((jarPath.getFileExtension() != null)
						&& (jarPath.getFileExtension().equalsIgnoreCase("jar"))) {
					classpathList[i] = JavaCore.newLibraryEntry(jarPath, null,
							null);
				} else {
					classpathList[i] = JavaCore.newVariableEntry(jarPath, null,
							null);
				}
			}

		}

		return classpathList;

	}

	public IPath getPath() {
		return PATH;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

}
