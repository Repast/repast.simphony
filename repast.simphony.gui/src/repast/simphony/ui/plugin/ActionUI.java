package repast.simphony.ui.plugin;

import javax.swing.JPopupMenu;

import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Interface for classes that are user interfaces for ControllerActions.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public interface ActionUI {

	/**
	 * @return a label for the action as it appears in the tree.
	 */
  public String getLabel();

	/**
	 * Gets the Editor (an editor panel) for editing an action.
	 *
	 * @param evt the ScenarioTreeEvent that triggered this method call.
	 * @return the Editor (an editor panel) for editing an action.
	 */
	public Editor getEditor(ScenarioTreeEvent evt);

	/**
	 * Gets JPopupMenu to attach to the tree entry for an action. This may return null for
	 * no popup.
	 *

	 * @return JPopupMenu to attach to the tree entry for an action. This may return null for
	 * no popup.
	 */
	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt);

}
