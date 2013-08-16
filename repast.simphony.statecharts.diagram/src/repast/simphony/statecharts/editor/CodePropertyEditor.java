/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Minimal editor used for editing code properties. This is necessary in order to
 * use Eclipse's code completion etc. 
 * 
 * @author Nick Collier
 */
public class CodePropertyEditor implements ITextEditor {
  
  private IEditorInput input;
  private IDocumentProvider provider = new TextFileDocumentProvider();
  private JavaSourceViewer viewer;
  private ViewerSupport support;
  

  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#getEditorInput()
   */
  @Override
  public IEditorInput getEditorInput() {
    return input;
  }
  
  public void setEditorInput(IEditorInput input) {
    if (input != null) provider.disconnect(input);
    this.input = input;
    
    try {
      provider.connect(input);
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
  
//  protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
//    return new JavaSourceViewer(parent, ruler, styles);
//  }
  
  public void createPartControl(Composite parent) {
    // TODO partitioner can be reused
    viewer = new JavaSourceViewer(parent);
    viewer.configure(this);
    
    
    // set up actions etc.
  }
  
 
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#getEditorSite()
   */
  @Override
  public IEditorSite getEditorSite() {
    // no editor site so return null
    return null;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  
  public void init(IWorkbenchPartSite site, IEditorInput input) {
    setEditorInput(input);
    JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
    IDocumentPartitioner partitioner = textTools.createDocumentPartitioner();
    IDocument doc = provider.getDocument(input);
    doc.setDocumentPartitioner(partitioner);
    partitioner.connect(doc);
    viewer.setDocument(doc);
    
    if (support == null) support = new ViewerSupport(viewer, (IHandlerService)site.getService(IHandlerService.class));
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  @Override
  public void dispose() {
    // TODO FILL IN
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#getSite()
   */
  @Override
  public IWorkbenchPartSite getSite() {
    return null;
  }

  
  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#getDocumentProvider()
   */
  @Override
  public IDocumentProvider getDocumentProvider() {
    return provider;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#addPropertyListener(org.eclipse.ui.IPropertyListener)
   */
  @Override
  public void addPropertyListener(IPropertyListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#getTitle()
   */
  @Override
  public String getTitle() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#getTitleImage()
   */
  @Override
  public Image getTitleImage() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#getTitleToolTip()
   */
  @Override
  public String getTitleToolTip() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#removePropertyListener(org.eclipse.ui.IPropertyListener)
   */
  @Override
  public void removePropertyListener(IPropertyListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#setFocus()
   */
  @Override
  public void setFocus() {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  @Override
  public Object getAdapter(Class adapter) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  public void doSave(IProgressMonitor monitor) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.ISaveablePart#doSaveAs()
   */
  @Override
  public void doSaveAs() {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.ISaveablePart#isDirty()
   */
  @Override
  public boolean isDirty() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
   */
  @Override
  public boolean isSaveAsAllowed() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
   */
  @Override
  public boolean isSaveOnCloseNeeded() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#close(boolean)
   */
  @Override
  public void close(boolean save) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#isEditable()
   */
  @Override
  public boolean isEditable() {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#doRevertToSaved()
   */
  @Override
  public void doRevertToSaved() {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#setAction(java.lang.String, org.eclipse.jface.action.IAction)
   */
  @Override
  public void setAction(String actionID, IAction action) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#getAction(java.lang.String)
   */
  @Override
  public IAction getAction(String actionId) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#setActionActivationCode(java.lang.String, char, int, int)
   */
  @Override
  public void setActionActivationCode(String actionId, char activationCharacter,
      int activationKeyCode, int activationStateMask) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#removeActionActivationCode(java.lang.String)
   */
  @Override
  public void removeActionActivationCode(String actionId) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#showsHighlightRangeOnly()
   */
  @Override
  public boolean showsHighlightRangeOnly() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#showHighlightRangeOnly(boolean)
   */
  @Override
  public void showHighlightRangeOnly(boolean showHighlightRangeOnly) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#setHighlightRange(int, int, boolean)
   */
  @Override
  public void setHighlightRange(int offset, int length, boolean moveCursor) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#getHighlightRange()
   */
  @Override
  public IRegion getHighlightRange() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#resetHighlightRange()
   */
  @Override
  public void resetHighlightRange() {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#getSelectionProvider()
   */
  @Override
  public ISelectionProvider getSelectionProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.texteditor.ITextEditor#selectAndReveal(int, int)
   */
  @Override
  public void selectAndReveal(int offset, int length) {
    // TODO Auto-generated method stub
  }
}
