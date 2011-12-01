
/*CopyrightHere*/
package repast.simphony.dataLoader.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.freezedry.datasource.JDBCContextBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardOption;
import repast.simphony.dataLoader.ui.wizard.FreezeDryedClassChooserStep;
import repast.simphony.util.wizard.AbstractWizardOption;

public class JDBCDataLoaderOption extends AbstractWizardOption implements DataLoaderWizardOption {
	
	public JDBCDataLoaderOption() {
		super("Freeze Dried Simulation - Database Format", "Load data from a database");
	}

	public SimplePath getWizardPath() {
		JDBCDataLoaderDataChooserStep dataBaseStep = new JDBCDataLoaderDataChooserStep();
		FreezeDryedClassChooserStep classChooserStep = new FreezeDryedClassChooserStep(dataBaseStep);
		SimplePath path = new SimplePath(dataBaseStep);
		path.addStep(classChooserStep);
		return path;
	}

	public JDBCContextActionBuilder createBuilder(ContextBuilder baseLoader) {
		return new JDBCContextActionBuilder((JDBCContextBuilder) baseLoader);
	}
}
