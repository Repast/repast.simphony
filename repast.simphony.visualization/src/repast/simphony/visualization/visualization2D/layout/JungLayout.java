package repast.simphony.visualization.visualization2D.layout;

import java.awt.Dimension;

import javax.vecmath.Point3f;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.AbstractNetworkLayout;
import repast.simphony.visualization.Box;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class JungLayout<T> extends AbstractNetworkLayout<T>{

	protected AbstractLayout<T, RepastEdge<T>> layout;
	protected Network<T> network;
	
	public JungLayout(){
		super();	
	}
	
	@Override
	public void setProjection(Network<T> projection) {
		this.baseGraph = projection;
		
		network = this.baseGraph;
			  
	  setLayout();
	}
	
	@Override
	public void update(){
		if (network!=null)
			setProjection(network);	
	}
	
  //override by subclass
	protected void setLayout(){
	}
	
	public float[] getLocation(T obj) {
		float[] location = new float[2];

		location[0] = (float)layout.getX(obj);
		location[1] = (float)layout.getY(obj);

		return location;
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    Dimension dim = layout.getSize();
    return new Box(new Point3f(0, 0, 0), new Point3f((float)dim.getWidth(), (float)dim.getHeight(), 0));
  }
	
	
}