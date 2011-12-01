package repast.simphony.parameter;

/**
 * This {@link repast.simphony.parameter.ParameterSetter} will run through a space of numbers. The space is
 * from the given base value through the given maximum value (including both: [base, max]). This
 * space is traversed using the given step value.
 *
 * @author Jerry Vos
 */
public class ShortSteppedSetter extends AbstractSweepParameterSetter<Short> {

	private short stepSize;

	private short max;

	private short base;

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
	public ShortSteppedSetter(String parameterName, short base, short max, short step) {
		super(parameterName);
		this.base = base;
		this.stepSize = step;
		this.max = max;
	}

	/**
	 * Resets the next value to the base value.
	 */
	public Short resetValue() {
		return base;
	}

	/**
	 * @return true if the next value is greater than the max value
	 */
	public boolean atEnd(Short prevValue) {
		if (prevValue == null) {
			return false;
		}
		return prevValue + stepSize > max;
	}

	public boolean atBeginning(Short prevValue) {
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
	protected Short nextValue(Short prevValue) {
		if (prevValue == null) {
			prevValue = base;
		}
		return (short) (prevValue + stepSize);
	}

	@Override
	protected Short randomValue() {
		int numSteps = (int) Math.floor((max - base) / stepSize);

		int steps = randInt(0, numSteps);

		return (short) (steps * stepSize + base);
	}

	@Override
	protected Short previousValue(Short prevValue) {
		if (prevValue == null) {
			prevValue = (short) (base + stepSize);
		}
		return (short) (prevValue - stepSize);
	}

    public String toString() {
        return "[short " + parameterName + " " + base + ".." + max + "," + stepSize + "]";
    }
}

