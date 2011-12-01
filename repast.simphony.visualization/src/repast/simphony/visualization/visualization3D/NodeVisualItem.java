package repast.simphony.visualization.visualization3D;

import repast.simphony.visualization.Layout;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class NodeVisualItem extends VisualItem3D {

  public NodeVisualItem(TaggedBranchGroup tGroup, Object o, Label label) {
    super(tGroup, o, label);
  }

  public void updateLocation(Layout layout) {
    float[] location = layout.getLocation(visualizedObject);
    setLocation(location);
  }
}
