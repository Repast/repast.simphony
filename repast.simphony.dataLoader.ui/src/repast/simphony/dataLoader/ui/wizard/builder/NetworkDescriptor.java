/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.Descriptor;

public class NetworkDescriptor implements Descriptor {

	private boolean directed;

	private String name;

	private List<RelationshipDescriptor> relationships;

	public NetworkDescriptor(String name) {
		this();
		this.name = name;
	}

	public NetworkDescriptor() {
		relationships = new ArrayList<RelationshipDescriptor>();
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RelationshipDescriptor> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<RelationshipDescriptor> relationships) {
		this.relationships = relationships;
	}

	public void addRelationship(AgentDescriptor source, AgentDescriptor target, double strength) {
		relationships.add(new RelationshipDescriptor(source, target, strength));
	}

	public void addRelationship(RelationshipDescriptor descriptor) {
		this.relationships.add(descriptor);
	}

	public void removeRelationships(AgentDescriptor descriptor) {
		for (RelationshipDescriptor relationship : new ArrayList<RelationshipDescriptor>(
				relationships)) {
			if (relationship.getSource().equals(descriptor)
					|| relationship.getTarget().equals(descriptor)) {
				relationships.remove(relationship);
			}
		}
	}
}
