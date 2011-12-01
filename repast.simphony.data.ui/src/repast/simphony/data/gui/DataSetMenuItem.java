package repast.simphony.data.gui;

import org.java.plugin.PluginManager;
import repast.simphony.data.engine.DefaultDataGathererDescriptorAction;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.ScenarioLoader;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

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

  private DefaultDataGathererDescriptorAction createAction(Component parent, List<Class<?>> agentClasses) {
    DataSetEditorWizard wizard = new DataSetEditorWizard(agentClasses);
    wizard.showDialog(parent, "Data Set Editor");
    if (wizard.wasCancelled()) return null;

    DefaultDataGathererDescriptorAction action = new DefaultDataGathererDescriptorAction<Object>(
            wizard.getModel().getDescriptor());
    return action;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    Object contextID = evt.getContextID();
    List<Class<?>> clazzes = evt.getScenario().getContext().getAgentClasses(true);
    DefaultDataGathererDescriptorAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()),
            clazzes);
    if (action != null) {
      ControllerRegistry registry = evt.getRegistry();

      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_SET_ROOT);
      registry.addAction(contextID, parent, action);
      evt.addActionToTree(action);
    }
  }

  @Override
  public void init(PluginManager manager) {
    DataMappingWizardPluginUtil.loadWizardOptions(manager);
  }
}
