package repast.simphony.visualization.network;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import repast.simphony.visualization.visualization3D.Label;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:53:54 $
 */
public class DirectedLineEdgeVisualItem extends EdgeVisualItem {
  
  private Transform3D arrowTranslate = new Transform3D();
  private TransformGroup arrowTransform = new TransformGroup();
  private float arrowHeight;
  private boolean applyUpdate = false;
	private Vector3f transVec = new Vector3f();
  
  public DirectedLineEdgeVisualItem(TaggedBranchGroup tGroup, Object o, Label label, EdgeStyle3D style) {
    super(tGroup, o, label);
    
    float radius = style.edgeRadius(o);
    arrowHeight = 4 * radius;
	
    Shape3D shape = ShapeFactory.createArrowHead(arrowHeight, "_ARROW_HEAD_");
    TaggedAppearance appearance = style.getAppearance(o, null, "_ARROW_HEAD");
    shapes.put(shape, appearance);
    shape.setAppearance(appearance.getAppearance());
    arrowTransform.addChild(shape);
	  transVec.set(0, .5f - arrowHeight,0);
    arrowTranslate.setTranslation(transVec);
    arrowTransform.setTransform(arrowTranslate);
    arrowTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    arrowTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    rotationGroup.addChild(arrowTransform);
  }

	public void updateLocation(Point3f source, float sourceRadius, Point3f target, float targetRadius) {
    height = source.distance(target) - sourceRadius - targetRadius;
	  transVec.set(0, height / 2 + arrowHeight / 2, 0);
	  arrowTranslate.setTranslation(transVec);
    doUpdateLocation(source, target);
    applyUpdate = true;
  }

  public void applyTransform() {
    super.applyTransform(); 
    if (applyUpdate) {
      arrowTransform.setTransform(arrowTranslate);
      applyUpdate = false;
    }
  }
}
