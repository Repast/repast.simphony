package repast.simphony.data.gui;

import java.util.List;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.data.engine.DataGathererDescriptor;
import repast.simphony.data.engine.DefaultDataGathererDescriptorAction;
import repast.simphony.scenario.ScenarioLoader;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Provides the scenario tree user interface for Data Gathering.
 * @author Nick Collier
 */
public class DataGathererDescriptorActionUI extends DescriptorActionUI<DataGathererDescriptor> {

  private DefaultDataGathererDescriptorAction action;

  public DataGathererDescriptorActionUI(DefaultDataGathererDescriptorAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
    List<Class<?>> clazzes = evt.getScenario().getContext().getAgentClasses(true);
    DataSetEditorWizard wizard = new DataSetEditorWizard(clazzes, action.getDescriptor());

    OptionsEditorDialog dialog = new OptionsEditorDialog();

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getSteps()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
