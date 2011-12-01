package repast.simphony.data2.gui;

import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DataSetActionEditorCreator implements ActionEditorCreator<DataSetComponentControllerAction> {

  /**
   * Creates an editor for the specfied action.
   * 
   * @param action
   *          the action to create the editor for
   * @return an editor for the specified action.
   */
  public ActionUI createEditor(DataSetComponentControllerAction action) {
    return new DataSetActionUI(action);
  }

  public Class<?> getActionType() {
    return DataSetComponentControllerAction.class;
  }
}
