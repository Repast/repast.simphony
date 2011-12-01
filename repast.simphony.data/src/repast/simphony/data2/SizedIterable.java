/**
 * 
 */
package repast.simphony.data2;

/**
 * An iterable with a size
 * 
 * @author Nick Collier
 */
public interface SizedIterable<T> extends Iterable<T> {
  
  /**
   * Gets the number of elements in the iterable.
   * 
   * @return the number of elements in the iterable.
   */
  int size();

}
