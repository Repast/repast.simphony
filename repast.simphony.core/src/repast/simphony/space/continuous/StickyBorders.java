package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;


/**
 * Border implementation where translates across the border limits are clamped to
 * the border coordinates. So, for example, the space goes from 0 to 10 and the translate
 * results in 11 you end up at 10 (when traveling to 11 you get stuck to the wall). Transforms
 * throw a SpatialException if the desired target location is outside of the space's
 * dimensions.
 * 
 * @author Jerry Vos
 */
public class StickyBorders extends AbstractPointTranslator {

	private transient double[][] spaceMinsMaxes;

	private static final int MIN = 0;

	private static final int MAX = 1;

	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	public void init(Dimensions dimensions) {
		super.init(dimensions);
		spaceMinsMaxes = new double[2][dimensions.size()];
		double[] origin = dimensions.originToDoubleArray(null);

		// double halfWidth;
		for (int i = 0; i < dimensions.size(); i++) {
			// halfWidth = dimensions.getDimension(i) / 2.0;

			// spaceMinsMaxes[MIN][i] = -halfWidth;
			// spaceMinsMaxes[MAX][i] = +halfWidth;
			double min = -origin[i];
			if (-min == 0.0 ) min = 0.0;
			spaceMinsMaxes[MIN][i] = min;
			spaceMinsMaxes[MAX][i] = dimensions.getDimension(i) - origin[i];
		}
	}
	


	protected double getNewCoord(int dimension, double coord) {
		if (coord < spaceMinsMaxes[MIN][dimension]) {
			return spaceMinsMaxes[MIN][dimension];
		} else if (coord >= spaceMinsMaxes[MAX][dimension]) {
			return minusEpsilon(spaceMinsMaxes[MAX][dimension]);
		} else {
			return coord;
		}
	}

	/**
	 * Translates the specified location by the amount of displacement along each dimensions. The
	 * results are clamped to the spaces's dimensions. The translated location is returned in
	 * the location array.
	 *
	 * @param location the current location
	 * @param displacement the amount of displacement.
	 */
	public void translate(double[] location, double... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			location[i] = getNewCoord(i, location[i] + displacement[i]);
		}
	}

	/**
	 * Translates the specified location by the amount of displacement along each dimensions. The
	 * results are clamped to the grid's dimensions. The translated location is returned in
	 * the newlocation array.
	 *
	 * @param location the current location
	 * @param newLocation holds the new location after the method completes
	 * @param displacement the amount of displacement.
	 */
	public void translate(NdPoint location, double[] newLocation, double... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			newLocation[i] = getNewCoord(i, location.point[i] + displacement[i]);
		}
	}


	public boolean isPeriodic() {
		return false;
	}
}