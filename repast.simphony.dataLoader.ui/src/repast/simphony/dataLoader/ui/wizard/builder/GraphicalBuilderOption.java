/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.dataLoader.ui.wizard.ContextActionBuilder;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardOption;
import repast.simphony.util.wizard.AbstractWizardOption;

public class GraphicalBuilderOption extends AbstractWizardOption implements DataLoaderWizardOption {
	
	public GraphicalBuilderOption() {
		super("Context Builder", "A graphical context builder");
	}

	public SimplePath getWizardPath() {
		return new SimplePath(new ContextBuilderWizardStep());
	}

	public ContextActionBuilder createBuilder(ContextBuilder baseLoader) {
		return new GUIContextActionBuilder();
	}
}