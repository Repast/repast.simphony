package repast.simphony.visualization.editor.space;

import repast.simphony.space.grid.*;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid that reprojects a 3D grid into a 2D grid. This is intended
 * to be used with editing code in visualization and such will not
 * work correctly if used as a regular projection.
 *
 * @author Nick Collier
 */
public class Projected3DGrid<T> extends DefaultProjection<T> implements Grid<T>, ProjectionListener {

  private Grid<T> grid;
  private int dimIndex1, dimIndex2, constantIndex;

  public Projected3DGrid(Grid<T> grid, int dimIndex1, int dimIndex2) {
    super("2D of " + grid.getName());
    this.grid = grid;
    this.grid.addProjectionListener(this);
    this.dimIndex1 = dimIndex1;
    this.dimIndex2 = dimIndex2;

    if (grid.getDimensions().size() != 3) {
      throw new IllegalArgumentException("Grid must be 3D");
    }

    if (dimIndex1 == 0) {
      if (dimIndex2 == 1) constantIndex = 2;
      else if (dimIndex2 == 2) constantIndex = 1;

    } else if (dimIndex1 == 1) {
      if (dimIndex2 == 2) constantIndex = 0;
      else if (dimIndex2 == 0) constantIndex = 2;

    } else {
      // dimIndex1 == 2
      if (dimIndex2 == 0) constantIndex = 1;
      if (dimIndex2 == 1) constantIndex = 0;
    }
  }

  /**
   * Gets the index of the constant dimension.
   *
   * @return the index of the constant dimension.
   */
  public int getConstantIndex() {
    return constantIndex;
  }

  public int getIndex1() {
    return dimIndex1;
  }

  public int getIndex2() {
    return dimIndex2;
  }

  /**
   * Gets the dimensions of the space.
   *
   * @return the dimensions of the space.
   */
  public GridDimensions getDimensions() {
    GridDimensions dimensions = grid.getDimensions();
    int[] dims = new int[]{dimensions.getDimension(dimIndex1), dimensions.getDimension(dimIndex2)};
    return new GridDimensions(dims);
  }

  /**
   * Sets the adder used by this space to add new objects.
   * Unsupported operation.
   *
   * @param gridAdder the adder
   */
  public void setAdder(GridAdder<T> gridAdder) {
    grid.setAdder(gridAdder);
  }

  /**
   * Gets the cell accessor used to control access to individual
   * grid cells.
   *
   * @return the cell accessor used to control access to individual
   *         grid cells.
   */
  public CellAccessor getCellAccessor() {
    return grid.getCellAccessor();
  }

  /**
   * Gets the adder used by this space to add new objects.
   *
   * @return the adder used by this space to add new objects.
   */
  public GridAdder<T> getAdder() {
    return grid.getAdder();
  }

  /**
   * Moves the specified object from its current location to the new location.
   * The object must previously have been introduced into the space. Objects
   * are introduced into the space by adding them to the context of which this
   * space is a projection.
   *
   * @param object
   * @param newLocation
   * @return true if the move was successful, otherwise false.
   * @throws repast.simphony.space.SpatialException if the object is not already in the space, if the number of
   *                                       dimensions in the location does not agree with the number in
   *                                       the space, or if the object is moved outside the grid
   *                                       dimensions.
   */
  public boolean moveTo(T object, int... newLocation) {
    GridPoint pt = grid.getLocation(object);
    int[] loc = new int[3];
    loc[dimIndex1] = newLocation[0];
    loc[dimIndex2] = newLocation[1];
    loc[constantIndex] = pt.getCoord(constantIndex);
    return grid.moveTo(object, loc);
  }

  /**
   * Gets the location of the specified object.
   *
   * @param obj
   * @return the location of the specified object or null if the object is not
   *         in the space.
   */
  public GridPoint getLocation(Object obj) {
    GridPoint pt = grid.getLocation(obj);
    int[] loc = new int[]{pt.getCoord(dimIndex1), pt.getCoord(dimIndex2)};
    return new GridPoint(loc);
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
    return grid.size();
  }

