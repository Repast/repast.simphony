package repast.simphony.visualization.grid;

import org.jogamp.vecmath.Point3f;

import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

/**
 * 3D Layout for a 2D or 3D single occupancy grid. Origin is in lower left hand corner.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class Grid3DLayout implements Layout<Object, Grid> {
  

	interface Locator {
		float[] getLocation(Object obj);
	}

	class Locator2D implements Locator {
		public float[] getLocation(Object obj) {
			GridPoint gpoint = grid.getLocation(obj);
			int[] origin = grid.getDimensions().originToIntArray(null);
			float xOffset = (float) origin[0];
			float yOffset = (float) origin[1];
			float x = (float) (gpoint.getX() + xOffset) * cellSize;
			 // flip the z coordinate so that lower left hand corner is 0,0
			float z = (float) (yDim - (gpoint.getY() + yOffset)) * cellSize;
			point[0] = x;
			point[2] = z;
			return point;
		}
	}

	class Locator3D implements Locator {

		public float[] getLocation(Object obj) {
			GridPoint gpoint = grid.getLocation(obj);
			int[] origin = grid.getDimensions().originToIntArray(null);
			float xOffset = (float) origin[0];
			float yOffset = (float) origin[1];
			float zOffset = (float) origin[2];
			float x = (float) (gpoint.getX() + xOffset) * cellSize;
			float y = (float) (gpoint.getY() + yOffset) * cellSize;
		// flip the z coordinate so that lower left hand corner is 0,0
			float z = (float) (zDim - (gpoint.getZ() + zOffset) ) * cellSize;
			point[0] = x;
			point[1] = y;
			point[2] = z;
			return point;
		}

	}

	private Grid grid;
	protected float[] point = new float[3];
	
	private Box box;

	private float cellSize = .06f;
	// private float yDim;
	private VisualizationProperties visualizationProps;
	private Locator locator = new Locator2D();
	private double yDim, zDim;

	public Grid3DLayout() {
	}

	public void setProjection(Grid grid) {
		this.grid = grid;
		this.yDim = grid.getDimensions().getHeight();
		if (grid.getDimensions().size() == 3) {
			locator = new Locator3D();
			zDim = grid.getDimensions().getDepth();
		}
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
			cellSize = (Float) obj;
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
		return locator.getLocation(obj);
	}

	public void update() {
	}
	
	public String getName() {
		return "Grid 3D";
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
	      int zOffset = origin[3];
	      
	      box = new Box(new Point3f(xOffset * cellSize, yOffset * cellSize, zOffset * cellSize), 
	          new Point3f((dims.getWidth() + xOffset) * cellSize, (dims.getHeight() + xOffset) * cellSize,
	              (dims.getDepth() + zOffset) * cellSize));
	 
	    }
	    
	    return box;
	  }
	
	
}
