package repast.simphony.visualization.network;

import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import repast.simphony.visualization.visualization3D.Label;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * 
 */
public class DirectedEdgeVisualItem extends EdgeVisualItem {
  
  protected Transform3D arrowTranslate = new Transform3D();
  protected TransformGroup arrowTransform = new TransformGroup();
  protected float arrowHeight;
  protected boolean applyUpdate = false;
	protected Vector3f transVec = new Vector3f();
  
  public DirectedEdgeVisualItem(TaggedBranchGroup tGroup, Object o, Label label, EdgeStyle3D style) {
    super(tGroup, o, label);
    
    float radius = style.edgeRadius(o);
    arrowHeight = 4 * radius;
    float arrowRadius = 2 * radius;
	
    Shape3D shape = ShapeFactory.createCone(arrowRadius, arrowHeight, "_ARROW_HEAD_");
    TaggedAppearance appearance = style.getAppearance(o, null, "_ARROW_HEAD_");
    shapes.put(shape, appearance);
    shape.setAppearance(appearance.getAppearance());
    arrowTransform.addChild(shape);
	  transVec.set(0, .5f ,0);
    arrowTranslate.setTranslation(transVec);
    arrowTransform.setTransform(arrowTranslate);
    arrowTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    arrowTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	  rotationGroup.addChild(arrowTransform);
  }
  
  public void updateLocation(Point3f source, float sourceRadius, Point3f target, 
  		float targetRadius) {
    
  	// note that the radius of the target bounding sphere is adjusted for the
  	// arrow height.
  	calculateEndPoints(source, sourceRadius, target, targetRadius + arrowHeight/2);	
  	
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