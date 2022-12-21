package repast.simphony.ui.probe;

import javax.measure.Quantity;

import repast.simphony.parameter.StringConverter;
import tech.units.indriya.AbstractQuantity;

/**
 * Converts an Amount to and from a String representation.
 * 
 * @author Nick Collier
 */
public class QuantityConverter implements StringConverter<Quantity<?>> {
	
	/**
	 * Converts the specified object to a String representation and returns that
	 * representation. The represenation should be such that
	 * <code>fromString</code> can recreate the Object.
	 * 
	 * @param obj
	 *            the Object to convert.
	 * @return a String representation of the Object.
	 */
	public String toString(Quantity<?> quantity) {
		return quantity.toString();
	}

	/**
	 * Creates an Object from a String representation.
	 * 
	 * @param strRep
	 *            the string representation
	 * @return the created Object.
	 */
	public Quantity<?> fromString(String strRep) {
		return AbstractQuantity.parse(strRep);
	}
}
