package repast.simphony.visualization.grid;

import javax.vecmath.Point3f;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;

/**
 * Layout for a 2D single occupancy grid.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class Grid2DLayout implements Layout<Object, Grid> {

	private Grid grid;
	protected float[] point = new float[2];

	private float cellSize = 15f;
	private VisualizationProperties visualizationProps;
	
	private Box box;

	public Grid2DLayout() {}

	public Grid2DLayout(Grid grid) {
		this.grid = grid;
	}

	public void setProjection(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the layout properties for this layout.
	 *
	 * @param props the layout properties
	 */
	public void setLayoutProperties(VisualizationProperties props) {
		this.visualizationProps = props;
		Object obj = props.getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
		if (obj != null && obj instanceof Float) {
			cellSize = (Float)obj;
		}
	}

	/**
	 * Gets the layout properties for this layout.
	 *
	 * @return the layout properties for this layout.
	 */
	public VisualizationProperties getLayoutProperties() {
		return visualizationProps;
	}

	public float[] getLocation(Object obj) {
		GridPoint gpoint = grid.getLocation(obj);
		int[] origin = grid.getDimensions().originToIntArray(null);
		float xOffset = (float) origin[0];
		float yOffset = (float) origin[1];

		if (gpoint == null) {
			point[0] = Float.POSITIVE_INFINITY;
			point[1] = Float.POSITIVE_INFINITY;
			return point;
		}
		
		float x = (float) (gpoint.getX() + xOffset) * cellSize;
		float y = (float) (gpoint.getY() + yOffset) * cellSize;
		point[0] = x;
		point[1] = y;
		return point;
	}

	public void update() {
	}
	
	public String getName() {
		return "Grid 2D";
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    if (box == null) {
      GridDimensions dims = grid.getDimensions();
      int[] origin = grid.getDimensions().originToIntArray(null);
      int xOffset = origin[0];
      int yOffset = origin[1];
      
      box = new Box(new Point3f(xOffset * cellSize, yOffset * cellSize, 0), 
          new Point3f((dims.getWidth() + xOffset) * cellSize, (dims.getHeight() + xOffset) * cellSize, 0));
 
    }
    
    return box;
  }
	
	
}
