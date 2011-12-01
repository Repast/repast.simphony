package repast.simphony.chart2.gui;

import repast.simphony.chart2.engine.HistogramComponentControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class HistogramChartActionEditorCreator implements
    ActionEditorCreator<HistogramComponentControllerAction> {

  /**
   * Creates an editor for the specfied action.
   * 
   * @param action
   *          the action to create the editor for
   * @return an editor for the specified action.
   */
  public ActionUI createEditor(HistogramComponentControllerAction action) {
    return new HistogramActionUI(action);
  }

  public Class<?> getActionType() {
    return HistogramComponentControllerAction.class;
  }
}
