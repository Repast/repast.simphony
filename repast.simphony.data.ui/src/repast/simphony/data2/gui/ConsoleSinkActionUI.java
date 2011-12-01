/**
 * 
 */
package repast.simphony.data2.gui;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.data2.engine.ConsoleSinkComponentControllerAction;
import repast.simphony.data2.engine.ConsoleSinkDescriptor;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.data2.wizard.ConsoleSinkEditorWizard;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Provides the scenario tree user inteface for console sink editing.
 * 
 * @author Nick Collier
 */
public class ConsoleSinkActionUI extends DescriptorActionUI<ConsoleSinkDescriptor> {
  
  private ConsoleSinkComponentControllerAction action;

  public ConsoleSinkActionUI(ConsoleSinkComponentControllerAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
  
    ConsoleSinkEditorWizard wizard = new ConsoleSinkEditorWizard(DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()),
        action.getDescriptor());

    OptionsEditorDialog dialog = new OptionsEditorDialog("Console Sink Editor");

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getStepsForEditor()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