  /**
   * Retrieves the rule being used for controlling what happens at or beyond
   * the borders of the space. Unsupported operation.
   *
   * @return the rule for handling out of bounds coordinates
   */
  public GridPointTranslator getGridPointTranslator() {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Sets the rule to use for controlling what happens at or beyond the
   * borders of the space. Unsupported operation. Unsupported operation.
   *
   * @param rule the rule for handling out of bounds coordinates
   */
  public void setGridPointTranslator(GridPointTranslator rule) {
    throw new UnsupportedOperationException("Unsupported");
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
    return grid.getObjects();
  }

  /**
   * Gets the object at the specified location.
   *
   * @param location
   * @return the object at the specified location.
   */
  public T getObjectAt(int... location) {
    int[] loc = new int[3];
    loc[dimIndex1] = location[0];
    loc[dimIndex2] = location[1];
    GridDimensions dimensions = grid.getDimensions();
    int n = dimensions.getDimension(constantIndex);
    for (int i = 0; i < n; i++) {
      loc[constantIndex] = i;
      T obj = grid.getObjectAt(loc);
      if (obj != null) return obj;
    }

    return null;
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
    int[] loc = new int[3];
    loc[dimIndex1] = location[0];
    loc[dimIndex2] = location[1];
    GridDimensions dimensions = grid.getDimensions();
    int n = dimensions.getDimension(constantIndex);
    List<T> objs = new ArrayList<T>();
    for (int i = 0; i < n; i++) {
      loc[constantIndex] = i;
      T obj = grid.getObjectAt(loc);
      if (obj != null) objs.add(obj);
    }

    return objs;
  }

  /**
   * Gets a random object from among those at the specified location. If this
   * is a single occupancy space this will return the single object at that
   * location, if any. Unsupported operation.
   *
   * @param location
   * @return the object at the specified location.
   */
  public T getRandomObjectAt(int... location) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Moves the specified object from its current location by the specified
   * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
   * will move the object by 3 along the x-axis, -2 along the y and 1 along
   * the z. The displacement argument can be less than the number of
   * dimensions in the space in which case the remaining argument will be set
   * to 0. For example, <code>moveByDisplacement(object, 3)</code> will move
   * the object 3 along the x-axis and 0 along the y and z axes, assuming a 3D
   * grid.
   * <p/>
   * Unsupported operation.
   *
   * @param object       the object to move
   * @param displacement the amount to move the object
   * @return the new location if the move was successful, otherwise null
   * @throws repast.simphony.space.SpatialException if the object is not already in the space or if the number of
   *                                       dimensions in the displacement greater than the number of
   *                                       grid dimensions.
   */
  public GridPoint moveByDisplacement(T object, int... displacement) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Moves the specifed object the specified distance from its current
   * position along the specified angle. For example,
   * <code>moveByVector(object, 1, Direction.NORTH)</code> will move the
   * object 1 unit "north" up the y-axis, assuming a 2D grid. Similarly,
   * <code>grid.moveByVector(object, 2, 0, Math.toRadians(90), 0)</code>
   * will rotate 90 degrees around the y-axis, thus moving the object 2 units
   * along the z-axis.
   * <p/>
   * <b> Note that the radians / degrees are incremented in a anti-clockwise
   * fashion, such that 0 degrees is "east", 90 degrees is "north", 180 is
   * "west" and 270 is "south."
   * <p/>
   * Unsupported operation.
   *
   * @param object          the object to move
   * @param distance        the distance to move
   * @param anglesInRadians the angle to move along in radians. Note that
   *                        Math.toRadians(degrees) is useful here
   * @return the new location or null if the move is not successful
   * @throws repast.simphony.space.SpatialException if the object is not already in the space or if the number of
   *                                       angles is greater than the number of dimensions
   * @see repast.simphony.space.Direction#EAST
   * @see repast.simphony.space.Direction#NORTH
   * @see repast.simphony.space.Direction#SOUTH
   * @see repast.simphony.space.Direction#WEST
   */
  public GridPoint moveByVector(T object, double distance, double... anglesInRadians) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * True if this grid is peridoic (in the sense that moving off one border
   * makes you appear on the other one), otherwise false. A 2D periodic grid
   * is a torus. This is defined by the grid's border behavior which is
   * determined by its GridPointTranslator.
   *
   * @return always false
   */
  public boolean isPeriodic() {
    return false;
  }

  /**
   * Destroys this Projected3DGrid by doing any necessary clean up.
   */
  public void destroy() {
    grid.removeProjectionListener(this);
  }

  /**
   * Invoked when a projection event occurs. This forwards on
   * projection events fired by the 3D grid to any listeners
   * of this grid.
   *
   * @param evt the object describing the event
   */
  public void projectionEventOccurred(ProjectionEvent evt) {
    fireProjectionEvent(evt);
  }

  
  /**
	 * Gets the the euclidian distance between the GridPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 *  
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the euclidian distance between the GridPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 */
	public double getDistance(GridPoint point1, GridPoint point2){
		double distanceSq = getDistanceSq(point1,point2);
		return Double.isNaN(distanceSq) ? distanceSq : Math.sqrt(distanceSq);
	}
	
	/**
	 * Gets the square of the euclidian distance between the GridPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 *
	 * @param point1 the first point
	 * @param point2 the second point
	 * @return the square of the euclidian distance between the GridPoints point1 and point2.
	 * If the points do not have the same dimension then this returns Double.NaN
	 */
	public double getDistanceSq(GridPoint point1, GridPoint point2) {
		if (point1.dimensionCount() != point2.dimensionCount()) return Double.NaN;

		double sum = 0;
		for (int i = 0; i < 2; i++) {
			double diff = point1.getCoord(i) - point2.getCoord(i);
			if (isPeriodic()){
				int dim = this.getDimensions().getDimension(i);
				double absDiff = Math.abs(diff);
				if (absDiff > dim / 2){
					diff = dim - absDiff;
				}
			}
			sum += diff * diff;
		}

		return sum;

	}
}
