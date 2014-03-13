package repast.simphony.visualization.gui;

import java.awt.BorderLayout;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

/**
 * @author Nick Collier
 */
public class GISStyleStep extends PanelWizardStep {

  private DisplayWizardModel model;
  private GISStylePanel panel = new GISStylePanel();

  public GISStyleStep() {
    super("Agent Style", "Please provide a style for each agent type in the model and the " +
            "order in which the layers will appear");
    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
    setComplete(true);
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
