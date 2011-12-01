/**
 * 
 */
package repast.simphony.visualization;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * @author Nick Collier
 */
public class Box {
  
  private Point3f upper, lower, center;
  private Vector3f diff;
  
  public Box() {
    this(new Point3f(), new Point3f());
  }
  
  public Box(Point3f lowerPt, Point3f upperPt) {
    upper = new Point3f(upperPt);
    lower = new Point3f(lowerPt);
    
    center = new Point3f(upper);
    center.add(lower);
    center.scale(0.5f);
    
    diff = new Vector3f(upper);
    diff.sub(lower);
  }
  
  public float getWidth() {
    return diff.x;
  }
  
  public float getHeight() {
    return diff.y;
  }
  
  public float getDepth() {
    return diff.z;
  }

  /**
   * @return the upper
   */
  public Point3f getUpper() {
    return upper;
  }

  /**
   * @return the lower
   */
  public Point3f getLower() {
    return lower;
  }

  /**
   * @return the center
   */
  public Point3f getCenter() {
    return center;
  }
  
  public String toString() {
    return "Box: upper = " + upper + ", lower = " + lower;
  }
}
