/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.FileSinkDescriptor;

/**
 */
public class FileSinkEditorWizard {
  
  protected FileSinkWizardModel model;
  protected Wizard wizard;
  
  public FileSinkEditorWizard(List<DataSetDescriptor> dataSets) {
    this(dataSets, null);
  }

  public FileSinkEditorWizard(List<DataSetDescriptor> dataSets, FileSinkDescriptor descriptor) {
    if (descriptor != null) {
      model = new FileSinkWizardModel(dataSets, descriptor);
    } else {
      model = new FileSinkWizardModel(dataSets);
    }

    buildPath();
    wizard = new Wizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }
  
  public List<PanelWizardStep> getStepsForEditor() {
    List<PanelWizardStep> steps = new ArrayList<PanelWizardStep>();
    steps.add(new SinkDataStep<FileSinkDescriptor, FileSinkWizardModel>("File DataProperties", ""));
    steps.add(new FileSinkPropertiesStep());
   
    return steps;
  }

  private void buildPath() {
    model.add(new SinkDataStep<FileSinkDescriptor, FileSinkWizardModel>( "File Data Properties",
        "Please enter the file sink's name, data set, and the data sources to record as output."));
    model.add(new FileSinkPropertiesStep());
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public FileSinkWizardModel getModel() {
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
