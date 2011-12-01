package repast.simphony.space.grid;

import repast.simphony.space.SpatialException;

/**
 * Strict border implementation. Any translate or transform outside
 * of the grids dimensions will throw a SpatialException.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class StrictBorders extends AbstractGridPointTranslator {

	/**
	 * True if this grid is toroidal (in the sense that moving off one border makes you appear on
	 * the other one), otherwise false. This always returns false.
	 *
	 * @return always false.
	 */
	public boolean isToroidal() {
		return false;
	}

	/**
	 * Translate the specified location by the specified displacement. The new location will stored
	 * in the location array. For example, if the location is (3, 4) and the displacement is (1,
	 * -2), the new location will be (4, 2). If the translated location is outside of the grid dimensions,
	 * then a SpatialException is thrown.
	 *
	 * @param location     the current location
	 * @param displacement the amount to translate
	 * @throws SpatialException if the new location as calculated from current location and displacement
	 * is outside of the grid's dimensions.
	 */
	public void translate(int[] location, int... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			int val = location[i] + displacement[i];
			boundsCheck(i, val);
			location[i] = val;
		}
	}
}
