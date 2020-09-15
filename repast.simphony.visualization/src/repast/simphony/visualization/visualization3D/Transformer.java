package repast.simphony.visualization.visualization3D;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class Transformer {

  protected Transform3D translate = new Transform3D();
  protected Transform3D scaling = new Transform3D();
  protected Transform3D rotation = new Transform3D();
  protected Transform3D rotX = new Transform3D();
  protected Transform3D rotY = new Transform3D();
  protected Transform3D rotZ = new Transform3D();
  protected boolean locationChanged, scaleChanged, rotationChanged, rotXChanged, rotYChanged, rotZChanged;

  public void setTranslation(Point3f translation) {
    locationChanged = true;
    translate.setTranslation(new Vector3f(translation));
  }

  public void setScale(float x, float y, float z) {
    Vector3d scale = new Vector3d();
    scaling.getScale(scale);
    if (scale.x != x || scale.y != y || scale.z != z ) {
      scaling.setScale(new Vector3d(x, y, z));
      scaleChanged = true;
    }
  }

  public void setRotationX(double angle) {
    //rotX.setIdentity();
    rotX.rotX(angle);
    rotXChanged = true;
  }

  public void setRotationY(double angle) {
    //rotY.setIdentity();
    rotY.rotY(angle);
    rotYChanged = true;
  }

  public void setRotationZ(double angle) {
    //rotZ.setIdentity();
    rotZ.rotZ(angle);
    rotZChanged = true;
  }

  public void setRotation(AxisAngle4f rotation) {
    //this.rotation.setIdentity();
    this.rotation.setRotation(rotation);
    rotationChanged = true;
  }

  public void apply(TransformGroup translateGroup, TransformGroup rotationGroup, TransformGroup scaleGroup) {
    if (rotationChanged) rotationGroup.setTransform(rotation);
    if (scaleChanged) scaleGroup.setTransform(scaling);
    if (locationChanged) translateGroup.setTransform(translate);
    locationChanged = rotationChanged = scaleChanged = rotXChanged = rotYChanged = rotZChanged = false;
    
    /*
    trans.mul(rotation);
    trans.mul(scaling);
    trans.mul(rotX);
    trans.mul(rotY);
    trans.mul(rotZ);
    group.setTransform(trans);
    */
  }
}
