/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import saf.v3d.picking.Accumulator;
import saf.v3d.picking.BoundingSphere;
import saf.v3d.picking.DefaultAccumulator;
import saf.v3d.render.RenderState;
import saf.v3d.render.Texture2D;
import saf.v3d.scene.TextureRenderable;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VSpatial;

/**
 * Wraps the VSpatial produced by a style. This allows us to avoid edge etc.
 * updates when a VSpatial is changed by the style.
 * 
 * @author Nick Collier
 */
public class SpatialWrapper extends VComposite implements TextureRenderable {
  
  private VSpatial nextChild;

  public SpatialWrapper(VSpatial child) {
    children = new ArrayList<VSpatial>();
    setChild(child);
  }

  public void setNextChild(VSpatial child) {
    nextChild = child;
  }
  
  public void updateChild() {
    setChild(nextChild);
    nextChild = null;
  }

  /* (non-Javadoc)
   * @see saf.v3d.scene.TextureRenderable#getTextureData()
   */
  public Texture2D getTextureData() {
    return ((TextureRenderable)getChild()).getTextureData();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see saf.v3d.scene.VSpatial#transform(javax.vecmath.Point3f)
   */
  @Override
  public Point3f transform(Point3f pt) {
    return getChild().transform(pt);
  }

  /**
   * Gets the VSpatial that this wraps.
   * 
   * @return the VSpatial that this wraps.
   */
  public VSpatial getChild() {
    return ((ArrayList<VSpatial>) children).get(0);
  }

  /**
   * Sets the VSpatial that this wraps.
   * 
   * @param child
   */
  public void setChild(VSpatial child) {
    if (children.size() > 0) child.translate(getChild().getLocalTranslation());
    this.removeAllChildren();
    addChild(child);
  }
  
  public float getLocalScale() {
    return getChild().getLocalScale();
  }

  /*
   * (non-Javadoc)
   * 
   * @see saf.v3d.scene.VSpatial#draw(javax.media.opengl.GL,
   * saf.v3d.render.RenderState)
   */
  @Override
  public void draw(GL2 gl, RenderState state) {
    getChild().draw(gl, state);
  }

  /*
   * (non-Javadoc)
   * 
   * @see anl.mifs.viz3d.VSpatial#updateWorldTransformation()
   */
  @Override
  public void updateWorldTransformation() {
    // super.updateWorldTransformation();
    VSpatial child = getChild();
    VComposite parent = getParent();
    // switch the parents as the updateWorldTransform uses parents transform
    // info
    // and this is just wrapper so we don't want to do any work on it.
    child.setParent(parent);
    getChild().updateWorldTransformation();
    child.setParent(this);
  }

  @Override
  protected BoundingSphere doGetBoundingSphere() {
    if (boundsDirty) {
      VSpatial child = getChild();
      Quat4f rotation = new Quat4f();
      rotation.set(child.getWorldRotation());
      rotation.normalize();
      boundingSphere = new BoundingSphere(child.getLocalBoundingSphere());
      boundingSphere.transform(child.getWorldScale(), rotation, child.getWorldTranslation());
    }
    return boundingSphere;
  }

  public BoundingSphere getLocalBoundingSphere() {
    return getChild().getLocalBoundingSphere();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * anl.mifs.viz3d.VSpatial#intersects(javax.vecmath.Point3f,anl.mifs.viz3d
   * .Accumulator)
   */
  @Override
  public void intersects(Point3f point, Accumulator accumulator) {
    // point is in world coordinates, so first check if the point intersects
    // with
    // the BoundingSphere if not, then do nothing, if it does then pass to
    // children.
    BoundingSphere sphere = getBoundingSphere();
    if (sphere.intersects(point)) {
      DefaultAccumulator tmp = new DefaultAccumulator();
      getChild().intersects(point, tmp);
      if (tmp.size() > 0) accumulator.add(this);
    }
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see anl.mifs.viz3d.VSpatial#intersects(javax.vecmath.Point3f,
   * javax.vecmath.Vector3f, anl.mifs.viz3d.Accumulator)
   */
  @Override
  public void intersects(Point3f rayOrigin, Vector3f rayDirection, Accumulator accumulator) {
    // ray is in world coordinates, so first check if the ray intersects with
    // the BoundingSphere if not, then do nothing, if it does then pass to
    // children.
    BoundingSphere sphere = getBoundingSphere();
    if (sphere.intersects(rayOrigin, rayDirection)) {
      DefaultAccumulator tmp = new DefaultAccumulator();
      getChild().intersects(rayOrigin, rayDirection, accumulator);
      if (tmp.size() > 0) accumulator.add(this);
    }
  }
}
