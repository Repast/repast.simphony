/*CopyrightHere*/
package repast.simphony.util.wizard;

import org.pietschy.wizard.*;
import org.pietschy.wizard.models.BranchingPath;
import org.pietschy.wizard.models.Condition;
import org.pietschy.wizard.models.SimplePath;
import repast.simphony.scenario.Scenario;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DynamicWizard implements WizardListener {
  private static final MessageCenter LOG = MessageCenter.getMessageCenter(DynamicWizard.class);

  private static Map<String, List<WizardOption>> wizardOptions;

  private Wizard wizard;

  private DynamicWizardModel model;

  private ChooseOptionStep firstStep;

  private WizardStep finishStep;

  private String wizardId;

  private boolean cancelled;

  public DynamicWizard(String wizardId, Scenario scenario, Object contextID,
                       String firstStepTitle, String firstStepPrompt, WizardStep finishStep,
                       WizardModelFactory modelFactory, WizardStep... firstSteps) {
    this.finishStep = finishStep;
    this.wizardId = wizardId;

    BranchingPath firstPath = createPaths(firstStepTitle, firstStepPrompt, firstSteps);
    model = modelFactory.create(firstPath, scenario, contextID);
    wizard = new Wizard(model);
    wizard.addWizardListener(model);
    wizard.addWizardListener(this);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);

  }

  class SelectedCondition implements Condition {
    WizardOption option;

    public SelectedCondition(WizardOption option) {
      super();
      this.option = option;
    }

    public boolean evaluate(WizardModel arg0) {
      return firstStep.getSelectedOption() == option;
    }
  }

  private BranchingPath createPaths(String firstStepTitle, String firstStepPrompt, WizardStep... firstSteps) {
    BranchingPath firstPath = new BranchingPath();
    firstStep = new ChooseOptionStep(firstStepTitle, firstStepPrompt);
    if (wizardOptions == null || wizardOptions.get(wizardId) == null) {
      LOG.warn("No wizard options for wizard id '" + wizardId + "' have been set.");
      return null;
    } else {
      firstStep.setOptions(wizardOptions.get(wizardId));
    }

    firstPath.addStep(firstStep);

    for (WizardStep step : firstSteps) {
      firstPath.addStep(step);
    }

    SimplePath endPath = new SimplePath();
    endPath.addStep(finishStep);


    for (WizardOption option : wizardOptions.get(wizardId)) {
      SimplePath optionPath = option.getWizardPath();
      if (optionPath == null || optionPath.getSteps().size() == 0) {
        firstPath.addBranch(endPath, new SelectedCondition(option));
      } else {
        firstPath.addBranch(optionPath, new SelectedCondition(option));
        optionPath.setNextPath(endPath);
      }
    }

    return firstPath;
  }

  public void showDialog(Component component, String title) {
    wizard.showInDialog(title, component, true);
  }

  public DynamicWizardModel getModel() {
    return model;
  }

  public static void registerWizardOption(String wizardId, WizardOption option) {
    if (wizardOptions == null) {
      wizardOptions = new HashMap<String, List<WizardOption>>();
    }
    if (wizardOptions.get(wizardId) == null) {
      wizardOptions.put(wizardId, new LinkedList<WizardOption>());
    }
    wizardOptions.get(wizardId).add(option);
  }

  public static Collection<WizardOption> getWizardOptions(String wizardId) {
    if (wizardOptions == null) {
      return null;
    } else if (wizardOptions.get(wizardId) == null) {
      return null;
    }
    return Collections.unmodifiableCollection(wizardOptions.get(wizardId));
  }

  public void wizardClosed(WizardEvent arg0) {
    cancelled = false;
  }

  public void wizardCancelled(WizardEvent arg0) {
    cancelled = true;
  }

  public boolean wasCancelled() {
    return cancelled;
  }
}
