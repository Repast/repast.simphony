/**
 * 
 */
package repast.simphony.chart2.gui;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.chart2.engine.TimeSeriesComponentControllerAction;
import repast.simphony.chart2.wizard.TimeSeriesEditorWizard;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Provides the scenario tree user inteface for console sink editing.
 * 
 * @author Nick Collier
 */
public class TimeSeriesActionUI extends DescriptorActionUI<TimeSeriesChartDescriptor> {
  
  private TimeSeriesComponentControllerAction action;

  public TimeSeriesActionUI(TimeSeriesComponentControllerAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
  
    TimeSeriesEditorWizard wizard = new TimeSeriesEditorWizard(DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()),
        action.getDescriptor());

    OptionsEditorDialog dialog = new OptionsEditorDialog("Time Series Editor");

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getStepsForEditor()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
