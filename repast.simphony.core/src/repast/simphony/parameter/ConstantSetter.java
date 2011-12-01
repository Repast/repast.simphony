/*CopyrightHere*/
package repast.simphony.parameter;

/**
 * A {@link ParameterSetter} that will set a parameter to a given value. This
 * always returns true on on its {@link #atEnd()} method.
 * 
 * @author Jerry Vos
 */
public class ConstantSetter<T> implements ParameterSetter {
	private T constant;

	protected String parameterName;

	public ConstantSetter(){
		
	}
	/**
	 * Constructs this to set the given parameter to the given value.
	 * 
	 * @param parameterName
	 *            the name of the parameter
	 * @param constant
	 *            the value to set the parameter to
	 */
	public ConstantSetter(String parameterName, T constant) {
		this.parameterName = parameterName;
		this.constant = constant;
	}

	/**
	 * Gets the name of the parameter that this setter should set.
	 *
	 * @return the name of the parameter that this setter should set.
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Sets the constant parameter value
	 */
	public void reset(Parameters params) {
		if (!params.isReadOnly(parameterName)) params.setValue(parameterName, constant);
	}

	/**
	 * Returns true.
	 */
	public boolean atEnd() {
		return true;
	}

	/**
	 * Sets its specified value to its specified parameter name.
	 * 
	 * @param params
	 *            the parameters object
	 */
	public void next(Parameters params) {
		params.setValue(parameterName, constant);
	}

}