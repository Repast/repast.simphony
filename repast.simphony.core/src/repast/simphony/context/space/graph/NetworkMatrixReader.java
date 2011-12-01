package repast.simphony.context.space.graph;

import java.io.IOException;
import java.util.List;

/**
 * Interface for reading a network matrix. All the specific network
 * matrix readers implement this interface.
 *
 * @author Nick Collier
 */
public interface NetworkMatrixReader {

  /**
   * Returns a list of the read AdjacencyMatrices.
   *
   * @return a list of the read AdjacencyMatrices.
   * @throws IOException if there is an error while attempting to read the matrices
   */
  List<AdjacencyMatrix> getMatrices() throws IOException;


  /**
   * Closes the reader.
   */
  void close();
}
