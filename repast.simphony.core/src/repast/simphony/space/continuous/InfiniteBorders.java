package repast.simphony.space.continuous;

import repast.simphony.space.Dimensions;


/**
 * This represents an infinite space. There are no edges so you can always move to a point. <p/>
 * This is the same as a StickyBorders space with dimensions {@link Double#POSITIVE_INFINITY}.
 * 
 * @author Jerry Vos
 */
public class InfiniteBorders<T> extends StickyBorders {
	/**
	 * This calls the super's {@link StickyBorders#init(repast.simphony.space.Dimensions)} method with a
	 * {@link repast.simphony.space.Dimensions} object that has its dimensions set to {@link Double#POSITIVE_INFINITY}.
	 */
	public void init(Dimensions dimensions) {
		double[] infiniteDimensions = new double[dimensions.size()];
		double[] origin = new double[dimensions.size()];

		for (int i = 0; i < dimensions.size(); i++) {
			infiniteDimensions[i] = Double.POSITIVE_INFINITY;
			origin[i] = Double.POSITIVE_INFINITY / 2;
		}

		super.init(new Dimensions(infiniteDimensions, origin));
	}
}