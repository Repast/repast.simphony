package repast.simphony.data2.gui;

import repast.simphony.data2.engine.ConsoleSinkComponentControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ConsoleSinkActionEditorCreator implements ActionEditorCreator<ConsoleSinkComponentControllerAction> {

  /**
   * Creates an editor for the specfied action.
   * 
   * @param action
   *          the action to create the editor for
   * @return an editor for the specified action.
   */
  public ActionUI createEditor(ConsoleSinkComponentControllerAction action) {
    return new ConsoleSinkActionUI(action);
  }

  public Class<?> getActionType() {
    return ConsoleSinkComponentControllerAction.class;
  }
}
