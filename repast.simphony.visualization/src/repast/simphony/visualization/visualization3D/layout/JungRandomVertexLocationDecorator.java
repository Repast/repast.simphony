package repast.simphony.visualization.visualization3D.layout;

/*
 *
 * Port from JUNG RandomVertexLocationDecorator.java v. 1.7.4
 * See http://jung.sourceforge.net/ for futher information
 *
 */

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jogamp.vecmath.Point3d;

import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class JungRandomVertexLocationDecorator implements JungVertexLocationFunction
{
    RandomEngine rand;
    Map<Object,Object> v_locations = new HashMap<Object,Object>();
    Dimension dim;
    double z;
   
    public JungRandomVertexLocationDecorator(Dimension d) 
    {
        this.rand = new DRand((int)(new Date().getTime()));
        this.dim = d;
    }
    
    public JungRandomVertexLocationDecorator(Dimension d,boolean zValue) 
    {
        if(zValue){
        
        	this.rand = new DRand((int)(new Date().getTime()));
        	this.dim =(DimensionLocal) d;
        	
        }
    }
    
    public JungRandomVertexLocationDecorator(Dimension d, int seed)
    {
        this.rand = new DRand(seed);
        this.dim = d;
    }
    
    /**
     * Resets all vertex locations returned by <code>getLocation</code>
     * to new (random) locations.
     */
    public void reset()
    {
        v_locations.clear();
    }
    
    public double[] getLocation(Object o)
    {
        Point2D location = (Point2D)v_locations.get(o);
        if (location == null) {
            location = new Point2D.Double(rand.nextDouble() * dim.width, rand.nextDouble() * dim.height);
            v_locations.put(o,location);
        }
        double[] coords = {location.getX(),location.getY()};
        
        return coords;
    }
    
   
    
    public double[] getLocation (Object o , boolean threeD)
    {
        if(!threeD) return null;
        
    	Point3d location = (Point3d)v_locations.get(o);
        if (location == null) {
            location = new Point3d(rand.nextDouble() * dim.width, rand.nextDouble() * dim.height, rand.nextDouble()*
            		((DimensionLocal)dim).getZ());
            v_locations.put(o,location);
        }
        double[] coords = {location.x,location.y,location.z};
        
        return coords;
    }

    public Iterator getVertexIterator()
    {
        return v_locations.keySet().iterator();
    }
}
