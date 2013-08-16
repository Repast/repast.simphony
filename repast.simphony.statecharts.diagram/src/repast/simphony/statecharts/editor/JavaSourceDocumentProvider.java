/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;

/**
 * DocumentProvider for the source that is edited in statechart code editors. 
 * 
 * @author Nick Collier
 */
public class JavaSourceDocumentProvider extends AbstractDocumentProvider {
  
  

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#isModifiable(java.lang.Object)
   */
  @Override
  public boolean isModifiable(Object element) {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(java.lang.Object)
   */
  @Override
  protected IDocument createDocument(Object element) throws CoreException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createAnnotationModel(java.lang.Object)
   */
  @Override
  protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
   */
  @Override
  protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document,
      boolean overwrite) throws CoreException {
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
    // TODO Auto-generated method stub
    return null;
  }

}
