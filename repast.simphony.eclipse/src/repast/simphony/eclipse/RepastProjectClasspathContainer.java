package repast.simphony.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class RepastProjectClasspathContainer extends ClasspathContainerInitializer implements
    IClasspathContainer {

  public static final String JAR_CLASSPATH_DEFAULT = "REPAST_SIMPHONY_SUPPORT";

  private static final IPath PATH = new Path(JAR_CLASSPATH_DEFAULT);

  private static final String DESCRIPTION = RepastSimphonyPlugin.getInstance().getResourceString(
      "Classpath_Container.description");

  private IClasspathEntry[] classpathList = null;

  public RepastProjectClasspathContainer() {
  }

  public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
    if (containerPath.equals(PATH)) {
      JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },
          new IClasspathContainer[] { new RepastProjectClasspathContainer() }, null);
    }
  }

  public IClasspathEntry[] getClasspathEntries() {

    if (classpathList == null) {

      String[] jarPathList = RepastSimphonyPlugin.getInstance().getCompilerClasspath();
      classpathList = new IClasspathEntry[jarPathList.length];

      for (int i = 0; i < jarPathList.length; i++) {
        IPath jarPath = new Path(jarPathList[i]);
        classpathList[i] = JavaCore.newLibraryEntry(jarPath, null, null);
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
