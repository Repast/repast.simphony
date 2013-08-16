package repast.simphony.statecharts.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

/**
 * Overrides JavaContentAssistInvocationContext.getCompilationUnit to return a
 * non-file related unit.
 */
public class JContentAssistInvocationContext extends JavaContentAssistInvocationContext {

  private IEditorInput input;

  // these mirror variables in super class
  private ICompilationUnit unit;
  private IPackageFragment frag;

  public JContentAssistInvocationContext(ITextViewer viewer, int offset, IEditorPart editor) {
    super(viewer, offset, editor);
    input = editor.getEditorInput();
    
    IProject project = ((IFileEditorInput) input).getFile().getProject();
    IJavaProject jProject = JavaCore.create(project);
    try {
      // path should come from statechart properties
      // package must exist on the build path for this to work
      frag = jProject.findPackageFragment(project.getFullPath().append(
          "src-gen/sample1/chart"));
    }  catch (JavaModelException e) {
      StatechartDiagramEditorPlugin.getInstance().logError(e.getMessage(), e); 
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext#
   * getCompilationUnit()
   */
  @Override
  public ICompilationUnit getCompilationUnit() {
    if (unit == null) {
      try {
        unit = frag.createCompilationUnit("StateActionTemplate.java",
            getDocument().get(), true, null);
      } catch (JavaModelException e) {
        StatechartDiagramEditorPlugin.getInstance().logError(e.getMessage(), e); 
      }
    }

    return unit;
  }

}
