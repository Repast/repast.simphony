/*CopyrightHere*/
package repast.simphony.parameter;

/**
 * This {@link repast.simphony.parameter.ParameterSetter} will run through a space of numbers. The space is
 * from the given base value through the given maximum value (including both: [base, max]). This
 * space is traversed using the given step value.
 * 
 * @author Jerry Vos
 */
public class IntSteppedSetter extends AbstractSweepParameterSetter<Integer> {
	private int stepSize;

	private int max;

	private int base;

	/**
	 * Constructs this with the given base value, step size, and maximum value.
	 * 
	 * @param parameterName
	 *            the name of the parameter
	 * @param base
	 *            the beginning value
	 * @param max
	 *            the maximum value (inclusive)
	 * @param step
	 *            the step size
	 */
	public IntSteppedSetter(String parameterName, int base, int max, int step) {
		super(parameterName);
		this.base = base;
		this.stepSize = step;
		this.max = max;
	}

	/**
	 * Resets the next value to the base value.
	 */
	public Integer resetValue() {
		return base;
	}

	/**
	 * @return true if the next value is greater than the max value
	 */
	public boolean atEnd(Integer prevValue) {
		if (prevValue == null) {
			return false;
		}
		return prevValue + stepSize > max;
	}

	public boolean atBeginning(Integer prevValue) {
		if (prevValue == null) {
			return true;
		}
		return prevValue - stepSize < base;
	}

	/**
	 * Retrieves the next value in the space (previous value + step size).
	 * 
	 * @return the next value
	 */
	@Override
	protected Integer nextValue(Integer prevValue) {
		if (prevValue == null) {
			prevValue = base;
		}
		return prevValue + stepSize;
	}

	@Override
	protected Integer randomValue() {
		int numSteps = (int) Math.floor((max - base) / stepSize);

		int steps = randInt(0, numSteps);

		return steps * stepSize + base;
	}

	@Override
	protected Integer previousValue(Integer prevValue) {
		if (prevValue == null) {
			prevValue = base + stepSize;
		}
		return prevValue - stepSize;
	}

    public String toString() {
        return "[int " + parameterName + " " + base + ".." + max + "," + stepSize + "]";
    }
}