package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * A cone point shape.
 * 
 * @author Eric Tatara
 *
 */
public class ConeShape extends PointShape {
	private float radius;
	private int slices;
	private int rings;
	private float height;

	/**
	 * Create a cone.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of slices.
	 * @param rings the number of rings.
	 */
	public ConeShape(float radius, float height, int slices, int rings){
		this.radius = radius;
		this.height = height;
		this.slices = slices;
		this.rings = rings;
		this.size = radius;
	}
	
	@Override
	protected void initialize(DrawContext dc){
		glListId = dc.getGL().glGenLists(1);

		GLU glu = dc.getGLU();
		GL gl = dc.getGL();
		GLUquadric quad = glu.gluNewQuadric();
		
		gl.glNewList(glListId, GL.GL_COMPILE);
		// tube (no ends)
		glu.gluCylinder(quad, radius, 0, height, slices, rings);
		// bottom disc
		glu.gluDisk(glu.gluNewQuadric(), radius, 0, slices, rings);
		gl.glEndList();
		glu.gluDeleteQuadric(quad);
		
		initialized = true;
	}
}