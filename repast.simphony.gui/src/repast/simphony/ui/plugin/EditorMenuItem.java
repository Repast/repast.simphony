package repast.simphony.ui.plugin;

import javax.swing.Action;

import org.java.plugin.PluginManager;

import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Interface for classes that are MenuItems in a parent / composite action's popup menu.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public interface EditorMenuItem extends Action, Comparable<EditorMenuItem> {

	/**
	 * Sets the ScenarioTreeEvent that triggers this menu item.
	 * 
	 * @param evt
	 *            the ScenarioTreeEvent that trigged this menu item.
	 */
	void setScenarioEvt(ScenarioTreeEvent evt);

	/**
	 * Gets the label for this menu item.
	 * 
	 * @return the label for this menu item.
	 */
	String getLabel();

	/**
	 * Called when the menu item is being initialized. This allows the menu item to use the manager
	 * if it needs it for any reason.
	 * 
	 * @param manager
	 *            the plugin manager
	 */
	void init(PluginManager manager);
}
