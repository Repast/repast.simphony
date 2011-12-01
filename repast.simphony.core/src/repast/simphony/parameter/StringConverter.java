package repast.simphony.parameter;

/**
 * Converts an Object to and from a String representation. Subclasses
 * are specialized for specified objects.
 *
 * @author Nick Collier
 */
public interface StringConverter<T> {

  /**
   * Converts the specified object to a String representation and
   * returns that representation. The represenation should be such
   * that <code>fromString</code> can recreate the Object.
   *
   * @param obj the Object to convert.
   *
   * @return a String representation of the Object.
   */
  String toString(T obj);

  /**
   * Creates an Object from a String representation.
   *
   * @param strRep the string representation
   * @return the created Object.
   */
  T fromString(String strRep);
}
