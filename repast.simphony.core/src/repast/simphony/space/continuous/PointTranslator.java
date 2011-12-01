package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;


/**
 * Interface for classes that will translate a point by some specified amount.
 * 
 * @author Nick Collier
 */
public interface PointTranslator {
	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	void init(Dimensions dimensions);

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
	void translate(double[] location, double... displacement);

	/**
	 * Translate the specified location by the specified displacement.For example, if the location
	 * is (3, 4) and the displacement is (1, -2), the new location will be (4, 2). The new location
	 * will be copied into the newLocation array.
	 * 
	 * @param location
	 *            the current location
	 * @param newLocation
	 *            this will hold the newLocation after the method has completed
	 * @param displacement
	 *            the amount to translate
	 */
	void translate(NdPoint location, double[] newLocation, double... displacement);

	/**
	 * Transform the given targetLocation according to this PointTranslator's semantics.
	 * The new transformed coordinates will be placed in the transformedTargetLocation.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid. For example, if the new location
	 * is outside of the grid.
	 */
	void transform(double[] transformedLocation, double... targetLocation);

	/**
	 * Transform the given targetLocation according to this PointTranslator's semantics.
	 * The new transformed coordinates will be placed in the transformedTargetLocation.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 * GridPointTranslator
	 * @param targetLocation the new target transformedLocation whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid. For example, if the new transformedLocation
	 * is outside of the grid.
	 */
	void transform(NdPoint transformedLocation, double... targetLocation);

	/**
	 * True if this translator is periodic (in the sense that moving off one border makes you appear on
	 * the other one), otherwise false.
	 *
	 * @return true if this translator is periodic, otherwise false.
	 */
	boolean isPeriodic();
}
