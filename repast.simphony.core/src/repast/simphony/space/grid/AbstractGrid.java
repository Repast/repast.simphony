/*CopyrightHere*/
package repast.simphony.space.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.space.SpatialException;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionPredicate;

/**
 * Default implementation of an n-dimensional grid.
 */
public abstract class AbstractGrid<T, U> extends DefaultProjection<T> implements Grid<T> {

  private final List<T> EMPTY_LIST = new ArrayList<T>();
  private double[] vectorTmp;

  /**
   * A little class used for getting rid of some hash table lookups.
   */
  public static class PointHolder {
    GridPoint point;
  }

  // this holds the objects that have been added, this includes ones that have
  // been
  // added, but not yet placed on the grid (through a move).  If they have not been
  // moved then they are assosciated with a null point
  protected Map<T, PointHolder> agentLocationMap;

  protected U locationStorage;

  protected GridDimensions dimensions;

  protected GridAdder<T> adder;

  protected GridPointTranslator translator;

  protected CellAccessor<T, U> accessor;

  protected boolean ok = true;

  protected int size = 0;

  /**
   * Constructs this space with the specified name, adder, translator,
   * accessor and size. The size is the size of the space meaning [3, 3] is a
   * 3x3 space.
   *
   * @param name the name of the space
   * @param size the dimensions of the space
   */
  public AbstractGrid(String name, GridAdder<T> adder,
                      GridPointTranslator translator, CellAccessor<T, U> accessor,
                      int... size) {
    super(name);
    this.adder = adder;
    this.translator = translator;
    this.accessor = accessor;

    int[] aSize = new int[size.length];
    int i = 0;
    for (int dim : size) {
      aSize[i++] = dim;
    }
    this.dimensions = new GridDimensions(aSize);
    vectorTmp = new double[this.dimensions.size()];

    this.agentLocationMap = new HashMap<T, PointHolder>();
    this.locationStorage = createLocationStorage();
    this.translator.init(dimensions);
  }

