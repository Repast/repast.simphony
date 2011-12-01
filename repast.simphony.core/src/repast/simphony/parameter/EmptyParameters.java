/*CopyrightHere*/
package repast.simphony.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Empty place holder parameters object that always returns Parameters.NULL.
 *
 * @author Nick Collier
 */
public class EmptyParameters implements Parameters {
	private Schema schema = new EmptySchema();

	/**
	 * Gets the Schema for this Parameters object.
	 *
	 * @return the Schema for this Parameters object.
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Gets the value associated with the specified parameter name. This always returns
	 * Parameters.NULL;
	 *
	 * @param paramName the name of the parameter whose value we want
	 * @return the value associated with the specified parameter name.
	 */
	public Object getValue(String paramName) {
		return Parameters.NULL;
	}

	public Parameters clone(){
		try{
			return (Parameters) super.clone();
		}
		catch(CloneNotSupportedException e){
			throw new InternalError(e.toString());
		}
		
	}

  /**
   * Gets a String representation of the specified parameter's value.
   *
   * @param paramName the name of the parameter
   * @return a String representation of the specified parameters's value.
   */
  public String getValueAsString(String paramName) {
    return "null";
  }

  /**
	 * Gets the display name for the specified parameter name.
	 *
	 * @param paramName the parameter name
	 * @return the display name for the specified parameter name.
	 */
	public String getDisplayName(String paramName) {
		return null;
	}

	/**
	 * Sets the specified parameter name to the specified value. This is no op in
	 * this implementation of Parameters.
	 *
	 * @param paramName the name of the parameter to set to the new value
	 * @param val       the new value
	 */
	public void setValue(String paramName, Object val) {}

	/**
	 * True if parameter is read only.
	 *
	 * @param paramName the name of the parameter
	 * @return true if parameter is read-only otherwise false.
	 */
	public boolean isReadOnly(String paramName) {
		return true;
	}


	private static class EmptySchema implements Schema {

		List<String> list = new ArrayList<String>();


    /**
     * Gets the schema for the specified parameter.
     *
     * @param paramName the name of the parameter
     * @return the schema for the specified parameter
     */
    public ParameterSchema getDetails(String paramName) {
      return null;
    }

    /**
		 * Gets an iterable over a list of parameter names.
		 *
		 * @return an iterable over a list of parameter names.
		 */
		public Iterable<String> parameterNames() {
			return list;
		}

		/**
		 * Validates the specified object against the type info contained for the parameter name.
		 *
		 * @param paramName the name of the parameter
		 * @param obj       the object to validate
		 * @return true if the object is of a type valid for the parameter, otherwise false.
		 */
		public boolean validate(String paramName, Object obj) {
			throw new IllegalParameterException("Type for parameter '" + paramName + "' not found.");
		}

		/**
		 * Returns true if this schema contains contains the specified parameter name, otherwise false.
		 *
		 * @param paramName
		 * @return true if this schema contains contains the specified parameter name, otherwise false.
		 */
		public boolean contains(String paramName) {
			return false; 
		}

		/**
		 * Gets the number of parameters in this schema.
		 *
		 * @return the number of parameters in this schema.
		 */
		public int size() {
			return 0;
		}
	}
}
