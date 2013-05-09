package repast.simphony.parameter;

import java.util.List;

import org.apache.commons.lang3.Range;

/**
 * Encapsulates a mutable set of simulation time model parameters.
 *
 * @author Michelle Kehrer
 */
public interface MutableParameters extends Parameters {

	 /**
	 * Adds a parameter to this Parameters.
	 *
	 * @param name  the name of the parameter
	 * @param displayName the display name for the parameter
	 * @param type  the type of the parameter
	 * @param value the default value of the parameter
	 * @param isReadOnly whether or not the parameter is read only.
	 */
	public void addParameter(String name, String displayName, Class type,
			Object value, boolean isReadOnly);

	/**
	   * Constains the value of the named to parameter to those in the list.
	   *
	   * @param name the name of the parameter
	   * @param constrainingList a list containing the possible values for
	   * the named parameter
	   */
	public void addConstraint(String name, List constrainingList);

	/**
	   * Adds a converter to convert the named parameter back and forth to a String.
	   *
	   * @param name the name of the parameter
	   * @param converter the converter
	   */
	public void addConvertor(String name, StringConverter converter);

	/**
	   * Constrains the value of the named parameter to those in the
	   * specified range.
	   *
	   * @param name the name of the parmaeter
	   * @param constrainingRange a range of the possible values for the named parameter
	   */
	public void addConstraint(String name, Range constrainingRange);

	/**
	 * Removes the named parameter
	 * @param name the name of the parameter
	 * @return whether or not removal was successful
	 */
	public boolean removeParameter(String name);

}
