package repast.simphony.space.continuous;

import cern.jet.random.Uniform;
import org.apache.commons.collections.iterators.SingletonIterator;
import repast.simphony.util.collections.IterableAdaptor;

import java.util.Collections;
import java.util.Map;

/**
 * Coordinate accessor for a space where each location holds a single object.
 * 
 * @author Nick Collier
 */
public class SingleOccupancyCoordinateAccessor<T> implements CoordinateAccessor<T, Map<NdPoint, Object>> {

	/**
	 * Gets the item at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return the item at the specified location
	 */
	@SuppressWarnings("unchecked")
	public T get(Map<NdPoint, Object> locationMap, NdPoint location) {
		return (T) locationMap.get(location);
	}

	/**
	 * Gets all the items at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return an iterator over all the items at the specified location
	 */
	@SuppressWarnings("unchecked")
	public Iterable<T> getAll(Map<NdPoint, Object> locationMap, NdPoint location) {
		T obj = get(locationMap, location);
		if (obj == null) {
			return Collections.EMPTY_LIST;
		}

		return new IterableAdaptor<T>(new SingletonIterator(obj, false));
	}

	/**
	 * Gets a random item from those at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return a random item at the specified location
	 */
	public T getRandom(Uniform distribution, Map<NdPoint, Object> locationMap, NdPoint location) {
		return get(locationMap, location);
	}

	/**
	 * Attempts to put the specified object at the specified location. Returns true if the put was
	 * successful. The semantics of the result will be determined by implementing classes.
	 * 
	 * @param obj
	 *            the object to put at the location
	 * @param locationMap
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return true if the put was successful, otherwise false.
	 */
	public boolean put(T obj, Map<NdPoint, Object> locationMap, NdPoint location) {
		if (locationMap.get(location) == null) {
			locationMap.put(location, obj);
			return true;
		}
		return false;
	}

	/**
	 * Removes the specified object from the location in the map.
	 * 
	 * @param obj
	 *            the object to remove
	 * @param locationMap
	 *            the location map to remove the object from
	 * @param location
	 *            the location in the map
	 */
	public void remove(T obj, Map<NdPoint, Object> locationMap, NdPoint location) {
		locationMap.remove(location);
	}
}
