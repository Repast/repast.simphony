/*CopyrightHere*/
package repast.simphony.space.continuous;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of an n-dimensional continuous space.
 */
public class DefaultContinuousSpace<T> extends AbstractContinuousSpace<T, Map<NdPoint, Object>> {

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
	public DefaultContinuousSpace(String name, ContinuousAdder<T> adder,
	                              PointTranslator translator, double... size) {
		super(name, adder, translator,
				new MultiOccupancyCoordinateAccessor<T>(), size);
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
	 * @param origin
	 *            the origin of the space
	 */
	public DefaultContinuousSpace(String name, ContinuousAdder<T> adder,
	                              PointTranslator translator, double[] size, double[] origin) {
		super(name, adder, translator,
				new MultiOccupancyCoordinateAccessor<T>(), size, origin);
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
	public DefaultContinuousSpace(String name, ContinuousAdder<T> adder,
	                              PointTranslator translator, CoordinateAccessor<T, Map<NdPoint, Object>> accessor,
	                              double... size) {
		super(name, adder, translator, accessor, size);
	}

	/**
	 * Constructs this space with the specified name and size. The size is the
	 * size of the space meaning [3, 3] is a 3x3 space. This uses by default a
	 * {@link SimpleContinuousAdder}, a {@link StickyBorders}, and a
	 * {@link MultiOccupancyCoordinateAccessor}; this means that objects cannot
	 * leave the space, are added to (0, 0, ..., 0) when added to the context,
	 * and that multiple objects can be at the same coordinate.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public DefaultContinuousSpace(String name, double... size) {
		super(name, new SimpleCartesianAdder<T>(), new StickyBorders(),
				new MultiOccupancyCoordinateAccessor<T>(), size);
	}

	@Override
	protected Map<NdPoint, Object> createLocationStorage() {
		return new HashMap<NdPoint, Object>();
	}
}
