package repast.simphony.data2.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.data2.wizard.DataSetEditorWizard;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;

/**
 * Implements the "Add Data Set" menu item.
 * 
 * @author Nick Collier
 */
public class DataSetMenuItem extends AbstractEditorMenuItem {
  private static final long serialVersionUID = 5878701522312055100L;

  public DataSetMenuItem() {
    super("Add Data Set");
  }

  private DataSetComponentControllerAction createAction(Component parent, List<Class<?>> agentClasses) {
    DataSetEditorWizard wizard = new DataSetEditorWizard(agentClasses);
    wizard.showDialog(parent, "Data Set Editor");
    if (wizard.wasCancelled()) return null;

    DataSetComponentControllerAction action = new DataSetComponentControllerAction(wizard.getModel().getDescriptor());
    return action;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    // evt is the scenario tree event that triggered this event
    Object contextID = evt.getContextID();
    List<Class<?>> clazzes = evt.getScenario().getContext().getAgentClasses(true);
    DataSetComponentControllerAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()),
            clazzes);
    if (action != null) {
      ControllerRegistry registry = evt.getRegistry();

      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_SET_ROOT);
      registry.addAction(contextID, parent, action);
      evt.addActionToTree(action);
      action.getDescriptor().addScenarioChangedListener(evt.getScenario());
      evt.getScenario().setDirty(true);
    }
  }
}
