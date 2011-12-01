/*CopyrightHere*/
package repast.simphony.parameter;

import repast.simphony.engine.environment.RunState;

public interface RunResultProducer {
	double getRunValue(RunState runState);
}
