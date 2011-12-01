/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardStep;

import repast.simphony.data.engine.DataGathererDescriptor;

/**
 * 
 * 
 * @author Jerry Vos
 */
public class DataSetEditorWizard {
  protected DataSetWizardModel model;

  protected Wizard wizard;

  private ArrayList<PanelWizardStep> steps;

  public DataSetEditorWizard(List<Class<?>> agentClasses) {
    this(agentClasses, null);
  }

  public DataSetEditorWizard(List<Class<?>> agentClasses, DataGathererDescriptor descriptor) {
    super();
    Collections.sort(agentClasses, new Comparator<Class<?>>() {
      public int compare(Class<?> o1, Class<?> o2) {
        return o1.getSimpleName().compareTo(o2.getSimpleName());
      }
    });

    if (descriptor != null) {
      model = new DataSetWizardModel(agentClasses, descriptor);
    } else {
      model = new DataSetWizardModel(agentClasses);
    }
    buildPath();
    wizard = new Wizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }

  private void buildPath() {
    steps = new ArrayList<PanelWizardStep>();
    steps.add(new DataSetGeneralStep());
    steps.add(new DataMappingsStep());
    // steps.add(new AggregateDataMappingsStep());
    steps.add(new DataSetScheduleStep());

    for (WizardStep step : steps) {
      model.add(step);
    }
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public DataSetWizardModel getModel() {
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

  public Collection<PanelWizardStep> getSteps() {
    return steps;
  }

  public static void main(String[] args) {
    DataSetEditorWizard wiz = new DataSetEditorWizard(new ArrayList<Class<?>>());

    wiz.showDialog(null, "");
  }
}
