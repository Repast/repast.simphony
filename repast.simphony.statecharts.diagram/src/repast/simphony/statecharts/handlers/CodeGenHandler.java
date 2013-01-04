/**
 * 
 */
package repast.simphony.statecharts.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import repast.simphony.statecharts.generator.CodeGenerator;
import repast.simphony.statecharts.svg.NotationReader;

/**
 * @author Nick Collier
 */
public class CodeGenHandler extends AbstractHandler {

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
   * ExecutionEvent)
   */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentSelection(event);

    if (selection instanceof IStructuredSelection) {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      Object firstElement = structuredSelection.getFirstElement();
      
      if (firstElement instanceof IFile) {
        IFile file = (IFile) firstElement;
        
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(file.getFullPath().segment(0));
        IPath path = project.getLocation().append(file.getFullPath().removeFirstSegments(1));
        
        if (path.getFileExtension().equals(StatechartBuilder.STATECHART_EXTENSION)) {
          try {
        	new NotationReader().readNotation(path);
            new CodeGenerator().run(project, path, null);
          } catch (CoreException e) {
            throw new ExecutionException("Error executing generate code handler", e);
          }
        }
      }
    }

    return null;
  }
}
