/**
 * 
 */
package repast.simphony.data2;

/**
 * Interface for classes that can function as the source of non aggregated data
 * to be logged or charted.
 *
 * @author Nick Collier
 */
public interface NonAggregateDataSource extends DataSource {

  /**
   * Gets data given the specified object.
   * 
   * @param obj
   *          an object to get the data from
   * @return the retrieved data.
   */
  Object get(Object obj);

}
