package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A Sphere point shape.
 * 
 * @author Eric Tatara
 * 
 */
public class SphereShape extends PointShape {
  private int slices;
  private int rings;
  private float radius;

  /**
   * Create a sphere shape.
   * 
   * @param radius
   *          the radius of the sphere.
   * @param slices
   *          the number of slices.
   * @param rings
   *          the number of rings.
   */
  public SphereShape(float radius, int slices, int rings) {
    this.radius = radius;
    this.slices = slices;
    this.rings = rings;
    this.size = radius;
  }

  public SphereShape(Geometry geometry) {
    super(geometry);
  }

  @Override
  protected void initialize(DrawContext dc) {
    glListId = dc.getGL().getGL2().glGenLists(1);

    GLU glu = dc.getGLU();
    GL2 gl = dc.getGL().getGL2();
    GLUquadric quad = glu.gluNewQuadric();

    gl.glNewList(glListId, GL2.GL_COMPILE);
    glu.gluSphere(quad, radius, slices, rings);
    gl.glEndList();
    glu.gluDeleteQuadric(quad);

    initialized = true;
  }

  public Geometry getGeometry() {
    return this.geometry;
  }

  public void setRadius(int i) {
    this.radius = i;
  }
}