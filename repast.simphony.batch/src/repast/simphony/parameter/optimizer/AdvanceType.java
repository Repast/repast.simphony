/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

/**
 * This represents the directions/actions the sweeper can take. These correlate to the methods in
 * {@link repast.simphony.parameter.optimizer.OptimizableParameterSetter}. An
 * {@link repast.simphony.parameter.optimizer.AdvancementChooser} will specify one of these types and the
 * parameter sweeper will attempt to follow that instruction if it can.
 * 
 * @author Jerry Vos
 */
public enum AdvanceType {
	/**
	 * Move forward in the space (ie right or down)
	 */
	FORWARD,
	/**
	 * Move backward in the space (ie left or up)
	 */
	BACKWARD,
	/**
	 * Move to a random position in the space
	 */
	RANDOM,
	/**
	 * Switch to a different parameter
	 */
	SWITCH;
}