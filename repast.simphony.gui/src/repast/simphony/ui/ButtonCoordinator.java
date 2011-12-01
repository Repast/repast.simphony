package repast.simphony.ui;


import saf.core.ui.GUIBarManager;
import saf.core.ui.actions.ActionFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/03 18:23:02 $
 */
public class ButtonCoordinator {

  private JLabel tickCountLabel;

  public void setGUIForStarted(GUIBarManager config) {
    JButton button = (JButton) config.getToolBarComponent(RSGUIConstants.START_ID);
    button.setAction(ActionFactory.getInstance().getAction(RSGUIConstants.PAUSE_ID));
    button.setIcon(RSGUIConstants.PAUSE_ICON);
    button.setToolTipText("Pause");
    button = (JButton) config.getToolBarComponent(RSGUIConstants.STEP_ID);
    button.setEnabled(false);

    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.STOP_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(false);

    config.getMenuItem(RSGUIConstants.START_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.PAUSE_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.STEP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(false);

    config.setStatusBarText(RSGUIConstants.STATUS_BAR, "Running");
    
    ActionFactory.getInstance().getAction(RSGUIConstants.RESET_LAYOUT_ACTION).setEnabled(false);
    ActionFactory.getInstance().getAction(RSGUIConstants.SAVE_DEFAULT_LAYOUT_ACTION).setEnabled(false);
  }

  public void setGUIForStepped(GUIBarManager config) {
    config.getToolBarComponent(RSGUIConstants.STOP_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(false);
    ActionFactory.getInstance().getAction(RSGUIConstants.RESET_LAYOUT_ACTION).setEnabled(false);
    ActionFactory.getInstance().getAction(RSGUIConstants.SAVE_DEFAULT_LAYOUT_ACTION).setEnabled(false);
  }

  public void setGUIForPaused(GUIBarManager config) {
    JButton button = (JButton) config.getToolBarComponent(RSGUIConstants.START_ID);
    button.setAction(ActionFactory.getInstance().getAction(RSGUIConstants.START_ID));
    button.setIcon(RSGUIConstants.START_ICON);
    button.setToolTipText("Start");
    button = (JButton) config.getToolBarComponent(RSGUIConstants.STEP_ID);
    button.setEnabled(true);

    config.getMenuItem(RSGUIConstants.START_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.PAUSE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.STEP_ID).setEnabled(true);

    config.setStatusBarText(RSGUIConstants.STATUS_BAR, "Paused");
  }

  public void setGUIForStopped(GUIBarManager config) {

    JButton button = (JButton) config.getToolBarComponent(RSGUIConstants.START_ID);
    // set this action for this button back to the start action
    // if we just start and then stop, the current action for the button will be the
    // pause action -- given that pause and start operate on the same button.
    button.setAction(ActionFactory.getInstance().getAction(RSGUIConstants.START_ID));
    button.setIcon(RSGUIConstants.START_ICON);
    button.setToolTipText("Start");

    config.getToolBarComponent(RSGUIConstants.STEP_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.START_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(true);

    config.getMenuItem(RSGUIConstants.START_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.STEP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.PAUSE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(true);

    config.setStatusBarText(RSGUIConstants.STATUS_BAR, "Stopped");
  }

  public void setGUIForPostSimInit(GUIBarManager config) {
    setEnabled(config, RSGUIConstants.RUN_TOOLS, true);
    config.getToolBarComponent(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(false);

    config.getMenuItem(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(false);
    
    ActionFactory.getInstance().getAction(RSGUIConstants.RESET_LAYOUT_ACTION).setEnabled(false);
    ActionFactory.getInstance().getAction(RSGUIConstants.SAVE_DEFAULT_LAYOUT_ACTION).setEnabled(false);
  }

  public void setGUIForModelLoaded(GUIBarManager config) {
    // enable start, step, and open
    tickCountLabel = (JLabel) config.getToolBarComponent(RSGUIConstants.TICK_COUNT_LABEL);
    config.getToolBarComponent(RSGUIConstants.START_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.STEP_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.INIT_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(false);
    setEnabled(config, RSGUIConstants.RUN_TOOLS, false);

    config.getMenuItem(RSGUIConstants.START_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.STEP_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.INIT_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.PAUSE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(false);
    
    ActionFactory.getInstance().getAction(RSGUIConstants.RESET_LAYOUT_ACTION).setEnabled(true);
    ActionFactory.getInstance().getAction(RSGUIConstants.SAVE_DEFAULT_LAYOUT_ACTION).setEnabled(true);
  }

  private void setEnabled(GUIBarManager config, String groupId, boolean state) {
    for (Component comp : config.getComponents(groupId)) {
      comp.setEnabled(state);
    }
  }

  public void init(GUIBarManager config) {
    config.getToolBarComponent(RSGUIConstants.START_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.STEP_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getToolBarComponent(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getToolBarComponent(RSGUIConstants.RESET_ID).setEnabled(false);
    setEnabled(config, RSGUIConstants.RUN_TOOLS, false);

    config.getMenuItem(RSGUIConstants.START_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.INIT_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.OPEN_ID).setEnabled(true);
    config.getMenuItem(RSGUIConstants.SAVE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.SAVE_AS_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.STEP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.PAUSE_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.STOP_ID).setEnabled(false);
    config.getMenuItem(RSGUIConstants.RESET_ID).setEnabled(false);
    
    ActionFactory.getInstance().getAction(RSGUIConstants.RESET_LAYOUT_ACTION).setEnabled(false);
    ActionFactory.getInstance().getAction(RSGUIConstants.SAVE_DEFAULT_LAYOUT_ACTION).setEnabled(false);
  }

  public void updateTickCountLabel(String val) {
    tickCountLabel.setText(val);
  }
}
