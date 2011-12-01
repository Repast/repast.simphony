package repast.simphony.ui;

import javax.swing.JPopupMenu;

import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * @author Nick Collier
 */
public class DefaultActionUI implements ActionUI {

	private String label;

	public DefaultActionUI(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Editor getEditor(ScenarioTreeEvent evt) {
		return null;
	}

	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
		return null;
	}
}
