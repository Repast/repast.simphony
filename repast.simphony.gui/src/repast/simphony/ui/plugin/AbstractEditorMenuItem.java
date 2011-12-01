package repast.simphony.ui.plugin;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.java.plugin.PluginManager;

import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Partial implementation of the EditorMenuItem interface. This implements setting and getting a
 * label, and setting the ScenarioTreeEvent. The user only needs to implement
 * <code>actionPerformed(ActionEvent evt)</code>. This also implements Comparable by comparing on
 * EditorMenuItem labels.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public abstract class AbstractEditorMenuItem extends AbstractAction implements EditorMenuItem {

	protected ScenarioTreeEvent evt;

	/**
	 * Creates an AbstractEditorMenuItem with the specified label.
	 * 
	 * @param label
	 */
	public AbstractEditorMenuItem(String label) {
		super(label);
	}

	/**
	 * Sets the ScenarioTreeEvent that triggers this menu item.
	 * 
	 * @param evt
	 *            the ScenarioTreeEvent that trigged this menu item.
	 */
	public void setScenarioEvt(ScenarioTreeEvent evt) {
		this.evt = evt;
	}

	/**
	 * Gets the label for this menu item.
	 * 
	 * @return the label for this menu item.
	 */
	public String getLabel() {
		return (String) getValue(Action.NAME);
	}

	public int compareTo(EditorMenuItem editorMenuItem) {
		return this.getLabel().compareTo(editorMenuItem.getLabel());
	}

	/**
	 * A null implementation of this method.
	 * 
	 * @param manager
	 *            ignored.
	 */
	public void init(PluginManager manager) {
		// do nothing
	}
}
