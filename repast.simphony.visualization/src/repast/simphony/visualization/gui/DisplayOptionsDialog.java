package repast.simphony.visualization.gui;

import java.awt.Component;
import java.awt.Dimension;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.visualization.engine.DefaultDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Options dialog for editing a display. This takes the steps used by the dialog
 * creation wizard and makes them each an option pane.
 * 
 * @author Nick Collier
 */
public class DisplayOptionsDialog extends OptionsEditorDialog implements Editor {
  private static final long serialVersionUID = -5983444933550736114L;

  private DisplayComponentControllerAction action;

  private DisplayWizardModel model;

  // note that the contextId may not be the id of the root context.
  public DisplayOptionsDialog(String contextId, ContextData rootContext, DisplayComponentControllerAction action) {
    super();
    // create the model and the steps.
    this.action = action;
    DisplayDescriptor descriptor = new DefaultDisplayDescriptor();
    descriptor.set(action.getDescriptor());

    NameOnlyGeneralStep gStep = new NameOnlyGeneralStep();
    SimplePath path = new SimplePath();
    path.addStep(gStep);
    DisplayConfigurationWizard displayWizard = new DisplayConfigurationWizard(contextId, descriptor, rootContext);

    model = displayWizard.getModel();
    Wizard wizard = displayWizard.getWizard();

    addContent("General", null, gStep, wizard);

    // we want to skip the first step, because we stick in the name only step
    while (model.isNextAvailable()) {
      model.nextStep();

      addContent(model.getActiveStep().getName(), null, (PanelWizardStep) model.getActiveStep(),
	  wizard);
    }
  }

  /**
   * calls super.ok() and then applies the descriptor to the action.
   */
  @Override
  protected void ok() {
    super.ok();
    DisplayDescriptor descriptor = model.getDescriptor();
    action.setDescriptor(descriptor);
  }

  /**
   * Initializes the dialog for display. This sets the layout, packs() it and so
   * forth.
   * 
   * @param parent
   */
  @Override
  protected void displayInit(Component parent) {
    super.displayInit(parent);
    // reset the size and location.
    dialog.setSize(new Dimension(650, 450));
    dialog.setLocationRelativeTo(parent);
  }
}
