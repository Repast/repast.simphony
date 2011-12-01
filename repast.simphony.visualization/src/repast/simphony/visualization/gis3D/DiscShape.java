package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * A disc point shape.
 * 
 * @author Eric Tatara
 *
 */
public class DiscShape extends PointShape {
	private float radius;
	private int slices;
	private int rings;
  private float innerRadius;

  /**
   * Create a disc.
   * 
   * @param outerRadius the outer radius.
   * @param innerRadius the inner radius.
   * @param slices the number of slices.
   * @param rings the number of rings.
   */
	public DiscShape(float outerRadius, float innerRadius, int slices, int rings){
		this.radius = outerRadius;
		this.innerRadius = innerRadius;
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
		glu.gluDisk(quad, radius, innerRadius, slices, rings);
		gl.glEndList();
		glu.gluDeleteQuadric(quad);
		
		initialized = true;
	}
}