/**
 * 
 */
package repast.simphony.query.space.grid;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

/**
 * Retrieves the MooreNeighborhood around a particular GridPoint.
 * The neighborhood is represented as a List of GridCell-s.
 * 
 * @author Nick Collier
 */
public class GridCellNgh<T> {

  private GridPoint point;
  protected Grid<? extends Object> grid;
  protected int[] extent;
  protected GridDimensions dims;
  protected int[] mins, maxs;
  protected Class<T> clazz;

  /**
   * Creates a GridCellNgh using the specified grid, point, type and extent. 
   * 
   * @param grid the grid that contains the neighborhood
   * @param point the central point of the neighborhood
   * @param clazz the type of objects we want to get in the neighborhood. Objects not
   * of this type will not be in the neighborhood list
   * @param extent the extent of the neighborhood in each dimension
   */
  public GridCellNgh(Grid<? extends Object> grid, GridPoint point, Class<T> clazz, int... extent) {
    this.clazz = clazz;
    this.grid = grid;
    this.dims = grid.getDimensions();
    int size = dims.size();
    if (size > 3 || size < 1)
      throw new IllegalArgumentException("Query is only " + "supported on 1D, 2D and 3D grids");
    mins = new int[size];
    maxs = new int[size];
    this.point = point;
    setExtent(size, extent);
    setupMinMax(size);
  }

  private void setExtent(int size, int... extent) {
    if (extent == null || extent.length == 0) {
      extent = new int[size];

      for (int i = 0; i < size; i++)
        extent[i] = 1;
    }
    if (extent.length != dims.size())
      throw new IllegalArgumentException("Number of extents must"
          + " match the number of grid dimensions");
    this.extent = extent;
  }

  private void addCell(List<GridCell<T>> list, int... pt) {
    
    GridPoint gpt = new GridPoint(pt);
    if (grid.isPeriodic()) {
      grid.getGridPointTranslator().transform(gpt, pt);
    }
    GridCell<T> cell = new GridCell<T>(gpt, clazz);
    list.add(cell);
    for (Object obj : grid.getObjectsAt(pt)) {
      cell.addObject(obj);
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
   * Gets the neighborhood of GridCells.
   * 
   * @param includeCenter if true then the center, i.e the point
   * GridCell will also be returned.
   * 
   * @return a list of GridCells that make up the neighborhood.
   */
  public List<GridCell<T>> getNeighborhood(boolean includeCenter) {
    List<GridCell<T>> list = new ArrayList<GridCell<T>>();
    if (mins.length == 1) {
      for (int i = mins[0]; i <= maxs[0]; i++) {
        if (includeCenter) {
          addCell(list, i);
        } else if (i != point.getX()) {
          addCell(list, i);
        }
      }
    } else if (mins.length == 2) {
      for (int x = mins[0]; x <= maxs[0]; x++) {
        for (int y = mins[1]; y <= maxs[1]; y++) {
          if (includeCenter) {
            addCell(list, x, y);
          } else if (x != point.getX() || y != point.getY()) {
            addCell(list, x, y);
          }
        }
      }
    } else {
      for (int x = mins[0]; x <= maxs[0]; x++) {
        for (int y = mins[1]; y <= maxs[1]; y++) {
          for (int z = mins[2]; z <= maxs[2]; z++) {
            if (includeCenter) {
              addCell(list, x, y, z);
            } else if (x != point.getX() || y != point.getY() || point.getZ() != z) {
              addCell(list, x, y, z);
            }
          }

        }

      }
    }
    return list;
  }
}
