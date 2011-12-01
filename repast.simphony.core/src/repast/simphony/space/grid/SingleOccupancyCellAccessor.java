package repast.simphony.space.grid;

import org.apache.commons.collections.iterators.SingletonIterator;
import repast.simphony.util.collections.IterableAdaptor;

import java.util.Collections;
import java.util.Map;

/**
 * Coordinate accessor for a space where each location holds a single object.
 *
 * @author Nick Collier
 */
public class SingleOccupancyCellAccessor<T> implements CellAccessor<T, Map<GridPoint, Object>> {

  /**
   * Gets the item at the specified location in the map.
   *
   * @param locationMap the location map to put the object in
   * @param location    the location in the map
   * @return the item at the specified location
   */
  @SuppressWarnings("unchecked")
  public T get(Map<GridPoint, Object> locationMap, GridPoint location) {
    return (T) locationMap.get(location);
  }

  /**
   * Gets all the items at the specified location in the map.
   *
   * @param locationMap the location map to retrieve from
   * @param location    the location in the map
   * @return an iterator over all the items at the specified location
   */
  @SuppressWarnings("unchecked")
  public Iterable<T> getAll(Map<GridPoint, Object> locationMap, GridPoint location) {
    T obj = get(locationMap, location);
    if (obj == null) {
      return Collections.EMPTY_LIST;
    }

    return new IterableAdaptor<T>(new SingletonIterator(obj, false));
  }

  /**
   * Gets a random item from those at the specified location in the map.
   *
   * @param locationMap the location map to retrieve from
   * @param location    the location in the map
   * @return a random item at the specified location
   */
  public T getRandom(Map<GridPoint, Object> locationMap, GridPoint location) {
    return get(locationMap, location);
  }

  /**
   * Attempts to put the specified object at the specified location. Returns true if the put was
   * successful. The semantics of the result will be determined by implementing classes.
   *
   * @param obj         the object to put at the location
   * @param locationMap the location map to put the object in
   * @param location    the location in the map
   * @return true if the put was successful, otherwise false.
   */
  public boolean put(T obj, Map<GridPoint, Object> locationMap, GridPoint location) {
    if (locationMap.get(location) == null) {
      locationMap.put(location, obj);
      return true;
    }
    return false;
  }

  /**
   * Always returns false.
   *
   * @return true if this cell accessor allows multi occupancy,
   *         otherwise false.
   */
  public boolean allowsMultiOccupancy() {
    return false;
  }

  /**
   * Removes the specified object from the location in the map.
   *
   * @param obj         the object to remove
   * @param locationMap the location map to remove the object from
   * @param location    the location in the map
   */
  public void remove(T obj, Map<GridPoint, Object> locationMap, GridPoint location) {
    locationMap.remove(location);
	}
}
