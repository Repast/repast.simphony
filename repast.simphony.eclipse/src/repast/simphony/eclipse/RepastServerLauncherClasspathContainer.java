package repast.simphony.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class RepastServerLauncherClasspathContainer extends ClasspathContainerInitializer {

    public static final String JAR_CLASSPATH_DEFAULT = "REPAST_SIMPHONY_SERVER_LAUNCHER";

    private static final IPath PATH = new Path(JAR_CLASSPATH_DEFAULT);

    public RepastServerLauncherClasspathContainer() {
    }

    public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
        if (containerPath.equals(PATH)) {
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },
                    new IClasspathContainer[] { new GroovyRepastServerLauncherClasspathContainer(project) }, null);
        }
    }
}
