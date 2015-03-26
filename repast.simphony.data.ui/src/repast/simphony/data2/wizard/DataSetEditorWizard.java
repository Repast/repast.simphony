/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.ui.plugin.editor.PluginWizard;

/**
 */
public class DataSetEditorWizard {
  protected DataSetWizardModel model;

  protected Wizard wizard;
  
  public DataSetEditorWizard(List<Class<?>> agentClasses) {
    this(agentClasses, null);
  }

  public DataSetEditorWizard(List<Class<?>> agentClasses, DataSetDescriptor descriptor) {
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
    wizard = new PluginWizard(model);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
    model.setLastVisible(false);
  }
  
  public List<PanelWizardStep> getStepsForEditor() {
    List<PanelWizardStep> steps = new ArrayList<PanelWizardStep>();
    DataSetGeneralStep generalStep = new DataSetGeneralStep();
    generalStep.disableTypeSelection();
    
    steps.add(generalStep);
    if (model.getDescriptor().getType() == DataSetType.AGGREGATE) {
      steps.add(new AggregateSourceStep());
    } else {
      steps.add(new NonAggregateSourceStep());
    }
    steps.add(new DataSetScheduleStep());
    return steps;
  }

  private void buildPath() {
    model.add(new DataSetGeneralStep());
    model.add(new NonAggregateSourceStep(), new Condition() {
      public boolean evaluate(WizardModel wizardModel) {
        DataSetWizardModel model = (DataSetWizardModel) wizardModel;
        return model.getDescriptor().getType() == DataSetType.NON_AGGREGATE;
      }
    });
    
    model.add(new AggregateSourceStep(), new Condition() {
      public boolean evaluate(WizardModel wizardModel) {
        DataSetWizardModel model = (DataSetWizardModel) wizardModel;
        return model.getDescriptor().getType() == DataSetType.AGGREGATE;
      }
    });
    
    model.add(new DataSetScheduleStep());
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
}
