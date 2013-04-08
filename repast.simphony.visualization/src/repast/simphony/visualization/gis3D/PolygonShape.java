package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import javax.media.opengl.glu.GLUtessellatorCallbackAdapter;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * @author Eric Tatara
 * 
 */
public class PolygonShape extends RenderableShape {

  private GLUtessellator tess;
  private static TessCallback tessCallback;
  private double[][] vertices;
  public static final double POLY_HEIGHT_OFFSET = 50000;

  // private MultiLine border;

  @Override
  protected void initialize(DrawContext dc) {
    tess = dc.getGLU().gluNewTess();
    initialized = true;
    // border = new MultiLine();
    // border.setGeometry(geometry);
    // border.setColor(Color.CYAN);
  }

  @Override
  protected void doRender(DrawContext dc) {
    GL2 gl = dc.getGL().getGL2();
    GLU glu = dc.getGLU();

    if (tess == null || geometry == null)
      return;

    if (tessCallback == null)
      tessCallback = new TessCallback(gl, glu);

    int numPts = geometry.getCoordinates().length;
    vertices = new double[numPts][3];
    Vec4 origin = null;

    int i = 0;
    for (Coordinate coord : geometry.getCoordinates()) {
      LatLon latlon = WWUtils.CoordToLatLon(coord);

      double elevation = dc.getGlobe().getElevation(latlon.getLatitude(), latlon.getLongitude());

      Vec4 pt = dc.getGlobe().computePointFromPosition(latlon.getLatitude(), latlon.getLongitude(),
          elevation + POLY_HEIGHT_OFFSET);

      // TODO FIX!!!
      if (i == 0) {
        origin = pt;
      }

      vertices[i] = pt.subtract3(origin).toArray3(new double[3], 0);

      i++;
    }

    // ***** start actual rendering code *****

    // turn off lighting for polygons.
    gl.glDisable(GL2.GL_LIGHTING);
    gl.glDisable(GL2.GL_NORMALIZE);

    GLU.gluTessCallback(tess, GLU.GLU_TESS_VERTEX, tessCallback);
    GLU.gluTessCallback(tess, GLU.GLU_TESS_BEGIN, tessCallback);
    GLU.gluTessCallback(tess, GLU.GLU_TESS_END, tessCallback);
    GLU.gluTessCallback(tess, GLU.GLU_TESS_ERROR, tessCallback);
    GLU.gluTessCallback(tess, GLU.GLU_TESS_COMBINE, tessCallback);

    // gl.glShadeModel(GL.GL_SMOOTH);
    // glu.gluTessProperty(tess, GLU.GLU_TESS_WINDING_RULE,
    // GLU.GLU_TESS_WINDING_POSITIVE);

    dc.getView().pushReferenceCenter(dc, origin);

    if (dc.isPickingMode()) {

    } else {
      gl.glLineWidth(1.0f);

      Color color = new Color(1, 0, 0, 0.75f);

      // TODO get color
      dc.getGL()
          .getGL2()
          .glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(),
              (byte) color.getAlpha());
    }

    glu.gluTessBeginPolygon(tess, null);
    glu.gluTessBeginContour(tess);
    for (int n = 0; n < numPts; n++)
      glu.gluTessVertex(tess, vertices[n], 0, vertices[n]);

    glu.gluTessEndContour(tess);
    glu.gluTessEndPolygon(tess);

    // draw the outline
    // gl.glLineWidth(2.0f);
    // gl.glBegin (GL.GL_LINE_STRIP);
    // for (int n=0; n<numPts; n++)
    // gl.glVertex3dv(vertices[n],0);
    // gl.glEnd();

    dc.getView().popReferenceCenter(dc);

    // glu.gluDeleteTess(tess);

    // gl.glEnable(GL.GL_POLYGON_OFFSET_FILL); // Avoid Stitching!
    // gl.glPolygonOffset(1.0f, 1.0f);
    // gl.glCallList(glListId);
    // gl.glDisable(GL.GL_POLYGON_OFFSET_FILL);
    // gl.glColor3f(0,1,0); // Color for your polygon border
    // gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
    // gl.glCallList(glListId); // Call the same rendering routine for the
    // previous polygon.
    // gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

    // border.render(dc);
  }

  public static class TessCallback extends GLUtessellatorCallbackAdapter {
    GL2 gl;
    GLU glu;

    public TessCallback(GL gl, GLU glu) {
      this.gl = gl.getGL2();
      this.glu = glu;
    };

    public void begin(int type) {
      gl.glBegin(type);
    }

    public void end() {
      gl.glEnd();
    }

    public void vertex(Object vertexData) {
      gl.glVertex3dv((double[]) vertexData, 0);
    }

    public void error(int errnum) {
      String estring = glu.gluErrorString(errnum);
      System.out.println("Tessellation Error: " + estring);
      throw new RuntimeException();
    }

    public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
      double[] vertex = new double[3];

      vertex[0] = coords[0];
      vertex[1] = coords[1];
      vertex[2] = coords[2];
      outData[0] = vertex;
    }
  }
}