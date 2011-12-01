/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.wizard.AbstractWizardOption;

public class ContextXMLDataLoaderOption extends AbstractWizardOption implements DataLoaderWizardOption {

	public ContextXMLDataLoaderOption() {
	  super("Context.xml file", "Creates a context builder from context.xml file metadata");
	}

	public static SimplePath getPath() {
		return new SimplePath();
	}

	public SimplePath getWizardPath() {
		return getPath();
	}

	public ContextActionBuilder createBuilder(ContextBuilder loader) {
		return new ContextXMLActionBuilder();
	}
}