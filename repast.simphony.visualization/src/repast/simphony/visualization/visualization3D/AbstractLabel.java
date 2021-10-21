package repast.simphony.visualization.visualization3D;

import java.awt.Color;
import java.awt.Font;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Font3D;
import org.jogamp.java3d.FontExtrusion;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.OrientedShape3D;
import org.jogamp.java3d.Text3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:53:54 $
 */
public abstract class AbstractLabel implements Label {

	// TODO Workaround to fix a problem in jogl Font3D that uses a ServiceLoader with
	//      the default Java classloader instead of the Repast classloader.
	static {
		Thread.currentThread().setContextClassLoader(AbstractLabel.class.getClassLoader());
	}
	
  private static final float SCALE = 3800f;
  protected String label;
  protected Style3D.LabelPosition position;
  protected BranchGroup group;
  protected boolean updated = true;
  protected Color3f color = new Color3f(Color.YELLOW.getRGBColorComponents(null));
  protected Color jColor = new Color(color.getX(), color.getY(), color.getZ());
  protected Font3D font = new Font3D(new Font("Helvetica", Font.PLAIN, 10), new FontExtrusion());
  protected Appearance appearance = new Appearance();
  protected float textScale = font.getFont().getSize() / SCALE;
  protected float offset = 0;
  protected float textWidth;
  private Point3d textUpper = new Point3d();

  public AbstractLabel() {
    this("", Style3D.LabelPosition.NORTH);
  }

  public AbstractLabel(String label, Style3D.LabelPosition position) {
    this.label = label;
    this.position = position;
    group = new BranchGroup();
    group.setCapability(BranchGroup.ALLOW_DETACH);
    group.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    group.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    group.setPickable(false);
    appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
    appearance.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.SHADE_GOURAUD));
    appearance.getColoringAttributes().setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
  }

  public String getLabel() {
    return label;
  }

//  public void setLabel(String label) {
//    if ((this.label == null && label != null)
//            || (this.label != null && label == null)
//            || (this.label != null && !this.label.equals(label))) {
//      this.label = label;
//      updated = true;
//    }
//  }

  public void setLabel(String label) {
    if ((this.label == null && label != null)
            || (this.label != null && label == null)
            || (this.label != null )) {
      this.label = label;
      updated = true;
    }
  }
  
  public Style3D.LabelPosition getPosition() {
    return position;
  }

  public void setColor(Color color) {
    if (color != null && !color.equals(jColor)) {
      this.color.set(color.getColorComponents(null));
      this.jColor = color;
      appearance.getColoringAttributes().setColor(this.color);
    }
  }

  public Color getColor() {
    return jColor;
  }

  public void setFont(Font font) {
    if (font != null && !this.font.getFont().equals(font)) {
      this.font = new Font3D(font, new FontExtrusion());
      textScale = font.getSize() / SCALE;
      updated = true;
    }
  }

  public Font getFont() {
    return this.font.getFont();
  }

  public float getOffset() {
    return offset;
  }

  public void setOffset(float offset) {
    if (this.offset != offset) {
      this.offset = offset;
      updated = true;
    }
  }

  public void setPosition(Style3D.LabelPosition position) {
    if (this.position != position) {
      this.position = position;
      updated = true;
    }
  }
  
  protected abstract Vector3f getTranslation(Point3f center, float offset);

  public BranchGroup getBranchGroup(Point3f center, Group object) {
    if (updated) {
      group.detach();
      group.removeAllChildren();
	    updated = false;

      if (label == null || label.trim().length() == 0) {
        return group;
      }

      float radius = offset;
      if (offset == 0) {
        Bounds bounds = object.getBounds();
        if (bounds instanceof BoundingSphere) {
          radius = (float) ((BoundingSphere) bounds).getRadius();
        }
      }
      
      /*
      Color yellow = Color.YELLOW;
      Color3f yellow3f = new Color3f(yellow.getRed() / 255f, yellow.getGreen() / 255f, yellow.getBlue() / 255f);
      Text2D text = new Text2D(label, yellow3f, "Helvetica", 12, Font.PLAIN);
      BoundingBox box = (BoundingBox)text.getBounds();
      box.getUpper(textUpper);
      */
      
      Text3D text = new Text3D(font, label);
      BoundingBox box = new BoundingBox();
      text.getBoundingBox(box);
      box.getUpper(textUpper);
      textUpper.scale(textScale);
      textWidth = (float)textUpper.x;

      OrientedShape3D shape3D = new OrientedShape3D(text, appearance, OrientedShape3D.ROTATE_ABOUT_AXIS,
              new Vector3f(0, 1, 0));
     
      /*
      OrientedShape3D shape3D = new OrientedShape3D();
      Enumeration geoms = text.getAllGeometries();
      while(geoms.hasMoreElements()) {
        shape3D.addGeometry((Geometry) geoms.nextElement());
      }
      shape3D.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_AXIS);
      shape3D.setAlignmentAxis(0, 1, 0);
      */
      
      Transform3D transform = new Transform3D();
      transform.set(getTranslation(center, radius));
      transform.setScale(textScale);
      TransformGroup tGroup = new TransformGroup(transform);
      tGroup.addChild(shape3D);
      BranchGroup tmp = new BranchGroup();
      tmp.addChild(tGroup);
      tmp.setCapability(BranchGroup.ALLOW_DETACH);
      group.addChild(tmp);
      group.compile();
      return group;
    }

    return null;
  }
}
