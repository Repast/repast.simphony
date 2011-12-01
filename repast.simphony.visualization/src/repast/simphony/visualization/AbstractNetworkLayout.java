package repast.simphony.visualization;

import java.util.Map;

import repast.simphony.space.graph.Network;

public abstract class AbstractNetworkLayout<T> implements Layout<T, Network<T>> {
	private VisualizationProperties properties;

	protected Network<T> baseGraph;
	protected String layoutName;
	protected Map<Object, double[]> locationData;
	
	public void update() {

	}

	public void setProjection(Network<T> projection) {
		this.baseGraph = projection;
	}

	public void setLayoutProperties(VisualizationProperties props) {
		this.properties = props;
	}

	public VisualizationProperties getLayoutProperties() {
		return properties;
	}
	
	public String getName() {
		return layoutName;
	}
	
	public Network<T> getGraph() {
		return baseGraph;
	}
	
	public Map<Object, double[]> getLocationData() {
		return locationData;
	}
}
