/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.groovy.core.util.ReflectionUtils;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.text.java.IJavaReconcilingListener;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.IDocumentProvider;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

/**
 * Adapts the Groovy editor for editing Statechart code properties.
 * 
 * @author Nick Collier
 */
public class StatechartGroovyEditor extends GroovyEditor implements StatechartCodeEditor {

  private IEditorInput input;
  private ViewerSupport support;
  private IWorkbenchPartSite site;

  private IDocument doc;

  private GroovySourceViewer viewer, importViewer;
  private GroovySemanticReconciler semanticReconciler;

  public StatechartGroovyEditor() {
    setPreferenceStore(JavaPlugin.getDefault().getCombinedPreferenceStore());
  }

  /**
   * Gets the viewer for this editor.
   * 
   * @return the viewer for this editor.
   */
  public GroovySourceViewer getCodeViewer() {
    return viewer;
  }

  /**
   * Gets the viewer for the imports.
   * 
   * @return the viewer for the imports.
   */
  public GroovySourceViewer getImportViewer() {
    return importViewer;
  }

  public void createPartControl(IWorkbenchPartSite site, Composite parent) {
    this.site = site;

    CTabFolder tabFolder = new CTabFolder(parent, SWT.FLAT);
    tabFolder.setTabHeight(20);
    tabFolder.setTabPosition(SWT.BOTTOM);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    tabFolder.setLayoutData(data);
    tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
        SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

    CTabItem item = new CTabItem(tabFolder, SWT.NONE);
    item.setText("Code");
    Composite comp = new Composite(tabFolder, SWT.NONE);
    comp.setLayout(new GridLayout(1, true));
    comp.setLayoutData(data);
    item.setControl(comp);

    viewer = new GroovySourceViewer(comp, new VerticalRuler(VERTICAL_RULER_WIDTH), null);// getOverviewRuler());
    viewer.getTextWidget().getParent().setLayoutData(data);
    // getSourceViewerDecorationSupport(viewer);

    IPreferenceStore prefStore = getPreferenceStore();
    viewer.configure(getPreferenceStore(), this);
    installGroovySemanticHighlighting(viewer);
    getSourceViewerDecorationSupport(viewer).install(prefStore);

    item = new CTabItem(tabFolder, SWT.NONE);
    item.setText("Imports");
    comp = new Composite(tabFolder, SWT.NONE);
    comp.setLayout(new GridLayout(1, true));
    comp.setLayoutData(data);
    item.setControl(comp);

    importViewer = new GroovySourceViewer(comp, new VerticalRuler(VERTICAL_RULER_WIDTH), null);// getOverviewRuler());
    importViewer.getTextWidget().getParent().setLayoutData(data);

    importViewer.configure(prefStore, this);
    installGroovySemanticHighlighting(importViewer);
    getSourceViewerDecorationSupport(importViewer).install(prefStore);

    importViewer.ignoreAutoIndent(true);
    tabFolder.setSelection(0);
  }

