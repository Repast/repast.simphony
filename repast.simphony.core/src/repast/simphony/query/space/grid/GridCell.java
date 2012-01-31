/**
 * 
 */
package repast.simphony.query.space.grid;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.grid.GridPoint;

/**
 * Encapsulates a Grid point location and the objects at that point. 
 * 
 * @author Nick Collier
 */
public class GridCell<T> {
  
  private GridPoint pt;
  private List<T> objs = new ArrayList<T>();
  private Class<T> clazz;

  /**
   * Creates a GridCell for the specified point. 
   * 
   * @param pt the location of the grid cell.
   * @param clazz the type of the objects the grid cell will hold
   */
  public GridCell(GridPoint pt, Class<T> clazz) {
    this.pt = pt;
    this.clazz = clazz;
  }
  
  /**
   * Adds an object to this GridCell. If the object is not
   * of the type specified in the constructor it will not be
   * added.
   * 
   * @param obj the object to add
   */
  @SuppressWarnings("unchecked")
  void addObject(Object obj) {
    if (clazz.isAssignableFrom(obj.getClass())) {
      objs.add((T)obj);
    }
  }
  
  /**
   * Gets the number of object held by this GridCell.
   *  
   * @return the number of object held by this GridCell.
   */
  public int size() {
    return objs.size();
  }
  
  /**
   * Gets an iterable of the objects held by this GridCell.
   * 
   * @return an iterable of the objects held by this GridCell.
   */
  public Iterable<T> items() {
    return objs;
  }

  /**
   * Gets the location of this GridCell.
   * 
   * @return the location of this GridCell.
   */
  public GridPoint getPoint() {
    return pt;
  }
  
  
  
  

}
