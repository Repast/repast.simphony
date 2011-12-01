package repast.simphony.ui.probe;

import repast.simphony.parameter.StringConverter;

/**
 * @author Nick Collier
 *         Date: Aug 20, 2007 2:48:59 PM
 */
public class MutableNumberConverter implements StringConverter<MutableNumber> {


  /**
   * Converts the specified object to a String representation and
   * returns that representation. The represenation should be such
   * that <code>fromString</code> can recreate the Object.
   *
   * @param obj the Object to convert.
   * @return a String representation of the Object.
   */
  public String toString(MutableNumber obj) {
    return String.valueOf(obj.getVal());
  }

  /**
   * Creates an Object from a String representation.
   *
   * @param strRep the string representation
   * @return the created Object.
   */
  public MutableNumber fromString(String strRep) {
    return new MutableNumber(Double.valueOf(strRep));
  }
}