  public void init(IWorkbenchPartSite site, IEditorInput input, int lineOffset) {
    viewer.ignoreAutoIndent(true);
    if (doc != null) {
      doc.getDocumentPartitioner().disconnect();
    }
    setEditorInput(input);
    this.site = site;
    JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
    IDocumentPartitioner partitioner = textTools.createDocumentPartitioner();
    doc = getDocumentProvider().getDocument(input);
    doc.setDocumentPartitioner(partitioner);
    partitioner.connect(doc);
    IAnnotationModel model = getDocumentProvider().getAnnotationModel(input);

    try {
      int offset = doc.getLineOffset(doc.getNumberOfLines() - lineOffset);
      viewer.setDocument(doc, model, offset, 0);
      importViewer.setDocument(doc, model, doc.getLineOffset(1), 0);
    } catch (BadLocationException e) {
      StatechartDiagramEditorPlugin.getInstance()
          .logError("Error creating code editor document", e);
    }

    doc.addDocumentListener(new IDocumentListener() {
      @Override
      public void documentAboutToBeChanged(DocumentEvent event) {
      }

      // this is necessary because the autocompletion adds text to the
      // document but does not notify the text widget. Consequently,
      // adding text via text completion doesn't set the inserted code
      // as the property of the eObject via binding.
      @Override
      public void documentChanged(DocumentEvent event) {
        getCodeTextWidget().notifyListeners(SWT.Modify, null);
        getImportTextWidget().notifyListeners(SWT.Modify, null);
      }
    });

    // sets up the keyboard actions
    if (support == null)
      support = new ViewerSupport(viewer, (IHandlerService) site.getService(IHandlerService.class));

    viewer.ignoreAutoIndent(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  @Override
  public void dispose() {
    if (fSourceViewerDecorationSupport != null) fSourceViewerDecorationSupport.uninstall();
    viewer.unconfigure();
    importViewer.unconfigure();
    uninstallGroovySemanticHighlighting();
    super.dispose();
  }

  // from GroovyEditor
  private void installGroovySemanticHighlighting(GroovySourceViewer viewer) {
    try {
      //fSemanticManager.uninstall();
      semanticReconciler = new GroovySemanticReconciler();
      semanticReconciler.install(this, viewer);
      ReflectionUtils
          .executePrivateMethod(CompilationUnitEditor.class, "addReconcileListener",
              new Class[] { IJavaReconcilingListener.class }, this,
              new Object[] { semanticReconciler });

    } catch (SecurityException e) {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "Unable to install semantic reconciler for groovy editor", e);
    }
  }

  // from GroovyEditor 
  private boolean semanticHighlightingInstalled() {
    return semanticReconciler != null;
  }

  // from GroovyEditor
  private void uninstallGroovySemanticHighlighting() {
    if (semanticHighlightingInstalled()) {
      try {
        semanticReconciler.uninstall();
        ReflectionUtils.executePrivateMethod(CompilationUnitEditor.class,
            "removeReconcileListener", new Class[] { IJavaReconcilingListener.class }, this,
            new Object[] { semanticReconciler });
        semanticReconciler = null;
      } catch (SecurityException e) {
        StatechartDiagramEditorPlugin.getInstance().logError("Unable to uninstall semantic reconciler for groovy editor", e);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorPart#getEditorInput()
   */
  @Override
  public IEditorInput getEditorInput() {
    return input;
  }

  public void setEditorInput(IEditorInput input) {
    IDocumentProvider provider = getDocumentProvider();
    if (this.input != null) {
      provider.disconnect(this.input);
      uninstallGroovySemanticHighlighting();
      fSourceViewerDecorationSupport.uninstall();
      fSourceViewerDecorationSupport = null;
    }

    this.input = input;

    try {
      provider.connect(input);
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorPart#getEditorSite()
   */
  @Override
  public IEditorSite getEditorSite() {
    // no editor site so return null
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
   * org.eclipse.ui.IEditorInput)
   */
  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#getSite()
   */
  @Override
  public IWorkbenchPartSite getSite() {
    return site;
  }

  public StyledText getCodeTextWidget() {
    return viewer.getTextWidget();
  }

  public StyledText getImportTextWidget() {
    return importViewer.getTextWidget();
  }

  /*
   * public boolean hasErrors() { for (Iterator iter =
   * viewer.getAnnotationModel().getAnnotationIterator(); iter.hasNext(); ) {
   * Annotation ann = (Annotation)iter.next(); // error ones have type of
   * "org.eclipse.jdt.ui.error" //System.out.println(ann.); } return false; }
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#getDocumentProvider()
   */
  // @Override
  // public IDocumentProvider getDocumentProvider() {
  // return provider;
  // }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#addPropertyListener(org.eclipse.ui.
   * IPropertyListener)
   */
  @Override
  public void addPropertyListener(IPropertyListener listener) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#getTitle()
   */
  @Override
  public String getTitle() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#getTitleImage()
   */
  @Override
  public Image getTitleImage() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#getTitleToolTip()
   */
  @Override
  public String getTitleToolTip() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#removePropertyListener(org.eclipse.ui.
   * IPropertyListener)
   */
  @Override
  public void removePropertyListener(IPropertyListener listener) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPart#setFocus()
   */
  @Override
  public void setFocus() {
    viewer.getTextWidget().setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
   */
  @Override
  public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor
   * )
   */
  @Override
  public void doSave(IProgressMonitor monitor) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#doSaveAs()
   */
  @Override
  public void doSaveAs() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isDirty()
   */
  @Override
  public boolean isDirty() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
   */
  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
   */
  @Override
  public boolean isSaveOnCloseNeeded() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#close(boolean)
   */
  @Override
  public void close(boolean save) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#isEditable()
   */
  @Override
  public boolean isEditable() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#doRevertToSaved()
   */
  @Override
  public void doRevertToSaved() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#setAction(java.lang.String,
   * org.eclipse.jface.action.IAction)
   */
  @Override
  public void setAction(String actionID, IAction action) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#getAction(java.lang.String)
   */
  @Override
  public IAction getAction(String actionId) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.texteditor.ITextEditor#setActionActivationCode(java.lang
   * .String, char, int, int)
   */
  @Override
  public void setActionActivationCode(String actionId, char activationCharacter,
      int activationKeyCode, int activationStateMask) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.texteditor.ITextEditor#removeActionActivationCode(java.lang
   * .String)
   */
  @Override
  public void removeActionActivationCode(String actionId) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#showsHighlightRangeOnly()
   */
  @Override
  public boolean showsHighlightRangeOnly() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#showHighlightRangeOnly(boolean)
   */
  @Override
  public void showHighlightRangeOnly(boolean showHighlightRangeOnly) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#setHighlightRange(int, int,
   * boolean)
   */
  @Override
  public void setHighlightRange(int offset, int length, boolean moveCursor) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#getHighlightRange()
   */
  @Override
  public IRegion getHighlightRange() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#resetHighlightRange()
   */
  @Override
  public void resetHighlightRange() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#getSelectionProvider()
   */
  @Override
  public ISelectionProvider getSelectionProvider() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.texteditor.ITextEditor#selectAndReveal(int, int)
   */
  @Override
  public void selectAndReveal(int offset, int length) {
  }

}
