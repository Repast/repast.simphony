package repast.simphony.parameter;

import java.util.HashMap;
import java.util.Map;


/**
 * Builder for creating parameter sweepers and Parameters for all the
 * parameters in the created sweeper.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParameterSweeperBuilder {

	private ParameterTreeSweeper sweeper = new ParameterTreeSweeper();
	private Map<String, ParameterSetter> setterMap = new HashMap<String, ParameterSetter>();
	private ParametersCreator creator = new ParametersCreator();
	private ParameterSetter lastSetter;

	/**
	 * Creates a ParametertSweeperBuilder
	 */
	public ParameterSweeperBuilder() {}

	/**
	 * Creates  ParameterSweeperBuilder with the specified
	 * ParameterTreeSweeper.
	 *
	 * @param sweeper the sweeper to use
	 */
	public ParameterSweeperBuilder(ParameterTreeSweeper sweeper) {
		this.sweeper = sweeper;
	}

	/**
	 * Gets the root parameter setter.
	 *
	 * @return the root parameter setter.
	 */
	public ParameterSetter getRootSetter() {
		return sweeper.getRootParameterSetter();
	}

	/**
	 * Sets the number of runs the sweeper should perform.
	 *
	 * @param runs the number of runs the sweeper should perform
	 */
	public void setRunCount(int runs) {
		sweeper.setRunCount(runs);
	}

	/**
	 * Adds the a constant parameter setter for the specified parameter
	 * and with the specified value to the sweeper.
	 *
	 * @param name the name of the parameter this setter will operate
	 * on
	 * @param value the contant value of the parameter
   * @return the created ConstantSetter.
	 */
	public <T> ParameterSetter addConstant(String name, T value) {
		ConstantSetter<T> setter = new ConstantSetter<T>(name, value);
		creator.addParameter(name, value.getClass(), value, false);
		addSetter(sweeper.getRootParameterSetter(), setter);
		// we don't want the constants ever to be parents,
		// so we set the last setter (the implicit parent) to null
		lastSetter = null;
  return setter;

  }

	/**
	 * Adds an int parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper as a child
	 * parameter setter of the most recently added parameter setter.
	 *
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(String name, int start, int end, int step) {
		IntSteppedSetter setter = createIntStepper(name, start, end, step);
		addSetter(null, setter);
		return setter;
	}

	private IntSteppedSetter createIntStepper(String name, int start, int end, int step) {
		IntSteppedSetter setter = new IntSteppedSetter(name, start, end, step);
		setterMap.put(name, setter);
		creator.addParameter(name, Integer.class, start, false);
		return setter;
	}

	/**
	 * Adds an int parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper tree as a child
	 * of the specified parent.
	 *
	 * @param parent the created parameter setter will be added as a
	 * child to this parent in the sweeper tree.
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(ParameterSetter parent, String name, int start, int end, int step) {
		if (parent == null) throw new IllegalArgumentException("Parent Stepper cannot be null");
		IntSteppedSetter setter = createIntStepper(name, start, end, step);
		addSetter(parent, setter);
		return setter;
	}

	/**
	 * Adds a double parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper as a child
	 * parameter setter of the most recently added parameter setter.
	 *
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(String name, double start, double end, double step) {
		DoubleSteppedSetter setter = createDoubleStepper(name, start, end, step);
		addSetter(null, setter);
		return setter;
	}

	private DoubleSteppedSetter createDoubleStepper(String name, double start, double end, double step) {
		DoubleSteppedSetter setter = new DoubleSteppedSetter(name, start, end, step);
		setterMap.put(name, setter);
		creator.addParameter(name, Double.class, start, false);
		return setter;
	}

	/**
	 * Adds a double parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper tree as a child
	 * of the specified parent.
	 *
	 * @param parent the created parameter setter will be added as a
	 * child to this parent in the sweeper tree.
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(ParameterSetter parent, String name, double start, double end, double step) {
		if (parent == null) throw new IllegalArgumentException("Parent Stepper cannot be null");
		DoubleSteppedSetter setter = createDoubleStepper(name, start, end, step);
		addSetter(parent, setter);
		return setter;
	}

	/**
	 * Adds a float parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper as a child
	 * parameter setter of the most recently added parameter setter.
	 *
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(String name, float start, float end, float step) {
		FloatSteppedSetter setter = createFloatStepper(name, start, end, step);
		addSetter(null, setter);
		return setter;
	}

	private FloatSteppedSetter createFloatStepper(String name, float start, float end, float step) {
		FloatSteppedSetter setter = new FloatSteppedSetter(name, start, end, step);
		setterMap.put(name, setter);
		creator.addParameter(name, Float.class, start, false);
		return setter;
	}

	/**
	 * Adds a float parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper tree as a child
	 * of the specified parent.
	 *
	 * @param parent the created parameter setter will be added as a
	 * child to this parent in the sweeper tree.
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(ParameterSetter parent, String name, float start, float end, float step) {
		if (parent == null) throw new IllegalArgumentException("Parent Stepper cannot be null");
		FloatSteppedSetter setter = createFloatStepper(name, start, end, step);
		addSetter(parent, setter);
		return setter;
	}

	/**
	 * Adds a long parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper as a child
	 * parameter setter of the most recently added parameter setter.
	 *
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(String name, long start, long end, long step) {
		LongSteppedSetter setter = createLongStepper(name, start, end, step);
		addSetter(null, setter);
		return setter;
	}

	private LongSteppedSetter createLongStepper(String name, long start, long end, long step) {
		LongSteppedSetter setter = new LongSteppedSetter(name, start, end, step);
		setterMap.put(name, setter);
		creator.addParameter(name, Long.class, start, false);
		return setter;
	}

	/**
	 * Adds a long parameter stepper with the specified name and
	 * value. The stepper will be added to the sweeper tree as a child
	 * of the specified parent.
	 *
	 * @param parent the created parameter setter will be added as a
	 * child to this parent in the sweeper tree.
	 * @param name the name of the parameter that this stepper will
	 * operate on
	 * @param start the starting value of the stepper
	 * @param end the ending value of the stepper
	 * @param step the amount to increment / decrement the stepper
	 *
	 * @return the created ParameterSetter.
	 */
	public ParameterSetter addStepper(ParameterSetter parent, String name, long start, long end, long step) {
		if (parent == null) throw new IllegalArgumentException("Parent Stepper cannot be null");
		LongSteppedSetter setter = createLongStepper(name, start, end, step);
		addSetter(parent, setter);
		return setter;
	}

	/**
	 * Adds a list setter with the specified name and list
	 * elements. The setter will be added to the sweeper tree
	 * as a child of the most recently added parameter
	 * setter.
	 *
	 * @param name the name of the parameter this setter
	 * will operate on
	 * @param list the list of values to set the parameter to
	 * @return the created ParameterSetter
	 */
	public <T> ParameterSetter addListSetter(String name, T[] list) {
		ListParameterSetter<T> setter = new ListParameterSetter<T>(name, list);
		setterMap.put(name, setter);
		creator.addParameter(name, list[0].getClass(), list[0], false);
		addSetter(null, setter);
		return setter;
	}

	/**
	 * Adds a list setter with the specified name and list
	 * elements. The setter will be added to the sweeper tree
	 * as a child of the specified parent
	 *
	 * @param parent the created parameter setter will be added
	 * as a child of this parent setter
	 * @param name the name of the parameter this setter
	 * will operate on
	 * @param list the list of values to set the parameter to
	 * @return the created ParameterSetter
	 */
	public <T> ParameterSetter addListSetter(ParameterSetter parent, String name, T[] list) {
		if (parent == null) throw new IllegalArgumentException("Parent Stepper cannot be null");
		ListParameterSetter<T> setter = new ListParameterSetter<T>(name, list);
		setterMap.put(name, setter);
		creator.addParameter(name, list[0].getClass(), list[0], false);
		addSetter(parent, setter);
		return setter;
	}

	/**
	 * Gets the added ParameterSetter for the named parameter
	 *
	 * @param name the name of the parameter whose setter we want to get
	 * @return the ParameterSetter for the named parameter or null if no
	 * such parameter setter can be found.
	 */
	public ParameterSetter getSetter(String name) {
		return setterMap.get(name);
	}

	private void addSetter(ParameterSetter parent, ParameterSetter toAdd) {
		if (parent == null) {
			if (lastSetter == null) parent = sweeper.getRootParameterSetter();
			else parent = lastSetter;
		}
		sweeper.add(parent, toAdd);
		lastSetter = toAdd;
	}

	/**
	 * Gets the ParameterTreeSweeper containing the added parameter setters.
	 *
	 * @return the ParameterTreeSweeper containing the added parameter setters.
	 */
	public ParameterTreeSweeper getSweeper() {
		return sweeper;
	}

	/**
	 * Gets the Parameters built from the added setters.
	 *
	 * @return the Parameters built from the added setters.
	 */
	public Parameters getParameters() {
		return creator.createParameters();
	}
}
