/*CopyrightHere*/
package repast.simphony.chart2.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizard;

/**
 */
public class HistogramEditorWizard {
  
  protected HistogramWizardModel model;
  protected Wizard wizard;
  
  public HistogramEditorWizard(List<DataSetDescriptor> dataSets) {
    this(dataSets, null);
  }

  public HistogramEditorWizard(List<DataSetDescriptor> dataSets, HistogramChartDescriptor descriptor) {
    if (descriptor != null) {
      model = new HistogramWizardModel(dataSets, descriptor);
    } else {
      model = new HistogramWizardModel(dataSets);
    }

    buildPath(descriptor == null);
    wizard = new PluginWizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }
  
  public List<PanelWizardStep> getStepsForEditor() {
    List<PanelWizardStep> steps = new ArrayList<PanelWizardStep>();
    HistogramDataStep dataStep = new HistogramDataStep();
    dataStep.disableDataSelection();
    steps.add(dataStep);
    steps.add(new HistogramPropertiesStep());
    steps.add(new HistogramChartPropertiesStep());
    
    return steps;
  }

  private void buildPath(boolean showDataStep) {
    if (showDataStep) model.add(new HistogramDataStep());
    model.add(new HistogramPropertiesStep());
    model.add(new HistogramChartPropertiesStep());
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public HistogramWizardModel getModel() {
    return model;
  }

  public boolean wasCancelled() {
    if (wizard == null) {
      return true;
    } else {
      return wizard.wasCanceled();
    }
  }

  public Wizard getWizard() {
    return wizard;
  }
}
