package repast.simphony.visualization.gui;

import javax.swing.JPanel;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Display wizard step for setting GIS style
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class GISStyleStep extends PluginWizardStep {

  private DisplayWizardModel model;
  private GISStylePanel panel = new GISStylePanel();

  public GISStyleStep() {
    super("Agent Style", "Please provide a style for each agent type in the model and the " +
            "order in which the layers will appear");
    
    setComplete(true);
  }
  
  @Override
	protected  JPanel getContentPanel(){ 
  	panel = new GISStylePanel();
  	return panel;
  }

  @Override
  public void prepare() {
    super.prepare();
    panel.init(model.getContext(), model.getDescriptor());
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    panel.done();
  }
}
