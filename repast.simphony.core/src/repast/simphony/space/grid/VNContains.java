package repast.simphony.space.grid;

import java.util.Arrays;




/**
 * Determines whether or not a particular target object is in the Von Neumann
 * neighborhood of a particular source. This works with 1D, 2D and 3D grids
 * only.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class VNContains<T> {

	private Grid<T> grid;
	private GridDimensions dims;
	private int[] tmpPoint;
	private int[] mins, maxs;


	/**
	 * Creates a VN contains for the specified grid.
	 *
	 * @param grid
	 */
	public VNContains(Grid<T> grid) {
		this.grid = grid;
		this.dims = grid.getDimensions();
		int size = dims.size();
		if (size > 3 || size < 1) throw new IllegalArgumentException("Von Neuman query is only " +
						"supported on 1D, 2D and 3D grids");
		tmpPoint = new int[size];
		mins = new int[size];
		maxs = new int[size];
	}

	/**
	 * Checks if the specified target is within the Von Neumann neighborhood of the
	 * specified source. The neighborhood is defined by the extent argument which
	 * contains the extent of the neighborhood in the relevant dimensions.
	 *
	 * @param source the source object of the neighborhood
	 * @param target the object to test if it is in the neighborhood
	 * @param extent defines the extents of the neighborhood.
	 * @return true if the target is in the Von Neumann neighborhood
	 * of the source.
	 */
	public boolean isNeighbor(T source, T target, int... extent) {
		if (extent == null || extent.length == 0) {
			extent = new int[dims.size()];
			for (int i=0; i<extent.length; i++)
				extent[i] = 1;
		}
		if (extent.length != dims.size()) throw new IllegalArgumentException("Number of extents must" +
						" match the number of grid dimensions");
		GridPoint point = grid.getLocation(source);
		int size = dims.size();
		int[] origin = dims.originToIntArray(null);

		for (int i = 0; i < size; i++) {
			int coord = point.getCoord(i);
			int max = coord + extent[i];
			int min = coord - extent[i];
			mins[i] = min;
			maxs[i] = max;
			if (!grid.isPeriodic()) {
				if (min < - origin[i]) min = - origin[i];
				int dimension = (int) dims.getDimension(i);
				if (max > dimension - origin[i] - 1) max = dimension - origin[i] - 1;
				mins[i] = min;
				maxs[i] = max;
			}
		}



		for (int i = 0; i < size; i++) {
			int min = mins[i];
			int max = maxs[i];
			point.toIntArray(tmpPoint);
			int pVal = point.getCoord(i);
			for (int j = min; j <= max; j++) {
				if (j != pVal) {
					tmpPoint[i] = j;
					Iterable<?> objs = grid.getObjectsAt(tmpPoint);
					for (Object obj : objs) {
						if (target.equals(obj)) return true;
					}
				}
			}
		}

		return false;
	}

	/*
	public Iterable<T> query(T source, int[] extent) {
		if (extent.length != dims.size()) throw new IllegalArgumentException("Number of extents must" +
						" match the number of grid dimensions");
		List<T> list = new ArrayList<T>();
		GridPoint point = grid.getLocation(source);
		int size = dims.size();

		for (int i = 0; i < size; i++) {
			int coord = point.getCoord(i);
			int max = coord + extent[i];
			int min = coord - extent[i];
			mins[i] = min;
			maxs[i] = max;
			if (!grid.isTorus()) {
				if (min < 0) min = 0;
				int dimension = (int) dims.getDimension(i);
				if (max > dimension - 1) max = dimension - 1;
				mins[i] = min;
				maxs[i] = max;
			}
		}

		for (int i = 0; i < size; i++) {
			int min = mins[i];
			int max = maxs[i];
			int pVal = point.getCoord(i);
			point.toIntArray(tmpPoint);

			for (int j = min; j <= max; j++) {
				if (j != pVal) {
					tmpPoint[i] = j;
					Iterable<T> objs = grid.getObjectsAt(tmpPoint);
					for (T obj : objs) {
						list.add(obj);
					}
				}
			}
		}

		return list;
	}
	*/
}
