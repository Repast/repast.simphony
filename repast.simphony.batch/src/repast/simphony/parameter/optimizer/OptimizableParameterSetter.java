/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;

public interface OptimizableParameterSetter extends ParameterSetter {
	boolean atBeginning();
	
	void previous(Parameters parameters);
	
	void random(Parameters parameters);
	
	void revert(Parameters parameters);
}
