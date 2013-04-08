package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * Util class for OGL methods
 * 
 * @author Eric Tatara
 * 
 */
public class RenderUtils {

  /**
   * Called before rendering an object
   * 
   * @param dc
   */
  public static void begin(DrawContext dc) {
    GL2 gl = dc.getGL().getGL2();
    Vec4 cameraPosition = dc.getView().getEyePoint();

    gl.glPushAttrib(GL2.GL_TEXTURE_BIT | GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT
        | GL2.GL_LIGHTING_BIT | GL2.GL_TRANSFORM_BIT);

    gl.glDisable(GL.GL_TEXTURE_2D);

    float[] lightPosition = { (float) (cameraPosition.x * 2), (float) (cameraPosition.y / 2),
        (float) (cameraPosition.z), 0.0f };

    float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
    float[] lightAmbient = { 1.0f, 1.0f, 1.0f, 1.0f };
    float[] lightSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };

    gl.glDisable(GL2.GL_COLOR_MATERIAL);

    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition, 0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse, 0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient, 0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightSpecular, 0);

    gl.glDisable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_LIGHT1);
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_NORMALIZE);

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glPushMatrix();
  }

  /**
   * Called after rendering an object
   * 
   * @param dc
   */
  public static void end(DrawContext dc) {
    GL2 gl = dc.getGL().getGL2();

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glPopMatrix();

    gl.glDisable(GL2.GL_LIGHT1);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glDisable(GL2.GL_LIGHTING);
    gl.glDisable(GL2.GL_NORMALIZE);
    gl.glPopAttrib();
  }
}
