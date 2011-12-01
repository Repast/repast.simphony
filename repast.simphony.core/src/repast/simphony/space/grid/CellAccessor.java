/*CopyrightHere*/
package repast.simphony.space.grid;


public interface CellAccessor<T, U> {
  /**
   * Attempts to put the specified object at the specified location. Returns true if the put was
   * successful. The semantics of the result will be determined by implementing classes.
   *
   * @param obj             the object to put at the location
   * @param locationStorage the location map to put the object in
   * @param location        the location in the map
   * @return true if the put was successful, otherwise false.
   */
  public boolean put(T obj, U locationStorage, GridPoint location);

  /**
   * Removes the specified object from the location in the map.
   *
   * @param obj             the object to remove
   * @param locationStorage the location map to remove the object from
   * @param location        the location in the map
   */
  public void remove(T obj, U locationStorage, GridPoint location);

  /**
   * Gets the item at the specified location in the map.
   *
   * @param locationStorage the location map to put the object in
   * @param location        the location in the map
   * @return the item at the specified location
   */
  public T get(U locationStorage, GridPoint location);

  /**
   * Gets all the items at the specified location in the map.
   *
   * @param locationStorage the location map to retrieve from
   * @param location        the location in the map
   * @return an iterator over all the items at the specified location
   */
  public Iterable<T> getAll(U locationStorage, GridPoint location);

  /**
   * Gets a random item from those at the specified location in the map.
   *
   * @param locationStorage the location map to retrieve from
   * @param location        the location in the map
   * @return a random item at the specified location
   */
  public T getRandom(U locationStorage, GridPoint location);

  /**
   *
   * @return true if this cell accessor allows multi occupancy,
   * otherwise false.
   */
  boolean allowsMultiOccupancy();
}
