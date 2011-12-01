package repast.simphony.dataLoader.engine;

import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactory;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.DefaultEdgeCreator;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.EdgeCreatorFactory;
import repast.simphony.space.graph.RepastEdge;
import simphony.util.messages.MessageCenter;

/**
 * Builds a Network based on SNetwork data.
 * 
 * @author Nick Collier
 */
public class NetworkProjectionBuilder implements ProjectionBuilderFactory {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(NetworkProjectionBuilder.class);

  private static class NetworkBuilder implements ContextBuilder {

    private boolean isDirected;
    private String name;
    private EdgeCreator creator;


    NetworkBuilder(String name, boolean isDirected, EdgeCreator creator) {
      this.name = name;
      this.isDirected = isDirected;
      this.creator = creator;
    }


    public Context build(Context context) {
      NetworkFactory fac = NetworkFactoryFinder.createNetworkFactory(new HashMap<String, Object>());
      fac.createNetwork(name, context, isDirected, creator);
      return context;
    }
  }


  /**
   * Gets a ContextBuilder to build the specified
   * Projection.
   *
   * @param proj the type of Projection to build
   * @return a ContextBuilder to build the specified
   *         Projection.
   */
  public ContextBuilder getBuilder(ProjectionData proj) {
    EdgeCreator creator = new DefaultEdgeCreator();
    String edgeClass = null;
    boolean isDirected = false;
    for (Attribute attrib : proj.attributes()) {
      String id = attrib.getId();
      if (id.equalsIgnoreCase(AutoBuilderConstants.EDGE_CLASS_ID)) {
        edgeClass = attrib.getValue();
      }
      
      if (id.equalsIgnoreCase(AutoBuilderConstants.IS_DIRECTED_ID)) {
        isDirected = Boolean.parseBoolean(attrib.getValue());
      }
    }
    if (edgeClass != null) {
      EdgeCreatorFactory fac = new EdgeCreatorFactory();
      try {
        creator = fac.createEdgeCreator(edgeClass);
      } catch (Exception ex) {
        msg.error("Error while creating network '" + proj.getId() + "'", ex);
      }
    }
    return new NetworkBuilder(proj.getId(), isDirected, creator);
  }
}