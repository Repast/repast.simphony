package repast.simphony.query;

/**
 * @author Nick Collier
 */
public interface PropertyEqualsPredicate<T, V> {

  /**
   * Evaluates t against v, returning true or false.
   *
   * @param t the property value as gotten from the agent
   * @param v the property value to evaluate against
   * @return true if t evaluates to true with respect to v, otherwise false.
   */
  boolean evaluate(T t, V v);

}
