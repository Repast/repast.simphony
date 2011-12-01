package repast.simphony.parameter;

import java.math.BigDecimal;

import repast.simphony.parameter.optimizer.OptimizableParameterSetter;

/**
 * This {@link repast.simphony.parameter.ParameterSetter} will run through a space of numbers. The space is
 * from the given base value through the given maximum value (including both: [base, max]). This
 * space is traversed using the given step value.
 *
 * @author Jerry Vos
 */
public class FloatSteppedSetter implements OptimizableParameterSetter {
	private BigDecimal stepSize;

	private BigDecimal base;

	// this is only used for the toString
	private float max;

	private IntSteppedSetter initializer;

	private Parameters intInitParams;

	private String parameterName;

	/**
	 * Constructs this with the given base value, step size, and maximum value. This is guaranteed
	 * to produce Math.rint((max - base) / step) number of steps.
	 *
	 * @param parameterName the name of the parameter
	 * @param base          the beginning value
	 * @param max           the maximum value (inclusive)
	 * @param step          the step size
	 */
	public FloatSteppedSetter(String parameterName, float base, float max, float step) {
		this.parameterName = parameterName;
		this.base = BigDecimal.valueOf(base);
		this.stepSize = BigDecimal.valueOf(step);
		this.max = max;

		// to minimize rounding errors we build an int initializer which actually handles the
		// stepping, we compute values just by multiplying the step by the number of steps the
		// int initializer has performed and adding that to the base
		ParametersCreator creator = new ParametersCreator();
     // we can set this to 0 initiallly because
    // before its used the initializer will set it to the
    // correct value
    creator.addParameter("step", Integer.class, 0, false);
		intInitParams = creator.createParameters();
		//intInitParams.setValidating(false);

		initializer = new IntSteppedSetter("step", 0, (int) Math
						.rint((max - base) / step), 1);

	}

	/**
	 * Gets the name of the parameter that this setter is responsible
	 * for setting.
	 *
	 * @return the name of the parameter that this setter is responsible
	 *         for setting.
	 */
	public String getParameterName() {
		return parameterName;
	}

	public void next(Parameters params) {
		initializer.next(intInitParams);
		params.setValue(parameterName, getValue());
	}

	protected float getValue() {
		return stepSize.multiply(new BigDecimal(getStep())).add(base).floatValue();
	}

	public int getStep() {
		return (Integer) intInitParams.getValue("step");
	}

	public void previous(Parameters parameters) {
		initializer.previous(intInitParams);
		parameters.setValue(parameterName, getValue());
	}

	public void random(Parameters parameters) {
		initializer.random(intInitParams);
		parameters.setValue(parameterName, getValue());
	}

	public boolean atBeginning() {
		return initializer.atBeginning();
	}

	public boolean atEnd() {
		return initializer.atEnd();
	}

	public String toString() {
		return "[float " + parameterName + " " + base + ".." + max + "," + stepSize + "]";
	}

	public void revert(Parameters parameters) {
		initializer.revert(intInitParams);
		parameters.setValue(parameterName, getValue());
	}

	public void reset(Parameters params) {
		initializer.reset(intInitParams);
		params.setValue(parameterName, getValue());
	}
}
