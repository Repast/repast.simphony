/*CopyrightHere*/
package repast.simphony.util.wizard;

import org.java.plugin.PluginManager;

public abstract class AbstractWizardOption implements WizardOption {
	protected String description;

	protected String title;

	public AbstractWizardOption(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public void init(PluginManager manager) {

	}

}
