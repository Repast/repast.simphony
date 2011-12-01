package repast.simphony.dataLoader.gui;

import repast.simphony.dataLoader.engine.JDBCDataLoaderControllerAction;
import repast.simphony.dataLoader.wizard.JDBCDataLoaderOption;
import repast.simphony.dataLoader.ui.DataLoaderActionUI;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class JDBCDataLoaderActionEditorCreator implements ActionEditorCreator<JDBCDataLoaderControllerAction> {
	public static class JDBCDataLoaderActionUI extends DataLoaderActionUI<JDBCDataLoaderControllerAction> {

		public JDBCDataLoaderActionUI(JDBCDataLoaderControllerAction action) {
			super(action, new JDBCDataLoaderOption(), "JDBC Data Loader");
		}
	}
	
	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(JDBCDataLoaderControllerAction action) {
		return new JDBCDataLoaderActionUI(action);
	}

	public Class getActionType() {
		return JDBCDataLoaderControllerAction.class;
	}
}
