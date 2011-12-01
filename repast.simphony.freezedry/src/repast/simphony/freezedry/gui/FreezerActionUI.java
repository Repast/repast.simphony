package repast.simphony.freezedry.gui;

import java.awt.Dimension;

import repast.simphony.freezedry.wizard.FreezeDryWizardOption;
import repast.simphony.freezedry.wizard.FreezerControllerAction;
import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.tree.ScenarioTreeEvent;

public class FreezerActionUI extends DefaultActionUI {

	private FreezeDryWizardOption option;
	private FreezerControllerAction action;

	public FreezerActionUI(FreezerControllerAction action, FreezeDryWizardOption option, String title) {
		super(title);
		this.action = action;
		this.option = option;
	}

	public FreezerEditor getEditor(ScenarioTreeEvent evt) {
		FreezerEditor editor = new FreezerEditor(option, action, evt.getScenario(), evt
				.getContextID(), "");
		
		editor.setPreferredSize(new Dimension(450, 350));
		
		return editor;
	}
}
