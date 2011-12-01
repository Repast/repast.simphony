/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

import repast.simphony.parameter.ParameterSetter;

/**
 * This is a simple advancement algorithm that will perform a <a
 * href="en.wikipedia.org/wiki/Hill_climbing"> Hill Climbing</a> traversal of the space.
 * 
 * @see #chooseAdvancement(repast.simphony.parameter.ParameterSetter, AdvanceType, double)
 * 
 * @author Jerry Vos
 */
public class HillClimber implements AdvancementChooser {

	protected double lastValue;

	protected boolean checkingLocal = true;

	protected boolean forwardValid = false;

	protected double forwardValue;

	protected double centerValue;

	protected ParameterSetter lastSetter;

	private boolean checkingForward;

	private double backwardValue;

	private boolean checkingBackward;

	/**
	 * Constructs the climber.
	 */
	public HillClimber() {
		this.lastValue = Double.NEGATIVE_INFINITY;
	}

	/**
	 * This performs the hill climbing algorithm. In the best case it will traverse the space by
	 * (assuming we've just chosen a new parameter):
	 * <ul>
	 * <li>Explore the local space to find which direction to follow. It first will explore to the
	 * right (FORWARD) of the current position, then to the left (BACKWARD).</li>
	 * <li>Next it will move whichever direction is best of left, right, and the neither (if we're
	 * at a peak). If we're on a peak it will return {@link AdvanceType#SWITCH}.
	 * </ul>
	 * 
	 * <p/> Because of the implementation, this may result in a single spot being executed multiple
	 * times in order. Also, this handles the border cases where it cannot move forwards or
	 * backwards (but wishes to) by switching.
	 * 
	 * @param setter
	 *            the current setter
	 * @param lastType
	 *            the last executed parameter command
	 * @param runResult
	 *            the last run's value
	 * 
	 * @return the action to perform
	 */
	public AdvanceType chooseAdvancement(ParameterSetter setter, AdvanceType lastType,
			double runResult) {
		ParameterSetter previousInit = lastSetter;
		lastSetter = setter;

		OptimizableParameterSetter advancedInit = null;
		if (setter instanceof OptimizableParameterSetter) {
			advancedInit = (OptimizableParameterSetter) setter;
		}

		double previousRunValue = lastValue;
		lastValue = runResult;
		
		// when we switch parameters we want to choose the next position by looking at a
		// 3x1 grid, including the position one back (PREVIOUS), current (runResult), and
		// one ahead (FORWARD)

		// start out checking FORWARD if we can
		if (previousInit != setter) {
			// we switched setters so must start over
			checkingLocal = true;
			centerValue = runResult;
			checkingBackward = false;
			if (!setter.atEnd()) {
				checkingForward = true;
				return AdvanceType.FORWARD;
			} else if (advancedInit != null && !advancedInit.atBeginning()) {
				// can't go forward but can go backwards, so go back
				forwardValue = Double.NEGATIVE_INFINITY;
				checkingForward = false;
				checkingBackward = true;
				return AdvanceType.BACKWARD;
			} else {
				checkingLocal = false;
				checkingForward = false;
				// can't go forwards or backwards
				return AdvanceType.SWITCH;
			}
		}

		

		// this means we switched setters recently and are exploring the local area to
		// figure out which way to go
		if (checkingLocal) {
			if (checkingForward) {
				// we just started checking, and first went forward, so now try backwards
				checkingForward = false;
				if (advancedInit == null || advancedInit.atBeginning()) {
					if (forwardValue > centerValue) {
						// update last value so that it doesn't think it can't go forward
						// because we're going to execute the same spot again (from the
						// revert and then a FORWARD after a FORWARD)
						lastValue = centerValue;
						checkingLocal = false;
						return AdvanceType.FORWARD;
					} else {
						return AdvanceType.SWITCH;
					}
				}
				checkingBackward = true;
				return AdvanceType.BACKWARD;
			} else if (checkingBackward) {
				// checked forwards, backwards, and the center
				checkingLocal = false;
				checkingBackward = false;

				double maxValue = Math.max(forwardValue, Math.max(backwardValue, centerValue));
				if (maxValue == centerValue) {
					return AdvanceType.SWITCH;
				} else if (maxValue == forwardValue) {
					// because of the revert we have to do this 
					lastValue = centerValue;
					return AdvanceType.FORWARD;
				} else {
					// because of the revert we have to do this 
					lastValue = centerValue;
					return AdvanceType.BACKWARD;
				}
			}
		}
		// strictly advance, do not follow plateaus
		if (previousRunValue >= runResult) {
			return AdvanceType.SWITCH;
		} else {
			return lastType;
		}
	}

	/**
	 * Returns true when the previous run's value was greater than runResult or when we're exploring
	 * the local space to figure out which direction to go.
	 * 
	 * @return if we should revert to the previous space
	 */
	public boolean shouldRevert(double runResult) {
		// TODO: possibly revert in a better way, the way its done now every
		// time it switches it will do an extra run (because of the extra revert)
		if (checkingLocal) {
			if (checkingForward) {
				forwardValue = runResult;
			} else if (checkingBackward) {
				backwardValue = runResult;
			}
			return true;
		}
		if (lastValue > runResult) {
			return true;
		} else {
			return false;
		}
	}

}
