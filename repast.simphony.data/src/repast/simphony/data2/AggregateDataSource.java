package repast.simphony.data2;

/**
 * Interface for classes that can function as the source of aggregate data to be
 * logged or charted.
 * 
 * @author Nick Collier
 */
public interface AggregateDataSource extends DataSource {

  /**
   * Gets the data using the specified iterable.
   * 
   * @param size
   *          the number of objects in the iterable
   * @param objs
   *          the iterable over objects to use in getting the data
   * 
   * @return the data using the specified iterable.
   */
  Object get(Iterable<?> objs, int size);

  /**
   * Resets this AggregateDataSource prior to the next get call.
   */
  void reset();
}
