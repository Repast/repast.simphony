package repast.simphony.visualization.visualization3D.layout;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.VisualizationProperties;

/**
 * Distributes objects uniformly at random within a 20 x 20 x 20 bounding box.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class Random3DLayout implements Layout {

  private float[] point = new float[3];

  public float[] getLocation(Object obj) {
    point[0] = RandomHelper.getUniform().nextFloatFromTo(-.4f, .4f);
    point[1] = RandomHelper.getUniform().nextFloatFromTo(-.4f, .4f);
    point[2] = RandomHelper.getUniform().nextFloatFromTo(-.4f, .4f);

    return point;
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
   * @param props
   *          the layout properties
   */
  public void setLayoutProperties(VisualizationProperties props) {
    // not used
  }

  public void update() {
  }

  public String getName() {
    return "Random 3D";
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    return new Box();
  }
}
