/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.dataLoader.engine.DelimitedFileContextBuilder;
import repast.simphony.util.wizard.AbstractWizardOption;

public class DelimitedFileDataLoaderOption extends AbstractWizardOption implements DataLoaderWizardOption {
	
	public DelimitedFileDataLoaderOption() {
		super("Freeze Dried Simulation - Plain Text Delimited Format", "");
	}

	public static SimplePath getPath() {
		DFDataLoaderDirectoryChooserStep fileChooserStep = new DFDataLoaderDirectoryChooserStep();
		FreezeDryedClassChooserStep classChooserStep = new FreezeDryedClassChooserStep(fileChooserStep);
		SimplePath path = new SimplePath(fileChooserStep);
		path.addStep(classChooserStep);
		
		return path;
	}
	
	public SimplePath getWizardPath() {
		return getPath();
	}

	public ContextActionBuilder createBuilder(ContextBuilder loader) {
		return new DFDataLoaderContextActionBuilder((DelimitedFileContextBuilder) loader);
	}
}
