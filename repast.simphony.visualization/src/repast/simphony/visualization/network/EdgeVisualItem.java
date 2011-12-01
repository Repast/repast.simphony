package repast.simphony.visualization.network;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import repast.simphony.visualization.visualization3D.Label;
import repast.simphony.visualization.visualization3D.VisualItem3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * 
 */
public class EdgeVisualItem extends VisualItem3D {

	private static final Vector3f init = new Vector3f(0.0f, 1.0f, 0.0f);
	protected AxisAngle4f rotation = new AxisAngle4f();
	protected float height;
	Vector3f axis = new Vector3f();
	Vector3f needed = new Vector3f();
	Point3f midpoint;
	Point3f Ps = new Point3f();  // point on the source bounding sphere
	Point3f Pt = new Point3f();  // point on the target bounding sphere

	public EdgeVisualItem(TaggedBranchGroup tGroup, Object o, Label label) {
		super(tGroup, o, label);
	}

	protected void doUpdateLocation(Point3f source, Point3f target) {
		// scale the item to this height, assume that item is unit sized
		setScale(1, height, 1);

		// set the rotation
		needed.sub(target, source);

		// To specify the rotation, we want an AxisAngle4f.  This specifies the amount to 
		// rotate and the vector to rotate around.  We can calculate these two quantities 
		// from the vector utilities:
		float angle = needed.angle(init);
		axis.cross(init, needed);
		rotation.set(axis.x, axis.y, axis.z, angle);
		setRotation(rotation);
	}

	/**
	 * returns the point on a sphere centered at P1 with radius r that intersects
	 * the line P1-P2.
	 * 
	 * @param P1 the center of the sphere and line origin
	 * @param P2 the second point of the line
	 * @param r the radius of the sphere
	 * @return the point where the line and sphere intersects
	 */
	private Point3f findLineSphereIntersect(Point3f P1, Point3f P2, float r){

		/* use the general equations of a 3D line and sphere:
		 *  
		 *  (x-x1)^2 + (y-y1)^2 + (z-z1)^2 = r^2
		 *  
		 *  x-x1    y-y1    z-z1
		 *  ----- = ----- = -----
		 *  x2-x1   y2-y1   z2-z1
		 *
		 * to solve for x and then solve the quadratic.  This will provide two
		 * possible solutions for the intersection of the line and sphere.
		 */
		
		float[] dc = new float[3];
		float[] p1 = new float[3];
		float[] p2 = new float[3];
		p1[0] = P1.x;
		p1[1] = P1.y;
		p1[2] = P1.z;
		p2[0] = P2.x;
		p2[1] = P2.y;
		p2[2] = P2.z;
		
		dc[0] = p2[0] - p1[0];
		dc[1] = p2[1] - p1[1];
		dc[2] = p2[2] - p1[2];
		
		// Find the largest absolute value of the denomenator for the line equalities
		// since we don't want to divide by zero if a coordinate delta is zero.
		float dcm = dc[0];
		int dcmi = 0;
		
		if (Math.abs(dc[0]) < Math.abs(dc[1])){
			dcm = dc[1];
			dcmi = 1;
    }
		if (Math.abs(dc[2]) > Math.abs(dcm)){
			dcm = dc[2];
			dcmi = 2;
		}
		
		float A = 1;
		
		for (int i=0; i<=2; i++){
			if (i != dcmi){
				A += (dc[i]/dcm) * (dc[i]/dcm);
			}
		}
		// quadratic coefficients
		float a = 1;
		float b = -2 * p1[dcmi];
		float c = p1[dcmi] * p1[dcmi] - r * r / A;

		float disc = b*b - 4*a*c; // discriminant
		
		float[] pplus = new float[3];
		float[] pminus = new float[3];
		
		pminus[dcmi] = (-b - (float)Math.sqrt(disc)) / 2;
		pplus[dcmi] = (-b + (float)Math.sqrt(disc)) / 2;
		
		for (int i=0; i<=2; i++){
			if (i != dcmi){
		    pminus[i] = dc[i]/dcm * (pminus[dcmi] - p1[dcmi]) + p1[i];
		    pplus[i] = dc[i]/dcm * (pplus[dcmi] - p1[dcmi]) + p1[i];
			}
		}
		
		Point3f Pplus = new Point3f(pplus[0], pplus[1], pplus[2]);
		Point3f Pminus = new Point3f(pminus[0], pminus[1], pminus[2]);
		
		// Check which solution is closer to P2 as this will be the point on the 
		// sphere from P1.
		if (P2.distance(Pplus) < P2.distance(Pminus))
			return Pplus;
		else 
			return Pminus;
	}
	
	/**
	 * First find the points at which the edge will intersect the bounding
	 *  spheres.  The distance between these points will be the edge length.
	 *  
	 * @param source
	 * @param sourceRadius
	 * @param target
	 * @param targetRadius
	 */
	protected void calculateEndPoints(Point3f source, float sourceRadius, 
			Point3f target, float targetRadius){

		midpoint = new Point3f();
		Point3f sum = new Point3f();

		// First check that the sum of the radii is smaller than the distance 
		// between points, since otherwise the edge would have negative length.
		if (source.distance(target) > sourceRadius + targetRadius){
			Ps = findLineSphereIntersect(source, target, sourceRadius);
			Pt = findLineSphereIntersect(target, source, targetRadius);

			// length of edge
			height = Ps.distance(Pt);
			sum.add(Ps,Pt);
		}
		else{
			height = 0;
			sum.add(target,source);
		}
		midpoint.scale(0.5f, sum);
		setLocation(midpoint);
	}

	public void updateLocation(Point3f source, float sourceRadius, Point3f target, 
			float targetRadius) {

		calculateEndPoints(source, sourceRadius, target, targetRadius);

		doUpdateLocation(source, target);
	}

	@Override
	public void updateScale(Style3D style) {
		float[] point = style.getScale(visualizedObject);
		if (point != null)
			setScale(point[0], height, point[2]);
	}
}