package repast.simphony.space.delaunay;

import java.io.Serializable;

/**
 * This is a representation of the QuadEdge data structure. The quadEdge
 * structure as described in guibas and stolfi on page 92, represents a
 * subdivision of space and its dual.
 * 
 * @author Howe
 * @version $revision$
 */
public class QuadEdge  {
	private static final long serialVersionUID = 3258416131596695094L;

	TriangulationPoint org = null; // Origin

	QuadEdge oNext = null; // next counterclockwise edge

	QuadEdge rot = null; // dual edge

	/**
	 * Set the origin of the edge.
	 * 
	 * @param pt
	 *            The origin of the edge.
	 */
	public void setOrg(TriangulationPoint pt) {
		org = pt;
	}

	/**
	 * Set the destination of this edge.
	 * 
	 * @param pt
	 *            The destination of the edge.
	 */
	public void setDest(TriangulationPoint pt) {
		getSym().org = pt;
	}

	/**
	 * The dual (perpendicular bisector) of this edge.
	 * 
	 * @param e
	 *            The dual of this edge.
	 */
	public void setRot(QuadEdge e) {
		rot = e;
	}

	/**
	 * The edge with opposite orientation to this one, i.e. the origin of this
	 * edge is the destination of the other edge and the destination of this
	 * edge is the origin of the other edge.
	 * 
	 * @param e
	 *            The symmetric of this edge.
	 */
	public void setSym(QuadEdge e) {
		rot.rot = e;
	}

	/**
	 * The result of applying the rot operator 3 times.
	 * 
	 * @param e
	 *            The other edge.
	 */
	public void setRot3(QuadEdge e) {
		rot.rot.rot = e;
	}

	public void setONext(QuadEdge e) {
		oNext = e;
	}

	public void setLNext(QuadEdge e) {
		getRot3().oNext.rot = e;
	}

	public void setOPrev(QuadEdge e) {
		rot.oNext.rot = e;
	}

	public void setRNext(QuadEdge e) {
		QuadEdge rn = rot.oNext.getRot3();
		rn = e;
	}

	// getting information
	public TriangulationPoint getOrg() {
		return org;
	}

	public TriangulationPoint getDest() {
		return getSym().org;
	}

	public QuadEdge getRot() {
		return rot;
	}

	public QuadEdge getSym() {
		return rot.rot;
	}

	public QuadEdge getRot3() {
		return rot.rot.rot;
	}

	public QuadEdge getONext() {
		return oNext;
	}

	public QuadEdge getLNext() {
		return getRot3().oNext.rot;
	}

	public QuadEdge getOPrev() {
		return rot.oNext.rot;
	}

	public QuadEdge getRNext() {
		return rot.oNext.getRot3();
	}

	public QuadEdge getDPrev() {
		return getRot3().oNext.getRot3();
	}

	public QuadEdge getLPrev() {
		return oNext.getSym();
	}

	/**
	 * Determine if this edge is one of the inifinite edges.  If so, this is not
	 * a valid edge.
	 * 
	 * @return true if neither org nor dest are infinite.
	 */
	public boolean isValidEdge() {
		TriangulationPoint og = getOrg();
		TriangulationPoint dt = getDest();

		if (og == null || og.isInf || dt == null || dt.isInf)
			return false;
		else
			return true;
	}
}