package repast.simphony.scenario.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import simphony.util.messages.MessageCenter;

/**
 * Encapsulates the context heirarchy.
 * 
 * @author Nick Collier
 */
public class ContextData extends AttributeContainer {
  
  private static final MessageCenter msg = MessageCenter.getMessageCenter(ContextData.class);
  
  private List<ProjectionData> projections = new ArrayList<ProjectionData>();
  private List<AgentData> agents = new ArrayList<AgentData>();
  private List<ContextData> children = new ArrayList<ContextData>();
  private Classpath classpath;
  private String contextClass;
  
  public ContextData(String id, Classpath classpath) {
    this(id, null, classpath);
  }
  
  public ContextData(String id, String contextClassName, Classpath classpath) {
    super(id);
    this.classpath = classpath;
    contextClass = contextClassName;
  }
  
  /**
   * Gets the name of the class to use a Context, or null
   * if the default should be used.
   * 
   * @return the name of the class to use a Context, or null
   * if the default should be used.
   */
  public String getContextClassName() {
    return contextClass;
  }

  public ProjectionData addProjection(String id, String type) {
    ProjectionData proj = new ProjectionData(id, type);
    projections.add(proj);
    return proj;
  }
  
  public void addSubContext(ContextData child) {
    children.add(child);
  }
  
  /**
   * Gets the classpath associated with this ContextData heirarchy.
   * 
   * @return the classpath associated with this ContextData heirarchy.
   */
  public Classpath getClasspath() {
    return classpath;
  }
  
  /**
   * Finds and returns the ContextData with the specified id.
   * This attempts to match against this context and all its
   * decendents.
   * 
   * @param contextId the id of ContextData to find.
   * @return the ContextData with the specified id.
   */
  public ContextData find(String contextId) {
    if (contextId.equals(this.id)) return this;
    for (ContextData child : children) {
      ContextData data = child.find(contextId);
      if (data != null) return data;
    }
    
    return null;
    
  }
  
  /**
   * Adds the class name of an agent contained by this context to 
   * this ContextData.
   * 
   * @param agentClass the agent class to add
   */
  public void addAgent(String agentClass) {
    AgentData agentData = new AgentData(agentClass);
    agents.add(agentData);
    
  }
  
  /**
   * Gets a list of the agent classes specified in this
   * ContextData and optionally those is all its decendent subclasses
   * as well.
   * @param includeSubContexts if true then agents classes specified in
   * sub contexts will be included.
   * 
   * @return a list of the agent classes.
   */
  public List<Class<?>> getAgentClasses(boolean includeSubContexts) {
    List<AgentData> allAgents = getAgentData(includeSubContexts);
    
    List<Class<?>> classes = new ArrayList<Class<?>>();
    ClassLoader loader = this.getClass().getClassLoader();
    try {
      for (AgentData agent : allAgents) {	
	classes.add(Class.forName(agent.getClassName(), false, loader));
      }
    } catch (ClassNotFoundException ex) {
      msg.error("Error while finding agent classes", ex);
    }
    
    return classes;
  }
  
  /**
   * Gets a list of the agent class names specified in this
   * ContextData and optionally those is all its decendent subclasses
   * as well.
   * @param includeSubContexts if true then agents class names specified in
   * sub contexts will be included.
   * 
   * @return a list of the agent class names.
   */
  public List<AgentData> getAgentData(boolean includeSubContexts) {
    Set<AgentData> allAgents = new HashSet<AgentData>();
    gatherAgents(allAgents, includeSubContexts);
    for (Iterator<AgentData> it = allAgents.iterator(); it.hasNext(); ){
    	String simpleName = it.next().getShortName();
        if ((simpleName.contains("$") && simpleName.contains("closure"))){
            it.remove();
        }
    }
    return new ArrayList<AgentData>(allAgents);
  }
  
  private void gatherAgents(Set<AgentData> agents, boolean includeSubContexts) {
    agents.addAll(this.agents);
    if (includeSubContexts) {
      for (ContextData child : children) {
	child.gatherAgents(agents, includeSubContexts);
      }
    }
  }
  
  /**
   * Gets a list which includes this ContextData and all of its
   * decendents.
   * 
   * @return a list which includes this ContextData and all of its
   * decendents.
   */
  public List<ContextData> getAllContexts() {
    List<ContextData> data = new ArrayList<ContextData>();
    gatherContexts(data);
    return data;
  }
  
  private void gatherContexts(List<ContextData> data) {
    data.add(this);
    for (ContextData child : children) {
      child.gatherContexts(data);
    }
  }
 
  /**
   * Gets an iterable over the sub-context data of this
   * ContextData.
   * 
   * @return an iterable over the sub-context data of this
   * ContextData.
   */
  public Iterable<ContextData> subContexts() {
    return children;
  }
  
  public int getAgentCount() {
    return agents.size();
  }
  
  public AgentData getAgentData(int index) {
    return agents.get(index);
  }
  
  public int getSubContextCount() {
    return children.size();
  }
  
  public int getProjectionCount(){
    return projections.size();
  }
  
  public ContextData getSubContext(int index) {
    return children.get(index);
  }
  
  public ProjectionData getProjection(int index) {
    return projections.get(index);
  }
  
  /**
   * Gets an iterable over all the ProjectionData-s contained
   * by this ContextData.
   * 
   * @return an iterable over all the ProjectionData-s contained
   * by this ContextData.
   */
  public Iterable<ProjectionData> projections() {
    return projections;
  }
}
