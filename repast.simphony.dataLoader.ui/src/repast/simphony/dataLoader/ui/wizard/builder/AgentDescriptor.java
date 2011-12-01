/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.schedule.Descriptor;

public class AgentDescriptor implements Descriptor {
	private Class agentClass;
	
	private String name;
	
	private Map<String, Object> properties;

	public AgentDescriptor(Class<?> clazz) {
		this.agentClass = clazz;
		
		this.properties = new HashMap<String, Object>();
	}

	public Class getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(Class agentClass) {
		this.agentClass = agentClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}
}
