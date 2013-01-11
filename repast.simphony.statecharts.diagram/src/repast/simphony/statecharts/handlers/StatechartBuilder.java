/**
 * 
 */
package repast.simphony.statecharts.handlers;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;

import repast.simphony.statecharts.generator.CodeGenerator;
import repast.simphony.statecharts.svg.SVGExporter;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

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
    try {
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
    } catch (CoreException ex) {
      throw new CoreException(new Status(IResourceStatus.BUILD_FAILED, StatechartDiagramEditorPlugin.ID,
          ex.getLocalizedMessage(), ex));
    }
    return null;
  }

  private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
      throws CoreException {
    delta.accept(new Visitor(getProject(), monitor));
  }

  private void fullBuild(IProgressMonitor monitor) throws CoreException {
    getProject().accept(new FullBuildVisitor(getProject(), monitor));
  }
  
  private static class FullBuildVisitor implements IResourceVisitor {
    
    IProgressMonitor monitor;
    IProject project;
    CodeGenerator generator;

    public FullBuildVisitor(IProject project, IProgressMonitor monitor) {
      this.monitor = monitor;
      this.project = project;
      generator = new CodeGenerator();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
     */
    @Override
    public boolean visit(IResource resource) throws CoreException {
      IPath path = resource.getRawLocation();
      if (path != null && path.getFileExtension() != null && path.getFileExtension().equals(STATECHART_EXTENSION)) {
        generator.run(project, path, monitor);
      }
      return true;
    }
    
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
      //System.out.println("statechart builder running: " + delta);
      if ((delta.getKind() == IResourceDelta.CHANGED || delta.getKind() == IResourceDelta.ADDED) && path != null
          && path.getFileExtension() != null
          && path.getFileExtension().equals(STATECHART_EXTENSION)) {
    	  // create svg file here
        IPath srcPath = new CodeGenerator().run(project, path, monitor);
        new SVGExporter().run(path, srcPath, monitor);
        project.getFolder(srcPath.lastSegment()).refreshLocal(IResource.DEPTH_INFINITE, monitor);
      }
      return true;
    }
  }
}
