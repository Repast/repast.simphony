/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;

/**
 * Overrides JavaCompletionProcoessor to return a ContextAssistInvocationContext that 
 * can create a ICompilationUnit that is not associated with a file.
 * 
 * @author Nick Collier
 */
public class JCompletionProcessor extends JavaCompletionProcessor {

  public JCompletionProcessor(IEditorPart editor, ContentAssistant assistant, String partition) {
    super(editor, assistant, partition);
  }
  
  /*
   * @see org.eclipse.jdt.internal.ui.text.java.ContentAssistProcessor#createContext(org.eclipse.jface.text.ITextViewer, int)
   */
  @Override
  protected ContentAssistInvocationContext createContext(ITextViewer viewer, int offset) {
          return new JContentAssistInvocationContext(viewer, offset, fEditor);
  }

}
