package repast.simphony.visualization.gui;

import java.awt.Component;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Creates a wizard when adding a new display to the scenario tree. 
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * 
 */
public class DisplayWizardCreator {

  private DisplayConfigurationWizard wizard;
  private DisplayComponentControllerAction action;

  public DisplayWizardCreator(Object contextID, ContextData context, DisplayComponentControllerAction action) {
    
  	
//  	String displayType = action.getDescriptor().getDisplayType();
  	
  	// TODO Projections: new descriptors are null here from create display
  	
//  	DisplayConfigurationWizardFactory fac = 
//  			new DisplayConfigurationWizardFactoryFinder().getFactoryFor(displayType);
  	
//  	wizard = fac.getConfigurationWizard(contextID, action.getDescriptor(), context);
  	
  	wizard = new DisplayConfigurationWizard(contextID, null, context);
  	
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
