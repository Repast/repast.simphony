package repast.simphony.userpanel.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.SwingUtilities;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;
import repast.simphony.util.ClassFinder;
import repast.simphony.util.ClassPathEntry;

public class UserPanelMenuItem extends AbstractEditorMenuItem {

	public UserPanelMenuItem() {
		super("Specify User Panel");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2743056843382284999L;

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		ControllerRegistry registry = evt.getRegistry();
	    Object contextID = evt.getContextID();
	    //Finds services implementing UserPanelCreator
//	    List clazzes = new ArrayList();
	    List<Class<?>> clazzes = new ImplementingClassesFinder(evt.getScenario(), UserPanelCreator.class).findClasses();
	    
	    //TODO: add the ability to specify, if a class allows component creators, the component creator	
	    //Adding a DescriptorControllerAction
	    DefaultUserPanelDescriptorAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()),clazzes);
	   
	    if (action != null) {
	      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.USER_PANEL_ROOT);
	      for (ControllerAction child : registry.getActionTree(contextID).getChildren(parent)) {
				registry.removeAction(contextID, child);
				evt.removeActionFromTree(child);
			}
	      registry.addAction(contextID, parent, action);
	      evt.addActionToTree(action);
	      evt.getScenario().setDirty(true);
	      action.getDescriptor().addScenarioChangedListener(evt.getScenario());
	    }

	}

	private DefaultUserPanelDescriptorAction createAction(Component parent, List<Class<?>> clazzes) {
	    UserPanelEditorWizard wizard = new UserPanelEditorWizard(clazzes);
	    wizard.showDialog(parent, "User Panel Editor");
	    if (wizard.wasCancelled()) return null;

	    //This is where the descriptor is obtained from the wizard model
	    DefaultUserPanelDescriptorAction action = new DefaultUserPanelDescriptorAction(
	            wizard.getModel().getDescriptor());
	    return action;
	  }
}
