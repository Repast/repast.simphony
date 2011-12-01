package repast.simphony.ui.probe;

import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

import repast.simphony.parameter.StringConverter;

/**
 * Converts an Amount to and from a String representation.
 * 
 * @author Nick Collier
 */
public class AmountConverter implements StringConverter<Amount> {

	Unit units = Unit.ONE;
	
	/**
	 * Converts the specified object to a String representation and returns that
	 * representation. The represenation should be such that
	 * <code>fromString</code> can recreate the Object.
	 * 
	 * @param obj
	 *            the Object to convert.
	 * @return a String representation of the Object.
	 */
	public String toString(Amount obj) {
		Amount a = (Amount) obj;
		if (units == null)
			units = a.getUnit();
		return Utils.getNumberFormatInstance().format(obj.getEstimatedValue());
	}

	/**
	 * Creates an Object from a String representation.
	 * 
	 * @param strRep
	 *            the string representation
	 * @return the created Object.
	 */
	public Amount fromString(String strRep) {
		return Amount.valueOf(Double.parseDouble(strRep), units);
	}
}
