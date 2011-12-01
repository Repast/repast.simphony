package repast.simphony.visualization.editor;

import repast.simphony.space.grid.CellAccessor;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridAdder;
import repast.simphony.visualization.editor.space.Projected3DGrid;

/**
 * Listens for add events and prepares
 * the grid so that the agent will be added
 * at the appropriate location.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GridAddListener implements AddListener {

  class LocationAdder<Object> implements GridAdder<Object> {

    private int[] location;

    public void initLocation(int[] location) {
      this.location = location;
    }

    public void add(Grid<Object> destination, Object object) {
      destination.moveTo(object, location);

    }
  }

  private Grid grid;
  private GridAdder adder;
  private LocationAdder locAdder;
  private int[] location;
  private int constantIndex = -1, dimIndex1, dimIndex2;
  private GridAddHandler handler;

  public GridAddListener(Grid grid, GridAddHandler handler) {
    this.grid = grid;
    this.adder = grid.getAdder();
    if (grid instanceof Projected3DGrid) {
      location = new int[3];
      Projected3DGrid pGrid = (Projected3DGrid) grid;
      constantIndex = pGrid.getConstantIndex();
      dimIndex1 = pGrid.getIndex1();
      dimIndex2 = pGrid.getIndex2();
    } else {
      location = new int[grid.getDimensions().size()];
    }
    locAdder = new LocationAdder();

    this.handler = handler;
  }

  /**
   * Called immediately prior to an agent being added.
   *
   * @param obj
   *@param point the location at which the agent should
   *              be added. @return true if an agent can be added to the specified location,
   *         otherwise false.
   */
  public boolean preAdd(Object obj, double... point) {
    if (constantIndex == -1) {
      for (int i = 0; i < this.location.length; i++) {
        this.location[i] = (int) point[i];
      }
    } else {
      this.location[constantIndex] = 0;
      this.location[dimIndex1] = (int) point[0];
      this.location[dimIndex2] = (int) point[1];
    }

    CellAccessor accessor = grid.getCellAccessor();
    if (accessor.allowsMultiOccupancy() || grid.getObjectAt(location) == null) {
      // the grid is multi occupancy then can add it, or if it not multi
      // then location must be empty.
      locAdder.initLocation(location);
      grid.setAdder(locAdder);
      return true;
    }
    return false;
  }

  /**
   * Called immediately after the agent has been added.
   */
  public void postAdd() {
    grid.setAdder(adder);
  }

  /**
   * Gets the PInputEvent handler that will handle
   * the gui part of the adding.
   *
   * @return the PInputEvent handler that will handle
   *         the gui part of the adding.
   */
  public PEditorEventListener getAddHandler() {
    return handler;
  }
}
