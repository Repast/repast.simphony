package repast.simphony.dataLoader.ui;

import java.awt.Dimension;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardOption;
import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * @author Jerry Vos
 */
public abstract class DataLoaderActionUI<T extends DataLoaderControllerAction> extends DefaultActionUI {

	private T action;
	private DataLoaderWizardOption option;


	public DataLoaderActionUI(T action, DataLoaderWizardOption option, String label) {
		super(label);
		this.action = action;
		this.option = option;
	}
	
	@Override
	public DataLoaderEditor getEditor(ScenarioTreeEvent evt) {
		DataLoaderEditor editor = new DataLoaderEditor(option, action, evt.getScenario(), evt
				.getContextID(), "");
		
		editor.setPreferredSize(new Dimension(450, 300));
		
		return editor;
	}
}
