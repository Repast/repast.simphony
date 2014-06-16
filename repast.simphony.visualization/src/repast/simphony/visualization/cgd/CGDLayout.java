package repast.simphony.visualization.cgd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.space.graph.DirectedJungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.AbstractNetworkLayout;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.visualization.cgd.graph.CGDGraph;
import repast.simphony.visualization.cgd.graph.CGDNode;
import simphony.util.messages.MessageCenter;



public class CGDLayout<T> extends AbstractNetworkLayout<T> implements ProjectionListener {
	
	private VisualizationProperties properties;

	protected Network network;

	private CGDGraph cgd;

	private HashMap data;

	public CGDLayout() {
		super();
	}
	
	public CGDLayout(Network network) {
		this.network=network;
	}
	//just a static update of the current network using CGD layout
	
	public void update() {
		
		if (network!=null)
			setProjection(network);	
	}

	public float[] getLocation(T obj) {
		Iterator ic = data.entrySet().iterator();
		
		float[] results = new float[2];
		while(ic.hasNext()) {
			Map.Entry e = (Map.Entry)ic.next();
			
			if(e.getKey()==obj){
			    CGDNode node = (CGDNode) e.getValue();
			    float x = (float) node.getX();
			    float y = (float) node.getY();
			    results[0]=x;
			    results[1]=y;
			    break;
			}
		}
		return results;
	}

	public void setLayoutProperties(VisualizationProperties props) {
		this.properties = props;
	}

	public VisualizationProperties getLayoutProperties() {
		return properties;
	}
	
	public void setProjection(Network projection) {
		if(projection != null && !projection.isDirected()){
			MessageCenter.getMessageCenter(CGDLayout.class).error( 
					"Network is not a tree type network",new Throwable());
		}
		this.network=projection;
		CGDProcessor cgdP = new CGDProcessor();
		cgdP.processGraph(projection);
		this.data=cgdP.getObjectData();
		this.cgd=cgdP.graph;
	}

	public void projectionEventOccurred(ProjectionEvent evt) {
		CGDProcessor cgdP=new CGDProcessor();
		cgdP.processGraph(network);
		this.cgd=cgdP.graph;
		this.data=cgdP.getObjectData();
		
	}

  @Override
  public Box getBoundingBox() {
    return new Box();
  }
	
	
	


}
