package repast.simphony.xml;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

/**
 * XStream converter for ContextJungNetworks, the default network type
 * in simphony.
 *
 * @author Nick Collier
 */
public class NetworkConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(ContextJungNetwork.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    Network network = (Network) o;
    writeString("name", network.getName(), writer);
//    writeString("edgeCreator", network.getEdgeCreator().getClass().getName(), writer);
    writeObject("edgeCreator", network.getEdgeCreator(), writer, mContext);
    writeString("isDirected", String.valueOf(network.isDirected()), writer);
    writeString("edgeCount", String.valueOf(network.getDegree()), writer);
    for (RepastEdge edge : (Iterable<RepastEdge>) network.getEdges()) {
      writeObject("edge", edge, writer, mContext);
    }

  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {
      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);
//      String edgeCreator = readNextString(reader);
      EdgeCreator creator = (EdgeCreator)readNextObject(umContext.currentObject(), reader, umContext);
      boolean isDirected = Boolean.parseBoolean(readNextString(reader));
//      EdgeCreator creator = (EdgeCreator) Class.forName(edgeCreator).newInstance();
      Network network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(name, context,
              isDirected, creator);

      int edgeCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < edgeCount; i++) {
        RepastEdge edge = (RepastEdge) readNextObject(network, reader, umContext);
        network.addEdge(edge);
      }

      return network;
    } catch (Exception ex) {
      throw new ConversionException("Error reading EdgeCreator", ex);
    }

  }
}
