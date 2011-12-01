package repast.simphony.visualization.continuous;

import javax.vecmath.Point3f;

import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;

/**
 * Layout for a 2D continuous space.
 *
 * @author Nick Collier
 * @author Jerry Vos
 */
public class Continuous2DLayout implements Layout<Object, ContinuousSpace> {

	private ContinuousSpace space;
	protected float[] point = new float[2];
	private float unitSize = 15f;
	
	private Box box = null;

	private VisualizationProperties visualizationProps;

	public Continuous2DLayout() {}

	public Continuous2DLayout(ContinuousSpace space) {
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
	public float[] getLocation(Object obj) {
		NdPoint gpoint = space.getLocation(obj);
		double[] origin = space.getDimensions().originToDoubleArray(null);
		float xOffset = (float) origin[0];
		float yOffset = (float) origin[1];
		
		if (gpoint == null) {
			point[0] = Float.POSITIVE_INFINITY;
			point[1] = Float.POSITIVE_INFINITY;
			return point;
		}
		float x = (float) gpoint.getX();
		float y = (float) gpoint.getY();
		point[0] = (x + xOffset) * unitSize;
		point[1] = (y + yOffset) * unitSize;

		return point;
	}

	public void update() {
	}

	public String getName() {
		return "Continuous 2D";
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
      
      box = new Box(new Point3f(xOffset * unitSize, yOffset * unitSize, 0), 
          new Point3f(((float)dims.getWidth() + xOffset) * unitSize, ((float)dims.getHeight() + xOffset) * unitSize, 0));
      
    }
    
    return box;
  }
	
	
}
