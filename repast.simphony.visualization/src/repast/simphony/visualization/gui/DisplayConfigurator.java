package repast.simphony.visualization.gui;

import java.awt.Component;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DisplayConfigurator {

  private DisplayConfigurationWizard wizard;
  private DisplayComponentControllerAction action;

  public DisplayConfigurator(Object contextID, ContextData context, DisplayComponentControllerAction action) {
    wizard = new DisplayConfigurationWizard(contextID, action.getDescriptor(), context);
    this.action = action;
  }

  public void showDialog(Component parent) {
    wizard.showDialog(parent);
  }

  public DisplayComponentControllerAction getAction() {
    DisplayDescriptor descriptor = wizard.getDescriptor();
    if (descriptor == null) return null;
    action.setDescriptor(descriptor);
    return action;
  }
}
