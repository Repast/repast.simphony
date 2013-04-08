package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * A cylinder point shape.
 * 
 * @author Eric Tatara
 *
 */
public class CylinderShape extends PointShape {
	private float radius;
	private int slices;
	private int rings;
	private float height;

	/**
	 * Create a cylinder.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of slices.
	 * @param rings the number of rings.
	 */
	public CylinderShape(float radius, float height, int slices, int rings){
		this.radius = radius;
		this.height = height;
		this.slices = slices;
		this.rings = rings;
		this.size = radius;
	}
	
	@Override
	protected void initialize(DrawContext dc){
		glListId = dc.getGL().getGL2().glGenLists(1);

		GLU glu = dc.getGLU();
		GL2 gl = dc.getGL().getGL2();
		GLUquadric quad = glu.gluNewQuadric();
		
		gl.glNewList(glListId, GL2.GL_COMPILE);
		// tube (no ends)
		glu.gluCylinder(quad, radius, radius, height, slices, rings);
		// bottom disc
		glu.gluDisk(quad, radius, 0, slices, rings);
		// top disc
		gl.glTranslatef(0, 0, height);
		glu.gluDisk(quad, radius, 0, slices, rings);
		gl.glEndList();
		glu.gluDeleteQuadric(quad);
		
		initialized = true;
	}
}