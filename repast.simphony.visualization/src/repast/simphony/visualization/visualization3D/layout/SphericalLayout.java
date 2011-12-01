package repast.simphony.visualization.visualization3D.layout;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.VisualizationProperties;

/**
 * 
 * z between -R and R, phi between 0 and 2 pi, each with a uniform distribution
 * To find the latitude (theta) of this point, note that z=Rsin(theta), so
 * theta=sin-1(z/R); its longitude is (surprise!) phi. In rectilinear
 * coordinates, x=Rcos(theta)cos(phi), y=Rcos(theta)sin(phi), z=Rsin(theta)=
 * (surprise!) z.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class SphericalLayout implements Layout {

  protected float[] location = new float[3];
  protected float radius = .4f;

  public float[] getLocation(Object o) {
    double phi = RandomHelper.getUniform().nextDoubleFromTo(0, 2 * Math.PI);
    float z = RandomHelper.getUniform().nextFloatFromTo(-radius, radius);
    double theta = Math.cos(Math.asin(z / radius));
    location[0] = (float) (Math.cos(phi) * theta * radius);
    location[1] = (float) (Math.sin(phi) * theta * radius);
    location[2] = z;
    return location;
  }

  public void setProjection(Projection projection) {

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

  /**
   * Gets the layout properties for this layout.
   * 
   * @return the layout properties for this layout.
   */
  public VisualizationProperties getLayoutProperties() {
    return null;
  }

  public void update() {
  }

  public String getName() {
    return "Spherical";
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
