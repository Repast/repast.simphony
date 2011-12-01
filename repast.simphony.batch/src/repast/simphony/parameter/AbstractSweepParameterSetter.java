/*CopyrightHere*/
package repast.simphony.parameter;

import repast.simphony.parameter.optimizer.OptimizableParameterSetter;
import repast.simphony.random.RandomHelper;

/**
 * An abstract {@link repast.simphony.parameter.ParameterSetter} that is meant to dealt with only one
 * parameter (hence the parameterName value it contains). This handles setting the parameter and the
 * case where it is next'd when it is already finished (it returns the last value it had.
 * 
 * TODO: rename this class
 * 
 * @author Jerry Vos
 */
public abstract class AbstractSweepParameterSetter<T> implements OptimizableParameterSetter {
	public static final int RESET_INDEX = 0;
	public static final int LAST_VALUE_INDEX = 1;
	
	protected String parameterName;

	protected T lastValue;

	protected T revertValue;
	
	
	/**
	 * Constructs this with the given name to store its parameters to.
	 * 
	 * @param parameterName
	 *            the name of the parameter
	 */
	public AbstractSweepParameterSetter(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * Gets the name of the parameter that this setter is responsible
	 * for setting.
	 *
	 * @return the name of the parameter that this setter is responsible
	 * for setting.
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * If the object has finished its space this will set the parameter to the last value it
	 * generaetd, otherwise it will set it to the value returned by {@link #nextValue()}.
	 * 
	 * @param params
	 *            the parameter object values will be stored in
	 */
	public void next(Parameters params) {
		if (!atEnd()) {
			revertValue = lastValue;
			lastValue = nextValue(lastValue);
		}
		params.setValue(parameterName, lastValue);
	}
	
	public void previous(Parameters parameters) {
		revertValue = lastValue;
		lastValue = previousValue(lastValue);
		parameters.setValue(parameterName, lastValue);
	}

	public void random(Parameters parameters) {
		revertValue = lastValue;
		lastValue = randomValue();
		parameters.setValue(parameterName, lastValue);
	}
	
	/**
	 * Retrieves the next value for the parameter. This will not be called when the space is
	 * finished.
	 * 
	 * @return the next parameter value
	 */
	protected abstract T nextValue(T prevValue);
	
	protected abstract T randomValue();
	
	protected abstract T previousValue(T prevValue);
	
	protected abstract T resetValue();
	
	protected abstract boolean atEnd(T prevValue);
	
	protected abstract boolean atBeginning(T prevValue);
	
	public boolean atBeginning() {
		return atBeginning(lastValue);
	}
	
	public boolean atEnd() {
		return atEnd(lastValue);
	}
	
	public void reset(Parameters params) {
		revertValue = lastValue;
		lastValue = resetValue();
		params.setValue(parameterName, lastValue);
	}
	
	
	protected double randDouble(double from, double to) {
		return RandomHelper.getUniform().nextDoubleFromTo(from, to);
	}
	
	protected int randInt(int from, int to) {
		return RandomHelper.getUniform().nextIntFromTo(from, to);
	}
	
	public void revert(Parameters params) {
		lastValue = revertValue;
		params.setValue(parameterName, revertValue);
	}
}