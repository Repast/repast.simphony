/**
 * 
 */
// we have to use this package so we can have access to some
// protected static classes
package org.eclipse.jdt.internal.ui.javaeditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;

/**
 * A CompilationUnitDocumentProviderd that delegates all its methods to
 * the default provider except for the AnnotationModel. 
 * 
 * @author Nick Collier
 */

public class SCCompilationUnitDocumentProvider implements ICompilationUnitDocumentProvider {
  
  protected static class SCAnnotationModel extends CompilationUnitDocumentProvider.CompilationUnitAnnotationModel {

    public SCAnnotationModel(IResource resource) {
      super(resource);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider.CompilationUnitAnnotationModel#acceptProblem(org.eclipse.jdt.core.compiler.IProblem)
     */
    @Override
    public void acceptProblem(IProblem problem) {
      super.acceptProblem(problem);
      System.out.println(problem);
    }
    
    
  }
  
  private CompilationUnitDocumentProvider provider;
  
  public SCCompilationUnitDocumentProvider(CompilationUnitDocumentProvider delegate) {
    this.provider = delegate;
  }

  /**
   * @return
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return provider.hashCode();
  }

  /**
   * @param obj
   * @return
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    return provider.equals(obj);
  }

  /**
   * @return
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return provider.toString();
  }

  /**
   * @param parentProvider
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#setParentDocumentProvider(org.eclipse.ui.texteditor.IDocumentProvider)
   */
  public final void setParentDocumentProvider(IDocumentProvider parentProvider) {
    provider.setParentDocumentProvider(parentProvider);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getDocument(java.lang.Object)
   */
  public IDocument getDocument(Object element) {
    return provider.getDocument(element);
  }

  /**
   * @param element
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#resetDocument(java.lang.Object)
   */
  public void resetDocument(Object element) throws CoreException {
    provider.resetDocument(element);
  }

  /**
   * @param monitor
   * @param element
   * @param document
   * @param overwrite
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#saveDocument(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
   */
  public final void saveDocument(IProgressMonitor monitor, Object element, IDocument document,
      boolean overwrite) throws CoreException {
    provider.saveDocument(monitor, element, document, overwrite);
  }

  /**
   * @param path
   * @return
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#createAnnotationModel(org.eclipse.core.runtime.IPath)
   */
  public IAnnotationModel createAnnotationModel(IPath path) {
    IResource file= ResourcesPlugin.getWorkspace().getRoot().findMember(path);
    if (file instanceof IFile)
            return new SCAnnotationModel(file);
    return new AnnotationModel();
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getModificationStamp(java.lang.Object)
   */
  public long getModificationStamp(Object element) {
    return provider.getModificationStamp(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getSynchronizationStamp(java.lang.Object)
   */
  public long getSynchronizationStamp(Object element) {
    return provider.getSynchronizationStamp(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isDeleted(java.lang.Object)
   */
  public boolean isDeleted(Object element) {
    return provider.isDeleted(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#mustSaveDocument(java.lang.Object)
   */
  public boolean mustSaveDocument(Object element) {
    return provider.mustSaveDocument(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#canSaveDocument(java.lang.Object)
   */
  public boolean canSaveDocument(Object element) {
    return provider.canSaveDocument(element);
  }

  /**
   * @param element
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#aboutToChange(java.lang.Object)
   */
  public void aboutToChange(Object element) {
    provider.aboutToChange(element);
  }

  /**
   * @param element
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#changed(java.lang.Object)
   */
  public void changed(Object element) {
    provider.changed(element);
  }

  /**
   * @param listener
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#addElementStateListener(org.eclipse.ui.texteditor.IElementStateListener)
   */
  public void addElementStateListener(IElementStateListener listener) {
    provider.addElementStateListener(listener);
  }

  /**
   * @param listener
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#removeElementStateListener(org.eclipse.ui.texteditor.IElementStateListener)
   */
  public void removeElementStateListener(IElementStateListener listener) {
    provider.removeElementStateListener(listener);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isReadOnly(java.lang.Object)
   */
  public boolean isReadOnly(Object element) {
    return provider.isReadOnly(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isModifiable(java.lang.Object)
   */
  public boolean isModifiable(Object element) {
    return provider.isModifiable(element);
  }

  /**
   * @param element
   * @param computationContext
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#validateState(java.lang.Object, java.lang.Object)
   */
  public void validateState(Object element, Object computationContext) throws CoreException {
    provider.validateState(element, computationContext);
  }

  /**
   * @param element
   * @throws CoreException
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#connect(java.lang.Object)
   */
  public void connect(Object element) throws CoreException {
    provider.connect(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isStateValidated(java.lang.Object)
   */
  public boolean isStateValidated(Object element) {
    return provider.isStateValidated(element);
  }

  /**
   * @param element
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#updateStateCache(java.lang.Object)
   */
  public void updateStateCache(Object element) throws CoreException {
    provider.updateStateCache(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#getAnnotationModel(java.lang.Object)
   */
  public IAnnotationModel getAnnotationModel(Object element) {
    return provider.getAnnotationModel(element);
  }

  /**
   * @param element
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#disconnect(java.lang.Object)
   */
  public void disconnect(Object element) {
    provider.disconnect(element);
  }

  /**
   * @param element
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#setCanSaveDocument(java.lang.Object)
   */
  public void setCanSaveDocument(Object element) {
    provider.setCanSaveDocument(element);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getStatus(java.lang.Object)
   */
  public IStatus getStatus(Object element) {
    return provider.getStatus(element);
  }

  /**
   * @param element
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#synchronize(java.lang.Object)
   */
  public void synchronize(Object element) throws CoreException {
    provider.synchronize(element);
  }

  /**
   * @param progressMonitor
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void setProgressMonitor(IProgressMonitor progressMonitor) {
    provider.setProgressMonitor(progressMonitor);
  }

  /**
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getProgressMonitor()
   */
  public IProgressMonitor getProgressMonitor() {
    return provider.getProgressMonitor();
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isSynchronized(java.lang.Object)
   */
  public boolean isSynchronized(Object element) {
    return provider.isSynchronized(element);
  }

  /**
   * @param element
   * @param ex
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#isNotSynchronizedException(java.lang.Object, org.eclipse.core.runtime.CoreException)
   */
  public boolean isNotSynchronizedException(Object element, CoreException ex) {
    return provider.isNotSynchronizedException(element, ex);
  }

  /**
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getDefaultEncoding()
   */
  public String getDefaultEncoding() {
    return provider.getDefaultEncoding();
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getEncoding(java.lang.Object)
   */
  public String getEncoding(Object element) {
    return provider.getEncoding(element);
  }

  /**
   * @param element
   * @param encoding
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#setEncoding(java.lang.Object, java.lang.String)
   */
  public void setEncoding(Object element, String encoding) {
    provider.setEncoding(element, encoding);
  }

  /**
   * @param element
   * @return
   * @throws CoreException
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#getContentType(java.lang.Object)
   */
  public IContentType getContentType(Object element) throws CoreException {
    return provider.getContentType(element);
  }

  /**
   * @param savePolicy
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#setSavePolicy(org.eclipse.jdt.internal.ui.javaeditor.ISavePolicy)
   */
  public void setSavePolicy(ISavePolicy savePolicy) {
    provider.setSavePolicy(savePolicy);
  }

  /**
   * @param listener
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#addGlobalAnnotationModelListener(org.eclipse.jface.text.source.IAnnotationModelListener)
   */
  public void addGlobalAnnotationModelListener(IAnnotationModelListener listener) {
    provider.addGlobalAnnotationModelListener(listener);
  }

  /**
   * @param listener
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#removeGlobalAnnotationModelListener(org.eclipse.jface.text.source.IAnnotationModelListener)
   */
  public void removeGlobalAnnotationModelListener(IAnnotationModelListener listener) {
    provider.removeGlobalAnnotationModelListener(listener);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#getWorkingCopy(java.lang.Object)
   */
  public ICompilationUnit getWorkingCopy(Object element) {
    return provider.getWorkingCopy(element);
  }

  /**
   * 
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#shutdown()
   */
  public void shutdown() {
    provider.shutdown();
  }

  /**
   * @param monitor
   * @param element
   * @param document
   * @param overwrite
   * @throws CoreException
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#saveDocumentContent(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
   */
  public void saveDocumentContent(IProgressMonitor monitor, Object element, IDocument document,
      boolean overwrite) throws CoreException {
    provider.saveDocumentContent(monitor, element, document, overwrite);
  }

  /**
   * @param element
   * @return
   * @see org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider#createLineTracker(java.lang.Object)
   */
  public ILineTracker createLineTracker(Object element) {
    return provider.createLineTracker(element);
  }
}
