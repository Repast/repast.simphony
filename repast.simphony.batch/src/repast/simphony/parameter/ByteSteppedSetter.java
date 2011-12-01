package repast.simphony.parameter;

public class ByteSteppedSetter extends AbstractSweepParameterSetter<Byte>{
	private byte stepSize;

	private byte max;

	private byte base;

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
	public ByteSteppedSetter(String parameterName, byte base, byte max, byte step) {
		super(parameterName);
		this.base = base;
		this.stepSize = step;
		this.max = max;
	}

	/**
	 * Resets the next value to the base value.
	 */
	public Byte resetValue() {
		return base;
	}

	/**
	 * @return true if the next value is greater than the max value
	 */
	public boolean atEnd(Byte prevValue) {
		if (prevValue == null) {
			return false;
		}
		return prevValue + stepSize > max;
	}

	public boolean atBeginning(Byte prevValue) {
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
	protected Byte nextValue(Byte prevValue) {
		if (prevValue == null) {
			prevValue = base;
		}
//		return prevValue + stepSize;
//		return prevValue;
//		return stepSize;
		
		return (byte) (prevValue + stepSize);
	}

	@Override
	protected Byte randomValue() {
		int numSteps = (int) Math.floor((max - base) / stepSize);

		int steps = randInt(0, numSteps);

		return (byte) (steps * stepSize + base);
	}

	@Override
	protected Byte previousValue(Byte prevValue) {
		if (prevValue == null) {
			prevValue = (byte) (base + stepSize);
		}
		return (byte) (prevValue - stepSize);
	}

    public String toString() {
        return "[byte " + parameterName + " " + base + ".." + max + "," + stepSize + "]";
    }
}
