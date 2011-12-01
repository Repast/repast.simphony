package repast.simphony.dataLoader.ui.wizard;

import org.pietschy.wizard.WizardEvent;
import org.pietschy.wizard.models.Path;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.scenario.Scenario;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.util.wizard.DynamicWizardModel;
import simphony.util.messages.MessageCenter;

public class DataLoaderWizardModel extends DynamicWizardModel<DataLoaderWizardOption> {
  private static MessageCenter LOG = MessageCenter.getMessageCenter(DataLoaderWizardModel.class);

  private DataLoaderControllerAction action = null;
  private ContextActionBuilder contextActionBuilder;

  private DataLoaderWizardOption contextBuilderOption;

  public DataLoaderWizardModel(Path path, Scenario scenario, Object contextID) {
    super(path, scenario, contextID);
  }

  public ContextActionBuilder getBuilder() {
    if (contextActionBuilder == null || !getChosenOption().equals(contextBuilderOption)) {
      contextBuilderOption = getChosenOption();
      if (action == null) {
	contextActionBuilder = contextBuilderOption.createBuilder(null);
      } else {
	contextActionBuilder = contextBuilderOption.createBuilder(action.getBuilder());
      }
    }
    return contextActionBuilder;
  }

  public void wizardCancelled(WizardEvent arg0) {
    // action = null;
  }

  public void wizardClosed(WizardEvent arg0) {
    ContextData dataContext = getScenario().getContext().find(getContextID().toString());
    
    if (dataContext == null) {
      LOG.warn("Couldn't find a context for id '" + getContextID().toString()
	  + "', cannot create an action.");
      return;
    }

    if (action != null) {
      // in this case we're working based off a pre-existing action
      // so we assume the generated action is of the same form (meaning with the
      // same data loader
      // type)
      DataLoaderControllerAction newAction = contextActionBuilder.getAction(scenario, dataContext
	  .getId());
      action.setBuilder(newAction.getBuilder());
    } else {
      action = contextActionBuilder.getAction(scenario, dataContext.getId());
    }

  }

  public DataLoaderControllerAction getAction() {
    return action;
  }

  public void setAction(DataLoaderControllerAction action) {
    this.action = action;
  }
}
