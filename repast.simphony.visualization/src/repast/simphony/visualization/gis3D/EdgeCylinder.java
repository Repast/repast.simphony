package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EdgeCylinder extends RenderableShape {

	private double radius = 1;
	private Vec4 bottomCenter; 
	private Vec4 topCenter; 
	private GLUquadric quadric;

	private LatLon sourceLatLon;
	private LatLon targetLatLon;

	public EdgeCylinder(LatLon sourceLatLon, LatLon targetLatLon){  	
		this.sourceLatLon = sourceLatLon;
		this.targetLatLon = targetLatLon;
	}
	
	@Override
	protected void initialize(DrawContext dc){
		glListId = dc.getGL().glGenLists(1);
		quadric = dc.getGLU().gluNewQuadric();

		dc.getGLU().gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		dc.getGLU().gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
		dc.getGLU().gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
		dc.getGLU().gluQuadricTexture(quadric, false);

		int slices = 10;
		int stacks = 10;

		dc.getGL().glNewList(glListId, GL.GL_COMPILE);
		dc.getGLU().gluCylinder(quadric, 1d, 1d, 2d, slices, stacks);
		dc.getGL().glEndList();

		initialized = true;
	}

	@Override
	protected void doRender(DrawContext dc){
		double TO_DEGREES = 180d / Math.PI;		

		double sourceElevation = dc.getGlobe().getElevation(sourceLatLon.getLatitude(), 
				sourceLatLon.getLongitude());
		
		bottomCenter = dc.getGlobe().computePointFromPosition(sourceLatLon.getLatitude(), 
				sourceLatLon.getLongitude(), sourceElevation + RenderableShape.HEIGHT_OFFSET);
		
		double targetElevation = dc.getGlobe().getElevation(targetLatLon.getLatitude(), 
				targetLatLon.getLongitude());
		
		topCenter = dc.getGlobe().computePointFromPosition(targetLatLon.getLatitude(), 
				targetLatLon.getLongitude(), targetElevation + RenderableShape.HEIGHT_OFFSET);

		double length = this.bottomCenter.distanceTo3(this.topCenter);

		Vec4 p1 = bottomCenter;
		Vec4 p2 = topCenter;

		Vec4 u1 = new Vec4((p2.x - p1.x) / length, 
				               (p2.y - p1.y) / length, 
				               (p2.z - p1.z) / length);

		double angle = Math.acos(u1.z);

		// Compute the direction cosine factors that define the rotation axis
		double A = -u1.y;
		double B = u1.x;
		double L = Math.sqrt(A * A + B * B);

		Vec4 eye = dc.getView().getCurrentEyePoint();
		
		double d1 = eye.distanceTo3(p1);
		double d2 = eye.distanceTo3(p2);
		
		double scale = radius * Math.min(d1,d2) / ZOOM_SCALE_FACTOR;
		
		dc.getView().pushReferenceCenter(dc, p1);
		dc.getGL().glRotated(angle * TO_DEGREES, A / L, B / L, 0);
		dc.getGL().glScaled(scale, scale, length / 2); 

		dc.getGL().glCallList(glListId);
		dc.getView().popReferenceCenter(dc);
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setSourceLatLon(LatLon sourceLatLon) {
		this.sourceLatLon = sourceLatLon;
	}

	public void setTargetLatLon(LatLon targetLatLon) {
		this.targetLatLon = targetLatLon;
	}
}
