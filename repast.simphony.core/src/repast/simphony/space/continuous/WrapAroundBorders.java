package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;


/**
 * Periodic type border implementation. Translates and transforms beyond the space's dimensions
 * are wrapped around the borders. Creating a 2D space with these types of border results in a torus.
 * 
 * @author Jerry Vos
 */
public class WrapAroundBorders extends AbstractPointTranslator {

	protected transient double[] spaceWidths;
	protected transient double[] origin;
	protected transient boolean newLocationWrapped;

	public boolean isNewLocationWrapped() {
		return newLocationWrapped;
	}

	public void setNewLocationWrapped(boolean newCoordinateWrapped) {
		this.newLocationWrapped = newCoordinateWrapped;
	}

	/**
	 * Initializes this with the given dimensions.
	 * 
	 * @param dimensions
	 *            the dimensions of the space
	 */
	public void init(Dimensions dimensions) {
		super.init(dimensions);
		spaceWidths = dimensions.toDoubleArray(null);
		origin = dimensions.originToDoubleArray(null);
		newLocationWrapped = false;
	}

	/**
	 * Translates the specified location by the specified displacement
	 * according to periodic "wrapped" semantics. Locations outside the grid
	 * will be wrapped into the spaces's dimensions. The results will be
	 * placed into the location array.
	 *
	 * @param location the current location
	 * @param displacement the amount of displacement
	 */
	public void translate(double[] location, double... displacement) {
		setNewLocationWrapped(false);
		for (int i = 0; i < displacement.length; i++) {
			location[i] = getNewCoord(i, location[i] + displacement[i]);
		}
	}

	/**
	 * Translates the specified location by the specified displacement
	 * according to periodic "wrapped" semantics. Locations outside the grid
	 * will be wrapped into the spaces's dimensions. The results will be
	 * placed into the newLocation array.
	 *
	 * @param location the current location
	 *  @param newLocation holds the newLocation after the method has completed
	 * @param displacement the amount of displacement
	 */
	public void translate(NdPoint location, double[] newLocation, double... displacement) {
		setNewLocationWrapped(false);
		for (int i = 0; i < displacement.length; i++) {
			newLocation[i] = getNewCoord(i, location.point[i] + displacement[i]);
		}
	}

	/**
	 * Transform the given targetLocation according to periodic "wrapped" semantics.
	 * The new transformed coordinates will be placed in the transformedLocation.
	 *
	 * @param  transformedLocation coordinates holds the current location and the new coordinates
	 * once they have been transformed by this PointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid.
	 */
	public void transform(double[] transformedLocation, double... targetLocation) {
		setNewLocationWrapped(false);
		for (int i = 0; i < targetLocation.length; i++) {
			transformedLocation[i] = getNewCoord(i, targetLocation[i]);
		}
	}

	/**
	 * Transform the given targetLocation according to periodic "wrapped" semantics.
	 * The new transformed coordinates will be placed in the transformedLocation.
	 *
	 * @param transformedLocation holds the original coordinates and the new coordinates once they have been
	 * transformed by this PointTranslator
	 * @param targetLocation the new target location whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid.
	 */
	public void transform(NdPoint transformedLocation, double... targetLocation) {
		transform(transformedLocation.point, targetLocation);
	}

	protected double getNewCoord(int dimension, double location) {
		double shiftedLocation = location + origin[dimension];
		// % is remainder, not true modulus, so -8 % 7 gives -1
		double value = shiftedLocation % spaceWidths[dimension];
		if (value < 0) value = spaceWidths[dimension] + value;
		if (value != shiftedLocation) setNewLocationWrapped(true);

		return (value - origin[dimension]);
	}

	public boolean isPeriodic() {
		return true;
	}
}