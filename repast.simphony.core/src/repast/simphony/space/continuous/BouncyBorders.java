package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;


/**
 * Calculates a new location by translating a current one. If the new location is outside some
 * specified bounds then, the new location will be calculated by "bouncing" off the boundaries. This
 * assumes that any boundaries are either horizontal or vertical such that the "bounce" only
 * involves a simple sign change of the direction vector.
 * 
 * @author Nick Collier
 */
public class BouncyBorders extends AbstractPointTranslator {

	private transient Dimension[] dimensions;

	/**
	 * Constructs a BouncyBorders translator that must have its {@link #init(repast.simphony.space.Dimensions)} method
	 * called before it is used. This is the standard constructor.
	 */
	public BouncyBorders() {

	}

	/**
	 * Creates a BounceTranslator whose boundaries are the specified dimensions. A boundary is
	 * created from each dimension having a min of 0 and a max of dimension - 1.
	 * 
	 * @param dimensions
	 */
	public BouncyBorders(double... dimensions) {
		init(new Dimensions(dimensions));
	}
	
	/**
	 * Creates a BounceTranslator whose boundaries are the specified dimensions. A boundary is
	 * created from each dimension having a min of 0 and a max of dimension - 1.
	 * 
	 * @param dimensions
	 */
	public BouncyBorders(double[] dimensions, double[] origin) {
		init(new Dimensions(dimensions, origin));
	}

	/**
	 * Translate the specified location by the specified displacement.For example, if the location
	 * is (3, 4) and the displacement is (1, -2), the new location will be (4, 2). The new location
	 * will be copied into the newLocation array.
	 * <p>
	 * <p/> This will bounce off of any boundaries if necessary.
	 * 
	 * @param location
	 *            the current location
	 * @param newLocation
	 *            this will hold the newLocation after the method has completed
	 * @param displacement
	 *            the amount to translate
	 */
	public void translate(NdPoint location, double[] newLocation, double... displacement) {
		double[] point = location.point;
		int i = 0;
		for (double val : displacement) {
			newLocation[i] = dimensions[i].translate(point[i], val);
			i++;
		}
	}

	/**
	 * Translate the specified location by the specified displacement. The new location will stored
	 * in the location array. For example, if the location is (3, 4) and the displacement is (1,
	 * -2), the new location will be (4, 2).
	 * <p>
	 * 
	 * This will bounce off of any boundaries if necessary.
	 * 
	 * @param location
	 *            the current location
	 * @param displacement
	 *            the amount to translate
	 */
	public void translate(double[] location, double... displacement) {
		int i = 0;
		for (double val : displacement) {
			location[i] = dimensions[i].translate(location[i], val);
			i++;
		}
	}

	public void init(Dimensions dimensions) {
		super.init(dimensions);
		this.dimensions = new Dimension[dimensions.size()];
		double[] origin = dimensions.originToDoubleArray(null);
		for (int i = 0; i < dimensions.size(); i++) {
			if (dimensions.getDimension(i) <= 0)
				throw new IllegalArgumentException("Dimensions of space must be greater than 0");
			double min = -origin[i];
			if (-min == 0.0 ) min = 0.0;
			this.dimensions[i] = new Dimension(min, dimensions.getDimension(i) - origin[i]);
		}
	}

	public boolean isPeriodic() {
		return false;
	}

	// encapsulates a dimensions range and performs
	// the bounce
	private static class Dimension {

		private double min, max;

		public Dimension(double min, double max) {
			this.max = max;
			this.min = min;
		}

		public double translate(double loc, double amt) {
			double newLoc = loc + amt;
			while (newLoc < min || newLoc >= max) {
				if (newLoc < min) {
					newLoc = min + (min - newLoc);
				} else {
					if (newLoc > max){
						newLoc = max - (newLoc - max);
					}
					else {
						newLoc = minusEpsilon(newLoc);
					}
				}
			}
			return newLoc;
		}
	}
}
