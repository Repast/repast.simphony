package repast.simphony.visualization.continuous;

import javax.vecmath.Point3f;

import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

/**
 * 3D Layout for a 3D continuous space. 
 *
 * @author Nick Collier
 * @author Jerry Vos
 */
public class Continuous3DLayout implements Layout<Object, ContinuousSpace> {

	private ContinuousSpace space;
	protected float[] point = new float[3];
	private float unitSize = .06f;
	
	private Box box;

	private VisualizationProperties visualizationProps;

	public Continuous3DLayout() {}

	public Continuous3DLayout(ContinuousSpace space) {
		this.space = space;
	}

	public void setProjection(ContinuousSpace space) {
		this.space = space;
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
			unitSize = (Float)obj;
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

//	int numb;

	public float[] getLocation(Object obj) {
		NdPoint gpoint = space.getLocation(obj);
		double[] origin = space.getDimensions().originToDoubleArray(null);
		float xOffset = (float) origin[0];
		float yOffset = (float) origin[1];
		float zOffset = 0;
		if (origin.length == 3) {
		  zOffset = (float) origin[2];
		}
		
		if (gpoint == null) {
			point[0] = Float.NEGATIVE_INFINITY;
			point[1] = Float.NEGATIVE_INFINITY;
			point[2] = Float.NEGATIVE_INFINITY;
			return point;
		}
		float x = (float) gpoint.getX();
		float y = (float) gpoint.getY();
		float z = 0;
		if (gpoint.dimensionCount() == 3) {
		  z = (float) gpoint.getZ();
		}

//		point[0] = ((numb & 4) >> 2);
//		point[1] = ((numb & 2) >> 1);
//		point[2] = ((numb & 1));
//		
//		numb++;
		point[0] = (x + xOffset) * unitSize;
		point[1] = (y + yOffset) * unitSize;
		point[2] = (z + zOffset) * unitSize;

		return point;
	}

	public void update() {
	}

	public String getName() {
		return "Continuous 3D";
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    if (box == null) {
      Dimensions dims = space.getDimensions();
      double[] origin = space.getDimensions().originToDoubleArray(null);
      float xOffset = (float) origin[0];
      float yOffset = (float) origin[1];
      float zOffset = (float) origin[2];
      
      box = new Box(new Point3f(xOffset * unitSize, yOffset * unitSize, zOffset * unitSize), 
          new Point3f(((float)dims.getWidth() + xOffset) * unitSize, ((float)dims.getHeight() + xOffset) * unitSize, 
              ((float)dims.getDepth() + zOffset) * unitSize));
    }
    
    return box;
  }
	
	
}
