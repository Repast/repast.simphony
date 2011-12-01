/*CopyrightHere*/
package repast.simphony.data.gui;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.util.wizard.AbstractWizardOption;

public class MethodMappingOption extends AbstractWizardOption implements MappingWizardOption {
	protected boolean aggregate = false;
	
	public MethodMappingOption() {
		super("Method Call", "Generate data values by calling a specific method on the agents.");
	}

	public void setIsAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

	public SimplePath getWizardPath() {
		SimplePath path = new SimplePath();
		
		if (aggregate) {
			path.addStep(new AggregationStep());
		} else {
		}
		path.addStep(new MethodMappingStep());
		
		return path;
	}

}
