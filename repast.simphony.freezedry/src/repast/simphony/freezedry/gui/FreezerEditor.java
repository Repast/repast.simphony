package repast.simphony.freezedry.gui;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.freezedry.wizard.ChooseContextStep;
import repast.simphony.freezedry.wizard.FreezeDryScheduleStep;
import repast.simphony.freezedry.wizard.FreezeDryWizardModel;
import repast.simphony.freezedry.wizard.FreezeDryWizardOption;
import repast.simphony.freezedry.wizard.FreezerControllerAction;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.plugin.editor.AbstractWizardEditor;

public class FreezerEditor extends AbstractWizardEditor<FreezerControllerAction> {
	private static final long serialVersionUID = 1L;
	
	private FreezeDryWizardOption option;

	public FreezerEditor(FreezeDryWizardOption option, FreezerControllerAction action, Scenario scenario, Object contextId, String title) {
		super(action, scenario, contextId, title);
		this.option = option;
	}

	@Override
	protected SimplePath getPath() {
		SimplePath path = new SimplePath();
		path.addStep(new ChooseContextStep());
		path.addStep(new FreezeDryScheduleStep());
		for (WizardStep step : (Iterable<WizardStep>) option.getWizardPath().getSteps()) {
			path.addStep(step);
		}
		
		return path;
	}

	@Override
	protected WizardModel getWizardModel(SimplePath path, Scenario scenario, Object contextId) {
		FreezeDryWizardModel model = new FreezeDryWizardModel(path, scenario, contextId);
		
		model.setChosenOption(option);
		model.setAction(action);
		
		return model;
	}
}
