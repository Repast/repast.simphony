package repast.simphony.engine.environment;

import java.util.HashMap;

import simphony.util.messages.MessageCenter;

public class ProjectionRegistry {
	private static MessageCenter msg = MessageCenter.getMessageCenter(ProjectionRegistry.class);
	
	protected static HashMap<String,ProjectionRegistryData> registryMap = new HashMap<String,ProjectionRegistryData>();
	
	// TODO Projections: use a singleton.
	
	public static Iterable<ProjectionRegistryData> getRegistryData() {
		return registryMap.values();
	}

	public static void addRegistryData(ProjectionRegistryData data) {
		
		if (registryMap.get(data.getTypeName()) != null){
			msg.warn("Duplicate projection type" + data.getTypeName());
		}
		else{
			registryMap.put(data.getTypeName(), data);
		}
	}
	
	public static ProjectionRegistryData getDataFor(String projectionTypeName){
		return registryMap.get(projectionTypeName);
	}
	
}
