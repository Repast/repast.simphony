package repast.simphony.space.delaunay;


/**
 * This adds geometric primitives to ILocations to support
 * DelaunayTriangulation. These primitives are defined in guibas and stolfi.
 * 
 * @author Howe
 * @version $revision$
 */
public class TriangulationPoint  {
	private static final long serialVersionUID = 3258416131596695094L;

	double x, y; // point coordinates

	boolean isInf = false;
        public Object contents;

	/**
	 * Construct a new TriangulationPoint using an x and a y cooridnate.
	 * 
	 * @param x
	 *            The x coordinate of the point.
	 * @param y
	 *            The y coordinate of the point.
	 */
	public TriangulationPoint(double x, double y, Object contents) {
		this.x = x;
		this.y = y;
                this.contents = contents;
	}

	/**
	 * Construct a new TriangulationPoint using an ILocation object.
	 * 
	 * @param loc
	 *            The location to be triangulated.
	 */
	/*public TriangulationPoint(Location<?> loc) {
		x = (int) loc.getCoords()[0];
		y = (int) loc.getCoords()[1];
                this.contents = loc;
	}*/
        
        public Object getContents(){
            return contents;
        }

	/**
	 * Is this point on the supplied edge?
	 * 
	 * @param e
	 *            The edge to test.
	 * @return true if this point is on the edge.
	 */
	public boolean isOn(QuadEdge e) {
		TriangulationPoint pts = e.getOrg();
		TriangulationPoint pte = e.getDest();
		return (Area(pts, this, pte) == 0);
	}

	/**
	 * Is this point to the right of the supplied Edge.
	 * 
	 * @param e
	 *            The edge to test.
	 * @return true if this point is right of the edge.
	 */
	public boolean isRightOf(QuadEdge e) {
		TriangulationPoint pts = e.getOrg();
		TriangulationPoint pte = e.getDest();

		return (Area(this, pts, pte) > 0);
	}

	/**
	 * Is this point to the left of the supplied Edge.
	 * 
	 * @param e
	 *            The edge to test.
	 * @return true if this point is left of the edge.
	 */
	public boolean isLeftOf(QuadEdge e) {
		TriangulationPoint pts = e.getOrg();
		TriangulationPoint pte = e.getDest();
		return (Area(this, pts, pte) < 0);
	}

	// Computes the sign of the orientable area. Used to determine whether
	// a point is to the left, right, or on an give edge.
	private double Area(TriangulationPoint p1, TriangulationPoint p2,
			TriangulationPoint p3) {
		return ((p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y));
	}

	/**
	 * Tests whether this point is inside the circumcircle of the other three
	 * points.
	 * 
	 * @param p1
	 *            A point in the circle
	 * @param p2
	 *            A point in the circle
	 * @param p3
	 *            A point in the circle.
	 * @return true if this point lies incide the circumcircle of the other
	 *         three points.
	 */
	public boolean isInCircle(TriangulationPoint p1, TriangulationPoint p2,
			TriangulationPoint p3) {
		long x4 = (long) this.x;
		long y4 = (long) this.y;
		long x1 = (long) p1.x;
		long y1 = (long) p1.y;
		long x2 = (long) p2.x;
		long y2 = (long) p2.y;
		long x3 = (long) p3.x;
		long y3 = (long) p3.y;

		long a = (x2 - x1) * (y3 - y1)
				* (x4 * x4 + y4 * y4 - x1 * x1 - y1 * y1) + (x3 - x1)
				* (y4 - y1) * (x2 * x2 + y2 * y2 - x1 * x1 - y1 * y1)
				+ (x4 - x1) * (y2 - y1)
				* (x3 * x3 + y3 * y3 - x1 * x1 - y1 * y1) - (x2 - x1)
				* (y4 - y1) * (x3 * x3 + y3 * y3 - x1 * x1 - y1 * y1)
				- (x3 - x1) * (y2 - y1)
				* (x4 * x4 + y4 * y4 - x1 * x1 - y1 * y1) - (x4 - x1)
				* (y3 - y1) * (x2 * x2 + y2 * y2 - x1 * x1 - y1 * y1);

		return (a > 0);
	}

	/**
	 * @return Returns the x.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}
}