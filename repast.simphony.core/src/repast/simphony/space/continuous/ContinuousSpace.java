/*CopyrightHere*/
package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;
import repast.simphony.space.Direction;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.projection.Projection;

/**
 * An n-dimensional continuous space.
 */
public interface ContinuousSpace<T> extends Projection<T> {

	/**
	 * Gets the dimensions of the space.
	 * 
	 * @return the dimensions of the space.
	 */
	Dimensions getDimensions();

	/**
	 * Sets the adder used by this space to add new objects.
	 * 
	 * @param adder
	 *            the adder
	 */
	void setAdder(ContinuousAdder<T> adder);

	/**
	 * Gets the adder used by this space to add new objects.
	 * 
	 * @return the adder used by this space to add new objects.
	 */
	ContinuousAdder<T> getAdder();

	/**
	 * Moves the specified object from its current location into the new
	 * location. The object must previously have been introduced into the space.
	 * Objects are introduced into the space by adding them to the context of
	 * which this space is a projection.
	 * 
	 * @param object
	 * @param newLocation
	 * @return true if the move was successful, otherwise false.
	 * @throws repast.simphony.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the location does not agree with the number in
	 *             the space.
	 */
	boolean moveTo(T object, double... newLocation);

	/**
	 * Gets the location of the specified object.
	 * 
	 * @param obj
	 * @return the location of the specified object or null if the object is not
	 *         in the space.
	 */
	NdPoint getLocation(Object obj);

	/**
	 * Gets the number of objects currently in the space. This does NOT include
	 * any objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return the number of objects currently in the space. This does NOT
	 *         include any objects that may have been added, but have NOT been
	 *         moved to a space location.
	 */
	int size();

	/**
	 * Retrieves the rule being used for controlling what happens at or beyond
	 * the borders of the space.
	 * 
	 * @return the rule for handling out of bounds coordinates
	 */
	PointTranslator getPointTranslator();

	/**
	 * Sets the rule to use for controlling what happens at or beyond the
	 * borders of the space.
	 * 
	 * @param rule
	 *            the rule for handling out of bounds coordinates
	 */
	void setPointTranslator(PointTranslator rule);

	/**
	 * Gets all the object currently in the space. This does NOT include any
	 * objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return an iteratable over all the object currently in the space. This
	 *         does NOT include any objects that may have been added, but have
	 *         NOT been moved to a space location.
	 */
	Iterable<T> getObjects();

	/**
	 * Gets the object at the specified location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	T getObjectAt(double... location);

	/**
	 * Gets all the objects at the specified location. For a multi occupancy
	 * space this will be all the objects at that location. For a single
	 * occupancy space this will be the single object at that location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	Iterable<T> getObjectsAt(double... location);

	/**
	 * Gets a random object from among those at the specified location. If this
	 * is a single occupancy space this will return the single object at that
	 * location, if any.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	T getRandomObjectAt(double... location);

	/**
	 * Moves the specified object from its current location by the specified
	 * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
	 * will move the object by 3 along the x-axis, -2 along the y and 1 along
	 * the z. The displacement argument can be less than the number of
	 * dimensions in the space.
	 * 
	 * @param object
	 *            the object to move
	 * @param displacement
	 *            the amount to move the object
	 * @throws repast.simphony.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the displacement greater than the number of grid dimensions.
	 * @return the new location if the move was successful, otherwise null
	 */
	NdPoint moveByDisplacement(T object, double... displacement);

	/**
	 * Moves the specifed object the specified distance from its current
	 * position along the specified angle.
	 * 
	 * @param object
	 *            the object to move
	 * @param distance
	 *            the distance to move
	 * @param anglesInRadians
	 *            the angle to move along
	 * @throws repast.simphony.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             angles is greater than the number of dimensions
	 * @see Direction#EAST
	 * @see Direction#NORTH
	 * @see Direction#SOUTH
	 * @see Direction#WEST
	 *
	 * @return the new location or null if the move is not successful
	 */
	NdPoint moveByVector(T object, double distance, double... anglesInRadians);

	/**
	 * True if this space is periodic (in the sense that moving off one border
	 * makes you appear on the other one), otherwise false.
	 * 
	 * @return true if this space is periodic, otherwise false.
	 */
	boolean isPeriodic();
	
	/**
	 * Returns the distance between the NdPoints point1 and point2, 
	 * taking into account the space's topology.
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the distance between the two points
	 */
	double getDistance(NdPoint point1, NdPoint point2);
	
	/**
	 * Returns the square of the distance between the NdPoints point1 and point2, 
	 * taking into account the space's topology.
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the square of the distance between the two points
	 */
	double getDistanceSq(NdPoint point1, NdPoint point2);
	
	/**
	 * Returns the displacement between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns null
	 * 
	 *  
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the displacement between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns null
	 */
	public double[] getDisplacement(NdPoint point1, NdPoint point2);

}
