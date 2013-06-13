package repast.simphony.dataLoader.ui;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import org.java.plugin.PluginManager;

import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardModel;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardPluginUtil;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;
import repast.simphony.util.wizard.DynamicWizard;

/**
 * @author Nick Collier
 */
public class DefaultDataLoaderMenuItem extends AbstractEditorMenuItem {
	private static final long serialVersionUID = 8226064471416757568L;

	public DefaultDataLoaderMenuItem() {
		super("Set Data Loader");
	}

	public void actionPerformed(ActionEvent e) {
		Object contextID = evt.getContextID();
		ControllerRegistry registry = evt.getRegistry();
		DynamicWizard wizard = DataLoaderWizardPluginUtil.create(evt.getScenario(), contextID);
		wizard.showDialog(SwingUtilities.getWindowAncestor(evt.getTree()), "");
		ControllerAction action = ((DataLoaderWizardModel) wizard.getModel()).getAction();
		if (action != null) {
			ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_LOADER_ROOT);
			for (ControllerAction child : registry.getActionTree(contextID).getChildren(parent)) {
				registry.removeAction(contextID, child);
				evt.removeActionFromTree(child);
			}
			registry.addAction(contextID, parent, action);
			evt.addActionToTree(action);
			evt.getScenario().setDirty(true);
		}
	}
	
	@Override
	public void init(PluginManager manager) {
		// grab all the available wizard options
		DataLoaderWizardPluginUtil.loadWizardOptions(manager);
	}
}
