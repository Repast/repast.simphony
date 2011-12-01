package repast.simphony.context.space.graph;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import simphony.util.messages.MessageCenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Network generator that loads a network from a file.
 * This will create the correct number of nodes, add
 * those to a context and then create the correct topology.
 *
 * @author Nick Collier
 */
public abstract class MatrixNetworkLoader<T> implements NetworkGenerator {

  private static MessageCenter msg = MessageCenter.getMessageCenter(MatrixNetworkLoader.class);

  private Context context;
  private InputStream stream;
  private NodeCreator<T> creator;
  private int matrixIndex;

  public MatrixNetworkLoader(Context context, InputStream stream, Class agentClass, int matrixIndex) {
    this(context, stream, new DefaultNodeCreator(agentClass), matrixIndex);

  }

  public MatrixNetworkLoader(Context context, InputStream stream, NodeCreator<T> nodeCreator, int matrixIndex) {
    this.context = context;
    this.stream = stream;
    this.creator = nodeCreator;
    this.matrixIndex = matrixIndex;
  }

  protected abstract NetworkMatrixReader getMatrixReader(InputStream stream) throws IOException;


  /**
   * Creates edges using the nodes in the specified network. The
   * semantics of edge creation is determined by implementing classes.
   *
   * @param network a network containing nodes
   * @return the generated network
   */
  public Network createNetwork(Network network) {
    NetworkMatrixReader reader = null;
    try {
      reader = getMatrixReader(stream);
      List<AdjacencyMatrix> matrices = reader.getMatrices();
      AdjacencyMatrix matrix = matrices.get(matrixIndex);
      Map<Integer, T> nodeMap = createNodes(matrix);

      for (int i = 0; i < matrix.rows(); i++) {
        T source = nodeMap.get(i);
        for (int j = 0; j < matrix.rows(); j++) {
          double val = matrix.get(i, j);
          if (val != 0) {
            T target = nodeMap.get(j);
            network.addEdge(source, target, val);
          }
        }
      }
    } catch (IOException ex) {
      msg.error("Error while reading network data", ex);
    } finally {
      if (reader != null) reader.close();
    }


    return network;
  }

  private Map<Integer, T> createNodes(AdjacencyMatrix matrix) {
    List<String> nodeLabels = matrix.getLabels();
    if (nodeLabels.size() > 0 && nodeLabels.size() != matrix.rows()) {
      msg.error("Number of matrix labels not equal to number of matrix rows", new IllegalArgumentException());
      return null;
    }


    Map<Integer, T> map = new HashMap<Integer, T>();
    if (nodeLabels.size() > 0) {
      int i = 0;
      for (String label : nodeLabels) {
        T node = creator.createNode(label);
        context.add(node);
        map.put(i++, node);
      }
    } else {
      for (int i = 0; i < matrix.rows(); i++) {
        T node = creator.createNode(String.valueOf(i));
        context.add(node);
        map.put(i, node);
      }
    }
    return map;
  }
}
