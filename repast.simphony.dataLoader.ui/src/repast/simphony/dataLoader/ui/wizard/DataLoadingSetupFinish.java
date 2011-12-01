package repast.simphony.dataLoader.ui.wizard;

import javax.swing.JLabel;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

public class DataLoadingSetupFinish extends PanelWizardStep {
	private static final long serialVersionUID = 5578377917258860531L;

  private DataLoaderWizardModel model;

  public DataLoadingSetupFinish() {
    super("Finished", "Your data source is ready to use");
    add(new JLabel("Your data source is ready to use in your model"));
		setComplete(true);
	}

  @Override
  public void init(WizardModel wizardModel) {
    model = (DataLoaderWizardModel) wizardModel;
  }

  public void applyState() {
    model.getBuilder();
  }
}
