package repast.simphony.query.space.projection;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.TransformException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ShortestPath;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import simphony.util.messages.MessageCenter;

/**
 * Predicate that evalutes whether one object is within some distance of another in a projection.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Within extends SpatialPredicate {

  private static MessageCenter msg = MessageCenter.getMessageCenter(Within.class);

  private Object obj1, obj2;
	private double distance, distanceSq;

	/**
	 * Creates a Within predicate that will evaluate whether or not the first object is
	 * within (<=) a specified path length of the other.
	 *
	 * @param obj1
	 * @param obj2
	 */
	public Within(Object obj1, Object obj2, double distance) {
		this.obj1 = obj1;
		this.obj2 = obj2;
		this.distance = distance;
		this.distanceSq = distance * distance;
	}

	/**
	 *
	 * @param network the network to evaluate against.
	 * @return true if the path length from the first object to the second in the specified
	 * Network is less than or equal to the specified value.
	 */
	public boolean evaluate(Network network) {
		ShortestPath path = new ShortestPath(network);
		return path.getPathLength(obj1,obj2) <= distance;
	}

	/**
	 * Evaluates against the specified space testing if the
	 * the two objects are within some distance of each other in
	 * that space.
	 *
	 * @param space the space to evaluate against
	 * @return true if the two are objects are within the specified
	 * distance of each other.
	 */
	public boolean evaluate(ContinuousSpace space) {
		NdPoint p1 = space.getLocation(obj1);
		NdPoint p2 = space.getLocation(obj2);

		if (p1 != null && p2 != null) {
			return space.getDistanceSq(p1,p2) <= distanceSq;
		}

		return false;
	}

		/**
	 * Evaluates against the specified space testing if the
	 * the two objects are within some distance of each other in
	 * that space.
	 *
	 * @param grid the grid to evaluate against
	 * @return true if the two are objects are within the specified
	 * distance of each other.
	 */
	public boolean evaluate(Grid grid) {
		GridPoint p1 = grid.getLocation(obj1);
		GridPoint p2 = grid.getLocation(obj2);

		if (p1 != null && p2 != null) {
			return grid.getDistanceSq(p1,p2) <= distanceSq;
		}

		return false;
	}

  /**
   * Evaluates the Geography against this predicate comparing
   * whether two objects are within a specified distance of each other.
   * The distance is orthodromic and in meters. Note that
   * for Polygons the distance is measured from the center
   * and not from the nearest point.
   *
   * @param geography the geography to evaluate against.
   * @return true if this predicate is true for the specified projection otherwise false.
   */
  @Override
  public boolean evaluate(Geography geography) {
    Geometry geom1 = geography.getGeometry(obj1);
    Geometry geom2 = geography.getGeometry(obj2);
    try {
    return JTS.orthodromicDistance(geom1.getCentroid().getCoordinate(),
            geom2.getCentroid().getCoordinate(), geography.getCRS()) <= distance;
    } catch (TransformException e) {
      msg.error("Error calculating orthodromic distance", e);
    }

    return false;
  }
}
