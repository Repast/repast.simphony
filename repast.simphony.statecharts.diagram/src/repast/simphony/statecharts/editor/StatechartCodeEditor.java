/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Interface for classes that implement editors for editing
 * Statechart code properties. 
 * 
 * @author Nick Collier
 */
public interface StatechartCodeEditor {

  /**
   * Creates the part control that will display the 
   * source to edit.
   * 
   * @param site
   * @param parent
   */
  void createPartControl(IWorkbenchPartSite site, Composite parent);

  /**
   * Gets the editor input of this editor. 
   *  
   * @return the editor input of this editor. 
   */
  IEditorInput getEditorInput();

  /**
   * Disposes of this editor.
   */
  void dispose();

  /**
   * Initializes this editor to work with the specified site, use the specified
   * input as its template file and set its StyledText to display at the 
   * specified offset.
   * 
   * @param site
   * @param input
   * @param lineOffset
   */
  void init(IWorkbenchPartSite site, IEditorInput input, int lineOffset);

  /**
   * Gets the viewer for the code body.
   * 
   * @return the viewer for the code body.
   */
  StatechartSourceViewer getCodeViewer();
  
  /**
   * Gets the viewer for the source code imports edited
   * by this editor.
   * 
   * @return the viewer for the source code imports edited
   * by this editor.
   */
  StatechartSourceViewer getImportViewer();

}
