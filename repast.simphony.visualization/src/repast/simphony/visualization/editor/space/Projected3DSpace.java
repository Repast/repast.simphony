package repast.simphony.visualization.editor.space;

import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A space that reprojects a 3D continuous space into a 2D grid. This is intended
 * to be used with editing code in visualization and such will not
 * work correctly if used as a regular projection.
 *
 * @author Nick Collier
 */
public class Projected3DSpace<T> extends DefaultProjection<T> implements ContinuousSpace<T>, ProjectionListener {

  private ContinuousSpace<T> space;
  private int dimIndex1, dimIndex2, constantIndex;

  public Projected3DSpace(ContinuousSpace<T> space, int dimIndex1, int dimIndex2) {
    super("2D of " + space.getName());
    this.space = space;
    this.space.addProjectionListener(this);
    this.dimIndex1 = dimIndex1;
    this.dimIndex2 = dimIndex2;

    if (space.getDimensions().size() != 3) {
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

  public int getIndex1() {
    return dimIndex1;
  }

  public int getIndex2() {
    return dimIndex2;
  }

  public int getConstantIndex() {
    return constantIndex;
  }

  /**
   * Gets the dimensions of the space.
   *
   * @return the dimensions of the space.
   */
  public Dimensions getDimensions() {
    Dimensions dimensions = space.getDimensions();
    double[] dims = new double[]{dimensions.getDimension(dimIndex1), dimensions.getDimension(dimIndex2)};
    return new Dimensions(dims);
  }

  /**
   * Sets the adder used by this space to add new objects.
   *
   * @param adder the adder
   */
  public void setAdder(ContinuousAdder<T> adder) {
    space.setAdder(adder);
  }

  /**
   * Gets the adder used by this space to add new objects.
   *
   * @return the adder used by this space to add new objects.
   */
  public ContinuousAdder<T> getAdder() {
    return space.getAdder();
  }

  /**
   * Moves the specified object from its current location into the new
   * location. The object must previously have been introduced into the space.
   * Objects are introduced into the space by adding them to the context of
   * which this space is a projection.
   *
   * @param object
   * @param newLocation
   * @return true if the move was successful, otherwise false.
   * @throws repast.simphony.space.SpatialException if the object is not already in the space or if the number of
   *                                       dimensions in the location does not agree with the number in
   *                                       the space.
   */
  public boolean moveTo(T object, double... newLocation) {
    NdPoint pt = space.getLocation(object);
    double[] loc = new double[3];
    loc[dimIndex1] = newLocation[0];
    loc[dimIndex2] = newLocation[1];
    loc[constantIndex] = pt.getCoord(constantIndex);
    return space.moveTo(object, loc);
  }

  /**
   * Gets the location of the specified object.
   *
   * @param obj
   * @return the location of the specified object or null if the object is not
   *         in the space.
   */
  public NdPoint getLocation(Object obj) {
    NdPoint pt = space.getLocation(obj);
    double[] loc = new double[] {pt.getCoord(dimIndex1), pt.getCoord(dimIndex2)};
    return new NdPoint(loc);
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
    return space.size();
  }

  /**
   * Retrieves the rule being used for controlling what happens at or beyond
   * the borders of the space. Unsupported operation.
   *
   * @return the rule for handling out of bounds coordinates
   */
  public PointTranslator getPointTranslator() {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Sets the rule to use for controlling what happens at or beyond the
   * borders of the space. Unsupported operation.
   *
   * @param rule the rule for handling out of bounds coordinates
   */
  public void setPointTranslator(PointTranslator rule) {
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
    return space.getObjects();
  }

  /**
   * Gets the object at the specified location.
   *
   * @param location
   * @return the object at the specified location.
   */
  public T getObjectAt(double... location) {
    double coord1 = location[0];
    double coord2 = location[1];
    for (T obj : getObjects()) {
      NdPoint pt = space.getLocation(obj);
      if (pt.getCoord(dimIndex1) == coord1 && pt.getCoord(dimIndex2) == coord2) return obj;
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
  public Iterable<T> getObjectsAt(double... location) {
    double[] loc = new double[3];
    double coord1 = location[0];
    double coord2 = location[1];
    List<T> objs = new ArrayList<T>();
    for (T obj : getObjects()) {
      NdPoint pt = getLocation(obj);
      if (pt.getCoord(dimIndex1) == coord1 && pt.getCoord(dimIndex2) == coord2) objs.add(obj);
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
  public T getRandomObjectAt(double... location) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Moves the specified object from its current location by the specified
   * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
   * will move the object by 3 along the x-axis, -2 along the y and 1 along
   * the z. The displacement argument can be less than the number of
   * dimensions in the space. Unsupported operation.
   *
   * @param object       the object to move
   * @param displacement the amount to move the object
   * @return the new location if the move was successful, otherwise null
   * @throws repast.simphony.space.SpatialException if the object is not already in the space or if the number of
   *                                       dimensions in the displacement greater than the number of grid dimensions.
   */
  public NdPoint moveByDisplacement(T object, double... displacement) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * Moves the specifed object the specified distance from its current
   * position along the specified angle. Unsupported operation.
   *
   * @param object          the object to move
   * @param distance        the distance to move
   * @param anglesInRadians the angle to move along
   * @return the new location or null if the move is not successful
   * @throws repast.simphony.space.SpatialException if the object is not already in the space or if the number of
   *                                       angles is greater than the number of dimensions
   * @see repast.simphony.space.Direction#EAST
   * @see repast.simphony.space.Direction#NORTH
   * @see repast.simphony.space.Direction#SOUTH
   * @see repast.simphony.space.Direction#WEST
   */
  public NdPoint moveByVector(T object, double distance, double... anglesInRadians) {
    throw new UnsupportedOperationException("Unsupported");
  }

  /**
   * True if this space is periodic (in the sense that moving off one border
   * makes you appear on the other one), otherwise false.
   *
   * @return true if this space is periodic, otherwise false.
   */
  public boolean isPeriodic() {
    return false;
  }

  /**
   * Destroys this Projected3DSpace by doing any necessary clean up.
   */
  public void destroy() {
    space.removeProjectionListener(this);
  }

  /**
   * Invoked when a projection event occurs. This forwards on
   * projection events fired by the 3D space to any listeners
   * of this space.
   *
   * @param evt the object describing the event
   */
  public void projectionEventOccurred(ProjectionEvent evt) {
    fireProjectionEvent(evt);
  }
  
	/**
	 * Gets the the euclidian distance between the NdPoints point1 and point2.
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
	 * Gets the square of the euclidian distance between the NdPoints point1 and point2.
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
		for (int i = 0; i < 2; i++) {
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
