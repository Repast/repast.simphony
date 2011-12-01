/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;

/**
 * This action grabs from the current parameters the value of
 * {@link repast.simphony.parameter.ParameterConstants#DEFAULT_RANDOM_SEED_USAGE_NAME} and if that is not null it
 * uses its int value to set the default seed through the
 * {@link repast.simphony.random.RandomHelper#setSeed(int)} method.
 * 
 * @author Jerry Vos
 */
public class DefaultRandomStreamAction extends DefaultControllerAction {
	@Override
	public void runInitialize(RunState runState, Object contextId, Parameters params) {
		if (params == null) {
			return;
		}
		// grab the specified random's seed
		if (params.getValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) != null) {
			Number seed = (Number) params.getValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME);
			RandomHelper.setSeed(seed.intValue());
		}
	}
}
