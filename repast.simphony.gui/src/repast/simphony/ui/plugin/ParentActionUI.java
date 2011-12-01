package repast.simphony.ui.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPopupMenu;

import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Default user interfaces for composite or parent actions.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public class ParentActionUI implements ActionUI {

	private String label;
	private List<EditorMenuItem> items = new ArrayList<EditorMenuItem>();

	public ParentActionUI(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Editor getEditor(ScenarioTreeEvent evt) {
		return null;
	}

	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
		JPopupMenu menu = new JPopupMenu();
		Collections.sort(items);
		for (EditorMenuItem item : items) {
			item.setScenarioEvt(evt);
			menu.add(item);
		}
		return menu;
	}

	public void addEditorMenuItem(EditorMenuItem item) {
		items.add(item);
	}
}
