package repast.simphony.visualization.network;

import java.util.Map;
import java.util.Set;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Point3f;

import repast.simphony.visualization.visualization3D.Label;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class LineEdgeVisualItem3D extends EdgeVisualItem {
  
  protected Point3f[] coords = new Point3f[2];

  public LineEdgeVisualItem3D(TaggedBranchGroup tGroup, Object o, Label label) {
    super(tGroup, o, label);
  }

  public void updateRepresentation(Style3D style) {}

  public void applyTransform() {
    
    // the cast is necessary to keep the compiler from complaining but it shouldn't be 
    // necessary
    for (Map.Entry<Shape3D, TaggedAppearance> entry : (Set<Map.Entry<Shape3D, TaggedAppearance>>)changedMap.entrySet()) {
      entry.getKey().setAppearance(entry.getValue().getAppearance());
    }
    
    BranchGroup labelGroup = label.getBranchGroup(location, oldUserBranch);
    if (labelGroup != null) {
      labelGroup.compile();
      branchGroup.addChild(labelGroup);
    }
    
    changedMap.clear();
    
    GeometryArray geom = (GeometryArray) ((Shape3D)shapes().next()).getGeometry();
    for (int i = 0, n = coords.length; i < n; i++) {
      geom.setCoordinate(i, coords[i]);
    }
  }

	@Override
	public void updateLocation(Point3f source, float sourceRadius, Point3f target, float targetRadius) {
		coords[0] = source;
    coords[1] = target;
	}
}
