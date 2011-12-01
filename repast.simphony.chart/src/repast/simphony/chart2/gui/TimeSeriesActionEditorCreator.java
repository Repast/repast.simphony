package repast.simphony.chart2.gui;

import repast.simphony.chart2.engine.TimeSeriesComponentControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class TimeSeriesActionEditorCreator implements
    ActionEditorCreator<TimeSeriesComponentControllerAction> {

  /**
   * Creates an editor for the specfied action.
   * 
   * @param action
   *          the action to create the editor for
   * @return an editor for the specified action.
   */
  public ActionUI createEditor(TimeSeriesComponentControllerAction action) {
    return new TimeSeriesActionUI(action);
  }

  public Class<?> getActionType() {
    return TimeSeriesComponentControllerAction.class;
  }
}
