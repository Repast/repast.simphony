package repast.simphony.visualization.engine;

import java.util.HashMap;

import simphony.util.messages.MessageCenter;

public class VisualizationRegistry {
	private static MessageCenter msg = MessageCenter.getMessageCenter(VisualizationRegistry.class);
	
	protected static HashMap<String,VisualizationRegistryData> registryMap = new HashMap<String,VisualizationRegistryData>();
	
	public static Iterable<VisualizationRegistryData> getRegistryData() {
		return registryMap.values();
	}

	public static void addRegistryData(VisualizationRegistryData data) {
		
		if (registryMap.get(data.getVisualizationType()) != null){
			msg.warn("Duplicate visualization type" + data.getVisualizationType());
		}
		else{
			registryMap.put(data.getVisualizationType(), data);
		}
	}
	
	public static VisualizationRegistryData getDataFor(String visualizationeType){
		return registryMap.get(visualizationeType);
	}
	
}
