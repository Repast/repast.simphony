package repast.simphony.ui.plugin.editor;

import javax.swing.JPanel;

/**
 * Interface for classes that wish to provide content in an options dialog.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface OptionsDialogContent {

	/**
	 * Invoked when this OptionsDialogContent is selected.
	 */
	void selected();

	/**
	 * Invoked when ok button is pressed.
	 *
	 * @throws InvalidStateException if the action taken fails.
	 */
	void ok() throws InvalidStateException;

	/**
	 * Invoked when the apply button is pressed.
	 *
	 * @throws InvalidStateException if the apply fails.
	 */
	void apply() throws InvalidStateException;

	/**
	 * Invoked when the cancel button is pressed.
	 */
	void cancel();

	/**
	 * Gets the panel to be shown in the dialog.
	 *
	 * @return the panel to be shown in the dialog.
	 */
	JPanel getPanel();
}
