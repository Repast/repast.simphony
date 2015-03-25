package repast.simphony.visualization.gui;

import java.awt.BorderLayout;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;

/**
 * Wizard step for setting GIS 3D options
 * 
 * @author Eric Tatara
 */
public class GIS3DOptionStep extends PanelWizardStep {

  private DisplayWizardModel model;
  private GIS3DOptionsPanel panel = new GIS3DOptionsPanel();

  public GIS3DOptionStep() {
    super("GIS Options", "Set GIS options.");
    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
    setComplete(true);
  }

  @Override
  public void prepare() {
    super.prepare();
    panel.init(model.getContext(), (GISDisplayDescriptor)model.getDescriptor());
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
