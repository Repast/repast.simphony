package repast.simphony.visualization;

import java.util.ArrayList;
import java.util.Map;

import repast.simphony.space.projection.Projection;

/**
 * Null empty dummy implementation of Layout.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NullLayout implements Layout {

	private float[] vals = new float[3];
	private VisualizationProperties props = new VisualizationProperties() {

		public Object getProperty(String id) {
			return null;
		}

		/**
		 * Gets an iterable over all the valid keys for these properties.
		 *
		 * @return an iterable over all the keys for these properties.
		 */
		public Iterable<String> getKeys() {
			return new ArrayList<String>();
		}
	};

	/**
	 * Gets the layout properties for this layout.
	 *
	 * @return the layout properties for this layout.
	 */
	public VisualizationProperties getLayoutProperties() {
		return props;
	}

	public float[] getLocation(Object obj) {
		return vals;
	}

	/**
	 * Sets the layout properties for this layout.
	 *
	 * @param props the layout properties
	 */
	public void setLayoutProperties(VisualizationProperties props) {}

	public Map getLocationData(){return null;}
	public void setProjection(Projection projection) {}

	public void update() {}
	
	public String getName() {
		return "null layout";
	}

  @Override
  public Box getBoundingBox() {
    return new Box();
  }
	
	

}
