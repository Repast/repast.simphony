package repast.simphony.query.space.grid;

import javolution.util.FastSet;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.collections.Contains;
import repast.simphony.util.collections.FilteredIterator;
import repast.simphony.util.collections.IterableAdaptor;
import repast.simphony.util.collections.IteratorOverIterables;

import java.util.Set;


/**
 *
 * Queries a grid for the Von Neumann neighborhood of a specific point or object.
 *
 * @author Nick Collier
 */
public class VNQuery<T> extends AbstractGridQuery<T> {

	private class VNIterator extends IteratorOverIterables<T> {

		int dimIndex = 0;
		int valIndex;
		int max;
		int dimMax;
		int[] tmpPoint;
		int centerVal;
		GridPoint center;

		public VNIterator(GridPoint center) {
			this.center = center;
			int size = dims.size();
			dimMax = size - 1;
			valIndex = mins[dimIndex];
			max = maxs[dimIndex];
			centerVal = center.getCoord(dimIndex);
			tmpPoint = new int[size];
			center.toIntArray(tmpPoint);
			init();
		}

		protected Iterable<T> getNext() {
			if (valIndex == centerVal) {
				valIndex++;
			}

			while (valIndex > max) {
				dimIndex++;
				if (dimIndex > dimMax) return null;
				valIndex = mins[dimIndex];
				max = maxs[dimIndex];
				center.toIntArray(tmpPoint);
				centerVal = center.getCoord(dimIndex);
				if (valIndex == centerVal) valIndex++;
			}

			tmpPoint[dimIndex] = valIndex++;
			return grid.getObjectsAt(tmpPoint);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Creates a Von Neumann query on the specified grid using the specified object
	 * as the source where the neighborhood is specified by the extent. The
	 * extent defines the dimensions of the neighborhood in the x, y, and
	 * optionaly z dimensions.  If the extent args are missing, they default to
	 * one.<p>
	 *
	 * This object can be reused by resetting the source and extents using the <code>
	 * reset</code> method. The queries will return an empty iterable if the
	 * source object does not have a grid location.
	 *
	 * @param grid
	 * @param source
	 * @param extent
	 */
	public VNQuery(Grid<T> grid, T source, int... extent) {
		super(grid, source, extent);
	}

	private void setupMinMax(int size) {
		for (int i = 0; i < size; i++) {
			double coord = point.getCoord(i);
			double max = coord + extent[i];
			double min = coord - extent[i];
			mins[i] = (int)min;
			maxs[i] = (int)max;
			if (!grid.isPeriodic()) {
				int origin = (int) dims.getOrigin(i);
				if (min < -origin) min = -origin;
				int dimension = (int) dims.getDimension(i);
				if (max > dimension - origin - 1) max = dimension - origin - 1;
				mins[i] = (int)min;
				maxs[i] = (int)max;
			}
		}
	}

	/**
	 * Gets an iterable over all the objects that make up the Von Neumann neighborhood of
	 * the source object. The source object and neighborhood extent are specified in the
	 * constructor or the reset method. The order of the returned objects is left to right,
	 * up to down, and then optionally back to front.. For example, a 2d neighborhood
	 * centered at 2,2 with an extent of 1,1 will return the objects at (1,2), (3,2), (2, 1),
	 * (2,3).<p>
	 *
	 * This will return an empty iterable if the  source object does not have a grid
	 * location.
	 *
	 * @return an iterable over all the objects that make up the Moore neighborhood of
	 * the source object.
	 */
	public Iterable<T> query() {
		if (point == null) return emptyList;
		int size = dims.size();
		setupMinMax(size);
		return new IterableAdaptor<T>(new VNIterator(point));
	}

	/**
	 * Gets an iterable over all the objects that make up the Von Neumann neighborhood of
	 * the source object and are in the specified iterable. The source object and neighborhood
	 * extent are specified in the constructor or the reset method. The order of the returned objects
	 * is left to right, up to down, and then optionally back to front. For example, a 2d neighborhood
	 * centered at 2,2 with an  extent of 1,1 will return the objects at (1,2), (3,2), (2, 1), (2,3).<p>
	 *
	 * This will return an empty iterable if the  source object does not have a grid
	 * location.
	 *
	 * @return an iterable over all the objects that make up the Moore neighborhood of
	 * the source object and are in the spefied iterable.
	 */
	public Iterable<T> query(Iterable<T> iter) {
		if (point == null) return emptyList;
		Set<T> set = new FastSet<T>();
		for (T item : iter) {
			set.add(item);
		}
		int size = dims.size();
		setupMinMax(size);
		return new FilteredIterator<T>(new VNIterator(point), new Contains<T>(set));
	}
}
