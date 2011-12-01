package repast.simphony.util.wizard;

import javax.swing.JLabel;

import org.pietschy.wizard.PanelWizardStep;

/**
 * This is just a simple step that displays a set of messages. This is intended
 * to be primarily used as the last step in a wizard, presenting some sort of
 * "this wizard has finished" information.<p/>
 * 
 * This step is always in a "completed" state, meaning in its construction it
 * does <code>setComplete(true)</code>.
 * 
 * @author Vos
 */
public class DefaultFinishStep extends PanelWizardStep {

	private static final long serialVersionUID = 5578377917258860531L;

	/**
	 * Constructs this step with the specified text to show as its title, in its
	 * caption bar, and optionally in a label centered in its panel. If the
	 * label text is null then no label will be added.
	 * 
	 * @param title
	 *            the title of the step
	 * @param caption
	 *            the caption on the step
	 * @param labelText
	 *            text for an optional label in the step
	 */
	public DefaultFinishStep(String title, String caption, String labelText) {
		super("Finished", caption);
		if (labelText != null) {
			add(new JLabel(labelText));
		}

		setComplete(true);
	}

	/**
	 * This is the same as
	 * <code>new DefaultFinishStep(title, caption, null)</code>.
	 * 
	 * @param title
	 *            the title of the step
	 * @param caption
	 *            the step's caption and label text
	 */
	public DefaultFinishStep(String title, String caption) {
		this(title, caption, null);
	}
}
