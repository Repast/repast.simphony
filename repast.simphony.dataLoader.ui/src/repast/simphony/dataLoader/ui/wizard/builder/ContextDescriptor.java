package repast.simphony.dataLoader.ui.wizard.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;

public class ContextDescriptor {
  private Map<Object, NetworkDescriptor> networkDescriptors;

  private List<AgentDescriptor> agentDescriptors;

  // this should be a property of the network descriptors
  // but because the gui context buidler doesn't really deal with
  // multiple nets anyway -- its here for now.
  private boolean directed = true;

  private String name;

  private List<Class<?>> agentClasses;

  // private SProjection[] projectionInfos;

  public ContextDescriptor(ContextData contextData) {
    this.name = contextData.getId();
    this.agentClasses = getAgentClasses(contextData);
    // this.projectionInfos = contextData.getProjections();

    agentDescriptors = new ArrayList<AgentDescriptor>();
    networkDescriptors = new HashMap<Object, NetworkDescriptor>();
    for (ProjectionData data : contextData.projections()) {
      if (data.getType() == ProjectionType.NETWORK) {
	String name = data.getId();
	networkDescriptors.put(name, new NetworkDescriptor(name));
      }
    }
  }

  public String getName() {
    return name;
  }

  public Object getParentID() {
    return name;
  }

  public int addAgentDescriptor(AgentDescriptor desc) {
    this.agentDescriptors.add(desc);
    return agentDescriptors.size() - 1;
  }

  public void addRelationship(Object networkId, AgentDescriptor source, AgentDescriptor target,
      double strength) {
    networkDescriptors.get(networkId).addRelationship(source, target, strength);
  }

  private List<Class<?>> getAgentClasses(ContextData context) {
    ArrayList<Class<?>> agentClasses = new ArrayList<Class<?>>();
    agentClasses.addAll(context.getAgentClasses(true));
    return agentClasses;
  }

  public List<Class<?>> getAgentClasses() {
    return agentClasses;
  }

  public Iterable<AgentDescriptor> getAgentDescriptors() {
    return agentDescriptors;
  }

  public Iterable<NetworkDescriptor> getNetworkDescriptors() {
    return networkDescriptors.values();
  }

  public Object getID() {
    return name;
  }

  public void setDirected(boolean directed) {
    this.directed = directed;
  }

  public boolean isDirected() {
    return directed;
  }

  public void removeAgentDescriptor(AgentDescriptor descriptor) {
    this.agentDescriptors.remove(descriptor);
    for (NetworkDescriptor netDescriptor : networkDescriptors.values()) {
      netDescriptor.removeRelationships(descriptor);
    }
  }

  // public SProjection[] getProjectionInfos() {
  // return projectionInfos;
  // }
}
