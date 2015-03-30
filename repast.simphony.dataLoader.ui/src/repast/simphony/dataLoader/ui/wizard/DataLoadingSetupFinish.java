package repast.simphony.dataLoader.ui.wizard;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

public class DataLoadingSetupFinish extends PluginWizardStep {
	private static final long serialVersionUID = 5578377917258860531L;

  private DataLoaderWizardModel model;

  public DataLoadingSetupFinish() {
    super("Finished", "Your data source is ready to use");
    setComplete(true);
  }
  
  @Override
	protected JPanel getContentPanel(){
    JPanel panel = new JPanel();
  	panel.add(new JLabel("Your data source is ready to use in your model"));
		
  	return panel;
	}

  @Override
  public void init(WizardModel wizardModel) {
    model = (DataLoaderWizardModel) wizardModel;
  }

  public void applyState() {
    model.getBuilder();
  }
}
