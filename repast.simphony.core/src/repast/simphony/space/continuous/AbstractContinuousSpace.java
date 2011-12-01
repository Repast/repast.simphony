/*CopyrightHere*/
package repast.simphony.space.continuous;

import java.util.Map;

import javolution.util.FastMap;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionPredicate;

/**
 * Default implementation of an n-dimensional continuous space.
 */
// NOTE THAT ALL MOVEMENT OF AGENTS SHOULD RESOLVE TO A CALL TO doMove.
public abstract class AbstractContinuousSpace<T, U> extends DefaultProjection<T> implements
		ContinuousSpace<T> {

	/**
	 * A little class used for getting rid of some hash table lookups.
	 */
	public static class PointHolder { 
		public NdPoint point;
	}
	
	// this holds the objects that have been added, this includes ones that have
	// been
	// added, but not yet placed on the grid (through a move).  If they have not been
	// moved then they are assosciated with a null point
	protected Map<T, PointHolder> agentLocationMap;

	protected U locationStorage;

	protected Dimensions dimensions;

	protected ContinuousAdder<T> adder;

	protected PointTranslator translator;

	protected CoordinateAccessor<T, U> accessor;
	
	protected int size = 0;

	/**
	 * Constructs this space with the specified name, adder, translator,
	 * accessor and size. The size is the size of the space meaning [3, 3] is a
	 * 3x3 space.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public AbstractContinuousSpace(String name, ContinuousAdder<T> adder,
			PointTranslator translator, CoordinateAccessor<T, U> accessor,
			double... size) {
		super(name);
		this.adder = adder;
		this.translator = translator;
		this.accessor = accessor;

		int _size = 1;
		double[] aSize = new double[size.length];
		int i = 0;
		for (double dim : size) {
			_size *= dim;
			aSize[i++] = dim;
		}
		this.dimensions = new Dimensions(aSize);

		this.agentLocationMap = new FastMap<T, PointHolder>();
		this.locationStorage = createLocationStorage();
		this.translator.init(dimensions);
	}
	/**
	 * Constructs this space with the specified name, adder, translator,
	 * accessor and size. The size is the size of the space meaning [3, 3] is a
	 * 3x3 space.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 * @param origin
	 * 			  the origin of the space         
	 */
	public AbstractContinuousSpace(String name, ContinuousAdder<T> adder,
			PointTranslator translator, CoordinateAccessor<T, U> accessor,
			double[] size, double[] origin) {
		super(name);
		this.adder = adder;
		this.translator = translator;
		this.accessor = accessor;

		this.dimensions = new Dimensions(size, origin);

		this.agentLocationMap = new FastMap<T, PointHolder>();
		this.locationStorage = createLocationStorage();
		this.translator.init(dimensions);
	}
	
	protected abstract U createLocationStorage();

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
	public boolean moveTo(T object, double... newLocation) {
		return doMove(object, null, newLocation);
	}

	// moves the specified object from the specifid point to the new location
	// returns true if the move succeeded.
	protected boolean doMove(T object, double[] displacement, double[] newLocation) {
		PointHolder holder = agentLocationMap.get(object);
		if (holder == null) {
			throw new IllegalArgumentException(
					"Object '"
							+ object
							+ "' must be added to the space's context before it can be moved");
		}
		if (newLocation != null && (newLocation.length != dimensions.size())) {
			throw new IllegalArgumentException(
					"An object's new location must have the same number of dimensions as the space. (newLocation's dimensions: "
							+ newLocation.length
							+ ", space's:"
							+ dimensions.size() + ".");
		}

		double[] movedCoords = new double[dimensions.size()];
		if (holder.point == null || displacement == null) {
			translator.transform(movedCoords, newLocation);
		} else {
			holder.point.toDoubleArray(movedCoords);
			translator.translate(movedCoords, displacement);
		}
		NdPoint movedPoint = new NdPoint(movedCoords);

		if (accessor.put(object, locationStorage, movedPoint)) {
			if (holder.point != null) {
				accessor.remove(object, locationStorage, holder.point);
			} else {
				// if the object hasn't yet been put in the space
				size++;
			}
			holder.point = movedPoint; 
			fireProjectionEvent(new ProjectionEvent(this, object,
					ProjectionEvent.OBJECT_MOVED));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the location of the specified object.
	 * 
	 * @param obj
	 * @return the location of the specified object or null if the object is not
	 *         in the space.
	 */
	public NdPoint getLocation(Object obj) {
		PointHolder holder = agentLocationMap.get(obj);
		if (holder == null) {
			return null;
		}
		return holder.point;
	}

	/**
	 * Gets the number of objects currently in the space. This does NOT include
	 * any objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return the number of objects currently in the space. This does NOT
	 *         include any objects that may have been added, but have NOT been
	 *         moved to a space location.
	 */
	public int size() {
		return agentLocationMap.size();
	}

	/**
	 * Retrieves the rule being used for controlling what happens at or beyond
	 * the borders of the space.
	 * 
	 * @return the rule for handling out of bounds coordinates
	 */
	public PointTranslator getPointTranslator() {
		return translator;
	}

	/**
	 * Sets the rule to use for controlling what happens at or beyond the
	 * borders of the space.
	 * 
	 * @param rule
	 *            the rule for handling out of bounds coordinates
	 */
	public void setPointTranslator(PointTranslator rule) {
		this.translator = rule;
	}

	/**
	 * Gets all the object currently in the space. This does NOT include any
	 * objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return an iteratable over all the object currently in the space. This
	 *         does NOT include any objects that may have been added, but have
	 *         NOT been moved to a space location.
	 */
	public Iterable<T> getObjects() {
		return agentLocationMap.keySet();
	}

	/**
	 * Gets the object at the specified location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public T getObjectAt(double... location) {
		return accessor.get(locationStorage, new NdPoint(
				getLocation(location)));
	}

	/**
	 * Gets all the objects at the specified location. For a multi occupancy
	 * space this will be all the objects at that location. For a single
	 * occupancy space this will be the single object at that location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public Iterable<T> getObjectsAt(double... location) {
		return accessor.getAll(locationStorage, new NdPoint(
				getLocation(location)));
	}

	protected double[] getLocation(double... location) {
		double[] loc = new double[location.length];
		translator.transform(loc, location);
		return loc;
	}

	/**
	 * Gets a random object from among those at the specified location. If this
	 * is a single occupancy space this will return the single object at that
	 * location, if any.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public T getRandomObjectAt(double... location) {
		return accessor.getRandom(RandomHelper.getUniform(), locationStorage, new NdPoint(
				getLocation(location)));
	}

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
	 * @return the new location if the move was successful, otherwise null
	 * @throws repast.simphony.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the displacement greater than the number of grid dimensions.
	 */
	public NdPoint moveByDisplacement(T object, double... displacement) {
		if (dimensions.size() < displacement.length) {
			throw new IllegalArgumentException(
					"Displacement matrix cannot have more dimensions than space");
		}

		if (doMove(object, displacement, null)) {
			return agentLocationMap.get(object).point;
		} else {
			return null;
		}
	}

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
	 * @return the new location or null if the move is not successful
	 * @throws repast.simphony.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             angles is greater than the number of dimensions
	 * @see repast.simphony.space.Direction#EAST
	 * @see repast.simphony.space.Direction#NORTH
	 * @see repast.simphony.space.Direction#SOUTH
	 * @see repast.simphony.space.Direction#WEST
	 */
	public NdPoint moveByVector(T object, double distance,
			double... anglesInRadians) {
		if (dimensions.size() != anglesInRadians.length) {
			throw new IllegalArgumentException(
					"Displacement matrix has different number of dimensions than space");
		}
		return moveByDisplacement(object, SpatialMath.getDisplacement(
				dimensions.size(), 0, distance, anglesInRadians));
	}

	/**
	 * Gets the dimensions of the grid.
	 * 
	 * @return the dimensions of the grid.
	 */
	public Dimensions getDimensions() {
		return dimensions;
	}

	/**
	 * Sets the adder used by this space to add new objects.
	 * 
	 * @param adder
	 *            the adder
	 */
	public void setAdder(ContinuousAdder<T> adder) {
		this.adder = adder;
	}

	/**
	 * Gets the adder used by this space to add new objects.
	 * 
	 * @return the adder used by this space to add new objects.
	 */
	public ContinuousAdder<T> getAdder() {
		return adder;
	}

	protected void removeAll() {
		for (T t : agentLocationMap.keySet()) {
			remove(t);
		}
	}

	protected void remove(T t) {
		NdPoint location = agentLocationMap.remove(t).point;
		if (location != null) {
			accessor.remove(t, locationStorage, location);
		}
		size--;
		fireProjectionEvent(new ProjectionEvent(this, t,
				ProjectionEvent.OBJECT_REMOVED));
	}

	public boolean isPeriodic() {
		return translator.isPeriodic();
	}

	/**
	 * Evaluate this Projection against the specified Predicate. This typically
	 * involves a double dispatch where the Projection calls back to the
	 * predicate, passing itself.
	 *
	 * @param predicate
	 * @return true if the predicate evaluates to true, otherwise false. False
	 *         can also mean that the predicate is not applicable to this
	 *         Projection. For example, a linked type predicate evaluated
	 *         against a grid projection.
	 */
//	@Override
	public boolean evaluate(ProjectionPredicate predicate) {
		return predicate.evaluate(this);
	}
	
	/**
	 * Calculates the euclidian distance between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 *  
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the euclidian distance between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 */
	public double getDistance(NdPoint point1, NdPoint point2){
		double distanceSq = getDistanceSq(point1,point2);
		return Double.isNaN(distanceSq) ? distanceSq : Math.sqrt(distanceSq);
	}
	
	/**
	 * Calculates the square of the euclidian distance between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 *
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the square of the euclidian distance between the NdPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 */
	public double getDistanceSq(NdPoint point1, NdPoint point2) {
		if (point1.dimensionCount() != point2.dimensionCount()) return Double.NaN;

		double sum = 0;
		for (int i = 0, n = point1.dimensionCount(); i < n; i++) {
			double diff = point1.getCoord(i) - point2.getCoord(i);
			if (isPeriodic()){
				double dim = this.getDimensions().getDimension(i);
				double absDiff = Math.abs(diff);
				if (absDiff > dim / 2){
					diff = dim - absDiff;
				}
			}
			sum += diff * diff;
		}

		return sum;

	}
	
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
	public double[] getDisplacement(NdPoint point1, NdPoint point2){
		if (point1.dimensionCount() != point2.dimensionCount()) return null;
		
		double[] displacement = new double[point1.dimensionCount()];
		for (int i = 0, n = point1.dimensionCount(); i < n; i++) {
			double diff = point2.getCoord(i) - point1.getCoord(i);
			if (isPeriodic()){
				double dim = this.getDimensions().getDimension(i);
				double absDiff = Math.abs(diff);
				if (absDiff > dim / 2){
					diff = - Math.signum(diff) * (dim - absDiff);
				}
			}
			displacement[i] = diff;
		}
		return displacement;
	}
	
	
}
