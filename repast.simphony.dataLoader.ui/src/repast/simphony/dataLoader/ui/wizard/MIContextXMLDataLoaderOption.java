/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.wizard.AbstractWizardOption;

public class MIContextXMLDataLoaderOption extends AbstractWizardOption implements DataLoaderWizardOption {

	public MIContextXMLDataLoaderOption() {
	  super("Context.xml file & ModelInitializer", "Creates a context builder from context.xml file metadata and executes a ModelInitializer");
		
	}

	public static SimplePath getPath() {
		return new SimplePath();
	}

	public SimplePath getWizardPath() {
		return getPath();
	}

	public ContextActionBuilder createBuilder(ContextBuilder loader) {
		return new MIContextXMLActionBuilder();
	}
}