  /**
   * Constructs this space with the specified name, adder, translator,
   * accessor and size. The size is the size of the space meaning [3, 3] is a
   * 3x3 space.
   *
   * @param name   the name of the space
   * @param size   the dimensions of the space
   * @param origin the origin of the space
   */
  public AbstractGrid(String name, GridAdder<T> adder,
                      GridPointTranslator translator, CellAccessor<T, U> accessor,
                      int[] size, int[] origin) {
    super(name);
    this.adder = adder;
    this.translator = translator;
    this.accessor = accessor;

    int[] aSize = new int[size.length];
    int i = 0;
    for (int dim : size) {
      aSize[i++] = dim;
    }
    this.dimensions = new GridDimensions(aSize, origin);
    vectorTmp = new double[this.dimensions.size()];

    this.agentLocationMap = new HashMap<T, PointHolder>();
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
   *          if the object is not already in the space or if the number of
   *          dimensions in the location does not agree with the number in
   *          the space.
   */
  public boolean moveTo(T object, int... newLocation) {
    PointHolder holder = agentLocationMap.get(object);
    if (holder == null) {
      throw new SpatialException("Object '" + object +
              "' must be added to the grid's context before it can be moved");
    }

    if (newLocation.length != dimensions.size()) {
      throw new SpatialException("Number of new location dimensions must match grid dimensions");
    }

    int[] movedCoords = new int[dimensions.size()];
    translator.transform(movedCoords, newLocation);
    return doMove(object, movedCoords, holder) != null;

  }


  // assigns the specified object to the specified coordinates.
  // returns null if the object cannot be put at the specified coordinates.
  private GridPoint doMove(T object, int[] movedCoords, PointHolder holder) {
    GridPoint movedPoint = new GridPoint(movedCoords);
    if (accessor.put(object, locationStorage, movedPoint)) {
      if (holder.point == null) {
        // if the object hasn't yet been put in the space
        size++;
      } else {
        accessor.remove(object, locationStorage, holder.point);
      }
      holder.point = movedPoint;
      fireProjectionEvent(new ProjectionEvent(this, object,
              ProjectionEvent.OBJECT_MOVED));
      return holder.point;
    } else {
      return null;
    }
  }

  /**
   * Gets the location of the specified object.
   *
   * @param obj
   * @return the location of the specified object or null if the object is not
   *         in the space.
   */
  public GridPoint getLocation(Object obj) {
    PointHolder loc = agentLocationMap.get(obj);
    if (loc == null) {
      return null;
    }
    return loc.point;
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
    return size;
  }

  /**
   * Retrieves the rule being used for controlling what happens at or beyond
   * the borders of the space.
   *
   * @return the rule for handling out of bounds coordinates
   */
  public GridPointTranslator getGridPointTranslator() {
    return translator;
  }

  /**
   * Sets the rule to use for controlling what happens at or beyond the
   * borders of the space.
   *
   * @param rule the rule for handling out of bounds coordinates
   */
  public void setGridPointTranslator(GridPointTranslator rule) {
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
  public T getObjectAt(int... location) {
    int[] loc = getTransformedLocation(location);
    if (loc == null) return null;
    return accessor.get(locationStorage, new GridPoint(loc));
  }

  /**
   * Gets all the objects at the specified location. For a multi occupancy
   * space this will be all the objects at that location. For a single
   * occupancy space this will be the single object at that location.
   *
   * @param location
   * @return the object at the specified location.
   */
  public Iterable<T> getObjectsAt(int... location) {
    int[] loc = getTransformedLocation(location);
    if (loc == null) return EMPTY_LIST;
    return accessor.getAll(locationStorage, new GridPoint(loc));
  }

  protected int[] getTransformedLocation(int... location) {
    int[] loc = new int[location.length];
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
  public T getRandomObjectAt(int... location) {
    int[] loc = getTransformedLocation(location);
    if (loc == null) return null;
    return accessor.getRandom(locationStorage, new GridPoint(loc));
  }

  /**
   * Moves the specified object from its current location by the specified
   * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
   * will move the object by 3 along the x-axis, -2 along the y and 1 along
   * the z. The displacement argument can be less than the number of
   * dimensions in the space in which case the remaining argument will be
   * set to 0. For example, <code>moveByDisplacement(object, 3)</code> will
   * move the object 3 along the x-axis and 0 along the y and z axes, assuming
   * a 3D grid.
   *
   * @param object       the object to move
   * @param displacement the amount to move the object
   * @return the new location if the move was successful, otherwise null
   * @throws repast.simphony.space.SpatialException
   *          if the object is not already in the space or if the number of
   *          dimensions in the displacement does not agree with the number in
   *          the grid.
   */
  public GridPoint moveByDisplacement(T object, int... displacement) {
    if (dimensions.size() < displacement.length) {
      throw new SpatialException(
              "Displacement matrix cannot have more dimensions than space");
    }

    PointHolder holder = agentLocationMap.get(object);
    if (holder == null) {
      throw new SpatialException(
              "Object '"
                      + object
                      + "' must be added to the space's context before it can be moved");
    }

    int[] movedCoords = new int[dimensions.size()];
    // assumes that the first introducing move into a grid is never by displacement
    holder.point.toIntArray(movedCoords);
    translator.translate(movedCoords, displacement);
    return doMove(object, movedCoords, holder);
  }

  /**
   * Moves the specifed object the specified distance from its current
   * position along the specified angle. For example, <code>moveByVector(object, 1, Grid.NORTH)</code>
   * will move the object 1 unit "north" up the y-axis, assuming a 2D grid. Similarly,
   * <code>grid.moveByVector(object, 2, 0, Math.toRadians(90), 0)</code> will rotate 90
   * degrees around the y-axis, thus moving the object 2 units along the z-axis.
   * <p/>
   * <b> Note that the radians / degrees are incremented in a anti-clockwise fashion, such that
   * 0 degrees is "east",  90 degrees is "north", 180 is "west" and 270 is "south."
   *
   * @param object          the object to move
   * @param distance        the distance to move
   * @param anglesInRadians the angle to move along in radians. Note that Math.toRadians(degrees)
   *                        is useful here
   * @return the new location or null if the move is not successful
   * @throws repast.simphony.space.SpatialException
   *          if the object is not already in the space or if the number of
   *          angles is greater than the number of dimensions
   * @see repast.simphony.space.Direction#EAST
   * @see repast.simphony.space.Direction#NORTH
   * @see repast.simphony.space.Direction#SOUTH
   * @see repast.simphony.space.Direction#WEST
   */
  public GridPoint moveByVector(T object, double distance, double... anglesInRadians) {
    int size = dimensions.size();
    int length = anglesInRadians.length;
    if (size < length) {
      throw new SpatialException("Number of angles must be less than or equal to the number of dimensions");
    }
    if (length < size) {
      for (int i = 0; i < vectorTmp.length; i++)
        vectorTmp[i] = 0;

      for (int i = 0; i < length; i++)
        vectorTmp[i] = anglesInRadians[i];

      return moveByDisplacement(object, SpatialMath.getDisplacementInt(size, 0, distance, vectorTmp));
    }

    return moveByDisplacement(object, SpatialMath.getDisplacementInt(size, 0, distance, anglesInRadians));
  }

  /**
   * Gets the dimensions of the grid.
   *
   * @return the dimensions of the grid.
   */
  public GridDimensions getDimensions() {
    return dimensions;
  }

  /**
   * Sets the adder used by this space to add new objects.
   *
   * @param adder the adder
   */
  public void setAdder(GridAdder<T> adder) {
    this.adder = adder;
  }

  /**
   * Gets the adder used by this space to add new objects.
   *
   * @return the adder used by this space to add new objects.
   */
  public GridAdder<T> getAdder() {
    return adder;
  }

  /**
   * Gets the cell accessor used to control access to individual
   * grid cells.
   *
   * @return the cell accessor used to control access to individual
   *         grid cells.
   */
  public CellAccessor getCellAccessor() {
    return accessor;
  }

  protected void removeAll() {
    for (T t : agentLocationMap.keySet()) {
      remove(t);
    }
  }

  protected void remove(T t) {
    GridPoint location = agentLocationMap.remove(t).point;
    // location can be null, if the agent has been added
    // but never movedTo a location in the grid.
    if (location != null) {
      accessor.remove(t, locationStorage, location);
      size--;
      fireProjectionEvent(new ProjectionEvent(this, t, ProjectionEvent.OBJECT_REMOVED));
    }
  }

  /**
   * True if this grid is peridoic (in the sense that moving off one border
   * makes you appear on the other one), otherwise false. A 2D periodic grid is
   * a torus. This is defined by the grid's border behavior which is determined by
   * its GridPointTranslator.
   *
   * @return true if this grid is periodic, otherwise false.
   */
  public boolean isPeriodic() {
    return translator.isToroidal();
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
   * Gets the the euclidian distance between the GridPoints point1 and point2.
   * If the points do not have the same dimension then this returns Double.NaN
   *
   * @param point1 the first point
   * @param point2 the second point
   * @return the euclidian distance between the GridPoints point1 and point2.
   *         If the points do not have the same dimension then this returns Double.NaN
   */
  public double getDistance(GridPoint point1, GridPoint point2) {
    double distanceSq = getDistanceSq(point1, point2);
    return Double.isNaN(distanceSq) ? distanceSq : Math.sqrt(distanceSq);
  }

  /**
   * Gets the square of the euclidian distance between the GridPoints point1 and point2.
   * If the points do not have the same dimension then this returns Double.NaN
   *
   * @param point1 the first point
   * @param point2 the second point
   * @return the square of the euclidian distance between the GridPoints point1 and point2.
   *         If the points do not have the same dimension then this returns Double.NaN
   */
  public double getDistanceSq(GridPoint point1, GridPoint point2) {
    if (point1.dimensionCount() != point2.dimensionCount()) return Double.NaN;

    double sum = 0;
    for (int i = 0, n = point1.point.length; i < n; i++) {
      double diff = point1.point[i] - point2.point[i];
      if (isPeriodic()) {
        int dim = this.getDimensions().getDimension(i);
        double absDiff = Math.abs(diff);
        if (absDiff > dim / 2) {
          diff = dim - absDiff;
        }
      }
      sum += diff * diff;
    }

    return sum;

  }
}
