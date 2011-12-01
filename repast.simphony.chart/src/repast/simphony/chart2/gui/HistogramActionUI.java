/**
 * 
 */
package repast.simphony.chart2.gui;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.chart2.engine.HistogramComponentControllerAction;
import repast.simphony.chart2.wizard.HistogramEditorWizard;
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
public class HistogramActionUI extends DescriptorActionUI<HistogramChartDescriptor> {
  
  private HistogramComponentControllerAction action;

  public HistogramActionUI(HistogramComponentControllerAction action) {
    super(action.getDescriptor());
    this.action = action;
  }

  @Override
  public Editor getEditor(ScenarioTreeEvent evt) {
  
    HistogramEditorWizard wizard = new HistogramEditorWizard(DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()),
        action.getDescriptor());

    OptionsEditorDialog dialog = new OptionsEditorDialog("Histogram Editor");

    for (PanelWizardStep step : (Iterable<PanelWizardStep>) wizard.getStepsForEditor()) {
      dialog.addContent(step.getName(), null, step, wizard.getWizard());
    }

    return dialog;
  }
}
