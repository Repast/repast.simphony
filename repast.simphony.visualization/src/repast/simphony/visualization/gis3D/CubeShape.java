package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;

/**
 * A cube point shape.
 * 
 * @author Eric Tatara
 *
 */
public class CubeShape extends PointShape {

	private static float[][] boxVertices;
  private static final float[][] boxNormals = {
    {-1.0f, 0.0f, 0.0f},
    {0.0f, 1.0f, 0.0f},
    {1.0f, 0.0f, 0.0f},
    {0.0f, -1.0f, 0.0f},
    {0.0f, 0.0f, 1.0f},
    {0.0f, 0.0f, -1.0f}
  };
  private static final int[][] boxFaces = {
    {0, 1, 2, 3},
    {3, 2, 6, 7},
    {7, 6, 5, 4},
    {4, 5, 1, 0},
    {5, 6, 2, 1},
    {7, 4, 0, 3}
  };
  
	public CubeShape(float size){
		this.size = size;
	}
	
	@Override
	protected void initialize(DrawContext dc){
		glListId = dc.getGL().glGenLists(1);

		GL gl = dc.getGL();
				
		// taken from GLUT.drawBox(GL gl, float size, int type) since we don't have
		// access to GLUT from the DrawContext.
		if (boxVertices == null) {
			float[][] v = new float[8][];
			for (int i = 0; i < 8; i++) {
				v[i] = new float[3];
			}
			v[0][0] = v[1][0] = v[2][0] = v[3][0] = -0.5f;
			v[4][0] = v[5][0] = v[6][0] = v[7][0] =  0.5f;
			v[0][1] = v[1][1] = v[4][1] = v[5][1] = -0.5f;
			v[2][1] = v[3][1] = v[6][1] = v[7][1] =  0.5f;
			v[0][2] = v[3][2] = v[4][2] = v[7][2] = -0.5f;
			v[1][2] = v[2][2] = v[5][2] = v[6][2] =  0.5f;
			boxVertices = v;
		}
		
    float[][] v = boxVertices;
    float[][] n = boxNormals;
    int[][] faces = boxFaces;
    dc.getGL().glNewList(glListId, GL.GL_COMPILE);
    for (int i = 5; i >= 0; i--) {
      gl.glBegin(GL.GL_QUADS);
      gl.glNormal3fv(n[i], 0);
      float[] vt = v[faces[i][0]];
      gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
      vt = v[faces[i][1]];
      gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
      vt = v[faces[i][2]];
      gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
      vt = v[faces[i][3]];
      gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
      gl.glEnd();
    }
    dc.getGL().glEndList();
		initialized = true;
	}
}