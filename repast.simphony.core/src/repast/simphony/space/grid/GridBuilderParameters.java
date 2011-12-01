package repast.simphony.space.grid;

/**
 * Parameters for creating grids. This class includes static convenience methods for
 * creating some common types of grids.
 *
 * @author Nick Collier
 *
 * @see GridAdder
 * @see GridPointTranslator
 */
public class GridBuilderParameters<T> {

	protected GridAdder<T> adder;
	protected GridPointTranslator trans;
	protected int[] dims;
	protected int[] origin;
	protected boolean isMulti;

	/**
	 * Creates parameters with the specified border rule, adder, multi occupancy and dimensions.
	 *
	 * @param borderRule the rule used to determine border behavior
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param multi whether or not each cell in the grid is multi-occupancy
	 * @param dims the dimensions of the grid
	 *
	 * @see GridAdder
   * @see GridPointTranslator
	 */
	public GridBuilderParameters(GridPointTranslator borderRule, GridAdder<T> adder, boolean multi,
	                             int... dims) {
		this.adder = adder;
		this.dims = dims;
		this.origin = new int[dims.length];
		isMulti = multi;
		this.trans = borderRule;
	}
	
	/**
	 * Creates parameters with the specified border rule, adder, multi occupancy, dimensions and origin.
	 *
	 * @param borderRule the rule used to determine border behavior
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param multi whether or not each cell in the grid is multi-occupancy
	 * @param dims the dimensions of the grid
	 * @param origin the origin of the grid
	 *
	 * @see GridAdder
   * @see GridPointTranslator
	 */
	public GridBuilderParameters(GridPointTranslator borderRule, GridAdder<T> adder, boolean multi,
	                             int[] dims, int[] origin) {
		this.adder = adder;
		this.dims = dims;
		this.origin = origin;
		isMulti = multi;
		this.trans = borderRule;
	}

	/**
	 * Gets the grid point translator used to calculate points when moving by displacement. This
	 * can be used to set the translator so that the new point is the result of a bounce off a boundary.
	 *
	 * @return the
	 */
	public GridPointTranslator getGridPointTranslator() {
		return trans;
	}

	/**
	 * Gets the adder used to add objects entered into the context automatically to the grid.
	 *
	 * @return the adder used to add objects entered into the context automatically to the grid.
	 */
	public GridAdder<T> getAdder() {
		return adder;
	}

	/**
	 * Gets the dimensions of the grid.
	 *
	 * @return the dimensions of the grid.
	 */
	public int[] getDimensions() {
		return dims;
	}
	
	/**
	 * Gets the origin of the grid.
	 *
	 * @return the origin of the grid.
	 */
	public int[] getOrigin() {
		return origin;
	}

	/**
	 * @return true if each grid cell can hold more than one object
	 */
	public boolean isMultOccupancy() {
		return isMulti;
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy 1 dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param size the size of the single dimension
	 * @return GridBuilderParameters suitable for creating a single occupancy 1 dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancy1D(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int size) {
		return new GridBuilderParameters<T>(borderRule, adder, false, size);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy 1 dimensional periodic
	 * wrapped grid. That is, the ends of the grid are joined.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param size the size of the single dimension
	 * @return GridBuilderParameters suitable for creating a single occupancy 1 dimensional periodic
	 * wrapped grid.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancy1DTorus(GridAdder<T> adder, int size) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, false, size);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy 2 dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param xSize the size of the x dimension
	 * @param ySize the size of the y dimension
	 * @return GridBuilderParameters suitable for creating a single occupancy 2 dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancy2D(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int xSize, int ySize) {
		return new GridBuilderParameters<T>(borderRule, adder, false, xSize, ySize);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy 2 dimensional torus.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param xSize the size of the x dimension
	 * @param ySize the size of the y dimension
	 * @return GridBuilderParameters suitable for creating a single occupancy 2 dimensional torus.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancy2DTorus(GridAdder<T> adder, int xSize, int ySize) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, false, xSize, ySize);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy n-dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param dimensions the dimensions of the grid
	 * @return GridBuilderParameters suitable for creating a single occupancy n-dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancyND(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int... dimensions) {
		return new GridBuilderParameters<T>(borderRule, adder, false, dimensions);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a single occupancy n-dimensional torus.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param dimensions the dimensions of the grid
	 * @return GridBuilderParameters suitable for creating a single occupancy n-dimensional torus.
	 */
	public static <T> GridBuilderParameters<T> singleOccupancyNDTorus(GridAdder<T> adder, int... dimensions) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, false, dimensions);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi-occupancy 1 dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param size the size of the single dimension
	 * @return GridBuilderParameters suitable for creating a single occupancy 1 dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancy1D(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int size) {
		return new GridBuilderParameters<T>(borderRule, adder, true, size);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi occupancy 1 dimensional periodic
	 * wrapped grid. That is, the ends of the grid are joined.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param size the size of the single dimension
	 * @return GridBuilderParameters suitable for creating a multi occupancy 1 dimensional periodic
	 * wrapped grid.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancy1DTorus(GridAdder<T> adder, int size) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, true, size);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi occupancy 2 dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param xSize the size of the x dimension
	 * @param ySize the size of the y dimension
	 * @return GridBuilderParameters suitable for creating a multi occupancy 2 dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancy2D(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int xSize, int ySize) {
		return new GridBuilderParameters<T>(borderRule, adder, true, xSize, ySize);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi occupancy 2 dimensional torus.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param xSize the size of the x dimension
	 * @param ySize the size of the y dimension
	 * @return GridBuilderParameters suitable for creating a multi occupancy 2 dimensional torus.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancy2DTorus(GridAdder<T> adder, int xSize, int ySize) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, true, xSize, ySize);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi occupancy n-dimensional grid.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param borderRule the rule used to determine border behavior
	 * @param dimensions the dimensions of the grid
	 * @return GridBuilderParameters suitable for creating a multi occupancy n-dimensional grid.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancyND(GridAdder<T> adder,
	                                                          GridPointTranslator borderRule, int... dimensions) {
		return new GridBuilderParameters<T>(borderRule, adder, true, dimensions);
	}

	/**
	 * Returns GridBuilderParameters suitable for creating a multi occupancy n-dimensional torus.
	 *
	 * @param adder the adder used to add objects to the grid when those objects are added to
	 * the context for which the grid is a projection
	 * @param dimensions the dimensions of the grid
	 * @return GridBuilderParameters suitable for creating a multi occupancy n-dimensional torus.
	 */
	public static <T> GridBuilderParameters<T> multiOccupancyNDTorus(GridAdder<T> adder, int... dimensions) {
		return new GridBuilderParameters<T>(new WrapAroundBorders(), adder, true, dimensions);
	}
}
