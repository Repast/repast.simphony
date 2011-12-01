package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;
import repast.simphony.space.SpatialException;

/**
 * Implements transform according to default continuous space moveTo semantics. Namely, transform will throw
 * a SpatialException if the target location is off the grid.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractPointTranslator implements PointTranslator {

	protected transient Dimensions dimensions;

	/**
	 * Initializes this with the given dimensions.
	 *
	 * @param dimensions the dimensions of the space
	 */
	public void init(Dimensions dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * Transform the given targetLocation according to default moveTo semantics. Namely,
	 * if the target location is within the grid dimensions then that location becomes
	 * the new transformedLocation. Otherwise, this will throw a SpatialException.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 *                            GridPointTranslator
	 * @param targetLocation      the new target location whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid. For example, if the new location
	 *                                       is outside of the grid.
	 */
	public void transform(double[] transformedLocation, double... targetLocation) throws SpatialException {
		for (int i = 0; i < targetLocation.length; i++) {
			boundsCheck(i, targetLocation[i]);
		}
	
		for (int i=0; i< targetLocation.length; i++)
			transformedLocation[i] = targetLocation[i];
	}

	protected void boundsCheck(int i, double value) {
		double[] origin = dimensions.originToDoubleArray(null);
		if (value < -origin[i] || value >= dimensions.getDimension(i) - origin[i]) {
			throw new SpatialException("Target location " + value +
							" is outside of grid's dimensions.");
		}
	}

	/**
	 * Transform the given targetLocation according to default moveTo semantics. Namely,
	 * if the target location is within the grid dimensions then that location becomes
	 * the new transformedLocation. Otherwise, this will throw a SpatialException.
	 *
	 * @param transformedLocation the coordinates once they have been transformed by this
	 *                            GridPointTranslator
	 * @param targetLocation      the new target location whose coordinates will be transformed
	 * @throws repast.simphony.space.SpatialException if the transform is invalid. For example, if the new location
	 *                                       is outside of the grid.
	 */
	public void transform(NdPoint transformedLocation, double... targetLocation) throws SpatialException {
		transform(transformedLocation.point, targetLocation);
	}
	
	static protected double minusEpsilon(double amount){
		double result = 0;
		double b = 10;
		int i = 1;
		while ( (amount - Math.pow(amount, - (double) i ) != amount) ){
			i++;
		}
		return (amount - Math.pow(amount, - (double) (i-1) ));
	}
}
