package repast.simphony.util.wizard;

import org.pietschy.wizard.WizardModel;

public class ModelAwarePanelStep<T extends WizardModel> extends CompleteHandlerWizardStep {
	private static final long serialVersionUID = -6973120014012365373L;
	
	protected T model;
	
	
	public ModelAwarePanelStep() {
		super();
	}

	public ModelAwarePanelStep(java.lang.String name,
            java.lang.String summary,
            javax.swing.Icon icon) {
		super(name, summary, icon);
	}

	public ModelAwarePanelStep(java.lang.String name,
            java.lang.String summary) {
		super(name, summary);
	}



	@Override
	public void init(WizardModel wizModel) {
		super.init(wizModel);
		
		this.model = (T) wizModel;
	}
}
