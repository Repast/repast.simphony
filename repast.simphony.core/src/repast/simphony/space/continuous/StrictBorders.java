package repast.simphony.space.continuous;

/**
 * Strict border implementation. Any translate or transform outside
 * of the space's dimensions will throw a SpatialException.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class StrictBorders extends AbstractPointTranslator {


	/**
	 * True if this translator is periodic (in the sense that moving off one border makes you appear on
	 * the other one), otherwise false.
	 *
	 * @return true if this translator is periodic, otherwise false.
	 */
	public boolean isPeriodic() {
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
	 */
	public void translate(double[] location, double... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			double val = location[i] + displacement[i];
			boundsCheck(i, val);
			location[i] = val;
		}
	}

	/**
	 * Translate the specified location by the specified displacement.For example, if the location
	 * is (3, 4) and the displacement is (1, -2), the new location will be (4, 2). The new location
	 * will be copied into the newLocation array. If the translated location is outside of the grid dimensions,
	 * then a SpatialException is thrown.
	 *
	 * @param location     the current location
	 * @param newLocation  this will hold the newLocation after the method has completed
	 * @param displacement the amount to translate
	 */
	public void translate(NdPoint location, double[] newLocation, double... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			double val = location.point[i] + displacement[i];
			boundsCheck(i, val);
			newLocation[i] = val;
		}
	}


}
