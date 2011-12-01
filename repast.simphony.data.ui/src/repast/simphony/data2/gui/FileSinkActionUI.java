/**
 * 
 */
package repast.simphony.data2.gui;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.data2.engine.FileSinkComponentControllerAction;
import repast.simphony.data2.engine.FileSinkDescriptor;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.data2.wizard.FileSinkEditorWizard;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Provides the scenario tree user inteface for data set editing.
 * 
 * @author Nick Collier
 */
public class FileSinkActionUI extends DescriptorActionUI<FileSinkDescriptor> {
  
  private FileSinkComponentControllerAction action;

  public FileSinkActionUI(FileSinkComponentControllerAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
    FileSinkEditorWizard wizard = new FileSinkEditorWizard(DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()),
        action.getDescriptor());

    OptionsEditorDialog dialog = new OptionsEditorDialog("File Sink Editor");

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getStepsForEditor()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
