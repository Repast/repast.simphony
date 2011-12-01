/*CopyrightHere*/
package repast.simphony.space.grid;

import repast.simphony.space.continuous.MultiOccupancyCoordinateAccessor;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Default implementation of an n-dimensional continuous space.
 */
public class FastDenseSingleOccuGrid<T> extends AbstractGrid<T, Object[]> {

	/**
	 * An acessor for this grid class.
	 */
	public static final class FastDenseAccessor<V> implements
          CellAccessor<V, Object[]> {

		public V get(Object[] locationStorage, GridPoint location) {
			int dims = location.dimensionCount();
			for (int i = 0; i < dims; i++) {
				if (i == dims - 1) {
					locationStorage = (Object[]) locationStorage[location
							.getCoord(i)];
				} else {
					return (V) locationStorage[location.getCoord(i)];
				}
			}
			return null;
		}

		public Iterable<V> getAll(Object[] locationStorage, GridPoint location) {
			return Collections.singleton(get(locationStorage, location));
		}

		public V getRandom(Object[] locationStorage, GridPoint location) {
			return get(locationStorage, location);
		}

		public boolean put(V obj, Object[] locationStorage, GridPoint location) {
			int dims = location.dimensionCount();
			for (int i = 0; i < dims; i++) {
				if (i != dims - 1) {
					locationStorage = (Object[]) locationStorage[location
							.getCoord(i)];
				} else {
					if (locationStorage[location.getCoord(i)] == null) {
						locationStorage[location.getCoord(i)] = obj;
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		}

    public boolean allowsMultiOccupancy() {
      return false; 
    }

    public void remove(V obj, Object[] locationStorage, GridPoint location) {
			int dims = location.dimensionCount();
			for (int i = 0; i < dims; i++) {
				if (i == dims - 1) {
					locationStorage = (Object[]) locationStorage[location
							.getCoord(i)];
				} else {
					locationStorage[location.getCoord(i)] = null;
				}
			}
		}

	}

	/**
	 * Constructs this space with the specified name, adder, translator,
	 * accessor and size. The size is the size of the space meaning [3, 3] is a
	 * 3x3 space.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public FastDenseSingleOccuGrid(String name, GridAdder<T> adder, GridPointTranslator translator,
			int... size) {
		super(name, adder, translator, new FastDenseAccessor<T>(), size);
	}

	/**
	 * Constructs this space with the specified name and size. The size is the
	 * size of the space meaning [3, 3] is a 3x3 space. This uses by default a
	 * {@link SimpleGridAdder}, a {@link repast.simphony.space.continuous.StickyBorders},
	 * and a {@link MultiOccupancyCoordinateAccessor}; this means that objects
	 * cannot leave the space, are added to (0, 0, ..., 0) when added to the
	 * context, and that multiple objects can be at the same coordinate.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public FastDenseSingleOccuGrid(String name, int... size) {
		super(name, new SimpleGridAdder<T>(), new StrictBorders(), new FastDenseAccessor<T>(), size);
	}

	@Override
	public Object[] createLocationStorage() {
		Object[] storage = new Object[dimensions.getDimension(0)];

		LinkedList<Object[]> prevLevel = new LinkedList<Object[]>();
		prevLevel.add(storage);

		for (int i = 1; i < dimensions.size(); i++) {
			LinkedList<Object[]> prevLevelTemp = new LinkedList<Object[]>();
			for (int j = 0; prevLevel.size() > 0; j++) {
				Object[] level = prevLevel.remove();
				for (int k = 0; k < level.length; k++) {
					level[k] = new Object[dimensions.getDimension(i)];
					prevLevelTemp.add((Object[]) level[k]);
				}
			}
			prevLevel = prevLevelTemp;
		}
		return storage;
	}
}
