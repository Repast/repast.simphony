package repast.simphony.ws.gis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.ws.DisplayServer;
import repast.simphony.ws.OutgoingMessageSocket;

public class DisplayServerGIS extends DisplayServer {

	protected Map<Class<?>, StyledLayerGIS> classStyleMap = new HashMap<>();
	protected Map<Network<?>, StyledNetLayerGIS> networkStyleMap = new HashMap<>();
	private int layeridx = 0;
	protected Geography<?> geography;
	
	/**
	 * Updates network layers when network edge add/remove events occur.
	 *
	 */
	private class NetworkLayerListener implements ProjectionListener {
		
		private StyledNetLayerGIS layer;
		
		public NetworkLayerListener(StyledNetLayerGIS layer) {
			this.layer = layer;
		}
		
		@Override
		public void projectionEventOccurred(ProjectionEvent evt) {
			if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
				layer.addObject((RepastEdge<?>) evt.getSubject(), idCounter);
				++idCounter;
			} 
			else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
				layer.removeObject((RepastEdge<?>) evt.getSubject());
			}
		}
	}
	
	public DisplayServerGIS(String outgoingAddr, DisplayData<?> data, DisplayDescriptor descriptor,
			Layout layout, int id) {
		super(outgoingAddr, data, descriptor, layout, id);
	}

	@Override
	public void init() {
		LOG.info("DisplayServerGIS.init()");
		outgoing = new OutgoingMessageSocket(outgoingAddr);

		// TODO Move some of this shared code with DisplayServer2D into the abstract parent
		
		StringBuilder builder = new StringBuilder("{\"id\": \"display_init\", \"name\":\"");
		builder.append(descriptor.getName());
		builder.append("\", \"display_id\" : ");
    builder.append(id);
		builder.append(", \"type\":\"");
		builder.append(descriptor.getDisplayType());

		// TODO refactor following into function to reduce repeating
		
		// TODO set the layer type: mark (and wkt shape), line, or polygon
		
		builder.append("\", \"agent_layers\": [");
		boolean first = true;
		for (StyledLayerGIS layer : classStyleMap.values()) {
			if (!first) {
				builder.append(",");
			}
			builder.append("{\"name\":\"").append(layer.getName()).append("\"");
			builder.append(", \"layer_id\" : ").append(layer.getLayerId());
    	builder.append(", \"symbol\":\"").append(layer.getSymbolType()).append("\"");
      
      builder.append("}");
			first = false;
		}
		builder.append("]");  // end agent layers
		
		builder.append(", \"net_layers\": [");
		first = true;
		for (StyledNetLayerGIS layer : networkStyleMap.values()) {
			if (!first) {
				builder.append(",");
			}
			builder.append("{\"name\":\"").append(layer.getName()).append("\"");
			builder.append(", \"layer_id\" : ").append(layer.getLayerId());
    	builder.append(", \"symbol\":\"").append("network").append("\"");
      
      builder.append("}");
			first = false;

		}
		builder.append("]");  // end net layers
		builder.append("}");  // end json
		
		outgoing.send(builder.toString());

		// TODO GIS This seems brittle since there technically could be multiple Geography
		for (Projection<?> proj : displayData.getProjections()) {
			if (proj instanceof Geography) {
				geography = (Geography<?>) proj;
			}
		}

		geography.addProjectionListener(this);  // Listen for agent add/remove from geog

		for (StyledLayerGIS layer : classStyleMap.values()) {
			layer.setGeography(geography);
		}
		
//		for (Map.Entry<String,String> entry : gfg.entrySet())  
		
		for (Map.Entry<Network<?>, StyledNetLayerGIS> entry : networkStyleMap.entrySet()) {
			StyledNetLayerGIS layer = entry.getValue();
			Network<?> net = entry.getKey();
					
			layer.setGeography(geography);
			
			for (RepastEdge<?> edge : net.getEdges()) {	
				layer.addObject(edge, idCounter);
				++idCounter;
			}
		}
		
		// Add all of the initial objects 
		for (Object obj : displayData.objects()) {
			addObject(obj);
		}

		
		// TODO implement needed following for WD display
		
//		// TODO GIS probable better to hand the geography to the layout ?
//		for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
//			layer.setGeography(geog);
//		}
//		for (AbstractRenderableLayer<?,?> layer : networkLayerMap.values()) {
//			layer.setGeography(geog);
//		}
//		for (CoverageLayer layer : coverageLayerMap.values()) {
//			layer.setGeography(geog);
//		}
//		
//		// TODO GIS should the axis order be part of the style?
//		List<RenderableLayer> staticLayers = new ArrayList<RenderableLayer>();
//		boolean forceLonLatOrder = true;
//		for (String fileName : initData.getStaticCoverageMap().keySet()) {
//			RenderableLayer layer = createStaticRasterLayer(fileName, forceLonLatOrder);
//
//			if (layer != null)
//				staticLayers.add(layer);
//		}
//		
//		// First collect all layers, then set the layer order using the order number 
//		// Ordered map of all renderable layers
//	  TreeMap<Integer, Layer> orderedLayerMap =	new TreeMap<Integer, Layer>();
//    
//	  // Unsorted layers have no specified layer order
//	  List<Layer> unsortedLayers = new ArrayList<Layer>();
//    List<Layer> layersToSort = new ArrayList<Layer>();
//   
//    layersToSort.addAll(classStyleMap.values());
//    layersToSort.addAll(coverageLayerMap.values());
//    layersToSort.addAll(networkLayerMap.values());
//    layersToSort.addAll(globeLayers);
//    layersToSort.addAll(staticLayers);
//    
//  	for (Layer layer : layersToSort) {
//  		Integer order = initData.getLayerOrders().get(layer.getValue(LAYER_ID_KEY));
//  		
//  		// If the order is non null and doesnt already exist, add the layer
//  		if (order != null && !orderedLayerMap.containsKey(order)) {	
//  			orderedLayerMap.put(order, layer);
//  		}
//  		// Otherwise save in the ordered layer list
//  		else {
//  			unsortedLayers.add(layer);
//  		}
//  	}
//  	
//  	for (Layer layer : orderedLayerMap.values()) {
//			model.getLayers().add(layer);
//  	}
//  	
//  	// Put all unsorted layers at the back (index 0 - auto index shift)
//  	for (Layer layer : unsortedLayers) {
//			model.getLayers().add(0,layer);
//  	}
//   		
//		// Create a background layer with color from descriptor
//		createBackgroundLayer();
//		
//		boundingSector = calculateBoundingSector();
//		
//		
//		resetHomeView();
//		
		
		
		
		doUpdate();
	}

	public void registerStyle(Class<?> clazz, ServerStyleGIS style) {
		// style.init(canvas.getShapeFactory());
		StyledLayerGIS layer = classStyleMap.get(clazz);
		if (layer == null) {
			layer = new StyledLayerGIS(style, clazz.getName(), layeridx);
			 ++layeridx;
			classStyleMap.put(clazz, layer);
		} else {
			layer.setStyle(style);
		}
	}

	public void registerNetworkStyle(Network<?> network, ServerNetStyleGIS style) {
		StyledNetLayerGIS layer = networkStyleMap.get(network);
		if (layer == null) {
			layer = new StyledNetLayerGIS(style, network.getName(), layeridx);
			++layeridx;
			
			network.addProjectionListener(new NetworkLayerListener(layer));
			
			networkStyleMap.put(network, layer);
		} else {
			layer.setStyle(style);
		}
	}

	protected StyledLayerGIS findLayer(Object obj) {
		Class<?> objClass = obj.getClass();
		StyledLayerGIS layer = classStyleMap.get(objClass);
		if (layer == null) {
			// find a parent class or interface
			for (Class<?> clazz : classStyleMap.keySet()) {
				if (clazz.isAssignableFrom(objClass)) {
					layer = classStyleMap.get(clazz);
					break;
				}
			}
		}
		return layer;
	}

	@Override
	protected void addObject(Object obj) {
		StyledLayerGIS layer = findLayer(obj);

		if (layer != null) {
			layer.addObject(obj, idCounter);
			++idCounter;
		}
	}

	@Override
	protected void removeObject(Object obj) {
		StyledLayerGIS layer = findLayer(obj);

		if (layer != null) {
			layer.removeObject(obj);
		}
	}

	@Override
	protected void moveObject(Object obj) {
		
		// TODO add to moved collections (only keep last moved in case of several move events)
		
		StyledLayerGIS layer = findLayer(obj);

		if (layer != null) {
			layer.moveObject(obj);
		}
		
		// TODO Moved agent updates also need to trigger network layer updates
		
	}

	@Override
	public void doUpdate() {
		
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		StringBuilder builder = new StringBuilder("{\"id\": \"display_update\", \"name\":\"");
		builder.append(descriptor.getName());
		
	  builder.append("\", \"display_id\" : ");
    builder.append(id);
    
    builder.append(", \"tick\": ");
    builder.append(tick);
    
    builder.append(", \"agent_layers\": [");
		
    boolean first = true;
		for (StyledLayerGIS layer : classStyleMap.values()) {
			if (!first) {
				builder.append(",");
			}
			layer.update(builder, layout);
			first = false;
		}
		builder.append("]");
		
		builder.append(", \"net_layers\": [");
		first = true;
		for (StyledNetLayerGIS layer : networkStyleMap.values()) {
			if (!first) {
				builder.append(",");
			}
			layer.update(builder, layout);
			first = false;

		}
		builder.append("]");
		builder.append("}");
		outgoing.send(builder.toString());
	}

    @Override
    public List<Pair<Integer,Object>> getAgents(List<Integer> ids) {
        // TODO Auto-generated method stub
        return new ArrayList<Pair<Integer, Object>>();
    }
}