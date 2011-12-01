package repast.simphony.space.grid;

import repast.simphony.space.SpatialException;

/**
 * Interface for classes that will translate a point by some specified amount.
 * 
 * @author Nick Collier
 */
public interface GridPointTranslator {
	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	void init(GridDimensions dimensions);

	/**
	 * Translate the specified location by the specified displacement. The new location will stored
	 * in the location array. For example, if the location is (3, 4) and the displacement is (1,
	 * -2), the new location will be (4, 2).
	 * 
	 * @param location
	 *            the current location
	 * @param displacement
	 *            the amount to translate
	 */
	void translate(int[] location, int... displacement);

	/**
	 * Transform the given targetLocation according to this GridPointTranslator's semantics.
	 * The new transformed coordinates will be placed in the transformedTargetLocation.
	 * 
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws SpatialException if the transform is invalid. For example, if the new location
	 * is outside of the grid.
	 */
	void transform(int[] transformedLocation, int... targetLocation) throws SpatialException;

	/**
	 * Transform the given targetLocation according to this GridPointTranslator's semantics.
	 * The new transformed coordinates will be placed in the transformedTargetLocation.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws SpatialException if the transform is invalid. For example, if the new location
	 * is outside of the grid.
	 */
	void transform(GridPoint transformedLocation, int... targetLocation) throws SpatialException;

	/**
	 * True if this grid is toroidal (in the sense that moving off one border makes you appear on
	 * the other one), otherwise false.
	 *
	 * @return true if this grid is toroidal, otherwise false.
	 */
	boolean isToroidal();
}
