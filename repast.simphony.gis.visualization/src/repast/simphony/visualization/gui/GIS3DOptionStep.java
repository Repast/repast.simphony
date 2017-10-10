package repast.simphony.visualization.gui;

import javax.swing.JPanel;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Display wizard step for setting GIS 3D options
 * 
 * @author Eric Tatara
 */
public class GIS3DOptionStep extends PluginWizardStep {

  private DisplayWizardModel model;
  private GIS3DOptionsPanel panel;

  public GIS3DOptionStep() {
    super("GIS Options", "Set GIS options.");
    
    setComplete(true);
  }
  
  @Override
	protected  JPanel getContentPanel(){
  	panel = new GIS3DOptionsPanel();
  	return panel;
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
    panel.applyChanges();
  }
}
