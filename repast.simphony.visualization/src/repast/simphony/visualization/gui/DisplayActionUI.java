package repast.simphony.visualization.gui;

import javax.swing.JPopupMenu;

import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;

/**
 * Editor for editing individual component display actions.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DisplayActionUI implements ActionUI {

  private DisplayComponentControllerAction action;

  public DisplayActionUI(DisplayComponentControllerAction action) {
    this.action = action;
  }

  public String getLabel() {
    return action.getDescriptor().getName();
  }

  public Editor getEditor(ScenarioTreeEvent evt) {
    return new DisplayOptionsDialog(evt.getContextID().toString(), evt.getScenario().getContext(), action);
  }

  public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
    return null;
  }
}
