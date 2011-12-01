/*CopyrightHere*/
package repast.simphony.space.continuous;

import cern.jet.random.Uniform;

public interface CoordinateAccessor<T, U> {
	/**
	 * Attempts to put the specified object at the specified location. Returns true if the put was
	 * successful. The semantics of the result will be determined by implementing classes.
	 * 
	 * @param obj
	 *            the object to put at the location
	 * @param locationStorage
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return true if the put was successful, otherwise false.
	 */
	public boolean put(T obj, U locationStorage, NdPoint location);

	/**
	 * Removes the specified object from the location in the map.
	 * 
	 * @param obj
	 *            the object to remove
	 * @param locationStorage
	 *            the location map to remove the object from
	 * @param location
	 *            the location in the map
	 */
	public void remove(T obj, U locationStorage, NdPoint location);

	/**
	 * Gets the item at the specified location in the map.
	 * 
	 * @param locationStorage
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return the item at the specified location
	 */
	public T get(U locationStorage, NdPoint location);

	/**
	 * Gets all the items at the specified location in the map.
	 * 
	 * @param locationStorage
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return an iterator over all the items at the specified location
	 */
	public Iterable<T> getAll(U locationStorage, NdPoint location);

	/**
	 * Gets a random item from those at the specified location in the map.
	 * 
	 * @param locationStorage
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return a random item at the specified location
	 */
	public T getRandom(Uniform distribution, U locationStorage, NdPoint location);
}
