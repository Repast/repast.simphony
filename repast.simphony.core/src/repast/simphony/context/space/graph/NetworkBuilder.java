package repast.simphony.context.space.graph;

import repast.simphony.context.Context;
import repast.simphony.space.graph.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A builder used to build networks.
 *
 * @author Nick Collier
 */
public class NetworkBuilder<T> {

  private Context<T> context;
  private boolean isDirected;
  private String name;
  private EdgeCreator edgeCreator = new DefaultEdgeCreator<T>();
  private NetworkGenerator<T> loader;
  private NetworkGenerator<T> generator;

  public NetworkBuilder(String networkName, Context<T> context, boolean isDirected) {
    this.name = networkName;
    this.context = context;
    this.isDirected = isDirected;
  }

  /**
   * Sets a generator to use to create the network topology.
   *
   * @param generator the generator to use to create the network
   *                  topology
   * @return this NetworkBuilder
   */
  public NetworkBuilder setGenerator(NetworkGenerator<T> generator) {
    this.generator = generator;
    return this;
  }

  /**
   * Sets the class that will be used to create edges for the
   * created network.
   *
   * @param edgeCreator the object to use to create edges for the created network
   * @return this NetworkBuilder
   */
  public NetworkBuilder setEdgeCreator(EdgeCreator edgeCreator) {
    this.edgeCreator = edgeCreator;
    return this;
  }

  /**
   * Sets this NetworkBuilder to create the network from a file. This
   * will use the first network found in the file.
   *
   * @param fileName    the name of the file to load from
   * @param format      the format of the file
   * @param nodeCreator a node creator that will be used to create the agents / nodes
   * @return this NetworkBuilder
   * @throws IOException if there is a file related error
   */
  public NetworkBuilder load(String fileName, NetworkFileFormat format, NodeCreator nodeCreator) throws IOException {
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
    loader = format.createLoader(context, in, nodeCreator, 0);
    return this;
  }

  /**
   * Sets this NetworkBuilder to create the network from a file. This
   * will use the first network found in the file.
   *
   * @param fileName    the name of the file to load from
   * @param format      the format of the file
   * @param nodeCreator a node creator that will be used to create the agents / nodes
   * @param matrixIndex the index of the matrix in the file to load. Starts with 0.
   * @return this NetworkBuilder
   * @throws IOException if there is a file related error
   */
  public NetworkBuilder load(String fileName, NetworkFileFormat format, NodeCreator nodeCreator, int matrixIndex) throws IOException {
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
    loader = format.createLoader(context, in, nodeCreator, matrixIndex);
    return this;
  }

  /**
   * Builds the network using all the previously set properties.
   *
   * @return a network built using all the previously set properties.
   */
  public Network<T> buildNetwork() {
    Network<T> net;
    if (isDirected) {
      net = new ContextJungNetwork<T>(new DirectedJungNetwork<T>(name, edgeCreator), context);
    } else {
      net = new ContextJungNetwork<T>(new UndirectedJungNetwork<T>(name, edgeCreator), context);
    }
    context.addProjection(net);
    if (loader != null) net = loader.createNetwork(net);
    if (generator != null) net = generator.createNetwork(net);
    return net;
  }
}
