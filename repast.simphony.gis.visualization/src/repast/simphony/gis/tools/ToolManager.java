package repast.simphony.gis.tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ToolManager {

  private JToolBar toolBar = new JToolBar();

  private Map<Action, AbstractButton> actionMap = new HashMap<Action, AbstractButton>();

  public static final String TOGGLE = "toggle";

  public static final String SELECTED = "selected";

  private ButtonGroup group = new ButtonGroup();

  public void setTool(Action action) {

  }

  public void addTool(final Action action) {
    AbstractButton button = null;
    Boolean toggle = (Boolean) action.getValue(TOGGLE);
    if (toggle != null && toggle) {
      button = new JToggleButton(action);
      toolBar.add(button);
      group.add(button);
      Boolean selected = (Boolean) action.getValue(SELECTED);
      if (selected != null && selected) {
        button.setSelected(true);
      }
    } else {
      button = toolBar.add(action);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
          setTool(action);
        }
      });
    }
    actionMap.put(action, button);
  }

  public JToolBar getToolBar() {
    return toolBar;
  }

  public void addToGroup(ButtonGroup group) {
    for (AbstractButton button : actionMap.values()) {
      group.add(button);
      button.setSelected(false);
    }
  }

  public void removeFromGroup(ButtonGroup group) {
    for (AbstractButton button : actionMap.values()) {
      group.remove(button);
    }
  }
}
