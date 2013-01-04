/**
 * 
 */
package repast.simphony.statecharts.handlers;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import repast.simphony.statecharts.generator.CodeGenerator;
import repast.simphony.statecharts.svg.NotationReader;

/**
 * @author Nick Collier
 */
public class StatechartBuilder extends IncrementalProjectBuilder {

  public static final String STATECHART_EXTENSION = "rsc";

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
   * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
      throws CoreException {
    if (kind == IncrementalProjectBuilder.FULL_BUILD) {
      fullBuild(monitor);
    } else {
      IResourceDelta delta = getDelta(getProject());
      if (delta == null) {
        fullBuild(monitor);
      } else {
        incrementalBuild(delta, monitor);
      }
    }
    return null;
  }

  private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
      throws CoreException {
    delta.accept(new Visitor(getProject(), monitor));
  }

  private void fullBuild(IProgressMonitor monitor) {
    // TODO get all the rsc files in the project and build them.
  }

  private static class Visitor implements IResourceDeltaVisitor {

    IProgressMonitor monitor;
    IProject project;

    public Visitor(IProject project, IProgressMonitor monitor) {
      this.monitor = monitor;
      this.project = project;
    }

    @Override
    public boolean visit(IResourceDelta delta) throws CoreException {
      IPath path = delta.getResource().getRawLocation();
      System.out.println("statechart builder running: " + delta);
      if ((delta.getKind() == IResourceDelta.CHANGED || delta.getKind() == IResourceDelta.ADDED) && path != null
          && path.getFileExtension() != null
          && path.getFileExtension().equals(STATECHART_EXTENSION)) {
    	  // create svg file here
//    	  new NotationReader().readNotation();
        new CodeGenerator().run(project, path, monitor);
        // write svg file here
      }
      return true;
    }
  }
}
