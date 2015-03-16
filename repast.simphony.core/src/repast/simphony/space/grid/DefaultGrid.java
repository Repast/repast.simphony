package repast.simphony.space.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of a grid backed by a map.
 * 
 * @author Jerry Vos
 */
public class DefaultGrid<T> extends AbstractGrid<T, Map<GridPoint, Object>> {

	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param adder
	 *            the adder for adding new objects to the grid
	 * @param translator
	 * 
	 * @param accessor
	 *            the accessor used for accessing grid cells
	 * @param size
	 *            the size of the space
	 * 
	 */
	public DefaultGrid(String name, GridAdder<T> adder,
			GridPointTranslator translator,
			CellAccessor<T, Map<GridPoint, Object>> accessor, int... size) {
		super(name, adder, translator, accessor, size);
	}
	
	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param adder
	 *            the adder for adding new objects to the grid
	 * @param translator
	 * 
	 * @param accessor
	 *            the accessor used for accessing grid cells
	 * @param size
	 *            the size of the space
	 * @param origin
	 * 			  the origin of the space           
	 * 
	 */
	public DefaultGrid(String name, GridAdder<T> adder,
			GridPointTranslator translator,
			CellAccessor<T, Map<GridPoint, Object>> accessor, int[] size, int[] origin) {
		super(name, adder, translator, accessor, size, origin);
	}

	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param size
	 *            the size of the space
	 * 
	 */
	public DefaultGrid(String name, int... size) {
		super(name, new SimpleGridAdder<T>(), new StrictBorders(), new MultiOccupancyCellAccessor<T>(),
				size);

	}
	
	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param size
	 *            the size of the space
	 * @param origin
	 * 			  the origin of the space           
	 * 
	 */
	public DefaultGrid(String name, int[] size, int[] origin) {
		super(name, new SimpleGridAdder<T>(), new StrictBorders(), new MultiOccupancyCellAccessor<T>(),
				size, origin);

	}

	@Override
	protected Map<GridPoint, Object> createLocationStorage() {
		return new HashMap<GridPoint, Object>();
	}
}
