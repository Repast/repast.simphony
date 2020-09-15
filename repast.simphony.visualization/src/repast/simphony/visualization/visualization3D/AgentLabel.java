package repast.simphony.visualization.visualization3D;

import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:53:54 $
 */
public class AgentLabel extends AbstractLabel {

  protected Vector3f getTranslation(Point3f center, float offset) {
    // NORTH, SOUTH, EAST, WEST, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST, ON_TOP_OF
    Vector3f trans = null;
    if (position == Style3D.LabelPosition.NORTH) {
      trans = new Vector3f(- (textWidth / 2), offset, 0);

    } else if (position == Style3D.LabelPosition.SOUTH) {
      trans = new Vector3f(- (textWidth / 2), -offset, 0);

    } else if (position == Style3D.LabelPosition.EAST) {
      trans = new Vector3f(- (textWidth + offset), 0, 0);

    } else if (position == Style3D.LabelPosition.WEST) {
      trans = new Vector3f((textWidth + offset), 0, 0);

    } else if (position == Style3D.LabelPosition.ON_TOP_OF) {
      trans = new Vector3f(-(textWidth / 2), 0, offset);

    } else {
      // default to NORTH
      trans = new Vector3f(-(textWidth / 2), offset, 0);
    }
    trans.add(center);
    return trans;
  }
}
