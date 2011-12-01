package repast.simphony.space.grid;


/**
 * This represents an infinite space. There are no edges so you can always move to a point. <p/>
 * This is the same as a StickyBorders space with dimensions {@link Double#POSITIVE_INFINITY}.
 * 
 * @author Jerry Vos
 */
public class InfiniteBorders<T> extends StickyBorders {
	/**
	 * This calls the super's {@link StickyBorders#init(GridDimensions)} method with a
	 * {@link GridDimensions} object that has its dimensions set to {@link Double#POSITIVE_INFINITY}.
	 */
	@Override
	public void init(GridDimensions dimensions) {
		int[] infiniteDimensions = new int[dimensions.size()];
		int[] origin = new int[dimensions.size()];;
		for (int i = 0; i < dimensions.size(); i++) {
			infiniteDimensions[i] = Integer.MAX_VALUE;
			origin[i] = Integer.MAX_VALUE / 2;
		}
		super.init(new GridDimensions(infiniteDimensions, origin));
	}
}