/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;

import repast.simphony.data2.engine.ConsoleSinkDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizard;

/**
 */
public class ConsoleSinkEditorWizard {
  
  protected ConsoleSinkWizardModel model;
  protected Wizard wizard;
  
  public ConsoleSinkEditorWizard(List<DataSetDescriptor> dataSets) {
    this(dataSets, null);
  }

  public ConsoleSinkEditorWizard(List<DataSetDescriptor> dataSets, ConsoleSinkDescriptor descriptor) {
    if (descriptor != null) {
      model = new ConsoleSinkWizardModel(dataSets, descriptor);
    } else {
      model = new ConsoleSinkWizardModel(dataSets);
    }

    buildPath();
    wizard = new PluginWizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }
  
  public List<PanelWizardStep> getStepsForEditor() {
    List<PanelWizardStep> steps = new ArrayList<PanelWizardStep>();
    steps.add(new SinkDataStep<ConsoleSinkDescriptor, ConsoleSinkWizardModel>("Console Data Properties",""));
    steps.add(new ConsoleSinkPropertiesStep());
    return steps;
  }

  private void buildPath() {
    model.add(new SinkDataStep<ConsoleSinkDescriptor, ConsoleSinkWizardModel>("Console Data Properties",
        "Please enter the console sink's name, data set, and the data sources to record as output."));
    model.add(new ConsoleSinkPropertiesStep());
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public ConsoleSinkWizardModel getModel() {
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
