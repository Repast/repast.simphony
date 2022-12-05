package repast.simphony.eclipse;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class StandAloneBatchClasspathContainer extends ClasspathContainerInitializer implements IClasspathContainer {

	public static final String JAR_CLASSPATH_DEFAULT = "STAND_ALONE_BATCH_LAUNCHER";

	private static final IPath PATH = new Path(JAR_CLASSPATH_DEFAULT);

	private static final String DESCRIPTION = RepastSimphonyPlugin.getInstance()
			.getResourceString("Classpath_Container_Stand_Alone_Batch.description");

	private IClasspathEntry[] classpathList = null;

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		if (containerPath.equals(PATH)) {
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },
					new IClasspathContainer[] { new StandAloneBatchClasspathContainer() }, null);
		}
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		if (classpathList == null) {

			String pluginDirectory = RepastSimphonyPlugin.getInstance().getPluginInstallationDirectory();
			File file;
			if (pluginDirectory.trim().equals("")) {
				file = new Path(RepastSimphonyPlugin.JAR_PATH_RELATIVE).toFile();
			} else {
				file = new Path(pluginDirectory + RepastSimphonyPlugin.JAR_PATH_RELATIVE).toFile();
			}
			boolean inDevEnv = !file.exists();

			String[] baseClasspathForStandAlone = {
					"ECLIPSE_HOME/plugins/repast.simphony.distributed.batch.ui_"
							+ RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION + "/bin-standalone",
					"/repast.simphony.runtime_" + RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION
							+ "/lib/commons-cli-1.3.1.jar",
					"/repast.simphony.core_" + RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION
							+ "/lib/commons-lang3-3.8.1.jar" };

			String[] standAloneClasspath = new String[baseClasspathForStandAlone.length];
			int i = 0;
			for (String jarElement : baseClasspathForStandAlone) {
				if ((jarElement != null) && (jarElement.toLowerCase().endsWith("jar"))) {
					standAloneClasspath[i] = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins" + jarElement;
				} else {
					standAloneClasspath[i] = jarElement;
				}

				if (inDevEnv) {
					// rewrite the jar element
					String item = standAloneClasspath[i];
					item = item.replace("_" + RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION, "");
					item = item.replace(JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins/", pluginDirectory);
					item = item.replace("ECLIPSE_HOME/plugins/", pluginDirectory);
					standAloneClasspath[i] = item;
				}
				i++;
			}
			
			classpathList = new IClasspathEntry[standAloneClasspath.length];

			for (int j = 0; j < standAloneClasspath.length; j++) {
				IPath jarPath = new Path(standAloneClasspath[j]);
				if ((jarPath.getFileExtension() != null) && (jarPath.getFileExtension().equalsIgnoreCase("jar"))) {
					classpathList[j] = JavaCore.newLibraryEntry(jarPath, null, null);
				} else {
					if (inDevEnv) {
						// the bin-standalone, since the ECLIPSE_HOME was stripped off
						classpathList[j] = JavaCore.newLibraryEntry(jarPath, null, null);
					} else {
						classpathList[j] = JavaCore.newVariableEntry(jarPath, null, null);
					}
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
