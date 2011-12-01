package repast.simphony.visualization.visualization3D.layout;

/*
 *  Port from JUNG RadiusGraphElementAccessor.java v. 1.7.4
 * See http://jung.sourceforge.net/ for futher information
 */

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import repast.simphony.space.graph.RepastEdge;


/**
 * Returns the vertex or edge
 * that is closest to the specified location.  This implementation
 * provides the same picking options that were available in
 * previous versions of AbstractLayout.
 * 
 * See JUNG RadiusGraphElementAccessor for further details
 * @author M. Altaweel
 */
public class JungRadiusGraphElementAccessor implements JungGraphElementAccessor {
    
    protected JungNetworkLayout layout;
    protected double maxDistance;
    
    public JungRadiusGraphElementAccessor(JungNetworkLayout l) {
        this(l, Math.sqrt(Double.MAX_VALUE - 1000));
    }
    
    public JungRadiusGraphElementAccessor(JungNetworkLayout l, double maxDistance) {
        this.maxDistance = maxDistance;
        this.layout = l;
    }
    
	/**
	 * Gets the vertex nearest to the location of the (x,y) location selected,
	 * within a distance of <tt>maxDistance</tt>. Iterates through all
	 * visible vertices and checks their distance from the click. Override this
	 * method to provde a more efficient implementation.
	 */
	public Object getVertex(double x, double y) {
	    return getVertex(x, y, this.maxDistance);
	}

	/**
	 * Gets the vertex nearest to the location of the (x,y) location selected,
	 * within a distance of <tt>maxDistance</tt>. Iterates through all
	 * visible vertices and checks their distance from the click. Override this
	 * method to provde a more efficient implementation.
	 * @param x
	 * @param y
	 * @param maxDistance temporarily overrides member maxDistance
	 */
	public Object getVertex(double x, double y, double maxDistance) {
		double minDistance = maxDistance * maxDistance;
        Object closest = null;
		while(true) {
		    try {
		        for (Iterator iter = layout.getGraph().getNodes().iterator();
		        iter.hasNext();
		        ) {
		            Object v = (Object) iter.next();
		            double p[] = layout.getCoordinates(v);
		            double dx = p[0] - x;
		            double dy = p[1] - y;
		            double dist = dx * dx + dy * dy;
		            if (dist < minDistance) {
		                minDistance = dist;
		                closest = v;
		            }
		        }
		        break;
		    } catch(ConcurrentModificationException cme) {}
		}
		return closest;
	}
	
	/**
	 * Gets the edge nearest to the location of the (x,y) location selected.
	 * Calls the longer form of the call.
	 */
	public RepastEdge getEdge(double x, double y) {
	    return getEdge(x, y, this.maxDistance);
	}

	/**
	 * Gets the edge nearest to the location of the (x,y) location selected,
	 * within a distance of <tt>maxDistance</tt>, Iterates through all
	 * visible edges and checks their distance from the click. Override this
	 * method to provide a more efficient implementation.
	 * 
	 * @param x
	 * @param y
	 * @param maxDistance temporarily overrides member maxDistance
	 * @return Edge closest to the click.
	 */
	public RepastEdge getEdge(double x, double y, double maxDistance) {
		double minDistance = maxDistance * maxDistance;
		RepastEdge closest = null;
		while(true) {
		    try {
		        for (Iterator iter = layout.getGraph().getEdges().iterator(); iter.hasNext();) {
		            RepastEdge e = (RepastEdge) iter.next();
		            // if anyone uses a hyperedge, this is too complex.
		            if (e.getSource()==null && e.getTarget()==null)
		                continue;
		           
		            Object v1 = (Object) e.getSource();
		            Object v2 = (Object) e.getTarget();
		            // Get coords
		            double[] p1 = layout.getCoordinates(v1);
		            double[] p2 = layout.getCoordinates(v2);
		            double x1 = p1[0];
		            double y1 = p1[1];
		            double x2 = p2[0];
		            double y2 = p2[1];
		            // Calculate location on line closest to (x,y)
		            // First, check that v1 and v2 are not coincident.
		            if (x1 == x2 && y1 == y2)
		                continue;
		            double b =
		                ((y - y1) * (y2 - y1) + (x - x1) * (x2 - x1))
		                / ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		            //
		            double distance2; // square of the distance
		            if (b <= 0)
		                distance2 = (x - x1) * (x - x1) + (y - y1) * (y - y1);
		            else if (b >= 1)
		                distance2 = (x - x2) * (x - x2) + (y - y2) * (y - y2);
		            else {
		                double x3 = x1 + b * (x2 - x1);
		                double y3 = y1 + b * (y2 - y1);
		                distance2 = (x - x3) * (x - x3) + (y - y3) * (y - y3);
		            }
		            
		            if (distance2 < minDistance) {
		                minDistance = distance2;
		                closest = e;
		            }
		        }
		        break;
		    } catch(ConcurrentModificationException cme) {}
		}
		return closest;
	}

    public void setLayout(JungNetworkLayout l)
    {
        this.layout = l;
    }
}
