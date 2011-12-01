package repast.simphony.parameter;

/**
 * This {@link repast.simphony.parameter.ParameterSetter} will run through a space of numbers. The space is
 * from the given base value through the given maximum value (including both: [base, max]). This
 * space is traversed using the given step value.
 *
 * @author Jerry Vos
 */
public class LongSteppedSetter extends AbstractSweepParameterSetter<Long> {

	private long stepSize;

	private long max;

	private long base;

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
	public LongSteppedSetter(String parameterName, long base, long max, long step) {
		super(parameterName);
		this.base = base;
		this.stepSize = step;
		this.max = max;
	}

	/**
	 * Resets the next value to the base value.
	 */
	public Long resetValue() {
		return base;
	}

	/**
	 * @return true if the next value is greater than the max value
	 */
	public boolean atEnd(Long prevValue) {
		if (prevValue == null) {
			return false;
		}
		return prevValue + stepSize > max;
	}

	public boolean atBeginning(Long prevValue) {
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
	protected Long nextValue(Long prevValue) {
		if (prevValue == null) {
			prevValue = base;
		}
		return prevValue + stepSize;
	}

	@Override
	protected Long randomValue() {
		int numSteps = (int) Math.floor((max - base) / stepSize);

		int steps = randInt(0, numSteps);

		return steps * stepSize + base;
	}

	@Override
	protected Long previousValue(Long prevValue) {
		if (prevValue == null) {
			prevValue = base + stepSize;
		}
		return prevValue - stepSize;
	}

    public String toString() {
        return "[long " + parameterName + " " + base + ".." + max + "," + stepSize + "]";
    }
}
