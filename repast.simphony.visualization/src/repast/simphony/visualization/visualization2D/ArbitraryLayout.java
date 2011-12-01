package repast.simphony.visualization.visualization2D;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.space.projection.Projection;

public class ArbitraryLayout implements Layout {
	private float[] origin = new float[] { 0, 0 };

	Map<Object, float[]> locationMap = new HashMap<Object, float[]>();

	public void setLocation(Object o, float... loc) {
		locationMap.put(o, loc);
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the layout properties for this layout.
	 *
	 * @param props the layout properties
	 */
	public void setLayoutProperties(VisualizationProperties props) {
		//todo implement method
	}

	/**
	 * Gets the layout properties for this layout.
	 *
	 * @return the layout properties for this layout.
	 */
	public VisualizationProperties getLayoutProperties() {
		return null;
	}

	public void setProjection(Projection projection) {

	}

	public float[] getLocation(Object obj) {
		float[] loc = locationMap.get(obj);
		if (loc == null) {
			loc = origin;
			setLocation(obj, loc);
		}
		return loc;
	}

	public String getName() {
		return "Arbitrary";
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    return new Box();
  }
	
	
}