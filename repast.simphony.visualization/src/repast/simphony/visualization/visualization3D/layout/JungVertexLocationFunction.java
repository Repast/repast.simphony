package repast.simphony.visualization.visualization3D.layout;

/*
 * Port from JUNG VertexLocationFunction.java v. 1.7.4
 * See http://jung.sourceforge.net/ for futher information
 */

import java.util.Iterator;


/**
 * An interface for classes that return a location for
 * an <code>Node</code>.
 * 
 */
public interface JungVertexLocationFunction
{
    public double[] getLocation(Object o);
    public double[] getLocation(Object o,boolean status);
    public Iterator getVertexIterator();
}

