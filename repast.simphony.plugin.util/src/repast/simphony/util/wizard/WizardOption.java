/*CopyrightHere*/
package repast.simphony.util.wizard;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.models.SimplePath;

/**
 * This represents an option in a wizard.
 * 
 * @see repast.simphony.util.wizard.WizardPluginUtil
 * @author Jerry Vos
 */
public interface WizardOption {
	/**
	 * The description of the option.
	 * 
	 * @return the option's description
	 */
	String getDescription();

	/**
	 * The text to be shown in the option select's list (or equivalent)
	 * 
	 * @return the option's title
	 */
	String getTitle();

	/**
	 * The path this wizard option represents.
	 * 
	 * @return the option's path
	 */
	SimplePath getWizardPath();

	/**
	 * Called when the option is first loaded.
	 * 
	 * @param manager
	 */
	void init(PluginManager manager);
}
