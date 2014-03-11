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
		
		if (registryMap.get(data.getVisualizationName()) != null){
			msg.warn("Duplicate visualization type" + data.getVisualizationName());
		}
		else{
			registryMap.put(data.getVisualizationName(), data);
		}
	}
	
	public static VisualizationRegistryData getDataFor(String visualizationeName){
		return registryMap.get(visualizationeName);
	}
	
}
