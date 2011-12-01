package repast.simphony.space.grid;

import repast.simphony.space.SpatialException;

/**
 * Periodic type border implementation. Translates and transforms beyond the grid's dimensions
 * are wrapped around the borders. Creating a 2D grid with these types of border results in a torus.
 * 
 * @author Jerry Vos
 */
public class WrapAroundBorders implements GridPointTranslator {

	protected transient GridDimensions spaceDimensions;
	protected transient int[] spaceWidths;
	protected transient int[] origin;

	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	public void init(GridDimensions dimensions) {
		this.spaceDimensions = dimensions;
		spaceWidths = dimensions.toIntArray(null);
		origin = dimensions.originToIntArray(null);
	}

	/**
	 * Translates the specified location by the specified displacement
	 * according to periodic "wrapped" semantics. Locations outside the grid
	 * will be wrapped into the grid's dimensions. The results will be
	 * placed into the location array.
	 *
	 * @param location the current location
	 * @param displacement the amount of displacement
	 */
	public void translate(int[] location, int... displacement) {
		for (int i = 0; i < displacement.length; i++) {
			location[i] = getNewCoord(i, location[i] + displacement[i]);
		}
	}

	/**
	 * Transform the given targetLocation according to periodic "wrapped" semantics.
	 * The new transformed coordinates will be placed in the transformedLocation.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws SpatialException if the transform is invalid.
	 */
	public void transform(int[] transformedLocation, int... targetLocation) throws SpatialException {
		for (int i = 0; i < targetLocation.length; i++) {
			transformedLocation[i] = getNewCoord(i, targetLocation[i]);
		}
	}

	/**
	 * Transform the given targetLocation according to periodic "wrapped" semantics.
	 * The new transformed coordinates will be placed in the transformedLocation.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws SpatialException if the transform is invalid.
	 */
	public void transform(GridPoint transformedLocation, int... targetLocation) throws SpatialException {
		transform(transformedLocation.point, targetLocation);
	}

	protected int getNewCoord(int dimension, int location) {
		int shiftedLocation = location + origin[dimension];
		// % is remainder, not true modulus, so -8 % 7 gives -1
		int value = shiftedLocation % spaceWidths[dimension];
		if (value < 0) value = spaceWidths[dimension] + value;

		return (value - origin[dimension]);
	}

	public boolean isToroidal() {
		return true;
	}
}