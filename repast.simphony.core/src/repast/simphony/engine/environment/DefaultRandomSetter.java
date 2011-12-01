/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.SimpleParameterSetter;
import repast.simphony.random.RandomHelper;

/**
 * Sets the default random seed to the current time and creates a uniform random stream.
 * 
 * @see repast.simphony.random.RandomHelper#setSeed(int)
 */
public class DefaultRandomSetter extends SimpleParameterSetter {
	/**
	 * Sets the random seed to the current time and creates a uniform stream.
	 * 
	 * @param params
	 *            ignored
	 */
	public void next(Parameters params) {
		RandomHelper.setSeed((int)System.currentTimeMillis());
	}
}
