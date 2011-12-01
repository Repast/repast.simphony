package repast.simphony.parameter;

import repast.simphony.parameter.StringConverter;

/**
 * Convertor for a Name object
 * @author Nick Collier
 */
public class NameConverter implements StringConverter<Name> {


  /**
   * Converts the specified object to a String representation and
   * returns that representation. The represenation should be such
   * that <code>fromString</code> can recreate the Object.
   *
   * @param obj the Object to convert.
   * @return a String representation of the Object.
   */
  public String toString(Name obj) {
    return obj.getFirst() + " " + obj.getLast();
  }

  /**
   * Creates an Object from a String representation.
   *
   * @param strRep the string representation
   * @return the created Object.
   */
  public Name fromString(String strRep) {
    int index = strRep.indexOf(" ");
    String first = strRep.substring(0, index);
    String last = strRep.substring(index + 1, strRep.length());
    return new Name(first, last);
  }
}
