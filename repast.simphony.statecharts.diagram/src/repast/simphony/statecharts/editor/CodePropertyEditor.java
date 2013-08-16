/**
 * 
 */
package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CommandNotMappedException;
import org.eclipse.ui.actions.ContributedAction;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.ConfigurationElementSorter;
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
  
  public void createPartControl(Composite parent) {
    // TODO partitioner can be reused
    JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
    IDocumentPartitioner partitioner = textTools.createDocumentPartitioner();
    
    IDocument doc = provider.getDocument(input);
    doc.setDocumentPartitioner(partitioner);
    partitioner.connect(doc);
    
    JavaSourceViewer viewer = new JavaSourceViewer(parent);
    viewer.configure(this);
    viewer.setDocument(doc);
  }
  
  /*
   * @see ITextEditor#getAction(String)
   */
  public IAction getAction(String actionID) {
          Assert.isNotNull(actionID);
          IAction action= (IAction) fActions.get(actionID);

          if (action == null) {
                  action= findContributedAction(actionID);
                  if (action != null)
                          setAction(actionID, action);
          }

          return action;
  }

  /**
   * Returns the action with the given action id that has been contributed via XML to this editor.
   * The lookup honors the dependencies of plug-ins.
   *
   * @param actionID the action id to look up
   * @return the action that has been contributed
   * @since 2.0
   */
  private IAction findContributedAction(String actionID) {
          List actions= new ArrayList();
          IConfigurationElement[] elements= Platform.getExtensionRegistry().getConfigurationElementsFor(PlatformUI.PLUGIN_ID, "editorActions"); //$NON-NLS-1$
          for (int i= 0; i < elements.length; i++) {
                  IConfigurationElement element= elements[i];
                  if (TAG_CONTRIBUTION_TYPE.equals(element.getName())) {
                          if (!getSite().getId().equals(element.getAttribute("targetID"))) //$NON-NLS-1$
                                  continue;

                          IConfigurationElement[] children= element.getChildren("action"); //$NON-NLS-1$
                          for (int j= 0; j < children.length; j++) {
                                  IConfigurationElement child= children[j];
                                  if (actionID.equals(child.getAttribute("actionID"))) //$NON-NLS-1$
                                          actions.add(child);
                          }
                  }
          }
          int actionSize= actions.size();
          if (actionSize > 0) {
                  IConfigurationElement element;
                  if (actionSize > 1) {
                          IConfigurationElement[] actionArray= (IConfigurationElement[])actions.toArray(new IConfigurationElement[actionSize]);
                          ConfigurationElementSorter sorter= new ConfigurationElementSorter() {
                                  /*
                                   * @see org.eclipse.ui.texteditor.ConfigurationElementSorter#getConfigurationElement(java.lang.Object)
                                   */
                                  public IConfigurationElement getConfigurationElement(Object object) {
                                          return (IConfigurationElement)object;
                                  }
                          };
                          sorter.sort(actionArray);
                          element= actionArray[0];
                  } else
                          element= (IConfigurationElement)actions.get(0);

                  try {
                          return new ContributedAction(getSite(), element);
                  } catch (CommandNotMappedException e) {
                          // out of luck, no command action mapping
                  }
          }

          return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#getEditorSite()
   */
  @Override
  public IEditorSite getEditorSite() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#addPropertyListener(org.eclipse.ui.IPropertyListener)
   */
  @Override
  public void addPropertyListener(IPropertyListener listener) {
    // TODO Auto-generated method stub
    
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
    // TODO Auto-generated method stub
    return null;
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
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Class adapter) {
    // TODO Auto-generated method stub
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
    // TODO Auto-generated method stub
    return false;
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
  
  
  
  /*
  protected void doSetInput(IEditorInput input) throws CoreException {
    IEditorInput oldInput= getEditorInput();
    if (oldInput != null)
            getDocumentProvider().disconnect(oldInput);

    super.setInput(input);
    

    IDocumentProvider provider= getDocumentProvider();
    if (provider == null) {
            IStatus s= new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, EditorMessages.Editor_error_no_provider, null);
            throw new CoreException(s);
    }

    provider.connect(input);

  }
  */
  

}
