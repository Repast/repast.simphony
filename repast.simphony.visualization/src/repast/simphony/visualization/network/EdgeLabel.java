package repast.simphony.visualization.network;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import repast.simphony.random.RandomHelper;
import repast.simphony.visualization.visualization3D.AbstractLabel;
import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * @author Nick Collier
 */
public class EdgeLabel extends AbstractLabel {

  Point3f randomOffset = new Point3f();

  protected Vector3f getTranslation(Point3f center, float offset) {
    // NORTH, SOUTH, EAST, WEST, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST, ON_TOP_OF
    float tmp = offset;
    randomOffset.set(RandomHelper.getUniform().nextFloatFromTo(-tmp, tmp), RandomHelper.getUniform().nextFloatFromTo(-tmp, tmp),
		    RandomHelper.getUniform().nextFloatFromTo(-tmp, tmp));
    Vector3f trans;
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
      // default to ON TOP OF
      trans = new Vector3f(-(textWidth / 2), 0, offset);
    }

    trans.add(center);
    trans.add(randomOffset);
    return trans;
  }
}
