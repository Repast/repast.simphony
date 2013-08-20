/**
 * 
 */
package repast.simphony.systemdynamics.figure;

import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Transform;

/**
 * Hourglass decoration for rate links. 
 * 
 * @author Nick Collier
 */
public class HourglassDecoration extends Polygon {
  
  private static final PointList HOURGLASS = new PointList();
  
  static {
    HOURGLASS.addPoint(-1, -1);
    HOURGLASS.addPoint(1, -1);
    HOURGLASS.addPoint(-1, 1);
    HOURGLASS.addPoint(1, 1);
  }
  
  private Point location = new Point();
  private Transform transform = new Transform();
  
  public HourglassDecoration() {
    transform.setScale(8, 10);
  }
  
  public void setLocation(Point p) {
    super.getPoints().removeAllPoints();
    bounds = null;
    location.setLocation(p);
    transform.setTranslation(p.x, p.y);
  }
  
  public PointList getPoints() {
    if (super.getPoints().size() == 0 && transform != null) {
      for (int i = 0; i < HOURGLASS.size(); i++) {
        addPoint(transform.getTransformed(HOURGLASS.getPoint(i)));
      }
    }
    return super.getPoints();
  }
}
