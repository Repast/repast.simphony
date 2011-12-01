package repast.simphony.data2.gui;

import repast.simphony.data2.engine.FileSinkComponentControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class FileSinkActionEditorCreator implements ActionEditorCreator<FileSinkComponentControllerAction> {

  /**
   * Creates an editor for the specfied action.
   * 
   * @param action
   *          the action to create the editor for
   * @return an editor for the specified action.
   */
  public ActionUI createEditor(FileSinkComponentControllerAction action) {
    return new FileSinkActionUI(action);
  }

  public Class<?> getActionType() {
    return FileSinkComponentControllerAction.class;
  }
}
