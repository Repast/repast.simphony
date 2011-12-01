/**
 * 
 */
package repast.simphony.data2.gui;

import java.util.List;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.data2.wizard.DataSetEditorWizard;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Provides the scenario tree user inteface for data set editing.
 * 
 * @author Nick Collier
 */
public class DataSetActionUI extends DescriptorActionUI<DataSetDescriptor> {
  
  private DataSetComponentControllerAction action;

  public DataSetActionUI(DataSetComponentControllerAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
    List<Class<?>> clazzes = evt.getScenario().getContext().getAgentClasses(true);
    DataSetEditorWizard wizard = new DataSetEditorWizard(clazzes, action.getDescriptor());

    String title = action.getDescriptor().getType() == 
        DataSetType.AGGREGATE ? "Aggregate Data Set Editor" : "Non-Aggregate Data Set Editor";
    OptionsEditorDialog dialog = new OptionsEditorDialog(title);

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getStepsForEditor()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
