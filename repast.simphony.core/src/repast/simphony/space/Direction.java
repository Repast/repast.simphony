package repast.simphony.space;

/**
 * Direction type constants.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface Direction {
	/**
	 * Directional constant for vector movement. On a 2D grid,
	 * this will move in a postive direction directly along the y-axis.
	 */
	double NORTH = Math.toRadians(90);
	/**
	 * Directional constant for vector movement. On a 2D grid,
	 * this will move in a negative direction directly along the y-axis.
	 */
	double SOUTH = Math.toRadians(270);
	/**
	 * Directional constant for vector movement. On a 2D grid,
	 * this will move in a postive direction directly along the x-axis.
	 */
	double EAST = Math.toRadians(0);
	/**
	 * Directional constant for vector movement. On a 2D grid,
	 * this will move in a negative direction directly along the x-axis.
	 */
	double WEST = Math.toRadians(180);
}
