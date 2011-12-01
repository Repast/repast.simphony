package repast.simphony.space.grid;

import java.util.Arrays;



/**
 * Determines whether or not a particular target is in the Moore
 * neighborhood of a particular source. This works with 1D, 2D and 3D grids
 * only.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class MooreContains<T> {

	private Grid<T> grid;
	private GridDimensions dims;
	private int[] mins, maxs;

	/**
	 * Creates a MooreContains for the specified grid.
	 *
	 * @param grid
	 */
	public MooreContains(Grid<T> grid) {
		this.grid = grid;
		this.dims = grid.getDimensions();
		int size = dims.size();
		if (size > 3 || size < 1) throw new IllegalArgumentException("Moore query is only " +
						"supported on 1D, 2D and 3D grids");
		mins = new int[size];
		maxs = new int[size];
	}

	/**
	 * Checks if the specified target is within the Moore neighborhood of the
	 * specified source. The neighborhood is defined by the extent argument which
	 * contains the extent of the neighborhood in the relevant dimensions. For
	 * example, assuming a 2D grid, a source at 4, 4, and and extent of 2, 1, the
	 * neighborhood would be a rectangle whose upper left corner was at 2, 5 with
	 * a width of 4 and a height of 2. If the extent is not specified it is assumed to
	 * be 1 in all dimensions.
	 *
	 * @param source the source object of the neighborhood
	 * @param target the object to test if it is in the neighborhood
	 * @param extent defines the extents of the neighborhood. If this is not specified then
	 * the extent is assumed to be 1 in all dimensions.
	 * @return true if the target is in the moore neighborhood of the source.
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
		int px = point.getX();


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

		if (size == 2) {
			int py = point.getY();
			int xMin = mins[0];
			int xMax = maxs[0];
			int yMin = mins[1];
			int yMax = maxs[1];
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					if (!(px == x && py == y)) {
						Iterable<?> objs = grid.getObjectsAt(x, y);
						for (Object obj : objs) {
							if (target.equals(obj)) return true;
						}
					}
				}
			}
		} else if (size == 3) {
			int py = point.getY();
			int pz = point.getZ();
			int xMin = mins[0];
			int xMax = maxs[0];
			int yMin = mins[1];
			int yMax = maxs[1];
			int zMin = mins[2];
			int zMax = maxs[2];
			for (int z = zMin; z <= zMax; z++) {
				for (int x = xMin; x <= xMax; x++) {
					for (int y = yMin; y <= yMax; y++) {
						if (!(px == x && py == y && pz == z)) {
							Iterable<?> objs = grid.getObjectsAt(x, y, z);
							for (Object obj : objs) {
								if (target.equals(obj)) return true;
							}
						}
					}
				}
			}
		} else {
			int xMin = mins[0];
			int xMax = maxs[0];
			
			for (int x = xMin; x <= xMax; x++) {
				if (!(px == x)) {
					Iterable<?> objs = grid.getObjectsAt(x);
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
		GridPoint point = grid.getLocation(source);
		List<T> list = new ArrayList<T>();
		int px = point.getX();
		int py = point.getY();
		int pz = point.getZ();

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

		if (size == 2) {
			int xMin = mins[0];
			int xMax = maxs[0];
			int yMin = mins[1];
			int yMax = maxs[1];
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					if (px != x && py != y) {
						Iterable<T> objs = grid.getObjectsAt(x, y);
						for (T obj : objs) {
							list.add(obj);
						}
					}
				}
			}
		} else {
			int xMin = mins[0];
			int xMax = maxs[0];
			int yMin = mins[1];
			int yMax = maxs[1];
			int zMin = mins[2];
			int zMax = maxs[2];
			for (int z = zMin; z <= zMax; z++) {
				for (int x = xMin; x <= xMax; x++) {
					for (int y = yMin; y <= yMax; y++) {
						if (px != x && py != y && pz != z) {
							Iterable<T> objs = grid.getObjectsAt(x, y, z);
							for (T obj : objs) {
								list.add(obj);
							}
						}
					}
				}
			}
		}

		return list;
	}
	*/
}
