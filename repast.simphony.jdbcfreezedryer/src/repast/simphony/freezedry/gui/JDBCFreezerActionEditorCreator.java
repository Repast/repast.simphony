package repast.simphony.freezedry.gui;

import repast.simphony.freezedry.engine.JDBCFreezerControllerAction;
import repast.simphony.freezedry.wizard.JDBCFreezerOption;
import repast.simphony.freezedry.gui.FreezerActionUI;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Jerry Vos
 */
public class JDBCFreezerActionEditorCreator implements
		ActionEditorCreator<JDBCFreezerControllerAction> {
	public static class JDBCFreezerActionUI extends FreezerActionUI {

		public JDBCFreezerActionUI(JDBCFreezerControllerAction action) {
			super(action, new JDBCFreezerOption(), "JDBC Data Freeze Dryer");
		}
	}

	/**
	 * Creates an editor for the specfied action.
	 * 
	 * @param action
	 *            the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(JDBCFreezerControllerAction action) {
		return new JDBCFreezerActionUI(action);
	}

	public Class getActionType() {
		return JDBCFreezerControllerAction.class;
	}
}
