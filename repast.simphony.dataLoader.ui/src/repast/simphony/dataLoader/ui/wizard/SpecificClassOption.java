/*CopyrightHere*/
/**
 * 
 */
package repast.simphony.dataLoader.ui.wizard;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.ContextBuilder;

public class SpecificClassOption implements DataLoaderWizardOption {
	public String getDescription() {
		return "Choose a specific class that will perform data loading";
	}

	public String getTitle() {
		return "Custom ContextBuilder Implementation";
	}

	public SimplePath getWizardPath() {
		return new SimplePath(new ClassBuilderStep());
	}

	public ContextActionBuilder createBuilder(ContextBuilder baseLoader) {
		return new ClassContextActionBuilder();
	}

	public void reset() {
		
	}

	public void init(PluginManager manager) {
		
	}
}