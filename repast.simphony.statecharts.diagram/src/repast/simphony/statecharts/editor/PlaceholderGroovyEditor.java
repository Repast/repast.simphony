/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Nick Collier
 */
public class PlaceholderGroovyEditor implements StatechartCodeEditor {
  
  private static class PGroovyViewer implements StatechartSourceViewer {
    
    private StyledText widget;
    
    public PGroovyViewer(Composite parent) {
      widget = new StyledText(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
        | SWT.FULL_SELECTION);
    }

    /* (non-Javadoc)
     * @see repast.simphony.statecharts.editor.StatechartSourceViewer#getTextWidget()
     */
    @Override
    public StyledText getTextWidget() {
      return widget;
    }

    /* (non-Javadoc)
     * @see repast.simphony.statecharts.editor.StatechartSourceViewer#ignoreAutoIndent(boolean)
     */
    @Override
    public void ignoreAutoIndent(boolean ignore) {}
  }
  
  private PGroovyViewer codeViewer;
  private PGroovyViewer importViewer;

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#createPartControl(org.eclipse.ui.IWorkbenchPartSite, org.eclipse.swt.widgets.Composite)
   */
  @Override
  public void createPartControl(IWorkbenchPartSite site, Composite parent) {
    
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
    codeViewer = new PGroovyViewer(comp);
    
    item = new CTabItem(tabFolder, SWT.NONE);
    item.setText("Imports");
    comp = new Composite(tabFolder, SWT.NONE);
    comp.setLayout(new GridLayout(1, true));
    comp.setLayoutData(data);
    item.setControl(comp);
    importViewer = new PGroovyViewer(comp);
    
    tabFolder.setSelection(0);
  }

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#getEditorInput()
   */
  @Override
  public IEditorInput getEditorInput() {
    return null;
  }

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#dispose()
   */
  @Override
  public void dispose() {
  }

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#init(org.eclipse.ui.IWorkbenchPartSite, org.eclipse.ui.IEditorInput, int)
   */
  @Override
  public void init(IWorkbenchPartSite site, IEditorInput input, int lineOffset) {
  }

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#getCodeViewer()
   */
  @Override
  public StatechartSourceViewer getCodeViewer() {
    return codeViewer;
  }

  /* (non-Javadoc)
   * @see repast.simphony.statecharts.editor.StatechartCodeEditor#getImportViewer()
   */
  @Override
  public StatechartSourceViewer getImportViewer() {
    return importViewer;
  }
  
  

}
