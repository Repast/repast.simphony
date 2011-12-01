package repast.simphony.visualization.visualization3D.layout;

import repast.simphony.space.graph.RepastEdge;


/*
 * Port from JUNG GraphElementAccessor.java v. 1.7.4
 * See http://jung.sourceforge.net/ for futher information
 */


/**
 * Interface for coordinate-based selection of graph components.
 * See JungGraphElementAccessor for further details
 * @author M. Altaweel
 */

public interface JungGraphElementAccessor 
{
    /**
     * Returns a <code>Vertex</code> which is associated with the 
     * location <code>(x,y)</code>.  This is typically determined
     * with respect to the <code>Vertex</code>'s location as specified
     * by a <code>Layout</code>.
     */
    Object getVertex(double x, double y);

    /**
     * Returns an <code>Edge</code> which is associated with the 
     * location <code>(x,y)</code>.  This is typically determined
     * with respect to the <code>Edge</code>'s location as specified
     * by a Layout.
     */
    RepastEdge getEdge(double x, double y);
    
    /**
     * Sets the <code>Network</code> that is used to specify the locations
     * of vertices and edges in this instance to <code>layout</code>.
     */
    void setLayout(JungNetworkLayout layout); 
}