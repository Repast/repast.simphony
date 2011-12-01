package repast.simphony.visualization.visualization2D;

import javax.vecmath.Point3f;

import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.VisualizationProperties;

public class Random2DLayout implements Layout {
	float x, y;

	public Random2DLayout() {
		this(400, 400);
	}

	public Random2DLayout(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void setProjection(Projection projection) {

	}

	/**
	 * Gets the layout properties for this layout.
	 *
	 * @return the layout properties for this layout.
	 */
	public VisualizationProperties getLayoutProperties() {
		return null;
	}

	/**
	 * Sets the layout properties for this layout.
	 *
	 * @param props the layout properties
	 */
	public void setLayoutProperties(VisualizationProperties props) {
		// not used
	}

	public float[] getLocation(Object obj) {
		return new float[] { (float) Math.random() * x,
				(float) Math.random() * y };
	}
	
	public String getName() {
		return "Random 2D";
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    return new Box(new Point3f(0, 0, 0), new Point3f(400, 400, 0));
  }
	
	

}
