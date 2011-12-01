/*CopyrightHere*/
package repast.simphony.parameter;


/**
 * An initializer that will go through a list of parameters.
 * 
 * @author Jerry Vos
 */
public class ListParameterSetter<T> extends AbstractSweepParameterSetter<T> {
	private T[] list;

	private int index;

	private int revertIndex;

	/**
	 * Constructs this with the specified parameter name (where the parameter will be stored) and
	 * iterating through the given parameter list.
	 * 
	 * @param parameterName
	 *            the name of the parameter values will be stored in
	 * @param list
	 *            the list of values to go through
	 */
	public ListParameterSetter(String parameterName, T[] list) {
		super(parameterName);
		this.list = list;
		this.index = 0;
		this.revertIndex = 0;
	}

	/**
	 * Resets the index of the parameters to the next value.
	 */
	@Override
	public void reset(Parameters params) {
		// in case we get reset multiple times in a row, we don't want to 
		// update the revert index
		revertIndex = index;
		index = 1;
		params.setValue(parameterName, list[0]);
	}

	/**
	 * Compares the index to the number of elements in the list/
	 * 
	 * @return true if the list's index is greater than the list's length
	 */
	@Override
	public boolean atEnd() {
		return index == list.length;
	}

	/**
	 * Fetches the next element in the list and increments the index.
	 * 
	 * @return the next element in the list
	 */
	@Override
	protected T nextValue(T prevValue) {
		revertIndex = index++;
		return list[index - 1];
	}

	@Override
	public void revert(Parameters params) {
		if (revertIndex == list.length) {
			params.setValue(parameterName, list[list.length - 1]);
		} else {
			params.setValue(parameterName, list[revertIndex]);
		}
		index = revertIndex;
	}

	public boolean atBeginning() {
		return index == 0;
	}

	@Override
	protected T previousValue(T prevValue) {
		if (atEnd()) {
			revertIndex = list.length;
			index = list.length - 1;
		} else {
			revertIndex = index--;
		}

		return list[index];
	}

	@Override
	protected T randomValue() {
		revertIndex = index;
		index = randInt(0, list.length - 1);
		return list[index];
	}

	@Override
	protected T resetValue() {
		// this isn't needed because we overrode reset
		throw new UnsupportedOperationException("resetValue unsupported");
	}

	@Override
	protected boolean atEnd(T prevValue) {
		// this isn't needed because we overrode atEnd
		throw new UnsupportedOperationException("atEnd unsupported");
	}

	@Override
	protected boolean atBeginning(T prevValue) {
		// this isn't needed because we overrode atBeginning
		throw new UnsupportedOperationException("atBeginning unsupported");
	}

    public String toString() {
        return "[list " + parameterName + list + "]";
    }
}