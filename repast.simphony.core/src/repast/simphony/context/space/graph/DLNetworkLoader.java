package repast.simphony.context.space.graph;

import repast.simphony.context.Context;

import java.io.InputStream;

/**
 * Loads a Network from a ucinet dl format file.
 *
 * @author Nick Collier
 */
public class DLNetworkLoader extends MatrixNetworkLoader {

  public DLNetworkLoader(Context context, InputStream stream, Class agentClass, int matrixIndex) {
    super(context, stream, agentClass, matrixIndex);
  }

  public DLNetworkLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
    super(context, stream, nodeCreator, matrixIndex);
  }

  protected NetworkMatrixReader getMatrixReader(InputStream stream) {
    return new DLMatrixReader(stream);
  }
}
