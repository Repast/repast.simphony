package repast.simphony.query.space.grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.space.grid.Grid;
import repast.simphony.util.collections.Contains;
import repast.simphony.util.collections.FilteredIterator;
import repast.simphony.util.collections.IterableAdaptor;
import repast.simphony.util.collections.IteratorOverIterables;

/**
 * Queries a grid for the Moore neighborhood of a specific point or object. This
 * works with 1, 2, and 3 dimensions.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class MooreQuery<T> extends AbstractGridQuery<T> {
  
  private final List<T> EMPTY_ITER  = new ArrayList<T>();

  private class Iterator1D extends IteratorOverIterables<T> {
    // center of ngh where source obj is.
    int cx;
    int x;
    int xMax;

    public Iterator1D(int xMin, int xMax, int cx) {

      this.cx = cx;
      x = xMin;
      this.xMax = xMax;
      init();
    }

    protected Iterable<T> getNext() {
      if (x == cx) {
        x++;
      }

      if (x > xMax)
        return null;
      Iterable<T> tmp = grid.getObjectsAt(x++);
      return tmp;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private class Iterator2D extends IteratorOverIterables<T> {
    // center of ngh where source obj is.
    int cx, cy;
    int x, y;
    int xMax, yMax, yMin;

    public Iterator2D(int xMin, int yMin, int xMax, int yMax, int cx, int cy) {

      this.cy = cy;
      this.cx = cx;
      x = xMin;
      y = yMin;
      this.yMin = yMin;
      this.xMax = xMax;
      this.yMax = yMax;
      init();
    }

    protected Iterable<T> getNext() {
      if (y > yMax) {
        y = yMin;
        x++;
      }
      if (x > xMax)
        return null;
      Iterable<T> tmp = (x == cx && y == cy) ? EMPTY_ITER : grid.getObjectsAt(x, y);
      y++;
      return tmp;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private class Iterator3D extends IteratorOverIterables<T> {
    // center of ngh where source obj is.
    int cx, cy, cz;
    int x, y, z;
    int xMax, yMax, yMin, xMin, zMax;

    public Iterator3D(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int cx, int cy,
        int cz) {
      this.cy = cy;
      this.cx = cx;
      this.cz = cz;
      x = xMin;
      y = yMin;
      z = zMin;
      this.yMin = yMin;
      this.xMin = xMin;
      this.xMax = xMax;
      this.yMax = yMax;
      this.zMax = zMax;
      init();
    }

    protected Iterable<T> getNext() {
      
      if (y > yMax) {
        y = yMin;
        x++;
      }

      if (x > xMax) {
        x = xMin;
        z++;
      }
      if (z > zMax)
        return null;
      Iterable<T> tmp = (x == cx && y == cy && z == cz) ? EMPTY_ITER : grid.getObjectsAt(x, y, z);
      y++;
      return tmp;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Creates a Moore query on the specified grid using the specified object as
   * the source where the neighborhood is specified by the extent. The extent
   * defines the dimensions of the neighborhood in the x, y, and optionaly z
   * dimensions. If the extent args are missing, they default to one.
   * <p>
   * 
   * This object can be reused by resetting the source and extents using the
   * <code>
   * reset</code> method. The queries will return an empty iterable if the
   * source object does not have a grid location.
   * 
   * @param grid
   * @param source
   * @param extent
   */
  public MooreQuery(Grid<T> grid, T source, int... extent) {
    super(grid, source, extent);
  }

  /**
   * Gets an iterable over all the objects that make up the Moore neighborhood
   * of the source object. The source object and neighborhood extent are
   * specified in the constructor or the reset method. The order of the objects
   * returned is back to front by column starting with the left side column.
   * <p>
   * 
   * This will return an empty iterable if the source object does not have a
   * grid location.
   * 
   * @return an iterable over all the objects that make up the Moore
   *         neighborhood of the source object.
   */
  public Iterable<T> query() {
    if (point == null)
      return emptyList;
    double px = point.getX();

    int size = dims.size();
    setupMinMax(size);

    if (size == 1) {
      return new IterableAdaptor<T>(new Iterator1D(mins[0], maxs[0], (int) px));
    } else if (size == 2) {
      double py = point.getY();
      return new IterableAdaptor<T>(new Iterator2D(mins[0], mins[1], maxs[0], maxs[1], (int) px,
          (int) py));
    } else {
      double py = point.getY();
      int pz = (int) point.getZ();
      return new IterableAdaptor<T>(new Iterator3D(mins[0], mins[1], mins[2], maxs[0], maxs[1],
          maxs[2], (int) px, (int) py, pz));
    }
  }

  private void setupMinMax(int size) {
    for (int i = 0; i < size; i++) {
      double coord = point.getCoord(i);
      double max = coord + extent[i];
      double min = coord - extent[i];
      mins[i] = (int) min;
      maxs[i] = (int) max;
      if (!grid.isPeriodic()) {
        int origin = (int) dims.getOrigin(i);
        if (min < -origin)
          min = -origin;
        int dimension = (int) dims.getDimension(i);
        if (max > dimension - origin - 1)
          max = dimension - origin - 1;
        mins[i] = (int) min;
        maxs[i] = (int) max;
      }
    }
  }

  /**
   * Gets an iterable over all the objects that make up the Moore neighborhood
   * of the source object and are in the specified iterable. The source object
   * and neighborhood extent are specified in the constructor or the reset
   * method. The order of the objects returned is back to front by column
   * starting with the left side column.
   * <p>
   * 
   * This will return an empty iterable if the source object does not have a
   * grid location.
   * 
   * @return an iterable over all the objects that make up the Moore
   *         neighborhood of the source object and are in the spefied iterable.
   */
  public Iterable<T> query(Iterable<T> iter) {
    if (point == null)
      return emptyList;
    Set<T> set = new HashSet<T>();
    for (T item : iter) {
      set.add(item);
    }
    double px = point.getX();
    double py = point.getY();
    int size = dims.size();

    setupMinMax(size);

    if (size == 2) {
      return new FilteredIterator<T>(new Iterator2D(mins[0], mins[1], maxs[0], maxs[1], (int) px,
          (int) py), new Contains<T>(set));
    } else {
      int pz = (int) point.getZ();
      return new FilteredIterator<T>(new Iterator3D(mins[0], mins[1], mins[2], maxs[0], maxs[1],
          maxs[2], (int) px, (int) py, pz), new Contains<T>(set));
    }
  }
}
