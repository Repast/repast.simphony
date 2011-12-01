package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BorderLayout;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardModel;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.util.wizard.ModelAwarePanelStep;

public class ContextBuilderWizardStep extends ModelAwarePanelStep {
	private static final long serialVersionUID = 8999135654803570217L;
	
	private AdderPanel panel;
	private DataLoaderWizardModel myModel;

	public ContextBuilderWizardStep() {
		super("Build Context", "Use the panel below to build your new Context.");
	}

	@Override
	public void init(WizardModel model) {
		myModel = (DataLoaderWizardModel) model;
		ContextData spec = myModel.getScenario().getContext();
		ContextData newContext = spec.find(myModel.getContextID().toString());
		
		if (newContext == null) {
			throw new RuntimeException(
					"Error initializing ContextBuilderStep, couldn't find the correct context (id='"
							+ myModel.getContextID() + "')");
		}
		panel = new AdderPanel(new ContextDescriptor(newContext));
		this.setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}

	@Override
	public void prepare() {
		setComplete(true);
	}

	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		((GUIContextActionBuilder) myModel.getBuilder()).setDescriptor(panel.getContextDescriptor());
	}
}
