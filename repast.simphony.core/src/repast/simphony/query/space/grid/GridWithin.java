package repast.simphony.query.space.grid;

import org.apache.commons.collections15.iterators.IteratorChain;
import repast.simphony.context.Context;
import repast.simphony.query.WithinDistance;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.RangeCombination;
import repast.simphony.util.collections.IterableAdaptor;

import java.util.HashSet;

/**
 * A within type query over a grid space. This query will return all the objects
 * that are within a specified distance of a specified object in either
 * all the grid spaces in a context or in a specific grid space.
 * "Within" includes the distance, so within 10 means all the objects from a
 * distance of 0 to 10 including 10.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GridWithin<T> extends WithinDistance<T> {

  private Grid<T> space;

  private static class MutablePoint extends GridPoint {

    public MutablePoint(int... point) {
      super(point);
    }

    void setCoord(int index, int value) {
      point[index] = value;
    }

    public int[] getCoords() {
      return point;
    }
  }

  private static abstract class IterableCreator<T> {

    Grid<T> space;
    GridPoint origin;
    double distSq;
    double distance;
    Object target;

    public IterableCreator(double distance, GridPoint origin, Grid<T> space, Object target) {
      this.distSq = distance * distance;
      this.distance = distance;
      this.origin = origin;
      this.space = space;
      this.target = target;
    }

    public int getMin(int index) {
      int val = (int) Math.floor(origin.getCoord(index) - distance);
      if (!space.isPeriodic()) {
        return Math.max(space.getDimensions().getOrigin(index), val);
      }

      return val;

    }

    public int getMax(int index) {
      int val = (int) Math.ceil(origin.getCoord(index) + distance);
      if (!space.isPeriodic()) {
        GridDimensions dims = space.getDimensions();
        return Math.min((dims.getOrigin(index) + dims.getDimension(index) - 1), val);
      }

      return val;

    }

    abstract Iterable<T> create();
  }

  private static class IterableCreator1D<T> extends IterableCreator {

    public IterableCreator1D(double distance, GridPoint origin, Grid<T> space, Object target) {
      super(distance, origin, space, target);
    }

    Iterable<T> create() {
      HashSet<T> set = new HashSet<T>();
      int start = getMin(0);
      int end = getMax(0);

      MutablePoint pt = new MutablePoint(0);
      for (int i = start; i <= end; i++) {
        pt.setCoord(0, i);
        if (space.getDistanceSq(origin, pt) <= distSq) {
          for (T obj : (Iterable<T>) space.getObjectsAt(i)) {
            set.add(obj);
          }
        }

      }
      set.remove(target);
      return set;

    }
  }

  private static class IterableCreator2D<T> extends IterableCreator {

    public IterableCreator2D(double distance, GridPoint origin, Grid<T> space, Object target) {
      super(distance, origin, space, target);
    }

    Iterable<T> create() {
      HashSet<T> set = new HashSet<T>();

      int xStart = getMin(0);
      int xEnd = getMax(0);

      int yStart = getMin(1);
      int yEnd = getMax(1);

      MutablePoint pt = new MutablePoint(0, 0);
      for (int x = xStart; x <= xEnd; x++) {
        pt.setCoord(0, x);
        for (int y = yStart; y <= yEnd; y++) {
          pt.setCoord(1, y);

          if (space.getDistanceSq(origin, pt) <= distSq) {
            for (T obj : (Iterable<T>) space.getObjectsAt(x, y)) {
              set.add(obj);
            }
          }

        }
      }

      set.remove(target);
      return set;
    }
  }

  private static class IterableCreator3D<T> extends IterableCreator {

    public IterableCreator3D(double distance, GridPoint origin, Grid<T> space, Object target) {
      super(distance, origin, space, target);
    }

    Iterable<T> create() {
      HashSet<T> set = new HashSet<T>();

      int xStart = getMin(0);
      int xEnd = getMax(0);

      int yStart = getMin(1);
      int yEnd = getMax(1);

      int zStart = getMin(2);
      int zEnd = getMax(2);

      MutablePoint pt = new MutablePoint(0, 0, 0);
      for (int x = xStart; x <= xEnd; x++) {
        pt.setCoord(0, x);
        for (int y = yStart; y <= yEnd; y++) {
          pt.setCoord(1, y);
          for (int z = zStart; z <= zEnd; z++) {
            pt.setCoord(2, z);

            if (space.getDistanceSq(origin, pt) <= distSq) {
              for (T obj : (Iterable<T>) space.getObjectsAt(x, y, z)) {
                set.add(obj);
              }
            }
          }
        }
      }

      set.remove(target);
      return set;
    }
  }

  private static class IterableCreatorND<T> extends IterableCreator {

    public IterableCreatorND(double distance, GridPoint origin, Grid<T> space, Object target) {
      super(distance, origin, space, target);
    }

    Iterable<T> create() {
      HashSet<T> set = new HashSet<T>();

      RangeCombination.RangeComboBuilder builder = new RangeCombination.RangeComboBuilder();
      for (int i = 0, n = origin.dimensionCount(); i < n; i++) {
        int min = getMin(i);
        int max = getMax(i);
        builder.addRange(min, max);
      }

      RangeCombination combo = builder.build();
      MutablePoint pt = new MutablePoint(new int[combo.numRanges()]);
      while (combo.hasNext()) {
        int[] coords = pt.getCoords();
        combo.next(coords);

        if (space.getDistanceSq(origin, pt) <= distSq) {
          for (T obj : (Iterable<T>) space.getObjectsAt(coords)) {
            set.add(obj);
          }
        }
      }

      set.remove(target);
      return set;
    }
  }

  /**
   * Creates a GridWithin query that will find all the objects
   * within the specified distance of the specified object in
   * all the grid spaces in the specified context.
   *
   * @param context
   * @param obj
   * @param distance
   */
  public GridWithin(Context<T> context, T obj, double distance) {
    super(context, distance, obj);
  }

  /**
   * Creates a GridWithin query that will find all the objects
   * within the specified distance of the specified object in the
   * specified space.
   *
   * @param space
   * @param obj
   * @param distance
   */
  public GridWithin(Grid<T> space, T obj, double distance) {
    super(null, distance, obj);
    this.space = space;
  }

  /**
   * Creates an iterable over all the objects within
   * the specified distance in the space.
   *
   * @return an iterable over all the objects within
   *         the specified distance in the space.
   */
  protected Iterable<T> createIterable() {
    return createIterable(space);
  }

  private Iterable<T> createIterable(Grid<T> space) {
    GridPoint origin = space.getLocation(obj);
    if (origin == null) return EMPTY;

    if (origin.dimensionCount() == 1) return new IterableCreator1D<T>(distance, origin, space, obj).create();
    if (origin.dimensionCount() == 2) return new IterableCreator2D<T>(distance, origin, space, obj).create();
    if (origin.dimensionCount() == 3) return new IterableCreator3D<T>(distance, origin, space, obj).create();
    else return new IterableCreatorND<T>(distance, origin, space, obj).create();

  }

  /**
   * Creates an iterable over all the objects within
   * the specified distance in all the continuoous spaces
   * in the context.
   *
   * @param context
   * @return an iterable over all the objects within
   *         the specified distance in all the continuoous spaces
   *         in the context.
   */
  protected Iterable<T> createIterable(Context<T> context) {
    IteratorChain<T> chain = new IteratorChain<T>();
    for (Grid<T> space : context.getProjections(Grid.class)) {
      GridPoint origin = space.getLocation(obj);
      if (origin != null) {
        chain.addIterator(createIterable(space).iterator());
      }
    }
    return new IterableAdaptor<T>(chain);
  }
}
