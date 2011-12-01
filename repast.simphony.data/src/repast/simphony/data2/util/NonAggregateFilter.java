/**
 * 
 */
package repast.simphony.data2.util;


/**
 * Filter for class types that are allowed on non-aggregate data sets.
 * 
 * @author Nick Collier
 */
public class NonAggregateFilter extends AggregateFilter {
  
  /**
   * Checks whether or not the specified class passes this filter.
   * 
   * @param clazz
   * @return true if the class passes, otherwise false.
   */
  public boolean check(Class<?> clazz) {
    if (super.check(clazz)) return true;
    return Enum.class.isAssignableFrom(clazz) || clazz.equals(String.class);
  }
}
