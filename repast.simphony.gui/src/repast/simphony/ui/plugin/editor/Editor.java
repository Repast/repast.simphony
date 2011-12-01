package repast.simphony.ui.plugin.editor;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Interface for classes that implement an editor. An Editor is displayed by the
 * double clicking on a node in the scenario tree. For example, if the user
 * had previously created a display and then double clicked on the node
 * representing that display, then the Editor for that display would be shown.
 *
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface Editor {

	/**
	 * Display this Editor with the specified component as a parent.
	 * <b>The resulting display should be modal.</b>
	 *
	 * @param parent
	 */
	void display(JFrame parent);

	/**
	 * Display this Editor with the specified component as a parent.
	 * <b>The resulting display should be modal.</b>
	 *
	 * @param parent
	 */
	void display(JDialog parent);

	/**
	 * @return true if the editing was canceled, otherwise false.
	 */
	boolean wasCanceled();
}
