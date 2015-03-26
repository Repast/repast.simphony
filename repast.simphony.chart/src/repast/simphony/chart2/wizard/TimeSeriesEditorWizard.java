/*CopyrightHere*/
package repast.simphony.chart2.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.ui.plugin.editor.PluginWizard;

/**
 */
public class TimeSeriesEditorWizard {
  
  protected TimeSeriesWizardModel model;
  protected Wizard wizard;
  
  public TimeSeriesEditorWizard(List<DataSetDescriptor> dataSets) {
    this(dataSets, null);
  }

  public TimeSeriesEditorWizard(List<DataSetDescriptor> dataSets, TimeSeriesChartDescriptor descriptor) {
    if (descriptor != null) {
      model = new TimeSeriesWizardModel(dataSets, descriptor);
    } else {
      model = new TimeSeriesWizardModel(dataSets);
    }

    buildPath(descriptor == null);
    wizard = new PluginWizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }
  
  public List<PanelWizardStep> getStepsForEditor() {
    List<PanelWizardStep> steps = new ArrayList<PanelWizardStep>();
    TimeSeriesGeneralStep dataStep = new TimeSeriesGeneralStep();
    dataStep.disableDataSelection();
    steps.add(dataStep);
    if (model.getDataSetType() == DataSetType.NON_AGGREGATE)
      steps.add(new NonAggregateChartStep());
    else 
      steps.add(new AggregateChartStep());
    
    steps.add(new ChartPropertiesStep());
    
    return steps;
  }

  private void buildPath(boolean showGeneralStep) {
    if (showGeneralStep) model.add(new TimeSeriesGeneralStep());
    model.add(new NonAggregateChartStep(), new Condition() {
      public boolean evaluate(WizardModel wizardModel) {
        TimeSeriesWizardModel model = (TimeSeriesWizardModel) wizardModel;
        return model.getDataSetType() == DataSetType.NON_AGGREGATE;
      }
    });
    
    model.add(new AggregateChartStep(), new Condition() {
      public boolean evaluate(WizardModel wizardModel) {
        TimeSeriesWizardModel model = (TimeSeriesWizardModel) wizardModel;
        return model.getDataSetType() == DataSetType.AGGREGATE;
      }
    });
    
    model.add(new ChartPropertiesStep());
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public TimeSeriesWizardModel getModel() {
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
