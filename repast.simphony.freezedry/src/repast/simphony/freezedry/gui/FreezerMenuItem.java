package repast.simphony.freezedry.gui;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.java.plugin.PluginManager;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.freezedry.wizard.FreezeDryWizardModel;
import repast.simphony.freezedry.wizard.FreezeDryWizardPluginUtil;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;
import repast.simphony.util.wizard.DynamicWizard;

/**
 * A menu item that allows a person to add a scheduled freeze drying through the freeze dryer
 * wizard.
 * 
 * @author Jerry Vos
 */
public class FreezerMenuItem extends AbstractEditorMenuItem {
	private static final long serialVersionUID = 1L;

	public FreezerMenuItem() {
		super("Add Freeze Drying Action");
	}

	private ControllerAction createAction(JFrame parentFrame, Scenario scenario, Object contextID) {
		DynamicWizard wizard = FreezeDryWizardPluginUtil.create(scenario, contextID);
		wizard.showDialog(parentFrame, "Freeze Drying Action Wizard");
		if (!wizard.wasCancelled()) {
			return ((FreezeDryWizardModel) wizard.getModel()).getAction();
		}
		return null;
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		ControllerRegistry registry = evt.getRegistry();
		Object contextID = evt.getContextID();
		ControllerAction action = createAction((JFrame) SwingUtilities.getWindowAncestor(evt
				.getTree()), evt.getScenario(), evt.getContextID());
		if (action != null) {
			ControllerAction parent = registry.findAction(contextID,
					ControllerActionConstants.OUTPUTTER_ROOT);
			registry.addAction(contextID, parent, action);
			evt.addActionToTree(action);
		}
	}

	@Override
	public void init(PluginManager manager) {
		FreezeDryWizardPluginUtil.loadWizardOptions(manager);
	}
}
