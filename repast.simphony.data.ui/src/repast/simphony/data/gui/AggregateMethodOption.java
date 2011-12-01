/*CopyrightHere*/
package repast.simphony.data.gui;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.util.wizard.AbstractWizardOption;

public class AggregateMethodOption extends AbstractWizardOption implements MappingWizardOption {

	public AggregateMethodOption() {
		super("Method Value", "Aggregate the value of a method call.");
	}

	public void setIsAggregate(boolean isAggregate) {
		if (!isAggregate) {
			throw new IllegalArgumentException("The aggregate method option only functions as an aggregate option, therefore isAggregate must be true.");
		}
	}

	public SimplePath getWizardPath() {
		SimplePath path = new SimplePath();
		path.addStep(new AggregationStep());
//		path.add
		
		return path;
	}

}
