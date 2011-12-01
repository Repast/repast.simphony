package repast.simphony.query.space.grid;

import repast.simphony.query.Query;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for grid queries.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractGridQuery<T> implements Query<T> {

	protected List<T> emptyList = new ArrayList<T>(0);

	protected Grid<T> grid;
	protected GridPoint point;
	protected int[] extent;
	protected GridDimensions dims;
	protected int[] mins, maxs;

	/**
	 *
	 * @param grid
	 * @param source
	 * @param extent
	 */
	public AbstractGridQuery(Grid<T> grid, T source, int... extent) {
		this.grid = grid;
		this.dims = grid.getDimensions();
		int size = dims.size();
		if (size > 3 || size < 1) throw new IllegalArgumentException("Query is only " +
						"supported on 1D, 2D and 3D grids");
		mins = new int[size];
		maxs = new int[size];
		point = grid.getLocation(source);
		setExtent(size, extent);
	}

	private void setExtent(int size, int... extent) {
		if (extent == null || extent.length == 0) {
			extent = new int[size];

			for (int i=0; i<size; i++)
				extent[i] = 1;
		}
		if (extent.length != dims.size()) throw new IllegalArgumentException("Number of extents must" +
						" match the number of grid dimensions");
		this.extent = extent;
	}

	/**
	 * Resets the query to use the specified source and extent.
	 *
	 * @param source the source or "center" point of the neighborhood.
	 * @param extent defines the dimensions of the neighborhood in the x, y, and
	 * optionaly z dimensions.  If the extent args are missing, they default to
	 * one
	 */
	public void reset(T source, int... extent) {
		setExtent(dims.size(), extent);
		point = grid.getLocation(source);
	}
}
