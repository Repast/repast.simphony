package repast.simphony.dataLoader.ui;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardModel;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardOption;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.plugin.editor.AbstractWizardEditor;

public class DataLoaderEditor extends AbstractWizardEditor<DataLoaderControllerAction> {
  private static final long serialVersionUID = 1L;
  private DataLoaderWizardOption option;

  public DataLoaderEditor(DataLoaderWizardOption option, DataLoaderControllerAction action,
      Scenario scenario, Object contextId, String title) {
    super(action, scenario, contextId, title);
    this.option = option;
  }

  @Override
  protected SimplePath getPath() {
    return option.getWizardPath();
  }

  @Override
  protected WizardModel getWizardModel(SimplePath path, Scenario scenario, Object contextId) {
    DataLoaderWizardModel model = new DataLoaderWizardModel(path, scenario, contextId);

    model.setChosenOption(option);
    model.setAction(action);

    return model;
  }

  /* (non-Javadoc)
   * @see repast.simphony.ui.plugin.editor.AbstractWizardEditor#ok()
   */
  @Override
  protected void ok() {
    super.ok();
    scenario.setDirty(true);
  }
  
  
}
