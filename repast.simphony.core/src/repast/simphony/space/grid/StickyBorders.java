package repast.simphony.space.grid;

/**
 * Border implementation where translates across the border limits are clamped to
 * the border coordinates. So, for example, the space goes from 0 to 10 and the translate
 * results in 11 you end up at 10 (when traveling to 11 you get stuck to the wall). Transforms
 * throw a SpatialException if the desired target location is outside of the grid's
 * dimensions.
 * 
 * @author Jerry Vos
 */
public class StickyBorders extends AbstractGridPointTranslator {

	private transient int[][] spaceMinsMaxes;
	private static final int MIN = 0;
	private static final int MAX = 1;

	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	public void init(GridDimensions dimensions) {
		super.init(dimensions);
		spaceMinsMaxes = new int[2][dimensions.size()];
		int[] origin = dimensions.originToIntArray(null);

		// int halfWidth;
		for (int i = 0; i < dimensions.size(); i++) {
			// halfWidth = dimensions.getDimension(i) / 2.0;

			// spaceMinsMaxes[MIN][i] = -halfWidth;
			// spaceMinsMaxes[MAX][i] = +halfWidth;
			spaceMinsMaxes[MIN][i] = - origin[i];
			spaceMinsMaxes[MAX][i] = dimensions.getDimension(i) - origin[i] - 1;
		}
	}

	protected int getNewCoord(int dimension, int coord) {
		if (coord < spaceMinsMaxes[MIN][dimension]) {
			return spaceMinsMaxes[MIN][dimension];
		} else if (coord > spaceMinsMaxes[MAX][dimension]) {
			return spaceMinsMaxes[MAX][dimension];
		} else {
			return coord;
		}
	}

	/**
	 * Translates the specified location by the amount of displacement along each dimensions. The
	 * results are clamped to the grid's dimensions. The translated location is returned in
	 * the location array.
	 *
	 * @param location the current location
	 * @param displacement the amount of displacement.
	 */
	public void translate(int[] location, int... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			location[i] = getNewCoord(i, location[i] + displacement[i]);
		}
	}

	public boolean isToroidal() {
		return false;
	}
}