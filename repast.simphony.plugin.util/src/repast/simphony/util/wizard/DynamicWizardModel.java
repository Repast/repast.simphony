package repast.simphony.util.wizard;

import org.pietschy.wizard.WizardEvent;
import org.pietschy.wizard.WizardListener;
import org.pietschy.wizard.models.MultiPathModel;
import org.pietschy.wizard.models.Path;

import repast.simphony.scenario.Scenario;

public class DynamicWizardModel<T extends WizardOption> extends MultiPathModel implements WizardListener {

	protected Object contextID;
	protected Scenario scenario;
	private T chosenOption;

	public DynamicWizardModel(Path path, Scenario scenario, Object contextID) {
		super(path);
		this.scenario = scenario;
		this.contextID = contextID;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public Object getContextID() {
		return contextID;
	}
	
	public T getChosenOption() {
		return this.chosenOption;
	}
	
	public void setChosenOption(T option) {
		this.chosenOption = option;
	}

	public void wizardCancelled(WizardEvent event) {
		
	}

	public void wizardClosed(WizardEvent event) {
	}
}
