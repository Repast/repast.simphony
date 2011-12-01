package repast.simphony.space.grid;

/**
 * Calculates a new location by translating a current one. If the new location is outside some
 * specified bounds then, the new location will be calculated by "bouncing" off the boundaries. This
 * assumes that any boundaries are either horizontal or vertical such that the "bounce" only
 * involves a simple sign change of the direction vector.
 *
 * @author Nick Collier
 */
public class BouncyBorders extends AbstractGridPointTranslator {

	private transient BounceDimension[] bounceDimensions;

	/**
	 * Creates a new BouncyBorders.
	 */
	public BouncyBorders() {}

	/**
	 * Creates a BounceTranslator whose boundaries are the specified dimensions. A boundary is
	 * created from each dimension having a min of 0 and a max of dimension - 1.
	 *
	 * @param dimensions
	 */
	public BouncyBorders(int... dimensions) {
		init(new GridDimensions(dimensions));
	}
	
	/**
	 * Creates a BounceTranslator whose boundaries are the specified dimensions. A boundary is
	 * created from each dimension having a min of 0 and a max of dimension - 1.
	 *
	 * @param dimensions
	 */
	public BouncyBorders(int[] dimensions, int[] origin) {
		init(new GridDimensions(dimensions, origin));
	}

	/**
	 * Translate the specified location by the specified displacement. The new location will stored
	 * in the location array. For example, if the location is (3, 4) and the displacement is (1,
	 * -2), the new location will be (4, 2).
	 * <p/>
	 * <p/>
	 * This will bounce off of any boundaries if necessary.
	 *
	 * @param location     the current location
	 * @param displacement the amount to translate
	 */
	public void translate(int[] location, int... displacement) {
		int i = 0;
		for (int val : displacement) {
			location[i] = bounceDimensions[i].translate(location[i], val);
			i++;
		}
	}

	public void init(GridDimensions dimensions) {
		super.init(dimensions);
		this.bounceDimensions = new BounceDimension[dimensions.size()];
		int[] origin = dimensions.originToIntArray(null);
		for (int i = 0; i < dimensions.size(); i++) {
			if (dimensions.getDimension(i) <= 0)
				throw new IllegalArgumentException("Dimensions of space must be greater than 0");
			
			
			this.bounceDimensions[i] = new BounceDimension(-origin[i], dimensions.getDimension(i) - origin[i] - 1);
		}
	}

	public boolean isToroidal() {
		return false;
	}

	// encapsulates a dimensions range and performs
	// the bounce
	private static class BounceDimension {

		private int min, max;

		public BounceDimension(int min, int max) {
			this.max = max;
			this.min = min;
		}

		public int translate(int loc, int amt) {
			int newLoc = loc + amt;
			while (newLoc < min || newLoc > max) {
				if (newLoc < min) {
					newLoc = min + (min - newLoc);
				} else {
					newLoc = max - (newLoc - max);
				}
			}
			return newLoc;
		}
	}
}
