package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;

/**
 * Util class for OGL methods
 *  
 * @author Eric Tatara
 *
 */
public class RenderUtils {

	/**
	 * Called before rendering an object
	 * @param dc
	 */
	public static void begin(DrawContext dc){
		GL gl = dc.getGL();
		Vec4 cameraPosition = dc.getView().getEyePoint();

		gl.glPushAttrib(GL.GL_TEXTURE_BIT | 
				            GL.GL_ENABLE_BIT | 
				            GL.GL_CURRENT_BIT |
				            GL.GL_LIGHTING_BIT | 
				            GL.GL_TRANSFORM_BIT);
		
		gl.glDisable(GL.GL_TEXTURE_2D);

		float[] lightPosition =	{(float) (cameraPosition.x * 2), 
				                     (float) (cameraPosition.y / 2), 
				                     (float) (cameraPosition.z), 0.0f};
		
		float[] lightDiffuse  = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightAmbient  = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};

		gl.glDisable(GL.GL_COLOR_MATERIAL);

		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE,  lightDiffuse,  0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT,  lightAmbient,  0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightSpecular, 0);

		gl.glDisable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHT1);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_NORMALIZE);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
	}

	/**
	 * Called after rendering an object
	 * @param dc
	 */
	public static void end(DrawContext dc) {
		GL gl = dc.getGL();

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();

		gl.glDisable(GL.GL_LIGHT1);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_NORMALIZE);
		gl.glPopAttrib();
	}
}
